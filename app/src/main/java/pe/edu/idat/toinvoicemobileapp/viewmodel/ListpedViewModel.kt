package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpedResponse
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListpeddetailedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListpedViewModel(application: Application) : AndroidViewModel(application) {

    val listMutableLiveData = MutableLiveData<List<ListpedResponse>>()
    val listpeddetailedResponseMutableLiveData = MutableLiveData<ListpeddetailedResponse>()

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
}