package `in`.universaltechnician.app.model

data class PriceResponse(
    val `data`: List<Data>?,
    val success: Boolean
) {
    data class Data(
        val id: String,
        val price: String,
        val product: String
    )
}