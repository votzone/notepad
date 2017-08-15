package wq.votzone.notebook.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import wq.votzone.notebook.R
import wq.votzone.notebook.enum.AccountLevel
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.dbmodel.OtherItem
import wq.votzone.notebook.dbmodel.Tag
import wq.votzone.notebook.dialog.RandPsDailog
import wq.votzone.notebook.util.Const
import wq.votzone.notebook.util.Const.REQUEST_EXTERNAL_STORAGE
import wq.votzone.notebook.util.Const.permissions_storage
import wq.votzone.notebook.util.showToast
import kotlin.collections.ArrayList


class AddItemActivity : AppCompatActivity() ,View.OnClickListener, RandPsDailog.RPDialogListener {
    override fun onCancel() {}

    override fun onOk(pass: String) {
        if(TextUtils.isEmpty(etPassword.text)){
            etPassword.setText(pass)
        }
    }

//    var backupmoreList = ArrayList<OtherItem>()
    override fun onClick(v: View) {
            if(v.id == R.id.tv_level_selector){
                // 打开半透明 Activity
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                HalfActivity.launch(this)
            }else if(v.id == R.id.tv_rand_pas){
                // 打开对话框
                val dialog = RandPsDailog(this)
                dialog.listener = this
                dialog.show()

            }else if(v.id == R.id.tv_tag_add){
                // 打开 创建/选择标签 页面
                TagActivity.launch(this)
            }else if(v.id == R.id.tv_more_item){
                // 添加一项
                val holder = ExtraItemHolder(inflater, llItems, holderList)
                llItems.addView(holder.root)
                holderList.add(holder)

            }else if(v.id == R.id.ll_save){

                item.platform = etPlatform.text.toString()
                item.username = etUsername.text.toString()
                item.password = etPassword.text.toString()

                if(TextUtils.isEmpty(item.platform)){
                    showToast( this, "请输入账号平台")
                    return
                }
                if(TextUtils.isEmpty(item.username)){
                    showToast( this, "请输账号")
                    return
                }
                if(TextUtils.isEmpty(item.password)){
                    showToast( this, "请输入密码")
                    return
                }

                for(holder in holderList){
                    if(!holder.isOk()){
                        return
                    }
                    item.otherItems.add(holder.convert2OtherItem())
                }

                // addall 仅适用于添加, 当修改时,需要判断根据 已有的otheritem 和当前的otheritem 来进行 增,删,该操作
//                item.moreItems.addAll(backupmoreList)
                PermissionGen.needPermission(this, REQUEST_EXTERNAL_STORAGE, permissions_storage)

            }else if(v == icTag){
                item.removeLastTag()
                initTagListPart()
            }

    }

    lateinit var etPlatform: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var tvLevel: TextView
    lateinit var tvTag1: TextView
    lateinit var tvTag2: TextView
    lateinit var tvTag3: TextView
    lateinit var tvTagAdd: TextView
    lateinit var tvRandps: TextView
    lateinit var tvMore: TextView
    lateinit var icPlatform: LinearLayout
    lateinit var icUsername: LinearLayout
    lateinit var icTag: LinearLayout
    lateinit var icPassword: LinearLayout

    lateinit var tvTaglist:Array<TextView>
    var item = AccountModel();
//    var canSave = true

    lateinit var tvTitle:TextView
    lateinit var llSave: LinearLayout

    lateinit var llItems: LinearLayout

    lateinit var inflater: LayoutInflater

    val holderList = ArrayList<ExtraItemHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        inflater = LayoutInflater.from(this)
        handleIntent(intent)
        initView()
        initData()
        initAction()
    }

    private fun handleIntent(intent: Intent){
        if(intent !=null){
//            canSave = intent.getBooleanExtra("canSave", true)
            var s = intent.getSerializableExtra("account_item") as AccountModel?
            if(s != null){
                item = s
            }
        }
    }

    private fun initView(){
        etPlatform = findViewById(R.id.et_value_platform) as EditText
        etUsername = findViewById(R.id.et_value_username) as EditText
        etPassword = findViewById(R.id.et_value_password) as EditText
        tvLevel = findViewById(R.id.tv_level_selector) as TextView
        tvTag1 = findViewById(R.id.tv_tag_0) as TextView
        tvTag2 = findViewById(R.id.tv_tag_1) as TextView
        tvTag3 = findViewById(R.id.tv_tag_2) as TextView
        tvTaglist = arrayOf(tvTag1,tvTag2,tvTag3)

        tvTagAdd = findViewById(R.id.tv_tag_add) as TextView
        tvRandps = findViewById(R.id.tv_rand_pas) as TextView
        tvMore = findViewById(R.id.tv_more_item) as TextView

        icPlatform = findViewById(R.id.platform_input_close) as LinearLayout
        icUsername = findViewById(R.id.username_input_close) as LinearLayout
        icPassword = findViewById(R.id.password_input_close) as LinearLayout
        icTag = findViewById(R.id.tag_input_close) as LinearLayout

        tvTitle = findViewById(R.id.tv_title) as TextView
        tvTitle.text = "添加账号"

        llSave = findViewById(R.id.ll_save)as LinearLayout
        llItems = findViewById(R.id.ll_item) as LinearLayout
    }

    private fun initAction(){
        tvLevel.setOnClickListener(this)
        tvRandps.setOnClickListener(this)
        tvMore.setOnClickListener(this)
        icPlatform.setOnClickListener { etPlatform.setText(item.platform) }
        icUsername.setOnClickListener { etUsername.setText(item.username) }
        icPassword.setOnClickListener { etPassword.setText(item.password) }
        tvTagAdd.setOnClickListener(this)
        llSave.setOnClickListener (this)
        icTag.setOnClickListener (this)
    }

    private fun initTagListPart(){
        tvTag2.visibility = View.GONE
        tvTag3.visibility = View.GONE
        for((index, value) in item.taglist.withIndex()){
            tvTaglist[index].visibility = View.VISIBLE
            tvTaglist[index].text = value.name
        }
        if(item.taglist.size >=3){
            tvTagAdd.visibility = View.GONE
        }else{
            tvTagAdd.visibility = View.VISIBLE
        }
        if(item.taglist.size ==0){
            tvTag1.text="默认"
        }
    }

    private fun initData(){
        etPlatform.setText(item.platform)
        etUsername.setText(item.username)
        etPassword.setText(item.password)

        tvLevel.text = item.level.des
        tvLevel.setTextColor(item.level.color)


        initTagListPart()

        //显示 accountItem 中的moreItems
        for(otheritem in item.otherItems){
            val holder = ExtraItemHolder(inflater, llItems, holderList)
            holder.bindOtherItem(otheritem)
            llItems.addView(holder.root)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home ->{
                    finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            request_code_selector ->{
                if(resultCode == Activity.RESULT_OK){
                    item.level = data!!.getSerializableExtra(request_data) as AccountLevel
                    tvLevel.text = item.level.des
                    tvLevel.setTextColor(item.level.color)
                }
            }
            Tag_Request_Code->{
                if(resultCode == Activity.RESULT_OK){
                    val tag = data!!.getSerializableExtra(Tag_request_data) as Tag

                    var needAdd = true
                    for(t in item.taglist){
                        if(TextUtils.equals(t.name, tag.name)){
                            needAdd = false
                        }
                    }
                    if(needAdd) {
                        item.taglist.add(tag)
                        initTagListPart()
                    }
                }
            }
            else->{
                // donothing
            }
        }
    }

    @PermissionSuccess(requestCode = REQUEST_EXTERNAL_STORAGE)
    fun doSuccess(){
        item.saveInstance()
        showToast(this,"保存成功!")
        val intent = Intent(Const.broadcast_account_list_updated)
        sendBroadcast(intent)
        finish()
    }

    @PermissionFail(requestCode = REQUEST_EXTERNAL_STORAGE)
    fun doFailed(){
        showToast(this, "没有获取到存储权限")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    companion object {

        fun launch(from: android.app.Activity) {
            val intent = android.content.Intent(from, AddItemActivity::class.java)
            from.startActivity(intent)
        }

        fun launch(from: Context, accitem:AccountModel){
            val intent = android.content.Intent(from, AddItemActivity::class.java)
            intent.putExtra("account_item", accitem)
            from.startActivity(intent)
        }
    }
}

class ExtraItemHolder(inflater: LayoutInflater, parent:ViewGroup, holderlist: ArrayList<ExtraItemHolder>){

    val root = inflater.inflate(R.layout.extras_item, null, false)

    val etKey = root.findViewById(R.id.et_key) as EditText

    val etValue = root.findViewById(R.id.et_value) as EditText

    val icClose = root.findViewById(R.id.input_close) as LinearLayout

    var otherItem:OtherItem?? = null

    init {
        icClose.setOnClickListener {
            if(!TextUtils.isEmpty(etValue.text)){
                etValue.setText("")
            }else if(!TextUtils.isEmpty(etKey.text)){
                etKey.setText("")
            }else{
                parent.removeView(root)
                holderlist.remove(this)
            }
        }
    }

    fun isOk():Boolean{
        if(TextUtils.isEmpty(etKey.text)){
            showToast(root.context,"请输入名称")
            return false
        }else if(TextUtils.isEmpty(etValue.text)){
            showToast(root.context,"请输入值")
            return false
        }
        return true
    }

    fun bindOtherItem(otherItem: OtherItem){
        this.otherItem = otherItem
        etKey.setText(otherItem.name)
        etValue.setText(otherItem.value)
    }

    fun convert2OtherItem(): OtherItem{
        if(otherItem==null)
            otherItem = OtherItem()

        otherItem!!.name = etKey.text.toString()
        otherItem!!.value = etValue.text.toString()
        return otherItem!!
    }

}
