package `in`.universaltechnician.app.model

import java.io.Serializable

data class OrderResponse(
    val `data`: List<Data>?,
    val success: Boolean
) : Serializable {
    data class Data(
        val ac_type: String,
        val chimney: String,
        val geezer_type: String,
        val homemill: String,
        val microwave: String,
        val oid: String,
        val order_status: String,
        val order_time: String,
        val payment_type: String,
        val refrigerator: String,
        val ro_waterfilter_type: String,
        val stove_type: String,
        val order_type: String,
        val uid: String,
        val washing_machine: String,
        val name: String,
        val phone: String,
        val address: String,
        val order_image: String
    ) : Serializable
}