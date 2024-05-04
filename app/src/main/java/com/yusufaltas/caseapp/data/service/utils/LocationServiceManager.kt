package com.yusufaltas.caseapp.data.service.utils

object LocationServiceManager {
    private var isServiceRunning = false

    fun isServiceRunning(): Boolean {
        return isServiceRunning
    }
    fun setServiceRunning(running: Boolean) {
        isServiceRunning = running
    }
}