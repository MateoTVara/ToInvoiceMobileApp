package pe.edu.idat.toinvoicemobileapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pe.edu.idat.toinvoicemobileapp.retrofit.*
import pe.edu.idat.toinvoicemobileapp.retrofit.request.*
import pe.edu.idat.toinvoicemobileapp.retrofit.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreapedViewModel(application: Application) : AndroidViewModel(application) {

    val sugerenciasLiveData = MutableLiveData<List<ListcliResponse>>()
    val sugerenciasproductosLiveData = MutableLiveData<List<ListproResponse>>()
    val sugerenciasusuariosLiveData = MutableLiveData<List<ListusuResponse>>()
    val regispedResponseMutableLiveData = MutableLiveData<RegispedResponse?>()
    val mensajeRegistroLiveData = MutableLiveData<String>()
    val detallesPorPedidoLiveData = MutableLiveData<List<ListdetalleResponse>>()
    val mensajeModificacionPedidoLiveData = MutableLiveData<String>()

    fun sugerenciasPorRazonSocial(razonSocial: String) {
        MobileCliente.getInstance().sugerenciasPorRazonSocial(razonSocial)
            .enqueue(object : Callback<List<ListcliResponse>> {
                override fun onResponse(
                    call: Call<List<ListcliResponse>>,
                    response: Response<List<ListcliResponse>>
                ) {
                    if (response.isSuccessful) {
                        sugerenciasLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ListcliResponse>>, t: Throwable) {
                    handleFailure(t)
                }
            })
    }

    private fun handleFailure(t: Throwable) {
        Log.e("CreapedViewModel", "Error en la conexión", t)
    }

    fun sugerenciasPorDescripcion(descripcion: String) {
        MobileCliente.getInstance().sugerenciasPorDescripcion(descripcion)
            .enqueue(object : Callback<List<ListproResponse>> {
                override fun onResponse(
                    call: Call<List<ListproResponse>>,
                    response: Response<List<ListproResponse>>
                ) {
                    if (response.isSuccessful) {
                        sugerenciasproductosLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ListproResponse>>, t: Throwable) {

                }
            })
    }

    fun sugerenciasPorNombre(nombre: String) {
        MobileCliente.getInstance().sugerenciasPorNombre(nombre)
            .enqueue(object : Callback<List<ListusuResponse>> {
                override fun onResponse(
                    call: Call<List<ListusuResponse>>,
                    response: Response<List<ListusuResponse>>
                ) {
                    if (response.isSuccessful) {
                        sugerenciasusuariosLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ListusuResponse>>, t: Throwable) {

                }
            })
    }

    fun registrarPedido(regispedRequest: RegispedRequest) {
        MobileCliente.getInstance().registroPedido(regispedRequest)
            .enqueue(object : Callback<RegispedResponse> {
                override fun onResponse(call: Call<RegispedResponse>, response: Response<RegispedResponse>) {
                    if (response.isSuccessful) {
                        val regispedResponse: RegispedResponse? = response.body()
                        val orderId: Int = regispedResponse?.id ?: return

                        // Asignar idped a detalles no asignados
                        asignarIdpedADetalles(orderId)

                        regispedResponseMutableLiveData.value = regispedResponse
                        mensajeRegistroLiveData.value = "Registro de pedido exitoso"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponse(response)
                    }
                }

                override fun onFailure(call: Call<RegispedResponse>, t: Throwable) {
                    // Manejar fallos en la conexión
                    handleFailure(t)
                }
            })
    }

    fun asignarIdpedADetalles(orderId: Int) {
        MobileCliente.getInstance().asignacionIdpedADetalles(orderId)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        // Puedes hacer algo con la respuesta si es necesario
                        mensajeRegistroLiveData.value = "Asignación de orderId a detalles exitosa"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponseString(response)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Manejar fallos en la conexión
                    handleFailure(t)
                }
            })
    }

    fun handleErrorResponse(response: Response<*>?) {
        if (response?.errorBody() != null) {
            val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
            Log.e("MobileCliente", "Error en la respuesta del servidor: $errorMessage")
            // Aquí puedes mostrar un mensaje de error en la interfaz de usuario, si es necesario
        } else {
            Log.e("MobileCliente", "La respuesta del servidor está vacía")
            // Aquí puedes mostrar un mensaje de error en la interfaz de usuario, si es necesario
        }
    }


    private fun handleErrorResponseString(response: Response<String>?) {
        val errorMessage = response?.errorBody()?.string() ?: "Error desconocido"
        Log.e("MobileCliente", "Error en la respuesta del servidor: $errorMessage")
        // Aquí puedes mostrar un mensaje de error en la interfaz de usuario, si es necesario
    }



    fun registrarDetalleParcial(regisdetalleRequest: RegisdetalleRequest) {
        Log.d("MobileCliente", "Enviando solicitud de registro de detalle parcial...")

        MobileCliente.getInstance().registroDetalleParcial(regisdetalleRequest)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("MobileCliente", "Respuesta recibida.")

                    if (response.isSuccessful) {
                        // Puedes hacer algo con la respuesta si es necesario
                        mensajeRegistroLiveData.value = "Registro de detalle parcial exitoso"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponseString(response)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MobileCliente", "Error en la conexión", t)
                    handleFailure(t)
                }
            })
    }

    fun eliminarDetalle(idDetalle: Int) {
        MobileCliente.getInstance().eliminacionDetalle(idDetalle)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        mensajeRegistroLiveData.value = "Detalle eliminado exitosamente"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponseString(response)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    handleFailure(t)
                }
            })
    }

    fun eliminarDetallesNoAsignados() {
        MobileCliente.getInstance().eliminacionDetallesNoAsignados()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    // No se realiza ninguna acción en onResponse
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // No se realiza ninguna acción en onFailure
                }
            })
    }

    fun modificarPedido(modifypedRequest: ModifypedRequest) {
        MobileCliente.getInstance().modificarPedido(modifypedRequest)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        // Puedes hacer algo con la respuesta si es necesario
                        mensajeModificacionPedidoLiveData.value = "Modificación de pedido exitosa"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponseString(response)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MobileCliente", "Error en la conexión", t)
                    handleFailure(t)
                }
            })
    }

    fun listarDetallesPorPedido(idPedido: Int) {
        MobileCliente.getInstance().listarDetallesPorPedido(idPedido)
            .enqueue(object : Callback<List<ListdetalleResponse>> {
                override fun onResponse(
                    call: Call<List<ListdetalleResponse>>,
                    response: Response<List<ListdetalleResponse>>
                ) {
                    if (response.isSuccessful) {
                        detallesPorPedidoLiveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<ListdetalleResponse>>, t: Throwable) {
                    handleFailure(t)
                }
            })
    }

    fun registrarDetalleParcialConIdped(regisdetalleRequest: RegisdetalleRequest) {
        Log.d("MobileCliente", "Enviando solicitud de registro de detalle parcial...")

        MobileCliente.getInstance().registroDetalleParcialConIdped(regisdetalleRequest)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("MobileCliente", "Respuesta recibida.")

                    if (response.isSuccessful) {
                        // Puedes hacer algo con la respuesta si es necesario
                        mensajeRegistroLiveData.value = "Registro de detalle parcial exitoso"
                    } else {
                        // Manejar errores en la respuesta
                        handleErrorResponseString(response)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("MobileCliente", "Error en la conexión", t)
                    handleFailure(t)
                }
            })
    }

}