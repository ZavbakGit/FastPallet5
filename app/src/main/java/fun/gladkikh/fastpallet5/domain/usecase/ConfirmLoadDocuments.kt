package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.intity.ConfirmDocumentsLoadRequest
import `fun`.gladkikh.fastpallet5.network.intity.ConfirmResponse
import `fun`.gladkikh.fastpallet5.network.intity.DocConfirm
import io.reactivex.Single


fun confirmLoadDocuments(listDocuments: List<Document>): Single<List<Document>> {
    val list = listDocuments.map {
        when (it) {
            is CreatePallet -> {
                DocConfirm(it.guidServer!!, it.typeFromServer!!)
            }
        }
    }

    //ToDo Взять из настроек
    return ApiFactory.reqest(
        command = "command_confirm_doc",
        username = "Администратор",
        pass = "",
        objReqest = ConfirmDocumentsLoadRequest(codeTSD = "333", list = list),
        classResponse = ConfirmResponse::class.java
    )
        .map {
            it as ConfirmResponse
        }
        .map { confirm ->
            if (list.map { it.guid }.sortedBy { it } !=
                confirm.listConfirm.map { it.guid }.sortedBy { it }) {
                throw Throwable("Не верное подтверждение!")
            }

            //Проставим статус из подтверждения
            return@map listDocuments.map {
                it.setNewStatus(confirm)
            }
        }
}

fun Document.setNewStatus(confirmResponse: ConfirmResponse): Document {
    return when (this) {
        is CreatePallet -> {
            this.apply {
                status =

                    Status.getStatusByString(
                        confirmResponse.listConfirm.find { it.guid == this.guidServer }?.status!!
                    ).id
            }
        }
    }
}