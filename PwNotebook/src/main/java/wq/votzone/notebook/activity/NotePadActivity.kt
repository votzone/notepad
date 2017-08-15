package wq.votzone.notebook.activity

import android.app.ProgressDialog
import android.content.*
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import wq.votzone.notebook.R
import wq.votzone.notebook.adapter.AccRVAdapter
import wq.votzone.notebook.enum.AccountLevel
import java.util.*
import android.content.pm.PackageManager
import android.didikee.donate.AlipayDonate
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager

import android.widget.RelativeLayout
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import org.json.JSONArray
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.dbmodel.getAllAccount
import wq.votzone.notebook.dialog.ConfirmDialog
import wq.votzone.notebook.dialog.OperationDialog
import wq.votzone.notebook.dialog.ShowAccountItemDialog
import wq.votzone.notebook.util.*
import wq.votzone.notebook.util.Const.broadcast_account_list_updated
import kotlin.collections.ArrayList
import android.didikee.donate.WeiXinDonate
import android.graphics.BitmapFactory
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.R.raw
import android.os.Environment
import java.io.File


class NotePadActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener ,
        View.OnClickListener{


    lateinit var rlContent : RelativeLayout
    lateinit var rvListView: RecyclerView
    lateinit var rvAdapter: AccRVAdapter
    lateinit var vEmpty: View

    var myreceiver = UpdateAccItemReceiver()

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(wq.votzone.notebook.R.layout.activity_note_pad)
        val toolbar = findViewById(wq.votzone.notebook.R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(wq.votzone.notebook.R.id.fab) as android.support.design.widget.FloatingActionButton
        fab.setOnClickListener(this)

        // 设置左侧边栏
        val drawer = findViewById(wq.votzone.notebook.R.id.drawer_layout) as android.support.v4.widget.DrawerLayout
        val toggle = android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(wq.votzone.notebook.R.id.nav_view) as android.support.design.widget.NavigationView
        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener(this)


        vEmpty = findViewById(R.id.rl_empty)
        rvListView = findViewById(R.id.rv_list) as RecyclerView
        rlContent = findViewById(R.id.rl_content) as RelativeLayout

        rvAdapter = AccRVAdapter(this,this)
        rvListView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvListView.adapter = rvAdapter

        checkEmpyt()
        var filter = IntentFilter(broadcast_account_list_updated)
        registerReceiver(myreceiver,filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myreceiver)
    }

    override fun onResume() {
        super.onResume()

    }

    fun checkEmpyt(){
        rvAdapter.checkDataSetChanged()
        if(rvAdapter.acclist.size >0){
            vEmpty.visibility = View.GONE
            rlContent.visibility = View.VISIBLE
        }else{
            vEmpty.visibility = View.VISIBLE
            rlContent.visibility = View.GONE
        }
    }

    var lastclick_time = 0L
    var timestamp_duration = 2000L
    //
    override fun onBackPressed() {
        val drawer = findViewById(wq.votzone.notebook.R.id.drawer_layout) as android.support.v4.widget.DrawerLayout
        if (drawer.isDrawerOpen(android.support.v4.view.GravityCompat.START)) {
            drawer.closeDrawer(android.support.v4.view.GravityCompat.START)
        } else {
            val willFinish = true
            if (willFinish) {
                val currTime = System.currentTimeMillis()
                if (lastclick_time == 0L) {
                    wq.votzone.notebook.util.showToast(this,
                            getString(R.string.double_click_back))
                    lastclick_time = currTime
                } else {
                    if (currTime - lastclick_time >= timestamp_duration) {
                        wq.votzone.notebook.util.showToast(this,
                                getString(R.string.double_click_back))
                        lastclick_time = currTime
                    } else {
                        finish()
                        lastclick_time = 0
                    }

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(wq.votzone.notebook.R.menu.note_pad, menu)
        return false
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == wq.votzone.notebook.R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == wq.votzone.notebook.R.id.nav_import) {
            ImportActivity.launch(this)

        } else if (id == wq.votzone.notebook.R.id.nav_outport) {
            var cd = AlertDialog.Builder(this)
            cd.setTitle("导出备份文件")
            cd.setMessage("备份文件将使用当前密码进行加密处理, 恢复时如果密码不一致需要输入,请牢记密码!")
            cd.setPositiveButton("确定",DialogInterface.OnClickListener {
                dialog, which ->
                PermissionGen.needPermission(this, Const.REQUEST_EXTERNAL_STORAGE, Const.permissions_storage)
            })
            cd.setNegativeButton("取消", null)
            cd.show()

        } else if (id == wq.votzone.notebook.R.id.nav_password) {

            PasswdActivity.launch(this)
        }
//        else if (id == wq.votzone.notebook.R.id.anav_navigator) {
//
//        }
        else if (id == wq.votzone.notebook.R.id.nav_remote) {
            RemoteActivity.launch(this);
        }
//        else if (id == wq.votzone.notebook.R.id.nav_hi_ascell) {
//
//        }
        else if (id == wq.votzone.notebook.R.id.nav_about) {
            AboutActivity.launch(this)
        } else if (id == wq.votzone.notebook.R.id.nav_scroe) {
//

            FeedbackActivity.launch(this)


        }

        val drawer = findViewById(wq.votzone.notebook.R.id.drawer_layout) as android.support.v4.widget.DrawerLayout
        drawer.closeDrawer(android.support.v4.view.GravityCompat.START)
        return true
    }



    var ai = AccountModel()

    override fun onClick(v: View?) {
        if(v !=null) {
            if (v.id == R.id.fab) {
                // start Activity to add acc info
                AddItemActivity.launch(this)
            }else if(v.getTag(R.id.tag_acc_item) !=null){
                val o = v.getTag(R.id.tag_acc_item) as AccountModel
                // 显示详情
                val dialog  = ShowAccountItemDialog(this)
                dialog.setAccountItem(o)
                dialog.show()
            }
        }
    }

    companion object {

        fun launch(from: android.app.Activity) {
            val intent = android.content.Intent(from, NotePadActivity::class.java)
            from.startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    DBUtil.saveBindingId(ai)

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    inner class UpdateAccItemReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkEmpyt()
        }

    }


    @PermissionSuccess(requestCode = Const.REQUEST_EXTERNAL_STORAGE)
    fun doSuccess(){
        exportData()
    }

    internal var mProgress: ProgressDialog? = null
    private fun newLoadingDialog(msg: String): ProgressDialog {
        if (mProgress == null) {
            mProgress = ProgressDialog(this)
            mProgress!!.setTitle(null)
            mProgress!!.isIndeterminate = true
            mProgress!!.setCancelable(true)
//            mProgress!!.setCanceledOnTouchOutside(true)
            mProgress!!.setCanceledOnTouchOutside(false)
        }
        mProgress!!.setMessage(msg)
        return mProgress!!
    }
    private fun hideLoadingDialog(){
        if(mProgress !=null){
            mProgress!!.dismiss()
        }
    }


    fun exportData(){

        newLoadingDialog("导出数据中...")
        var acclist = getAllAccount()
        var path = FileUtils.getExportPath()
        var jary = JSONArray()
        if(acclist.isEmpty()){
            showToast(this,"没有记录需要导出!")
            hideLoadingDialog()
            return
        }
        for( accitem in acclist){
            jary.put(accitem.convert2Json())
        }
        FileUtils.saveWithEncode(path, jary.toString())
        hideLoadingDialog()
        var msgdb = AlertDialog.Builder(this)
        msgdb.setTitle("提示")
        msgdb.setMessage("导出成功! 文件保存目录:"+path.absoluteFile)
        msgdb.setPositiveButton("确定", null)
        msgdb.show()
    }

}

