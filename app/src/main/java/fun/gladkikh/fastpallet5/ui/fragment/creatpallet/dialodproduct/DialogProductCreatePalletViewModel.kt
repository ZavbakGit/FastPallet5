package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class DialogProductCreatePalletViewModel :
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

            CreatePalletRepository.getDocByGuid(guidDoc).apply {
                addSource(this) {
                    doc = it
                    update()
                }
                listSourse.add(this)
            }

            CreatePalletRepository.getProductByGuid(guidProduct).apply {
                addSource(this) {
                    product = it
                    update()
                }
                listSourse.add(this)
            }


        }

    }


    fun setDataChangeListener(barcode: String?, start: String?, finish: String?, coff: String?) {
        val product = liveDataMerger.value?.product
        val doc = liveDataMerger.value?.doc

        product?.barcode = barcode
        product?.weightStartProduct = start?.toIntOrNull() ?: 0
        product?.weightEndProduct = finish?.toIntOrNull() ?: 0
        product?.weightCoffProduct = coff?.toFloatOrNull() ?: 0f


        liveDataMerger.value = WrapDataDialogProductCreatePallet(
            doc = doc,
            product = product,
            weight = getWeightByBarcode(
                barcode = product?.barcode ?: "",
                start = product?.weightStartProduct ?: 0,
                finish = product?.weightEndProduct ?: 0,
                coff = product?.weightCoffProduct ?: 0f
            )
        )
    }

    fun onFragmentDestroy() {
        liveDataMerger.value?.product?.let { prod ->
            liveDataMerger.value?.doc?.guid?.let { guid ->
                CreatePalletRepository.saveProduct(
                    prod,
                    guid
                )
            }
        }
    }
}