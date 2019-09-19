package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.extend.InfoListBoxWrap
import `fun`.gladkikh.fastpallet5.domain.extend.getInfoWrap
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

class PalletCreatPalletViewModel(
    val guidDoc: String,
    val guidProduct: String,
    val guidPallet: String
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val dataPublishSubject = PublishSubject.create<List<Box>?>()

    private val infoWrap = MutableLiveData<InfoListBoxWrap>()

    init {
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

    fun getDocLiveData(): LiveData<CreatePallet> = CreatePalletRepository.getDocByGuid(guidDoc)
    fun getProductLiveData(): LiveData<Product> = CreatePalletRepository.getProductByGuid(guidProduct)
    fun getPalletByGuid(): LiveData<Pallet> = CreatePalletRepository.getPalletByGuid(guidPallet)
    fun getListBoxByPallet(): LiveData<List<Box>> = CreatePalletRepository.getListBoxByPallet(guidPallet)
    fun getInfoWrap(): LiveData<InfoListBoxWrap> = infoWrap

    fun refreshInfo(list: List<Box>) {
        dataPublishSubject.onNext(list)
    }

    fun addBox(barcode: String) {
        val box = Box(
            guid = UUID.randomUUID().toString(),
            barcode = barcode,
            countBox = 1,
            weight = 100f,
            data = Date()
        )
        CreatePalletRepository.saveBox(box, guidPallet)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    class ViewModelFactory(
        private val guid: String,
        private val guidStringProduct: String,
        private val guidPallet: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PalletCreatPalletViewModel(guid, guidStringProduct, guidPallet) as T
        }
    }

}