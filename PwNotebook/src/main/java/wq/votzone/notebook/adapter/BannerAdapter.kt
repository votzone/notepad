package wq.votzone.notebook.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup



/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/29
 * Modification History:
 * Why & What modified:
 */
class BannerAdapter(activity: Activity) : PagerAdapter() {

    var mData = ArrayList<View>()
        set(value) {
            field.clear()
            field.addAll(value)
            this.notifyDataSetChanged()
        }


    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun destroyItem(container: ViewGroup, position: Int,
                             `object`: Any) {
        container.removeView(mData.get(position))
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mData.get(position))
        return mData.get(position)
    }



}