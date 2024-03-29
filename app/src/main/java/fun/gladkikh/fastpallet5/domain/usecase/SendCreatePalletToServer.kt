package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.maping.creatpallet.toCreatPalletOld
import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.intity.SendDocumentsReqest
import `fun`.gladkikh.fastpallet5.network.intity.SendDocumentsResponse
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import io.reactivex.Completable


fun sendCreatePalletToServer(createPallet: CreatePallet,
                             createPalletRepository:CreatePalletRepository): Completable {
    //ToDo Взять из настроек



    val objReqest = SendDocumentsReqest(codeTSD = "333", list = listOf(createPallet.toCreatPalletOld()))


    return ApiFactory.reqest(
        command = "command_send_doc",
        username = "Администратор",
        pass = "",
        objReqest = objReqest,
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



