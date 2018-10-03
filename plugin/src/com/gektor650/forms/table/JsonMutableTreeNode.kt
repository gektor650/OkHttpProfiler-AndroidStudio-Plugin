package com.gektor650.forms.table

import javax.swing.tree.DefaultMutableTreeNode

class JsonMutableTreeNode: DefaultMutableTreeNode {

    private val isText: Boolean

    constructor(): super() {
        isText = false
    }


    constructor(name: String): super(name) {
        isText = false
    }

    constructor(name: String, value: String?): super("\"$name\":\"$value\"") {
        isText = true
    }
}