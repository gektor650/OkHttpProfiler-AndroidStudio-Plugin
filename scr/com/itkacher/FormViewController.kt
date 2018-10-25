package com.itkacher

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import com.itkacher.data.DebugRequest
import com.itkacher.data.generation.NodeToClassesConverter
import com.itkacher.data.generation.printer.JavaModelPrinter
import com.itkacher.data.generation.printer.KotlinModelPrinter
import com.itkacher.util.SystemUtil
import com.itkacher.views.Tabs
import com.itkacher.views.TabsHelper
import com.itkacher.views.form.DataForm
import com.itkacher.views.form.MainForm
import com.itkacher.views.json.JTreeItemMenuListener
import com.itkacher.views.json.JsonMutableTreeNode
import com.itkacher.views.list.ForcedListSelectionModel
import com.itkacher.views.list.RequestTableCellRenderer
import com.itkacher.views.list.RequestTableModel
import com.itkacher.views.list.TableMouseAdapter
import java.awt.BorderLayout
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JTable


class FormViewController(private val form: MainForm, settings: PluginPreferences, private val project: Project) : JTreeItemMenuListener {

    private val dataForm = DataForm()
    private val requestTable = dataForm.requestTable
    private val requestListModel = RequestTableModel()
    private val tabsHelper = TabsHelper(dataForm.tabsPane, settings, this)
    private var firstLaunch = true

    init {
        requestTable.addMouseListener(TableMouseAdapter())
        requestTable.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        requestTable.model = requestListModel
        requestTable.setDefaultRenderer(Any::class.java, RequestTableCellRenderer())
        requestTable.selectionModel = ForcedListSelectionModel()
        requestTable.selectionModel.addListSelectionListener { it ->
            if (!it.valueIsAdjusting) {
                requestListModel.getRequest(requestTable.selectedRow)?.let {
                    fillRequestInfo(it)
                }
            }
        }
        resizeTableColumnsWidth()
        form.clearButton.addActionListener {
            requestListModel.clear()
            tabsHelper.removeAllTabs()
        }
        form.scrollToBottomButton.addActionListener {
            requestTable.clearSelection()
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount - 1, 0, true))
        }
    }

    private fun resizeTableColumnsWidth() {
        requestTable.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                val tW = requestTable.width
                val jTableColumnModel = requestTable.columnModel
                val cantCols = jTableColumnModel.columnCount
                for (i in 0 until cantCols) {
                    val column = jTableColumnModel.getColumn(i)
                    val percent = com.itkacher.views.list.TableColumn.values()[i].widthPercent
                    val colW = (tW / 100) * percent
                    column.preferredWidth = colW
                }
            }
        })
    }

    private fun fillRequestInfo(debugRequest: DebugRequest) {
        tabsHelper.removeListener()
        tabsHelper.removeAllTabs()
        tabsHelper.addRawTab(Tabs.TAB_RAW_REQUEST.resName, debugRequest.getRawRequest())
        if (debugRequest.requestHeaders.isNotEmpty()) {
            tabsHelper.addHeaderTab(Tabs.TAB_REQUEST_HEADERS.resName, debugRequest.requestHeaders)
        }
        if (debugRequest.isClosed) {
            tabsHelper.addRawTab(Tabs.TAB_RAW_RESPONSE.resName, debugRequest.getRawResponse())
            val requestJson = debugRequest.getRequestBodyString()
            tabsHelper.addJsonTab(Tabs.TAB_JSON_REQUEST.resName, requestJson)
            tabsHelper.addFormattedTab(Tabs.TAB_REQUEST_FORMATTED.resName, requestJson)

            val responseBody = debugRequest.getResponseBodyString()
            if (debugRequest.responseHeaders.isNotEmpty()) {
                tabsHelper.addHeaderTab(Tabs.TAB_RESPONSE_HEADERS.resName, debugRequest.responseHeaders)
            }
            tabsHelper.addJsonTab(Tabs.TAB_JSON_RESPONSE.resName, responseBody)
            tabsHelper.addFormattedTab(Tabs.TAB_RESPONSE_FORMATTED.resName, responseBody)
            if (debugRequest.errorMessage?.isNotEmpty() == true) {
                tabsHelper.addRawTab(Tabs.TAB_ERROR_MESSAGE.resName, debugRequest.errorMessage)
            }
        }
        tabsHelper.selectByPreference()
        tabsHelper.addListener()
    }

    fun insertOrUpdate(debugRequest: DebugRequest) {
        if (firstLaunch) {
            form.mainContainer.removeAll()
            form.mainContainer.add(dataForm.dataPanel, BorderLayout.CENTER)
            firstLaunch = false
        }
        if (requestTable.selectedColumn == -1) {
            requestTable.scrollRectToVisible(requestTable.getCellRect(requestTable.rowCount - 1, 0, true))
        }
        requestListModel.addOrUpdate(debugRequest)
    }

    fun clear() {
        requestListModel.clear()
    }

    override fun createJavaModel(node: JsonMutableTreeNode) {
        val classes = NodeToClassesConverter().buildClasses(node).getClasses()
        val javaText = JavaModelPrinter(classes).build()
        openFile("Request.java", javaText.toString())
//        FileChooser.chooseFiles(FileChooserDescriptor(true, true, false, false, false, false), project, null)
    }

    override fun createKotlinModel(node: JsonMutableTreeNode) {
        val classes = NodeToClassesConverter().buildClasses(node).getClasses()
        val kotlinText = KotlinModelPrinter(classes).build()
        openFile("Request.kt", kotlinText.toString())
    }

    override fun copyToClipboard(node: JsonMutableTreeNode) {
        SystemUtil.copyToClipBoard(node.value.toString())
    }

    override fun openInEditor(node: JsonMutableTreeNode) {
        val text = node.value.toString()
        openFile("${node.name}.json", text)
    }

    private fun openFile(className: String, classText: String) {
        val vf = LightVirtualFile(className, classText)
        val file = PsiManager.getInstance(project).findFile(vf)
        file?.let {
            file.navigate(true)
        }
//        CodeStyleManager.getInstance(project).reformatText(file, 0, file.textLength)
//        PsiManager.getInstance(project).findFile(vf)?.navigate(true)
//        FileEditorManager.getInstance(project).openFile(vf, true, true)
//        val fileType = FileTypeManager.getInstance().getKnownFileTypeOrAssociate(vf, project)
//        fileType?.let {
//            PsiFileFactory.getInstance(project).createFileFromText(className, fileType ,className).navigate(true)
//        }
    }
}