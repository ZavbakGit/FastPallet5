package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box1

import java.util.*

data class Box1CreatePalletViewState(
    val docNumber: String? = null,

    val productName: String? = null,
    val prodStart: Int? = null,
    val prodEnd: Int? = null,
    val prodCoeff: Float? = null,

    val palletNumber: String? = null,
    val palletGuid: String? = null,
    val palletWeight: Float? = null,
    val palletCountBox: Int? = null,
    val palletRow: Int? = null,

    val boxGuid: String? = null,
    val boxBarcode: String? = null,
    val boxDate: Date? = null,
    val boxCountBox:Int? = null,
    val boxWeight:Float? = null
)