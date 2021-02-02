package com.theapache64.tpbcli

import com.jakewharton.picnic.table
import com.theapache64.tpbcli.core.TPB
import com.theapache64.tpbcli.utils.GREEN
import com.theapache64.tpbcli.utils.InputUtils
import com.theapache64.tpbcli.utils.SimpleCommandExecutor
import com.theapache64.tpbcli.utils.color

private const val IS_DEBUG = false
private const val OPTION_GET_MAGNET = 1
private const val OPTION_OPEN_LINK = 2

fun main(args: Array<String>) {

    @Suppress("ConstantConditionIf")
    val keyword = if (IS_DEBUG) {
        "Assassin"
    } else {
        if (args.isEmpty()) {
            InputUtils.getString("Enter keyword", true)
        } else {
            args.first()
        }
    }

    println("🔍 Searching for `$keyword`")

    val files = TPB.getFiles(keyword)
        .take(10) // take top 10 only

    if (files.isNotEmpty()) {

        println("🎁 Found ${files.size} files")

        val table = table {

            cellStyle {
                border = true
            }

            header {
                row("ID", "Title", "Date", "Size", "Seeders", "Leechers")
            }

            for (file in files) {
                row(
                    file.id,
                    file.name,
                    file.uploadedDate,
                    file.fileSize,
                    file.seederCount,
                    file.leecherCount
                )
            }
        }

        println(table)
        askFile(files)

    } else {
        println("😥 No match found")
    }

}

fun askFile(files: List<File>) {
    val fileId = InputUtils.getInt("Enter file ID", 1, files.last().id)
    askOption(files.find { it.id == fileId }!!)
}


fun askOption(file: File) {


    val table = table {

        cellStyle {
            border = true
        }

        header {
            row("ID", "Title", "Date", "Size", "Seeders", "Leechers")
        }

        row(
            file.id,
            file.name,
            file.uploadedDate,
            file.fileSize,
            file.seederCount,
            file.leecherCount
        )
    }
    println(table)
    println("${OPTION_GET_MAGNET}) 🔻 Get Magnet")
    println("${OPTION_OPEN_LINK}) 👁️️ Open Link")

    when (InputUtils.getInt("Option", OPTION_GET_MAGNET, OPTION_OPEN_LINK)) {

        OPTION_GET_MAGNET -> {
            val magnetLink = TPB.getMagnet(file.link)
            println("👍 Magnet collected ")
            println("-------------------------------------------------------------")
            println(magnetLink.color(GREEN))
            println("-------------------------------------------------------------")
        }

        OPTION_OPEN_LINK -> {
            SimpleCommandExecutor.executeCommand("sensible-browser \"${file.link}\"")
        }
    }
}
