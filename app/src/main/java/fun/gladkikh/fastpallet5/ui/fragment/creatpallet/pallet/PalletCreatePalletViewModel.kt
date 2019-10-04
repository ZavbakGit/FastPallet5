package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.domain.cheskEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.InfoListBoxWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getInfoWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.domain.intety.*
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.base.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit


class PalletCreatePalletViewModel :
    BaseViewModel<PalletWrapDataCreatePallet?, PalletCreatPalletViewState>() {


    private var liveDataMerger: MediatorLiveData<PalletWrapDataCreatePallet> = MediatorLiveData()

    private val dataPublishSubject = PublishSubject.create<List<Box>?>()
    private val infoWrap = MutableLiveData<InfoListBoxWrap>()
    fun getInfoWrap(): LiveData<InfoListBoxWrap> = infoWrap

    private val commandOpenBoxFormLd = SingleLiveEvent<String>()
    fun getCommandOpenBoxFormLd(): LiveData<String> = commandOpenBoxFormLd

    private val documentObserver = Observer<PalletWrapDataCreatePallet> {
        viewStateLiveData.value = PalletCreatPalletViewState(
            wrapData = it
        )
    }

    init {
        viewStateLiveData.value = PalletCreatPalletViewState()
        liveDataMerger.observeForever(documentObserver)

        disposables.add(
            dataPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap {
                    return@switchMap Flowable.just(it).map { it.getInfoWrap() }
                }
                .doOnNext {
                    infoWrap.postValue(it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

    }

    fun refreshInfo() {
        liveDataMerger.value?.pallet?.boxes?.let { dataPublishSubject.onNext(it) }
    }

    fun addBox(barcode: String) {

        if (!cheskEditDoc(liveDataMerger.value?.doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }

        val product = liveDataMerger.value?.product!!

        val weight = getWeightByBarcode(
            barcode = barcode,
            start = product.weightStartProduct ?: 0,
            finish = product.weightEndProduct ?: 0,
            coff = product.weightCoffProduct ?: 0f
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
        CreatePalletRepository.saveBox(box, liveDataMerger.value?.pallet?.guid!!)
        commandOpenBoxFormLd.postValue(box.guid)
    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun setGuid(guidDoc: String, guidProduct: String, guidPallet: String) {
        liveDataMerger.addSource(CreatePalletRepository.getDocByGuid(guidDoc)) {
            liveDataMerger.value =
                PalletWrapDataCreatePallet(
                    doc = it,
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = liveDataMerger.value?.pallet ?: Pallet()
                )
        }

        liveDataMerger.addSource(CreatePalletRepository.getProductByGuid(guidProduct)) {
            liveDataMerger.value =
                PalletWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = it,
                    pallet = liveDataMerger.value?.pallet ?: Pallet()

                )
        }


        liveDataMerger.addSource(CreatePalletRepository.getPalletByGuid(guidPallet)) {
            liveDataMerger.value =
                PalletWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = it
                )
        }


        liveDataMerger.addSource(CreatePalletRepository.getListBoxByPallet(guidPallet)) { list ->
            liveDataMerger.value =
                PalletWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = (liveDataMerger.value?.pallet ?: Pallet()).apply { this.boxes = list }
                )

            refreshInfo()


        }
    }


}