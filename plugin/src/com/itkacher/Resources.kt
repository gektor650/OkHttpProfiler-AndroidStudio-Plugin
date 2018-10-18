package com.itkacher

import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class Resources {
    companion object {
        fun getString(key: String): String {
            val bundle: ResourceBundle = ResourceBundle.getBundle(DebuggerToolWindowFactory.STRING_BUNDLE)
            return bundle.getString(key)
        }

        fun getIcon(classLoader: ClassLoader, key: String) : ImageIcon {
            val stream = classLoader.getResourceAsStream("/icons/$key")
            return ImageIcon(ImageIO.read(stream))
        }
    }
}