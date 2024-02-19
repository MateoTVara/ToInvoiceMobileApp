package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.toinvoicemobileapp.databinding.ItemDetallepedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListdetalleResponse

class ListdetailsAdapter : RecyclerView.Adapter<ListdetailsAdapter.ViewHolder>() {

    private var listdetalleResponseList: MutableList<ListdetalleResponse> = ArrayList()
    private var onDeleteButtonClickListener: OnDeleteButtonClickListener? = null

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(idDetalle: Int)
    }

    fun setOnDeleteButtonClickListener(listener: OnDeleteButtonClickListener) {
        onDeleteButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetallepedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listdetalleResponse = listdetalleResponseList[position]
        with(holder.binding) {
            tviddetalle.text = listdetalleResponse.iddetalle.toString()
            tvdescripcion.text = listdetalleResponse.desproduc
            tvunidad.text = listdetalleResponse.uniproduc
            tvcantidad.text = listdetalleResponse.cantidad.toString()
            tvimporte.text = listdetalleResponse.importe.toString()
            btnborrardetalle.setOnClickListener {
                onDeleteButtonClickListener?.onDeleteButtonClick(listdetalleResponse.iddetalle)
            }
        }
    }

    override fun getItemCount(): Int {
        return listdetalleResponseList.size
    }

    fun setDetalles(detalles: List<ListdetalleResponse>) {
        val previousSize = listdetalleResponseList.size
        listdetalleResponseList.clear()
        listdetalleResponseList.addAll(detalles)
        val newSize = listdetalleResponseList.size

        // Notifica solo las inserciones, si las hay
        if (newSize > previousSize) {
            notifyItemRangeInserted(previousSize, newSize - previousSize)
        } else {
            notifyDataSetChanged()
        }
    }

    fun calcularSumaImportes(): Double {
        var suma = 0.0
        for (detalle in listdetalleResponseList) {
            suma += detalle.importe
        }
        return suma
    }

    inner class ViewHolder(val binding: ItemDetallepedBinding) : RecyclerView.ViewHolder(binding.root)
}