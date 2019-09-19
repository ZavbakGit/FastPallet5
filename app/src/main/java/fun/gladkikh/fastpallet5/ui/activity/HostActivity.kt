package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import io.reactivex.Flowable

interface HostActivity{
    fun showMessage(text: CharSequence)
    fun isShowProgress():Boolean
    fun showProgress()
    fun hideProgress()
    fun getKeyListenerFlowable():Flowable<Int>
    fun getHostNavController(): NavController
    fun getBarcodeSingleLiveData():SingleLiveEvent<String>
}