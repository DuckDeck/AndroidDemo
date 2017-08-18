package stan.androiddemo.project.petal.Widget

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BaseDialogFragment
import stan.androiddemo.project.petal.Event.OnEditDialogInteractionListener
import stan.androiddemo.tool.Logger

/**
 * Created by stanhu on 18/8/2017.
 */
class BoardEditDialogFragment: BaseDialogFragment() {



    //UI
    lateinit var mEditTextBoardName: EditText
    lateinit var mEditTextBoardDescribe: EditText
    lateinit var mSpinnerBoardTitle: Spinner


    private var mContext: Context? = null

    //外部传入的值
    private var mStringBoardId: String? = null
    private var mStringBoardName: String? = null
    private var mStringDescribe: String? = null
    private var mStringBoardType: String? = null

    private var isChange = false//输入值是否有变化 默认false

    internal var mListener: OnEditDialogInteractionListener? = null

    override fun getTAGInfo(): String {
        return this.toString()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    companion object {
        //bundle key
        private val KEYBOARDID = "keyBoardId"
        private val KEYBOARDNAME = "keyBoardName"
        private val KEYDESCRIBE = "keyDescribe"
        private val KEYBOARDTYPE = "keyBoardType"
        fun create(boardId:String,name:String,describe:String,boardTitle:String):BoardEditDialogFragment{
            val bundle = Bundle()
            bundle.putString(KEYBOARDID, boardId)
            bundle.putString(KEYBOARDNAME, name)
            bundle.putString(KEYDESCRIBE, describe)
            bundle.putString(KEYBOARDTYPE, boardTitle)
            val fragment = BoardEditDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null){
            mStringBoardId = args.getString(KEYBOARDID)
            mStringBoardName = args.getString(KEYBOARDNAME)
            mStringBoardType = args.getString(KEYBOARDTYPE)
            mStringDescribe = args.getString(KEYDESCRIBE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(resources.getString(R.string.dialog_title_edit))
        val inflate = LayoutInflater.from(mContext)
        val dialogView = inflate.inflate(R.layout.petal_dialog_board_edit,null)

        initView(dialogView)
        setData()
        builder.setView(dialogView)

        builder.setNegativeButton(resources.getString(R.string.dialog_negative),null)
        builder.setNeutralButton(resources.getString(R.string.dialog_delete_positive)) { p0, p1 -> mListener?.onDialogNeutralClick(mStringBoardId!!,mStringBoardName!!) }
        builder.setPositiveButton(R.string.dialog_edit_positive) { dialog, which ->
            Logger.d()
            //如果检测到值有变化才有回调
            if (DataChange()) {
                mListener?.onDialogPositiveClick(mStringBoardId!!,
                        mEditTextBoardName.text.toString(), mEditTextBoardDescribe.text.toString(), mStringBoardType!!)
            }
        }
        return builder.create()

    }

    private fun DataChange(): Boolean {
        //Spinner 控件会影响 所以先取内部变量
        val isChange = this.isChange
        if (isChange) {
            return true
        }
        //临时变量
        var input: String
        input = mEditTextBoardName.text.toString().trim { it <= ' ' }//取名称输入框值
        //判断 不为空 并且值有变化
        if (!TextUtils.isEmpty(input) && input != mStringBoardName) {
            return true
        }

        input = mEditTextBoardDescribe.text.toString().trim { it <= ' ' }//取描述输入框值
        if (!TextUtils.isEmpty(input) && input != mStringDescribe) {
            return true
        }

        return false
    }

    private fun initView(dialogView: View) {
        mEditTextBoardName = dialogView.findViewById(R.id.edit_board_name)
        mEditTextBoardDescribe = dialogView.findViewById(R.id.edit_board_describe)
        mSpinnerBoardTitle = dialogView.findViewById(R.id.spinner_title)

    }

    private fun setData() {
        //画板名称
        if (!TextUtils.isEmpty(mStringBoardName)) {
            mEditTextBoardName.setText(mStringBoardName)
        } else {
            mEditTextBoardName.setText(R.string.text_is_default)
        }

        //画板描述 可以为空
        mEditTextBoardDescribe.setText(mStringDescribe)

        //画板类型 定义在本地资源中
        val titleList = resources.getStringArray(R.array.title_array_all)
        val typeList = resources.getStringArray(R.array.type_array_all)
        var selectPosition = 0//默认选中第一项

        if (mStringBoardType != null) {
            //遍历查找
            var i = 0
            val size = titleList.size
            while (i < size) {
                if (typeList[i] == mStringBoardType) {
                    selectPosition = i
                }
                i++
            }
        }

        val adapter = ArrayAdapter(mContext, R.layout.support_simple_spinner_dropdown_item, titleList)
        mSpinnerBoardTitle.adapter = adapter
        mSpinnerBoardTitle.setSelection(selectPosition)
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

}