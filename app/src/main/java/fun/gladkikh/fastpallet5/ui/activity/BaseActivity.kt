package `fun`.gladkikh.fastpallet5.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.gladkikh.mylibrary.BarcodeHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject


abstract class BaseActivity: AppCompatActivity(){

    private val publishSubjectKeyClick = PublishSubject.create<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        publishSubjectKeyClick.onNext(keyCode)
        return super.onKeyDown(keyCode, event)
    }

    fun getKeyListenerFlowable(): Flowable<Int> = publishSubjectKeyClick.toFlowable(BackpressureStrategy.DROP)

    protected abstract fun getLayout(): Int
}