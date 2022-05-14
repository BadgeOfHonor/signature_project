package signature

import java.io.File

class FontExpres() {
    val alphabet = mutableMapOf<Char, FontSig>()
    var sizeRows: Int = 0
    var numChar: Int = 0

    fun fontLoadFromFile(fileName: String) {
        val fontFile = File(fileName)
        var n = 0
        var fontChar = ' '
        var fontSizeColumns = 0
        val fontSignature = mutableListOf<String>()
        fontFile.forEachLine {
            if (n == 0) {
                if (it.matches("\\d{1,2} \\d{1,2}".toRegex())) {
                    val a = it.split(" ");
                    sizeRows = a.first().toInt()
                    numChar = a.last().toInt()
                }
                if (it.matches("[a-zA-Z] \\d{1,2}".toRegex())) {
                    val a = it.split(" ")
                    fontChar = a.first().first()
                    fontSizeColumns = a.last().toInt()
                    //fontSignature.clear()
                    n = sizeRows
                }
            } else {
                fontSignature.add(it)
                n--
                if (n==0) {
                    alphabet[fontChar] = FontSig(fontChar, sizeRows, fontSizeColumns, fontSignature.toList())
                    fontSignature.clear()
                    fontChar = ' '
                    fontSizeColumns = 0
                }
            }
        }
        if (sizeRows == 10) {
            val sizeColumns = 10
            alphabet[' '] = FontSig(' ', sizeRows, sizeColumns, List(sizeRows) {" ".repeat(sizeColumns)})
        } else {
            val sizeColumns = 5
            alphabet[' '] = FontSig(' ', sizeRows, sizeColumns, List(sizeRows) {" ".repeat(sizeColumns)})
        }
    }

    fun stringLine(str: String): Pair<List<String>, Int> {
        val strLine = MutableList(sizeRows) {""}
        var strLength = 0
        for (i in str) {
            for (j in strLine.indices) {
                strLine[j] += alphabet[i]!!.signature[j]
            }
        }
        for (i in strLine.indices) {
            strLine[i] = strLine[i].removeSuffix(" ")
        }
        strLength = strLine[0].length
        return Pair(strLine, strLength)
    }

    data class FontSig (val char: Char, val sizeRows: Int, val sizeColumns: Int, val signature: List<String>)
}

class Label(_surname: Pair<List<String>, Int>, _status: Pair<List<String>, Int>) {
    val simbol = "88"
    val romanSurname = _surname.first.toMutableList()
    val romanSurnameLength = _surname.second
    val mediumStatus = _status.first.toMutableList()
    val mediumStatusLength = _status.second
    val merginRight = 3
    val merginLeft = 2
    var labelLength = 0

    fun creat(): String {
        var labelBorderUpDown = ""
        val result = { maxLength: Int, maxList: MutableList<String>, minLength: Int, minList: MutableList<String> ->
            labelLength = merginLeft + maxLength + merginRight
            labelBorderUpDown = "$simbol${"8".repeat(labelLength)}$simbol"
            for (i in maxList.indices) {
                maxList[i] = "$simbol${" ".repeat(merginLeft)}${maxList[i]}${" ".repeat(merginRight)}$simbol"
            }
            val sub = (labelLength - minLength) / 2
            val nLeft = sub
            var nRight = sub + 1
            if ((labelLength - minLength) % 2 != 0) {
                nRight += 1
            }
            for (i in minList.indices) {
                minList[i] = "$simbol${" ".repeat(nLeft)}${minList[i]}${" ".repeat(nRight)}$simbol"
            }
        }
        if (romanSurnameLength >= mediumStatusLength) {
            result(romanSurnameLength, romanSurname, mediumStatusLength + 1, mediumStatus)
        } else {
            result(mediumStatusLength, mediumStatus, romanSurnameLength + 1, romanSurname)
        }
        val strLabelList = mutableListOf<String>()
        strLabelList += labelBorderUpDown
        strLabelList += romanSurname
        strLabelList += mediumStatus
        strLabelList += labelBorderUpDown
        return strLabelList.joinToString("\n")
    }

    override fun toString(): String = creat()
}

fun main() {

    val roman = FontExpres()
    roman.fontLoadFromFile("C:\\Users\\TYRAN\\IdeaProjects\\ASCII Text Signature\\roman.txt")
    val medium = FontExpres()
    medium.fontLoadFromFile("C:\\Users\\TYRAN\\IdeaProjects\\ASCII Text Signature\\medium.txt")

    println("Enter name and surname: ")
    val surname = readln()

    println("Enter person's status: ")
    val status = readln()

    println(Label(roman.stringLine(surname), medium.stringLine(status)))

}


