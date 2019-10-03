package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class ProductCreatPalletViewState(val wrapData: WrapDataProductCreatePallet? = null, error: Throwable? = null):
    BaseViewState<WrapDataProductCreatePallet?>(wrapData, error)

data class WrapDataProductCreatePallet(val doc:CreatePallet? = null, val product: Product? = null)