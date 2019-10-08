package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.maping.toDocument
import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.intity.GetListDocsRequest
import `fun`.gladkikh.fastpallet5.network.intity.ListDocResponse
import io.reactivex.Single

/**
 * 1. Получаем ответ со списком документов DocResponse
 * 2. Мэпим в Document
 * 3. в confirmLoadDocuments отправляем подтверждение в виде guid типа документа
 *    получаем ответ, сравниваем со старым полученным списком вдруг с 1С, что то не дошло
 *    проставляем новый пришедщий в подтверждении статус
 * 4. Потом при сохранении не будем убивать все
 */
fun getListDocumentsDbFromServer(): Single<List<Document>> {
    //ToDo Взять из настроек
    val getListDocsRequest = GetListDocsRequest(codeTSD = "333")

    return ApiFactory.reqest(
        command = "command_get_doc",
        username = "Администратор",
        pass = "",
        objReqest = getListDocsRequest,
        classResponse = ListDocResponse::class.java
    ).map {
        it as ListDocResponse
    }.map { resp ->
        resp.listDocuments?.map {
            it.toDocument()
        }
    }.flatMap {
        //Отправляем подтверждение и проверяем что в 1С применился новый статус
        confirmLoadDocuments(it)
    }
}



