package com.takemysigns.takemysigns.main

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.compose.ui.graphics.Color
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.models.AppButton
import com.takemysigns.takemysigns.models.FyreKitMenuItem
import com.takemysigns.takemysigns.ui.fab.MultiFabItem
import dev.hotwire.turbo.session.TurboSession

data class FabButton(
    val title: String,
    val name: String,
    val icon: String,
    val url: String?,
    val buttons: List<AppButton>?
)


class WebAppInterface(private val session: TurboSession) {
    @JavascriptInterface
    fun addMenu(jsonData: String) {
        val types = Types.newParameterizedType(List::class.java, FyreKitMenuItem::class.java)
        val adapter: JsonAdapter<List<FyreKitMenuItem>> = TakeMySignsApp.moshi.adapter(types)

        val fragment = session.currentVisitNavDestination?.fragment as WebFragment
        fragment.actions.value = adapter.fromJson(jsonData)!!
    }
}
