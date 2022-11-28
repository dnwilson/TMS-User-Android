package com.takemysigns.takemysigns.features.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.models.TabItem
import com.takemysigns.takemysigns.util.BASE_URL
import dev.hotwire.turbo.fragments.TurboFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import dev.hotwire.turbo.visit.TurboVisitAction
import dev.hotwire.turbo.visit.TurboVisitOptions


@TurboNavGraphDestination(uri = "turbo://fragment/web/tabs")
class WebTabsFragment : TurboFragment() {
    private var tabs = listOf<TabItem>()
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation.setOnItemSelectedListener { item ->
            Log.d("WebFragments", "Go to ${tabs[item.itemId].url}")
            replace(tabs[item.itemId].url)
            true
        }
    }

    private fun replace(path: String) {
        Log.d("WebFragments", "Go to full url ${BASE_URL + path}")
        navigate(BASE_URL + path, options = TurboVisitOptions(action = TurboVisitAction.REPLACE))
    }
}