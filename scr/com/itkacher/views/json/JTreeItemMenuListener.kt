package com.itkacher.views.json

interface JTreeItemMenuListener {
    fun copyToClipboard(node: JsonMutableTreeNode)
    fun openInEditor(node: JsonMutableTreeNode)
    fun createJavaModel(node: JsonMutableTreeNode)
    fun createKotlinModel(node: JsonMutableTreeNode)
}