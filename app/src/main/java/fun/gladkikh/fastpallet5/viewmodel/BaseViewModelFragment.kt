package `fun`.gladkikh.fastpallet5.viewmodel

import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModelFragment:ViewModel(){
    val message = SingleLiveEvent<String>()
    val messageError = SingleLiveEvent<String>()

    fun getMessageLiveData():LiveData<String> = message
    fun getmessageErrorLiveData():LiveData<String> = messageError
}