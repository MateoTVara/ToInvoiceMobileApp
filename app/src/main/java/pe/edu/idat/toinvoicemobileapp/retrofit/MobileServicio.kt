package pe.edu.idat.toinvoicemobileapp.retrofit

import pe.edu.idat.toinvoicemobileapp.retrofit.request.*
import pe.edu.idat.toinvoicemobileapp.retrofit.response.*
import retrofit2.Call
import retrofit2.http.*

interface MobileServicio {
    @GET("pedido/listarCustom")
    fun listarPedidos(): Call<List<ListpedResponse>>

    @PUT("pedido/agregarParcial")
    fun registroPedido(@Body regispedRequest: RegispedRequest): Call<RegispedResponse>

    @DELETE("pedido/eliminar/{id}")
    fun eliminacionPedido(@Path("id") id: Int): Call<String>

    @GET("pedido/buscarDetallado/{id}")
    fun buscarPedidoDetallado(@Path("id") id: Int): Call<ListpeddetailedResponse>

    @POST("pedido/modificarParcial/{idped}")
    fun modificarPedido(@Path("idped") idped: Int, @Body modifypedRequest: ModifypedRequest): Call<String>

    @GET("cliente/buscarPorRazonSocialParcial/{partialRazonSocial}")
    fun sugerenciasPorRazonSocial(@Path("partialRazonSocial") razonSocial: String): Call<List<ListcliResponse>>

    @GET("clientes/listar")
    fun listadoClientes(): Call<List<ListcliResponse>>

    @GET("producto/buscarPorDescripcionParcial/{partialDescripcion}")
    fun sugerenciasPorDescripcion(@Path("partialDescripcion") descripcion: String): Call<List<ListproResponse>>

    @GET("usuarios/buscarPorNombreParcial/{partialNombre}")
    fun sugerenciasPorNombre(@Path("partialNombre") nombre: String): Call<List<ListusuResponse>>

    @POST("usuarios/autenticar")
    fun autenticarUsuario(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("detalle/listarNoAsignados")
    fun listarDetallesNoAsignados(): Call<List<ListdetalleResponse>>

    @GET("detalle/listarPorPedido/{idped}")
    fun listarDetallesPorPedido(@Path("idped") idped: Int): Call<List<ListdetalleResponse>>

    @PUT("detalle/registrarDetalleParcial")
    fun registroDetalleParcial(@Body regisdetalleRequest: RegisdetalleRequest): Call<String>

    @PUT("detalle/registrarDetalleParcialConIdped")
    fun registroDetalleParcialConIdped(@Body regisdetalleRequest: RegisdetalleRequest): Call<String>

    @DELETE("detalle/eliminar/{id}")
    fun eliminacionDetalle(@Path("id") id: Int): Call<String>

    @DELETE("detalle/eliminarNoAsignados")
    fun eliminacionDetallesNoAsignados(): Call<String>

    @POST("detalle/asignarIdped/{idped}")
    fun asignacionIdpedADetalles(@Path("idped") idped: Int): Call<String>

    @GET("promocion/listar")
    fun listarPromociones(): Call<List<PromocionesResponse>>
}
