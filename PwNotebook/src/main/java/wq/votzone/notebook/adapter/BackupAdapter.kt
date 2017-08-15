package wq.votzone.notebook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import wq.votzone.notebook.R
import wq.votzone.notebook.model.BackupItem

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/8/1
 * Modification History:
 * Why & What modified:
 */
class BackupAdapter (context: Context,var listener: View.OnClickListener): BaseAdapter() {

    var mInflater = LayoutInflater.from(context)
    var mData = ArrayList<BackupItem>()

    fun setData(data: ArrayList<BackupItem>){
        mData.clear()
        mData.addAll(data)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder :BackupHolder?
        var v = convertView
        if(convertView == null){
            v = mInflater.inflate(R.layout.backup_adapter_layout, null, false)
            holder = BackupHolder(v)
            holder.bindListener(listener)
            v.tag = holder
        }else{
            holder = v!!.tag as BackupHolder
        }
        holder.bindData(mData[position])

        return v!!
    }

    override fun getItem(position: Int): Any {
        return mData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mData.size
    }

    class BackupHolder(var root:View){
        var tvName = root.findViewById(R.id.tv_name) as TextView
        var tvLocation = root.findViewById(R.id.tv_location) as TextView
        var tvTime = root.findViewById(R.id.tv_time)  as TextView

        fun bindListener(listener: View.OnClickListener){
            root.setOnClickListener (listener)
            root.setOnClickListener(listener)
        }

        fun bindData(data: BackupItem){
            tvName.text = data.fileName
            tvLocation.text = data.filePath
            tvTime.text = data.createTime
            root.setTag(R.id.tag_backup_item, data)
        }
    }
}