package com.takemysigns.takemysigns.features.web

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
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
import dev.hotwire.turbo.visit.TurboVisitAction.REPLACE
import dev.hotwire.turbo.visit.TurboVisitOptions

@TurboNavGraphDestination(uri = "turbo://fragment/web")
open class WebFragment : TurboWebFragment(), NavDestination {
    var title: MutableState<String> = mutableStateOf("")
    private lateinit var appBar: ComposeView
    var actions :  MutableState<List<FyreKitMenuItem>> = mutableStateOf(emptyList())

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val main = activity as MainActivity
//
//        val onBackPressedCallback = object : OnBackPressedCallback(false) {
//            override fun handleOnBackPressed() {
//                Log.d("ActionMenu", "onBackPressed")
//                main.actions = mutableStateOf(emptyList())
//            }
//        }
//
//        main.onBackPressedDispatcher.addCallback(onBackPressedCallback)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val main = activity as MainActivity
        val tab : TabItem? = main.tabs.find { _tab ->
            BASE_URL + _tab.url == location
        }

        Log.d("WebFragment", "title (1) ----- ${title.value}")
        main.bottomNavigation.visibility = if (tab == null) View.GONE else {
            View.VISIBLE
        }

        if (shouldObserveTitleChanges()) {
            observeToolbarChanges()
            pathProperties.title?.let {
                fragmentViewModel.setTitle(it)
                title.value = it
                Log.d("ActionMenu", "Menu should be changing --- $it")
            }
        }

        Log.d("WebFragment", "title (2) ----- ${title.value}")
        Log.d("WebFragment", "currentNavDestination ----- ${sessionNavHostFragment.currentNavDestination}")

        appBar = view.findViewById(R.id.compose_view)
        appBar.setContent {
            FyreKitToolbar(title = title, actions = actions, delegate = this)
        }

        setupMenu()
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
        Log.d("WebFragment -- onVisitErrorReceived", errorCode.toString())
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
    override fun toolbarForNavigation(): Toolbar? {
//        return view?.findViewById(R.id.toolbar)
        return null
    }

//    fun topBarForNavigation(): Unit? {
//        return view?.findViewById<ComposeView>(R.id.compose_view)?.setContent {
//            FyreKitToolbar(title = title)
//        }
//    }

    private fun observeToolbarChanges() {
        fragmentViewModel.title.observe(viewLifecycleOwner) {
            title.value = it
        }
    }

    private fun setupMenu() {
    }

    override fun createWebChromeClient(): TurboWebChromeClient {
        return FyreKitWebViewChromeClient(session)
    }
}
