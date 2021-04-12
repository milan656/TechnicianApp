package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class AutoSuggestProductAdapter(
    context: Context,
    resource: Int,
    skuList: ArrayList<String>
) : ArrayAdapter<String>(context, resource, skuList) {

    val mListData = skuList
    override fun getCount(): Int {
        return mListData.size
    }

    @Nullable
    override fun getItem(position: Int): String? {
        return mListData[position]
    }

    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    fun getObject(position: Int): String {
        return mListData[position]
    }

    @NonNull
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.e("getchar",""+constraint?.toString())
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mListData
                    filterResults.count = mListData.size
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                if(constraint != null){
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }

            }
        }
    }


}
