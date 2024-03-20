package pe.edu.idat.toinvoicemobileapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import pe.edu.idat.toinvoicemobileapp.R
import pe.edu.idat.toinvoicemobileapp.databinding.ActivityMainBinding
import pe.edu.idat.toinvoicemobileapp.retrofit.request.LoginRequest
import pe.edu.idat.toinvoicemobileapp.retrofit.response.LoginResponse
import pe.edu.idat.toinvoicemobileapp.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.btnIngresar.setOnClickListener(this)
        loginViewModel.loginResponseMutableLiveData.observe(this, Observer { loginResponse ->
            validarLogin(loginResponse)
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnIngresar -> invocarLogin()
        }
    }

    private fun invocarLogin() {
        val loginRequest = LoginRequest().apply {
            usuario = binding.txUsuario.text.toString()
            contrasenia = binding.txPassword.text.toString()
        }
        loginViewModel.autenticarUsuario(loginRequest)
    }

    private fun validarLogin(loginResponse: LoginResponse?) {
        if (loginResponse != null && loginResponse.nombre != null) {
            // Las credenciales son válidas, continuar con el flujo normal
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.putExtra("NOMBRE", loginResponse.nombre)
            startActivity(intent)
        } else {
            // Las credenciales son inválidas o loginResponse es null, mostrar mensaje de error
            Snackbar.make(binding.root, "Credenciales incorrectas", Snackbar.LENGTH_SHORT).show()
        }
    }


}