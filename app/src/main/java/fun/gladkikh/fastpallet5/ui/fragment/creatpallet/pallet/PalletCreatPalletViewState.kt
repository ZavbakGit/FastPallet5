package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet

import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class PalletCreatPalletViewState(
    val wrapData: PalletWrapDataCreatePallet? = null,
    error: Throwable? = null
) :
    BaseViewState<PalletWrapDataCreatePallet?>(wrapData, error)

data class PalletWrapDataCreatePallet(
    val doc: CreatePallet? = null,
    val product: Product? = null,
    val pallet: Pallet? = null
)