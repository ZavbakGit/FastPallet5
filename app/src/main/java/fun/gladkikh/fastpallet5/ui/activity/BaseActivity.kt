package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.ui.base.SingleLiveEvent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


abstract class BaseActivity: AppCompatActivity(){


    private val keyDownLiveData = MutableLiveData<Int>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        keyDownLiveData.postValue(keyCode)
        return super.onKeyDown(keyCode, event)
    }



    fun getKeyListenerLd(): LiveData<Int> = keyDownLiveData


    protected abstract fun getLayout(): Int
}