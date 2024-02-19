package pe.edu.idat.toinvoicemobileapp.retrofit.response

class ListproResponse {
    var idproduc: Int = 0
    var desproduc: String? = null
    var uniproduc: String? = null
    var precio: Double = 0.0

    override fun toString(): String {
        return desproduc ?: "" // or any other field you want to display
    }
}