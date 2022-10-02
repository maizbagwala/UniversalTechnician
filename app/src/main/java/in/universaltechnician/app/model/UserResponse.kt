package `in`.universaltechnician.app.model

data class UserResponse(
    val `data`: List<Data>?,
    val success: Boolean
) {
    data class Data(
        val username: String,
        val password: String,
        val address: String,
        val name: String,
        val phone: String,
        val uid: String
    )
}