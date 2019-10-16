package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.domain.entity.Box
import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Pallet

import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class PalletCreatPalletViewState(
    val wrapData: PalletWrapDataCreatePallet? = null,
    error: Throwable? = null
) :
    BaseViewState<PalletWrapDataCreatePallet?>(wrapData, error)

data class PalletWrapDataCreatePallet(
    val doc: CreatePallet? = null,
    val product: Product? = null,
    val pallet: Pallet? = null,
    val listItem:List<ItemBox> = listOf()
)

data class ItemBox(
    val number:Int? = null,
    val box: Box? = null
)