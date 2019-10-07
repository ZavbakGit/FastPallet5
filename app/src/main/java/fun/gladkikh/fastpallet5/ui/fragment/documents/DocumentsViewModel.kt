package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.domain.usecase.LoadDocumentsFromServer
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.repository.DocumetRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DocumentsViewModel(documentRepository: DocumetRepository) : BaseViewModel<List<Document>?, DocumentsViewState>() {

    private val documentListLd = documentRepository.getDocumentListLiveData()

    private val documentsObserver = Observer<List<Document>> {
        viewStateLiveData.value = DocumentsViewState(
            documents = it
        )
    }

    override fun onCleared() {
        super.onCleared()
        documentListLd.removeObserver(documentsObserver)
    }



    init {
        viewStateLiveData.value = DocumentsViewState()
        documentListLd.observeForever(documentsObserver)
    }

    fun loadDocs() {
        disposables.add(
            LoadDocumentsFromServer().getCreatePaletDbFromServer()
                .doOnSuccess {
                    CreatePalletRepository.saveCreatPallet(it)
                    it.forEach { doc ->
                        CreatePalletRepository.addListProduct(doc.listProduct, doc.guid)
                    }
                }
                .doOnSubscribe {
                    showProgress.postValue(true)
                }
                .doFinally {
                    showProgress.postValue(false)
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

}