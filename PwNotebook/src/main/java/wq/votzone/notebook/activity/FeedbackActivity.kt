package wq.votzone.notebook.activity

import android.app.AlertDialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.didikee.donate.AlipayDonate
import android.didikee.donate.WeiXinDonate
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import wq.votzone.notebook.R
import java.io.File
import android.widget.Toast
import wq.votzone.notebook.dialog.WxDnaFragment
import wq.votzone.notebook.util.PrefUtil
import wq.votzone.notebook.util.put2ClipboardOnly
import wq.votzone.notebook.util.showToast


class FeedbackActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        if(v == tvMarket){
            try {
                val uri = Uri.parse("market://details?id=" + packageName)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }else if(v == tvQQ){
            val key = "hgeK31Cz1comXCcumj7RELcwavnQ4tqm"
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key)
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                // 未安装手Q或安装的版本不支持
            }
        }else if(v == tvWX){
            put2ClipboardOnly(this,"votzone")
            showToast(this, "微信号:votzone已复制, 去微信找我吧!")
        }else if(v == tvDnaZfb){
            if(AlipayDonate.hasInstalledAlipayClient(this)){
                AlipayDonate.startAlipayClient(this, "FKX08233AILFKO2HLAFU1C")
            }
        }else if(v == tvDnaWx){
            donateDialog()
        }else if( v == ivLoan){
            intent = Intent()
            intent.setAction("android.intent.action.VIEW")
            var content_url = Uri.parse("https://cloud.haodai.com/Mobile/marketloan?ref=hd_11018167")
            intent.setData(content_url)
            startActivity(intent)
        }else if(v == tvOs){
            intent = Intent()
            intent.setAction("android.intent.action.VIEW")
            var content_url = Uri.parse("https://github.com/votzone/notepad")
            intent.setData(content_url)
            startActivity(intent)
        }
    }

    lateinit var tvMarket:View
    lateinit var tvQQ:View
    lateinit var tvWX: View

    lateinit var tvDnaWx:View
    lateinit var tvDnaZfb: View

    lateinit var tvOs:View

    lateinit var ivLoan: View


    lateinit var pu: PrefUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        tvMarket= findViewById(R.id.btn_market)
        tvQQ = findViewById(R.id.btn_qq)
        tvWX= findViewById(R.id.btn_wx)
        tvDnaWx = findViewById(R.id.btn_wx_dna)
        tvDnaZfb= findViewById(R.id.btn_zfb)
        ivLoan = findViewById(R.id.iv_loan_market)
        tvOs = findViewById(R.id.btn_open_source)

        tvMarket.setOnClickListener(this)
        tvQQ.setOnClickListener(this)
        tvWX.setOnClickListener(this)
        tvDnaZfb.setOnClickListener(this)
        tvDnaWx.setOnClickListener(this)
        ivLoan.setOnClickListener(this)
        tvOs.setOnClickListener(this)

        pu = PrefUtil(this)
        if(pu.canShowLoanMarket()){
            ivLoan.visibility = View.VISIBLE
        }else{
            ivLoan.visibility = View.GONE
        }

    }

    companion object {
        fun launch(from: Context){
            var intent = Intent(from, FeedbackActivity::class.java)
            from.startActivity(intent)
        }
    }


    fun donateDialog(){
        var dg = AlertDialog.Builder(this)
        var inflater = LayoutInflater.from(this)
        var view = inflater.inflate(R.layout.fragment_wx_dna, null, false)
        dg.setView(view)
        dg.setPositiveButton("确定"){ dialog, which->
            donateWeixin()
        }
        dg.setNegativeButton("取消",null)
        dg.show()

    }

    private fun donateWeixin() {
        val weixinQrIs = resources.openRawResource(R.raw.votzone_wx)
        val qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonate" + File.separator +
                "votzone_weixin.png"
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs))
        WeiXinDonate.donateViaWeiXin(this, qrPath)
    }
}
