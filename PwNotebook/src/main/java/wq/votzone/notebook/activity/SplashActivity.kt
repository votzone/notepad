package wq.votzone.notebook.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import wq.votzone.notebook.R
import wq.votzone.notebook.adapter.BannerAdapter
import wq.votzone.notebook.util.PrefUtil
import wq.votzone.notebook.util.dip2px


class SplashActivity : AppCompatActivity(), Runnable, ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        for(idx in mIdlist.indices){
            val iv = mIdlist.get(idx)
            if(idx == position){
                iv.setImageResource(R.mipmap.ic_point_choosed)
            }else{
                iv.setImageResource(R.mipmap.ic_point_normal)
            }
        }
    }

    override fun run() {
        NotePadActivity.launch(this)
        finish()
    }

    var mHandler = Handler()

    lateinit var mViewPagger : ViewPager
    lateinit var mIndicators: LinearLayout
    var mIdlist = ArrayList<ImageView>()

    lateinit var rlNormal: RelativeLayout
    lateinit var rlBanner: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        rlNormal = findViewById(R.id.rl_normal) as RelativeLayout
        rlBanner = findViewById(R.id.rl_banners) as RelativeLayout
        initNormalView()
//        val pu = PrefUtil(this)
//        if(pu.isFirstlaunch()){
//            initBanner()
//        }else {
//            initNormalView()
//        }
    }

    private fun initNormalView(){
        rlNormal.visibility = View.VISIBLE
        rlBanner.visibility = View.GONE
        mHandler.postDelayed(this, 2000)
    }

    private fun initBanner(){
        rlNormal.visibility = View.GONE
        rlBanner.visibility = View.VISIBLE

        // 初始化banner 相关view
        mViewPagger = findViewById(R.id.vp_banners) as ViewPager
        mIndicators = findViewById(R.id.ll_indicator) as LinearLayout
        var badapter = BannerAdapter(this)
        mViewPagger.adapter = badapter
        mViewPagger.addOnPageChangeListener(this)

        var ivlist = ArrayList<View>()

        var resids = ArrayList<Int>()
        resids.add(R.mipmap.splash_page_1)
        resids.add(R.mipmap.splash_page_2)
        for(resid in resids){
            // 添加内容imageview
            var imageView = ImageView(this)
            imageView.setImageResource(resid)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            ivlist.add(imageView)

            if(resid == R.mipmap.splash_page_2){
                imageView.setOnClickListener {
                    NotePadActivity.launch(this)
                    finish()
                }
            }

            // 添加指示器imageview
            imageView = ImageView(this)
            if(resid == R.mipmap.splash_page_1) {
                imageView.setImageResource(R.mipmap.ic_point_choosed)
            }else{
                imageView.setImageResource(R.mipmap.ic_point_normal)
            }
            mIdlist.add(imageView)

            // 添加指示器到 指示器linearlayout
            var lp = LinearLayout.LayoutParams(dip2px(this, 8f), dip2px(this, 8f))
            lp.leftMargin = dip2px(this,10f)
            mIndicators!!.addView(imageView, lp)
        }
        badapter.mData = ivlist
    }

}
