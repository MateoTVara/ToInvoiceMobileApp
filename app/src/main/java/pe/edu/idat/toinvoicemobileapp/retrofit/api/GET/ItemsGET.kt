package pe.edu.idat.toinvoicemobileapp.retrofit.api.GET

class ItemsGET {

    // Atributos de la tabla detalles
    var id: Int = 0
    var orderId: Int = 0
    var cantidad: Int = 0
    var valorUnitario: Double = 0.0
    var descuento: Double = 0.0
    var subtotal: Double = 0.0
    var tipoDeIsc: Int = 0
    var isc: Double = 0.0
    var tipoDeIgv: Int = 0
    var igv: Double = 0.0
    var total: Double = 0.0
    var anticipoRegularizacion: Boolean = false
    var anticipoDocumentoSerie: String? = null
    var anticipoDocumentoNumero: String? = null

    // Atributos de la tabla productos
    var codigo: Int = 0
    var descripcion: String? = null
    var precioUnitario: Double = 0.0
    var codigoProductoSunat: String? = null
    var unidadDeMedida: String? = null

}