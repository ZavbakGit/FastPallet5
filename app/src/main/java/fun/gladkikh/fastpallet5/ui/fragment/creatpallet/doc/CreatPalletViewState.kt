package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class CreatPalletViewState(val document: CreatePallet? = null, error: Throwable? = null):
    BaseViewState<CreatePallet?>(document, error)