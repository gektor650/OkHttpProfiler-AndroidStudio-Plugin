package com.itkacher

import com.intellij.ide.util.PropertiesComponent

class PluginPreferences(private val preferences: PropertiesComponent) {

    fun getSelectedTabName(): String? {
        return preferences.getValue(Key.SELECTED_TAB.key)
    }

    fun setSelectedTabName(tabName: String?) {
        preferences.setValue(Key.SELECTED_TAB.key, tabName)
    }

    enum class Key(val key: String) {
        SELECTED_TAB("selected_tab")
    }
}