package wq.votzone.notebook.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import org.w3c.dom.Text
import wq.votzone.notebook.R
import wq.votzone.notebook.callback.ConfirmCallback

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/23
 * Modification History:
 * Why & What modified:
 */
class ConfirmDialog(context: Context):
        Dialog(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar),
View.OnClickListener{
    override fun onClick(v: View?) {
        if(v!=null){
            if(v == btnCancel){
                if(confirmCallback !=null){
                    confirmCallback!!.cancelCallback()
                }
                dismiss()
            }else if(v == btnOk){
                if(confirmCallback !=null){
                    confirmCallback!!.okCallback()
                }
                dismiss()
            }
        }
    }



    lateinit var tvTitle: TextView
    lateinit var tvContent: TextView
    lateinit var btnCancel: TextView
    lateinit var btnOk: TextView

    var confirmCallback : ConfirmCallback? = null

    init {
        initDialog()
    }

    fun setTitleAndContent(title:String, content:String){
        tvTitle.text = title
        tvContent.text = content
    }

    fun initDialog(){
        setContentView(R.layout.action_confirm_dialog_layout)
        tvTitle = findViewById(R.id.tv_title) as TextView
        tvContent = findViewById(R.id.tv_content) as TextView
        btnCancel = findViewById(R.id.tv_btn_cancel) as TextView
        btnOk = findViewById(R.id.tv_btn_ok) as TextView

        btnCancel.setOnClickListener(this)
        btnOk.setOnClickListener(this)

    }

}