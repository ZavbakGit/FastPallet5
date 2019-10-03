package `fun`.gladkikh.fastpallet5.domain.intety

import java.util.*

data class Document(
    val guid: String,
    val status: Int?,
    val number: String?,
    val date: Date?,
    val description: String?,
    val type: Int,
    val dataChange:Date?
)