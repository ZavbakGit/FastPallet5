package `fun`.gladkikh.fastpallet5.domain.intety

import java.util.*

data class Box(
    var barcode: String? = null,
    var weight: Float? = null,
    var countBox: Int? = null,
    var data: Date? = null,
    var guid: String = ""
)