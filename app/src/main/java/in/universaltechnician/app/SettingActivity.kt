package `in`.universaltechnician.app

import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.shareApp
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible

class SettingActivity : AppCompatActivity() {
    lateinit var btnLogout: TextView
    private lateinit var btnPrices: TextView
    private lateinit var btnPrivacyPolicy: TextView
    private lateinit var btnContactUs: TextView
    private lateinit var btnShareApp: TextView
    lateinit var btnPayment: TextView
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sp = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)
        btnLogout = findViewById(R.id.tv_logout)
        btnPrices = findViewById(R.id.tv_price)
        btnPrivacyPolicy = findViewById(R.id.tv_privacy)
        btnContactUs = findViewById(R.id.tv_contact)
        btnShareApp = findViewById(R.id.tv_share)
        btnPayment = findViewById(R.id.tv_payment)
        btnLogout.setOnClickListener {
            showLogoutPopUp()
        }
        btnPayment.setOnClickListener {
            startActivity(Intent(this, PaymentDetailActivity::class.java))
        }
        btnPrices.isVisible = sp.getBoolean(Const.IS_ADMIN, false)

        btnPrices.setOnClickListener {
            startActivity(Intent(this, PricesActivity::class.java))
        }
        btnPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java).putExtra("url",Const.PRIVACY_POLICY_URL))
        }
        btnContactUs.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java).putExtra("url",Const.CONTACT_US_URL))
        }
        btnShareApp.setOnClickListener { this.shareApp() }
    }

    private fun showLogoutPopUp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.logout_popup)
        dialog.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.btn_logout).setOnClickListener {
            sp.edit().clear().apply()
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TASK })
            finishAffinity()
        }
        dialog.show()
    }
}