package its.nugrohodimas.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import its.nugrohodimas.chatapp.R
import its.nugrohodimas.chatapp.model.User
import its.nugrohodimas.chatapp.view.ChatActivity
import java.util.*

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user,context)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ChatActivity::class.java).apply {
                putExtra(ChatActivity.EXTRA_DATA, user)
            })
        }
    }

    override fun getItemCount(): Int = userList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.tv_username_main)
        val message: TextView = view.findViewById(R.id.tv_message_main)
        val image: ImageView = view.findViewById(R.id.img_user_main)

        fun bind(user: User, context: Context) {
            username.text = user.userName
            Glide.with(context).load(user.profileImage).placeholder(R.drawable.social_talk).into(image)
        }
    }
}