package com.itkacher.data

data class DebugProcess(
        val pid: Int?,
        val packageName: String?,
        val clientDescription: String?) {
    override fun toString(): String {
        return "$packageName[$pid]"
    }
}