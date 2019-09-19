package `fun`.gladkikh.fastpallet5.domain.intety


import java.util.*

data class CreatePallet(

    var guid: String,
    var guidServer: String?,

    var typeFromServer: String?,

    var status: Int?,
    var number: String?,

    var date: Date?,

    var dataChanged: Date?,

    var isWasLoadedLastTime: Boolean?,
    var description: String?,
    var barcode: String?,

    var listProduct:List<Product> = listOf()
)