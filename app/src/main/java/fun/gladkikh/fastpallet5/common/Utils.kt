package `fun`.gladkikh.fastpallet5.common

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("dd.mm.yyyy hh:mm:ss")
    return format.format(this)
}

fun String.getDecimalStr():String{
    return "[^\\d,.]".toRegex().replace(this, "").replace(",",".")
}

fun String.getIntByParseStr():Int{
   return this.getDecimalStr().toIntOrNull()?:0
}

fun String.getFloatByParseStr():Float{
    return this.getDecimalStr().toFloatOrNull()?:0f
}