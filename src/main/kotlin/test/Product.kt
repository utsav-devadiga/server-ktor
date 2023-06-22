package test

import kotlinx.serialization.Serializable

@Serializable
class Product(
    val id: String,
    val name: String,
    val description: String
)