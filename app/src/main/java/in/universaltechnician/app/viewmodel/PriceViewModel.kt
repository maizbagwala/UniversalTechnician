package `in`.universaltechnician.app.viewmodel

import `in`.universaltechnician.app.model.PriceResponse
import `in`.universaltechnician.app.network.RetrofitClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceViewModel : ViewModel() {

    val totalPrice = MutableLiveData<Int>(0)
    val gasPrice = MutableLiveData<Int>(0)
    val roPrice = MutableLiveData<Int>(0)
    val geezerPrice = MutableLiveData<Int>(0)
    val acPrice = MutableLiveData<Int>(0)
    val flourmillPrice = MutableLiveData<Int>(0)
    val freezePrice = MutableLiveData<Int>(0)
    val washingmachinePrice = MutableLiveData<Int>(0)
    val microwavePrice = MutableLiveData<Int>(0)
    val chimneyPrice = MutableLiveData<Int>(0)

    private var getPriceLiveData: MutableLiveData<PriceResponse>? = null

    fun getPrice(): MutableLiveData<PriceResponse>? {
        if (getPriceLiveData == null) {
            getPriceLiveData = MutableLiveData()
        }

        callGetPrice()

        return getPriceLiveData
    }

    private fun callGetPrice() {
        RetrofitClient.getClient().getPrices()
            .enqueue(object : Callback<PriceResponse> {
                override fun onResponse(
                    call: Call<PriceResponse>,
                    response: Response<PriceResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            getPriceLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                    getPriceLiveData!!.value = PriceResponse(null, false)
                }

            })
    }

    private var updatePriceLiveData: MutableLiveData<PriceResponse>? = null

    fun updatePrice(
        p_gas_2burner: String,
        p_gas_3burner: String,
        p_gas_4burner: String,
        p_gas_hob: String,
        p_ro: String,
        p_waterfilter: String,
        p_geezer_electrical_3_6: String,
        p_geezer_solar: String,
        p_geezer_gas: String,
        p_ac_1pic: String,
        p_ac_2pic: String,
        p_ac_3pic: String,
        p_ac_4pic: String,
        p_flourmill: String,
        p_refrigerator: String,
        p_washingmachine: String,
        p_microwave: String,
        p_chimany: String,
        p_geezer_electrical_10_15: String,
        p_geezer_electrical_20_25: String,
        p_geezer_electrical_50_100: String
    ): MutableLiveData<PriceResponse>? {
        if (updatePriceLiveData == null) {
            updatePriceLiveData = MutableLiveData()
        }

        callUpdatePrice(
            p_gas_2burner,
            p_gas_3burner,
            p_gas_4burner,
            p_gas_hob,
            p_ro,
            p_waterfilter,
            p_geezer_electrical_3_6,
            p_geezer_solar,
            p_geezer_gas,
            p_ac_1pic,
            p_ac_2pic,
            p_ac_3pic,
            p_ac_4pic,
            p_flourmill,
            p_refrigerator,
            p_washingmachine,
            p_microwave,
            p_chimany,
            p_geezer_electrical_10_15,
            p_geezer_electrical_20_25,
            p_geezer_electrical_50_100
        )

        return updatePriceLiveData
    }

    private fun callUpdatePrice(
        p_gas_2burner: String,
        p_gas_3burner: String,
        p_gas_4burner: String,
        p_gas_hob: String,
        p_ro: String,
        p_waterfilter: String,
        p_geezer_electrical_3_6: String,
        p_geezer_solar: String,
        p_geezer_gas: String,
        p_ac_1pic: String,
        p_ac_2pic: String,
        p_ac_3pic: String,
        p_ac_4pic: String,
        p_flourmill: String,
        p_refrigerator: String,
        p_washingmachine: String,
        p_microwave: String,
        p_chimany: String,
        p_geezer_electrical_10_15: String,
        p_geezer_electrical_20_25: String,
        p_geezer_electrical_50_100: String,
    ) {
        RetrofitClient.getClient().updatePrices(
            p_gas_2burner = p_gas_2burner,
            p_gas_3burner = p_gas_3burner,
            p_gas_4burner = p_gas_4burner,
            p_gas_hob = p_gas_hob,
            p_ro = p_ro,
            p_waterfilter = p_waterfilter,
            p_geezer_electrical_3_6 = p_geezer_electrical_3_6,
            p_geezer_solar = p_geezer_solar,
            p_geezer_gas = p_geezer_gas,
            p_ac_1pic = p_ac_1pic,
            p_ac_2pic = p_ac_2pic,
            p_ac_3pic = p_ac_3pic,
            p_ac_4pic = p_ac_4pic,
            p_flourmill = p_flourmill,
            p_refrigerator = p_refrigerator,
            p_washingmachine = p_washingmachine,
            p_microwave = p_microwave,
            p_chimany = p_chimany,
            p_geezer_electrical_10_15 = p_geezer_electrical_10_15,
            p_geezer_electrical_20_25 = p_geezer_electrical_20_25,
            p_geezer_electrical_50_100 = p_geezer_electrical_50_100,

            )
            .enqueue(object : Callback<PriceResponse> {
                override fun onResponse(
                    call: Call<PriceResponse>,
                    response: Response<PriceResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            updatePriceLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                    updatePriceLiveData!!.value = PriceResponse(null, false)
                }

            })
    }
}