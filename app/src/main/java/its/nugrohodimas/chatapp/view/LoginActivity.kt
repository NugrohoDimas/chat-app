package its.nugrohodimas.chatapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import its.nugrohodimas.chatapp.R
import its.nugrohodimas.chatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        binding.apply {
            btnLogin.setOnClickListener(this@LoginActivity)
            tvRegister.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val email = binding.edtLoginEmail.text.toString().trim()
                val password = binding.edtLoginPassword.text.toString().trim()

                when {
                    email.isEmpty() -> {
                        binding.apply {
                            edtLoginEmail.error = "This field can't be blank!"
                            edtLoginEmail.isFocusable
                        }
                    }

                    password.isEmpty() -> {
                        binding.apply {
                            edtLoginPassword.error = "This field can't be blank!"
                            edtLoginPassword.isFocusable
                        }
                    }

                    else -> {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    binding.apply {
                                        edtLoginEmail.text?.clear()
                                        edtLoginPassword.text?.clear()
                                    }
                                    startActivity(Intent(this, UsersActivity::class.java))
                                }
                            }.addOnFailureListener { exception ->
                                Toast.makeText(
                                    applicationContext,
                                    exception.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
            }

            R.id.tv_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }
}