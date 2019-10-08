package `fun`.gladkikh.fastpallet5.maping

import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.domain.intety.Type
import `fun`.gladkikh.fastpallet5.network.intity.DocResponse

fun DocResponse.toDocument(): Document {
    return when (this.type) {
        Type.CREATE_PALLET.nameServer -> {
            this.toCreatePallet()
        }
        else -> throw Throwable("Прищел неизвестный тип документа!")
    }
}
