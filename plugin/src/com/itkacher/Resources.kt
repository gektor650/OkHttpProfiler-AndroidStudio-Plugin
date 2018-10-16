package com.itkacher

import java.util.*

class Resources {
    companion object {
        fun getString(key: String): String {
            val bundle: ResourceBundle = ResourceBundle.getBundle(DebuggerToolWindowFactory.STRING_BUNDLE)
            return bundle.getString(key)
        }
    }
}