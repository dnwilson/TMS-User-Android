package com.takemysigns.takemysigns.features.web

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.NavDestination
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.main.MainActivity
import com.takemysigns.takemysigns.models.FyreKitMenuItem
import com.takemysigns.takemysigns.models.TabItem
import com.takemysigns.takemysigns.ui.FyreKitToolbar
import com.takemysigns.takemysigns.util.BASE_URL
import com.takemysigns.takemysigns.util.CHECK_AUTH_URL
import com.takemysigns.takemysigns.util.SIGN_IN_URL
import dev.hotwire.turbo.config.title
import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import dev.hotwire.turbo.views.TurboWebChromeClient
import dev.hotwire.turbo.visit.TurboVisitAction
import dev.hotwire.turbo.visit.TurboVisitAction.REPLACE
import dev.hotwire.turbo.visit.TurboVisitOptions

@TurboNavGraphDestination(uri = "turbo://fragment/web")
open class WebFragment : TurboWebFragment(), NavDestination {
    private lateinit var appBar: ComposeView
    private lateinit var fab: FloatingActionButton
    var title: MutableState<String> = mutableStateOf("")
    var actions :  MutableState<List<FyreKitMenuItem>> = mutableStateOf(emptyList())
//    private lateinit var mainFab: ExtendedFloatingActionButton
//    private lateinit var directionalFab: FloatingActionButton
//    private lateinit var promotionalFab: FloatingActionButton
//    private lateinit var directionalFabText: TextView
//    private lateinit var promotionalFabText: TextView
//    private var showFabs: Boolean = true

//    private fun toggleTabs(visible: Boolean) {
//        mainFab.visibility = if (visible) View.VISIBLE else { View.GONE }
//        listOf(directionalFab, promotionalFab, directionalFabText, promotionalFabText)
//            .forEach { view ->
//                view.visibility = if (!visible) View.VISIBLE else { View.GONE }
//            }
//    }

//    private fun setupFloatingActionButtons() {
//        mainFab.shrink()
//
//        mainFab.setOnClickListener {
//            showFabs = if (!showFabs!!) {
//                directionalFab.show()
//                promotionalFab.show()
//                directionalFabText.visibility = View.VISIBLE
//                promotionalFabText.visibility = View.VISIBLE
//
//                mainFab.extend()
//
//                true
//            } else {
//
//                directionalFab.hide()
//                promotionalFab.hide()
//                directionalFabText.visibility = View.GONE
//                promotionalFabText.visibility = View.GONE
//
//                mainFab.shrink()
//
//                false
//            }
//        }
//
//        directionalFab.setOnClickListener {
//            navigate("$BASE_URL/orders/new?order%5Bservice_id%5D=1", options = TurboVisitOptions(action = TurboVisitAction.REPLACE))
//        }
//
//        promotionalFab.setOnClickListener {
//            navigate("$BASE_URL/orders/new?order%5Bservice_id%5D=2", options = TurboVisitOptions(action = TurboVisitAction.REPLACE))
//        }
//    }

    private fun observeToolbarChanges() {
        fragmentViewModel.title.observe(viewLifecycleOwner) {
            title.value = it
        }
    }

    private fun toggleFab() {
        if (location == BASE_URL) { fab.show() } else { fab.hide() }
    }

    fun hasBackStack() : Boolean {
        Log.d("LOCATION", "location: $location --- previousLocation: $previousLocation")
        val main = activity as MainActivity
        return main.tabs.none { tabItem -> location == BASE_URL + tabItem.url }
    }

    override fun createWebChromeClient(): TurboWebChromeClient {
        return FyreKitWebViewChromeClient(session)
    }

    override fun onVisitStarted(location: String) {
        super.onVisitStarted(location)

        toggleFab()
    }

    override fun onVisitCompleted(location: String, completedOffline: Boolean) {
        super.onVisitCompleted(location, completedOffline)

        toggleFab()

        if (!TakeMySignsApp.pushTokenSaved() && TakeMySignsApp.getPushToken()?.isNotBlank() == true) {
            val script = "window.bridge.register('${TakeMySignsApp.getPushToken()}', 'android');"
            session.webView.evaluateJavascript(script) {
                TakeMySignsApp.setPushTokenSaved(true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val main = activity as MainActivity
        val tab : TabItem? = main.tabs.find { _tab ->
            BASE_URL + _tab.url == location
        }

        main.bottomNavigation.visibility = if (tab == null) View.GONE else {
            View.VISIBLE
        }

        if (shouldObserveTitleChanges()) {
            observeToolbarChanges()
            pathProperties.title?.let {
                fragmentViewModel.setTitle(it)
                title.value = it
            }
        }

        appBar = view.findViewById(R.id.compose_view)
        appBar.setContent {
            FyreKitToolbar(title = title, actions = actions, delegate = this)
        }

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            navigate("$BASE_URL/orders/new")
        }

//        directionalFab = view.findViewById(R.id.add_directional_fab)
//        promotionalFab = view.findViewById(R.id.add_promotional_fab)
//        directionalFabText = view.findViewById(R.id.directional_fab_text)
//        promotionalFabText = view.findViewById(R.id.promotional_fab_text)

//        mainFab.visibility = View.VISIBLE
//        setupFloatingActionButtons()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web_home, container, false)
    }

    override fun onFormSubmissionStarted(location: String) {
        menuProgress?.isVisible = true
    }

    override fun onFormSubmissionFinished(location: String) {
        menuProgress?.isVisible = false
    }

    override fun onVisitErrorReceived(location: String, errorCode: Int) {
        when (errorCode) {
            401 -> {
                if (TakeMySignsApp.isLoggedIn()) {
                    navigate(CHECK_AUTH_URL, TurboVisitOptions(action = REPLACE))
                } else {
                    TakeMySignsApp.setAuthToken("")
                    navigate(SIGN_IN_URL, TurboVisitOptions(action = REPLACE))
                }
            }
            else -> super.onVisitErrorReceived(location, errorCode)
        }
    }
}
