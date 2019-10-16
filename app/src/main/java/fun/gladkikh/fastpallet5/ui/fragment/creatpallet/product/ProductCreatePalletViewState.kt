package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product

import `fun`.gladkikh.fastpallet5.domain.entity.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.entity.Pallet
import `fun`.gladkikh.fastpallet5.domain.entity.Product
import `fun`.gladkikh.fastpallet5.domain.extend.InfoPalletListBoxWrap
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class ProductCreatePalletViewState(val wrapData: WrapDataProductCreatePallet? = null, error: Throwable? = null):
    BaseViewState<WrapDataProductCreatePallet?>(wrapData, error)

data class WrapDataProductCreatePallet(val doc:CreatePallet? = null,
                                       val product: Product? = null,
                                       val listItem:List<ItemPallet> = listOf(),
                                       var infoListBoxWrap: InfoPalletListBoxWrap? = null)


data class ItemPallet(
    val index:Int? = null,
    val number:Int? = null,
    val pallet: Pallet? = null,
    var infoListBoxWrap: InfoPalletListBoxWrap? = null
)