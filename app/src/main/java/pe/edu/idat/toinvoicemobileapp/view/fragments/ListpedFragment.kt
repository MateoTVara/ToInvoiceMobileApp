package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentListpedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.api.DBApi
import pe.edu.idat.toinvoicemobileapp.retrofit.api.ENT.DocumentoCabeceraENT
import pe.edu.idat.toinvoicemobileapp.retrofit.api.ENT.DocumentoITEMSENT
import pe.edu.idat.toinvoicemobileapp.retrofit.api.GET.CabeceraGET
import pe.edu.idat.toinvoicemobileapp.retrofit.api.GET.ItemsGET
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpedResponse
import pe.edu.idat.toinvoicemobileapp.util.ConfirmationDialog
import pe.edu.idat.toinvoicemobileapp.view.adapters.ListpedAdapter
import pe.edu.idat.toinvoicemobileapp.viewmodel.ListpedViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class ListpedFragment : Fragment(), SearchView.OnQueryTextListener,
    ListpedAdapter.OnEditButtonClickListener, View.OnClickListener,
    ListpedAdapter.OnEnviarButtonClickListener{

    private lateinit var binding: FragmentListpedBinding
    private lateinit var listpedViewModel: ListpedViewModel
    private val listpedAdapter = ListpedAdapter()
    private var isObservingLiveData = false

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
        listpedAdapter.setOnEnviarButtonClickListener(this)

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


    override fun onEnviarButtonClick(idPed: Int) {

        // Antes de mostrar el cuadro de diálogo de confirmación, verificamos el estado enviado_a_sunat
        listpedViewModel.verificarEnvioSunat(idPed) { enviadoASunat ->

            if (enviadoASunat) {
                // El pedido ya ha sido enviado a SUNAT, mostrar un mensaje o realizar otra acción según lo necesites
                // Por ejemplo, mostrar un Toast indicando que el pedido ya ha sido enviado
                Toast.makeText(requireContext(), "El pedido ya ha sido enviado a SUNAT", Toast.LENGTH_SHORT).show()
            } else {
                // El pedido no ha sido enviado a SUNAT, proceder con el cuadro de diálogo de confirmación
                val confirmationDialog = ConfirmationDialog(requireContext(), object : ConfirmationDialog.ConfirmationListener {
                    override fun onConfirmed() {
                        listpedViewModel.cabeceraENTMutableLiveData.removeObservers(viewLifecycleOwner)
                        // Aquí iría el código para enviar los datos a través de DBApi
                        listpedViewModel.buscarPedidoDetalladoConDetalles(idPed)
                        listpedViewModel.cabeceraENTMutableLiveData.observe(viewLifecycleOwner, Observer { cabeceraGET ->
                            val documentoCabeceraEnt = transferirDatosCabecera(cabeceraGET)
                            documentoCabeceraEnt.items = cabeceraGET.items?.map { itemGET ->
                                transferirDatosItem(itemGET)
                            }

                            val documentoJson = Gson().toJson(documentoCabeceraEnt)

                            val token = "eyJhbGciOiJIUzI1NiJ9.IjIwYmUyMjIyYzAwMTQyMTc5MDQxZDA2OTNiMDU2NmUyMTI1M2ZjNzFjNGE4NDBkMGE3MjNlOTRjZjE1MmEzM2Mi.mXBrOrQ8bykTFePv8Kx6RQdmjsHl4X6fKm0EhzOSZjU" // Asegúrate de tener un token válido aquí
                            val dbApi = DBApi()
                            lifecycleScope.launch {
                                try {
                                    val response = dbApi.postafirmativo(documentoJson, token)

                                    if (response != null) {
                                        println("La solicitud fue exitosa: $response")
                                        // Solo marcamos como enviado a Sunat si la solicitud fue exitosa
                                        listpedViewModel.marcarComoEnviadoSunat(idPed)
                                    } else {
                                        println("Hubo un error al enviar la solicitud")
                                        Toast.makeText(requireContext(), "Hubo un error al enviar la solicitud", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    println("Error al enviar la solicitud: ${e.message}")
                                    Toast.makeText(requireContext(), "Error al enviar la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }

                })
                confirmationDialog.show()
            }
        }
    }


    private fun transferirDatosCabecera(cabeceraGET: CabeceraGET): DocumentoCabeceraENT {
        return DocumentoCabeceraENT().apply {
            tipo_de_comprobante = cabeceraGET.tipoDeComprobante
            serie = cabeceraGET.serie
            numero = cabeceraGET.numero
            sunat_transaction = cabeceraGET.sunatTransaction
            cliente_tipo_de_documento = cabeceraGET.tipoDocumento
            cliente_numero_de_documento = cabeceraGET.numeroDocumento
            cliente_denominacion = cabeceraGET.denominacion
            cliente_direccion = cabeceraGET.direccion
            cliente_email = cabeceraGET.email
            cliente_email_1 = cabeceraGET.email1
            cliente_email_2 = cabeceraGET.email2
            fecha_de_emision = cabeceraGET.fechaDeEmision
            fecha_de_vencimiento = cabeceraGET.fechaDeVencimiento
            moneda = cabeceraGET.moneda
            tipo_de_cambio = cabeceraGET.tipoDeCambio.toString()
            porcentaje_de_igv = cabeceraGET.porcentajeDeIgv
            descuento_global = cabeceraGET.descuentoGlobal
            total_descuento = cabeceraGET.totalDescuento
            total_anticipo = cabeceraGET.totalAnticipo
            total_gravada = cabeceraGET.totalGravada
            total_inafecta = cabeceraGET.totalInafecta
            total_exonerada = cabeceraGET.totalExonerada
            total_isc = cabeceraGET.totalIsc
            total_igv = cabeceraGET.totalIgv
            total_gratuita = cabeceraGET.totalGratuita
            total_otros_cargos = cabeceraGET.totalOtrosCargos
            total = cabeceraGET.total
            percepcion_tipo = cabeceraGET.percepcionTipo
            percepcion_base_imponible = cabeceraGET.percepcionBaseImponible
            total_percepcion = cabeceraGET.totalPercepcion
            total_incluido_percepcion = cabeceraGET.totalIncluidoPercepcion
            retencion_tipo = cabeceraGET.retencionTipo
            retencion_base_imponible = cabeceraGET.retencionBaseImponible
            total_retencion = cabeceraGET.totalRetencion
            total_impuestos_bolsas = cabeceraGET.totalImpuestosBolsas
            detraccion = cabeceraGET.detraccion
            observaciones = cabeceraGET.observaciones
            documento_que_se_modifica_tipo = cabeceraGET.documentoQueSeModificaTipo
            documento_que_se_modifica_serie = cabeceraGET.documentoQueSeModificaSerie
            documento_que_se_modifica_numero = cabeceraGET.documentoQueSeModificaNumero
            tipo_de_nota_de_credito = cabeceraGET.tipoDeNotaDeCredito
            tipo_de_nota_de_debito = cabeceraGET.tipoDeNotaDeDebito
            enviar_automaticamente_a_la_sunat = cabeceraGET.enviarAutomaticamenteALaSunat
            enviar_automaticamente_al_cliente = cabeceraGET.enviarAutomaticamenteAlCliente
            condiciones_de_pago = cabeceraGET.condicionesDePago
            medio_de_pago = cabeceraGET.medioDePago
            placa_vehiculo = cabeceraGET.placaVehiculo
            orden_compra_servicio = cabeceraGET.ordenCompraServicio
            formato_de_pdf = cabeceraGET.formatoDePdf
            generado_por_contigencia = cabeceraGET.generadoPorContigencia
            bienes_region_selva = cabeceraGET.bienesRegionSelva
            servicios_region_selva = cabeceraGET.serviciosRegionSelva
        }
    }

    private fun transferirDatosItem(itemGET: ItemsGET): DocumentoITEMSENT {
        return DocumentoITEMSENT().apply {
            cantidad = itemGET.cantidad
            valor_unitario = itemGET.valorUnitario
            descuento = itemGET.descuento
            subtotal = itemGET.subtotal
            tipo_de_isc = itemGET.tipoDeIsc
            isc = itemGET.isc
            tipo_de_igv = itemGET.tipoDeIgv
            igv = itemGET.igv
            total = itemGET.total
            anticipo_regularizacion = itemGET.anticipoRegularizacion
            anticipo_documento_serie = itemGET.anticipoDocumentoSerie
            anticipo_documento_numero = itemGET.anticipoDocumentoNumero
            codigo = itemGET.codigo.toString()
            precio_unitario = itemGET.precioUnitario
            codigo_producto_sunat = itemGET.codigoProductoSunat
            unidad_de_medida = itemGET.unidadDeMedida
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
    }

    private fun observeLiveData() {
        if (!isObservingLiveData) {
            listpedViewModel.listMutableLiveData.observe(viewLifecycleOwner) { listpedResponses ->
                listpedAdapter.setPedidos(listpedResponses)
            }
            isObservingLiveData = true
        }
    }

    override fun onDestroyView() {
        listpedViewModel.listMutableLiveData.removeObservers(viewLifecycleOwner)
        isObservingLiveData = false

        super.onDestroyView()
    }

}