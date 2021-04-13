package com.walkins.technician.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.adapter.LeadHistoryAdapter
import com.walkins.technician.adapter.SectionRecyclerViewAdapter
import com.walkins.technician.common.RecyclerViewType
import com.walkins.technician.model.login.LeadHistoryData
import com.walkins.technician.model.login.SectionModel


class ListHeaderActivity : AppCompatActivity() {
    var listwithHeader: RecyclerView? = null
    var mAdapter: LeadHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private val recyclerViewType: RecyclerViewType? = null
    var historyDataList: ArrayList<LeadHistoryData> = ArrayList<LeadHistoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_header)

        listwithHeader = findViewById(R.id.sectioned_recycler_view)

        listwithHeader?.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        listwithHeader?.setLayoutManager(linearLayoutManager)

        populateRecyclerView()
//        getList()
    }

    private fun populateRecyclerView() {
        val sectionModelArrayList: ArrayList<SectionModel> = ArrayList()
        //for loop for sections
        for (i in 1..5) {
            val itemArrayList: ArrayList<String> = ArrayList()
            //for loop for items
            for (j in 1..10) {
                itemArrayList.add("Item $j")
            }

            //add the section and items to array list
            sectionModelArrayList.add(SectionModel("section $i", itemArrayList))
        }
//        val adapter = SectionRecyclerViewAdapter(this, RecyclerViewType.LINEAR_VERTICAL, sectionModelArrayList,this)
//        listwithHeader?.setAdapter(adapter)
    }

    private fun getList() {
        for (i in 0..5) {
            var model = LeadHistoryData()
            model.business = "business"
            model.createdAt = "1346524199000".toLong()
            model.name = "name"
            model.phoneNumber = "9845632145"
            model.updatedAt = "1346524199000".toLong()
            historyDataList.add(model)

        }
        mAdapter?.notifyDataSetChanged()
    }
}