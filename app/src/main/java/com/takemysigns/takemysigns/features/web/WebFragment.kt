package com.takemysigns.takemysigns.features.web

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.NavDestination
import com.takemysigns.takemysigns.util.CHECK_AUTH_URL
import com.takemysigns.takemysigns.util.SIGN_IN_URL
import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import dev.hotwire.turbo.visit.TurboVisitAction.REPLACE
import dev.hotwire.turbo.visit.TurboVisitOptions

@TurboNavGraphDestination(uri = "turbo://fragment/web")
open class WebFragment : TurboWebFragment(), NavDestination {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    override fun onFormSubmissionStarted(location: String) {
        menuProgress?.isVisible = true
    }

    override fun onFormSubmissionFinished(location: String) {
        menuProgress?.isVisible = false
    }

    override fun onVisitErrorReceived(location: String, errorCode: Int) {
        when (errorCode) {
            401 -> navigate(CHECK_AUTH_URL, TurboVisitOptions(action = REPLACE))
            else -> super.onVisitErrorReceived(location, errorCode)
        }
    }

    private fun setupMenu() {
        toolbarForNavigation()?.inflateMenu(R.menu.web)
    }
}
