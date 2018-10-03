package com.gektor650.views.json

import com.fasterxml.jackson.databind.JsonNode
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
        private fun buildTree(name: String, node: JsonNode): JsonMutableTreeNode {
            val parentType = if(node.isArray) JsonMutableTreeNode.NodeType.ARRAY else JsonMutableTreeNode.NodeType.OBJECT
            val treeNode = JsonMutableTreeNode(name, parentType)
            val it = node.fields()
            while (it.hasNext()) {
                val entry = it.next()
                if(entry.value.isValueNode) {
                    treeNode.add(JsonMutableTreeNode(entry.key, entry.value))
                } else {
                    treeNode.add(buildTree(entry.key, entry.value))
                }
            }

            if (node.isArray) {
                for (i in 0 until node.size()) {
                    val child = node.get(i)
                    if (child.isValueNode)
                        treeNode.add(JsonMutableTreeNode(name, child))
                    else
                        treeNode.add(buildTree(String.format("[%d]", i), child))
                }
            } else if (node.isValueNode) {
                treeNode.add(JsonMutableTreeNode(name, node))
            }

            return treeNode
        }

//        private fun getValueTreeNode(name: String, jsonNode: JsonNode) {
//            when {
//                jsonNode.isNull -> JsonMutableTreeNode(name, child.asText())
//            }
//        }
    }
}