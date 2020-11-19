/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            val deviceName = device.name.safe().replace("_", " ")
            val shortened = if(deviceName.length > 20) {
                deviceName.substring(0, 20)
            } else {
                deviceName
            }
            "${shortened.capitalize()} Android ${device.getProperty(IDevice.PROP_BUILD_VERSION).safe()}, API ${device.getProperty(IDevice.PROP_BUILD_API_LEVEL).safe()}"
        }
    }
}