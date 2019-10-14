package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product

import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.ValidationResult
import `fun`.gladkikh.fastpallet5.domain.extend.getNumberDocByBarCode
import `fun`.gladkikh.fastpallet5.domain.extend.isPallet
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.*


class ProductCreatePalletViewModel(private val createPalletRepository: CreatePalletRepository) :
    BaseViewModel<WrapDataProductCreatePallet?, ProductCreatPalletViewState>() {

    private var liveDataMerger: MediatorLiveData<WrapDataProductCreatePallet> = MediatorLiveData()

    private val documentObserver = Observer<WrapDataProductCreatePallet> {
        viewStateLiveData.value = ProductCreatPalletViewState(
            wrapData = it
        )
    }


    init {
        viewStateLiveData.value = ProductCreatPalletViewState()
        liveDataMerger.observeForever(documentObserver)


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

                    value = WrapDataProductCreatePallet(
                        doc = doc,
                        product = product
                    )
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


}