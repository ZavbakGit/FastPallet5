package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class DialogProductCreatePalletViewModel(private val cretePalletRepository: CreatePalletRepository) :
    BaseViewModel<WrapDataDialogProductCreatePallet?, DialogProductCreatePalletViewState>() {

    private var liveDataMerger: MediatorLiveData<WrapDataDialogProductCreatePallet> =
        MediatorLiveData()

    private val documentObserver = Observer<WrapDataDialogProductCreatePallet> {
        viewStateLiveData.value = DialogProductCreatePalletViewState(
            wrapData = it
        )
    }

    init {
        viewStateLiveData.value = DialogProductCreatePalletViewState()
        liveDataMerger.observeForever(documentObserver)
    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)
    }

    fun setGuid(guidProduct: String, guidDoc: String) {

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {

            var doc: CreatePallet? = null
            var product: Product? = null

            fun update() {
                if (doc != null && product != null
                ) {
                    value = WrapDataDialogProductCreatePallet(
                        doc = doc,
                        product = product
                    )
                }
            }

            cretePalletRepository.getDocByGuidLd(guidDoc).apply {
                addSource(this) {
                    doc = it
                    update()
                }
                listSourse.add(this)
            }

            cretePalletRepository.getProductByGuid(guidProduct).apply {
                addSource(this) {
                    product = it
                    update()
                }
                listSourse.add(this)
            }


        }

    }


    fun save(barcode: String?, start: String?, finish: String?, coff: String?) {
        val product = liveDataMerger.value?.product
        val doc = liveDataMerger.value?.doc

        if (product == null) return
        if (doc == null) return

        product.apply {
            this.barcode = barcode
            this.weightStartProduct = start?.toIntOrNull() ?: 0
            this.weightEndProduct = finish?.toIntOrNull() ?: 0
            this.weightCoffProduct = coff?.toFloatOrNull() ?: 0f
        }

        cretePalletRepository.saveProduct(
            product,
            doc.guid
        )
    }
}