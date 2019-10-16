package `fun`.gladkikh.fastpallet5.domain.extend

import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import io.reactivex.Flowable
import java.math.BigDecimal

fun Product.getWeightFromBarcode(barcode: String): Float {
    return barcode.substring(0..2).toFloatOrNull() ?: 0f
}

//ToDo Надо перенести куданибудь
fun List<Box>.getInfoWrap(): InfoPalletListBoxWrap {
    val infoPalletWrap = InfoPalletListBoxWrap(0, 0f,0,0)

    val size = this.size

    this.forEach {
        infoPalletWrap.apply {
            countBox = countBox!! + (it.countBox ?: 0)
            weight = weight!!.toBigDecimal().add((it.weight ?: 0f).toBigDecimal()).toFloat()
            row = size
            countPallet = 0
        }
    }

    return infoPalletWrap
}




data class InfoPalletListBoxWrap(
    var countBox: Int? = null,
    var weight: Float? = null,
    var row: Int? = null,
    var countPallet:Int? = null
) {

    fun getInfoBox() = "стр: ".plus(row ?: 0).toString()
        .plus(" кор: ").plus(countBox ?: 0)
        .plus(" кол: ").plus(weight ?: 0)

    fun getInfoPallet() = "пaл: ".plus(countPallet ?: 0)
        .plus(" кор: ").plus(countBox ?: 0)
        .plus(" кол: ").plus(weight ?: 0)

}

operator fun InfoPalletListBoxWrap.plus(infoListBoxWrap: InfoPalletListBoxWrap?): InfoPalletListBoxWrap {
    val weight = BigDecimal(this.weight?.toString() ?: "0")
        .plus(
            BigDecimal(infoListBoxWrap?.weight?.toString() ?: "0")
        )
        .toFloat()

    return InfoPalletListBoxWrap(
        countBox = (infoListBoxWrap?.countBox ?: 0) + (this.countBox ?: 0),
        row = (infoListBoxWrap?.row ?: 0) + (this.row ?: 0),
        weight = weight,
        countPallet = (infoListBoxWrap?.countPallet ?: 0) + (this.countPallet ?: 0)
    )
}


fun getWeightByBarcode(barcode: String, start: Int, finish: Int, coff: Float): Float {


    if (start == 0) {
        return 0F
    }

    if (finish == 0) {
        return 0F
    }

    if (barcode.isEmpty()) return 0F
    if (start >= finish) return 0F
    if (barcode.length < finish) return 0F

    val weightInt: Int = try {
        barcode.subSequence(start - 1, finish).toString().toIntOrNull() ?: 0
    } catch (e: Exception) {
        0
    }

    return BigDecimal(weightInt)
        .multiply(BigDecimal(coff.toString())).toString().toFloatOrNull() ?: 0f

}

fun isPallet(barcode: String): Boolean {
    return barcode.startsWith("<pal>", true) && barcode.endsWith("</pal>", true)
}

fun getNumberDocByBarCode(barcode: String): String {
    //<pal>021400000007</pal>
    if (!isPallet(barcode)) {
        throw Throwable("Не паллета!")
    }

    val strCode = barcode.replace("<pal>", "").replace("</pal>", "")


    val finishPref = 2 + strCode.substring(0, 2).toInt()
    val strKir = strCode.substring(2, finishPref)

    val prefKir = Flowable.fromIterable(strKir.toCharArray().toList())
        .buffer(2)
        .map {
            getCyrillicLetterByNumber(it.joinToString(""))
        }
        .toList().blockingGet().joinToString("")


    return prefKir + strCode.takeLast(strCode.length - finishPref)
}

fun getCyrillicLetterByNumber(s: String): String {

    return when (s) {
        "01" -> "А"

        "02" -> "Б"

        "03" -> "В"

        "04" -> "Г"

        "05" -> "Д"

        "06" -> "Е"

        "07" -> "Ё"

        "08" -> "Ж"

        "09" -> "З"

        "10" -> "И"

        "11" -> "Й"

        "12" -> "К"

        "13" -> "Л"

        "14" -> "М"

        "15" -> "Н"

        "16" -> "О"

        "17" -> "П"

        "18" -> "Р"

        "19" -> "С"

        "20" -> "Т"

        "21" -> "У"

        "22" -> "Ф"

        "23" -> "Х"

        "24" -> "Ц"

        "25" -> "Ч"

        "26" -> "Ш"

        "27" -> "Щ"

        "28" -> "Ъ"

        "29" -> "Ы"

        "30" -> "Ь"

        "31" -> "Э"

        "32" -> "Ю"

        "33" -> "Я"

        else -> ""
    }
}

data class ValidationResult(val result: Boolean, val message: String? = null, val code: Int? = null)