package `in`.universaltechnician.app.viewmodel

import `in`.universaltechnician.app.model.OrderResponse
import `in`.universaltechnician.app.model.UserResponse
import `in`.universaltechnician.app.network.RetrofitClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel : ViewModel() {

    private var getUserOrdersLiveData: MutableLiveData<OrderResponse>? = null

    fun getUserOrders(userid: String): MutableLiveData<OrderResponse>? {
        if (getUserOrdersLiveData == null) {
            getUserOrdersLiveData = MutableLiveData()
        }

        callGetUserOrders(userid)

        return getUserOrdersLiveData
    }

    private fun callGetUserOrders(userid: String) {
        RetrofitClient.getClient().getUserOrders(userid = userid)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            getUserOrdersLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    getUserOrdersLiveData!!.value = OrderResponse(null, false)
                }

            })
    }

    private var getAdminOrdersLiveData: MutableLiveData<OrderResponse>? = null

    fun getAdminOrders(isTest:Boolean): MutableLiveData<OrderResponse>? {
        if (getAdminOrdersLiveData == null) {
            getAdminOrdersLiveData = MutableLiveData()
        }

        callGetAdminOrders(isTest)

        return getAdminOrdersLiveData
    }

    private fun callGetAdminOrders(isTest: Boolean) {
        RetrofitClient.getClient().getAdminOrders(isTest=if(isTest)"0" else "")
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            getAdminOrdersLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    getAdminOrdersLiveData!!.value = OrderResponse(null, false)
                }

            })
    }


    private var createOrderLiveData: MutableLiveData<OrderResponse>? = null

    fun createOrder(
        userid: String,
        stove_type: String,
        ro_waterfilter_type: String,
        geezer_type: String,
        ac_type: String,
        homemill: String,
        refrigerator: String,
        washing_machine: String,
        microwave: String,
        chimney: String,
        orderType:String,
        orderImage:String
    ): MutableLiveData<OrderResponse>? {
        if (createOrderLiveData == null) {
            createOrderLiveData = MutableLiveData()
        }

        callCreateOrder(
            userid,
            stove_type,
            ro_waterfilter_type,
            geezer_type,
            ac_type,
            homemill,
            refrigerator,
            washing_machine,
            microwave,
            chimney,
            orderType,
            orderImage
        )

        return createOrderLiveData
    }

    private fun callCreateOrder(
        userid: String,
        stove_type: String,
        ro_waterfilter_type: String,
        geezer_type: String,
        ac_type: String,
        homemill: String,
        refrigerator: String,
        washing_machine: String,
        microwave: String,
        chimney: String,
        orderType: String,
        orderImage: String
    ) {
        RetrofitClient.getClient().createOrders(
            userid = userid,
            stove_type = stove_type,
            ro_waterfilter_type = ro_waterfilter_type,
            geezer_type = geezer_type,
            ac_type = ac_type,
            homemill = homemill,
            refrigerator = refrigerator,
            washing_machine = washing_machine,
            microwave = microwave,
            chimney = chimney,
            orderType = orderType,
            orderImage = orderImage
        )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            createOrderLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    createOrderLiveData!!.value = OrderResponse(null, false)
                }

            })
    }


    private var completeOrderLiveData: MutableLiveData<OrderResponse>? = null

    fun completeOrder(
        orderId: String,
    ): MutableLiveData<OrderResponse>? {
        if (completeOrderLiveData == null) {
            completeOrderLiveData = MutableLiveData()
        }

        callCompleteOrder(
            orderId
        )

        return completeOrderLiveData
    }

    private fun callCompleteOrder(
        orderId: String,
    ) {
        RetrofitClient.getClient().orderComplete(
            orderId = orderId
        )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            completeOrderLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    completeOrderLiveData!!.value = OrderResponse(null, false)
                }

            })
    }

    private var completePaymentLiveData: MutableLiveData<OrderResponse>? = null

    fun completePayment(
        orderId: String,
    ): MutableLiveData<OrderResponse>? {
        if (completePaymentLiveData == null) {
            completePaymentLiveData = MutableLiveData()
        }

        callCompletePayment(
            orderId
        )

        return completePaymentLiveData
    }

    private fun callCompletePayment(
        orderId: String,
    ) {
        RetrofitClient.getClient().completePayment(
            orderId = orderId
        )
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            completePaymentLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    completePaymentLiveData!!.value = OrderResponse(null, false)
                }

            })
    }
}