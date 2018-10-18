package com.itkacher.views.list

import javax.swing.*

class ForcedListSelectionModel : DefaultListSelectionModel() {
    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }

    override fun clearSelection() {}

    override fun removeSelectionInterval(index0: Int, index1: Int) {}

}