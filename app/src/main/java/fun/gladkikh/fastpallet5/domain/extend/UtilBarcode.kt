package `fun`.gladkikh.fastpallet5.domain.extend

import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.Product

fun Product.getWeightFromBarcode(barcode: String): Float {
    return barcode.substring(0..2).toFloatOrNull() ?: 0f
}

fun List<Box>.getInfoWrap(): InfoListBoxWrap {
    val infoPalletWrap = InfoListBoxWrap(0, 0f)

    this.forEach {
        infoPalletWrap.apply {
            countBox += it.countBox ?: 0
            weight = weight.toBigDecimal().add((it.weight?:0f).toBigDecimal()).toFloat()
        }

    }

    return infoPalletWrap
}

data class InfoListBoxWrap(var countBox: Int, var weight: Float)