package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.App
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Document
import `fun`.gladkikh.fastpallet5.domain.entity.Status
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


    return ApiFactory.request(
        command = "command_confirm_doc",
        objRequest = ConfirmDocumentsLoadRequest(App.settingsRepository.settings.code?:"", list = list),
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