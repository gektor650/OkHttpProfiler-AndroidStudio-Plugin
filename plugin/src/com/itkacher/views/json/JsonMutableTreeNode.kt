package com.itkacher.views.json

import com.fasterxml.jackson.databind.JsonNode
import org.apache.batik.anim.dom.AbstractSVGAnimatedLength
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.tree.DefaultMutableTreeNode

class JsonMutableTreeNode : DefaultMutableTreeNode {

    private val type: NodeType
    private val name: String
    private val value: JsonNode?
    private val formattedText: String
    private val maxLength: AtomicInteger

    constructor(name: String, type: NodeType, maxLength: AtomicInteger) : super(name) {
        this.type = type
        this.name = name
        this.value = null
        this.formattedText = name
        this.maxLength = maxLength
    }

    constructor(name: String, value: JsonNode?, maxLength: AtomicInteger) : super() {
        this.name = name
        this.value = value
        this.maxLength = maxLength
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
            val spaces = StringBuilder()
            for (i in name.length until maxLength.get()) {
                spaces.append(' ')
            }
            "$name $spaces ${type.nodeName}"
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