package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.technician.common.PrefManager
import com.walkins.technician.R
import com.walkins.technician.adapter.AutoSuggestProductAdapter
import com.walkins.technician.model.login.makemodel.VehicleMakeData
import com.walkins.technician.model.login.makemodel.VehicleModelData
import com.walkins.technician.viewmodel.MakeModelViewModel

class ReportFilterActivity : AppCompatActivity(), View.OnClickListener {
    private var ivBack: ImageView? = null
    private var btnConfirm: Button? = null
    private var btnCancel: Button? = null

    private val listClicked = ArrayList<String>()
    private val listClickedModel = ArrayList<String>()
    private var adapter: AutoSuggestProductAdapter? = null

    private lateinit var prefManager: PrefManager
    private lateinit var makeModelViewModel: MakeModelViewModel
    private var actvehicleMake: AutoCompleteTextView? = null
    private var actvehicleModel: AutoCompleteTextView? = null
    private var actvehicleSociety: AutoCompleteTextView? = null

    private var makeSearchdata: ArrayList<VehicleMakeData>? = ArrayList()
    private var modelSearchdata: ArrayList<VehicleModelData>? = ArrayList()
    private var selectedMakeId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_filter)
        prefManager = this.let { PrefManager(it) }
        makeModelViewModel = ViewModelProviders.of(this).get(MakeModelViewModel::class.java)
        init()
    }

    private fun init() {
        ivBack = findViewById(R.id.ivBack)
        btnConfirm = findViewById<Button>(R.id.btnConfirm)
        btnCancel = findViewById<Button>(R.id.btnCancel)

        actvehicleMake = findViewById(R.id.actvehicleMake)
        actvehicleModel = findViewById(R.id.actvehicleModel)
        actvehicleSociety = findViewById(R.id.actvehicleSociety)


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

                    selectedMakeId = makeSearchdata?.get(p2)?.id!!
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

        ivBack?.setOnClickListener(this)
        btnCancel?.setOnClickListener(this)
        btnConfirm?.setOnClickListener(this)
    }

    private fun searchModel(toString: String) {
        this.let {
            makeModelViewModel.getVehicleModel(
                it,
                selectedMakeId,
                prefManager.getAccessToken()!!
            )
        }

        makeModelViewModel.getVehicleModelList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {
                    modelSearchdata?.clear()
                    modelSearchdata?.addAll(it.data)
                    try {
                        Log.e("getmodelata",""+modelSearchdata?.size)
                        modelDataForSearchApi(modelSearchdata!!)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
            } else {

            }
        })
    }

    private fun searchMake(toString: String) {
        makeModelViewModel.getVehicleMake(this, prefManager.getAccessToken()!!)

        makeModelViewModel.getVehicleMakeList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {

                    makeSearchdata?.clear()
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
                    makeSearchdata.get(index).name/* + " --> " + makeSearchdata.get(index).id*/
                listClicked.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("listClicked", "" + listClicked)
        if (listClicked.size > 0) {
            adapter =
                this?.let {
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
                this?.let {
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

    private fun modelDataForSearchApi(modelSearchData: ArrayList<VehicleModelData>) {

        listClickedModel.clear()
        try {
            for ((index, value) in modelSearchData.withIndex()) {
                val string =
                    modelSearchData.get(index).name /*+ " --> " + modelSearchData.get(index).id*/
                listClickedModel.add(string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("listClickedModel", "" + listClickedModel)
        if (listClickedModel.size > 0) {
            adapter =
                this?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        listClickedModel
                    )
                }
            actvehicleModel?.threshold = 1
            actvehicleModel?.setAdapter<ArrayAdapter<String>>(adapter)
        } else {
            var noValueList: ArrayList<String> = ArrayList()
            noValueList.add("No any dealer found")
            adapter =
                this?.let {
                    AutoSuggestProductAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        noValueList
                    )
                }
            actvehicleModel?.threshold = 1
            actvehicleModel?.setAdapter<ArrayAdapter<String>>(adapter)
        }
    }

    override fun onClick(v: View?) {
        var id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnCancel -> {
                var intent = Intent()
                intent.putExtra("action", "reset")
                setResult(100, intent)
                finish()
            }
            R.id.btnConfirm -> {
                var intent = Intent()
                intent.putExtra("action", "confirm")
                setResult(100, intent)
                finish()
            }
        }
    }
}