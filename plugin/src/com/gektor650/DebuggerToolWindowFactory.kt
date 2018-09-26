package com.gektor650

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.gektor650.forms.DebuggerForm
import com.gektor650.models.DebugDevice
import com.gektor650.models.DebugProcess
import com.gektor650.models.DebugRequest
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.jgoodies.common.collect.ArrayListModel
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel

class DebuggerToolWindowFactory : ToolWindowFactory {
    private val debugger = DebuggerForm()
    private val logCatListener = AndroidLogcatService.getInstance()
    private val requestListModel = ArrayListModel<DebugRequest>()
    private val logListModel = ArrayListModel<String>()

    private var selectedDevice: IDevice? = null
    private var selectedProcess: DebugProcess? = null

    private val deviceListener = object : AndroidLogcatService.LogcatListener {
        override fun onLogLineReceived(line: LogCatMessage) {
            if (selectedProcess?.pid == line.pid && line.tag == TAG_KEY) {
                val debugRequest = RequestDataSource.logMessage(line.message)
                if(! requestListModel.contains(debugRequest) && debugRequest != null) {
                    requestListModel.add(debugRequest)
                }
                if(debugger.requestList.isSelectionEmpty) {
                    debugger.requestList.ensureIndexIsVisible(requestListModel.size.minus(1))
                }
            }
        }
        override fun onCleared() {}
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.component.add(debugger.panel)
        initDeviceList(AndroidSdkUtils.getDebugBridge(project))
        debugger.requestList.model = requestListModel
        debugger.logList.model = logListModel
        debugger.scrollToBottomButton.addActionListener {
            debugger.requestList.clearSelection()
            debugger.requestList.ensureIndexIsVisible(requestListModel.size.minus(1))
        }
    }

    private fun initDeviceList(debugBridge: AndroidDebugBridge?) {
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it.devices
            if (devices.isNotEmpty()) {
                log("addDebugBridgeChangeListener $it")
                createDeviceList(devices)
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${debugBridge?.isConnected}")
            }
        }
    }

    private fun createDeviceList(devices: Array<IDevice>) {
        log("createDeviceList ${devices.size}")
        val debugDevices = ArrayList<DebugDevice>()
        for (device in devices) {
            debugDevices.add(DebugDevice(device))
        }
        val model = DefaultComboBoxModel<DebugDevice>(debugDevices.toTypedArray())
        val list = debugger.deviceList
        list.model = model
        list.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                val device = list.selectedItem as DebugDevice
                attachToDevice(device.device)
            }
        }
        attachToDevice(devices[0])
    }

    private fun attachToDevice(device: IDevice) {
        createProcessList(device)
        setListener(device)
        attach(device)
    }

    private fun createProcessList(device: IDevice) {
        val debugProcessList = ArrayList<DebugProcess>()
        log("createProcessList ${device.clients.size}")
        for (client in device.clients) {
            val clientData = client.clientData
            val process = DebugProcess(
                    clientData.pid,
                    clientData.packageName,
                    clientData.clientDescription
            )
            log("addClient $process")
            debugProcessList.add(process)
        }
        val model = DefaultComboBoxModel<DebugProcess>(debugProcessList.toTypedArray())
        debugger.appList.model = model
        debugger.appList.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                val client = debugger.appList.selectedItem as DebugProcess
                selectedProcess = client
                log("selectedProcess $selectedProcess")
                requestListModel.clear()
            }
        }
        if (debugProcessList.isNotEmpty()) {
            selectedProcess = debugProcessList[0]
        }
    }

    private fun attach(device: IDevice) {
        log(device.toString())
        setListener(device)
    }

    private fun log(text: String) {
        logListModel.add(text)
    }

    private fun setListener(device: IDevice) {
        val prevDevice = selectedDevice
        if(prevDevice != null) {
            logCatListener.removeListener(prevDevice, deviceListener)
            debugger.logList.ensureIndexIsVisible(requestListModel.size.minus(1))
        }
        logCatListener.addListener(device, deviceListener)
        selectedDevice = device
    }

    companion object {
        private const val TAG_KEY = "_OKPRFL"
    }

}
