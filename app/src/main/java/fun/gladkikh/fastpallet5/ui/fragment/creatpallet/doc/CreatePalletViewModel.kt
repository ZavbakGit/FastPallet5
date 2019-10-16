package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.domain.extend.getInfoWrap
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class CreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel<DocWrapDataCreatePallet?, CreatePalletViewState>() {

    private val dataPublishSubject = PublishSubject.create<DocWrapDataCreatePallet>()

    private var liveDataMerger: MediatorLiveData<DocWrapDataCreatePallet> = MediatorLiveData()

    private val documentObserver = Observer<DocWrapDataCreatePallet> {
        viewStateLiveData.value = CreatePalletViewState(
            wrapData = it
        )
    }


    init {
        viewStateLiveData.value = CreatePalletViewState(
            wrapData = DocWrapDataCreatePallet()
        )
        liveDataMerger.observeForever(documentObserver)

        disposables.add(
            dataPublishSubject.toFlowable(BackpressureStrategy.BUFFER)
                .switchMap {
                    Flowable.fromIterable(it.listItem.indices)
                }
                .map { it ->

                    //Расчитаем по всем паллетам
                    val infoWrap = liveDataMerger.value!!.listItem[it].product?.pallets
                        ?.flatMap { pallet ->
                            createPalletRepository.getListBoxByPallet(pallet.guid)
                        }
                        ?.getInfoWrap()


                    val data = DocWrapDataCreatePallet(
                        doc = liveDataMerger.value!!.doc,

                        listItem = liveDataMerger.value!!.listItem.mapIndexed { index, itemProduct ->
                            if (index == it) {
                                itemProduct.infoListBoxWrap = infoWrap
                            }

                            return@mapIndexed itemProduct
                        }
                    )

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

    fun setGuid(guid: String) {

        //Обязательно добавляем и удаляем
        cleanSourseMediator(liveDataMerger)

        liveDataMerger.apply {
            var doc: CreatePallet? = null
            var listProduct: List<Product>? = null

            fun update() {
                if (doc != null && listProduct != null) {
                    doc!!.listProduct = listProduct!!

                    value = DocWrapDataCreatePallet(
                        doc = doc,
                        listItem = doc!!.listProduct.mapIndexed { index, product ->
                            ItemProduct(
                                product = product,
                                number = doc!!.listProduct.size - index
                            )
                        }
                    )

                    //Запустим обновление
                    value?.let {
                        dataPublishSubject.onNext(it)
                    }
                }
            }


            createPalletRepository.getDocByGuidLd(guid).apply {
                addSource(this) {
                    doc = it
                    update()
                }
                listSourse.add(this)
            }

            createPalletRepository.getListProductByDocLd(guid).apply {
                addSource(this) {
                    listProduct = it
                    update()
                }
                listSourse.add(this)
            }


        }


    }

}