package `in`.universaltechnician.app

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    lateinit var wb: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        wb = findViewById(R.id.webView)
        loadPage(intent.getStringExtra("url"))
    }

    private fun loadPage(Url: String?) {
        wb.setBackgroundColor(Color.TRANSPARENT)
        wb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url!!)
                return true
            }
        }
        wb.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {

                } else {

                }
            }
        }
        wb.settings.loadsImagesAutomatically = true
        wb.settings.javaScriptEnabled = true
        wb.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        wb.loadUrl(Url!!)
    }
}