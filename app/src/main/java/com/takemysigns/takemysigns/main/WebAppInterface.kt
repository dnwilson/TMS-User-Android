package com.takemysigns.takemysigns.main

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.compose.runtime.mutableStateOf
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.models.FyreKitMenuItem
import dev.hotwire.turbo.fragments.TurboFragment
import dev.hotwire.turbo.session.TurboSession


class WebAppInterface(private val session: TurboSession) {
    @JavascriptInterface
    fun addMenu(jsonData: String) {
        val types = Types.newParameterizedType(List::class.java, FyreKitMenuItem::class.java)
        val adapter: JsonAdapter<List<FyreKitMenuItem>> = TakeMySignsApp.moshi.adapter(types)

        val fragment = session.currentVisitNavDestination?.fragment as WebFragment
        fragment.actions.value = adapter.fromJson(jsonData)!!
        Log.d("ActionMenu", "Set actions -- ${fragment.actions.value}")
    }
}
