package com.theapache64.tpbcli.utils

private const val RESET = "\u001B[0m"
const val BLACK = "\u001B[30m"
const val RED = "\u001B[31m"
const val GREEN = "\u001B[32m"
const val YELLOW = "\u001B[33m"
const val BLUE = "\u001B[34m"
const val PURPLE = "\u001B[35m"
const val CYAN = "\u001B[36m"
const val WHITE = "\u001B[37m"

fun println(color: String, text: String) {
    printlnColor("$color$text")
}


fun print(color: String, text: String) {
    printColor("$color$text")
}

fun printColor(text: String) {
    print("$text$RESET")
}

fun printlnColor(text: String) {
    println("$text$RESET")
}

fun String.color(color: String): String {
    return "$color$this$RESET"
}