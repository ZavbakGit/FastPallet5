package `fun`.gladkikh.fastpallet5.maping.creatpallet

import `fun`.gladkikh.fastpallet5.domain.intety.*
import `fun`.gladkikh.fastpallet5.network.intity.old.metaobj.BoxServer
import `fun`.gladkikh.fastpallet5.network.intity.old.metaobj.CreatePalletServer
import `fun`.gladkikh.fastpallet5.network.intity.old.metaobj.PalletServer
import `fun`.gladkikh.fastpallet5.network.intity.old.metaobj.ProductServer

fun CreatePallet.toCreatPalletOld(): CreatePalletServer {
    return CreatePalletServer(
        guid = this.guid,
        barcode = this.barcode,
        status = Status.getStatusById(this.status!!)!!,
        number = this.number,
        description = this.description,
        date = this.date,
        dataChanged = this.dataChanged,
        guidServer = this.guidServer,

        isWasLoadedLastTime = this.isWasLoadedLastTime,
        typeFromServer = this.typeFromServer,
        listProduct = this.listProduct.toListProductServer()
    )
}

fun List<Product>.toListProductServer(): List<ProductServer> {
    return this.map {
        ProductServer(
            guid = it.guid,
            count = 0f,
            countBox = 0,
            nameProduct = it.nameProduct,
            guidProduct = it.guidProduct,
            number = it.number,
            dataChanged = it.dataChanged,
            barcode = it.barcode,
            isWasLoadedLastTime = it.isWasLoadedLastTime,
            codeProduct = it.codeProduct,
            countPallet = 0,
            ed = it.ed,
            edCoff = it.edCoff ?: 1f,
            weightStartProduct = it.weightStartProduct ?: 0,
            weightEndProduct = it.weightEndProduct ?: 0,

            weightBarcode = it.weightBarcode,
            weightCoffProduct = it.edCoff ?: 0f,
            pallets = it.pallets.toListPalletServer(),
            boxes = it.boxes?.toListBoxServer() ?: listOf()

        )
    }
}

fun List<Pallet>.toListPalletServer(): List<PalletServer> {
    return this.map {
        PalletServer(
            guid = it.guid,
            barcode = it.barcode,
            dataChanged = it.dataChanged,
            number = it.number,
            guidProduct = null,
            nameProduct = null,
            sclad = null,
            state = null,
            countBox = 0,
            count = 0f,
            boxes = it.boxes.toListBoxServer()
        )
    }
}

fun List<Box>.toListBoxServer(): List<BoxServer> {
    return this.map { box ->
        BoxServer(
            guid = box.guid,
            barcode = box.barcode,
            weight = box.weight ?: 0f,
            data = box.data,
            countBox = box.countBox ?: 0
        )
    }
}