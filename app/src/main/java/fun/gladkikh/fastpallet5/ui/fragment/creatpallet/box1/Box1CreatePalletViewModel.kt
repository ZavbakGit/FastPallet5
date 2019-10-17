package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box1

import `fun`.gladkikh.fastpallet5.db.DataQueryForBoxScreen
import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel1
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

class Box1CreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel1() {

    private val liveData: MutableLiveData<Box1CreatePalletViewState> = MutableLiveData()
    var repositoryBox: LiveData<DataQueryForBoxScreen>? = null
    private val saveBufferBoxPublishSubject = PublishSubject.create<List<Box>>()
    private val bufferBoxList: MutableList<Box> = mutableListOf()

    val countBufferSaveLiveData = MutableLiveData<Int>()

    fun getCountBufferSaveLiveData(): LiveData<Int> = countBufferSaveLiveData


    private val observer = Observer<DataQueryForBoxScreen> {

        //message.postValue(it.boxGuid)

        val date = it.boxDate?.let { it1 -> Date(it1) }

        liveData.value = Box1CreatePalletViewState(
            docNumber = it.docNumber,
            productName = it.prodName,
            prodStart = it.prodStart,
            prodEnd = it.prodEnd,
            prodCoeff = it.prodCoeff,
            palletNumber = it.palNumber,
            palletGuid = it.palGuid,
            palletRow = it.palTotalRowsCount,
            palletCountBox = it.palTotalCountBox,
            palletWeight = it.palTotalWeight,
            boxBarcode = it.boxBarcode,
            boxCountBox = it.boxCountBox,
            boxWeight = it.boxWeight,
            boxDate = date,
            boxGuid = it.boxGuid

        )
    }

    init {
        liveData.value = Box1CreatePalletViewState()
        countBufferSaveLiveData.value = 0

        disposables.add(
            saveBufferBoxPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
                .doOnNext {
                    countBufferSaveLiveData.postValue(bufferBoxList.size)
                    val clon = liveData.value!!.copy(
                        boxWeight = it.last().weight,
                        boxGuid = it.last().guid,
                        boxCountBox = it.last().countBox,
                        boxDate = it.last().data)
                    liveData.postValue(clon)

                }
                .debounce(2000, TimeUnit.MILLISECONDS)
                .map { it1 ->
                    val list = it1.map {
                        it.copy()
                    }
                    bufferBoxList.clear()
                    createPalletRepository.saveListBox(list, liveData.value!!.palletGuid!!)
                    countBufferSaveLiveData.postValue(bufferBoxList.size)


                    return@map list.last().guid

                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setGuid(it)
                }
        )
    }

    fun getBox1CreatePalletViewStateLd(): LiveData<Box1CreatePalletViewState> = liveData


    fun setGuid(guid: String) {
        repositoryBox = createPalletRepository.getDataForBoxScreen(guid)
        repositoryBox?.observeForever(observer)
    }

    fun addBox(barcode: String) {

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = liveData.value?.prodStart ?: 0,
            finish = liveData.value?.prodEnd ?: 0,
            coff = liveData.value?.prodCoeff ?: 0f
        )

        if (weight == 0f) {
            messageError.value = "Ошибка в считывания веса! \n $barcode"
            return
        }

        val box = Box(
            guid = UUID.randomUUID().toString(),
            barcode = barcode,
            countBox = 1,
            weight = weight,
            data = Date()
        )

        bufferBoxList.add(box)
        saveBufferBoxPublishSubject.onNext(bufferBoxList)

        //Удалим наблюдателя
        repositoryBox?.removeObserver(observer)
        //createPalletRepository.saveBox(box, liveData.value!!.palletGuid!!)
        //setGuid(box.guid)
    }

}