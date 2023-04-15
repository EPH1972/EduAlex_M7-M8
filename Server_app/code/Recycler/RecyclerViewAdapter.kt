package com.example.m8uf2_server.Recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m8uf2_server.Classes.Pedido
import com.example.m8uf2_server.Classes.Producto
import com.example.m8uf2_server.Fragments.Pedido_DetailFragment
import com.example.m8uf2_server.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecyclerViewAdapter(private var list: MutableList<Pedido>,
                          private var database: FirebaseDatabase
): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // This function will update ViewHolder whit the necessary values of the attributes for every object.
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.productName.text = list[position].name.toString()
        Glide.with(holder.itemView).load(list[position].img).into(holder.productImage)
//        holder.productDescription.text = list[position].description.toString()
//        holder.productPrice.text = list[position].price.toString()
        holder.itemView.setOnClickListener { view ->
            val activity = view!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                Pedido_DetailFragment(list[position], database)
            ).commit()
        }
    }

    // BluePrint for every object that shows on the list.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.itemName)
        val productImage: ImageView = view.findViewById(R.id.itemImage)
//        val productDescription: TextView = view.findViewById(R.id.itemDescription)
//        val productPrice: TextView = view.findViewById(R.id.itemPrice)

    }
}