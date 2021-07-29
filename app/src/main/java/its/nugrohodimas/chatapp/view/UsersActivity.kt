package its.nugrohodimas.chatapp.view

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import its.nugrohodimas.chatapp.adapter.UserAdapter
import its.nugrohodimas.chatapp.databinding.ActivityUsersBinding
import its.nugrohodimas.chatapp.model.User

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    var dummyUserList = ArrayList<User>()
    private lateinit var imageUser: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        getUserList()
    }

    private fun getUserList() {
        val userFirebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dummyUserList.clear()

                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(User::class.java)
                    Log.d("Test", user.toString())
                    if (!user?.userId.equals(userFirebase.uid)) {
                        if (user != null) {
                            dummyUserList.add(user)
                        }
                    }
                }

                val adapter = UserAdapter(this@UsersActivity, dummyUserList)
                binding.rvUser.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}