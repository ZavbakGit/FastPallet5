package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.extend.isPallet
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.viewmodel.BaseViewModelFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class ProductCreatPalletViewModel(
    val guidDoc: String,
    val guidProduct: String
) : BaseViewModelFragment() {


    var docCreatePallet:CreatePallet? = null
        set(value) {
            field = value
        }


    lateinit var status:Status

    fun getDocLiveData(): LiveData<CreatePallet> = CreatePalletRepository.getDocByGuid(guidDoc)
    fun getProductLiveData(): LiveData<Product> = CreatePalletRepository.getProductByGuid(guidProduct)
    fun getListPalletByProduct() = CreatePalletRepository.getListPalletByProduct(guidProduct)

    fun addPallet(barcode: String) {
        when{
            isPallet(barcode) -> messageError.postValue("Это не штрих код паллеты!")
            docCreatePallet.status?.let { Status.getStatusById(it) }
                    in [null,Status.LOADED,Status.UNLOADED] ->  messageError.postValue("Это не штрих код паллеты!")
        }

        //ToDo Выполнить необходимые проверки при добавлении паллеты
        val pallet = Pallet(
            guid = UUID.randomUUID().toString(),
            count = null,
            countBox = null,
            number = null,
            barcode = barcode,
            dataChanged = Date(),
            nameProduct = null,
            sclad = null,
            state = null
        )

        CreatePalletRepository.savePallet(pallet, guidProduct)
    }

    fun setDoc(doc:CreatePallet){
        docCreatePallet = doc
    }

    class ViewModelFactory(
        private val guid: String,
        private val guidStringProduct: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductCreatPalletViewModel(guid, guidStringProduct) as T
        }
    }
}