package wq.votzone.notebook.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import wq.votzone.notebook.R
import wq.votzone.notebook.enum.AccountLevel

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/6
 * Modification History:
 * Why & What modified:
 */

class SelectorAdapter(mActivity: Activity, var listener: View.OnClickListener) : BaseAdapter() {

    var mData = AccountLevel.values()

    var mInflater = LayoutInflater.from(mActivity)


    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Any? {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var root:View? = convertView
        var holder:SelectorViewHolder?
        if(root == null){
            root = mInflater.inflate(R.layout.selector_item, null, false)
            holder = SelectorViewHolder(root)
            holder.bindListener(listener)
            root!!.tag = holder
        }else{
            holder = root.tag as SelectorViewHolder?

        }

        val item = mData[position]

        holder!!.bindData(item)

        return root
    }
}

class SelectorViewHolder(root:View){
    var btn = root.findViewById(R.id.btn_text) as TextView

    fun bindData(acclevel: AccountLevel){
        btn.setText(acclevel.des)
        btn.setTag(R.id.tag_acc_level, acclevel)
        btn.setTextColor(acclevel.color)
    }

    fun bindListener(listener:View.OnClickListener){
        btn.setOnClickListener(listener)
    }
}