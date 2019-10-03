package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.ui.base.BaseViewState


class DocumentsViewState(val documents: List<Document>? = null, error: Throwable? = null):
    BaseViewState<List<Document>?>(documents, error)