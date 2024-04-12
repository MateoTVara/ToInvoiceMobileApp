package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentModifypedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.request.*
import pe.edu.idat.toinvoicemobileapp.retrofit.response.*
import pe.edu.idat.toinvoicemobileapp.view.adapters.*
import pe.edu.idat.toinvoicemobileapp.viewmodel.*
import java.util.*

class ModifypedFragment : Fragment() {

    private lateinit var binding: FragmentModifypedBinding
    private lateinit var listdetalleViewModel: ListdetalleViewModel
    private val listdetailsAdapter = ListdetailsAdapter()
    private lateinit var creapedViewModel: CreapedViewModel
    private lateinit var listpedViewModel: ListpedViewModel
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
        binding = FragmentModifypedBinding.inflate(inflater, container, false)
        listdetalleViewModel = ViewModelProvider(requireActivity()).get(ListdetalleViewModel::class.java)
        listpedViewModel = ViewModelProvider(requireActivity()).get(ListpedViewModel::class.java)
        binding.rvdetallepedido.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvdetallepedido.adapter = listdetailsAdapter
        listdetalleViewModel.getListLiveData().observe(
            viewLifecycleOwner,
            Observer<List<ListdetalleResponse>> { listdetalleResponses ->
                listdetailsAdapter.setDetalles(listdetalleResponses)
                actualizarSumaImportes()
            }
        )

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
                creapedViewModel.listarDetallesPorPedido(idped)
                listpedViewModel.buscarPedidoDetallado(idped)
            }
        }
        listpedViewModel.listpeddetailedResponseMutableLiveData.observe(viewLifecycleOwner,
            Observer { listpeddetailedResponse ->
                if (listpeddetailedResponse != null) {
                    actualizarVistaConDetalles(listpeddetailedResponse)
                    actualizarSumaImportes()
                }
            })
        creapedViewModel.detallesPorPedidoLiveData.observe(viewLifecycleOwner,
            Observer { listdetalleResponses -> listdetailsAdapter.setDetalles(listdetalleResponses) })
        setupViewsCliente()
        setupViewsProducto()
        setupViewsUsuarios()
        setupAgregarDetalleButton()
        borrarDetalle()
        setupModificarPedidoButton()
        setupActualizarListadoButton()
        actualizarSumaImportes()
        setupCancelarButton()
        return binding.root
    }

    private fun setupViewsCliente() {
        ptrazonsocial.setOnItemClickListener { parent, view, position, id ->
            val clienteSeleccionado = parent.getItemAtPosition(position) as ListcliResponse
            if (clienteSeleccionado != null) {
                ptidcli.setText(clienteSeleccionado.idcli.toString())
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

        creapedViewModel.sugerenciasLiveData.observe(viewLifecycleOwner, Observer { sugerencias ->
            mostrarSugerenciasDeClientes(sugerencias)
        })
    }

    private fun mostrarSugerenciasDeClientes(sugerencias: List<ListcliResponse>) {
        val adapter = ClienteAutoCompleteAdapter(requireContext(), sugerencias)
        ptrazonsocial.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewsProducto() {
        ptdescripcionproducto.setOnItemClickListener { parent, view, position, id ->
            val productoSeleccionado = parent.getItemAtPosition(position) as ListproResponse
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

        creapedViewModel.sugerenciasproductosLiveData.observe(viewLifecycleOwner, Observer { sugerencias ->
            mostrarSugerenciasDeProductos(sugerencias)
        })
    }

    private fun setupViewsUsuarios() {
        ptvendedor.setOnItemClickListener { parent, view, position, id ->
            val usuarioSeleccionado = parent.getItemAtPosition(position) as ListusuResponse
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

        creapedViewModel.sugerenciasusuariosLiveData.observe(viewLifecycleOwner, Observer { sugerencias ->
            mostrarSugerenciasDeVendedores(sugerencias)
        })
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

    private fun actualizarVistaConDetalles(listpeddetailedResponse: ListpeddetailedResponse) {
        ptidped.setText(listpeddetailedResponse.id.toString())
        ptidcli.setText(listpeddetailedResponse.idcli.toString())
        ptidusu.setText(listpeddetailedResponse.idusu.toString())
        ptdocumento.setText(listpeddetailedResponse.tipoDeComprobante)
        ptrazonsocial.setText(listpeddetailedResponse.denominacion)
        ptvendedor.setText(listpeddetailedResponse.nombre)
        ptfchareparto.setText(listpeddetailedResponse.fechaDeEmision)
        ptrucdni.setText(listpeddetailedResponse.numeroDocumento)
        ptdireccion.setText(listpeddetailedResponse.direccion)
        tvtotal.setText(listpeddetailedResponse.total.toString())
    }

    private fun setupAgregarDetalleButton() {
        btnagregardetalle.setOnClickListener {
            val idproducText = ptidproduc.text.toString().trim()
            val cantidadText = ptcantidadproducto.text.toString().trim()
            val idpedText = ptidped.text.toString().trim()

            if (!idproducText.isEmpty() && !cantidadText.isEmpty() && !idpedText.isEmpty()) {
                try {
                    val idproduc = idproducText.toInt()
                    val cantidad = cantidadText.toInt()
                    val idped = idpedText.toInt()

                    val regisdetalleRequest = RegisdetalleRequest().apply {
                        this.codigo = idproduc
                        this.cantidad = cantidad
                        this.orderId = idped
                    }

                    creapedViewModel.registrarDetalleParcialConIdped(regisdetalleRequest)

                    actualizarListadoDetalles()
                    limpiarDetallesCampos()
                    actualizarSumaImportes()
                } catch (e: NumberFormatException) {
                    Log.e("ModifypedFragment", "Error al convertir cadena a número", e)
                }
            }
        }
    }


    private fun limpiarDetallesCampos() {
        ptidproduc.text.clear()
        ptdescripcionproducto.text.clear()
        ptunidadproducto.text.clear()
        ptcantidadproducto.text.clear()
        ptprecioproducto.text.clear()
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

    private fun borrarDetalle() {
        listdetailsAdapter.setOnDeleteButtonClickListener(object : ListdetailsAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClick(idDetalle: Int) {
                val idpedText = ptidped.text.toString().trim()
                val idped = idpedText.toInt()
                // Llama al método para eliminar el detalle en el ViewModel
                creapedViewModel.eliminarDetalle(idDetalle)
                creapedViewModel.listarDetallesPorPedido(idped)
                actualizarSumaImportes()
            }
        })
    }

    private fun setupModificarPedidoButton() {
        btnguardar.setOnClickListener {
            val idpedText = ptidped.text.toString().trim()
            val documento = ptdocumento.text.toString().trim().toUpperCase(Locale.getDefault())
            val idcliText = ptidcli.text.toString().trim()
            val fchareparto = ptfchareparto.text.toString().trim()
            val idusuText = ptidusu.text.toString().trim()

            if (!documento.isEmpty() && !idcliText.isEmpty() && !fchareparto.isEmpty() && !idusuText.isEmpty()) {
                try {
                    val idped = idpedText.toInt()
                    val idcli = idcliText.toInt()
                    val idusu = idusuText.toInt()

                    val modifypedRequest = ModifypedRequest().apply {
                        this.id = idped
                        this.tipoDeComprobante = documento
                        this.numeroDocumento = idcli
                        this.fechaDeEmision = fchareparto
                        this.idusu = idusu
                    }

                    creapedViewModel.modificarPedido(idped, modifypedRequest)
                    regresarALaVistaAnterior()
                } catch (e: NumberFormatException) {
                    Log.e("ModifypedFragment", "Error al convertir cadena a número", e)
                }
            }
        }
    }

    private fun setupActualizarListadoButton() {
        btnactualizarlistado.setOnClickListener { actualizarListadoDetalles() }
    }

    private fun actualizarListadoDetalles() {
        val idpedText = ptidped.text.toString().trim()
        if (!idpedText.isEmpty()) {
            try {
                val idped = idpedText.toInt()
                creapedViewModel.listarDetallesPorPedido(idped)
                actualizarSumaImportes()
            } catch (e: NumberFormatException) {
                Log.e("ModifypedFragment", "Error al convertir cadena a número", e)
            }
        }
    }

    private fun actualizarSumaImportes() {
        val sumaImportes = listdetailsAdapter.calcularSumaImportes()
        tvtotal.text = String.format(Locale.getDefault(), "Total: %.2f", sumaImportes)
    }

    private fun setupCancelarButton() {
        btncancelar.setOnClickListener { regresarALaVistaAnterior() }
    }

    private fun regresarALaVistaAnterior() {
        parentFragmentManager.popBackStack()
    }
}
