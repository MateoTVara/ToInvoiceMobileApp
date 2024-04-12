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
import pe.edu.idat.toinvoicemobileapp.view.adapters.ClienteAutoCompleteAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListdetailsAdapter
import pe.edu.idat.toinvoicemobileapp.view.adapters.ProductoAutoCompleteAdapter
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
    private lateinit var ptrucdni: EditText
    private lateinit var ptdireccion: EditText
    private lateinit var ptserie: EditText
    private lateinit var ptnumero: EditText
    private lateinit var ptdescripcionproducto: AutoCompleteTextView
    private lateinit var ptidproduc: EditText
    private lateinit var ptunidadproducto: EditText
    private lateinit var ptprecioproducto: EditText
    private lateinit var ptcantidadproducto: EditText
    private lateinit var btnagregardetalle: Button
    private lateinit var btnactualizarlistado: Button
    private lateinit var btnguardar: Button
    private lateinit var ptfchareparto: EditText
    private lateinit var btncancelar: Button
    private lateinit var ptidped: EditText
    private lateinit var tvtotal: TextView
    private lateinit var spinnerTipoDocumento: Spinner

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
        ptrucdni = binding.ptrucdni
        ptdireccion = binding.ptdireccion
        ptserie = binding.ptserie
        ptnumero = binding.ptnumero
        ptdescripcionproducto = binding.ptdescripcionproducto
        ptidproduc = binding.ptidproduc
        ptunidadproducto = binding.ptunidadproducto
        ptprecioproducto = binding.ptprecioproducto
        ptcantidadproducto = binding.ptcantidadproducto
        btnagregardetalle = binding.btnagregardetalle
        btnactualizarlistado = binding.btnactualizarlistado
        btnguardar = binding.btnguardar
        spinnerTipoDocumento = binding.spinnerTipoDocumento
        ptfchareparto = binding.ptfchaemision
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
        //setupViewsUsuarios()
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
                ptrucdni.setText(clienteSeleccionado.numeroDocumento)
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
                ptidproduc.setText(productoSeleccionado.codigo.toString())
                ptunidadproducto.setText(productoSeleccionado.unidadDeMedida)
                ptprecioproducto.setText(productoSeleccionado.precioUnitario.toString())
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

    private fun mostrarSugerenciasDeProductos(sugerencias: List<ListproResponse>) {
        val adapter = ProductoAutoCompleteAdapter(requireContext(), sugerencias)
        ptdescripcionproducto.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    /*private fun setupViewsUsuarios() {
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
    }*/

    /*private fun mostrarSugerenciasDeVendedores(sugerencias: List<ListusuResponse>) {
        val adapter = UsuarioAutoCompleteAdapter(requireContext(), sugerencias)
        ptvendedor.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }*/

    private fun setupAgregarDetalleButton() {
        btnagregardetalle?.setOnClickListener {
            val idproducText = ptidproduc.text.toString().trim()
            val cantidadText = ptcantidadproducto.text.toString().trim()
            if (!idproducText.isEmpty() && !cantidadText.isEmpty()) {
                try {
                    val idproduc = idproducText.toInt()
                    val cantidad = cantidadText.toInt()

                    val regisdetalleRequest = RegisdetalleRequest()
                    regisdetalleRequest.orderId = getArguments()?.getInt("orderId") ?: 0 // Establecer idped
                    regisdetalleRequest.codigo = idproduc
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
            val tipoComprobante = spinnerTipoDocumento.selectedItem.toString().trim().toUpperCase(Locale.getDefault())
            val rucdni = ptrucdni.text.toString().trim()
            val fchareparto = ptfchareparto.text.toString().trim()
            val serieText = ptserie.text.toString().trim()
            val numeroText = ptnumero.text.toString().trim()
            if (!tipoComprobante.isEmpty() && !rucdni.isEmpty() && !fchareparto.isEmpty()) {
                try {
                    val numeroVal = numeroText.toInt()

                    val regispedRequest = RegispedRequest()
                    regispedRequest.serie = serieText
                    regispedRequest.numero = numeroVal
                    regispedRequest.tipoDeComprobante = tipoComprobante
                    regispedRequest.numeroDocumento = rucdni
                    regispedRequest.fechaDeEmision = fchareparto

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
        ptrazonsocial.text.clear()
        ptserie.text.clear()
        ptrucdni.text.clear()
        ptdireccion.text.clear()
        ptfchareparto.text.clear()
        ptidped.text.clear()
    }

    private fun onclickActualizarListado() {
        btnactualizarlistado.setOnClickListener {
            listdetalleViewModel.listarDetallesNoAsignados()
        }
    }

    private fun actualizarVistaConDetalles(listpeddetailedResponse: ListpeddetailedResponse) {
        val tipoDocumento = listpeddetailedResponse.tipoDeComprobante

        // Seleccionar la opción correspondiente en el Spinner
        val spinnerPosition = when (tipoDocumento) {
            "FACTURA" -> 1 // El índice 1 corresponde a FACTURA en el array
            "BOLETA" -> 0 // El índice 0 corresponde a BOLETA en el array
            else -> 0 // Valor predeterminado si el tipo de documento no coincide con ninguno de los esperados
        }

        ptidped.setText(listpeddetailedResponse.id.toString())
        ptserie.setText(listpeddetailedResponse.serie)
        ptnumero.setText(listpeddetailedResponse.numero.toString())
        spinnerTipoDocumento.setSelection(spinnerPosition)
        ptfchareparto.setText(listpeddetailedResponse.fechaDeEmision)
        ptrucdni.setText(listpeddetailedResponse.numeroDocumento)
        ptdireccion.setText(listpeddetailedResponse.direccion)
    }

    private fun actualizarTotal(detalles: List<ListdetalleResponse>) {
        var total = 0.0
        for (detalle in detalles) {
            total += detalle.total
        }
        tvtotal.text = String.format(Locale.getDefault(), "Total: $%.2f", total)
    }

    private fun limpiarCampos() {
        ptrazonsocial.text.clear()
        ptserie.text.clear()
        ptnumero.text.clear()
        ptrucdni.text.clear()
        ptdireccion.text.clear()
        ptdescripcionproducto.text.clear()
        ptidproduc.text.clear()
        ptunidadproducto.text.clear()
        ptprecioproducto.text.clear()
        ptcantidadproducto.text.clear()
        spinnerTipoDocumento.setSelection(0)
        ptfchareparto.text.clear()
        ptidped.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        creapedViewModel.eliminarDetallesNoAsignados()
        limpiarPedidosCampos()
        limpiarDetallesCampos()
    }
}
