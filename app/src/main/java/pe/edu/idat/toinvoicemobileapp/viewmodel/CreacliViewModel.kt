package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegiscliRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegispedRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.response.RegispedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreacliViewModel (application: Application) : AndroidViewModel(application) {

    val mensajeRegistroLiveData = MutableLiveData<String>()

    fun registrarCliente(regiscliRequest: RegiscliRequest) {
        MobileCliente.getInstance().registrarClientes(regiscliRequest)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        mensajeRegistroLiveData.value = "Registro de pedido exitoso"
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }
            })
    }

}