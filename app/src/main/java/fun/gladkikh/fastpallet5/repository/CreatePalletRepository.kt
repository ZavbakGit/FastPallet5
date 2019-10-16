package `fun`.gladkikh.fastpallet5.repository

import `fun`.gladkikh.fastpallet5.db.CreatePalletDao
import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Pallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.maping.creatpallet.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

class CreatePalletRepository(private val createPalletDao: CreatePalletDao) {

    //region Doc
    fun getListDoc() = createPalletDao.getAllLd()

    fun saveDoc(creatPallet: CreatePallet) {
        createPalletDao.insertOrUpdate(creatPallet.toCreatePalletDb())
    }

    fun dellDoc(creatPallet: CreatePallet) =
        createPalletDao.dellDoc(creatPallet.toCreatePalletDb())

    fun getDocByGuidServer(guidServer: String) =
        createPalletDao.getDocByGuidServer(guidServer)?.toCreatePallet()

    fun getDocByGuidLd(guidDoc: String): LiveData<CreatePallet> = Transformations.map(
        createPalletDao.getDocByGuidLd(guidDoc)
    ) {
        it.toCreatePallet()
    }

    fun getFullDocByGuid(guid: String): CreatePallet {
        val doc = createPalletDao.getDocByGuid(guid)?.toCreatePallet()!!
        doc?.listProduct = getListProductByDoc(guid)

        doc?.listProduct?.forEach { product ->
            product.pallets = getListPalletByProduct(product.guid)
            product.pallets.forEach { pallet ->
                pallet.boxes = getListBoxByPallet(pallet.guid)
            }
        }

        return doc
    }


    //fun getFullDocByGuid()


    //endregion

    //region Product
    fun getListProductByDocLd(guidDoc: String): LiveData<List<Product>> = Transformations.map(
        createPalletDao.getProductByDocLd(guidDoc)
    ) {
        it.map { prod ->
            prod.toProduct()
        }
    }

    fun getListProductByDoc(guidDoc: String) =
        createPalletDao.getProductByDoc(guidDoc).map {
            it.toProduct()
        }


    fun getProductByGuid(guidProduct: String): LiveData<Product> = Transformations.map(
        createPalletDao.getProductByGuidLd(guidProduct)
    ) {
        it.toProduct()
    }

    fun saveProduct(product: Product, guidDoc: String) =
        createPalletDao.insertOrUpdate(product.toProductCreatePalletDb(guidDoc))

    fun dellProduct(product: Product, guidDoc: String) =
        createPalletDao.dellProduct(product.toProductCreatePalletDb(guidDoc))
    //endregion

    //region PalletServer
    fun savePallet(pallet: Pallet, guidProduct: String) =
        createPalletDao.insertOrUpdate(pallet.toPalletCreatePalletDb(guidProduct))

    //Для Теста
    fun getPalletAll() = createPalletDao.getPalletAll()

    fun getListPalletByProductLd(guidProduct: String): LiveData<List<Pallet>> = Transformations.map(
        createPalletDao.getListPalletByProductLd(guidProduct)
    ) {
        it.map { pallet ->
            pallet.toPallet()
        }
    }

    fun getListPalletByProduct(guidProduct: String) =
        createPalletDao.getListPalletByProduct(guidProduct).map {
            it.toPallet()
        }

    fun getPalletByGuid(guidPallet: String): LiveData<Pallet> = Transformations.map(
        createPalletDao.getPalletByGuidLd(guidPallet)
    ) {
        it.toPallet()
    }


    //endregion

    //region Box

    fun getListBoxByPalletLd(guidPallet: String): LiveData<List<Box>> = Transformations.map(
        createPalletDao.getListBoxByPalletLd(guidPallet)
    ) {
        it.map { box ->
            box.toBox()
        }
    }

    fun getListBoxByPallet(guidPallet: String) =
        createPalletDao.getListBoxByPallet(guidPallet).map {
            it.toBox()
        }

    fun saveBox(box: Box, guidPallet: String) =
        createPalletDao.insertOrUpdate(box.toBoxCreatePalletDb(guidPallet))

    fun dellPallet(pallet: Pallet, guidProduct: String) {
        createPalletDao.dellPallet(pallet.toPalletCreatePalletDb(guidProduct))
    }

    fun dellBox(box: Box, guidPallet: String) {
        createPalletDao.dellBox(box.toBoxCreatePalletDb(guidPallet))
    }
    //endregion
}