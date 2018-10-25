package com.itkacher.views.json

import com.fasterxml.jackson.databind.JsonNode
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.tree.DefaultTreeModel


class JsonTreeModel(json: JsonNode) : DefaultTreeModel(JsonTreeModel.buildTree("", json)) {

    companion object {
        /**
         * Builds a tree of TreeNode objects using the tree under the
         * given JsonNode.
         *
         * @param name Text to be associated with node
         * @param node
         * @return root TreeNode
         */
        private fun buildTree(name: String, node: JsonNode, maxValueLength: AtomicInteger = AtomicInteger(0), isArrayElement: Boolean = false): JsonMutableTreeNode {
            val parentType = if (node.isArray) JsonMutableTreeNode.NodeType.ARRAY else JsonMutableTreeNode.NodeType.OBJECT
            val treeNode = JsonMutableTreeNode(name, node, parentType, maxValueLength, isArrayElement)
            val it = node.fields()
            while (it.hasNext()) {
                val entry = it.next()
                if (entry.value.isValueNode) {
                    if (maxValueLength.get().compareTo(entry.key.length) == -1) {
                        maxValueLength.set(entry.key.length)
                    }
                    treeNode.add(JsonMutableTreeNode(entry.key, entry.value, maxValueLength))
                } else {
                    treeNode.add(buildTree(entry.key, entry.value, maxValueLength))
                }
            }

            if (node.isArray) {
                for (i in 0 until node.size()) {
                    val child = node.get(i)
                    val arrayIndex = String.format("[%d]", i)
                    if (maxValueLength.get().compareTo(name.length) == -1) {
                        maxValueLength.set(name.length)
                    }
                    if (child.isValueNode) {
                        treeNode.add(JsonMutableTreeNode(arrayIndex, child, maxValueLength, true))
                    } else {
                        treeNode.add(buildTree(arrayIndex, child, maxValueLength, true))
                    }
                }
            } else if (node.isValueNode) {
                treeNode.add(JsonMutableTreeNode(name, node, maxValueLength))
            }

            return treeNode
        }

    }
}