package com.testalan

data class ProductModel(
    val productName:String,
    val productImage:String
) {
    constructor():this(
        "",
        ""
    )
}