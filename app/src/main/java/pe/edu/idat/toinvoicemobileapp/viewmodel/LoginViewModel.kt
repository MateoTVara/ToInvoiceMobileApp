package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.MobileCliente
import pe.edu.idat.toinvoicemobileapp.retrofit.request.LoginRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val loginResponseMutableLiveData = MutableLiveData<LoginResponse>()

    fun autenticarUsuario(loginRequest: LoginRequest) {
        MobileCliente.getInstance().autenticarUsuario(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    loginResponseMutableLiveData.value = response.body()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle failure here
                }
            })
    }
}