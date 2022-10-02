package `in`.universaltechnician.app

import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.LoginViewModel
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class AdminLoginActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var loginViewModel: LoginViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)
        dialog = this.myDialog()

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        findViewById<TextView>(R.id.login_btn).setOnClickListener {

            if (!etUsername.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()) {
                if (etUsername.text.toString()=="test@gmail.com" && etPassword.text.toString()=="test123test"){
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().putString(Const.ADMIN_USERNAME, "Test Admin")
                        .apply()
                    sharedPreferences.edit().putBoolean(Const.IS_TEST_ADMIN, true).apply()
                    sharedPreferences.edit().putBoolean(Const.IS_LOGIN, true).apply()
                    sharedPreferences.edit().putBoolean(Const.IS_ADMIN, true).apply()
                    startActivity(Intent(this, AdminActivity::class.java))
                    finishAffinity()
                }else{
                    adminLogin(etUsername.text.toString(), etPassword.text.toString())
                }
            } else {
                Toast.makeText(this, "Username or Password is required", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun adminLogin(username: String, password: String) {
        dialog.show()
        loginViewModel.loginAdmin(username, password)?.observe(this) {
            dialog.dismiss()
            when {
                it.data == null -> {
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
                it.success -> {
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().putString(Const.ADMIN_USERNAME, it.data[0].username)
                        .apply()
                    sharedPreferences.edit().putBoolean(Const.IS_LOGIN, true).apply()
                    sharedPreferences.edit().putBoolean(Const.IS_ADMIN, true).apply()
                    startActivity(Intent(this, AdminActivity::class.java))
                    finishAffinity()
                }
                else -> {
                    Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }
}