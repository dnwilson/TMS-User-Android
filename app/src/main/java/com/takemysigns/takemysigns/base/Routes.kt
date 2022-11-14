package com.takemysigns.takemysigns.base

sealed class Routes(val name: String) {
    object Start : Routes("start_page")
    object Login : Routes("login_page")
    object Register : Routes("register_page")
}