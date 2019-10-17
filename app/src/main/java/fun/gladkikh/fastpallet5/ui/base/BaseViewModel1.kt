package `fun`.gladkikh.fastpallet5.ui.base

import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel1: ViewModel() {

    val message = SingleLiveEvent<String>()
    val messageError = SingleLiveEvent<String>()
    val showProgress = MutableLiveData<Boolean>()

    protected val commandLd = SingleLiveEvent<Command>()
    fun getCommandLd(): LiveData<Command> = commandLd

    protected val disposables = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}