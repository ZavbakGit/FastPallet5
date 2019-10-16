package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class DialogProductCreatePalletViewState(val wrapData: WrapDataDialogProductCreatePallet? = null, error: Throwable? = null):
    BaseViewState<WrapDataDialogProductCreatePallet?>(wrapData, error)

data class WrapDataDialogProductCreatePallet(
    val doc:CreatePallet? = null,
    val product: Product? = null,
    val weight: Float? = null
)

