package com.walkins.aapkedoorstep.adapter

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
                Log.e("getchar",""+filterResults.count)
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.e("getchar",""+constraint?.toString())
                if(constraint != null){
                    Log.e("getchar",""+results)
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
