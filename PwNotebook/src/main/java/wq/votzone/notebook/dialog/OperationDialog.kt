package wq.votzone.notebook.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import wq.votzone.notebook.R
import wq.votzone.notebook.activity.AddItemActivity
import wq.votzone.notebook.callback.ConfirmCallback
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.util.Const.broadcast_account_list_updated

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/20
 * Modification History:
 * Why & What modified:
 */

class OperationDialog(context: Context, var accItem: AccountModel):
        Dialog(context,android.R.style.Theme_Material_Light_Dialog_NoActionBar),
        View.OnClickListener, ConfirmCallback{
    override fun cancelCallback() {

    }

    override fun okCallback() {
        accItem.delete()
        val intent = Intent(broadcast_account_list_updated)
        context.sendBroadcast(intent)
    }


    lateinit var viewUpdate:View
    lateinit var viewDelate:View

    init {
        initDialog()
    }

    fun initDialog(){
        setContentView(R.layout.show_operation_dialog)
        viewUpdate = findViewById(R.id.ll_update)
        viewDelate = findViewById(R.id.ll_delete)
        viewDelate.setOnClickListener(this)
        viewUpdate.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        dismiss()
        if(v !=null){
            if(v == viewDelate){

                val confirmDialog = ConfirmDialog(context)
                confirmDialog.setTitleAndContent("确认删除","确认删除当前账号信息?")
                confirmDialog.confirmCallback = this
                confirmDialog.show()
            }else if(v == viewUpdate){

                AddItemActivity.launch(context, accItem)
            }
        }
    }

}