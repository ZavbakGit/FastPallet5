package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class CreatePalletViewModel(private val createPalletRepository: CreatePalletRepository)
    : BaseViewModel<CreatePallet?, CreatePalletViewState>() {

    private var liveDataMerger: MediatorLiveData<CreatePallet> = MediatorLiveData()

    private val documentObserver = Observer<CreatePallet> {
        viewStateLiveData.value = CreatePalletViewState(
            document = it
        )
    }


    init {
        viewStateLiveData.value = CreatePalletViewState(
            document = CreatePallet()
        )
        liveDataMerger.observeForever(documentObserver)

    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun setGuid(guid: String) {

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {
            var doc: CreatePallet? = null
            var listProduct: List<Product>? = null

            fun update() {
                if (doc != null && listProduct != null) {
                    doc!!.listProduct = listProduct!!
                    value = doc
                }
            }
            createPalletRepository.getDocByGuidLd(guid).apply {
                addSource(this) {
                    doc = it
                    update()
                }
                listSourse.add(this)
            }

            createPalletRepository.getListProductByDocLd(guid).apply {
                addSource(this) {
                    listProduct = it
                    update()
                }
                listSourse.add(this)
            }


        }


    }

}