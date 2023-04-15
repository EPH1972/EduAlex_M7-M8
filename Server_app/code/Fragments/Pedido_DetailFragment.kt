package com.example.m8uf2_server.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.m8uf2_server.Classes.Pedido
import com.example.m8uf2_server.R
import com.example.m8uf2_server.Classes.Producto
import com.google.firebase.database.FirebaseDatabase

class Pedido_DetailFragment(private var product: Pedido, var database: FirebaseDatabase) : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View =  inflater.inflate(R.layout.fragment_pedido__detail, container, false)

        val itemImage: ImageView = view.findViewById(R.id.detailItemImage)
        val itemName: TextView = view.findViewById(R.id.detailItemName)
        val itemDescription: TextView = view.findViewById(R.id.detailItemDescription)
        val itemAcceptButton: Button = view.findViewById(R.id.buttonAccept)
        val itemEditButton: Button = view.findViewById(R.id.buttonEdit)

        Glide.with(this).load(product.img).into(itemImage)
        itemName.text = product.name.toString()
        itemDescription.text = product.description.toString()
        itemAcceptButton.setOnClickListener() {
            database.getReference("Products").child(product.id!!).child("accepted").setValue(true)
            Toast.makeText(context, "Orderd accepted", Toast.LENGTH_SHORT).show()
        }
        itemEditButton.setOnClickListener() {
            database.getReference("Products").child(product.id!!).child("accepted").setValue(false)
            Toast.makeText(context, "Ordered canceled", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}