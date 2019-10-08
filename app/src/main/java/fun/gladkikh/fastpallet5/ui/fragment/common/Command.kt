package `fun`.gladkikh.fastpallet5.ui.fragment.common

sealed class Command {
    object Close : Command()
    data class OpenForm(val data: Any? = null) : Command()
    data class ConfirmDialog(val message: String, val data: Any? = null) : Command()
}


