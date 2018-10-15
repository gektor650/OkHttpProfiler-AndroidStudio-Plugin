package com.gektor650.data

data class DebugProcess(
        val pid: Int?,
        val packageName: String?,
        val clientDescription: String?) {
    override fun toString(): String {
        return "$packageName[$pid]"
    }
}