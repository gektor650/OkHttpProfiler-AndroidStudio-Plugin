package com.itkacher

import com.intellij.openapi.util.IconLoader
import java.util.*
import javax.swing.Icon

class Resources {
    companion object {
        fun getString(key: String): String {
            val bundle: ResourceBundle = ResourceBundle.getBundle(AdbController.STRING_BUNDLE)
            return bundle.getString(key)
        }

        fun getIcon(key: String) : Icon {
            return IconLoader.getIcon("/icons/$key")
        }
    }
}