package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListusuResponse

class UsuarioAutoCompleteAdapter(context: Context, listusuResponses: List<ListusuResponse>) :
    ArrayAdapter<ListusuResponse>(context, android.R.layout.simple_dropdown_item_1line, listusuResponses) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getView(position, convertView, parent) as TextView
        val item = getItem(position)
        item?.let {
            textView.text = it.nombre
        }
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getDropDownView(position, convertView, parent) as TextView
        val item = getItem(position)
        item?.let {
            textView.text = it.nombre
        }
        return textView
    }
}