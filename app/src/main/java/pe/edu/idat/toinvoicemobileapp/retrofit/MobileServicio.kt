package pe.edu.idat.toinvoicemobileapp.retrofit

import pe.edu.idat.toinvoicemobileapp.retrofit.api.GET.CabeceraGET
import pe.edu.idat.toinvoicemobileapp.retrofit.request.*
import pe.edu.idat.toinvoicemobileapp.retrofit.response.*
import retrofit2.Call
import retrofit2.http.*

interface MobileServicio {
    @GET("pedido/listarCustom")
    fun listarPedidos(): Call<List<ListpedResponse>>

    @PUT("pedido/agregar")
    fun registroPedido(@Body regispedRequest: RegispedRequest): Call<RegispedResponse>

    @DELETE("pedido/eliminar/{id}")
    fun eliminacionPedido(@Path("id") id: Int): Call<String>

    @GET("pedido/buscarDetallado/{id}")
    fun buscarPedidoDetallado(@Path("id") id: Int): Call<ListpeddetailedResponse>
    @GET("pedido/buscarDetallado/{id}")
    fun buscarPedidoDetalladoConDetalles(@Path("id") id: Int): Call<CabeceraGET>

    @POST("pedido/modificar")
    fun modificarPedido(@Body modifypedRequest: ModifypedRequest): Call<String>

    @GET("pedido/verificarEnvioSunat/{id}")
    fun verificarEnvioSunat(@Path("id") id: Int): Call<SendStateResponse>

    @POST("pedido/marcarComoEnviadoSunat/{id}")
    fun marcarComoEnviadoSunat(@Path("id") id:Int): Call<SendStateResponse>

    @GET("cliente/buscarPorDenominacionParcial/{partialDenominacion}")
    fun sugerenciasPorRazonSocial(@Path("partialDenominacion") razonSocial: String): Call<List<ListcliResponse>>

    @GET("cliente/listar")
    fun listadoClientes(): Call<List<ListcliResponse>>

    @PUT("cliente/agregar")
    fun registrarClientes(@Body regiscliRequest: RegiscliRequest): Call<String>

    @GET("producto/buscarPorDescripcionParcial/{partialDescripcion}")
    fun sugerenciasPorDescripcion(@Path("partialDescripcion") descripcion: String): Call<List<ListproResponse>>

    @GET("producto/listar")
    fun listarProductos() : Call<List<ListproResponse>>

    @GET("usuarios/buscarPorNombreParcial/{partialNombre}")
    fun sugerenciasPorNombre(@Path("partialNombre") nombre: String): Call<List<ListusuResponse>>

    @POST("usuarios/autenticar")
    fun autenticarUsuario(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("detalle/listarNoAsignados")
    fun listarDetallesNoAsignados(): Call<List<ListdetalleResponse>>

    @GET("detalle/listarPorOrderId/{orderId}")
    fun listarDetallesPorPedido(@Path("orderId") idped: Int): Call<List<ListdetalleResponse>>

    @PUT("detalle/registrarDetalleParcial")
    fun registroDetalleParcial(@Body regisdetalleRequest: RegisdetalleRequest): Call<String>

    @PUT("detalle/registrarDetalleParcialConIdped")
    fun registroDetalleParcialConIdped(@Body regisdetalleRequest: RegisdetalleRequest): Call<String>

    @DELETE("detalle/eliminar/{id}")
    fun eliminacionDetalle(@Path("id") id: Int): Call<String>

    @DELETE("detalle/eliminarNoAsignados")
    fun eliminacionDetallesNoAsignados(): Call<String>

    @POST("detalle/asignarOrderId/{orderId}")
    fun asignacionIdpedADetalles(@Path("orderId") orderId: Int): Call<String>

    @GET("promocion/listar")
    fun listarPromociones(): Call<List<PromocionesResponse>>
}
