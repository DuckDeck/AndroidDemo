package stan.androiddemo.project.Mito

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageCatInfo
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.project.Mito.Model.Resolution


/**
 * Created by stanhu on 25/8/2017.
 */
class FilterMitoDialogFragment: AppCompatDialogFragment(){

    lateinit var imgCatInfo:ImageCatInfo
    lateinit var mContext: Context
    lateinit var cyResolution: RecyclerView
    lateinit var mAdapter: BaseQuickAdapter<Resolution, BaseViewHolder>
    var resolutionBLock:((resulution: Resolution)->Unit)? = null
    companion object {
        private val KEYIMAGECAT = "keyAuthorization"
        fun create(resolutionArray:ImageCatInfo): FilterMitoDialogFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEYIMAGECAT,resolutionArray)

            val fragment = FilterMitoDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgCatInfo = arguments.getParcelable(KEYIMAGECAT)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("选择分辨率")
        val inflate = LayoutInflater.from(mContext)
        val dialogView = inflate.inflate(R.layout.dialog_resolution_filter,null)
        initView(dialogView)
        builder.setView(dialogView)
        return builder.create()
    }

    fun  initView(view: View){
        cyResolution = view.findViewById(R.id.recyclerViewResolution)
        mAdapter = object:BaseQuickAdapter<Resolution, BaseViewHolder>(R.layout.md_listitem,imgCatInfo.resulotions){
            override fun convert(helper: BaseViewHolder?, item: Resolution?) {
                var str = item.toString()
                if (!item!!.device.isNullOrEmpty()){
                    str = str + "(" + item!!.device + ")"
                }
                helper!!.setText(R.id.md_title,str)
            }
        }
        cyResolution.layoutManager = LinearLayoutManager(mContext)
        cyResolution.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            if (resolutionBLock != null){
                resolutionBLock!!(imgCatInfo.resulotions[position])
            }
        }
    }



}