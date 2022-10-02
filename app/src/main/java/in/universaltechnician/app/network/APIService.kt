package `in`.universaltechnician.app.network

import `in`.universaltechnician.app.model.OrderResponse
import `in`.universaltechnician.app.model.PriceResponse
import `in`.universaltechnician.app.model.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {


    @FormUrlEncoded
    @POST("order.php")
    fun createOrders(
        @Field("operation") operation: String = "createOrder",
        @Field("uid") userid: String,
        @Field("stove_type") stove_type: String,
        @Field("ro_waterfilter_type") ro_waterfilter_type: String,
        @Field("geezer_type") geezer_type: String,
        @Field("ac_type") ac_type: String,
        @Field("homemill") homemill: String,
        @Field("refrigerator") refrigerator: String,
        @Field("washing_machine") washing_machine: String,
        @Field("microwave") microwave: String,
        @Field("chimney") chimney: String,
        @Field("order_type") orderType: String,
        @Field("order_image") orderImage: String
    ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("price.php")
    fun updatePrices(
        @Field("operation") operation: String = "updatePrice",
        @Field("p_gas_2burner") p_gas_2burner: String,
        @Field("p_gas_3burner") p_gas_3burner: String,
        @Field("p_gas_4burner") p_gas_4burner: String,
        @Field("p_gas_hob") p_gas_hob: String,
        @Field("p_ro") p_ro: String,
        @Field("p_waterfilter") p_waterfilter: String,
        @Field("p_geezer_electrical_3_6") p_geezer_electrical_3_6: String,
        @Field("p_geezer_solar") p_geezer_solar: String,
        @Field("p_geezer_gas") p_geezer_gas: String,
        @Field("p_ac_1pic") p_ac_1pic: String,
        @Field("p_ac_2pic") p_ac_2pic: String,
        @Field("p_ac_3pic") p_ac_3pic: String,
        @Field("p_ac_4pic") p_ac_4pic: String,
        @Field("p_flourmill") p_flourmill: String,
        @Field("p_refrigerator") p_refrigerator: String,
        @Field("p_washingmachine") p_washingmachine: String,
        @Field("p_microwave") p_microwave: String,
        @Field("p_chimany") p_chimany: String,
        @Field("p_geezer_electrical_10_15") p_geezer_electrical_10_15: String,
        @Field("p_geezer_electrical_20_25") p_geezer_electrical_20_25: String,
        @Field("p_geezer_electrical_50_1000") p_geezer_electrical_50_100: String
    ): Call<PriceResponse>

    @FormUrlEncoded
    @POST("price.php")
    fun getPrices(
        @Field("operation") operation: String = "getPrice",
    ): Call<PriceResponse>

    @FormUrlEncoded
    @POST("order.php")
    fun getUserOrders(
        @Field("operation") operation: String = "selectOrder",
        @Field("uid") userid: String,
    ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order.php")
    fun getAdminOrders(
        @Field("operation") operation: String = "selectAdminOrder",
        @Field("is_test") isTest: String,
    ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order.php")
    fun completePayment(
        @Field("operation") operation: String = "completePayment",
        @Field("oid") orderId: String,
        ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order.php")
    fun orderComplete(
        @Field("operation") operation: String = "orderComplete",
        @Field("oid") orderId: String,
        ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("user.php")
    fun checkUser(
        @Field("operation") operation: String = "checkUser",
        @Field("phone") phone: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("admin.php")
    fun checkAdmin(
        @Field("operation") operation: String = "checkAdmin",
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user.php")
    fun createUser(
        @Field("operation") operation: String = "insert",
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
    ): Call<UserResponse>
}