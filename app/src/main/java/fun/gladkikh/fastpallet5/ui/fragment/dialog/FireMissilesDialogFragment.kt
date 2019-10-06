package `fun`.gladkikh.fastpallet5.ui.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FireMissilesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("Мессаже")
            .setPositiveButton("Ок") { dialog, id ->
                // FIRE ZE MISSILES!
            }
            .setNegativeButton("Но") { dialog, id ->
                // User cancelled the dialog
            }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
