package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentCreapedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegisdetalleRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegispedRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListcliResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListdetalleResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpeddetailedResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListproResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListusuResponse
import pe.edu.idat.toinvoicemobileapp.view.adapters.ClienteAutoCompleteAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListdetailsAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.ProductoAutoCompleteAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.UsuarioAutoCompleteAdapter
import pe.edu.idat.toinvoicemobileapp.viewmodel.CreapedViewModel
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListdetalleViewModel
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListpedViewModel
import java.util.*

class CreapedFragment : Fragment() {

    private lateinit var binding: FragmentCreapedBinding
    private lateinit var listdetalleViewModel: ListdetalleViewModel
    private lateinit var listpedViewModel: ListpedViewModel
    private lateinit var creapedViewModel: CreapedViewModel
    private lateinit var listdetailsAdapter: ListdetailsAdapter

    private lateinit var ptrazonsocial: AutoCompleteTextView
    private lateinit var ptidcli: EditText
    private lateinit var ptrucdni: EditText
    private lateinit var ptdireccion: EditText
    private lateinit var ptdescripcionproducto: AutoCompleteTextView
    private lateinit var ptidproduc: EditText
    private lateinit var ptunidadproducto: EditText
    private lateinit var ptprecioproducto: EditText
    private lateinit var ptcantidadproducto: EditText
    private lateinit var btnagregardetalle: Button
    private lateinit var btnactualizarlistado: Button
    private lateinit var btnguardar: Button
    private lateinit var ptdocumento: EditText
    private lateinit var ptfchareparto: EditText
    private lateinit var ptidusu: EditText
    private lateinit var ptvendedor: AutoCompleteTextView
    private lateinit var btncancelar: Button
    private lateinit var ptidped: EditText
    private lateinit var tvtotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreapedBinding.inflate(inflater, container, false)
        listdetailsAdapter = ListdetailsAdapter()
        binding.rvdetallepedido.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvdetallepedido.adapter = listdetailsAdapter
        listdetalleViewModel = ViewModelProvider(requireActivity()).get(ListdetalleViewModel::class.java)
        listpedViewModel = ViewModelProvider(requireActivity()).get(ListpedViewModel::class.java)

        listdetalleViewModel.getListLiveData().observe(viewLifecycleOwner, Observer { listdetalleResponses ->
            listdetailsAdapter.setDetalles(listdetalleResponses)
            actualizarTotal(listdetalleResponses)
        })

        ptrazonsocial = binding.ptrazonsocial
        ptidcli = binding.ptidcli
        ptrucdni = binding.ptrucdni
        ptdireccion = binding.ptdireccion
        ptdescripcionproducto = binding.ptdescripcionproducto
        ptidproduc = binding.ptidproduc
        ptunidadproducto = binding.ptunidadproducto
        ptprecioproducto = binding.ptprecioproducto
        ptcantidadproducto = binding.ptcantidadproducto
        btnagregardetalle = binding.btnagregardetalle
        btnactualizarlistado = binding.btnactualizarlistado
        btnguardar = binding.btnguardar
        ptdocumento = binding.ptdocumento
        ptfchareparto = binding.ptfchareparto
        ptidusu = binding.ptidusu
        ptvendedor = binding.ptvendedor
        btncancelar = binding.btncancelar
        ptidped = binding.ptidped
        tvtotal = binding.tvtotal

        creapedViewModel = ViewModelProvider(this).get(CreapedViewModel::class.java)

        arguments?.let {
            if (it.containsKey("idped")) {
                val idped = it.getInt("idped")
                listpedViewModel.buscarPedidoDetallado(idped)
            }
        }

        listpedViewModel.listpeddetailedResponseMutableLiveData.observe(viewLifecycleOwner, Observer { listpeddetailedResponse ->
            if (listpeddetailedResponse != null) {
                actualizarVistaConDetalles(listpeddetailedResponse)
            }
        })

        listdetalleViewModel.listarDetallesNoAsignados()
        setupViewsCliente()
        setupViewsProducto()
        setupViewsUsuarios()
        setupAgregarDetalleButton()
        onclickActualizarListado()
        borrarDetalle()
        setupGuardarPedidoButton()
        cancelarPedido()
        limpiarCampos()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        limpiarCampos()
    }

    private fun setupViewsCliente() {
        ptrazonsocial.setOnItemClickListener { parent, view, position, id ->
            val clienteSeleccionado = parent.getItemAtPosition(position) as ListcliResponse?
            if (clienteSeleccionado != null) {
                ptidcli.setText(clienteSeleccionado.idcli.toString())
                ptrucdni.setText(clienteSeleccionado.rucdni)
                ptdireccion.setText(clienteSeleccionado.direccion)
            }
        }

        ptrazonsocial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creapedViewModel.sugerenciasPorRazonSocial(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        creapedViewModel.sugerenciasLiveData.observe(viewLifecycleOwner) { sugerencias ->
            mostrarSugerenciasDeClientes(sugerencias)
        }
    }

    private fun mostrarSugerenciasDeClientes(sugerencias: List<ListcliResponse>) {
        val adapter = ClienteAutoCompleteAdapter(requireContext(), sugerencias)
        ptrazonsocial.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewsProducto() {
        ptdescripcionproducto.setOnItemClickListener { parent, view, position, id ->
            val productoSeleccionado = parent.getItemAtPosition(position) as ListproResponse?
            if (productoSeleccionado != null) {
                ptidproduc.setText(productoSeleccionado.idproduc.toString())
                ptunidadproducto.setText(productoSeleccionado.uniproduc)
                ptprecioproducto.setText(productoSeleccionado.precio.toString())
            }
        }

        ptdescripcionproducto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creapedViewModel.sugerenciasPorDescripcion(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        creapedViewModel.sugerenciasproductosLiveData.observe(viewLifecycleOwner) { sugerencias ->
            mostrarSugerenciasDeProductos(sugerencias)
        }
    }

    private fun setupViewsUsuarios() {
        ptvendedor.setOnItemClickListener { parent, view, position, id ->
            val usuarioSeleccionado = parent.getItemAtPosition(position) as ListusuResponse?
            if (usuarioSeleccionado != null) {
                ptidusu.setText(usuarioSeleccionado.idusu.toString())
            }
        }

        ptvendedor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creapedViewModel.sugerenciasPorNombre(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        creapedViewModel.sugerenciasusuariosLiveData.observe(viewLifecycleOwner) { sugerencias ->
            mostrarSugerenciasDeVendedores(sugerencias)
        }
    }

    private fun mostrarSugerenciasDeProductos(sugerencias: List<ListproResponse>) {
        val adapter = ProductoAutoCompleteAdapter(requireContext(), sugerencias)
        ptdescripcionproducto.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun mostrarSugerenciasDeVendedores(sugerencias: List<ListusuResponse>) {
        val adapter = UsuarioAutoCompleteAdapter(requireContext(), sugerencias)
        ptvendedor.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun setupAgregarDetalleButton() {
        btnagregardetalle?.setOnClickListener {
            val idproducText = ptidproduc.text.toString().trim()
            val cantidadText = ptcantidadproducto.text.toString().trim()
            if (!idproducText.isEmpty() && !cantidadText.isEmpty()) {
                try {
                    val idproduc = idproducText.toInt()
                    val cantidad = cantidadText.toInt()

                    val regisdetalleRequest = RegisdetalleRequest()
                    regisdetalleRequest.idped = getArguments()?.getInt("idped") ?: 0 // Establecer idped
                    regisdetalleRequest.idproduc = idproduc
                    regisdetalleRequest.cantidad = cantidad

                    creapedViewModel.registrarDetalleParcial(regisdetalleRequest)
                    listdetalleViewModel.listarDetallesNoAsignados()
                    limpiarDetallesCampos()
                } catch (e: NumberFormatException) {
                    Log.e("CreapedFragment", "Error al convertir cadena a número", e)
                }
            }
        }
    }


    private fun borrarDetalle() {
        listdetailsAdapter.setOnDeleteButtonClickListener(object : ListdetailsAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClick(idDetalle: Int) {
                creapedViewModel.eliminarDetalle(idDetalle)
                listdetalleViewModel.listarDetallesNoAsignados()
            }
        })
    }

    private fun limpiarDetallesCampos() {
        ptidproduc.text.clear()
        ptdescripcionproducto.text.clear()
        ptunidadproducto.text.clear()
        ptcantidadproducto.text.clear()
        ptprecioproducto.text.clear()
    }

    private fun setupGuardarPedidoButton() {
        btnguardar?.setOnClickListener {
            val documento = ptdocumento.text.toString().trim().toUpperCase(Locale.getDefault())
            val idcliText = ptidcli.text.toString().trim()
            val fchareparto = ptfchareparto.text.toString().trim()
            val idusuText = ptidusu.text.toString().trim()
            if (!documento.isEmpty() && !idcliText.isEmpty() && !fchareparto.isEmpty() && !idusuText.isEmpty()) {
                try {
                    val idcli = idcliText.toInt()
                    val idusu = idusuText.toInt()

                    val regispedRequest = RegispedRequest()
                    regispedRequest.documento = documento
                    regispedRequest.idcli = idcli
                    regispedRequest.fchareparto = fchareparto
                    regispedRequest.idusu = idusu

                    creapedViewModel.registrarPedido(regispedRequest)
                    limpiarPedidosCampos()
                    listdetalleViewModel.listarDetallesNoAsignados()
                } catch (e: NumberFormatException) {
                    Log.e("CreapedFragment", "Error al convertir cadena a número", e)
                }
            }
        }
    }


    private fun cancelarPedido() {
        btncancelar.setOnClickListener {
            creapedViewModel.eliminarDetallesNoAsignados()
            limpiarPedidosCampos()
            limpiarDetallesCampos()
            listdetalleViewModel.listarDetallesNoAsignados()
        }
    }

    private fun limpiarPedidosCampos() {
        ptdocumento.text.clear()
        ptrazonsocial.text.clear()
        ptidcli.text.clear()
        ptrucdni.text.clear()
        ptdireccion.text.clear()
        ptfchareparto.text.clear()
        ptidusu.text.clear()
        ptvendedor.text.clear()
        ptidped.text.clear()
    }

    private fun onclickActualizarListado() {
        btnactualizarlistado.setOnClickListener {
            listdetalleViewModel.listarDetallesNoAsignados()
        }
    }

    private fun actualizarVistaConDetalles(listpeddetailedResponse: ListpeddetailedResponse) {
        ptidped.setText(listpeddetailedResponse.idped.toString())
        ptidcli.setText(listpeddetailedResponse.idcli.toString())
        ptidusu.setText(listpeddetailedResponse.idusu.toString())
        ptdocumento.setText(listpeddetailedResponse.documento)
        ptrazonsocial.setText(listpeddetailedResponse.razonsocial)
        ptvendedor.setText(listpeddetailedResponse.nombre)
        ptfchareparto.setText(listpeddetailedResponse.fchareparto)
        ptrucdni.setText(listpeddetailedResponse.rucdni)
        ptdireccion.setText(listpeddetailedResponse.direccion)
    }

    private fun actualizarTotal(detalles: List<ListdetalleResponse>) {
        var total = 0.0
        for (detalle in detalles) {
            total += detalle.importe
        }
        tvtotal.text = String.format(Locale.getDefault(), "Total: $%.2f", total)
    }

    private fun limpiarCampos() {
        ptrazonsocial.text.clear()
        ptidcli.text.clear()
        ptrucdni.text.clear()
        ptdireccion.text.clear()
        ptdescripcionproducto.text.clear()
        ptidproduc.text.clear()
        ptunidadproducto.text.clear()
        ptprecioproducto.text.clear()
        ptcantidadproducto.text.clear()
        ptdocumento.text.clear()
        ptfchareparto.text.clear()
        ptidusu.text.clear()
        ptvendedor.text.clear()
        ptidped.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        creapedViewModel.eliminarDetallesNoAsignados()
        limpiarPedidosCampos()
        limpiarDetallesCampos()
    }
}
