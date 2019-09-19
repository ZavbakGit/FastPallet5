package `fun`.gladkikh.fastpallet5.repository

import `fun`.gladkikh.fastpallet5.App
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.maping.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

object CreatePalletRepository {


    fun getCreatPalletLiveData(): LiveData<List<CreatePallet>> = Transformations.map(
        App.database.getCreatPalletDao().getAllLd()
    ) {
        it.map { doc ->
            doc.toCreatPallet()
        }
    }

    fun getDocByGuid(guidDoc: String): LiveData<CreatePallet> = Transformations.map(
        App.database.getCreatPalletDao().getDocByGuidLd(guidDoc)
    ) {
        it.toCreatPallet()
    }


    fun getCreatePallet(guid: String) = App.database.getCreatPalletDao().getDocByGuid(guid).toCreatPallet()

    fun getListProductByDoc(guidDoc: String) = Transformations.map(
        App.database.getCreatPalletDao().getProductByDoc(guidDoc)
    ) {
        it.map { prod ->
            prod.toProduct()
        }
    }

    fun getProductByGuid(guidProduct: String): LiveData<Product> = Transformations.map(
        App.database.getCreatPalletDao().getProductByGuidLd(guidProduct)
    ) {
        it.toProduct()
    }

    fun getListPalletByProduct(guidProduct: String) = Transformations.map(
        App.database.getCreatPalletDao().getListPalletByProduct(guidProduct)
    ) {
        it.map { pallet ->
            pallet.toPallet()
        }
    }

    fun getPalletByGuid(guidPallet: String): LiveData<Pallet> = Transformations.map(
        App.database.getCreatPalletDao().getPalletByGuidLd(guidPallet)
    ) {
        it.toPallet()
    }

    fun getListBoxByPallet(guidPallet: String) = Transformations.map(
        App.database.getCreatPalletDao().getListBoxByPallet(guidPallet)
    ) {
        it.map { box ->
            box.toBox()
        }
    }

    fun saveCreatPallet(creatPallet: List<CreatePallet>) =
        App.database.getCreatPalletDao().insert(creatPallet.map {
            it.toCreatePalletDb()
        })

    fun saveListProduct(list: List<Product>, guidDoc: String) =
        App.database.getCreatPalletDao().insertProdList(list.map {
            it.toProductCreatePalletDb(guidDoc)
        })

    fun savePallet(pallet: Pallet, guidProduct: String) =
        App.database.getCreatPalletDao().insert(pallet.toPalletCreatePalletDb(guidProduct))

    fun saveBox(box: Box,guidPallet:String) = App.database.getCreatPalletDao().insert(box.toBoxCreatPalletDb(guidPallet))


}