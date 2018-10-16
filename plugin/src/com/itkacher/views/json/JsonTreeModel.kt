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
        private fun buildTree(name: String, node: JsonNode, maxValueLength: AtomicInteger = AtomicInteger(0)): JsonMutableTreeNode {
            val parentType = if (node.isArray) JsonMutableTreeNode.NodeType.ARRAY else JsonMutableTreeNode.NodeType.OBJECT
            val treeNode = JsonMutableTreeNode(name, parentType, maxValueLength)
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
                    if (child.isValueNode) {
                        if (maxValueLength.get().compareTo(name.length) == -1) {
                            maxValueLength.set(name.length)
                        }
                        treeNode.add(JsonMutableTreeNode(name, child, maxValueLength))
                    } else {
                        treeNode.add(buildTree(String.format("[%d]", i), child, maxValueLength))
                    }
                }
            } else if (node.isValueNode) {
                treeNode.add(JsonMutableTreeNode(name, node, maxValueLength))
            }

            return treeNode
        }

    }
}