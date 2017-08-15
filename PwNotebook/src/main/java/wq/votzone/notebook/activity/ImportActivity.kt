package wq.votzone.notebook.activity

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.ipaulpro.afilechooser.FileChooserActivity
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import org.json.JSONArray

import wq.votzone.notebook.R
import wq.votzone.notebook.adapter.BackupAdapter
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.dbmodel.getAllAccount
import wq.votzone.notebook.ex.WrongKeyException
import wq.votzone.notebook.model.BackupItem
import wq.votzone.notebook.util.Const
import wq.votzone.notebook.util.FileUtils
import wq.votzone.notebook.util.showToast
import java.io.File
import java.util.regex.Pattern

class ImportActivity : AppCompatActivity(), View.OnClickListener{
    private val REQUEST_CHOOSER = 1234
    lateinit var evInput:EditText

    override fun onClick(v: View?) {
        var o= v!!.getTag(R.id.tag_backup_item)
        if(o !=null && o is BackupItem){

            if(v.id == R.id.ll_btn1){
                if(dialog != null){
                    dialog!!.dismiss()
                }
                try {
                    var jstr= o.decodeFile()
                    merge(jstr)
                }catch ( e: WrongKeyException){
                    var ld = AlertDialog.Builder(this)
                    evInput = EditText(this)
                    ld.setView(evInput)
                    ld.setMessage("当前密码解密失败,请输入密码!")
                    ld.setNegativeButton("取消",null)
                    ld.setPositiveButton("确定"){ dialog, which ->
                        var inputstr = evInput.text .toString()
                        if(!TextUtils.isEmpty(inputstr)){
                            try {
                                var jstr = o.decodeFileWithKey(inputstr)
                                merge(jstr)
                            }catch (e: WrongKeyException){
                                showToast(mActivity, "密码错误!")
                            }
                        }
                    }
                    dialog = ld.create()
                    dialog!!.show()
                }


            }else if(v.id == R.id.ll_btn2){
                if(dialog != null){
                    dialog!!.dismiss()
                }

                try {
                    var jstr= o.decodeFile()
                    cover(jstr)
                }catch ( e: WrongKeyException){
                    var ld = AlertDialog.Builder(this)
                    evInput = EditText(this)
                    ld.setView(evInput)
                    ld.setMessage("当前密码解密失败,请输入密码!")
                    ld.setNegativeButton("取消",null)
                    ld.setPositiveButton("确定"){ dialog, which ->
                        var inputstr = evInput.text as String
                        if(!TextUtils.isEmpty(inputstr)){
                            try {
                                var jstr = o.decodeFileWithKey(inputstr)
                                cover(jstr)
                            }catch (e: WrongKeyException){
                                showToast(mActivity, "密码错误!")
                            }
                        }
                    }
                    dialog = ld.create()
                    dialog!!.show()
                }
            }else if(v.id == R.id.ll_btn3){
                o.deleteFile()
                initData()
                if(dialog != null){
                    dialog!!.dismiss()
                }
            }else if(v.id == R.id.ll_btn4){
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(o.innerfile))
                intent.type = "*/*"
                startActivity(Intent.createChooser(intent, "发送"))
                if(dialog != null){
                    dialog!!.dismiss()
                }
            }
            else if(v.id == R.id.ll_root) {
                var ld = AlertDialog.Builder(this)
                ld.setView(BtnDialogHolder(R.layout.dialog_btn_list_layout, o, this).root)
                dialog = ld.create()
                dialog!!.show()
            }
        }
    }

    fun merge(jstr:String){
        var list = getAllAccount()
        var jary = JSONArray(jstr)
        for (i in 0.. jary.length()-1){
            var ai = AccountModel()
            ai.convertFrom(jary.getJSONObject(i))
            var notSave = false
            for (a2 in list){
                if(ai.equals(a2)){
                    notSave = true
                    break
                }
            }
            if(!notSave){
                ai.saveInstance()
            }
        }
        val intent = Intent(Const.broadcast_account_list_updated)
        sendBroadcast(intent)
        showToast(this, "合并导入成功")
    }

    fun cover(jstr: String){
        var list = getAllAccount()
        for (a2 in list){
            a2.delete()
        }

        var jary = JSONArray(jstr)
        for (i in 0.. jary.length()-1){
            var ai = AccountModel()
            ai.convertFrom(jary.getJSONObject(i))
            ai.saveInstance()
        }

        val intent = Intent(Const.broadcast_account_list_updated)
        sendBroadcast(intent)
        showToast(this, "覆盖导入成功")
    }

    lateinit  var adapter:BackupAdapter

    lateinit var vEmpty:View
    lateinit var lvList:ListView
    lateinit var tvChooseFile:View

    lateinit var mActivity: Activity
    var dialog:Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import)


        handleIntent()

        mActivity = this

        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { navBack() }

        tvChooseFile = toolbar.findViewById(R.id.tv_choose_file)

        vEmpty = findViewById(R.id.rl_empty)
        lvList = findViewById(R.id.lv_list) as ListView
        adapter = BackupAdapter(this,this)

        lvList.adapter = adapter
        initData()

        tvChooseFile.setOnClickListener {
            MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(REQUEST_CHOOSER)
                    .withFilter(Pattern.compile(".*\\.export$")) // Filtering files and directories by file name using regexp
//                    .withFilterDirectories(true) // Set directories filterable (false by default)
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHOOSER) {
            var uri = data!!.getData()

            // Get the File path from the Uri
            var filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Alternatively, use FileUtils.getFile(Context, Uri)
            if (filePath != null) {
                var ofile = File(filePath)
                var nfile = File(FileUtils.getExportDir(), ofile.name)
                if(!nfile.exists()){
                    ofile.copyTo(nfile)
                    showToast(this,"导入数据成功!")
                    initData()
                }else{
                    showToast(this,"数据已存在,不需要导入!")
                }
            }
        }
    }

    fun handleIntent(){
        var uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        if(uri != null) {
            Log.e("path", uri.toString() + ":" + uri.path)
            if(!uri.path.endsWith(".export")){
                showToast(this,"文件格式不正确!")
                return
            }
            var dir = FileUtils.getExportDir()
            var ofile = File(uri.path)
            var nfile = File(dir,ofile.name)
            if(!nfile.exists()){
                ofile.copyTo(nfile)
                showToast(this,"导入数据成功!")
            }else{
                showToast(this,"数据已存在,不需要导入!")
            }
        }
    }

    fun navBack(){
        finish()
    }

    fun initData(){
        PermissionGen.needPermission(this, Const.REQUEST_EXTERNAL_STORAGE, Const.permissions_storage)
    }

    @PermissionSuccess(requestCode = Const.REQUEST_EXTERNAL_STORAGE)
    fun doSuccess(){
        val list = BackupItem.getBackupItemList()
        if(list.size >0) {
            adapter.setData(list)
            adapter.notifyDataSetChanged()
            vEmpty.visibility = View.GONE
            lvList.visibility = View.VISIBLE
        }else{
            vEmpty.visibility = View.VISIBLE
            lvList.visibility = View.GONE
        }
    }

    companion object {

        fun launch(from: android.app.Activity) {
            val intent = android.content.Intent(from, ImportActivity::class.java)
            from.startActivity(intent)
        }
    }

    inner class BtnDialogHolder(id: Int, data: BackupItem, listener: View.OnClickListener){
        var mInflater = LayoutInflater.from(mActivity)
        var root = mInflater.inflate(id, null, false)
        var btn1 = root.findViewById(R.id.ll_btn1)
        var btn2 = root.findViewById(R.id.ll_btn2)
        var btn3 = root.findViewById(R.id.ll_btn3)
        var btn4 = root.findViewById(R.id.ll_btn4)
        init {
            btn1.setOnClickListener(listener)
            btn1.setTag(R.id.tag_backup_item, data)
            btn2.setOnClickListener(listener)
            btn2.setTag(R.id.tag_backup_item, data)
            btn3.setOnClickListener(listener)
            btn3.setTag(R.id.tag_backup_item, data)
            btn4.setOnClickListener (listener)
            btn4.setTag(R.id.tag_backup_item, data)
        }
    }
}
