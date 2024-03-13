package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentListcliBinding
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListcliAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListpedAdapter
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListcliViewModel

class ListcliFragment : Fragment(), SearchView.OnQueryTextListener, View.OnClickListener {

    private lateinit var binding: FragmentListcliBinding
    private lateinit var listcliViewModel: ListcliViewModel
    private var listcliAdapter = ListcliAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListcliBinding.inflate(
            inflater, container, false
        )
        listcliViewModel = ViewModelProvider(requireActivity()).get(ListcliViewModel::class.java)
        binding.rvliscli.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvliscli.adapter = listcliAdapter
        listcliViewModel.listarClientes()
        listcliViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listcliResponses ->
                listcliAdapter.setClientes(listcliResponses)
            }
        )
        binding.svliscli.setOnQueryTextListener(this)
        return binding.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        listcliAdapter.filtrarClientes(newText)
        return false
    }

    override fun onClick(v: View?) {
        listcliViewModel.listarClientes()
        listcliViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listcliResponses ->
                listcliAdapter.setClientes(listcliResponses)
            }
        )
    }

}