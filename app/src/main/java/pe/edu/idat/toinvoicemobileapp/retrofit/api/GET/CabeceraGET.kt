package pe.edu.idat.toinvoicemobileapp.retrofit.api.GET

class CabeceraGET {
    // Atributos de la tabla pedidos
    var id: Int = 0
    var serie: String? = null
    var numero: Int = 0
    var tipoDeComprobante: String? = null
    var sunatTransaction: Int = 0
    var fechaDeEmision: String? = null
    var fechaDeVencimiento: String? = null
    var moneda: Int = 0
    var tipoDeCambio: Double = 0.0
    var porcentajeDeIgv: String? = null
    var descuentoGlobal: Double = 0.0
    var totalDescuento: Double = 0.0
    var totalAnticipo: String? = null
    var totalGravada: Double = 0.0
    var totalInafecta: String? = null
    var totalExonerada: String? = null
    var totalIsc: Double = 0.0
    var totalIgv: Double = 0.0
    var totalGratuita: String? = null
    var totalOtrosCargos: String? = null
    var total: Double = 0.0
    var percepcionTipo: String? = null
    var percepcionBaseImponible: String? = null
    var totalPercepcion: String? = null
    var totalIncluidoPercepcion: String? = null
    var retencionTipo: String? = null
    var retencionBaseImponible: String? = null
    var totalRetencion: String? = null
    var totalImpuestosBolsas: String? = null
    var detraccion: Boolean = false
    var observaciones: String? = null
    var documentoQueSeModificaTipo: String? = null
    var documentoQueSeModificaSerie: String? = null
    var documentoQueSeModificaNumero: String? = null
    var tipoDeNotaDeCredito: String? = null
    var tipoDeNotaDeDebito: String? = null
    var enviarAutomaticamenteALaSunat: Boolean = false
    var enviarAutomaticamenteAlCliente: Boolean = false
    var condicionesDePago: String? = null
    var medioDePago: String? = null
    var placaVehiculo: String? = null
    var ordenCompraServicio: String? = null
    var formatoDePdf: String? = null
    var generadoPorContigencia: String? = null
    var bienesRegionSelva: String? = null
    var serviciosRegionSelva: String? = null

    // Atributos de la tabla clientes
    var tipoDocumento: Int = 0
    var numeroDocumento: String? = null
    var denominacion: String? = null
    var direccion: String? = null
    var email: String? = null
    var email1: String? = null
    var email2: String? = null

    var items: List<ItemsGET>? = null
}