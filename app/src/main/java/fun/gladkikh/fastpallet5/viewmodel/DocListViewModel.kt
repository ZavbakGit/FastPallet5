package `fun`.gladkikh.fastpallet5.viewmodel

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.usecase.LoadDocumentsFromServer
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class DocListViewModel : ViewModel() {

    val message = SingleLiveEvent<String>()
    private val disposables = CompositeDisposable()


    fun loadDocs() {
        disposables.add(
            LoadDocumentsFromServer().getCreatePaletDbFromServer()
                .doOnSuccess {

                    CreatePalletRepository.saveCreatPallet(it)

                    it.forEach {doc->
                        CreatePalletRepository.saveListProduct(doc.listProduct,doc.guid)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    message.value = "Загрузили ${it.size}"
                }, {
                    message.value = it.message
                })
        )
    }

    fun getListCreatePallets() = CreatePalletRepository.getCreatPalletLiveData()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}