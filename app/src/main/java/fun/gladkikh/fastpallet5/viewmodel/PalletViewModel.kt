package `fun`.gladkikh.fastpallet5.viewmodel

import `fun`.gladkikh.fastpallet5.domain.intety.Box
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class PalletViewModel : ViewModel() {
    val infoMld = MutableLiveData<String>()

    val listBoxMld = MutableLiveData<List<Box>>()

    init {
        listBoxMld.value = (0..3).map {
            Box(
                barcode = "11111$it",
                countBox = it,
                data = Date(),
                guid = UUID.randomUUID().toString(),
                weight = 150f + it
            )
        }


    }


    fun addBox(box: Box) {
        Flowable.just(listBoxMld.value)
            .map {
                listBoxMld.postValue(listBoxMld.value?.plus(box)?.sortedByDescending { it.data })
            }
            .doOnNext {

                var sum = 0f
                var count = 0

                listBoxMld.value?.forEach {
                    sum += it.weight?:0f
                    count += it.countBox?:0
                }
                infoMld.postValue("Вес:$sum Кол: $count")

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }
}