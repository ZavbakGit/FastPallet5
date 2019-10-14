package `fun`.gladkikh.fastpallet5.network.intity.old.metaobj

import java.util.*


class ProductServer (

    var guid: String? = null,
    var number: String? = null,
    var barcode: String? = null,

    var guidProduct: String? = null,
    var nameProduct: String? = null,
    var codeProduct: String? = null,
    var ed: String? = null,

    var weightBarcode: String? = null,
    var weightStartProduct: Int = 0,
    var weightEndProduct: Int = 0,
    var weightCoffProduct: Float = 0f,

    var edCoff: Float = 1f,
    var count: Float = 0f,
    var countBox: Int = 0,
    var countPallet: Int = 0, //Это под вопросом


    var dataChanged: Date? = null,
    var isWasLoadedLastTime: Boolean? = null,


    val boxes: List<BoxServer> = listOf(),
    var pallets: List<PalletServer> = listOf()

)