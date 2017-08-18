package stan.androiddemo.project.petal.Module.UserInfo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BaseDialogFragment
import stan.androiddemo.project.petal.Event.OnDialogInteractionListener
import stan.androiddemo.tool.Logger

/**
 * Created by stanhu on 16/8/2017.
 */
class BoardAddDialogFragment:BaseDialogFragment(){

    lateinit var mEditTextBoardName: EditText
    lateinit var mEditTextBoardDescribe: EditText
    lateinit var mSpinnerBoardTitle: Spinner

    private var mContext: Context? = null

    private var mStringBoardName: String? = null

    private var mStringBoardType: String? = null

    private var titleList: Array<String>? = null

    private var isChange = false//输入值是否有变化 默认false

    var mLintener : OnDialogInteractionListener? = null

    override fun getTAGInfo(): String {
      return this.toString()
    }

    companion object {
        fun create():BoardAddDialogFragment{
            val fragment = BoardAddDialogFragment()
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(resources.getString(R.string.dialog_title_add))
        val factory = LayoutInflater.from(mContext)
        val dialogView = factory.inflate(R.layout.petal_dialog_board_edit,null)
        mEditTextBoardName = dialogView.findViewById(R.id.edit_board_name)
        mEditTextBoardDescribe = dialogView.findViewById(R.id.edit_board_describe)
        mSpinnerBoardTitle = dialogView.findViewById(R.id.spinner_title)

        setData()

        builder.setView(dialogView)
        builder.setNegativeButton(resources.getString(R.string.dialog_negative),null)
        builder.setPositiveButton(resources.getString(R.string.dialog_title_add),DialogInterface.OnClickListener { dialogInterface, i ->
            if (dataChange()){
                var boardName = mEditTextBoardName.text.toString().trim()
                if (boardName.isNullOrEmpty()){
                    boardName = mEditTextBoardName.hint.toString()
                }
                mLintener?.onDialogClick(true, hashMapOf("boardname" to boardName, "describe" to mEditTextBoardDescribe.text.toString(),
                        "selectType" to mStringBoardType!!))
            }
        })
        return super.onCreateDialog(savedInstanceState)
    }

    fun setData(){
        mEditTextBoardName.hint = resources.getString(R.string.text_is_default)
        mStringBoardName = mEditTextBoardName.hint.toString()
        titleList = resources.getStringArray(R.array.title_array_all)
        val typeList = resources.getStringArray(R.array.type_array_all)
        val selctionPosition = 0
        val adapter = ArrayAdapter<String>(mContext,R.layout.support_simple_spinner_dropdown_item,titleList)
        mSpinnerBoardTitle.adapter = adapter
        mSpinnerBoardTitle.setSelection(selctionPosition)

        mSpinnerBoardTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Logger.d("position=" + position)
                //选中监听事件 产生变化 赋值
                val selected = typeList[position]
                if (selected != mStringBoardType) {
                    mStringBoardType = typeList[position]
                    isChange = true//有选择 就表示数据发生变化
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

    fun dataChange():Boolean{
        var changed = this.isChange
        if (changed){
            return true
        }
        val input = mEditTextBoardName.text.toString().trim()
        if (!input.isNullOrEmpty() && input == mStringBoardName){
            return true
        }
        return false
    }
}