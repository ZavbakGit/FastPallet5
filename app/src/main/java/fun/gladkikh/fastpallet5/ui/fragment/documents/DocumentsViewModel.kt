package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.ItemDocument
import `fun`.gladkikh.fastpallet5.domain.usecase.getListDocumentsDbFromServer
import `fun`.gladkikh.fastpallet5.domain.usecase.sendCreatPalletToServer
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.repository.DocumentRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DocumentsViewModel(private val documentRepository: DocumentRepository,
                         private val createPalletRepository: CreatePalletRepository) :
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
            getListDocumentsDbFromServer(documentRepository)
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

    fun sendDocToServer(position: Int) {
        val doc = documentListLd.value?.get(position)?.document

        if (!checkEditDoc(doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }

        when (doc) {
            is CreatePallet -> {
                disposables.add(
                    sendCreatPalletToServer(doc,createPalletRepository)
                        .doOnSubscribe {
                            showProgress.postValue(true)
                        }
                        .doFinally {
                            showProgress.postValue(false)
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            message.value = "Отправили"
                        }, {
                            message.value = it.message
                        })
                )
            }
        }
    }

    fun confirmedDell(position: Int) {
        documentListLd.value?.get(position)?.document?.let { documentRepository.dellDocument(it) }
    }

    fun dell(position: Int) {
        commandLd.value = Command.ConfirmDialog("Удалить?", position)
    }

}