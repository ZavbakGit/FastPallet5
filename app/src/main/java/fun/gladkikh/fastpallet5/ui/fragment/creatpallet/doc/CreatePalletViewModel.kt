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
import io.reactivex.android.schedulers.AndroidSchedulers
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
//                .switchMap {
//                    Flowable.just(it)
//                }
                .doOnNext {

//                  return@map  it.listItem.forEach {
//                        val pallets =
//                            createPalletRepository.getListPalletByProduct(it.product!!.guid)
//
//                        it.infoListBoxWrap = pallets.flatMap {
//                            createPalletRepository.getListBoxByPallet(it.guid)
//                        }.getInfoWrap()
//
//                        it.infoListBoxWrap?.countPallet = pallets.size
//                  } as DocWrapDataCreatePallet


                   val sum =  createPalletRepository.createPalletDao.getSumWeight()
                    val f = 1
                    //342381.8
                    //342504.12

                    message.postValue(sum.total.toString())


//                    //Расчитаем по всем паллетам
//                    val guidProd = liveDataMerger.value!!.listItem[it].product?.guid
//
//                    val infoWrap = createPalletRepository.getListPalletByProduct(guidProd!!)
//                        .flatMap {
//                            createPalletRepository.getListBoxByPallet(it.guid)
//                        }
//                        .getInfoWrap()
//
//                    infoWrap.countPallet =
//                        createPalletRepository.getListPalletByProduct(guidProd).size
//
//
//                    val data = DocWrapDataCreatePallet(
//                        doc = liveDataMerger.value!!.doc,
//
//                        listItem = liveDataMerger.value!!.listItem.mapIndexed { index, itemProduct ->
//                            if (index == it) {
//                                itemProduct.infoListBoxWrap = infoWrap
//                            }
//
//                            return@mapIndexed itemProduct
//                        }
//                    )
//
//                    liveDataMerger.postValue(data)
                    //return@map infoWrap
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    //liveDataMerger.postValue(it)
                }
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