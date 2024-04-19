package pe.edu.idat.toinvoicemobileapp.util

import android.app.AlertDialog
import android.content.Context

class ConfirmationDialog(context: Context, private val listener: ConfirmationListener) {

    interface ConfirmationListener {
        fun onConfirmed()
    }

    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("¿Estás seguro de enviar los datos del pedido?")
        builder.setPositiveButton("Sí") { _, _ ->
            listener.onConfirmed()
        }
        builder.setNegativeButton("No", null)
        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }
}
