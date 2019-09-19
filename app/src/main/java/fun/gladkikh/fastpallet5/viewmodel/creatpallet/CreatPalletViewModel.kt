package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreatPalletViewModel(val guidDoc: String) : ViewModel() {

    fun getDoc() = CreatePalletRepository.getDocByGuid(guidDoc)
    fun getListProduct(): LiveData<List<Product>> = CreatePalletRepository.getListProductByDoc(guidDoc)

    class ViewModelFactory(
        private val guid: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreatPalletViewModel(guid) as T
        }
    }
}