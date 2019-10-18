package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product

import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Pallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.domain.extend.*
import `fun`.gladkikh.fastpallet5.maping.creatpallet.toBoxCreatePalletDb
import `fun`.gladkikh.fastpallet5.maping.creatpallet.toPalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*


class ProductCreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel<WrapDataProductCreatePallet?, ProductCreatePalletViewState>() {

    private var liveDataMerger: MediatorLiveData<WrapDataProductCreatePallet> = MediatorLiveData()
    private val dataPublishSubject = PublishSubject.create<WrapDataProductCreatePallet>()

    private val documentObserver = Observer<WrapDataProductCreatePallet> {
        viewStateLiveData.value = ProductCreatePalletViewState(
            wrapData = it
        )
    }


    init {
        viewStateLiveData.value = ProductCreatePalletViewState()
        liveDataMerger.observeForever(documentObserver)

        disposables.add(
            dataPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
                .switchMap {
                    Flowable.fromIterable(it.listItem.indices)
                }
                .map {

                    val infoWrap = liveDataMerger.value!!.listItem[it].pallet?.guid?.let {
                        createPalletRepository.getListBoxByPallet(it).getInfoWrap()
                    }

                    val data = WrapDataProductCreatePallet(
                        doc = liveDataMerger.value!!.doc,
                        product = liveDataMerger.value!!.product,
                        listItem = liveDataMerger.value!!.listItem.mapIndexed { index, itemPallet ->
                            if (index == it) {
                                itemPallet.infoListBoxWrap = infoWrap
                            }

                            return@mapIndexed itemPallet
                        }
                    )

                    val totalInfoListBoxWrap = liveDataMerger.value?.listItem?.map {
                        it.infoListBoxWrap
                    }?.fold(InfoPalletListBoxWrap()) { total, next -> total + next }

                    totalInfoListBoxWrap?.countPallet = liveDataMerger.value?.listItem?.size

                    data.infoListBoxWrap = totalInfoListBoxWrap

                    liveDataMerger.postValue(data)

                    return@map infoWrap
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )


    }

    override fun onCleared() {
        super.onCleared()
        liveDataMerger.removeObserver(documentObserver)

    }

    fun setGuid(guidDoc: String, guidProduct: String) {

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {
            var doc: CreatePallet? = null
            var product: Product? = null
            var listPallet: List<Pallet>? = null

            fun update() {
                if (doc != null && product != null && listPallet != null) {
                    product!!.pallets = listPallet!!

                    val listItem = product!!.pallets.mapIndexed { index, pallet ->
                        ItemPallet(
                            number = product!!.pallets.size - index,
                            pallet = pallet,
                            index = index
                        )
                    }

                    value = WrapDataProductCreatePallet(
                        doc = doc,
                        product = product,
                        listItem = listItem
                    )


                    //Запустим обновление
                    value?.let {
                        dataPublishSubject.onNext(it)
                    }
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

            createPalletRepository.getListPalletByProductLd(guidProduct).apply {
                addSource(this) { list ->
                    listPallet = list.sortedByDescending { it.dataChanged }
                    update()
                }
                listSourse.add(this)
            }
        }
    }

    fun addPallet(barcode: String) {

        val isValid = isValid(barcode)

        if (!isValid.result) {
            messageError.postValue(isValid.message)
        } else {
            val pallet = Pallet(
                guid = UUID.randomUUID().toString(),
                count = null,
                countBox = null,
                number = getNumberDocByBarCode(barcode),
                barcode = barcode,
                dataChanged = Date(),
                nameProduct = null,
                sclad = null,
                state = null
            )

            createPalletRepository.savePallet(pallet, liveDataMerger.value?.product?.guid!!)
        }

    }

    private fun isValid(barcode: String): ValidationResult {

        if (!isPallet(barcode)) return ValidationResult(false, "Этот штрих код не паллеты")

        if (!checkEditDoc(liveDataMerger.value?.doc)) return ValidationResult(
            false,
            "Нельзя изменять документ с этим статусом"
        )

        val number: String?

        try {
            number = getNumberDocByBarCode(barcode)
        } catch (e: Exception) {
            return ValidationResult(false, "Ошибка получения номмера паллеты!")
        }


        if (createPalletRepository.getListPalletByProduct(liveDataMerger.value?.product?.guid!!).find {
                it.number.equals(
                    number
                )
            } != null) {
            return ValidationResult(false, "Паллета уже внесена!")
        }

        return ValidationResult(true)
    }

    fun dell(position: Int) {

        if (!checkEditDoc(liveDataMerger.value?.doc)) {
            messageError.postValue("Нельзя изменять документ с этим статусом")
            return
        }

        commandLd.value = Command.ConfirmDialog("Удалить?", position)
    }

    fun confirmedDell(position: Int) {
        liveDataMerger.value?.product?.pallets?.get(position)?.let {
            createPalletRepository.dellPallet(
                liveDataMerger.value?.product?.pallets?.get(position)!!,
                liveDataMerger.value?.product!!.guid
            )
        }
    }

    fun setTestData(palletCount: Int, boxCount: Int) {

        val listPallet = (0..palletCount).map {
            //Thread.sleep(200)
            Pallet(
                guid = it.toString(),
                count = null,
                countBox = null,
                number = it.toString(),
                barcode = "<pal>0214000000$it}</pal>",
                dataChanged = Date(),
                nameProduct = null,
                sclad = null,
                state = null
            ).toPalletCreatePalletDb(liveDataMerger.value?.product?.guid!!)
        }

        createPalletRepository.createPalletDao.insertOrUpdateListPallet(listPallet)

        listPallet.mapIndexed { index, pallet ->

            val listBox = (0..boxCount).map {

                Box(
                    guid = "${pallet.guid}_$it",
                    countBox = 1,
                    data = Date(),
                    weight = 10.25f
                    //weight = (10000..99999).random().toFloat() * 0.001f
                ).toBoxCreatePalletDb(pallet.guid)
            }

            createPalletRepository.createPalletDao.insertOrUpdateList(
                listBox
            )
            message.postValue("save ${pallet.number}")
        }








    }

}