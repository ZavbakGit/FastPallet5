package `fun`.gladkikh.fastpallet5.maping

import `fun`.gladkikh.fastpallet5.db.initity.BoxCreatPalletDb
import `fun`.gladkikh.fastpallet5.db.initity.CreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.PalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.ProductCreatePalletDb
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import java.util.*

fun CreatePalletDb.toCreatePallet(): CreatePallet {

    return CreatePallet(
        guid = this.guid,
        description = this.description,
        guidServer = this.guidServer,
        typeFromServer = this.typeFromServer,
        isWasLoadedLastTime = this.isWasLoadedLastTime,
        dataChanged = this.dataChanged?.let { Date(it) },
        barcode = this.barcode,
        number = this.number,
        date = this.date?.let { Date(it) },
        status = this.status
    )
}

fun CreatePallet.toCreatePalletDb(): CreatePalletDb {

    return CreatePalletDb(
        guid = this.guid,
        status = this.status,
        number = this.number,
        date = this.date?.time,
        barcode = this.barcode,
        dataChanged = this.dataChanged?.time,
        isWasLoadedLastTime = this.isWasLoadedLastTime,
        typeFromServer = this.typeFromServer,
        guidServer = this.guidServer,
        description = this.description
    )
}

fun ProductCreatePalletDb.toProduct():Product{
    return Product(
        guid = this.guid,
        number = this.number,
        barcode = this.barcode,
        dataChanged = this.dataChanged?.let { Date(it) },
        isWasLoadedLastTime = this.isWasLoadedLastTime,
        countBox = this.countBox,
        nameProduct = this.nameProduct,
        count = this.count,
        weightStartProduct = this.weightStartProduct,
        weightEndProduct = this.weightEndProduct,
        weightCoffProduct = this.weightCoffProduct,
        weightBarcode = this.weightBarcode,
        edCoff = this.edCoff,
        ed = this.ed,
        codeProduct = this.codeProduct,
        countPallet = this.countPallet,
        guidProduct = this.guidProduct
    )
}

fun Product.toProductCreatePalletDb(guidDoc:String):ProductCreatePalletDb{
    return ProductCreatePalletDb(
        guid = this.guid,
        countPallet = this.countPallet,
        codeProduct = this.codeProduct,
        ed = this.ed,
        edCoff = this.edCoff,
        weightBarcode = this.weightBarcode,
        weightCoffProduct = this.weightCoffProduct,
        weightEndProduct = this.weightEndProduct,
        weightStartProduct = this.weightStartProduct,
        guidProduct = this.guidProduct,
        count = this.count,
        nameProduct = this.nameProduct,
        countBox = this.countBox,
        isWasLoadedLastTime = this.isWasLoadedLastTime,
        dataChanged = this.dataChanged?.time,
        barcode = this.barcode,
        number = this.number,
        guidDoc = guidDoc
    )
}

fun PalletCreatePalletDb.toPallet():Pallet{
    return Pallet(
        guid = this.guid,
        number = this.number,
        barcode = this.barcode,
        dataChanged = this.dataChanged?.let { Date(it) },
        countBox = this.countBox,
        nameProduct = this.nameProduct,
        count = this.count,
        state = this.state,
        sclad = this.sclad
    )
}

fun Pallet.toPalletCreatePalletDb(guidProduct:String):PalletCreatePalletDb{
    return PalletCreatePalletDb(
        guid = this.guid,
        sclad = this.sclad,
        state = this.state,
        count = this.count,
        nameProduct = this.nameProduct,
        countBox = this.countBox,
        dataChanged = this.dataChanged?.time,
        barcode = this.barcode,
        number = this.number,
        guidProduct = guidProduct
    )
}

fun BoxCreatPalletDb.toBox(): Box {
    return Box(
        guid = this.guid,
        barcode = this.barcode,
        countBox = this.countBox,
        data = this.data?.let { Date(it) },
        weight = this.weight
    )
}

fun Box.toBoxCreatePalletDb(guidPallet:String):BoxCreatPalletDb{
    return BoxCreatPalletDb(
        guid = this.guid,
        weight = this.weight,
        data = this.data?.time,
        countBox = this.countBox,
        barcode = this.barcode,
        guidPallet = guidPallet
    )
}