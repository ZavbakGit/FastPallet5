package `fun`.gladkikh.fastpallet5.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T1, T2, R> zip2(src1: LiveData<T1>, src2: LiveData<T2>,
                     zipper: (T1, T2) -> R): LiveData<R> {

    return MediatorLiveData<R>().apply {
        var src1Version = 0
        var src2Version = 0

        var lastSrc1: T1? = null
        var lastSrc2: T2? = null

        fun updateValueIfNeeded() {
            if (src1Version > 0 && src2Version > 0 &&
                lastSrc1 != null && lastSrc2 != null) {
                value = zipper(lastSrc1!!, lastSrc2!!)
                src1Version = 0
                src2Version = 0
            }
        }

        addSource(src1) {
            lastSrc1 = it
            src1Version++
            updateValueIfNeeded()
        }

        addSource(src2) {
            lastSrc2 = it
            src2Version++
            updateValueIfNeeded()
        }
    }
}


fun <T1, T2, R> myZip2(src1: LiveData<T1>, src2: LiveData<T2>,
                     zipper: (T1, T2) -> R): LiveData<R> {

    return MediatorLiveData<R>().apply {
        var src1Version = 0
        var src2Version = 0

        var lastSrc1: T1? = null
        var lastSrc2: T2? = null

        fun updateValueIfNeeded() {
            if (lastSrc1 != null && lastSrc2 != null) {
                value = zipper(lastSrc1!!, lastSrc2!!)
                src1Version = 0
                src2Version = 0
            }
        }

        addSource(src1) {
            lastSrc1 = it
            src1Version++
            updateValueIfNeeded()
        }

        addSource(src2) {
            lastSrc2 = it
            src2Version++
            updateValueIfNeeded()
        }
    }
}

//myZip2(live1, live2) { name: String, age: String -> "$name $age" }.observeForever {
//    message.value = it
//}
//
//Handler().postDelayed({
//    live2.postValue(" World1")
//}, 1000)
//
//live1.postValue("Hello1")
//
//Handler().postDelayed({
//    live1.postValue(" Hello2")
//}, 2000)
//
//
//Handler().postDelayed({
//    live2.postValue(" World2")
//}, 6000)