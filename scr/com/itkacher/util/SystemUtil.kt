package com.itkacher.util

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

class SystemUtil {
    companion object {
        fun copyToClipBoard(text: String?) {
            if (text != null) {
                val stringSelection = StringSelection(text)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            }
        }

        fun openUrlInBrowser(url: String?) {
            if (url != null) {
                try {
                    Desktop.getDesktop().browse(URI.create(url))
                } catch (e: IllegalArgumentException) {

                }
            }
        }
    }
}