package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.toinvoicemobileapp.databinding.*
import pe.edu.idat.toinvoicemobileapp.retrofit.response.*

class ListcliAdapter: RecyclerView.Adapter<ListcliAdapter.ViewHolder>()  {

    private var listcliResponseList = mutableListOf<ListcliResponse>()
    private var listcliResponseListOriginal = mutableListOf<ListcliResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListcliBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListcliAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listcliResponseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listcliResponse = listcliResponseList[position]

        with(holder.binding) {
            tvclirazonsocial.text=listcliResponse.razonsocial.toString()
            tvclirucdni.text=listcliResponse.rucdni.toString()
            tvclidireccion.text=listcliResponse.direccion.toString()

        }
    }

    fun setClientes(clientes: List<ListcliResponse>) {
        listcliResponseList.clear()
        listcliResponseList.addAll(clientes)
        notifyDataSetChanged()
        listcliResponseListOriginal.clear()
        listcliResponseListOriginal.addAll(clientes)
    }

    fun filtrarClientes(filtro: String?) {
        if (filtro.isNullOrEmpty()) {
            listcliResponseList.clear()
            listcliResponseList.addAll(listcliResponseListOriginal)
        } else {
            val busquedaCliente= listcliResponseList.filter { c ->
                c.razonsocial?.toLowerCase()?.contains(
                    filtro.toLowerCase()
                ) ?: false
            }
            listcliResponseList.clear()
            listcliResponseList.addAll(busquedaCliente)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemListcliBinding) : RecyclerView.ViewHolder(binding.root)
}