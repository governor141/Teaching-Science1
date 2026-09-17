package com.teachingscience.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(webView)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/www/index.html")

        // Let in-app screens (term list, scheme list) handle back navigation first.
        onBackPressedDispatcher.addCallback(this) {
            webView.evaluateJavascript(
                "(function(){" +
                    "var active=document.querySelector('.screen.active');" +
                    "if(active && (active.id==='term-screen' || active.id==='scheme-screen')){" +
                    "window.dispatchEvent(new Event('ts:back'));return 'handled';}" +
                    "return 'exit';" +
                    "})();"
            ) { result ->
                if (result != null && result.contains("exit")) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
    }
}
