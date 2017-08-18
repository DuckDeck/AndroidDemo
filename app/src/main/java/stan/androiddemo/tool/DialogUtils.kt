package stan.androiddemo.tool

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import stan.androiddemo.R

/**
 * Created by stanhu on 18/8/2017.
 */
class DialogUtils{
    companion object {
        fun showDeleteDialog(mContext: Context, boardTitle: String, listener: DialogInterface.OnClickListener) {
            val builder = AlertDialog.Builder(mContext)
            builder.setIcon(R.drawable.ic_report_problem_black_24dp)
            builder.setTitle(mContext.getString(R.string.dialog_title_delete))
            val message = mContext.getString(R.string.dialog_delete_message)
            builder.setMessage(String.format(message, boardTitle))
            builder.setPositiveButton(mContext.getString(R.string.dialog_delete_positive), listener)
            builder.setNegativeButton(mContext.getString(R.string.dialog_negative), null)
            builder.show()
        }
    }
}