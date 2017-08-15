package wq.votzone.notebook.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import wq.votzone.notebook.R
import wq.votzone.notebook.util.put2Clipboard
import wq.votzone.notebook.util.put2ClipboardOnly
import wq.votzone.notebook.util.showToast

class AboutActivity : AppCompatActivity() ,View.OnClickListener{
    override fun onClick(v: View?) {
        if(v == vqq){
            val key = "hgeK31Cz1comXCcumj7RELcwavnQ4tqm"
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key)
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                // 未安装手Q或安装的版本不支持
            }

        }else if(v == vwx){
            put2ClipboardOnly(this,"votzone")
            showToast(this, "微信号:votzone已复制, 去微信找我吧!")
        }
    }

    lateinit var vqq:View
    lateinit var vwx:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        vqq = findViewById(R.id.tv_qq)
        vwx = findViewById(R.id.tv_wx)

        vqq.setOnClickListener(this)
        vwx.setOnClickListener(this)
    }

    companion object {
        fun launch( from: Context){
            var intent = Intent(from, AboutActivity::class.java)
            from. startActivity(intent)
        }
    }
}
