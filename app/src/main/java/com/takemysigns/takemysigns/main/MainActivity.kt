package com.takemysigns.takemysigns.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.models.TabItem
import com.takemysigns.takemysigns.util.BASE_URL
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.visit.TurboVisitAction
import dev.hotwire.turbo.visit.TurboVisitOptions

class MainActivity : AppCompatActivity(), TurboActivity {
    override lateinit var delegate: TurboActivityDelegate
    var tabs = listOf<TabItem>()
    lateinit var bottomNavigation : BottomNavigationView

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        delegate = TurboActivityDelegate(this, R.id.main_nav_host)
        bottomNavigation = findViewById(R.id.bottom_nav)

        if (!TakeMySignsApp.isFirstRun()) {
            Log.d("MainActivity", "App Started...")
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val types = Types.newParameterizedType(List::class.java, TabItem::class.java)
            val adapter: JsonAdapter<List<TabItem>> = moshi.adapter(types)

            val session = delegate.currentSessionNavHostFragment.session
            tabs = adapter.fromJson(session.pathConfiguration.settings["tabs"].toString())!!
            val menu: Menu = bottomNavigation.menu
            tabs.forEachIndexed { index, tab ->
                menu.add(Menu.NONE, index, index, tab.label)
                    .setIcon(ResourcesCompat.getDrawable(resources, resources.getIdentifier(tab.icon, "drawable", packageName), theme))
                    .title = null
            }
        } else {
            bottomNavigation.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation.setOnItemSelectedListener { item ->
            replace(tabs[item.itemId].url)
            true
        }
    }

    private fun replace(path: String) {
        Log.d("WebFragments", "Go to full url ${BASE_URL + path}")
        delegate.clearBackStack(onCleared = {
            Log.d("WebFragments", "Back stack is cleared")
            delegate.navigate(BASE_URL + path, options = TurboVisitOptions(action = TurboVisitAction.REPLACE))
        })
    }
}
