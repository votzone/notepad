package wq.votzone.notebook.adapter

import android.content.Context
import android.graphics.drawable.shapes.Shape
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import wq.votzone.notebook.R
import wq.votzone.notebook.dbmodel.AccountModel
import wq.votzone.notebook.dbmodel.getAllAccount
import wq.votzone.notebook.dialog.OperationDialog

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/30
 * Modification History:
 * Why & What modified:
 */
class AccRVAdapter(var context: Context, var listener:View.OnClickListener)
    :RecyclerView.Adapter<AccRVAdapter.AccViewHolder>()
,View.OnLongClickListener{
    override fun onLongClick(v: View?): Boolean {
        //root.setTag(, item)
        if(v !=null && v.getTag(R.id.tag_acc_item) !=null){
            var accitem = v.getTag(R.id.tag_acc_item) as AccountModel
            var opdialog = OperationDialog(context, accitem)
            opdialog.show()
        }
        return true
    }

    val mInflater = LayoutInflater.from(context)
    
    val acclist = getAllAccount()

    fun checkDataSetChanged(){
        acclist.clear()
        acclist.addAll(getAllAccount())
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccViewHolder {
        val root = mInflater.inflate(R.layout.acc_rv_item, parent, false)
        return AccViewHolder(root).bindListener(listener).bindLongClickListener(this)
    }

    override fun onBindViewHolder(holder: AccViewHolder?, position: Int) {
        var item = acclist[position]
        holder!!.bindData(item)
    }

    override fun getItemCount(): Int {
        return acclist.size
    }

    fun getItem(position: Int):AccountModel{
        return acclist[position]
    }

    class AccViewHolder(var root: View): RecyclerView.ViewHolder(root){

        val vLevel = root.findViewById(R.id.v_acc_level)
        val tvPlatform = root.findViewById(R.id.tv_platform) as TextView
        val tvUsername = root.findViewById(R.id.tv_username)as TextView

        fun bindListener(listener:View.OnClickListener):AccViewHolder{
            root.setOnClickListener (listener)
            return this
        }

        fun bindLongClickListener(longClickListener: View.OnLongClickListener):AccViewHolder{
            root.setOnLongClickListener(longClickListener)
            return this
        }

        fun bindData(item: AccountModel){
            vLevel.setBackgroundResource(item.level.drawable)
            tvPlatform.text = item.platform
            tvUsername.text = item.username
            root.setTag(R.id.tag_acc_item, item)
        }

    }
}