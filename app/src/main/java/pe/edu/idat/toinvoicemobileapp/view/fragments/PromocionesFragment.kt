package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentPromocionesBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.PromocionesResponse
import pe.edu.idat.toinvoicemobileapp.view.adapters.PromocionesAdapter
import pe.edu.idat.toinvoicemobileapp.viewmodel.PromocionesViewModel

class PromocionesFragment : Fragment() {

    private lateinit var binding: FragmentPromocionesBinding
    private lateinit var promocionesViewModel: PromocionesViewModel
    private val promocionesAdapter = PromocionesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromocionesBinding.inflate(inflater, container, false)
        promocionesViewModel = ViewModelProvider(requireActivity()).get(PromocionesViewModel::class.java)
        binding.rvpromociones.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvpromociones.adapter = promocionesAdapter
        promocionesViewModel.listarPromociones()
        promocionesViewModel.listpromocionesMutableLiveData.observe(viewLifecycleOwner,
            Observer<List<PromocionesResponse>> { promocionesResponses ->
                promocionesAdapter.setPromociones(promocionesResponses)
            })
        return binding.root
    }
}