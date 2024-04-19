package pe.edu.idat.toinvoicemobileapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MobileCliente {
    private const val BASE_URL =
        "http://toinvoice.jelastic.saveincloud.net/MobileService-Ped-InUseVersion/rest/"
        //"http://192.168.18.8:8080/MobileService-Ped-InUseVersion/rest/"
    private val mobileServicio: MobileServicio by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MobileServicio::class.java)
    }

    fun getInstance(): MobileServicio {
        return mobileServicio
    }
}