package licola.demo.com.huabandemo.Module.Search

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter


class SearcHHintAdapter(context: Context, resource: Int, private var mObjects: List<String>?) : ArrayAdapter<String>(context, resource, mObjects) {

    private var mFilter: Filter? = null

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = HintFilter()
        }
        return mFilter!!
    }

    /**
     *
     * An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.
     * 重写过滤类 自定义一个不会过滤任何数的Filter
     */
    private inner class HintFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): Filter.FilterResults {
            //                Logger.d(s);
            val suggestions = mObjects!!.toList()

            val filterResults = Filter.FilterResults()
            filterResults.values = suggestions
            filterResults.count = suggestions.size
            //            Logger.d("filterResults.count=" + filterResults.count);
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {

            mObjects = results.values as List<String>
            //            Logger.d("results.count=" + results.count);
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }

    companion object {
        private val TAG = "SearHintAdapter"
    }
}
