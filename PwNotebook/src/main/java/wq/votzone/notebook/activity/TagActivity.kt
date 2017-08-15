package wq.votzone.notebook.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView

import wq.votzone.notebook.R
import wq.votzone.notebook.adapter.TagAdapter
import wq.votzone.notebook.dbmodel.Tag
import wq.votzone.notebook.util.showToast

class TagActivity : AppCompatActivity() ,View.OnClickListener{
    override fun onClick(v: View?) {
        if(v==null){
            return
        }
        if(v.id == R.id.tv_tag){
            // 点击标签列表中的一项
            val o = v.getTag(R.id.tag_tag_tag)
            if( o != null && o is Tag){
                o.set2Tag(mtag)
                etInput.setText(mtag.name)
            }
        }else if(v.id == R.id.tv_btn_ok){

            navBack()

        }
    }

    fun navBack(){
        if(saveTag()){
            setOk()
        }else{
            showToast(this, "取消设置标签")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    fun setOk(){
        val intent = Intent(this, AddItemActivity.javaClass)
        intent.putExtra(Tag_request_data, mtag)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun saveTag():Boolean{
        val inputtag = etInput.text
        if(TextUtils.isEmpty(inputtag)){
            showToast(this, "请输入标签")
            return false
        }
        if(!TextUtils.equals(mtag.name, inputtag)){
            mtag.name = inputtag.toString()
            mtag.id = 0
            mtag.saveInstance()
        }
        return true
    }

    lateinit var etInput:EditText
    lateinit var tvBtnOk: TextView
    lateinit var gvTags:GridView

    val mtag = Tag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { navBack() }

        initView()
        initData()
    }

    fun initView(){
        etInput = findViewById(R.id.et_input) as EditText
        tvBtnOk = findViewById(R.id.tv_btn_ok) as TextView
        gvTags = findViewById(R.id.gv_tags) as GridView
    }

    fun initData(){
        gvTags.adapter = TagAdapter(this, this)
        tvBtnOk.setOnClickListener(this)
    }

    companion object{
        fun launch(from: Activity){
            val intent = Intent(from, TagActivity::class.java)
            from.startActivityForResult(intent, Tag_Request_Code)
        }
    }
}

const val Tag_Request_Code = 18405
const val Tag_request_data = "tag_request_data"
