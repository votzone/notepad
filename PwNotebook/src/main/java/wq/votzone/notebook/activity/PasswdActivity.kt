package wq.votzone.notebook.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.umeng.analytics.MobclickAgent

import wq.votzone.notebook.R
import wq.votzone.notebook.util.Const
import wq.votzone.notebook.util.PrefUtil
import wq.votzone.notebook.util.showToast

class PasswdActivity : AppCompatActivity() , View.OnClickListener{
    override fun onClick(v: View?) {
        if (password.length < 6){
            if (v == vNum0) {
                password += "0"
            }else if (v == vNum1) {
                password += "1"
            }else if (v == vNum2) {
                password += "2"
            }else if (v == vNum3) {
                password += "3"
            }else if (v == vNum4) {
                password += "4"
            }else if (v == vNum5) {
                password += "5"
            }else if (v == vNum6) {
                password += "6"
            }else if (v == vNum7) {
                password += "7"
            }else if (v == vNum8) {
                password += "8"
            }else if (v == vNum9) {
                password += "9"
            }
        }
        if (password.length >0){
            if(v == vBack){
                password = password.substring(0, password.length-1)
            }

        }
        setPassword()
        if(v == vOk){
            handleOk()
        }

        if(v == vCancel){
            finish()
        }
    }

    lateinit var vNum0: View
    lateinit var vNum1:View
    lateinit var vNum2:View
    lateinit var vNum3:View
    lateinit var vNum4:View
    lateinit var vNum5:View
    lateinit var vNum6:View
    lateinit var vNum7:View
    lateinit var vNum8:View
    lateinit var vNum9:View
    lateinit var vOk:View
    lateinit var vCancel:View
    lateinit var vBack: View

    lateinit var tvPassword: TextView
    lateinit var tvHint: TextView

    var password = ""
    var tmpPass = ""

    /**
     * state ==0 输入密码解锁
     * state ==1 设置保护密码
     * state ==2 重新输入密码
     * state ==10 输入原始密码
     * state ==11 输入新密码
     * state ==12 重新输入密码
      */

    var state = 0

    lateinit var pu:PrefUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passwd)
        pu = PrefUtil(this)
        handIntent(intent)
        initView()
        initAction()
        setPassword()
        setState()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }


    fun handIntent(intent: Intent){
        state = intent.getIntExtra(Const.pass_state_key,0)
        if(!pu.canVerifyPass()){
            state = 1
        }
    }

    fun handleOk(){
        if(state == 0){
            if(pu.verifyPass(password)){
                goMain()
            }else{
                showToast(this,"密码不正确")
            }
        }else if(state == 1){
            tmpPass = password
            state = 2
        }else if(state == 2){
            if(TextUtils.equals(tmpPass, password)){
                state = 0
                pu.setPassword(tmpPass)

            }else{
                showToast(this,"两次密码不一致,请再次设置")
                state = 1
            }

        }else if(state == 10){
            if(pu.verifyPass(password)){
                state = 11
            }else{
                showToast(this, "密码不正确")
            }

        }else if(state == 11){
            tmpPass = password
            state = 12
        }else if(state == 12){
            if(TextUtils.equals(tmpPass, password)){
                pu.setPassword(tmpPass)
                showToast(this, "密码修改成功")
                finish()
            }else{
                showToast(this, "两次密码不一致,请再次设置")
                state = 10
            }
        }
        password = ""
        setState()
        setPassword()
    }

    fun goMain(){
        NotePadActivity.launch(this)
        finish()
    }

    override fun onBackPressed() {

    }

    fun setState(){
        if(state ==0){
            // 启动时进入,输入密码解锁
            tvHint.text="输入密码解锁"
        }else if(state ==1){
            // 设置密码
            tvHint.text="设置保护密码"

        }else if(state ==2){
            // 设置密码
            tvHint.text="重新输入密码"

        }else if(state == 10){
            // 修改密码
            tvHint.text="输入原始密码"
        }else if(state == 11){
            // 修改密码
            tvHint.text="输入新密码"
        }else if(state == 12){
            // 修改密码
            tvHint.text="重新输入密码"
        }
    }

    fun initView(){
        vNum0 = findViewById(R.id.tv_num0)
        vNum1 = findViewById(R.id.tv_num1)
        vNum2 = findViewById(R.id.tv_num2)
        vNum3 = findViewById(R.id.tv_num3)
        vNum4 = findViewById(R.id.tv_num4)
        vNum5 = findViewById(R.id.tv_num5)
        vNum6 = findViewById(R.id.tv_num6)
        vNum7 = findViewById(R.id.tv_num7)
        vNum8 = findViewById(R.id.tv_num8)
        vNum9 = findViewById(R.id.tv_num9)
        vOk = findViewById(R.id.tv_ok)
        vCancel = findViewById(R.id.tv_cancel)
        vBack = findViewById(R.id.ll_back)

        tvHint = findViewById(R.id.tv_hint) as TextView
        tvPassword = findViewById(R.id.tv_password) as TextView
    }

    fun initAction(){
        vNum0.setOnClickListener(this)
        vNum1.setOnClickListener(this)
        vNum2.setOnClickListener(this)
        vNum3.setOnClickListener(this)
        vNum4.setOnClickListener(this)
        vNum5.setOnClickListener(this)
        vNum6.setOnClickListener(this)
        vNum7.setOnClickListener(this)
        vNum8.setOnClickListener(this)
        vNum9.setOnClickListener(this)
        vOk.setOnClickListener(this)
        vCancel.setOnClickListener(this)
        vBack.setOnClickListener(this)
    }

    fun setPassword(){
        var pwFont = StringBuilder()
        for (i in 1.. password.length){
            pwFont.append("* ")
        }
        tvPassword.text = pwFont.toString()

        vOk.isEnabled = password.length>=6
    }

    companion object{
        fun launch(from: Activity){
            val intent = android.content.Intent(from, PasswdActivity::class.java)
            intent.putExtra(Const.pass_state_key,10)
            from.startActivity(intent)
        }
    }
}
