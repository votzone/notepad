package wq.votzone.notebook.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView

import wq.votzone.notebook.R
import wq.votzone.notebook.adapter.SelectorAdapter
import wq.votzone.notebook.enum.AccountLevel

class HalfActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var lvItems:ListView
    lateinit var adapter:SelectorAdapter
    lateinit var llContentView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_half)

        initView()
        initAction()
        initData()
    }

    fun initView(){

        lvItems = findViewById(R.id.lv_items) as ListView
        adapter = SelectorAdapter(this, this)
        lvItems.adapter = adapter

        llContentView = findViewById(R.id.ll_contentview)

    }

    fun initAction(){
        llContentView.setOnClickListener(this)
    }

    fun initData(){

    }

    override fun onClick(v: View?) {
        if(v !=null) {
            if(v.id == R.id.ll_contentview){
                setResult(Activity.RESULT_CANCELED)
                finish()
            }else {
                val o = v.getTag(R.id.tag_acc_level)
                if (o != null && o is AccountLevel) {
                    val intent = Intent(this, AddItemActivity.javaClass)
                    intent.putExtra(request_data, o)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {

        fun launch(from: android.app.Activity) {
            val intent = android.content.Intent(from, HalfActivity::class.java)
//            from.startActivity(intent)
            from.startActivityForResult(intent, request_code_selector)
        }
    }
}


val request_code_selector = 19191
val request_data = "request_data"
