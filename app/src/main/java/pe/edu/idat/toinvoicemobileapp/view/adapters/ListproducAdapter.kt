package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.toinvoicemobileapp.databinding.ItemListproducBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListproResponse

class ListproducAdapter : RecyclerView.Adapter<ListproducAdapter.ViewHolder>() {

    private var listproResponseList = mutableListOf<ListproResponse>()
    private var listproResponseListOriginal = mutableListOf<ListproResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListproducBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListproducAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listproResponse = listproResponseList[position]

        with(holder.binding) {
            tvdesproduc.text = listproResponse.descripcion
            tvprecio.text = listproResponse.precioUnitario.toString()
            tvuniproduc.text = listproResponse.unidadDeMedida
        }
    }

    override fun getItemCount(): Int {
        return listproResponseList.size
    }

    fun setProductos(productos: List<ListproResponse>) {
        listproResponseList.clear()
        listproResponseList.addAll(productos)
        notifyDataSetChanged()
        listproResponseListOriginal.clear()
        listproResponseListOriginal.addAll(productos)
    }

    fun filtrarProductos(filtro: String?) {
        if (filtro.isNullOrEmpty()) {
            listproResponseList.clear()
            listproResponseList.addAll(listproResponseListOriginal)
        } else {
            val busquedaProducto = listproResponseList.filter { p ->
                p.descripcion?.toLowerCase()?.contains(
                    filtro.toLowerCase()
                ) ?: false
            }
            listproResponseList.clear()
            listproResponseList.addAll(busquedaProducto)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemListproducBinding) : RecyclerView.ViewHolder(binding.root)
}