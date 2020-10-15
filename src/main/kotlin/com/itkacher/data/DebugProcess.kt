package com.itkacher.data

class DebugProcess(
        val pid: Int?,
        var packageName: String?,
        var clientDescription: String?) {

    fun getClientKey(): String {
        return "$packageName$clientDescription"
    }

    override fun toString(): String {
        return if(packageName == null && clientDescription == null) {
            "Process [$pid]"
        } else if(clientDescription?.isNotEmpty() == true) {
            "$clientDescription [$pid]"
        } else {
            "$packageName [$pid]"
        }
    }
}