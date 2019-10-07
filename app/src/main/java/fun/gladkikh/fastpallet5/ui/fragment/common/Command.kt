package `fun`.gladkikh.fastpallet5.ui.fragment.common

sealed class Command{
    class Close : Command()
    class OpenForm(val data:Any? = null) : Command()
    class ConfirmDialog(val message:String,val data:Any? = null):Command()
}

