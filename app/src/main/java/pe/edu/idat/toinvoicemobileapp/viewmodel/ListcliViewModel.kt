package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.response.ListcliResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListcliViewModel(application: Application) : AndroidViewModel(application) {

    val listMutableLiveData = MutableLiveData<List<ListcliResponse>>()

    fun listarClientes(){
        MobileCliente.getInstance().listadoClientes()
            .enqueue(object : Callback<List<ListcliResponse>>{
                override fun onResponse(call: Call<List<ListcliResponse>>, response: Response<List<ListcliResponse>>) {
                    listMutableLiveData.value=response.body()
                }

                override fun onFailure(call: Call<List<ListcliResponse>>, t: Throwable) {
                }
            })
    }

}