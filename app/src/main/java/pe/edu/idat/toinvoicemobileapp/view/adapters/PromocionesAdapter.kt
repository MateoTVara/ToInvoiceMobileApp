package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.idat.toinvoicemobileapp.databinding.ItemPromocionesBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.PromocionesResponse

class PromocionesAdapter : RecyclerView.Adapter<PromocionesAdapter.ViewHolder>() {

    private val promocionesResponseList = ArrayList<PromocionesResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPromocionesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promocionesResponse = promocionesResponseList[position]

        holder.binding.tvpromodes.text = promocionesResponse.despromo
        holder.binding.tvfchainicio.text = promocionesResponse.fchaini
        holder.binding.tvfchafin.text = promocionesResponse.fchafin
        Glide.with(holder.binding.root)
            .load(promocionesResponse.imagen)
            .into(holder.binding.ivpromocion)
    }

    override fun getItemCount(): Int {
        return promocionesResponseList.size
    }

    fun setPromociones(promociones: List<PromocionesResponse>) {
        promocionesResponseList.clear()
        promocionesResponseList.addAll(promociones)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemPromocionesBinding) :
        RecyclerView.ViewHolder(binding.root)
}