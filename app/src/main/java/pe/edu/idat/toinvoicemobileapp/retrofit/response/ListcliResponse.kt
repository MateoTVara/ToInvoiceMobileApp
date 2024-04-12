package pe.edu.idat.toinvoicemobileapp.retrofit.response

class ListcliResponse {
    var tipoDocumento: Int = 0
    var denominacion: String? = null
    var numeroDocumento: String? = null
    var direccion: String? = null
    var email: String? = null
    var email1: String? = null
    var email2: String? = null

    override fun toString(): String {
        return denominacion ?: ""
    }
}