package wq.votzone.notebook.enum

import android.graphics.Color
import android.support.annotation.IdRes
import wq.votzone.notebook.R

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/30
 * Modification History:
 * Why & What modified:
 * <color name="tag1">#3064e3</color>
<color name="tag2">#4f68a6</color>
<color name="tag3">#eda900</color>
<color name="tag4">#d72a24</color>
<color name="tag5">#28973d</color>
 */
enum class AccountLevel(val level:Int, val des:String, val color: Int, val drawable: Int) {
    spam(-2,"批量注册", Color.argb(255,0x30,0x64,0xe3), R.drawable.acc_level_bg_1),
    soso(-1,"一般账号",  Color.argb(255,0x4f,0x68,0xa6), R.drawable.acc_level_bg_2),
    common(0,"普通账号",Color.argb(255,0xed,0xa9,0x00), R.drawable.acc_level_bg_3),
    vip(1,"重要账号", Color.argb(255,0xd7,0x2a,0x24), R.drawable.acc_level_bg_4),
    autonym(2,"实名账号", Color.argb(255,0x28,0x97,0x3d), R.drawable.acc_level_bg_4)
}