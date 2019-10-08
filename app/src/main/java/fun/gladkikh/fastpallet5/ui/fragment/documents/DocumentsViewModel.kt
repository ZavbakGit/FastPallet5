package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.intety.ItemDocument
import `fun`.gladkikh.fastpallet5.domain.usecase.getListDocumentsDbFromServer
import `fun`.gladkikh.fastpallet5.repository.DocumentRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DocumentsViewModel(val documentRepository: DocumentRepository) :
    BaseViewModel<List<ItemDocument>?, DocumentsViewState>() {


    private val documentListLd = documentRepository.getDocumentListLiveData()

    private val documentsObserver = Observer<List<ItemDocument>> {
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
            getListDocumentsDbFromServer()
                .doOnSuccess {
                    it.forEach { doc ->
                        DocumentRepository.saveDocument(doc)
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

    fun confirmedDell(position: Int) {
        documentListLd.value?.get(position)?.document?.let { documentRepository.dellDocument(it) }
    }

    fun dell(position: Int) {
        commandLd.value = Command.ConfirmDialog("Удалить?", position)
    }

}