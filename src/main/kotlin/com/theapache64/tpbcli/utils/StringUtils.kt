package com.theapache64.moviemonk.utils

object StringUtils {
    fun getFromPattern(input: String, regex: Regex, key: String): List<String> {
        val results = regex.findAll(input)
        val data = mutableListOf<String>()
        for (result in results) {
            data.add(result.groups[key]!!.value)
        }
        return data
    }

    fun getProper(count: Int?, singular: String, plural: String): String? {
        if (count == null) {
            return null
        }
        return if (count > 1) {
            plural
        } else {
            singular
        }
    }

    fun removeNewLinesAndMultipleSpaces(input: String): String {
        return input.replace("\n", "").replace("\\s{2,}".toRegex(), " ")
    }
}