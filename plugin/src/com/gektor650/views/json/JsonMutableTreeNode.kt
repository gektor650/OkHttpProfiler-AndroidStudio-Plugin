package com.gektor650.views.json

import com.fasterxml.jackson.databind.JsonNode
import javax.swing.tree.DefaultMutableTreeNode

class JsonMutableTreeNode : DefaultMutableTreeNode {

    val type: NodeType

    constructor(name: String, type: NodeType) : super(name) {
        this.type = type
    }

    constructor(name: String, value: JsonNode?) : super() {
        when {
            value?.isTextual == true -> {
                type = NodeType.STRING
                setUserObject("\"$name\":\"${value.textValue()}\"")
                return
            }
            value?.isNumber == true -> {
                type = NodeType.NUMBER
                setUserObject("\"$name\":${value.numberValue()}")
                return
            }
            value?.isBoolean == true -> {
                type = NodeType.BOOLEAN
                setUserObject("\"$name\":${value.booleanValue()}")
                return
            }
            value?.isNull == true -> {
                type = NodeType.NULL
                setUserObject("\"$name\":null")
            }
            else -> {
                type = NodeType.OBJECT
            }
        }
    }

    enum class NodeType(val nodeName: String) {
        STRING("String"),
        NUMBER("Number"),
        NULL("Null"),
        BOOLEAN("Boolean"),
        OBJECT("Object"),
        ARRAY("Array"),
    }
}