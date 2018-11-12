package com.itkacher

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.itkacher.data.DebugDevice
import com.itkacher.data.DebugProcess
import com.itkacher.data.MessageType
import com.itkacher.data.RequestDataSource
import com.itkacher.views.form.MainForm
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel

class DebuggerToolWindowFactory : ToolWindowFactory, DumbAware {


    private val debugger = MainForm()
    private lateinit var requestTableController: FormViewController
    private lateinit var preferences: PluginPreferences

    private val logCatListener = AndroidLogcatService.getInstance()

    private var selectedDevice: IDevice? = null
    private var selectedProcess: DebugProcess? = null

    private val deviceListener = object : AndroidLogcatService.LogcatListener {
        override fun onLogLineReceived(line: LogCatMessage) {
            val tag = line.tag
            val selected = selectedProcess
            if (selected != null && selected.pid == line.pid && tag.startsWith(TAG_KEY)) {
                val sequences = tag.split(TAG_DELIMITER)
                if (sequences.size == 3) {
                    val id = sequences[1]
                    val messageType = MessageType.fromString(sequences[2])
                    val debugRequest = RequestDataSource.logMessage(id, messageType, line.message)
                    if (debugRequest != null) {
//                        RequestDataSource.saveRequest(selected.getClientKey(), debugRequest)
                        requestTableController.insertOrUpdate(debugRequest)
                    }
                }
            }
        }

        override fun onCleared() {}
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        preferences = PluginPreferences(PropertiesComponent.getInstance(project))
        requestTableController = FormViewController(debugger, preferences, project)
        toolWindow.component.add(debugger.panel)
        initDeviceList(project)
    }

    private fun initDeviceList(project: Project) {
        AndroidDebugBridge.addDeviceChangeListener(object : AndroidDebugBridge.IDeviceChangeListener {
            override fun deviceChanged(device: IDevice?, p1: Int) {
                log("deviceChanged $device")
                device?.let {
                    attachToDevice(device)
                }
            }

            override fun deviceConnected(device: IDevice?) {
                log("deviceConnected $device")
                updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected $device")
                updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }
        })
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it.devices
            if (devices.isNotEmpty()) {
                log("addDebugBridgeChangeListener $it")
                updateDeviceList(devices)
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${it?.isConnected}")
            }
        }
        AndroidDebugBridge.addClientChangeListener { client: Client?, _: Int ->
            updateClient(client)
        }
        val bridge0: AndroidDebugBridge? = AndroidSdkUtils.getDebugBridge(project)
        log("initDeviceList bridge0 ${bridge0?.isConnected}")
    }

    private fun updateClient(client: Client?) {
        val prefSelectedPackage = preferences.getSelectedProcessPackage()
        val clientData = client?.clientData
        val clientModel = debugger.appList.model
        if (clientData != null && clientModel != null) {
            for (i in 0 until clientModel.size) {
                val model = clientModel.getElementAt(i)
                if (model.pid == clientData.pid) {
                    log("updateClient ${clientData.pid}")
                    model.packageName = clientData.packageName
                    model.clientDescription = clientData.clientDescription
                    if (model.getClientKey() == prefSelectedPackage) {
                        debugger.appList.selectedItem = model
                        selectedProcess = model
                    }
                    break
                }
            }
            debugger.appList.revalidate()
            debugger.appList.repaint()
        }
    }

    private fun updateDeviceList(devices: Array<IDevice>?) {
        log("updateDeviceList ${devices?.size}")
        val selectedDeviceName = preferences.getSelectedDevice()
        var selectedDevice: IDevice? = null
        if (devices != null) {
            debugger.mainContainer.isVisible = true
            val debugDevices = ArrayList<DebugDevice>()
            for (device in devices) {
                val debugDevice = DebugDevice(device)
                if (device.name == selectedDeviceName) {
                    selectedDevice = device
                }
                debugDevices.add(debugDevice)
            }
            val model = DefaultComboBoxModel<DebugDevice>(debugDevices.toTypedArray())
            val list = debugger.deviceList
            list.model = model
            list.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    log("Selected ${list.selectedItem}")
                    val device = list.selectedItem as DebugDevice
                    attachToDevice(device.device)
                    preferences.setSelectedDevice(device.device.name)
                    requestTableController.clear()
                }
            }
            if (selectedDevice != null) {
                attachToDevice(selectedDevice)
            } else {
                devices.firstOrNull()?.let {
                    attachToDevice(it)
                }
            }
        } else {
            debugger.mainContainer.isVisible = false
        }
    }

    private fun attachToDevice(device: IDevice) {
        createProcessList(device)
        setListener(device)
    }

    private fun createProcessList(device: IDevice) {
        val prefSelectedPackage = preferences.getSelectedProcessPackage()
        var defaultSelection: DebugProcess? = null
        val debugProcessList = ArrayList<DebugProcess>()
        log("createProcessList ${device.clients.size}")
        for (client in device.clients) {
            val clientData = client.clientData
            val process = DebugProcess(
                    clientData.pid,
                    clientData.packageName,
                    clientData.clientDescription
            )
            if (prefSelectedPackage == process.getClientKey()) {
                defaultSelection = process
            }
            log("addClient $process")
            debugProcessList.add(process)
        }
        val model = DefaultComboBoxModel<DebugProcess>(debugProcessList.toTypedArray())
        debugger.appList.model = model
        debugger.appList.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                val client = debugger.appList.selectedItem as DebugProcess
                preferences.setSelectedProcessPackage(client.getClientKey())
                defaultSelection = client
                selectedProcess = client
                log("selectedProcess $defaultSelection")
                requestTableController.clear()
//                requestTableController.addAll(RequestDataSource.getRequestList(client.getClientKey()))
            }
        }
        if (defaultSelection != null) {
            debugger.appList.selectedItem = defaultSelection
            selectedProcess = defaultSelection
        } else {
            selectedProcess = debugProcessList.firstOrNull()
        }
    }


    private fun log(text: String) {
        println(text)
    }

    private fun setListener(device: IDevice) {
        log(device.toString())
        val prevDevice = selectedDevice
        if (prevDevice != null) {
            logCatListener.removeListener(prevDevice, deviceListener)
        }
        logCatListener.addListener(device, deviceListener)
        selectedDevice = device
        val clients = device.clients
        if (clients != null) {
            for (client in clients) {
                updateClient(client)
            }
        }
    }

    companion object {
        private const val TAG_KEY = "OKPRFL"
        private const val TAG_DELIMITER = "_"
        const val STRING_BUNDLE = "strings"
    }

}
