package wq.votzone.notebook.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.media.Image
import android.support.annotation.StyleRes
import android.util.Log
import android.view.View
import android.widget.*
import wq.votzone.notebook.R
import wq.votzone.notebook.util.PrefUtil
import wq.votzone.notebook.util.put2Clipboard

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/10
 * Modification History:
 * Why & What modified:
 */

class RandPsDailog : Dialog ,View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        if(fromUser){
            refreshPass()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onClick(v: View?) {
        if(v == tvBtnRefresh){
            refreshPass()
        }else if(v == tvBtnCancel){
            if(listener != null){
                listener!!.onCancel()
            }
            dismiss()
        }else if(v == tvBtnOk){
            if(listener != null){
                listener!!.onOk(password)
            }
            put2Clipboard(context,password)
            dismiss()
        }
    }

    //android.R.style.Theme_Holo_Light_Dialog_NoActionBar
    constructor(context: Context) : super(context,android.R.style.Theme_Material_Light_Dialog_NoActionBar) {
        initDialog(context)
    }

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {
        initDialog(context)
    }

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        initDialog(context)
    }


    lateinit var tvTitle:TextView
    lateinit var tvPassWd: TextView
    lateinit var seekbar: SeekBar
    lateinit var tvPassLen: TextView
    lateinit var cbUcase: CheckBox  // 大写字符
    lateinit var cbDcase: CheckBox  // 小写字符
    lateinit var cbScase: CheckBox // 特殊字符
    lateinit var tvBtnCancel: TextView
    lateinit var tvBtnOk: TextView
    lateinit var tvBtnRefresh: TextView
    lateinit var llShowHint: LinearLayout
    lateinit var ivClose: ImageView

    var passLen = 20
    var password = "12345678"

    lateinit var pref:PrefUtil

    var listener: RPDialogListener?? = null

    fun initDialog(context: Context){
        setContentView(R.layout.rand_ps_dialog_layout)
//        setTitle("随机生成密码")
        pref = PrefUtil(context)
        initView()
        initAction()
    }

    fun initView(){
        tvTitle = findViewById(R.id.tv_title) as TextView
        tvPassLen = findViewById(R.id.tv_pass_length) as TextView
        tvPassWd = findViewById(R.id.tv_password) as TextView
        seekbar = findViewById(R.id.seek_bar) as SeekBar
        cbUcase = findViewById(R.id.cb_upcase) as CheckBox
        cbDcase = findViewById(R.id.cb_dwoncase) as CheckBox
        cbScase = findViewById(R.id.cb_sp_case) as CheckBox
        tvBtnCancel = findViewById(R.id.tv_btn_cancel) as TextView
        tvBtnOk = findViewById(R.id.tv_btn_ok) as TextView
        tvBtnRefresh = findViewById(R.id.tv_btn_refresh) as TextView

        llShowHint = findViewById(R.id.ll_hinting) as LinearLayout
        ivClose = findViewById(R.id.iv_no_more_hinting) as ImageView




        if (pref.needShowPassHint()){
            llShowHint.visibility = View.VISIBLE
        }else{
            llShowHint.visibility = View.GONE
        }
        refreshPass()
    }

    fun initAction(){
        ivClose.setOnClickListener{ llShowHint.visibility = View.GONE }

        tvBtnRefresh.setOnClickListener(this)
        tvBtnOk.setOnClickListener(this)
        tvBtnCancel.setOnClickListener(this)
        seekbar.setOnSeekBarChangeListener(this)

        cbScase.setOnCheckedChangeListener { buttonView, isChecked -> refreshPass() }
        cbDcase.setOnCheckedChangeListener { buttonView, isChecked ->  refreshPass()}
        cbUcase.setOnCheckedChangeListener { buttonView, isChecked -> refreshPass() }
    }

    val lowerCase = "abcdefghijklmnopqrstuvwxyz".toCharArray()
    val upperCase = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray()
    val specialCase = """!@#$%^&*()_+-=[]{}\|'";:/?.>,<~""".toCharArray()
    val numberCase = "1234567890".toCharArray()
    fun refreshPass(){
        passLen = seekbar.progress+6
        tvPassLen.text = "[ "+passLen.toString()+" ]"
        val chars = ArrayList<Char>()
        for (c in numberCase){
            chars.add(c)
        }
        if(cbScase.isChecked){
            for (c in specialCase){
                chars.add(c)
            }
        }
        if(cbUcase.isChecked){
            for (c in upperCase){
                chars.add(c)
            }
        }
        if(cbDcase.isChecked){
            for (c in lowerCase){
                chars.add(c)
            }
        }

        val sbuilder = StringBuilder()
        for (c in 1..passLen){
            val index = (Math.random() *1876564 % chars.size).toInt()
            sbuilder.append(chars[index])
        }
        password = sbuilder.toString()
        tvPassLen.text

        tvPassWd.text = password
    }

    interface RPDialogListener{
        fun onCancel()

        fun onOk(pass: String)
    }
}


