package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import androidx.annotation.AttrRes
import com.devappspros.barcodescanner.R
import java.io.Serializable

class DevAppsNutrient(val entitled: DevAppsNutritionFactsEnum,
                      val values: NutrientValues,
                      private val quantity: Quantity? = null): Serializable {

    fun getQuantityValue(): QuantityRate = quantity?.determineQuantity(values.value100g) ?: QuantityRate.NONE
    fun getLowQuantity(): Float? = quantity?.getLowQuantity()
    fun getHighQuantity(): Float? = quantity?.getHighQuantity()

    class NutrientValues(val value100g: Number?, val valueServing: Number?, val unit: String): Serializable {

        fun getValue100gString(): String = getValueString(value100g)
        fun getValueServingString(): String = getValueString(valueServing)

        private fun getValueString(number: Number?): String {
            val numberStr = number.toString()
            return when {
                numberStr == "null" || numberStr.isBlank() -> "-"
                else -> {
                    try {
                        "${round(number?.toFloat())}$unit"
                    } catch (e: Exception) {
                        "$number$unit"
                    }
                }
            }
        }

        //On arrondie à 2 chiffres après la virgule, et on supprime le dernier caractère si il est à 0
        private fun round(value: Float?): String = "%.2f".format(value).removeSuffix("0")
    }

    class Quantity(private val lowQuantity: Float,
                   private val highQuantity: Float,
                   private val isBeverage: Boolean = false): Serializable {

        fun determineQuantity(value100g: Number?): QuantityRate {

            return if(value100g == null){
                QuantityRate.NONE
            }else{

                val div: Int = if(isBeverage) 2 else 1 // -> Pour les boissons on divise toutes les valeures par 2

                when {
                    value100g.toFloat()>highQuantity/div -> QuantityRate.HIGH
                    value100g.toFloat()<lowQuantity/div -> QuantityRate.LOW
                    else -> QuantityRate.MODERATE
                }
            }
        }

        fun getLowQuantity(): Float = if (isBeverage) lowQuantity / 2 else lowQuantity

        fun getHighQuantity(): Float = if (isBeverage) highQuantity / 2 else highQuantity
    }

    enum class QuantityRate(@AttrRes val colorResource: Int, val stringResource: Int) {
        LOW(R.attr.colorPositive, R.string.off_quantity_low_label),
        MODERATE(R.attr.colorMedium, R.string.off_quantity_moderate_label),
        HIGH(R.attr.colorNegative, R.string.off_quantity_high_label),
        NONE(R.attr.colorUnknown, R.string.off_quantity_none_label)
    }
}