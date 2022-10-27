package com.testalan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.component1
import com.testalan.databinding.ActivityMyPhotosBinding
import com.testalan.databinding.ActivitySecondBinding

class myPhotos : AppCompatActivity() {
    private lateinit var binding: ActivityMyPhotosBinding
    private lateinit var user: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    var products: List<ProductModel> = ArrayList<ProductModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvPhotos.apply {
            layoutManager = LinearLayoutManager(this@myPhotos)
        }
        fetchData()
    }

    private fun fetchData(){

        FirebaseFirestore.getInstance().collection("products")
            .get()
            .addOnSuccessListener { documents ->

                    products = documents.toObjects(ProductModel::class.java)
                    val adapter=ProductAdapter(this,products)

                binding.rvPhotos.adapter=adapter
                adapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {


                        val intent = Intent(this@myPhotos,ThirdActivity::class.java)
                        intent.putExtra("title",products.get(position).productName)
                        startActivity(intent)

                    }
                })



            }
            .addOnFailureListener{
                Toast.makeText(this, "this not working",Toast.LENGTH_SHORT).show()
            }
    }


}