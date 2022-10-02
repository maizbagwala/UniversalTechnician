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

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etAddress: EditText
    lateinit var registerBtn: TextView
    private lateinit var viewModel: LoginViewModel
    lateinit var dialog: Dialog
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sp = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)

        dialog = this.myDialog()
        etName = findViewById(R.id.et_name)
        etAddress = findViewById(R.id.et_address)
        registerBtn = findViewById(R.id.register_btn)


        registerBtn.setOnClickListener {
            if (!etName.text.isNullOrEmpty() && !etAddress.text.isNullOrEmpty()) {
                if (intent.hasExtra("phone") && !intent.getStringExtra("phone").isNullOrEmpty()) {
                    registerUser(
                        intent.getStringExtra("phone").toString(),
                        etName.text.toString(),
                        etAddress.text.toString()
                    )
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "All Fields Are Required!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun registerUser(phone: String, name: String, address: String) {
        dialog.show()
        viewModel.createUser(phone, name, address)?.observe(this) {
            if (it.success && !it.data.isNullOrEmpty()) {
                sp.edit().putInt(Const.USER_ID, it.data[0].uid.toInt()).apply()
                sp.edit().putString(Const.USER_NAME, it.data[0].name).apply()
                sp.edit().putString(Const.USER_ADDRESS, it.data[0].address).apply()
                sp.edit().putString(Const.USER_PHONE, it.data[0].phone).apply()
                sp.edit().putBoolean(Const.IS_LOGIN, true).apply()
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                this@RegisterActivity.finish()
                LoginActivity().finish()
            } else {
                dialog.dismiss()
                Toast.makeText(this@RegisterActivity, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}