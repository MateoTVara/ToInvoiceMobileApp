package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.response.PromocionesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromocionesViewModel(application: Application) : AndroidViewModel(application) {
    val listpromocionesMutableLiveData = MutableLiveData<List<PromocionesResponse>>()

    fun listarPromociones() {
        MobileCliente.getInstance().listarPromociones()
            .enqueue(object : Callback<List<PromocionesResponse>> {
                override fun onResponse(
                    call: Call<List<PromocionesResponse>>,
                    response: Response<List<PromocionesResponse>>
                ) {
                    if (response.isSuccessful) {
                        listpromocionesMutableLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<PromocionesResponse>>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}