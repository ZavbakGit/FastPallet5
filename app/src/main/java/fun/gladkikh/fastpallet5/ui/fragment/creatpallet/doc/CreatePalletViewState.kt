package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.domain.extend.InfoPalletListBoxWrap
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class CreatePalletViewState(val wrapData: DocWrapDataCreatePallet? = null, error: Throwable? = null):
    BaseViewState<DocWrapDataCreatePallet?>(wrapData, error)

data class DocWrapDataCreatePallet(
    val doc:CreatePallet? = null,
    val listItem:List<ItemProduct> = listOf()
)

data class ItemProduct(
    val number:Int? = null,
    val product: Product? = null,
    var infoListBoxWrap: InfoPalletListBoxWrap? = null
)