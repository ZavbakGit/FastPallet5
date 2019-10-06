package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box

import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet

import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class BoxCreatePalletViewState(
    val wrapData: BoxWrapDataCreatePallet? = null,
    error: Throwable? = null
) :
    BaseViewState<BoxWrapDataCreatePallet?>(wrapData, error)

data class BoxWrapDataCreatePallet(
    val doc: CreatePallet? = null,
    val product: Product? = null,
    val pallet: Pallet? = null,
    val box: Box? = null
)