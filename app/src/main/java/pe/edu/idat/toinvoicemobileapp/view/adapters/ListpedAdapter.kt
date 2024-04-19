package pe.edu.idat.toinvoicemobileapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.toinvoicemobileapp.databinding.ItemListpedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpedResponse

class ListpedAdapter : RecyclerView.Adapter<ListpedAdapter.ViewHolder>() {

    private var listpedResponseList = mutableListOf<ListpedResponse>()
    private var listpedResponseListOriginal = mutableListOf<ListpedResponse>()
    private var onDeleteButtonClickListener: OnDeleteButtonClickListener? = null
    private var onEditButtonClickListener: OnEditButtonClickListener? = null
    private var onEnviarButtonClickListener: OnEnviarButtonClickListener? = null


    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(idPed: Int)
    }

    fun setOnDeleteButtonClickListener(listener: OnDeleteButtonClickListener) {
        onDeleteButtonClickListener = listener
    }

    interface OnEditButtonClickListener {
        fun onEditButtonClick(idPed: Int)
    }

    fun setOnEditButtonClickListener(listener: OnEditButtonClickListener) {
        onEditButtonClickListener = listener
    }

    interface OnEnviarButtonClickListener {
        fun onEnviarButtonClick(idPed: Int)
    }

    fun setOnEnviarButtonClickListener(listener: OnEnviarButtonClickListener) {
        onEnviarButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListpedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listpedResponse = listpedResponseList[position]

        with(holder.binding) {
            tvid.text = listpedResponse.id.toString()
            tvrazonsocial.text = listpedResponse.denominacion
            tvdocumento.text = listpedResponse.tipoDeComprobante
            tvfchareparto.text = listpedResponse.fechaDeEmision

            btndelete.setOnClickListener {
                onDeleteButtonClickListener?.onDeleteButtonClick(listpedResponse.id)
            }

            btnedit.setOnClickListener {
                onEditButtonClickListener?.onEditButtonClick(listpedResponse.id)
            }

            btnenviar.setOnClickListener{
                onEnviarButtonClickListener?.onEnviarButtonClick(listpedResponse.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return listpedResponseList.size
    }

    fun setPedidos(pedidos: List<ListpedResponse>) {
        listpedResponseList.clear()
        listpedResponseList.addAll(pedidos)
        notifyDataSetChanged()
        listpedResponseListOriginal.clear()
        listpedResponseListOriginal.addAll(pedidos)
    }

    fun filtrarPedidos(filtro: String?) {
        if (filtro.isNullOrEmpty()) {
            listpedResponseList.clear()
            listpedResponseList.addAll(listpedResponseListOriginal)
        } else {
            val busquedaPedido = listpedResponseList.filter { p ->
                p.denominacion?.toLowerCase()?.contains(
                    filtro.toLowerCase()
                ) ?: false
            }
            listpedResponseList.clear()
            listpedResponseList.addAll(busquedaPedido)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemListpedBinding) : RecyclerView.ViewHolder(binding.root)
}