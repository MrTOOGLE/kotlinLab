package com.example.lab12.src

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab12.R

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var onCartClickListener: ((Product, Int) -> Unit)? = null

    fun setOnCartClickListener(listener: (Product, Int) -> Unit) {
        onCartClickListener = listener
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val addToCartButton: ImageButton = itemView.findViewById(R.id.addToCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val context = holder.itemView.context

        holder.productImage.setImageResource(product.imageResource)
        holder.productName.text = product.name
        holder.productPrice.text = String.format(
            context.getString(R.string.price_format),
            String.format("%.2f", product.price)
        )

        // Устанавливаем правильную иконку в зависимости от состояния
        holder.addToCartButton.setImageResource(
            if (product.isInCart) R.drawable.ic_cart
            else R.drawable.ic_cart_plus
        )

        holder.addToCartButton.setOnClickListener {
            onCartClickListener?.invoke(product, position)
        }
    }

    override fun getItemCount(): Int = products.size
}