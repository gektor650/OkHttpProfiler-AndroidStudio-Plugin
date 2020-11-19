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
package com.itkacher.views.form

import com.intellij.ui.treeStructure.Tree
import com.itkacher.views.FrameScrollPanel
import java.awt.Font

class JsonTreeForm {
    val tree = Tree()
    val treePanel = FrameScrollPanel(tree)

    init {
        tree.font = Font(Font.MONOSPACED, Font.PLAIN, 12)
    }
}
