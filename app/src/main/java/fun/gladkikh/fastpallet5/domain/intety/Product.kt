package `fun`.gladkikh.fastpallet5.domain.intety

import java.util.*

data class Product(

    var guid: String,
    var number: String?,
    var barcode: String?,

    var guidProduct: String?,
    var nameProduct: String?,
    var codeProduct: String?,
    var ed: String?,

    var weightBarcode: String?,
    var weightStartProduct: Int?,
    var weightEndProduct: Int?,
    var weightCoffProduct: Float?,

    var edCoff: Float?,
    var count: Float?,
    var countBox: Int?,
    var countPallet: Int?,


    var dataChanged: Date?,
    var isWasLoadedLastTime: Boolean?,


    val boxes: List<Box>? = listOf(),
    var pallets: List<Pallet> = listOf()

)