package com.gektor650.views.list

enum class TableColumn(val text: String, val width: Int) {
    NUMBER("#", 10),
    METHOD("Method", 20),
    REQUEST("Request", 600),
    DURATION("Duration", 20),
    TIME("Time", 50),
    STATUS("Status", 50)
}
