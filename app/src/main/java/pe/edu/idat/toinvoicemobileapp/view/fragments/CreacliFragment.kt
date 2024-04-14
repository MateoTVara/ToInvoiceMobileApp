package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentCreacliBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegiscliRequest
import pe.edu.idat.toinvoicemobileapp.viewmodel.CreacliViewModel

class CreacliFragment : Fragment() {

    private lateinit var binding : FragmentCreacliBinding
    private lateinit var creacliViewModel: CreacliViewModel

    private lateinit var ptcreaclirazonsocial: EditText
    private lateinit var ptcreaclirucdni: EditText
    private lateinit var ptcreaclidireccion: EditText
    private lateinit var etcreacliemail1: EditText
    private lateinit var etcreacliemail2: EditText
    private lateinit var etcreacliemail3: EditText
    private lateinit var btncreacliguardar: Button
    private lateinit var btncreaclicancelar: Button
    private lateinit var spinnerTipoDocumento: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentCreacliBinding.inflate(inflater, container, false)

        creacliViewModel = ViewModelProvider(this).get(CreacliViewModel::class.java)

        ptcreaclirazonsocial=binding.etcreaclirazonsocial
        ptcreaclirucdni=binding.etcreaclirucdni
        ptcreaclidireccion=binding.etcreclidireccion
        etcreacliemail1=binding.etcreacliemail1
        etcreacliemail2=binding.etcreacliemail2
        etcreacliemail3=binding.etcreacliemail3
        btncreacliguardar=binding.btncreacliguardar
        btncreaclicancelar=binding.btncreaclicancelar

        setupGuardarButton()
        setupCancelarButton()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        limpiarcampos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        limpiarcampos()
    }

    private fun limpiarcampos(){
        ptcreaclirazonsocial.text.clear()
        ptcreaclirucdni.text.clear()
        ptcreaclidireccion.text.clear()
        etcreacliemail1.text.clear()
        etcreacliemail2.text.clear()
        etcreacliemail3.text.clear()
    }

    private fun setupGuardarButton(){
        btncreacliguardar?.setOnClickListener{
            val razonsocial = ptcreaclirazonsocial.text.toString().trim()
            val rucdni = ptcreaclirucdni.text.toString().trim()
            val direccion = ptcreaclidireccion.text.toString().trim()
            val email = etcreacliemail1.text.toString().trim()
            val email1= etcreacliemail2.text.toString().trim()
            val email2= etcreacliemail3.text.toString().trim()
            val spinner = view?.findViewById<Spinner>(R.id.spinnerTipoDocumento)
            val selectedItem = spinner?.selectedItem.toString()

            if (!razonsocial.isEmpty() &&
                !rucdni.isEmpty() &&
                !direccion.isEmpty()){

                val tipoDocumentoValue = when (selectedItem) {
                    "RUC" -> 6
                    "DNI" -> 1
                    else -> -1 // Default value or handle error case
                }

                val regiscliRequest = RegiscliRequest()
                regiscliRequest.denominacion = razonsocial
                regiscliRequest.numeroDocumento = rucdni
                regiscliRequest.direccion = direccion
                regiscliRequest.tipoDocumento = tipoDocumentoValue
                regiscliRequest.email =email
                regiscliRequest.email1 = email1
                regiscliRequest.email2= email2

                creacliViewModel.registrarCliente(regiscliRequest)
                limpiarcampos()
            }
        }

    }

    private fun setupCancelarButton(){
        btncreaclicancelar.setOnClickListener{
            limpiarcampos()
        }
    }

}