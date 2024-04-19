package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.api.GET.CabeceraGET
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpedResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpeddetailedResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.SendStateResponse
import pe.edu.idat.toinvoicemobileapp.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListpedViewModel(application: Application) : AndroidViewModel(application) {

    val listMutableLiveData = MutableLiveData<List<ListpedResponse>>()
    val listpeddetailedResponseMutableLiveData = MutableLiveData<ListpeddetailedResponse>()
    val cabeceraENTMutableLiveData = MutableLiveData<CabeceraGET>()
    val sendStateLiveData = SingleLiveEvent<SendStateResponse>()

    fun listarPedidos() {
        MobileCliente.getInstance().listarPedidos()
            .enqueue(object : Callback<List<ListpedResponse>> {
                override fun onResponse(call: Call<List<ListpedResponse>>, response: Response<List<ListpedResponse>>) {
                    if (response.isSuccessful) {
                        listMutableLiveData.value = response.body()
                    } else {
                        handleErrorResponse(response)
                    }
                }

                override fun onFailure(call: Call<List<ListpedResponse>>, t: Throwable) {
                    handleFailure(t)
                }
            })
    }

    private fun handleErrorResponse(response: Response<List<ListpedResponse>>) {
        Log.e("ListpedViewModel", "Error en la respuesta: " + response.code())
    }

    private fun handleFailure(t: Throwable) {
        Log.e("ListpedViewModel", "Error en la conexión", t)
    }

    fun eliminarPedido(idPedido: Int) {
        MobileCliente.getInstance().eliminacionPedido(idPedido)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    // Handle response
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle failure
                }
            })
    }

    fun buscarPedidoDetallado(idPedido: Int) {
        MobileCliente.getInstance().buscarPedidoDetallado(idPedido)
            .enqueue(object : Callback<ListpeddetailedResponse> {
                override fun onResponse(call: Call<ListpeddetailedResponse>, response: Response<ListpeddetailedResponse>) {
                    listpeddetailedResponseMutableLiveData.value = response.body()
                }

                override fun onFailure(call: Call<ListpeddetailedResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }

    fun buscarPedidoDetalladoConDetalles(idOrder: Int) {
        MobileCliente.getInstance().buscarPedidoDetalladoConDetalles(idOrder)
            .enqueue(object : Callback<CabeceraGET> {
                override fun onResponse(
                    call: Call<CabeceraGET>,
                    response: Response<CabeceraGET>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!= null) {
                            cabeceraENTMutableLiveData.value = response.body()
                            Log.d("BuscarPedidoDetallado", "Solicitud exitosa: ${response.code()}")
                        } else {
                            Log.e("BuscarPedidoDetallado", "Respuesta nula: ${response.code()}")
                        }
                    } else {
                        Log.e("BuscarPedidoDetallado", "Error en la solicitud: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CabeceraGET>, t: Throwable) {
                    Log.e("BuscarPedidoDetallado", "Error en la comunicación con el servidor: ${t.message}")
                }
            })
    }

    fun verificarEnvioSunat(idOrder: Int, callback: (Boolean) -> Unit) {
        MobileCliente.getInstance().verificarEnvioSunat(idOrder)
            .enqueue(object : Callback<SendStateResponse> {
                override fun onResponse(
                    call: Call<SendStateResponse>,
                    response: Response<SendStateResponse>
                ) {
                    if (response.isSuccessful) {
                        val enviadoASunat = response.body()?.enviado_a_sunat ?: false
                        callback(enviadoASunat)
                    }
                }

                override fun onFailure(call: Call<SendStateResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }


    fun marcarComoEnviadoSunat(idOrder: Int){
        MobileCliente.getInstance().marcarComoEnviadoSunat(idOrder)
            .enqueue(object : Callback<SendStateResponse>{
                override fun onResponse(
                    call: Call<SendStateResponse>,
                    response: Response<SendStateResponse>
                ) {
                    if (response.isSuccessful) {
                        // Establecer enviado_a_sunat como verdadero
                        sendStateLiveData.value = SendStateResponse(true, response.body()?.message)
                    } else {
                        // Si la respuesta no es exitosa, mantener el valor actual de enviado_a_sunat
                        val sendStateResponse = response.body() ?: SendStateResponse()
                        sendStateLiveData.value = sendStateResponse
                    }
                }

                override fun onFailure(call: Call<SendStateResponse>, t: Throwable) {
                    // En caso de fallo, mantener el valor actual de enviado_a_sunat
                    sendStateLiveData.value = SendStateResponse()
                }

            })
    }


}