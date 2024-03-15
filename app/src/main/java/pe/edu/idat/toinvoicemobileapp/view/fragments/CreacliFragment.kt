package pe.edu.idat.toinvoicemobileapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentCreacliBinding
import pe.edu.idat.toinvoicemobileapp.databinding.FragmentCreapedBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegiscliRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.request.RegispedRequest
import pe.edu.idat.toinvoicemobileapp.viewmodel.CreacliViewModel

class CreacliFragment : Fragment() {

    private lateinit var binding : FragmentCreacliBinding
    private lateinit var creacliViewModel: CreacliViewModel

    private lateinit var ptcreaclirazonsocial: EditText
    private lateinit var  ptcreaclirucdni: EditText
    private lateinit var ptcreaclidireccion: EditText
    private lateinit var btncreacliguardar: Button
    private lateinit var btncreaclicancelar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentCreacliBinding.inflate(inflater, container, false)

        creacliViewModel = ViewModelProvider(this).get(CreacliViewModel::class.java)

        ptcreaclirazonsocial=binding.etcreaclirazonsocial
        ptcreaclirucdni=binding.etcreaclirucdni
        ptcreaclidireccion=binding.etcreclidireccion
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
    }

    private fun setupGuardarButton(){
        btncreacliguardar?.setOnClickListener{
            val razonsocial = ptcreaclirazonsocial.text.toString().trim()
            val rucdni = ptcreaclirucdni.text.toString().trim()
            val direccion = ptcreaclidireccion.text.toString().trim()

            if (!razonsocial.isEmpty() &&
                !rucdni.isEmpty() &&
                !direccion.isEmpty()){
                val rucdniconverted=rucdni.toInt()

                val regiscliRequest = RegiscliRequest()
                regiscliRequest.razonsocial = razonsocial
                regiscliRequest.rucdni = rucdniconverted
                regiscliRequest.direccion = direccion

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