package com.testalan

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(public val context: android.content.Context,val products: List<ProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder> () {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false),mListener
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.productName

        Glide.with(context)
            .load(product.productImage)
            .into(holder.productImage)


    }

    inner class ProductViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val productName: TextView = itemView.findViewById(R.id.textView3)
        val productImage: ImageView = itemView.findViewById(R.id.imageView3)
        init{
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }


}