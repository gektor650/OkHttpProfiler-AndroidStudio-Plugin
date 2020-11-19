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