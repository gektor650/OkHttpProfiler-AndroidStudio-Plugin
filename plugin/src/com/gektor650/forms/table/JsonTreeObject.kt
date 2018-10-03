package com.gektor650.forms.table

import com.fasterxml.jackson.databind.JsonNode
import javax.swing.tree.DefaultTreeModel


class JsonTreeObject(json: JsonNode) : DefaultTreeModel(JsonTreeObject.buildTree("", json)) {

    companion object {
        /**
         * Builds a tree of TreeNode objects using the tree under the
         * given JsonNode.
         *
         * @param name Text to be associated with node
         * @param node
         * @return root TreeNode
         */
        private fun buildTree(name: String, node: JsonNode): JsonMutableTreeNode {
            val treeNode = JsonMutableTreeNode(name)
            val it = node.fields()
            while (it.hasNext()) {
                val entry = it.next()
                if(entry.value.isValueNode) {
                    treeNode.add(JsonMutableTreeNode(entry.key, entry.value.asText()))
                } else {
                    treeNode.add(buildTree(entry.key, entry.value))
                }
            }

            if (node.isArray) {
                for (i in 0 until node.size()) {
                    val child = node.get(i)
                    if (child.isValueNode)
                        treeNode.add(JsonMutableTreeNode(name, child.asText()))
                    else
                        treeNode.add(buildTree(String.format("[%d]", i), child))
                }
            } else if (node.isValueNode) {
                treeNode.add(JsonMutableTreeNode(name, node.asText()))
            }

            return treeNode
        }

        enum class NodeType(val nodeName: String) {
            STRING("String"),
            OBJECT("Array"),
            ARRAY("Array"),
//            STRING("String"),
//            STRING("String")
        }

//        private fun getValueTreeNode(name: String, jsonNode: JsonNode) {
//            when {
//                jsonNode.isNull -> JsonMutableTreeNode(name, child.asText())
//            }
//        }
    }
}