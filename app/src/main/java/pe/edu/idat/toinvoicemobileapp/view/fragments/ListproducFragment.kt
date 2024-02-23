package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentListproducBinding
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListproducViewModel
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListproducAdapter

class ListproducFragment : Fragment(), SearchView.OnQueryTextListener, View.OnClickListener {

    private lateinit var binding: FragmentListproducBinding
    private lateinit var listproducViewModel: ListproducViewModel
    private val listproducAdapter = ListproducAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListproducBinding.inflate(
            inflater, container, false
        )
        listproducViewModel = ViewModelProvider(requireActivity()).get(ListproducViewModel::class.java)
        binding.rvlisproduc.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvlisproduc.adapter = listproducAdapter
        listproducViewModel.listarProductos()
        listproducViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listproResponse ->
                listproducAdapter.setProductos(listproResponse)
            }
        )

        binding.svlisproduc.setOnQueryTextListener(this)
        return binding.root
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        listproducAdapter.filtrarProductos(newText)
        return false
    }

    override fun onClick(v: View) {
        listproducViewModel.listarProductos()
        listproducViewModel.listMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { listproResponse ->
                listproducAdapter.setProductos(listproResponse)
            }
        )
    }
}
