package com.itkacher.data

class DebugProcess(
        val pid: Int?,
        var packageName: String?,
        var clientDescription: String?) {
    override fun toString(): String {
        return if(packageName == null) {
            "Process [$pid]"
        } else {
            "$packageName[$pid]"
        }
    }
}