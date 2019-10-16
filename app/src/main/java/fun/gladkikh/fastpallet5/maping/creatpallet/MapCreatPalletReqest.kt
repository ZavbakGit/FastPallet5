package `fun`.gladkikh.fastpallet5.maping.creatpallet

import `fun`.gladkikh.fastpallet5.common.getFloatByParseStr
import `fun`.gladkikh.fastpallet5.common.getIntByParseStr
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.domain.entity.Status
import `fun`.gladkikh.fastpallet5.network.intity.DocResponse
import java.util.*

fun DocResponse.toCreatePallet(): CreatePallet {

    val listProd = this.listStringsProduct?.map { stringProd ->
        Product(
            guid = UUID.randomUUID().toString(),
            nameProduct = stringProd.nameProduct,
            isWasLoadedLastTime = true,
            dataChanged = Date(),
            barcode = null,
            number = stringProd.number,
            countBox = stringProd.countBox?.getIntByParseStr(),
            count = stringProd.count?.getFloatByParseStr(),
            countPallet = null,
            boxes = null,
            codeProduct = stringProd.codeProduct,
            ed = stringProd.ed,
            edCoff = stringProd.edCoff?.getFloatByParseStr(),
            guidProduct = stringProd.guidProduct,
            weightBarcode = stringProd.barcode,
            weightCoffProduct = stringProd.weightCoffProduct?.getFloatByParseStr(),
            weightEndProduct = stringProd.weightEndProduct?.getIntByParseStr(),
            weightStartProduct = stringProd.weightStartProduct?.getIntByParseStr()
        )
    } ?: listOf()


    return CreatePallet(
        guid = UUID.randomUUID().toString(),
        date = this.date,
        number = this.number,
        status = Status.getStatusByString(this.status).id,
        barcode = null,
        dataChanged = null,
        description = this.description,
        guidServer = this.guid,
        isWasLoadedLastTime = null,
        typeFromServer = this.type,
        listProduct = listProd
    )
}