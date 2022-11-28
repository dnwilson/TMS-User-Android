package com.takemysigns.takemysigns.main

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.features.native.StartFragment
import com.takemysigns.takemysigns.features.web.WebBottomSheetFragment
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.features.web.WebHomeFragment
import com.takemysigns.takemysigns.features.web.WebModalFragment
import com.takemysigns.takemysigns.features.web.WebTabsFragment
import com.takemysigns.takemysigns.imageviewer.ImageViewerFragment
import com.takemysigns.takemysigns.models.ActionButton
import com.takemysigns.takemysigns.util.BASE_URL
import com.takemysigns.takemysigns.util.initDayNightTheme
import dev.hotwire.turbo.BuildConfig
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlin.reflect.KClass


@Suppress("unused")
class MainSessionNavHostFragment : TurboSessionNavHostFragment() {
    override val sessionName = "main"

    override val startLocation = if (TakeMySignsApp.isFirstRun()) "$BASE_URL/start-app" else { BASE_URL }

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebHomeFragment::class,
            WebModalFragment::class,
            WebBottomSheetFragment::class,
            WebTabsFragment::class,
            ImageViewerFragment::class,
            StartFragment::class
        )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json"
        )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = customUserAgent(session.webView)
        session.webView.initDayNightTheme()
        session.webView.settings.javaScriptEnabled = true
        session.webView.addJavascriptInterface(WebAppInterface(session), "Android")

        if (BuildConfig.DEBUG) {
            session.setDebugLoggingEnabled(true)
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    private fun customUserAgent(webView: WebView): String {
        return "Turbo Native Android ${webView.settings.userAgentString}"
    }

    fun addMenuToToolbar(menuButton: ActionButton) {
        val toolbar = session.currentVisitNavDestination?.toolbarForNavigation()
        Log.d("MainSessionNavHostFragment", "Menu item")
        toolbar?.menu?.forEach { menuItem ->
            menuItem.isVisible = true
            Log.d("MainSessionNavHostFragment", "Menu item $menuItem")
        }
//        val menu = toolbar?.menu
//        Log.d("MainSessionNavHostFragment", "addMenuToToolbar")
//
//        val activity = session.currentVisitNavDestination?.fragment?.activity as MainActivity
//        val resources = activity.resources!!
//        val packageName = activity.packageName
//        menu?.add(menuButton.name)
//            ?.setIcon(ResourcesCompat.getDrawable(resources, resources.getIdentifier("notifications", "drawable", packageName), activity.theme))
//            ?.title = null
    }
}
