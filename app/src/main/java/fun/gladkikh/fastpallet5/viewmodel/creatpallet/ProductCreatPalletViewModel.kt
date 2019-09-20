package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.extend.ValidationResult
import `fun`.gladkikh.fastpallet5.domain.extend.getNumberDocByBarCode
import `fun`.gladkikh.fastpallet5.domain.extend.isPallet
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.domain.intety.Status.*
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


    private var statusDoc: Status? = null
    var docCreatePallet: CreatePallet? = null
        set(value) {
            field = value
            statusDoc = Status.getStatusById(field?.status ?: 0)
        }


    lateinit var status: Status

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

    private fun isValid(barcode:String): ValidationResult {
        if (!isPallet(barcode)) return ValidationResult(false,"Этот штрих код не паллеты")
        if (status !in listOf(LOADED,NEW)) return ValidationResult(false,"Нельзя изменять документ с этим статусом")

        try {
            getNumberDocByBarCode(barcode)
        } catch (e: Exception) {
            return ValidationResult(false,"Ошибка получения номмера паллеты!")
        }

        return ValidationResult(true)
    }
}