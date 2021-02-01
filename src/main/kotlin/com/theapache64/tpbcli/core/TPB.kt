package com.theapache64.tpbcli.core

import com.theapache64.moviemonk.utils.StringUtils
import com.theapache64.tpbcli.File
import com.theapache64.tpbcli.utils.RestClient
import java.net.URLEncoder


object TPB {

    private const val PROXIES_URL = "https://piratebay-proxylist.net/"
    private val PROXY_REGEX =
        "<span class=\"domain\" style=\"margin-right: 4px;\">(?<domain>.+)</span>".toRegex()
    private val FILE_REGEX =
        "<div class=\"detName\"> <a href=\"(?<link>.+?)\" class=\"detLink\" (?:.+?)>(?<fileName>.+?)<\\/a>[\\s\\S]*<\\/div>[\\s\\S]+?Uploaded\\s(?<date>.+?), Size (?<size>.+?),.+?\"right\">(?<seederCount>\\d+).+?\"right\">(?<leecherCount>\\d+)".toRegex()

    private val MAGNET_REGEX = "href=\"(?<magnetUrl>magnet:.+?)\" title=\"Get this torrent".toRegex()

    fun getFiles(keyword: String): List<File> {
        println("➡️ Getting best proxy...")
        val baseDomain = "thepiratebay10.org"
        val encKeyword = URLEncoder.encode(keyword, "UTF-8").replace(
            "+", "%20"
        )
        val baseUrl = "https://$baseDomain"
        //val searchUrl = "$baseUrl/s/?q=$encKeyword&page=0&orderby=99"
        val searchUrl = "$baseUrl/search/$encKeyword/1/99/0"
        println("🔍 Searching `$keyword`... -> $searchUrl")
        val respString = RestClient.get(searchUrl).body!!.string()
        val response = StringUtils.removeNewLinesAndMultipleSpaces(respString)
        // java.io.File("x.txt").writeText(response)
        val results = FILE_REGEX.findAll(response)
        val files = mutableListOf<File>()
        for ((index, result) in results.withIndex()) {
            val groups = result.groups
            files.add(
                File(
                    index + 1,
                    groups["fileName"]!!.value,
                    groups["date"]!!.value.nbspToSpace(),
                    groups["size"]!!.value.nbspToSpace(),
                    groups["seederCount"]!!.value.toInt(),
                    groups["leecherCount"]!!.value.toInt(),
                    groups["link"]!!.value
                )
            )
        }
        return files
    }


    /**
     * To get thepiratebay proxy domains
     */
    private fun getProxies(): List<String> {
        val resp = RestClient.get(PROXIES_URL).body!!.string()
        val results = PROXY_REGEX.findAll(resp)
        val proxies = mutableListOf<String>()
        for (result in results) {
            proxies.add(result.groups["domain"]!!.value)
        }

        return proxies
    }

    fun getMagnet(link: String): String {
        val resp = RestClient.get(link)
        val respString = resp.body!!.string()
        val respStringLine = StringUtils.removeNewLinesAndMultipleSpaces(respString)
        return MAGNET_REGEX.find(respStringLine)!!.groups["magnetUrl"]!!.value
    }


}

private fun String.nbspToSpace(): String {
    return this.replace("&nbsp;", " ")
}
