package pe.edu.idat.toinvoicemobileapp.retrofit.response

class ListproResponse {
    var codigo: Int = 0
    var descripcion: String? = null
    var unidadDeMedida: String? = null
    var precioUnitario: Double = 0.0

    override fun toString(): String {
        return descripcion ?: "" // or any other field you want to display
    }
}