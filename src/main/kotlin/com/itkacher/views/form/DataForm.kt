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

import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.table.JBTable
import com.itkacher.views.FrameScrollPanel
import com.itkacher.views.SimpleJBPanel
import java.awt.BorderLayout

class DataForm {
    val dataPanel = SimpleJBPanel()
    val requestTable = JBTable()
    val tabsPane = JBTabbedPane()


    init {
        val panel1 = FrameScrollPanel(requestTable)
        val panel2 = SimpleJBPanel()
        panel2.add(tabsPane, BorderLayout.CENTER)
        val splitter = JBSplitter(true)
        splitter.proportion = 0.5f
        splitter.firstComponent = panel1
        splitter.secondComponent = panel2
        dataPanel.add(splitter)
    }

}
