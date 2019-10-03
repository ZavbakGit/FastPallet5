package `fun`.gladkikh.fastpallet5.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel<T, S : BaseViewState<T>> : ViewModel() {
    open val viewStateLiveData = MutableLiveData<S>()
    open fun getViewState(): LiveData<S> = viewStateLiveData

    val message = SingleLiveEvent<String>()
    val messageError = SingleLiveEvent<String>()
    val showProgress = MutableLiveData<Boolean>()

    protected val disposables = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}