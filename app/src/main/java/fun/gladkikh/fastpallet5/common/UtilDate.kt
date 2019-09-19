package `fun`.gladkikh.fastpallet5.common

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("dd-mm-yyyyy hh:mm:ss")
    return format.format(this)
}