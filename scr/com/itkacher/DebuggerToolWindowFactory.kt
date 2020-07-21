package com.itkacher

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.itkacher.views.form.MainForm

class DebuggerToolWindowFactory : ToolWindowFactory, DumbAware {

    private var adbController: AdbController? = null;

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val preferences = PluginPreferences(PropertiesComponent.getInstance(project))
        val mainForm = MainForm()
        adbController = AdbController(mainForm, project, preferences, toolWindow)
        toolWindow.component.add(mainForm.panel)
    }
}
