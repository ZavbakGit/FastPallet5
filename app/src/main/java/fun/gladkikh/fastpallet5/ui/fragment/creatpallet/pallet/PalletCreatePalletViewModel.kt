package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.InfoListBoxWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getInfoWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.OpenForm
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


class PalletCreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel<PalletWrapDataCreatePallet?, PalletCreatPalletViewState>() {


    private var liveDataMerger: MediatorLiveData<PalletWrapDataCreatePallet> = MediatorLiveData()

    private val dataPublishSubject = PublishSubject.create<List<Box>?>()
    private val infoWrap = MutableLiveData<InfoListBoxWrap>()
    fun getInfoWrap(): LiveData<InfoListBoxWrap> = infoWrap

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
                .switchMap { list ->
                    return@switchMap Flowable.just(list).map { it.getInfoWrap() }
                }
                .doOnNext {
                    infoWrap.postValue(it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

    }

    private fun refreshInfo() {
        liveDataMerger.value?.pallet?.boxes?.let { dataPublishSubject.onNext(it) }
    }


    fun addBox() {
        if (!checkEditDoc(liveDataMerger.value?.doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }
        val pallet = liveDataMerger.value?.pallet?:return

        val box = Box(
            guid = UUID.randomUUID().toString(),
            data = Date()
        )
        createPalletRepository.saveBox(box,pallet.guid)
        commandLd.postValue(OpenForm(box.guid))
    }

    fun addBox(barcode: String) {

        if (!checkEditDoc(liveDataMerger.value?.doc)) {
            messageError.value = "Нельзя изменять документ!"
            return
        }

        val pallet = liveDataMerger.value?.pallet?:return
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
        createPalletRepository.saveBox(box, pallet.guid)

        commandLd.postValue(OpenForm(box.guid))
    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun setGuid(guidDoc: String, guidProduct: String, guidPallet: String) {

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {

            var doc: CreatePallet? = null
            var product: Product? = null
            var pallet: Pallet? = null
            var listBox: List<Box>? = null

            fun update() {
                if (doc != null && product != null
                    && pallet != null && listBox != null
                ) {

                    pallet!!.boxes = listBox!!

                    value = PalletWrapDataCreatePallet(
                        doc = doc,
                        product = product,
                        pallet = pallet
                    )
                }
            }

            createPalletRepository.getDocByGuidLd(guidDoc).apply {
                addSource(this) {
                    doc = it
                    update()
                }
                listSourse.add(this)
            }

            createPalletRepository.getProductByGuid(guidProduct).apply {
                addSource(this) {
                    product = it
                    update()
                }
                listSourse.add(this)
            }


            createPalletRepository.getPalletByGuid(guidPallet).apply {
                addSource(this) {
                    pallet = it
                    update()
                }
                listSourse.add(this)
            }

            createPalletRepository.getListBoxByPalletLd(guidPallet).apply {
                addSource(this) { list ->
                    listBox = list.sortedByDescending { it.data }
                    update()
                    refreshInfo()
                }
                listSourse.add(this)
            }

        }

    }

    fun dell(position: Int) {
        if (!checkEditDoc(liveDataMerger.value?.doc)) {
            messageError.postValue("Нельзя изменять документ с этим статусом")
            return
        }
        commandLd.value = ConfirmDialog("Удалить?", position)
    }

    fun confirmedDell(position: Int) {
        liveDataMerger.value?.pallet?.boxes?.get(position)?.let {
            createPalletRepository.dellBox(
                liveDataMerger.value?.pallet?.boxes?.get(position)!!,
                liveDataMerger.value?.pallet!!.guid
            )
        }
    }


}