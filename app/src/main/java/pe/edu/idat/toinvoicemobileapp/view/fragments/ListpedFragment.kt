package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentListpedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpedResponse
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListpedAdapter
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListpedViewModel

class ListpedFragment : Fragment(), SearchView.OnQueryTextListener,
    ListpedAdapter.OnEditButtonClickListener, View.OnClickListener {

    private lateinit var binding: FragmentListpedBinding
    private lateinit var listpedViewModel: ListpedViewModel
    private val listpedAdapter = ListpedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListpedBinding.inflate(
            inflater, container, false
        )
        binding.btnrefrescar.setOnClickListener(this)
        listpedViewModel = ViewModelProvider(requireActivity()).get(ListpedViewModel::class.java)
        binding.rvlisped.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvlisped.adapter = listpedAdapter
        listpedViewModel.listarPedidos()
        listpedViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listpedResponses ->
                listpedAdapter.setPedidos(listpedResponses)
            }
        )

        binding.svlisped.setOnQueryTextListener(this)
        listpedAdapter.setOnEditButtonClickListener(this)

        borrarPedido()
        return binding.root
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        listpedAdapter.filtrarPedidos(newText)
        return false
    }

    private fun borrarPedido() {
        listpedAdapter.setOnDeleteButtonClickListener(object : ListpedAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClick(idPed: Int) {
                listpedViewModel.eliminarPedido(idPed)
                listpedViewModel.listarPedidos()
            }
        })
    }


    override fun onEditButtonClick(idPed: Int) {
        val navController = Navigation.findNavController(requireView())
        val bundle = Bundle()
        bundle.putInt("idped", idPed)
        navController.navigate(R.id.action_listpedFragment_to_modifypedFragment, bundle)
    }

    override fun onClick(v: View) {
        listpedViewModel.listarPedidos()
        listpedViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listpedResponses ->
                listpedAdapter.setPedidos(listpedResponses)
            }
        )
    }
}