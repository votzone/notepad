package wq.ktrs.tmp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import wq.votzone.notebook.R;

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/28
 * Modification History:
 * Why & What modified:
 */

public class TempActivity extends Activity implements View.OnClickListener{

    ViewPager mVp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVp = (ViewPager) findViewById(R.id.action_settings);

        String a = "dfdf";
        a.split("a");

        CharSequence [] array = new CharSequence[3];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(array, null);

        mVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println();
                System.out.println();
                System.out.println();
                String a = "";
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = "";
                if(a.isEmpty()) {
                    Intent intent = new Intent(TempActivity.this, TempActivity.class);
                    TempActivity.this.startActivity(intent);
                }
            }
        });
    }

    public static void launch(Activity from){
        Intent intent = new Intent(from, TempActivity.class);
        from.startActivity(intent);


    }

    @Override
    public void onClick(View v) {

    }
}
