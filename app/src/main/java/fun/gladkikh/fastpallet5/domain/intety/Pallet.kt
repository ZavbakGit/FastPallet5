package `fun`.gladkikh.fastpallet5.domain.intety

import java.util.*

data class Pallet(
    var number: String?,
    var barcode: String?,
    var guid: String,

    var dataChanged: Date?,
    var count: Float?,
    var countBox: Int?,

    var nameProduct: String?,
    var state: String?,
    var sclad: String?,

    var boxes: List<Box> = listOf()
)