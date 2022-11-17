package com.takemysigns.takemysigns.base

sealed class Routes(val name: String) {
    object Start : Routes("start_page")
    object Login : Routes("login_page")
    object Register : Routes("register_page")
    object AddCameraAccess : Routes("add_camera_access")
    object AddPushNotificationAccess : Routes("add_push_notification_access")
    object AddLocationAccess : Routes("add_location_access")
}