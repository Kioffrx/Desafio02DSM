package udb.edu.sv.dsm.aerogo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import udb.edu.sv.dsm.aerogo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonRegister.setOnClickListener { register() }
        binding.textGoToLogin.setOnClickListener { finish() }
    }

    private fun register() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val confirmPassword = binding.editConfirmPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError(getString(R.string.error_required_field))
            return
        }

        if (password != confirmPassword) {
            showError(getString(R.string.error_passwords_mismatch))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMain()
                } else {
                    showError(task.exception?.localizedMessage ?: "Error al registrarse")
                }
            }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        binding.textError.text = message
        binding.textError.visibility = android.view.View.VISIBLE
    }
}