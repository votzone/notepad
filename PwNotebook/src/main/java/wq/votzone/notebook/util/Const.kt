package wq.votzone.notebook.util

import android.Manifest
import android.content.Context

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/29
 * Modification History:
 * Why & What modified:
 */
object Const {
    const val sp_file_name = "wq_notepad"
    const val First_Launch = "first_launch"
    const val Need_Show_PassHint = "Need_Show_PassHint"
    const val pass_word_key = "local_password"

    val permissions_storage = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE )
    const val REQUEST_EXTERNAL_STORAGE = 11010


    const val broadcast_account_list_updated = "account_list_updated"

    const val item_sep_er = ";"
    const val item_inner_sep_er = ":"

    const val pass_state_key = "pass_state_is_update"

}
