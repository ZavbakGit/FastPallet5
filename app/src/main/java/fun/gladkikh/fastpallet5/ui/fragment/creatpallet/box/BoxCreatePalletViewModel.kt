package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box


import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.InfoPalletListBoxWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Pallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.Close
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit


class BoxCreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel<BoxWrapDataCreatePallet?, BoxCreatePalletViewState>() {

    private var liveDataMerger: MediatorLiveData<BoxWrapDataCreatePallet> = MediatorLiveData()
    private val refreshPublishSubject = PublishSubject.create<List<Box>?>()
    private val saveBufferBoxPublishSubject = PublishSubject.create<List<Box>>()

    private val infoWrap = MutableLiveData<InfoPalletListBoxWrap>()
    fun getInfoWrap(): LiveData<InfoPalletListBoxWrap> = infoWrap

    private val documentObserver = Observer<BoxWrapDataCreatePallet> {
        viewStateLiveData.value = BoxCreatePalletViewState(
            wrapData = it
        )
    }


    init {
        viewStateLiveData.value = BoxCreatePalletViewState()
        liveDataMerger.observeForever(documentObserver)




        //Сохранение box через буффер
        disposables.add(
            saveBufferBoxPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
                .doOnNext {
                    //Прокинем список и последний box
                    liveDataMerger.postValue(
                        BoxWrapDataCreatePallet(
                            doc = liveDataMerger.value?.doc,
                            product = liveDataMerger.value?.product,
                            pallet = liveDataMerger.value?.pallet,
                            box = it.last(),
                            bufferSaveListBox = it
                        )
                    )
                }
                .debounce(2000, TimeUnit.MILLISECONDS)
//                .map {
//
//                    return@map lastGuid
//                }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    createPalletRepository.saveListBox(it, liveDataMerger.value?.pallet?.guid!!)
                    val lastGuid = it.last().guid

                    liveDataMerger.postValue(
                        BoxWrapDataCreatePallet(
                            doc = liveDataMerger.value?.doc,
                            product = liveDataMerger.value?.product,
                            pallet = liveDataMerger.value?.pallet,
                            box = it.last(),
                            bufferSaveListBox = listOf()
                        )
                    )

                    setGuid(
                        guidDoc = liveDataMerger.value?.doc!!.guid,
                        guidProduct = liveDataMerger.value?.product!!.guid,
                        guidPallet = liveDataMerger.value?.pallet!!.guid,
                        guidBox = it.last().guid
                    )
                }
        )


        //Подписка на пересчет
//        disposables.add(
////            refreshPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
////                .debounce(300, TimeUnit.MILLISECONDS)
////                .switchMap { list ->
////                    val info =
////                        createPalletRepository.createPalletDao.getInfoPallet(liveDataMerger.value?.pallet!!.guid)
////
////                    val inf = InfoPalletListBoxWrap(
////                        countBox = info.count,
////                        weight = info.weight,
////                        row = 1,
////                        countPallet = 1
////                    )
////
////
////                    //return@switchMap Flowable.just(list).map { it.getInfoWrap() }
////                    return@switchMap Flowable.just(inf)
////                    //return@switchMap Flowable.just(InfoPalletListBoxWrap())
////                }
////                .doOnNext {
////                    infoWrap.postValue(it)
////                }
////                .subscribeOn(Schedulers.io())
////                .subscribe()
////        )
////
////        disposables.add(
////            saveBoxPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
////                .doOnNext {
////                    createPalletRepository.saveBox(it, liveDataMerger.value?.pallet?.guid!!)
////                }
////                .subscribeOn(Schedulers.io())
////                .subscribe()
//        )


    }

    /**
     * Пересчитываем колличество
     */
    private fun refreshInfo() {
        liveDataMerger.value?.pallet?.boxes?.let { refreshPublishSubject.onNext(it) }
    }


    fun setGuid(
        guidDoc: String,
        guidProduct: String,
        guidPallet: String,
        guidBox: String,
        boxParam: Box? = null
    ) {

        if (boxParam != null) {
            liveDataMerger.value =
                BoxWrapDataCreatePallet(
                    doc = liveDataMerger.value?.doc,
                    product = liveDataMerger.value?.product,
                    pallet = liveDataMerger.value?.pallet,
                    box = boxParam
                )

        }

       //val model =  createPalletRepository.createPalletDao.getDataForBoxScreen(guidBox)

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {

            var doc: CreatePallet? = null
            var product: Product? = null
            var pallet: Pallet? = null
            var box: Box? = null
            var listBox: List<Box>? = null

            fun update() {
                if (doc != null && product != null
                    && pallet != null && box != null
                    && listBox != null
                ) {

                    pallet!!.boxes = listBox!!

                    postValue(
                        BoxWrapDataCreatePallet(
                            doc = doc,
                            product = product,
                            pallet = pallet,
                            box = box
                        )
                    )

                    refreshInfo()
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
                    //ToDo Для проверки
//                    listBox = listOf()
//                    if (boxParam == null) {
//                        box = createPalletRepository.createPalletDao.getBoxByGuid(guidBox).toBox()
//                    } else {
//                        box = boxParam
//                    }

                    update()
                }
                listSourse.add(this)

            }

            createPalletRepository.getBoxByGuidLd(guidBox).apply {
                addSource(this) {
                    listBox = listOf()
                    box = it
                    update()
                }
                listSourse.add(this)
            }


//            createPalletRepository.getListBoxByPalletLd(guidPallet).apply {
//                addSource(this) { list ->
//                    listBox = list.sortedByDescending { it.data }
//                    boxParam = list.find { it.guid == guidBox }
//                    update()
//
//                }
//                listSourse.add(this)
//            }


        }

    }

    fun addBox(barcode: String) {

        if (!checkEditDoc(liveDataMerger.value?.doc)) {
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

        //Передаем в буфер
        saveBufferBoxPublishSubject.onNext(liveDataMerger.value!!.bufferSaveListBox + box)


        //Надо вынести на после сохранения
//        setGuid(
//            guidDoc = liveDataMerger.value?.doc!!.guid,
//            guidProduct = liveDataMerger.value?.product!!.guid,
//            guidPallet = liveDataMerger.value?.pallet!!.guid,
//            guidBox = box.guid,
//            boxParam = box
//        )


        //saveBoxPublishSubject.onNext(box)
        //createPalletRepository.saveBox(box, liveDataMerger.value?.pallet?.guid!!)
    }

    /**
     * Намеренье удалить
     */
    fun dell() {
        if (!checkEditDoc(liveDataMerger.value?.doc)) {
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
        commandLd.value = Close
    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun save(weight: String?, countBox: String?) {
        val box = liveDataMerger.value?.box ?: return
        val pallet = liveDataMerger.value?.pallet ?: return

        box.apply {
            this.weight = weight?.toFloatOrNull() ?: 0f
            this.countBox = countBox?.toIntOrNull() ?: 0
        }

        createPalletRepository.saveBox(box, pallet.guid)
    }
}