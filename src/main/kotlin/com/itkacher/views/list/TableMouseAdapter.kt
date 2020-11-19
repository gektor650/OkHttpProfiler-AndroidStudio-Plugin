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
package com.itkacher.views.list

import com.itkacher.Resources
import com.itkacher.data.generation.CurlRequest
import com.itkacher.util.SystemUtil
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTable


class TableMouseAdapter(val listener: TableClickListener) : MouseAdapter() {

    private fun popupEvent(e: MouseEvent) {
        val x = e.x
        val y = e.y
        val source = e.source as JTable
        val row = source.rowAtPoint(e.point)
        val model = (source.model as RequestTableModel).getRequest(row)
        val popup = JPopupMenu()
        val copyUrl = JMenuItem(Resources.getString("jtable_popup_copy_url"))
        copyUrl.addActionListener {
            if(model != null) {
                SystemUtil.copyToClipBoard(model.url)
            }
        }
        val openUrl = JMenuItem(Resources.getString("jtable_popup_open_url_in_browser"))
        openUrl.addActionListener {
            if(model != null) {
                SystemUtil.openUrlInBrowser(model.url)
            }
        }
        val copyResponse = JMenuItem(Resources.getString("jtable_popup_copy_response"))
        copyResponse.addActionListener {
            if(model != null) {
                SystemUtil.copyToClipBoard(model.getResponseBodyString())
            }
        }
        val copyCurlRequest = JMenuItem(Resources.getString("copy_curl_request"))
        copyCurlRequest.addActionListener {
            if(model != null) {
                SystemUtil.copyToClipBoard(CurlRequest(model).toString())
            }
        }
        popup.add(copyUrl)
        popup.add(copyCurlRequest)
        popup.add(openUrl)
        popup.add(copyResponse)
        popup.show(source, x, y)
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        } else {
            mouseEvent(e)
        }
    }

    private fun mouseEvent(e: MouseEvent) {
        val source = e.source as JTable
        val row = source.rowAtPoint(e.point)
        (source.model as RequestTableModel).getRequest(row)?.let {
            listener.leftButtonClick(it)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.isPopupTrigger) {
            popupEvent(e)
        }
    }
}