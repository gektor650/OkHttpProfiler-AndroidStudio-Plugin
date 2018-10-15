package com.gektor650.views.json

import com.fasterxml.jackson.databind.JsonNode
import javax.swing.tree.DefaultMutableTreeNode

class JsonMutableTreeNode : DefaultMutableTreeNode {

    private val type: NodeType
    private val name: String
    private val value: JsonNode?
    private val formattedText: String

    constructor(name: String, type: NodeType) : super(name) {
        this.type = type
        this.name = name
        this.value = null
        this.formattedText = name
    }

    constructor(name: String, value: JsonNode?) : super() {
        this.name = name
        this.value = value
        when {
            value?.isTextual == true -> {
                type = NodeType.STRING
                formattedText = "\"$name\":\"${value.textValue()}\""
                return
            }
            value?.isNumber == true -> {
                type = NodeType.NUMBER
                formattedText = "\"$name\":${value.numberValue()}"
                return
            }
            value?.isBoolean == true -> {
                type = NodeType.BOOLEAN
                formattedText = "\"$name\":${value.booleanValue()}"
                return
            }
            value?.isNull == true -> {
                type = NodeType.NULL
                formattedText = "\"$name\":null"
            }
            else -> {
                type = NodeType.OBJECT
                this.formattedText = name
            }
        }
    }

    override fun toString(): String {
        return if (isLeaf) {
            formattedText
        } else {
            "$name \t\t\t\t\t\t ${type.nodeName}"
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