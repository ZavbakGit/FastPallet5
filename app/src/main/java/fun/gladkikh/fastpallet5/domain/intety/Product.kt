package `fun`.gladkikh.fastpallet5.domain.intety

import java.util.*

data class Product(

    var guid: String = "",
    var number: String? = null,
    var barcode: String? = null,

    var guidProduct: String? = null,
    var nameProduct: String? = null,
    var codeProduct: String? = null,
    var ed: String? = null,

    var weightBarcode: String? = null,
    var weightStartProduct: Int? = null,
    var weightEndProduct: Int? = null,
    var weightCoffProduct: Float? = null,

    var edCoff: Float? = null,
    var count: Float? = null,
    var countBox: Int? = null,
    var countPallet: Int? = null,


    var dataChanged: Date? = null,
    var isWasLoadedLastTime: Boolean? = null,


    val boxes: List<Box>? = listOf(),
    var pallets: List<Pallet> = listOf()

)