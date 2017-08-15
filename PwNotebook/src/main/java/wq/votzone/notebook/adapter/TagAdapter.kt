package wq.votzone.notebook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import wq.votzone.notebook.R
import wq.votzone.notebook.dbmodel.Tag
import wq.votzone.notebook.dbmodel.getAllTag

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/12
 * Modification History:
 * Why & What modified:
 */
class TagAdapter(context: Context, val listener: View.OnClickListener): BaseAdapter(){

    val mInflater = LayoutInflater.from(context)
    val mData: ArrayList<Tag> = getAllTag()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        var holder:TagHolder?
        if(convertView == null){
            view = mInflater.inflate(R.layout.gv_tag_item, null , false)
            holder = TagHolder(view)
            holder.bindListener(listener)
            view.tag = holder
        }else{
            holder = view!!.tag as TagHolder
        }

        val item = mData[position]
        holder.bindTag(item)

        return view!!
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mData.size
    }

}

class TagHolder(root: View){
    val textView = root.findViewById(R.id.tv_tag) as TextView

    fun bindListener(listener: View.OnClickListener){
        textView.setOnClickListener(listener)
    }

    fun bindTag(tag: Tag){
        textView.text = tag.name
        textView.setTag(R.id.tag_tag_tag, tag)
    }



}