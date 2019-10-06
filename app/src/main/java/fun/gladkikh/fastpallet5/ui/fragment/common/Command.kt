package `fun`.gladkikh.fastpallet5.ui.fragment.common

sealed class Command{
    class Close : Command()
    class Confirm(val message:String):Command()
}

