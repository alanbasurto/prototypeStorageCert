package com.testalan

import android.net.Uri
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.testalan.databinding.ActivitySecondBinding
import com.testalan.databinding.ActivityThirdBinding
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding
    private lateinit var user: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var images: List<StorageReference>
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras
        val title = bundle?.get("title").toString()
        val dir: String = "gs://test-alan2.appspot.com/products/${title}"
        binding.textView.setText(title)
        val listRef = FirebaseStorage.getInstance().getReference().child("products/")
        listRef.listAll()
            .addOnSuccessListener { (items, prefixes) ->
                prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    if(dir == prefix.toString()){
                        prefix.listAll()
                            .addOnSuccessListener { (items,prefixes)->
                                images=items
                                Glide.with(this)
                                    .load(items[0])
                                    .into(binding.imgSwi)
                            }
                        return@forEach
                    }
                }

            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }

        binding.btnPrev.setOnClickListener {
            if(position>0){
                position--
                Glide.with(this)
                    .load(images[position])
                    .into(binding.imgSwi)
            }else{
                Toast.makeText(this,"no more...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            if(position<images.size-1){
                position++
                Glide.with(this)
                    .load(images[position])
                    .into(binding.imgSwi)
            }else {
                Toast.makeText(this, "no more...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener{
            try {


            for((count,img) in images.withIndex()){
                img.delete().addOnSuccessListener {
                    Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
                    if (count==0){
                        FirebaseFirestore.getInstance().collection("products")
                            .document(title).delete()
                            .addOnSuccessListener { Log.d( "","DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w("Error deleting document", e) }
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "${it.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
                val i = Intent(this,SecondActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i)

                finish()
            }catch (e: Exception){

            }

        }

    }
}