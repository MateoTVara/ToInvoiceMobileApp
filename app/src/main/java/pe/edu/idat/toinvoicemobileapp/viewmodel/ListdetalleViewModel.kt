package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListdetalleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListdetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val listMutableLiveData = MutableLiveData<List<ListdetalleResponse>>()

    fun getListLiveData(): LiveData<List<ListdetalleResponse>> {
        return listMutableLiveData
    }

    fun listarDetallesNoAsignados() {
        MobileCliente.getInstance().listarDetallesNoAsignados()
            .enqueue(object : Callback<List<ListdetalleResponse>> {
                override fun onResponse(
                    call: Call<List<ListdetalleResponse>>,
                    response: Response<List<ListdetalleResponse>>
                ) {
                    if (response.isSuccessful) {
                        listMutableLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ListdetalleResponse>>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}