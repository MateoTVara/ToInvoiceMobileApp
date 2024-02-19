package pe.edu.idat.toinvoicemobileapp.retrofit.response

class ListcliResponse {
    var idcli: Int = 0
    var razonsocial: String? = null
    var rucdni: String? = null
    var direccion: String? = null

    override fun toString(): String {
        return razonsocial ?: "" // or any field you want to display
    }
}