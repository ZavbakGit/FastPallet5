package `fun`.gladkikh.fastpallet5.domain.intety


import java.util.*

sealed class Document
data class CreatePallet(
    var guid: String = "",
    var guidServer: String? = null,
    var typeFromServer: String? = "",

    var status: Int? = null,
    var number: String? = null,

    var date: Date? = null,

    var dataChanged: Date? = null,

    var isWasLoadedLastTime: Boolean? = null,
    var description: String? = null,
    var barcode: String? = null,

    var listProduct: List<Product> = listOf()
) : Document()