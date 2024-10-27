package com.devappspros.barcodescanner.domain.entity.barcode

import com.devappspros.barcodescanner.R
import com.google.zxing.BarcodeFormat
import java.io.Serializable

enum class BarcodeFormatDetails(val stringResource: Int, val drawableResource: Int, val format: BarcodeFormat): Serializable {
    AZTEC(R.string.barcode_aztec_label, R.drawable.ic_aztec_code_24, BarcodeFormat.AZTEC),
    CODABAR(R.string.barcode_codabar_label, R.drawable.ic_bar_code_24, BarcodeFormat.CODABAR),
    CODE_39(R.string.barcode_code_39_label, R.drawable.ic_bar_code_24, BarcodeFormat.CODE_39),
    CODE_93(R.string.barcode_code_93_label, R.drawable.ic_bar_code_24, BarcodeFormat.CODE_93),
    CODE_128(R.string.barcode_code_128_label, R.drawable.ic_bar_code_24, BarcodeFormat.CODE_128),
    DATA_MATRIX(R.string.barcode_data_matrix_label, R.drawable.ic_data_matrix_code_24, BarcodeFormat.DATA_MATRIX),
    EAN_8(R.string.barcode_ean_8_label, R.drawable.ic_bar_code_24, BarcodeFormat.EAN_8),
    EAN_13(R.string.barcode_ean_13_label, R.drawable.ic_bar_code_24, BarcodeFormat.EAN_13),
    ITF(R.string.barcode_itf_label, R.drawable.ic_bar_code_24, BarcodeFormat.ITF),
    PDF_417(R.string.barcode_pdf_417_label, R.drawable.ic_pdf_417_code_24, BarcodeFormat.PDF_417),
    QR_AGENDA(R.string.qr_code_type_name_agenda, R.drawable.baseline_event_note_24, BarcodeFormat.QR_CODE),
    QR_APPLICATION(R.string.qr_code_type_name_apps, R.drawable.baseline_apps_24, BarcodeFormat.QR_CODE),
    QR_CONTACT(R.string.qr_code_type_name_contact, R.drawable.baseline_contacts_24, BarcodeFormat.QR_CODE),
    QR_EPC(R.string.qr_code_type_name_epc, R.drawable.baseline_qr_code_24, BarcodeFormat.QR_CODE),
    QR_LOCALISATION(R.string.qr_code_type_name_geographic_coordinates, R.drawable.baseline_place_24, BarcodeFormat.QR_CODE),
//    QR_MAIL(R.string.qr_code_type_name_mail, R.drawable.baseline_mail_24, BarcodeFormat.QR_CODE),
    QR_PHONE(R.string.qr_code_type_name_phone, R.drawable.baseline_call_24, BarcodeFormat.QR_CODE),
    QR_SMS(R.string.qr_code_type_name_sms, R.drawable.baseline_textsms_24, BarcodeFormat.QR_CODE),
    QR_TEXT(R.string.qr_code_type_name_text, R.drawable.baseline_text_fields_24, BarcodeFormat.QR_CODE),
    QR_URL(R.string.qr_code_type_name_web_site, R.drawable.baseline_web_24, BarcodeFormat.QR_CODE),
    QR_WIFI(R.string.qr_code_type_name_wifi, R.drawable.baseline_wifi_24, BarcodeFormat.QR_CODE),
    UPC_A(R.string.barcode_upc_a_label, R.drawable.ic_bar_code_24, BarcodeFormat.UPC_A),
    UPC_E(R.string.barcode_upc_e_label, R.drawable.ic_bar_code_24, BarcodeFormat.UPC_E),
}