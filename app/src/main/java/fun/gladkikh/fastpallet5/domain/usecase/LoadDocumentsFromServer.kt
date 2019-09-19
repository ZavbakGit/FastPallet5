package `fun`.gladkikh.fastpallet5.domain.usecase

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.intity.GetListDocsRequest
import `fun`.gladkikh.fastpallet5.network.intity.ListDocResponse
import io.reactivex.Single
import java.util.*

class LoadDocumentsFromServer {

    fun getCreatePaletDbFromServer(): Single<List<CreatePallet>> {
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

                val listProd = it.listStringsProduct?.map { stringProd ->
                    Product(
                        guid = UUID.randomUUID().toString(),
                        nameProduct = stringProd.nameProduct,
                        isWasLoadedLastTime = true,
                        dataChanged = Date(),
                        barcode = null,
                        number = stringProd.number,
                        countBox = stringProd.countBox?.toIntOrNull() ?: 0,
                        count = stringProd.count?.toFloatOrNull() ?: 0f,
                        countPallet = null,
                        boxes = null,
                        codeProduct = stringProd.codeProduct,
                        ed = stringProd.ed,
                        edCoff = stringProd.edCoff?.toFloatOrNull(),
                        guidProduct = stringProd.guidProduct,
                        weightBarcode = stringProd.barcode,
                        weightCoffProduct = stringProd.weightCoffProduct?.toFloatOrNull() ?: 0f,
                        weightEndProduct = stringProd.weightEndProduct?.toIntOrNull() ?: 0,
                        weightStartProduct = stringProd.weightStartProduct?.toIntOrNull() ?: 0
                    )
                } ?: listOf()

                val createPallet = CreatePallet(
                    guid = UUID.randomUUID().toString(),
                    date = it.date,
                    number = it.number,
                    status = Status.getStatusByString(it.status).id,
                    barcode = null,
                    dataChanged = null,
                    description = null,
                    guidServer = null,
                    isWasLoadedLastTime = null,
                    typeFromServer = null,
                    listProduct = listProd
                )


               return@map createPallet

            }
        }

    }
}