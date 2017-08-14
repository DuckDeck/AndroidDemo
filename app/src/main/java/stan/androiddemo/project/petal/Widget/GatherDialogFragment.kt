package stan.androiddemo.project.petal.Widget

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.project.petal.API.OperateAPI
import stan.androiddemo.project.petal.Base.BaseDialogFragment
import stan.androiddemo.project.petal.Event.OnDialogInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Module.ImageDetail.GatherInfoBean

/**
 * Created by stanhu on 14/8/2017.
 */
class GatherDialogFragment: BaseDialogFragment() {



    lateinit var mEditTextDescribe: EditText
    lateinit var mTVGatherWarning: TextView
    lateinit var mSpinnerBoardTitle: Spinner
    lateinit var mContext: Context
    lateinit var mViaId: String
    lateinit var mDescribeText: String
    lateinit var mBoardTitleArray: Array<String>
    private var mSelectPosition = 0//默认的选中项

    var mListener:OnDialogInteractionListener? = null

    override fun getTAGInfo(): String {
        return this.toString()
    }

    companion object {
        private val KEYAUTHORIZATION = "keyAuthorization"
        private val KEYVIAID = "keyViaId"
        private val KEYDESCRIBE = "keyDescribe"
        private val KEYBOARDTITLEARRAY = "keyBoardTitleArray"
        fun create(authorization:String,viaId:String,describe:String,boardTitleArray:ArrayList<String>):GatherDialogFragment{
            val bundle = Bundle()
            bundle.putString(KEYAUTHORIZATION,authorization)
            bundle.putString(KEYVIAID,viaId)
            bundle.putString(KEYDESCRIBE,describe)
            bundle.putStringArrayList(KEYBOARDTITLEARRAY,boardTitleArray)
            val fragment = GatherDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        if (context is OnDialogInteractionListener){
            mListener = context
        }
        else{
            throwRuntimeException(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthorization = arguments.getString(KEYAUTHORIZATION)
        mViaId = arguments.getString(KEYVIAID)
        mDescribeText = arguments.getString(KEYDESCRIBE)
        mBoardTitleArray = arguments.getStringArray(KEYBOARDTITLEARRAY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(resources.getString(R.string.dialog_title_gather))
        val inflate = LayoutInflater.from(mContext)
        val dialogView = inflate.inflate(R.layout.petal_dialog_gather,null)

        initView(dialogView)

        builder.setView(dialogView)

        builder.setNegativeButton(resources.getString(R.string.dialog_negative),null)
        builder.setPositiveButton(resources.getString(R.string.dialog_gather_positive),DialogInterface.OnClickListener { dialogInterface, i ->
            var input = mEditTextDescribe.text.toString().trim()
            if (input.isNullOrEmpty()){
                input = mEditTextDescribe.hint.toString()
            }
            mListener?.onDialogClick(true, hashMapOf("describe" to input,"position" to mSelectPosition))
        })
        addSubscription(getGatherInfo())
        return builder.create()
    }

    fun  initView(view: View){
        mEditTextDescribe = view.findViewById(R.id.edit_describe)
        mTVGatherWarning = view.findViewById(R.id.tv_gather_warning)
        mSpinnerBoardTitle = view.findViewById(R.id.spinner_title)
        val adapter = ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,mBoardTitleArray)
        if (!mDescribeText.isNullOrEmpty()){
            mEditTextDescribe.hint = mDescribeText
        }
        else{
            mEditTextDescribe.hint = resources.getString(R.string.text_image_describe_null)
        }
        mSpinnerBoardTitle.adapter = adapter
        mSpinnerBoardTitle.setOnItemClickListener { adapterView, view, i, l ->
            mSelectPosition = i
        }
    }

    fun getGatherInfo():Subscription{
        return RetrofitClient.createService(OperateAPI::class.java).httpsGatherInfo(mAuthorization,mViaId,true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<GatherInfoBean>(){
                    override fun onNext(t: GatherInfoBean?) {
                        if (t?.exist_pin != null){
                            val format = resources.getString(R.string.text_gather_warning)
                            mTVGatherWarning.visibility = View.VISIBLE
                            mTVGatherWarning.text = String.format(format,t!!.exist_pin!!.board?.title)
                        }
                    }

                    override fun onError(e: Throwable?) {
                    }

                    override fun onCompleted() {
                    }

                })
    }
}