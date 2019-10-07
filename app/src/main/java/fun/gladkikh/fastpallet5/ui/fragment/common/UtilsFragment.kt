package `fun`.gladkikh.fastpallet5.ui.fragment.common

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun startConfirmDialog(context: Context, message: String, positiveFun: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle("Вы уверены!")
        .setMessage(message)
        .setNegativeButton(
            android.R.string.cancel,
            null
        ) // dismisses by default
        .setPositiveButton("Да") { dialog, which ->
            positiveFun()
        }
        .show()
}