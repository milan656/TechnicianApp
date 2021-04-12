package com.walkins.technician.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.activity.CompletedServiceDetailActivity
import com.walkins.technician.activity.SkippedServiceDetailActivity
import com.walkins.technician.adapter.AutoSuggestProductAdapter
import com.walkins.technician.adapter.ReportAdpater
import com.walkins.technician.adapter.ReportSkippAdpater
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.model.login.makemodel.VehicleMakeData
import com.walkins.technician.model.login.makemodel.VehicleModelData
import com.walkins.technician.viewmodel.MakeModelViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivFilterImg: ImageView? = null
    private var reportRecycView: RecyclerView? = null
    private val listClicked = ArrayList<String>()
    private var adapter: AutoSuggestProductAdapter? = null

    private lateinit var prefManager: PrefManager
    private lateinit var makeModelViewModel: MakeModelViewModel

    private var llCompleted: LinearLayout? = null
    private var tvSkipped: TextView? = null
    private var tvCompleted: TextView? = null
    private var llSkipped: LinearLayout? = null
    private var skipSelected = false

    private var actvehicleMake: AutoCompleteTextView? = null
    private var actvehicleModel: AutoCompleteTextView? = null
    private var actvehicleSociety: AutoCompleteTextView? = null

    private var makeSearchdata: ArrayList<VehicleMakeData>? = ArrayList()
    private var modelSearchdata: ArrayList<VehicleModelData>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        prefManager = context?.let { PrefManager(it) }!!
        makeModelViewModel = ViewModelProviders.of(this).get(MakeModelViewModel::class.java)
        init(view)
        return view
    }

    private fun init(view: View?) {
        llSkipped = view?.findViewById(R.id.llSkippedReport)
        llCompleted = view?.findViewById(R.id.llCompletedReport)
        tvSkipped = view?.findViewById(R.id.tvSkipped)
        tvCompleted = view?.findViewById(R.id.tvCompleted)



        reportRecycView = view?.findViewById(R.id.reportRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        ivBack = view?.findViewById(R.id.ivBack)
        ivFilterImg = view?.findViewById(R.id.ivFilterImg)
        ivFilterImg?.setOnClickListener(this)

        ivBack?.setOnClickListener(this)
        llCompleted?.setOnClickListener(this)
        llSkipped?.setOnClickListener(this)

        tvTitle?.text = "Your Report"

        ivBack?.visibility = View.GONE

        setadapter(skipSelected)
    }

    private fun setadapter(skipSelected: Boolean) {
        if (!skipSelected) {
            val arrayAdapter = context?.let { ReportAdpater(Common.commonPhotoChooseArr, it, this) }
            reportRecycView?.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            /* reportRecycView?.addItemDecoration(
                 DividerItemDecoration(
                     this,
                     DividerItemDecoration.VERTICAL
                 )
             )*/
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
        } else {
            val arrayAdapter =
                context?.let { ReportSkippAdpater(Common.commonPhotoChooseArr, it, this) }
            reportRecycView?.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            /* reportRecycView?.addItemDecoration(
                 DividerItemDecoration(
                     this,
                     DividerItemDecoration.VERTICAL
                 )
             )*/
            reportRecycView?.adapter = arrayAdapter
            arrayAdapter?.onclick = this
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (skipSelected) {
            var intent = Intent(context, SkippedServiceDetailActivity::class.java)
            startActivity(intent)

        } else {
            var intent = Intent(context, CompletedServiceDetailActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
//                onBackPressed()
            }
            R.id.ivFilterImg -> {
                openReportFilterDialogue("Choose Filter")
            }
            R.id.llCompletedReport -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.white))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.text_color1))
                skipSelected = false

                setadapter(skipSelected)
            }
            R.id.llSkippedReport -> {
                llCompleted?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_white_layout))
                llSkipped?.setBackgroundDrawable(this.resources?.getDrawable(R.drawable.rounded_red_layout))

                tvCompleted?.setTextColor(this.resources.getColor(R.color.text_color1))
                tvSkipped?.setTextColor(this.resources.getColor(R.color.white))
                skipSelected = true
                setadapter(skipSelected)

            }

        }
    }

    private fun openReportFilterDialogue(titleStr: String) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_report_filter, null)
        val dialog =
            context.let {
                it?.let { it1 ->
                    BottomSheetDialog(
                        it1,
                        R.style.CustomBottomSheetDialogTheme
                    )
                }
            }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
//        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        actvehicleMake = view?.findViewById(R.id.actvehicleMake)
        actvehicleModel = view?.findViewById(R.id.actvehicleModel)
        actvehicleSociety = view?.findViewById(R.id.actvehicleSociety)


        actvehicleMake!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.toString()?.length!! > 0) {

                    searchMake(actvehicleMake?.text.toString())
                } else {

                }
            }

        })

        actvehicleMake!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        actvehicleModel!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.toString()?.length!! > 0) {

                    searchModel(actvehicleModel?.text.toString())
                } else {

                }
            }

        })

        actvehicleModel!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        actvehicleSociety!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.toString()?.length!! > 0) {

                    searchSociety(actvehicleSociety?.text.toString())
                } else {

                }
            }

        })

        actvehicleSociety!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        tvTitleText?.text = titleStr

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog?.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun searchModel(toString: String) {

    }

    private fun searchMake(toString: String) {
        context?.let { makeModelViewModel?.getVehicleMake(it) }

        makeModelViewModel?.getVehicleMakeList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {

                    makeSearchdata?.addAll(it.data)
                    try {
                        makeDataForSearchApi(makeSearchdata!!)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
            } else {

            }
        })
    }

    private fun searchSociety(toString: String) {

    }

    private fun makeDataForSearchApi(makeSearchdata: ArrayList<VehicleMakeData>) {

        listClicked.clear()
        try {
            for ((index, value) in makeSearchdata.withIndex()) {
                val string =
                    makeSearchdata.get(index).name + " --> " + makeSearchdata.get(index).id
                listClicked.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("listClicked", "" + listClicked)
        if (listClicked.size > 0) {
            adapter =
                context?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        listClicked
                    )
                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        } else {
            var noValueList: ArrayList<String> = ArrayList()
            noValueList.add("No any dealer found")
            adapter =
                context?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        noValueList
                    )
                }
            actvehicleMake?.threshold = 1
            actvehicleMake?.setAdapter<ArrayAdapter<String>>(adapter)
        }
    }

}