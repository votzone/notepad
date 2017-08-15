package wq.votzone.notebook.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text
import wq.votzone.notebook.R
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.dbmodel.OtherItem
import wq.votzone.notebook.util.GlobalUtil
import wq.votzone.notebook.util.put2Clipboard

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/19
 * Modification History:
 * Why & What modified:
 */

class ShowAccountItemDialog(context: Context) :
        Dialog(context,android.R.style.Theme_Material_Light_Dialog_NoActionBar),
        View.OnClickListener{

    override fun onClick(v: View?) {
        if(v !=null){
            if(v.getTag(R.id.tag_copy_content) !=null) {
                val str = v.getTag(R.id.tag_copy_content) as String
                put2Clipboard(context, str)
            }
        }
    }

    lateinit var vLevel:View
    lateinit var tvPlatform: TextView
    lateinit var tvUsername: TextView
    lateinit var tvPassword: TextView
    lateinit var vUsername: View
    lateinit var vPassword: View
    lateinit var llItems:LinearLayout
    val extraHolderList = ArrayList<ExtraViewHolder>()

    val inflater = LayoutInflater.from(context)

    init {
        initDialog()
    }
    fun initDialog(){
        setContentView(R.layout.show_acc_item_dialog)

        vLevel = findViewById(R.id.v_acc_level)
        tvPlatform = findViewById(R.id.tv_value_platform) as TextView
        tvUsername = findViewById(R.id.tv_value_username) as TextView
        tvPassword = findViewById(R.id.tv_value_password) as TextView
        vUsername = findViewById(R.id.ll_username)
        vPassword = findViewById(R.id.ll_password)
        llItems = findViewById(R.id.ll_items) as LinearLayout

        vUsername.setOnClickListener(this)
        vPassword.setOnClickListener(this)
    }

    fun setAccountItem(accitem: AccountModel){
        vLevel.setBackgroundResource(accitem.level.drawable)
        tvPlatform .text = accitem.platform
        tvUsername.text = accitem.username
        tvPassword.text = accitem.password
        vUsername.setTag(R.id.tag_copy_content, accitem.username)
        vPassword.setTag(R.id.tag_copy_content, accitem.password)

        for(extraItem in accitem.otherItems){
            val root = inflater.inflate(R.layout.show_acc_item_layout, null, false)
            extraHolderList.add(ExtraViewHolder(root, extraItem).initListener(this))
            llItems.addView(root)
        }
    }



    class ExtraViewHolder(root: View, extraItem: OtherItem){
        val content = root
        val tvKey = root.findViewById(R.id.tv_key) as TextView
        val tvValue = root.findViewById(R.id.tv_value)as TextView
        init {
            tvKey.text = extraItem.name
            tvValue.text = extraItem.value
            content.setTag(R.id.tag_copy_content, extraItem.value)
        }

        fun initListener(listener: View.OnClickListener):ExtraViewHolder{
            content.setOnClickListener (listener)
            return this
        }

    }
}
