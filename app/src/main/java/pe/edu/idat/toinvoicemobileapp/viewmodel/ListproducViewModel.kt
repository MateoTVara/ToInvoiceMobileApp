package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListproResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListproducViewModel (application: Application) : AndroidViewModel(application) {

    val listMutableLiveData = MutableLiveData<List<ListproResponse>>()

    fun listarProductos() {
        MobileCliente.getInstance().listarProductos()
            .enqueue(object : Callback<List<ListproResponse>> {
                override fun onResponse(call: Call<List<ListproResponse>>, response: Response<List<ListproResponse>>) {
                    if (response.isSuccessful) {
                        listMutableLiveData.value = response.body()
                    } else {
                        handleErrorResponse(response)
                    }
                }

                override fun onFailure(call: Call<List<ListproResponse>>, t: Throwable) {
                    handleFailure(t)
                }
            })
    }

    private fun handleErrorResponse(response: Response<List<ListproResponse>>) {
        Log.e("ListproducViewModel", "Error en la respuesta: " + response.code())
    }

    private fun handleFailure(t: Throwable) {
        Log.e("ListproducViewModel", "Error en la conexión", t)
    }

}