package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.ui.base.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.navigation.NavController

interface HostActivity{
    fun showMessage(text: CharSequence)
    fun showErrorMessage(text: CharSequence)
    fun isShowProgress():Boolean
    fun showProgress()
    fun hideProgress()
    fun getKeyListenerLd():LiveData<Int>
    fun getNavController(): NavController
    fun getBarcodeSingleLiveData(): SingleLiveEvent<String>
}