package com.walkins.technician.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.common.RecyclerViewType
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.SectionModel

class SectionRecyclerViewAdapter(
    context: Context,
    recyclerViewType: RecyclerViewType,
    sectionModelArrayList: ArrayList<SectionModel>,
    onPositionClick: onClickAdapter


) :
    RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder>(), onClickAdapter {
    public var onclick: onClickAdapter? = null

    inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView
        val itemRecyclerView: RecyclerView

        init {
            tvDate = itemView.findViewById(R.id.tvDate)
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view)
        }
    }

    private val context: Context
    private val recyclerViewType: RecyclerViewType
    private val sectionModelArrayList: ArrayList<SectionModel>
    private var sectionModelMain: SectionModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.section_custom_row_layout, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val sectionModel = sectionModelArrayList[position]
        sectionModelMain = sectionModel
        holder.tvDate.text = sectionModel.sectionLabel

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true)
        holder.itemRecyclerView.isNestedScrollingEnabled = false
        when (recyclerViewType) {
            RecyclerViewType.LINEAR_VERTICAL -> {
                val linearLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                holder.itemRecyclerView.layoutManager = linearLayoutManager
            }
            RecyclerViewType.LINEAR_HORIZONTAL -> {
                val linearLayoutManager1 =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                holder.itemRecyclerView.layoutManager = linearLayoutManager1
            }
            RecyclerViewType.GRID -> {
                val gridLayoutManager = GridLayoutManager(context, 3)
                holder.itemRecyclerView.layoutManager = gridLayoutManager
            }
        }
//        val adapter = sectionModel.itemArrayList?.let { ItemRecyclerViewAdapter(context, it) }
        var adapter = ItemRecyclerViewAdapter(context, sectionModel.itemArrayList, this)
        holder.itemRecyclerView.adapter = adapter
        adapter?.onclick = this


    }

    override fun getItemCount(): Int {
        return sectionModelArrayList.size
    }

    init {
        this.context = context
        this.recyclerViewType = recyclerViewType
        this.sectionModelArrayList = sectionModelArrayList
    }

    override fun onPositionClick(variable: Int, check: Int) {
        Log.e("getclick", "" + sectionModelMain?.itemArrayList?.get(variable))
        Log.e("getclick", "" + sectionModelMain?.sectionLabel)

        if (onclick != null) {
            onclick?.onPositionClick(variable, check)
        }
    }
}