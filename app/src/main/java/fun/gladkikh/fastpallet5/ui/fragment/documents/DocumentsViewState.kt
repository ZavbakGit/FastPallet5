package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.domain.entity.ItemDocument
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class DocumentsViewState(val documents: List<ItemDocument>? = null, error: Throwable? = null):
    BaseViewState<List<ItemDocument>?>(documents, error)