package its.nugrohodimas.chatapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import its.nugrohodimas.chatapp.R
import its.nugrohodimas.chatapp.adapter.UserAdapter
import its.nugrohodimas.chatapp.databinding.ActivityUsersBinding
import its.nugrohodimas.chatapp.firebase.FirebaseService
import its.nugrohodimas.chatapp.model.User
import java.util.*

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    private lateinit var userFirebase: FirebaseUser
    private lateinit var auth: FirebaseAuth
    var dummyUserList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseService.sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                FirebaseService.token = it.result.toString()
            }
        }

        auth = FirebaseAuth.getInstance()

        userFirebase = FirebaseAuth.getInstance().currentUser!!

        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        getUserList()
    }

    private fun getUserList() {
        var userId = userFirebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userId")
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dummyUserList.clear()

                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(User::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}