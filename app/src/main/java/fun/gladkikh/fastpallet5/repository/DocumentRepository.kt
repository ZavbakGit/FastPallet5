package `fun`.gladkikh.fastpallet5.repository

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.domain.intety.ItemDocument
import `fun`.gladkikh.fastpallet5.domain.intety.Type.CREATE_PALLET
import `fun`.gladkikh.fastpallet5.maping.toCreatePallet
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

class DocumentRepository(private val createPalletRepository: CreatePalletRepository) {

    fun getDocumentListLiveData(): LiveData<List<ItemDocument>> = Transformations.map(
        createPalletRepository.getListDoc()
    ) {
        it.map { doc ->
            val createPallet = doc.toCreatePallet()
            ItemDocument(
                guid = createPallet.guid,
                type = CREATE_PALLET.id,
                date = createPallet.date,
                description = createPallet.description,
                number = createPallet.number,
                status = createPallet.status,
                dataChange = createPallet.dataChanged,
                document = createPallet
            )
        }
    }

    fun saveDocument(document: Document) {
        when (document) {
            is CreatePallet -> {
                val createPalletFromDb =
                    createPalletRepository.getDocByGuidServer(document.guidServer!!)

                val doc = createPalletFromDb?.apply { document.mixWithDb(this) } ?: document

                createPalletRepository.saveDoc(doc)
                doc.listProduct.forEach {
                    createPalletRepository.saveProduct(it, document.guid)
                }

            }
        }
    }

    fun dellDocument(document: Document) {
        when (document) {
            is CreatePallet -> {
                createPalletRepository.dellDoc(document)
                //var list =  CreatePalletRepository.getPalletAll()
            }
        }
    }

    private fun CreatePallet.mixWithDb(createPalletFromDb: CreatePallet): CreatePallet {

        this.guid = createPalletFromDb.guid
        var oldProduct = createPalletRepository.getListProductByDoc(this.guid)

        //Удаляем каскадно все что без паллет
        oldProduct.forEach { prod ->
            prod.apply {
                pallets = createPalletRepository.getListPalletByProduct(prod.guid)
            }
            if (prod.pallets.isEmpty()) {
                createPalletRepository.dellProduct(
                    product = prod
                    , guidDoc = this.guid
                )
            }
        }

        //Читаем Еще раз
        oldProduct = createPalletRepository.getListProductByDoc(this.guid)

        //Оставили только те которых нет в старом списке
        val newList =
            this.listProduct
                .filter { product ->
                    product.guidProduct !in oldProduct.map { it.guidProduct }
                }

        //Сложили два списка
        createPalletFromDb.listProduct = oldProduct + newList

        return createPalletFromDb

    }

}

