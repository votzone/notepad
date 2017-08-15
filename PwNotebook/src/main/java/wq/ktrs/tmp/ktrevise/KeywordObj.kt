package wq.ktrs.tmp.ktrevise

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/3
 * Modification History:
 * Why & What modified:
 */

class KeywordObj{
    object TestObj {
        fun testfun():Boolean{
            return false
        }
    }

    fun test(){
        TestObj.testfun()
    }


}
fun main(args: Array<String>) {
    { println(42) }()
}