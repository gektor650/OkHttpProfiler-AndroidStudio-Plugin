package com.gektor650.forms.table

import com.fasterxml.jackson.databind.JsonNode
import com.google.gson.JsonElement
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath

class JsonTreeObject(private val json: JsonNode) : TreeModel {

    override fun getRoot(): Any {
        return  json
    }

    override fun isLeaf(node: Any?): Boolean {
//        (node as JsonNode).l
        return  false//if(node === ROOT) false else cast(node).isJsonPrimitive
    }

    override fun getChildCount(parent: Any?): Int {
        return (parent as JsonNode).size()
//        val jsonParent = if(parent === ROOT)  {
//            json
//        } else {
//            parent
//        }
//        val element = cast(jsonParent)
//        return when {
//            element.isJsonArray -> element.asJsonArray.size()
//            element.isJsonObject -> element.asJsonObject.keySet().size
//            else -> 0
//        }
    }

    override fun valueForPathChanged(path: TreePath?, newValue: Any?) {

    }

    override fun getIndexOfChild(parent: Any?, child: Any?): Int {
        return (parent as JsonNode).indexOf(child)
//        return (cast(parent).asJsonArray).indexOf(child)
    }

    override fun getChild(parent: Any?, index: Int): Any {
        return (parent as JsonNode).get((parent as JsonNode).fieldNames().[index])
//
//        val jsonParent = if(parent === ROOT)  {
//            json
//        } else {
//            parent
//        }
//        val element = cast(jsonParent)
//        return when {
//            element.isJsonObject -> {
//                val jObject = element.asJsonObject
//                jObject[jObject.keySet().toTypedArray()[index]]
//            }
//            element.isJsonArray -> {
//                element.asJsonArray[index]
//            }
//            else -> {
//                element.asJsonPrimitive
//            }
//        }
    }

    override fun addTreeModelListener(l: TreeModelListener?) {

    }

    override fun removeTreeModelListener(l: TreeModelListener?) {

    }

    private fun cast(element: Any?): JsonElement {
        val jElement = (element as JsonElement)
        return when {
            jElement.isJsonArray -> jElement.asJsonArray
            jElement.isJsonObject -> jElement.asJsonObject
            jElement.isJsonNull -> jElement.asJsonNull
            else -> {
                jElement.asJsonPrimitive
            }
        }
    }

    companion object {
        const val ROOT = "root"
    }

}