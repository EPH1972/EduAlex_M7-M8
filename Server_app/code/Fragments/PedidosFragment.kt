package com.example.m8uf2_server.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.m8uf2_server.Classes.Pedido
import com.example.m8uf2_server.Classes.Producto
import com.example.m8uf2_server.R
import com.example.m8uf2_server.Recycler.RecyclerViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * A simple [Fragment] subclass.
 * Use the [PedidosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PedidosFragment(private var db: FirebaseDatabase) : Fragment() {
    var list: MutableList<Pedido> = mutableListOf()
    var storageRef = Firebase.storage.reference
    val productRef = storageRef.child("productImage")

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_pedidos, container, false)

        activity?.title = "Ordered items list"

        db.getReference("Products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        for (snapshotProduct: DataSnapshot in snapshot.children) {
                            val product: Pedido? = snapshotProduct.getValue<Pedido>()
                            if (product!!.ordered == true) {
                                product.id = snapshotProduct.key
                                list.add(product)
                            }
//                            list.add(product!!)
                        }

                        val recyclerView: RecyclerView = view.findViewById(R.id.ItemList)
                        recyclerView.layoutManager = LinearLayoutManager(context)

                        val adapter = RecyclerViewAdapter(list, db)
                        recyclerView.adapter = adapter

                        recyclerView.addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.w("FirebaseTest", "Failed to read value.", error.toException())
                }
            })


        return view
    }
}