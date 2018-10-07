package com.gektor650

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.gektor650.views.DebuggerForm
import com.gektor650.views.FormViewController
import com.gektor650.models.DebugDevice
import com.gektor650.models.DebugProcess
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel

class DebuggerToolWindowFactory : ToolWindowFactory {


    private val debugger = DebuggerForm()
    private val requestTableController = FormViewController(debugger)

    private val logCatListener = AndroidLogcatService.getInstance()

    private var selectedDevice: IDevice? = null
    private var selectedProcess: DebugProcess? = null

    private val deviceListener = object : AndroidLogcatService.LogcatListener {
        override fun onLogLineReceived(line: LogCatMessage) {
            val tag = line.tag
            if (selectedProcess?.pid == line.pid && tag.startsWith(TAG_KEY)) {
                val sequences = tag.split(TAG_DELIMITER)
                if (sequences.size == 3) {
                    val id = sequences[1]
                    val messageType = MessageType.fromString(sequences[2])
                    val debugRequest = RequestDataSource.logMessage(id, messageType, line.message)
                    if (debugRequest != null) {
                        requestTableController.insertOrUpdate(debugRequest)
                    }
                }
            }
        }

        override fun onCleared() {}
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.component.add(debugger.panel)
        initDeviceList()
        debugger.scrollToBottomButton.addActionListener {
            requestTableController.clearSelection()
        }
    }

    private fun initDeviceList() {
        AndroidDebugBridge.addClientChangeListener { client: Client, i: Int ->
            log("addClientChangeListener ${client.device}")
            createProcessList(client.device)
        }
        AndroidDebugBridge.addDeviceChangeListener(object: AndroidDebugBridge.IDeviceChangeListener {
            override fun deviceChanged(device: IDevice?, p1: Int) {
                log("deviceChanged $device")
            }

            override fun deviceConnected(device: IDevice?) {
                log("deviceConnected $device")

            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected $device")
            }
        })
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it.devices
            if (devices.isNotEmpty()) {
                log("addDebugBridgeChangeListener $it")
                createDeviceList(devices)
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${it?.isConnected}")
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
                requestTableController.clear()
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
        println(text)
    }

    private fun setListener(device: IDevice) {
        val prevDevice = selectedDevice
        if (prevDevice != null) {
            logCatListener.removeListener(prevDevice, deviceListener)
        }
        logCatListener.addListener(device, deviceListener)
        selectedDevice = device
    }

    companion object {
        private const val TAG_KEY = "OKPRFL"
        private const val TAG_DELIMITER = "_"
    }

}
