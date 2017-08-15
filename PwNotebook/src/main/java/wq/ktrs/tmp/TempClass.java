package wq.ktrs.tmp;

import android.app.AlertDialog;

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/30
 * Modification History:
 * Why & What modified:
 */

public class TempClass  implements B, C{

    @Override
    public void fun() {
        System.out.println("TempClass");
    }

    public static void temp(){
        TempClass t = new TempClass();
        t.fun();

        B b = new TempClass();
        b.fun();
        C c = new TempClass();
        c.fun();

    }
}
interface B{
    void fun();
}
interface C{
    void fun();
}

 abstract class A{
     void fun(){
        System.out.println("A");

         String [] array = {"",""};
         AlertDialog.Builder builder = new AlertDialog.Builder(null);
         builder.setItems(array, null);
     }



}


