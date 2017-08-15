package wq.votzone.notebook.model

import wq.votzone.notebook.enum.AccountLevel

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/5
 * Modification History:
 * Why & What modified:
 */
data class AccAddItem(
        var key:String,
        var type:AccAddItemType,
        var value: Any? = null)

enum class AccAddItemType{
    Platform, Username, Password, Acclevel, Tag, Other
}