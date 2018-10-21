package com.itkacher.views.json

interface JTreeItemMenuListener {
    fun createJavaModel(node: JsonMutableTreeNode)
    fun createKotlinModel(node: JsonMutableTreeNode)
}