package pe.edu.idat.toinvoicemobileapp.retrofit.response

class ListusuResponse {
    var idusu: Int = 0
    var usuario: String? = null
    var contrasenia: String? = null
    var nombre: String? = null

    override fun toString(): String {
        return nombre ?: "" // or any other field you want to display
    }
}