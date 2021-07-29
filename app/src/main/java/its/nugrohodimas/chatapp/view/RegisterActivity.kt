package its.nugrohodimas.chatapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import its.nugrohodimas.chatapp.R
import its.nugrohodimas.chatapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.apply {
            tvSignIn.setOnClickListener(this@RegisterActivity)
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sign_in -> {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
            R.id.btn_register -> {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val username = binding.edtRegisUsername.text.toString().trim()
        val email = binding.edtRegisEmail.text.toString().trim()
        val password = binding.edtRegisPassword.text.toString().trim()
        val retypePassword = binding.edtRegisRepassword.text.toString().trim()

        when {
            username.isEmpty() -> {
                binding.edtRegisUsername.error = "This field can't be blank!"
                binding.edtRegisUsername.isFocusable
            }
            email.isEmpty() -> {
                binding.edtRegisEmail.error = "This field can't be blank!"
                binding.edtRegisEmail.isFocusable
            }
            password.isEmpty() -> {
                binding.edtRegisPassword.error = "This field can't be blank!"
                binding.edtRegisPassword.isFocusable
            }
            retypePassword.isEmpty() -> {
                binding.edtRegisRepassword.error = "This field can't be blank!"
                binding.edtRegisRepassword.isFocusable
            }
            password != retypePassword -> {
                binding.edtRegisRepassword.error = "Password not match!"
                binding.edtRegisRepassword.isFocusable
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            reference = FirebaseDatabase.getInstance().getReference("Users")
                            val userId: String? = user?.uid

                            val hashMap: HashMap<String, String> = HashMap()
                            hashMap.put("userId", userId!!)
                            hashMap.put("userName", username)
                            hashMap.put("profileImage", "")

                            reference.child(userId).setValue(hashMap)
                                .addOnCompleteListener(this) { taskDatabase ->
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }

                            Toast.makeText(
                                this,
                                "User has been registered successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
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

}