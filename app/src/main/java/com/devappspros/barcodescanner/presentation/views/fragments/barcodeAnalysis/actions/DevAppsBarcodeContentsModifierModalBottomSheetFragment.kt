/*
 * Barcode Scanner
 * Copyright (C) 2021  Atharok
 *
 * This file is part of Barcode Scanner.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devappspros.barcodescanner.common.extensions.getDisplayName
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.utils.BARCODE_KEY
import com.devappspros.barcodescanner.common.utils.CODE_128_LENGTH
import com.devappspros.barcodescanner.common.utils.CODE_39_LENGTH
import com.devappspros.barcodescanner.common.utils.CODE_93_LENGTH
import com.devappspros.barcodescanner.common.utils.EAN_13_LENGTH
import com.devappspros.barcodescanner.common.utils.EAN_8_LENGTH
import com.devappspros.barcodescanner.common.utils.ITF_LENGTH
import com.devappspros.barcodescanner.common.utils.PDF_417_LENGTH
import com.devappspros.barcodescanner.common.utils.UPC_A_LENGTH
import com.devappspros.barcodescanner.common.utils.UPC_E_LENGTH
import com.devappspros.barcodescanner.databinding.FragmentBarcodeContentsModifierModalBottomSheetBinding
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeFormatChecker
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeAnalysisActivityDevApps
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.koin.android.ext.android.inject

class DevAppsBarcodeContentsModifierModalBottomSheetFragment : BottomSheetDialogFragment() {

    private val devAppsBarcodeFormatChecker: DevAppsBarcodeFormatChecker by inject()

    private var _binding: FragmentBarcodeContentsModifierModalBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeContentsModifierModalBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.serializable(BARCODE_KEY, Barcode::class.java)?.let { barcode: Barcode ->

            val format: BarcodeFormat = barcode.getBarcodeFormat()

            binding.fragmentBarcodeContentsModifierModalBottomSheetFormatTextView.text =
                format.getDisplayName(requireContext())

            when(format) {
                BarcodeFormat.AZTEC -> configureAztec(barcode)
                BarcodeFormat.CODABAR -> configureCodabar(barcode)
                BarcodeFormat.CODE_39 -> configureCode39(barcode)
                BarcodeFormat.CODE_93 -> configureCode93(barcode)
                BarcodeFormat.CODE_128 -> configureCode128(barcode)
                BarcodeFormat.DATA_MATRIX -> configureDataMatrix(barcode)
                BarcodeFormat.EAN_8 -> configureEAN8(barcode)
                BarcodeFormat.EAN_13 -> configureEAN13(barcode)
                BarcodeFormat.ITF -> configureITF(barcode)
                BarcodeFormat.MAXICODE -> configureMaxicode(barcode)
                BarcodeFormat.PDF_417 -> configurePDF417(barcode)
                BarcodeFormat.QR_CODE -> configureQrCode(barcode)
                BarcodeFormat.RSS_14 -> configureDefaultBarcode(barcode)
                BarcodeFormat.RSS_EXPANDED -> configureDefaultBarcode(barcode)
                BarcodeFormat.UPC_A -> configureUPCA(barcode)
                BarcodeFormat.UPC_E -> configureUPCE(barcode)
                BarcodeFormat.UPC_EAN_EXTENSION -> configureEAN13(barcode)
            }
            binding.fragmentBarcodeContentsModifierModalBottomSheetInputEditText.apply {
                this.setText(barcode.contents)
                this.requestFocus()
            }
        } ?: run { dismiss() }
    }


    private fun configureTextInputEditTextCode(length: Int, inputType: Int) {
        binding.fragmentBarcodeContentsModifierModalBottomSheetInputEditText.apply {
            this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
            this.inputType = inputType
        }
    }

    private fun configureErrorMessage(message: String) {
        binding.fragmentBarcodeContentsModifierModalBottomSheetErrorTextView.apply {
            this.text = message
            this.visibility = View.VISIBLE
        }
    }

    private fun configureModifyButton(barcode: Barcode, checkError: (contents: String) -> String?) {
        binding.fragmentBarcodeContentsModifierModalBottomSheetModifyButton.setOnClickListener {
            // Récupère le contenu du TextInputEditText
            val newBarcodeContents: String =
                binding.fragmentBarcodeContentsModifierModalBottomSheetInputEditText.text.toString()
            // On vérifie si le nouveau contenu est au bon format
            checkError(newBarcodeContents)?.let {
                // Si le format est incorrect, on affiche un message d'erreur
                configureErrorMessage(it)
            } ?: run {
                // Si il n'y a pas d'erreur on applique la modification avec le nouveau code-barres.
                (requireActivity() as? DevAppsBarcodeAnalysisActivityDevApps)?.updateBarcodeContents(barcode, newBarcodeContents)
                dismiss()
            }
        }
    }

    // ----

    private fun configureAztec(barcode: Barcode) {
        configureTextInputEditTextCode(Int.MAX_VALUE, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkBlankError(it)
        }
    }

    private fun configureCodabar(barcode: Barcode) {
        configureTextInputEditTextCode(Int.MAX_VALUE, InputType.TYPE_CLASS_TEXT)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkCodabarError(it)
        }
    }

    private fun configureCode39(barcode: Barcode) {
        configureTextInputEditTextCode(CODE_39_LENGTH, InputType.TYPE_CLASS_TEXT)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkCode39Error(it)
        }
    }

    private fun configureCode93(barcode: Barcode) {
        configureTextInputEditTextCode(CODE_93_LENGTH, InputType.TYPE_CLASS_TEXT)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkCode93Error(it)
        }
    }

    private fun configureCode128(barcode: Barcode) {
        configureTextInputEditTextCode(CODE_128_LENGTH, InputType.TYPE_CLASS_TEXT)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkCode128Error(it)
        }
    }

    private fun configureDataMatrix(barcode: Barcode) {
        configureTextInputEditTextCode(Int.MAX_VALUE, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkDataMatrixError(it)
        }
    }

    private fun configureEAN8(barcode: Barcode) {
        configureTextInputEditTextCode(EAN_8_LENGTH, InputType.TYPE_CLASS_NUMBER)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkEAN8Error(it)
        }
    }

    private fun configureEAN13(barcode: Barcode) {
        configureTextInputEditTextCode(EAN_13_LENGTH, InputType.TYPE_CLASS_NUMBER)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkEAN13Error(it)
        }
    }

    private fun configureITF(barcode: Barcode) {
        configureTextInputEditTextCode(ITF_LENGTH, InputType.TYPE_CLASS_NUMBER)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkITFError(it)
        }
    }

    private fun configureMaxicode(barcode: Barcode) {
        configureTextInputEditTextCode(150, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkBlankError(it)
        }
    }

    private fun configurePDF417(barcode: Barcode) {
        configureTextInputEditTextCode(PDF_417_LENGTH, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkBlankError(it)
        }
    }

    private fun configureQrCode(barcode: Barcode) {
        val errorCorrectionLevel =
            barcode.getQrCodeErrorCorrectionLevel().errorCorrectionLevel ?: ErrorCorrectionLevel.L
        configureTextInputEditTextCode(
            length = when(errorCorrectionLevel) {
                ErrorCorrectionLevel.L -> 7089
                ErrorCorrectionLevel.M -> 5596
                ErrorCorrectionLevel.Q -> 3993
                ErrorCorrectionLevel.H -> 3057
            },
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        )
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkBlankError(it)
        }
    }

    private fun configureUPCA(barcode: Barcode) {
        configureTextInputEditTextCode(UPC_A_LENGTH, InputType.TYPE_CLASS_NUMBER)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkUPCAError(it)
        }
    }

    private fun configureUPCE(barcode: Barcode) {
        configureTextInputEditTextCode(UPC_E_LENGTH, InputType.TYPE_CLASS_NUMBER)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkUPCEError(it)
        }
    }

    private fun configureDefaultBarcode(barcode: Barcode) {
        configureTextInputEditTextCode(Int.MAX_VALUE, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        configureModifyButton(barcode) {
            devAppsBarcodeFormatChecker.checkBlankError(it)
        }
    }

    companion object {
        fun newInstance(barcode: Barcode): DevAppsBarcodeContentsModifierModalBottomSheetFragment =
            DevAppsBarcodeContentsModifierModalBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BARCODE_KEY, barcode)
                }
            }
    }
}