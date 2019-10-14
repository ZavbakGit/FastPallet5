package `fun`.gladkikh.fastpallet5.network.intity.old.metaobj

import java.util.*


class PalletServer (
    var number: String? = null,
    var barcode: String? = null,
    var guid: String? = null,

    var dataChanged: Date? = null,
    var count: Float = 0f,
    var countBox: Int = 0,

    var guidProduct: String? = null,
    var nameProduct: String? = null,
    var state: String? = null,
    var sclad: String? = null,

    val boxes: List<BoxServer> = listOf()

)