package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import io.reactivex.Flowable

interface HostActivity{
    fun showMessage(text: CharSequence)
    fun showErrorMessage(text: CharSequence)
    fun isShowProgress():Boolean
    fun showProgress()
    fun hideProgress()
    fun getKeyListenerLd():LiveData<Int>
    fun getHostNavController(): NavController
    fun getBarcodeSingleLiveData():SingleLiveEvent<String>
}