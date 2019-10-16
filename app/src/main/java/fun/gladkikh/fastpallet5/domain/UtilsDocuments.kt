package `fun`.gladkikh.fastpallet5.domain

import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Status

fun <T>getStatusDoc(doc: T):Status?{
    when (doc) {
        is CreatePallet -> {
            return Status.getStatusById(doc.status?:0)
        }
        else -> throw Throwable()
    }
}

fun <T>checkEditDoc(doc:T):Boolean{
    val status = getStatusDoc(doc)
    return status in listOf(Status.LOADED, Status.NEW)
}