package com.takemysigns.takemysigns.main

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.takemysigns.takemysigns.features.authentication.StartPageFragment
import com.takemysigns.takemysigns.features.web.WebBottomSheetFragment
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.features.web.WebHomeFragment
import com.takemysigns.takemysigns.features.web.WebModalFragment
import com.takemysigns.takemysigns.imageviewer.ImageViewerFragment
import com.takemysigns.takemysigns.util.BASE_URL
import com.takemysigns.takemysigns.util.initDayNightTheme
import dev.hotwire.turbo.BuildConfig
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlin.reflect.KClass

@Suppress("unused")
class MainSessionNavHostFragment : TurboSessionNavHostFragment() {
    override val sessionName = "main"

    override val startLocation = BASE_URL

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebHomeFragment::class,
            WebModalFragment::class,
            WebBottomSheetFragment::class,
//            NumbersFragment::class,
//            NumberBottomSheetFragment::class,
            ImageViewerFragment::class,
            StartPageFragment::class,
        )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json"
        )

    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = customUserAgent(session.webView)
        session.webView.initDayNightTheme()
        session.webView.settings.javaScriptEnabled = true
        session.webView.addJavascriptInterface(WebAppInterface(session.webView.context), "Android")

        if (BuildConfig.DEBUG) {
            session.setDebugLoggingEnabled(true)
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    private fun customUserAgent(webView: WebView): String {
        return "Turbo Native Android ${webView.settings.userAgentString}"
    }
}
