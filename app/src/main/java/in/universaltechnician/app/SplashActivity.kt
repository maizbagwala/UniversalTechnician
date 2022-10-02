package `in`.universaltechnician.app

import `in`.universaltechnician.app.utils.Const
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sp = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)
//        startActivity(Intent(this, MainActivity::class.java))

        Handler(mainLooper).postDelayed({
            if (sp.getBoolean(Const.IS_LOGIN, false)) {
                if (sp.getBoolean(Const.IS_ADMIN, false)) {
                    startActivity(Intent(this, AdminActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}