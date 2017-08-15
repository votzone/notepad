package wq.votzone.notebook.dbmodel

import org.json.JSONObject
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table
import wq.votzone.notebook.util.DBUtil

import java.util.Date

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/5
 * Modification History:
 * Why & What modified:
 */

@Table(name = "other_item")
class OtherItem {
    @Column(name = "id", isId = true)
    internal var id: Int = 0

    @Column(name = "name")
     var name: String = "备注"

    @Column(name = "value")
     var value: String = ""

    @Column(name = "time")
    internal var time: Date = Date()

    @Column(name = "accountId")
    internal var accountId = -1

}
