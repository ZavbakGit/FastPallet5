package `fun`.gladkikh.fastpallet5.viewmodel.creatpallet

import `fun`.gladkikh.fastpallet5.domain.extend.ValidationResult
import `fun`.gladkikh.fastpallet5.domain.extend.getNumberDocByBarCode
import `fun`.gladkikh.fastpallet5.domain.extend.isPallet
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.domain.intety.Status.LOADED
import `fun`.gladkikh.fastpallet5.domain.intety.Status.NEW
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.viewmodel.BaseViewModelFragment
import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import androidx.lifecycle.*
import java.util.*

class ProductCreatPalletViewModel(
    val guidDoc: String,
    val guidProduct: String
) : BaseViewModelFragment() {


    private var dataModel = DataModel()

    private val mediatorDataModel: MediatorLiveData<DataModel> =
        MediatorLiveData<DataModel>().apply {
            addSource(CreatePalletRepository.getDocByGuid(guidDoc)) {
                dataModel.docCreatePallet = it
                this.postValue(dataModel)
            }

            addSource(CreatePalletRepository.getProductByGuid(guidProduct)) {
                dataModel.product = it
                this.postValue(dataModel)
            }

            addSource(CreatePalletRepository.getListPalletByProductLd(guidProduct)) {
                dataModel.listPallet = it
                this.postValue(dataModel)
            }
        }

    private val confirmDell = SingleLiveEvent<DataConfirmDell>()


    fun getDataModelLd(): LiveData<DataModel> = mediatorDataModel

    fun getConfirmDellLd(): LiveData<DataConfirmDell> = confirmDell

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

            CreatePalletRepository.savePallet(pallet, guidProduct)
        }

    }

    class ViewModelFactory(
        private val guid: String,
        private val guidStringProduct: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductCreatPalletViewModel(guid, guidStringProduct) as T
        }
    }

    private fun isValid(barcode: String): ValidationResult {
        val status = dataModel.docCreatePallet?.status?.let { Status.getStatusById(it) }

        if (!isPallet(barcode)) return ValidationResult(false, "Этот штрих код не паллеты")
        if (status !in listOf(LOADED, NEW)) return ValidationResult(
            false,
            "Нельзя изменять документ с этим статусом"
        )

        val number: String?

        try {
            number = getNumberDocByBarCode(barcode)
        } catch (e: Exception) {
            return ValidationResult(false, "Ошибка получения номмера паллеты!")
        }

        if (CreatePalletRepository.getListPalletByProduct(guidProduct).find {
                it.number.equals(
                    number
                )
            } != null) {
            return ValidationResult(false, "Паллета уже внесена!")
        }

        return ValidationResult(true)
    }

    fun keyPressDell(position: Int) {
        val status = dataModel.docCreatePallet?.status?.let { Status.getStatusById(it) }
        if (status !in listOf(LOADED, NEW)) {
            messageError.postValue("Нельзя изменять документ с этим статусом")
            return
        }

        dataModel.listPallet[position].number?.let {
            confirmDell.postValue(
                DataConfirmDell(
                    "Удалить паллету № $it", position
                )
            )
        }
    }

    fun confirmedDell(position: Int) {
        CreatePalletRepository.dellPallet(dataModel.listPallet[position],dataModel.product!!.guid)
    }

    data class DataModel(
        var docCreatePallet: CreatePallet? = null,
        var product: Product? = null,
        var listPallet: List<Pallet> = listOf()
    )

    data class DataConfirmDell(var message: String, var position: Int)
}