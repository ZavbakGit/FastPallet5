package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box


import `fun`.gladkikh.fastpallet5.domain.cheskEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.InfoListBoxWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getInfoWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.domain.intety.*
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository


import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.*
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


class BoxCreatePalletViewModel :
    BaseViewModel<BoxWrapDataCreatePallet?, BoxCreatePalletViewState>() {

    private var liveDataMerger: MediatorLiveData<BoxWrapDataCreatePallet> = MediatorLiveData()

    private val dataPublishSubject = PublishSubject.create<List<Box>?>()

    private val infoWrap = MutableLiveData<InfoListBoxWrap>()
    fun getInfoWrap(): LiveData<InfoListBoxWrap> = infoWrap

    private val documentObserver = Observer<BoxWrapDataCreatePallet> {
        viewStateLiveData.value = BoxCreatePalletViewState(
            wrapData = it
        )
    }

    var getBoxGetListBoxByPalletld: LiveData<List<Box>> = MutableLiveData<List<Box>>()

    val createPalletRepository = CreatePalletRepository

    init {
        viewStateLiveData.value = BoxCreatePalletViewState()
        liveDataMerger.observeForever(documentObserver)

        //Подписка на пересчет
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

    /**
     * Пересчитываем колличество
     */
    private fun refreshInfo() {
        liveDataMerger.value?.pallet?.boxes?.let { dataPublishSubject.onNext(it) }
    }

    private fun addSurseGetListBoxByPallet(guidPallet: String, guidBox: String) {
        liveDataMerger.removeSource(getBoxGetListBoxByPalletld)
        getBoxGetListBoxByPalletld = createPalletRepository.getListBoxByPallet(guidPallet)

        liveDataMerger.addSource(getBoxGetListBoxByPalletld) { list ->

            val pallet = (liveDataMerger.value?.pallet ?: Pallet()).apply { this.boxes = list }

            liveDataMerger.value =
                BoxWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = pallet,
                    box = pallet.boxes.find { it.guid == guidBox }
                )

            refreshInfo()

        }
    }

    fun setGuid(guidDoc: String, guidProduct: String, guidPallet: String, guidBox: String) {
        liveDataMerger.addSource(createPalletRepository.getDocByGuid(guidDoc)) {
            liveDataMerger.value =
                BoxWrapDataCreatePallet(
                    doc = it,
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = liveDataMerger.value?.pallet ?: Pallet(),
                    box = liveDataMerger.value?.box ?: Box()
                )
        }

        liveDataMerger.addSource(createPalletRepository.getProductByGuid(guidProduct)) {
            liveDataMerger.value =
                BoxWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = it,
                    pallet = liveDataMerger.value?.pallet ?: Pallet(),
                    box = liveDataMerger.value?.box ?: Box()

                )
        }


        liveDataMerger.addSource(createPalletRepository.getPalletByGuid(guidPallet)) {
            liveDataMerger.value =
                BoxWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc ?: CreatePallet(),
                    product = liveDataMerger.value?.product ?: Product(),
                    pallet = it,
                    box = liveDataMerger.value?.box ?: Box()
                )
        }


        //Эту подписку будем менять
        addSurseGetListBoxByPallet(guidPallet, guidBox)
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
        createPalletRepository.addBox(box, liveDataMerger.value?.pallet?.guid!!)

        //Подпишимся заново
        addSurseGetListBoxByPallet(liveDataMerger.value?.pallet?.guid!!, box.guid)
    }

    /**
     * Намеренье удалить
     */
    fun dell() {
        if (!cheskEditDoc(liveDataMerger.value?.doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }
        commandLd.value = ConfirmDialog("Удалить?")
    }

    /**
     * Подтверждение удаления
     */
    fun confirmedDell() {
        createPalletRepository.dellBox(
            liveDataMerger.value?.box!!,
            liveDataMerger.value?.pallet!!.guid
        )
        commandLd.value = Close()
    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun onFragmentDestroy() {
        if (!cheskEditDoc(liveDataMerger.value?.doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }

        liveDataMerger.value?.box?.let { box ->
            liveDataMerger.value?.pallet?.guid?.let { palletGuid ->
                CreatePalletRepository.update(box, palletGuid)
            }
        }

    }
}