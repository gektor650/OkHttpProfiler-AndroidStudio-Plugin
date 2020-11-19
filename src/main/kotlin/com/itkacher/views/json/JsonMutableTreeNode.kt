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
package com.itkacher.views.json

import com.fasterxml.jackson.databind.JsonNode
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.tree.DefaultMutableTreeNode

class JsonMutableTreeNode : DefaultMutableTreeNode {

    private val type: NodeType
    val name: String
    val value: JsonNode?
    private val formattedText: String
    private val maxLength: AtomicInteger
    private var isArrayElement: Boolean

    constructor(name: String, node: JsonNode?, type: NodeType, maxLength: AtomicInteger = AtomicInteger(), isArrayElement: Boolean = false) : super(name) {
        this.type = type
        this.name = name
        this.value = node
        this.formattedText = name
        this.maxLength = maxLength
        this.isArrayElement = isArrayElement
    }

    constructor(name: String, value: JsonNode?, maxLength: AtomicInteger = AtomicInteger(), isArrayElement: Boolean = false) : super() {
        this.name = name
        this.value = value
        this.maxLength = maxLength
        this.isArrayElement = isArrayElement
        val pattern = if(isArrayElement) {
            "%s: %s"
        } else {
            "\"%s\": %s"
        }
        when {
            value?.isTextual == true -> {
                type = NodeType.STRING
                formattedText = String.format(pattern, name, "\"${value.textValue()}\"")
                return
            }
            value?.isNumber == true -> {
                type = NodeType.NUMBER
                formattedText = String.format(pattern, name, value.numberValue().toString())
                return
            }
            value?.isBoolean == true -> {
                type = NodeType.BOOLEAN
                formattedText = String.format(pattern, name, value.booleanValue().toString())
                return
            }
            value?.isNull == true -> {
                type = NodeType.NULL
                formattedText = String.format(pattern, name, NULL_STRING)
            }
            else -> {
                type = NodeType.OBJECT
                this.formattedText = name
            }
        }
    }

    override fun toString(): String {
        return if (type != NodeType.ARRAY && type != NodeType.OBJECT) {
            formattedText
        } else if(type == NodeType.ARRAY && isLeaf) {
            var spaces = getSpaces()
            if(spaces.length >= 3) {
                spaces = spaces.substring(0, spaces.length - 3)
            }
            "$name:[] $spaces ${type.nodeName}"
        } else {
            "$name ${getSpaces()} ${type.nodeName}"
        }
    }

    private fun getSpaces(): String {
        val spaces = StringBuilder()
        for (i in name.length until maxLength.get()) {
            spaces.append(' ')
        }
        return spaces.toString()
    }

    enum class NodeType(val nodeName: String) {
        STRING("String"),
        NUMBER("Number"),
        NULL("Null"),
        BOOLEAN("Boolean"),
        OBJECT("Object"),
        ARRAY("Array"),
    }

    companion object {
        const val NULL_STRING = "null"
    }
}