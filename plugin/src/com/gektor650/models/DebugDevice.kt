package com.gektor650.models

import com.android.ddmlib.IDevice

data class DebugDevice(
        val device: IDevice
) {
    override fun toString(): String {
        return if(device.isEmulator) {
            "Emulator ${device.avdName} Android ${device.getProperty(IDevice.PROP_BUILD_VERSION)}, API ${device.getProperty(IDevice.PROP_BUILD_API_LEVEL)}"
        } else {
            "${device.avdName} Android ${device.getProperty(IDevice.PROP_BUILD_VERSION)}, API ${device.getProperty(IDevice.PROP_BUILD_API_LEVEL)}"
        }
    }
}