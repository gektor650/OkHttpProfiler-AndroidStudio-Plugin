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
package com.itkacher

import com.intellij.ide.util.PropertiesComponent

class PluginPreferences(private val preferences: PropertiesComponent) {

    fun getSelectedTabName(): String? {
        return preferences.getValue(Key.SELECTED_TAB.key)
    }

    fun setSelectedTabName(tabName: String?) {
        preferences.setValue(Key.SELECTED_TAB.key, tabName)
    }

    fun getSelectedProcessPackage(): String? {
        return preferences.getValue(Key.SELECTED_PID.key)
    }

    fun setSelectedProcessPackage(name: String?) {
        preferences.setValue(Key.SELECTED_PID.key, name)
    }

    fun getSelectedDevice(): String? {
        return preferences.getValue(Key.SELECTED_DEVICE.key)
    }

    fun setSelectedDevice(name: String?) {
        preferences.setValue(Key.SELECTED_DEVICE.key, name)
    }

    enum class Key(val key: String) {
        SELECTED_TAB("selected_tab"),
        SELECTED_PID("selected_pid"),
        SELECTED_DEVICE("selected_device")
    }
}