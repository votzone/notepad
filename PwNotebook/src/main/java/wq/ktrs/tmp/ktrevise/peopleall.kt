package wq.ktrs.tmp.ktrevise

import java.io.File
import org.junit.Before
import org.junit.Test
import org.junit.Assert
import java.io.BufferedReader

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/4
 * Modification History:
 * Why & What modified:
 */
fun main(args: Array<String>) {
    val letters = Array(26) { i -> ('a' + i).toString() }

    println(letters)
    println(letters.joinToString(""))
}