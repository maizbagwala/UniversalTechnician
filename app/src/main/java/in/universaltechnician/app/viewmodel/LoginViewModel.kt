package `in`.universaltechnician.app.viewmodel

import `in`.universaltechnician.app.model.UserResponse
import `in`.universaltechnician.app.network.RetrofitClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private var checkUserLiveData: MutableLiveData<UserResponse>? = null

    fun checkUser(phone: String): MutableLiveData<UserResponse>? {
        if (checkUserLiveData == null) {
            checkUserLiveData = MutableLiveData()
        }

        callCheckUser(phone)

        return checkUserLiveData
    }

    private fun callCheckUser(phone: String) {
        RetrofitClient.getClient().checkUser(phone = phone)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            checkUserLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    checkUserLiveData!!.value = UserResponse(null, false)
                }

            })
    }

    private var createUserLiveData: MutableLiveData<UserResponse>? = null

    fun createUser(phone: String, name: String, address: String): MutableLiveData<UserResponse>? {
        if (createUserLiveData == null) {
            createUserLiveData = MutableLiveData()
        }

        callCreateUser(phone, name, address)

        return createUserLiveData
    }

    private fun callCreateUser(phone: String, name: String, address: String) {
        RetrofitClient.getClient().createUser(phone = phone, name = name, address = address)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            createUserLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    createUserLiveData!!.value = UserResponse(null, false)
                }

            })
    }


    private var loginAdminLiveData: MutableLiveData<UserResponse>? = null

    fun loginAdmin(username: String, password: String): MutableLiveData<UserResponse>? {
        if (loginAdminLiveData == null) {
            loginAdminLiveData = MutableLiveData()
        }

        callLoginAdmin(username, password)

        return loginAdminLiveData
    }

    private fun callLoginAdmin(username: String, password: String) {
        RetrofitClient.getClient().checkAdmin(username = username, password = password)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            loginAdminLiveData!!.value = response.body()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    loginAdminLiveData!!.value = UserResponse(null, false)
                }

            })
    }
}