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

import com.intellij.openapi.util.IconLoader
import java.io.FileNotFoundException
import java.util.*
import javax.swing.Icon

class Resources {
    companion object {
        fun getString(key: String): String {
            val bundle: ResourceBundle = ResourceBundle.getBundle(AdbController.STRING_BUNDLE)
            return bundle.getString(key)
        }

        fun getIcon(key: String) : Icon {
            return IconLoader.findIcon("/icons/$key") ?: throw FileNotFoundException("Icon $key not found")
        }
    }
}