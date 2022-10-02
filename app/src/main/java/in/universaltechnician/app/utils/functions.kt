package `in`.universaltechnician.app.utils

import `in`.universaltechnician.app.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

object Functions {
    fun Context.myDialog(showText:Boolean=false):Dialog{
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.loader)
        dialog.findViewById<TextView>(R.id.txt_loader).isVisible=showText
        dialog.setCancelable(false)
        return dialog
    }

    fun Activity.checkPermission(permission:String): Boolean {
        return if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1234)
            false
        }else{
            true
        }
    }

    fun Activity.makeACall(num:String){
        startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:${num}")))
    }
    fun Activity.shareApp() {
        val appLink = "https://play.google.com/store/apps/details?id=${this.packageName}"
        val sendInt = Intent(Intent.ACTION_SEND)
        sendInt.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name))
        sendInt.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.share_app_message) + appLink)
        sendInt.type = "text/plain"
        this.startActivity(Intent.createChooser(sendInt, "Share"))
    }
}