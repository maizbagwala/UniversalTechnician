package `in`.universaltechnician.app

import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Const.TAG
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.LoginViewModel
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    lateinit var phone: EditText
    lateinit var auth: FirebaseAuth
    lateinit var verificationCode: String
    lateinit var loginBtn: TextView
    lateinit var otpBtn: TextView
    lateinit var etOtp: EditText
    lateinit var loginViewModel: LoginViewModel
    lateinit var dialog: Dialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ccp: CountryCodePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)

        dialog = this.myDialog()
        auth = FirebaseAuth.getInstance()
        phone = findViewById(R.id.phone)
        otpBtn = findViewById(R.id.send_otp_btn)
        loginBtn = findViewById(R.id.login_btn)
        etOtp = findViewById(R.id.et_otp)
        ccp = findViewById(R.id.ccp)
        otpBtn.setOnClickListener {
            if (!phone.text.isNullOrEmpty()) {
                dialog.show()
                getOTP(ccp.selectedCountryCodeWithPlus + phone.text.toString())
            } else {
                Toast.makeText(this, "please enter phone", Toast.LENGTH_SHORT).show()
            }
        }
        loginBtn.setOnClickListener {
            if (!etOtp.text.isNullOrEmpty() && !phone.text.isNullOrEmpty()) {
                dialog.show()
                verifyOtp(etOtp.text.toString())
            } else {
                Toast.makeText(this, "please enter OTP/phone", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.admin_login_btn).setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }

    private fun verifyOtp(otp: String) {
        val cred = PhoneAuthProvider.getCredential(verificationCode, otp)
        auth.signInWithCredential(cred).addOnCompleteListener {
            if (it.isSuccessful) {
                checkUserExist(ccp.selectedCountryCodeWithPlus + phone.text.toString())
            } else {
                dialog.dismiss()
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserExist(phone: String) {
        loginViewModel.checkUser(phone)!!.observe(this) {

            dialog.dismiss()
            if (it.data.isNullOrEmpty()) {
                startActivity(Intent(this, RegisterActivity::class.java).putExtra("phone", phone))
            } else {
                sharedPreferences.edit().putInt(Const.USER_ID, it.data[0].uid.toInt()).apply()
                sharedPreferences.edit().putString(Const.USER_NAME, it.data[0].name).apply()
                sharedPreferences.edit().putString(Const.USER_PHONE, it.data[0].phone).apply()
                sharedPreferences.edit().putString(Const.USER_ADDRESS, it.data[0].address).apply()
                sharedPreferences.edit().putBoolean(Const.IS_LOGIN, true).apply()
                startActivity(Intent(this, MainActivity::class.java))
                this@LoginActivity.finish()
            }
        }
    }

    private fun getOTP(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    dialog.dismiss()
                    Log.d(TAG, "onVerificationCompleted: verification Complete")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, "${p0.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onVerificationFailed: verification Failed\n$p0")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationCode = p0
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Code Sent", Toast.LENGTH_SHORT).show()
                    startTimer()
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun startTimer() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(p0: Long) {
                val hour: Long = p0 / 3600000 % 24
                val min: Long = p0 / 60000 % 60
                val sec: Long = p0 / 1000 % 60
                otpBtn.text = "$min : $sec"
                otpBtn.isEnabled = false
            }

            override fun onFinish() {
                otpBtn.isEnabled = true
                otpBtn.text = "Resend OTP"
            }
        }.start()

    }
}
