package com.itkacher.data

import com.android.ddmlib.IDevice
import com.itkacher.extension.safe

data class DebugDevice(
        val device: IDevice
) {
    override fun toString(): String {
        return if(device.isEmulator) {
            "Emulator ${device.avdName.safe()} Android ${device.getProperty(IDevice.PROP_BUILD_VERSION).safe()}, API ${device.getProperty(IDevice.PROP_BUILD_API_LEVEL).safe()}"
        } else {
            val name = device.name.safe().replace("_", " ").substring(0, 20).capitalize()
            "$name Android ${device.getProperty(IDevice.PROP_BUILD_VERSION).safe()}, API ${device.getProperty(IDevice.PROP_BUILD_API_LEVEL).safe()}"
        }
    }
}