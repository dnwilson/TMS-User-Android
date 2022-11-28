package com.takemysigns.takemysigns.features.web

import android.util.Log
import android.webkit.JsResult
import android.webkit.WebView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.hotwire.turbo.session.TurboSession
import dev.hotwire.turbo.views.TurboWebChromeClient

class FyreKitWebViewChromeClient(session: TurboSession) : TurboWebChromeClient(session) {
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        val context = view?.context ?: return false

        Log.d("FyreKitWebViewChromeClient", "onJsAlert: $message")
        MaterialAlertDialogBuilder(context)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
                result?.confirm()
            }
            .setCancelable(false)
            .create()
            .show()

        return true
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        val context = view?.context ?: return false

        Log.d("FyreKitWebViewChromeClient", "onJsAlert: $message")
        MaterialAlertDialogBuilder(context)
            .setMessage(message)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                result?.cancel()
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
                result?.confirm()
            }
            .setCancelable(false)
            .create()
            .show()

        return true
    }
}