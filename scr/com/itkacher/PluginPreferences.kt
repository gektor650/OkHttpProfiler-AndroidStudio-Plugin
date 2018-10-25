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