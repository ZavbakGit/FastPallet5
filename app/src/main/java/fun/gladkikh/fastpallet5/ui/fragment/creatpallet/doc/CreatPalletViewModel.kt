package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class CreatPalletViewModel : BaseViewModel<CreatePallet?, CreatPalletViewState>() {


    private var liveDataMerger: MediatorLiveData<CreatePallet> = MediatorLiveData()

    private val documentObserver = Observer<CreatePallet> {
        viewStateLiveData.value = CreatPalletViewState(
            document = it
        )
    }


    init {
        viewStateLiveData.value = CreatPalletViewState(
            document = CreatePallet()
        )
        liveDataMerger.observeForever(documentObserver)

    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun setGuid(guid: String) {
        liveDataMerger.addSource(CreatePalletRepository.getDocByGuid(guid)) {
            val doc = it
            liveDataMerger.value?.listProduct?.let {
                doc.listProduct = liveDataMerger.value?.listProduct!!
            }

            liveDataMerger.value = doc
        }

        liveDataMerger.addSource(CreatePalletRepository.getListProductByDoc(guid)) {list->
            //Если это событие прийдет раньше первого
            var doc = liveDataMerger.value?:CreatePallet()

            doc.listProduct = list
            liveDataMerger.value = doc
        }
    }

}