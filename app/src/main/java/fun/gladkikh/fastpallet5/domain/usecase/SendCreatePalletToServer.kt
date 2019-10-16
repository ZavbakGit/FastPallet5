package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.App
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Status
import `fun`.gladkikh.fastpallet5.maping.creatpallet.toCreatPalletOld
import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.intity.SendDocumentsReqest
import `fun`.gladkikh.fastpallet5.network.intity.SendDocumentsResponse
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import io.reactivex.Completable


fun sendCreatePalletToServer(createPallet: CreatePallet,
                             createPalletRepository:CreatePalletRepository): Completable {


    val objReqest = SendDocumentsReqest(App.settingsRepository.settings.code?:"", list = listOf(createPallet.toCreatPalletOld()))


    return ApiFactory.request(
        command = "command_send_doc",
        objRequest = objReqest,
        classResponse = SendDocumentsResponse::class.java
    )
        .map {
            it as SendDocumentsResponse
        }
        .doOnSuccess { response ->
            response.listConfirm.forEach {
                val doc = createPalletRepository.getDocByGuidServer(it.guid).apply {
                    this?.status = Status.getStatusByString(it.status).id
                }
                createPalletRepository.saveDoc(doc!!)
            }
        }
        .ignoreElement()
}



