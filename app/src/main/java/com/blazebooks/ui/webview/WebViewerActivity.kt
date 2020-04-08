package com.blazebooks.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.blazebooks.R
import kotlinx.android.synthetic.main.activity_web_viewer.*

class WebViewerActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)

        progress_circular_webviewer.visibility = View.VISIBLE

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.setGeolocationEnabled(false);

        val url: String = "file:///android_asset/index.html"
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        progress_circular_webviewer.visibility = View.GONE
    }
}
