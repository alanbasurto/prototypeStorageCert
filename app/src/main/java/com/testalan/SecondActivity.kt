package com.testalan

import android.R
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.testalan.databinding.ActivitySecondBinding


class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var user: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var uri: Uri
    private lateinit var images: List<Uri>
    private var position = 0
    private val PICK_IMAGES_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myDrawable = resources.getDrawable(resources.getIdentifier("image","drawable",packageName))

        user = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        binding.imgSwi.setImageDrawable(myDrawable)
        if(user.currentUser != null){
            user.currentUser?.let {

            }
        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetMultipleContents(),
            ActivityResultCallback {
                //  binding.imageView.setImageURI(it)
                if (it != null) {
                    images=it
                    binding.imgSwi.setImageURI(images[0])
                    position=0

                }
            }
        )

        binding.btnPrev.setOnClickListener {
            if(position>0){
                position--
                binding.imgSwi.setImageURI(images[position])
            }else{
                Toast.makeText(this,"no more...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            if(position<images.size-1){
                position++
                binding.imgSwi.setImageURI(images[position])
            }else {
                Toast.makeText(this, "no more...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGallery.setOnClickListener{
            galleryImage.launch("image/*")
        }

        binding.btnUpload.setOnClickListener {
            var productURL: String = "https://th.bing.com/th/id/OIP.vDf037OKUo0H03weRxdWuAHaHa?pid=ImgDet&rs=1"
            var product = hashMapOf(
                "productImage" to productURL,
                "productName" to binding.etProduct.text.toString()
            )
            var metadata = storageMetadata {

                setCustomMetadata("cuid", user.uid)
            }
            for((cont, img) in images.withIndex()){
                val ref = storage.getReference("products/${binding.etProduct.text}").child("${binding.etProduct.text}${cont}")
                    ref.putFile(img,metadata)
                        .continueWithTask { task ->
                            // Forward any exceptions
                            if (!task.isSuccessful) {
                                throw task.exception!!
                            }


                            // Request the public download URL
                            ref.downloadUrl
                        }
                    .addOnSuccessListener {
                        if(cont == 0){
                             product = hashMapOf(
                                "productImage" to it.toString(),
                                "productName" to binding.etProduct.text.toString()
                            )
                            FirebaseFirestore.getInstance().collection("products")
                                .document("${binding.etProduct.text}")
                                .set(product)
                                .addOnSuccessListener {
                                    //Toast.makeText(this, "succesfulFirestore",Toast.LENGTH_SHORT).show()
                                }

                        }
                        Toast.makeText(this, "This is good",Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener{

                        Toast.makeText(this, "${it.toString() }",Toast.LENGTH_SHORT).show()
                    }
            }




        }

        binding.btnProducts.setOnClickListener {
            binding.etProduct.text.clear()
            position = 0
            binding.imgSwi.setImageDrawable(myDrawable)
            images= emptyList()
            startActivity(
                Intent(
                    this,
                    myPhotos::class.java
                )
            )

        }

        binding.btnSignOut.setOnClickListener{
            user.signOut()
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }
    }
}