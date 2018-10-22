package com.itkacher

import com.intellij.openapi.util.IconLoader
import java.util.*
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon

class Resources {
    companion object {
        fun getString(key: String): String {
            val bundle: ResourceBundle = ResourceBundle.getBundle(DebuggerToolWindowFactory.STRING_BUNDLE)
            return bundle.getString(key)
        }

        fun getIcon(key: String) : Icon {
            return IconLoader.getIcon("/icons/$key")
        }
    }
}