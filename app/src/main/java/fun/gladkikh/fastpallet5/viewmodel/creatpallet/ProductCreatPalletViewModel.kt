package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class ProductCreatPalletViewModel(
    val guidDoc: String,
    val guidProduct: String
) : ViewModel() {

    fun getDocLiveData(): LiveData<CreatePallet> = CreatePalletRepository.getDocByGuid(guidDoc)
    fun getProductLiveData(): LiveData<Product> = CreatePalletRepository.getProductByGuid(guidProduct)
    fun getListPalletByProduct() = CreatePalletRepository.getListPalletByProduct(guidProduct)

    fun addPallet(barcode: String) {
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

    class ViewModelFactory(
        private val guid: String,
        private val guidStringProduct: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductCreatPalletViewModel(guid, guidStringProduct) as T
        }
    }

}