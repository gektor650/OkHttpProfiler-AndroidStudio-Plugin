package com.gektor650

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.gektor650.views.form.DebuggerForm
import com.gektor650.data.DebugDevice
import com.gektor650.data.DebugProcess
import com.gektor650.data.MessageType
import com.gektor650.data.RequestDataSource
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
        initDeviceList(project)
        debugger.scrollToBottomButton.addActionListener {
            requestTableController.clearSelection()
        }
    }

    private fun initDeviceList(project: Project) {
//        AndroidDebugBridge.addClientChangeListener { client: Client, _: Int ->
//            log("addClientChangeListener ${client.device}")
//            createProcessList(client.device)
//        }
        AndroidDebugBridge.addDeviceChangeListener(object: AndroidDebugBridge.IDeviceChangeListener {
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
        val bridge0 : AndroidDebugBridge? = AndroidSdkUtils.getDebugBridge(project)
        log("initDeviceList bridge0 ${bridge0?.isConnected}")
    }

    private fun updateDeviceList(devices: Array<IDevice>?) {
        log("updateDeviceList ${devices?.size}")
        if(devices != null) {
            debugger.mainContainer.isVisible = true
            val debugDevices = ArrayList<DebugDevice>()
            for (device in devices) {
                debugDevices.add(DebugDevice(device))
            }
            val model = DefaultComboBoxModel<DebugDevice>(debugDevices.toTypedArray())
            val list = debugger.deviceList
            list.model = model
            list.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    log("Selected ${list.selectedItem}")
                    val device = list.selectedItem as DebugDevice
                    attachToDevice(device.device)
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
    }

    companion object {
        private const val TAG_KEY = "OKPRFL"
        private const val TAG_DELIMITER = "_"
        const val STRING_BUNDLE = "strings"
    }

}
