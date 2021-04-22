package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.DB.VehicleMakeModelClass
import com.walkins.technician.DB.VehiclePatternModelClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleMakeAdapterNew
import com.walkins.technician.adapter.VehiclePatternAdapter
import com.walkins.technician.common.*
import com.walkins.technician.model.login.patternmodel.PatternData
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.networkApi.WarrantyApi
import com.walkins.technician.viewmodel.WarrantyViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class VehiclePatternActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private var patternModel: PatternModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehiclePatternAdapter? = null
    private var gridviewRecycModel: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    var arrList: ArrayList<VehiclePatternModelClass>? = ArrayList()
    private lateinit var mDb: DBClass
    private var btnNext: Button? = null
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var tvSelectedModel: TextView? = null
    private var ivEditVehicleMake: ImageView? = null
    private var selectedPosition = -1
    private var chkRR: CheckBox? = null
    private var chkRF: CheckBox? = null
    private var chkLR: CheckBox? = null
    private var selectedPos = -1
    private var selectedTyre = ""
    private var selectedId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_model)
        prefManager = PrefManager(this)
        warrantyViewModel = ViewModelProviders.of(this).get(WarrantyViewModel::class.java)
        mDb = DBClass.getInstance(applicationContext)
        init()
    }

    private fun init() {
        gridviewRecycModel = findViewById(R.id.gridviewRecycModel)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        btnNext = findViewById(R.id.btnNext)
        chkRR = findViewById(R.id.chkRR)
        chkRF = findViewById(R.id.chkRF)
        chkLR = findViewById(R.id.chkLR)

        llVehicleMakeselectedView = findViewById(R.id.llVehicleMakeselectedView)
        tvSelectedModel = findViewById(R.id.tvSelectedModel)
        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)

        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        ivEditVehicleMake?.setOnClickListener(this)

//        Common.setClearAllValues()

        if (intent != null) {
            if (intent.hasExtra("selectedTyre")) {

                tvTitle?.text =
                    "Select Tyre Pattern - " + intent.getStringExtra("selectedTyre")
                selectedTyre = intent.getStringExtra("selectedTyre")!!
            }
        }


        gridviewRecycModel?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
//        gridviewRecycModel?.addItemDecoration(
//            SpacesItemDecoration(
//                20
//            )
//        )


        if (selectedTyre.equals("LF")) {
            chkRF?.text = "RF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"
        } else if (selectedTyre.equals("RF")) {
            chkRF?.text = "LF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"
        } else if (selectedTyre.equals("LR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "RR"
        } else if (selectedTyre.equals("RR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "LR"
        }
        Common.showLoader(this)

        var thread = Thread {

            Log.e("getsizee", "" + mDb.patternDaoClass().getAllPattern().size)
            if (mDb.patternDaoClass().getAllPattern() != null && mDb.patternDaoClass()
                    .getAllPattern().size > 0
            ) {
                arrList?.addAll(mDb.patternDaoClass().getAllPattern())

                for (i in arrList?.indices!!) {

                    Log.e("getpatter", "" + arrList?.get(i)?.patternId)
                    Log.e("getpatter", "" + arrList?.get(i)?.name)
                }

                Log.e("getpatter", "" + selectedTyre)
                if (selectedTyre.equals("LF")) {
                    if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
                    ) {
                        var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                        try {
                            var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                            selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                            Log.e("getpatterlf", "" + json + " " + selectedId)
                            setData(json)
                            runOnUiThread {

                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Pattern)
                                        != null && json.get(TyreKey.chk1Pattern)?.asString
                                            .equals("RF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Pattern)
                                        != null && json.get(TyreKey.chk2Pattern)?.asString
                                            .equals("LR,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Pattern)
                                        != null && json.get(TyreKey.chk3Pattern)?.asString
                                            .equals("RR,true")
                                    ) true else false

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                if (selectedTyre.equals("LR")) {
                    if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
                    ) {
                        var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                        try {
                            var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                            selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                            Log.e("getpatterlr", "" + json + " " + selectedId)
                            setData(json)
                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                            TyreKey.chk1Pattern
                                        )?.asString.equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                            TyreKey.chk2Pattern
                                        )?.asString.equals("RF,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                            TyreKey.chk3Pattern
                                        )?.asString.equals("RR,true")
                                    ) true else false
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                if (selectedTyre.equals("RF")) {
                    if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
                    ) {
                        var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                        try {
                            var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                            selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                            Log.e("getpatterrf", "" + json + " " + selectedId)
                            setData(json)
                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                            TyreKey.chk1Pattern
                                        )?.asString.equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                            TyreKey.chk2Pattern
                                        )?.asString.equals("LR,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                            TyreKey.chk3Pattern
                                        )?.asString.equals("RR,true")
                                    ) true else false

                                Log.e(
                                    "getval0",
                                    "" + chkRF?.isChecked + " " + chkLR?.isChecked + " " + chkRR?.isChecked
                                )
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                if (selectedTyre.equals("RR")) {
                    if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
                    ) {
                        var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                        try {
                            var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                            selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                            Log.e("getpatterrr", "" + json + " " + selectedId)
                            setData(json)
                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                            TyreKey.chk1Pattern
                                        )?.asString.equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                            TyreKey.chk2Pattern
                                        ).asString.equals("RF,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                            TyreKey.chk3Pattern
                                        ).asString.equals("LR,true")
                                    ) true else false

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }

//                =======================================================================

                if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                    !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        Log.e(
                            "getobjlf",
                            "" + json.get("vehiclePatternId")?.asString + " " + selectedId
                        )
                        if (selectedId == json.get("vehiclePatternId")?.asString?.toInt()) {
                            Log.e(
                                "getobjlf",
                                "" + selectedId + " " + json.get("vehiclePatternId")?.asString
                            )
                            Log.e("getobjlf", "" + json + " ")
                            runOnUiThread {

                                if (chkRF?.text?.toString().equals("LF")) {
                                    chkRF?.isChecked = true
                                }
                                if (chkLR?.text?.toString().equals("LF")) {
                                    chkLR?.isChecked = true
                                }
                                if (chkRR?.text?.toString().equals("LF")) {
                                    chkRR?.isChecked = true
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                    !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        Log.e("getobjlr", "" + json.get("vehiclePatternId")?.asString + " ")
                        if (selectedId == json.get("vehiclePatternId")?.asString?.toInt()) {
                            Log.e("getobjlr", "" + selectedId + " ")
                            Log.e("getobjlr", "" + json + " ")
                            runOnUiThread {

                                if (chkRF?.text?.toString().equals("LR")) {
                                    chkRF?.isChecked = true
                                }
                                if (chkLR?.text?.toString().equals("LR")) {
                                    chkLR?.isChecked = true
                                }
                                if (chkRR?.text?.toString().equals("LR")) {
                                    chkRR?.isChecked = true
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                    !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        Log.e("getobjrfpa", "" + json.get("vehiclePatternId")?.asString + " ")
                        if (selectedId == json.get("vehiclePatternId")?.asString?.toInt()) {
                            Log.e("getobjrfpa", "" + selectedId + " ")
                            runOnUiThread {

                                if (chkRF?.text?.toString().equals("RF")) {
                                    chkRF?.isChecked = true
                                }
                                if (chkLR?.text?.toString().equals("RF")) {
                                    chkLR?.isChecked = true
                                }
                                if (chkRR?.text?.toString().equals("RF")) {
                                    chkRR?.isChecked = true
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                    !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        Log.e("getobjrr", "" + json.get("vehiclePatternId")?.asString + " ")
                        if (selectedId == json.get("vehiclePatternId")?.asString?.toInt()) {
                            Log.e("getobjrr", "" + selectedId + " ")
                            Log.e("getobjrr", "" + json + " ")
                            runOnUiThread {

                                if (chkRF?.text?.toString().equals("RR")) {
                                    chkRF?.isChecked = true
                                }
                                if (chkLR?.text?.toString().equals("RR")) {
                                    chkLR?.isChecked = true
                                }
                                if (chkRR?.text?.toString().equals("RR")) {
                                    chkRR?.isChecked = true
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }


            }

        }
        thread.start()

        val handler = Handler()
        handler.postDelayed(Runnable {
            Log.e("getid", "" + selectedId)
            adapter = VehiclePatternAdapter(this, arrList, this, selectedId)
            gridviewRecycModel?.adapter = adapter
            gridviewRecycModel?.visibility = View.VISIBLE
            Common.hideLoader()
        }, 1000)

        Log.e("getvaluess_all", TyreDetailCommonClass.tyreType!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMake!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleMakeId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehiclePattern!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehiclePatternId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleSize!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.vehicleSizeId!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.manufaturingDate!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.psiInTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.psiOutTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.weightTyreService!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.sidewell!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.shoulder!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.treadDepth!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.treadWear!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.rimDamage!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.bubble!!)
        Log.e("getvaluess_all", "" + TyreDetailCommonClass.issueResolvedArr!!)
        Log.e("getvaluess_all", TyreDetailCommonClass.visualDetailPhotoUrl!!)

        Log.e("getvaluess_all--1 make", TyreDetailCommonClass.chk1Make!!)
        Log.e("getvaluess_all--2 make", TyreDetailCommonClass.chk2Make!!)
        Log.e("getvaluess_all--3 make", TyreDetailCommonClass.chk3Make!!)
        Log.e("getvaluess_all--1 patte", TyreDetailCommonClass.chk1Pattern!!)
        Log.e("getvaluess_all--2 patte", TyreDetailCommonClass.chk2Pattern!!)
        Log.e("getvaluess_all--3 patte", TyreDetailCommonClass.chk3Pattern!!)
        Log.e("getvaluess_all--1 size", TyreDetailCommonClass.chk1Size!!)
        Log.e("getvaluess_all--2 size", TyreDetailCommonClass.chk2Size!!)
        Log.e("getvaluess_all--3 size", TyreDetailCommonClass.chk3Size!!)


    }


    override fun onPositionClick(variable: Int, check: Int) {

//        Log.e("getmake", "" + arrList?.get(variable)?.name)
//        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
//        intent.putExtra("which", "vehiclepattern")
//        startActivityForResult(intent, 1006)

        Common.slideUp(gridviewRecycModel!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        tvSelectedModel?.text = arrList?.get(variable)?.name
        selectedPosition = variable
        selectedPos = variable

        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    Log.e("getpatterlf", "" + json + " " + selectedId)

                    chkRF?.isChecked=false
                    chkLR?.isChecked=false
                    chkRR?.isChecked=false

                    if (json.get(TyreKey.vehiclePatternId) != null) {
                        if (selectedId == json.get(TyreKey.vehiclePatternId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Pattern)
                                    != null && json.get(TyreKey.chk1Pattern)?.asString
                                        .equals("RF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Pattern)
                                    != null && json.get(TyreKey.chk2Pattern)?.asString
                                        .equals("LR,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Pattern)
                                    != null && json.get(TyreKey.chk3Pattern)?.asString
                                        .equals("RR,true")
                                ) true else false

                        }
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (selectedTyre.equals("LR")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    Log.e("getpatterlr", "" + json + " " + selectedId)
                    chkRF?.isChecked=false
                    chkLR?.isChecked=false
                    chkRR?.isChecked=false

                    if (json.get(TyreKey.vehiclePatternId) != null) {
                        if (selectedId == json.get(TyreKey.vehiclePatternId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                        TyreKey.chk1Pattern
                                    )?.asString.equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                        TyreKey.chk2Pattern
                                    )?.asString.equals("RF,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                        TyreKey.chk3Pattern
                                    )?.asString.equals("RR,true")
                                ) true else false

                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (selectedTyre.equals("RF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    Log.e("getpatterrf", "" + json + " " + selectedId)
                    chkRF?.isChecked=false
                    chkLR?.isChecked=false
                    chkRR?.isChecked=false

                    if (json.get(TyreKey.vehiclePatternId) != null) {
                        if (selectedId == json.get(TyreKey.vehiclePatternId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                        TyreKey.chk1Pattern
                                    )?.asString.equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                        TyreKey.chk2Pattern
                                    )?.asString.equals("LR,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                        TyreKey.chk3Pattern
                                    )?.asString.equals("RR,true")
                                ) true else false

                        }
                    }

                    Log.e(
                        "getval0",
                        "" + chkRF?.isChecked + " " + chkLR?.isChecked + " " + chkRR?.isChecked
                    )

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (selectedTyre.equals("RR")) {
            if (prefManager?.getValue(TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreRRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreRRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    Log.e("getpatterrr", "" + json + " " + selectedId)
                    chkRF?.isChecked=false
                    chkLR?.isChecked=false
                    chkRR?.isChecked=false

                    if (json.get(TyreKey.vehiclePatternId) != null) {
                        if (selectedId == json.get(TyreKey.vehiclePatternId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Pattern) != null && json.get(
                                        TyreKey.chk1Pattern
                                    )?.asString.equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Pattern) != null && json.get(
                                        TyreKey.chk2Pattern
                                    ).asString.equals("RF,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Pattern) != null && json.get(
                                        TyreKey.chk3Pattern
                                    ).asString.equals("LR,true")
                                ) true else false

                        }
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnNext -> {

                updateRecords()

            }
            R.id.ivEditVehicleMake -> {
                gridviewRecycModel?.layoutManager =
                    GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                adapter = VehiclePatternAdapter(this, arrList, this, -1)
                gridviewRecycModel?.adapter = adapter

                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                Common.slideDown(gridviewRecycModel!!, null)
            }

        }
    }

    private fun updateRecords() {
        var thread = Thread {
//            var entity = VehiclePatternModelClass()
//            entity.Id = arrList?.get(selectedPos)?.Id!!
//            entity.name = arrList?.get(selectedPos)?.name
//            entity.isSelected = true
//            entity.isLRSelected = chkLR?.isChecked!!
//            entity.concat = arrList?.get(selectedPos)?.concat
//            entity.image_url = arrList?.get(selectedPos)?.image_url
//            entity.isRFSelected = chkRF?.isChecked!!
//            entity.isRRSelected = chkRR?.isChecked!!
//            mDb.patternDaoClass().update(entity)

        }
        thread.start()

        if (selectedTyre.equals("LF")) {

            TyreConfigClass.LFVehiclePattern = true
        } else if (selectedTyre.equals("LR")) {
            TyreConfigClass.LRVehiclePattern = true
        } else if (selectedTyre.equals("RF")) {
            TyreConfigClass.RFVehiclePattern = true
        } else if (selectedTyre.equals("RR")) {
            TyreConfigClass.RRVehiclePattern = true
        }


        if (selectedTyre.equals("LF")) {
            chkRF?.text = "RF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"
            TyreConfigClass.RFVehiclePattern = false
            TyreConfigClass.LRVehiclePattern = false
            TyreConfigClass.RRVehiclePattern = false
            if (chkRF?.isChecked!!) {
                TyreConfigClass.RFVehiclePattern = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehiclePattern = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehiclePattern = true
            }
        } else if (selectedTyre.equals("RF")) {
            chkRF?.text = "LF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"
            TyreConfigClass.LFVehiclePattern = false
            TyreConfigClass.LRVehiclePattern = false
            TyreConfigClass.RRVehiclePattern = false
            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehiclePattern = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehiclePattern = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehiclePattern = true
            }

        } else if (selectedTyre.equals("LR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "RR"
            TyreConfigClass.LFVehiclePattern = false
            TyreConfigClass.RFVehiclePattern = false
            TyreConfigClass.RRVehiclePattern = false
            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehiclePattern = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehiclePattern = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehiclePattern = true
            }

        } else if (selectedTyre.equals("RR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "LR"
            TyreConfigClass.LFVehiclePattern = false
            TyreConfigClass.RFVehiclePattern = false
            TyreConfigClass.LRVehiclePattern = false
            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehiclePattern = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehiclePattern = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.LRVehiclePattern = true
            }

        }

        TyreDetailCommonClass.tyreType = selectedTyre
        TyreDetailCommonClass.vehiclePattern = arrList?.get(selectedPos)?.name
        TyreDetailCommonClass.vehiclePatternId = arrList?.get(selectedPos)?.patternId?.toString()

        TyreDetailCommonClass.chk1Pattern = chkRF?.text.toString() + "," + chkRF?.isChecked
        TyreDetailCommonClass.chk2Pattern = chkLR?.text.toString() + "," + chkLR?.isChecked
        TyreDetailCommonClass.chk3Pattern = chkRR?.text.toString() + "," + chkRR?.isChecked

        Log.e("getvalueee11", "" + selectedTyre + " " + TyreConfigClass.RFVehiclePattern)
        var intent = Intent(this, VehicleSizeActivity::class.java)
        intent.putExtra("selectedTyre", selectedTyre)
        startActivityForResult(intent, 1003)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getresultt", "" + resultCode)
        when (resultCode) {
            1002 -> {
                setResult(1002)
                finish()
            }
            1001 -> {
                setResult(1002)
                finish()
            }
            1003 -> {
                setResult(1002)
                finish()
            }
            1004 -> {
                setResult(1002)
                finish()
            }
        }
    }

    private fun setData(json: JsonObject) {

        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }
        if (json.get(TyreKey.vehicleMake) != null) {
            TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
        }
        if (json.get(TyreKey.vehicleMakeId) != null) {
            TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
        }
        if (json.get(TyreKey.vehiclePattern) != null) {
            TyreDetailCommonClass.vehiclePattern = json.get(TyreKey.vehiclePattern)?.asString
        }
        if (json.get(TyreKey.vehiclePatternId) != null) {
            TyreDetailCommonClass.vehiclePatternId = json.get(TyreKey.vehiclePatternId)?.asString
        }
        if (json.get(TyreKey.vehicleSize) != null) {
            TyreDetailCommonClass.vehicleSize = json.get(TyreKey.vehicleSize)?.asString
        }
        if (json.get(TyreKey.vehicleSizeId) != null) {
            TyreDetailCommonClass.vehicleSizeId = json.get(TyreKey.vehicleSizeId)?.asString
        }
        if (json.get(TyreKey.manufaturingDate) != null) {
            TyreDetailCommonClass.manufaturingDate = json.get(TyreKey.manufaturingDate)?.asString
        }
        if (json.get(TyreKey.psiInTyreService) != null) {
            TyreDetailCommonClass.psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString
        }
        if (json.get(TyreKey.psiOutTyreService) != null) {
            TyreDetailCommonClass.psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString
        }
        if (json.get(TyreKey.weightTyreService) != null) {
            TyreDetailCommonClass.weightTyreService = json.get(TyreKey.weightTyreService)?.asString
        }
        if (json.get(TyreKey.sidewell) != null) {
            TyreDetailCommonClass.sidewell = json.get(TyreKey.sidewell)?.asString
        }
        if (json.get(TyreKey.shoulder) != null) {
            TyreDetailCommonClass.shoulder = json.get(TyreKey.shoulder)?.asString
        }
        if (json.get(TyreKey.treadDepth) != null) {
            TyreDetailCommonClass.treadDepth = json.get(TyreKey.treadDepth)?.asString
        }
        if (json.get(TyreKey.treadWear) != null) {
            TyreDetailCommonClass.treadWear = json.get(TyreKey.treadWear)?.asString
        }
        if (json.get(TyreKey.rimDamage) != null) {
            TyreDetailCommonClass.rimDamage = json.get(TyreKey.rimDamage)?.asString
        }
        if (json.get(TyreKey.bubble) != null) {
            TyreDetailCommonClass.bubble = json.get(TyreKey.bubble)?.asString
        }
        if (json.get(TyreKey.visualDetailPhotoUrl) != null) {
            TyreDetailCommonClass.visualDetailPhotoUrl =
                json.get(TyreKey.visualDetailPhotoUrl)?.asString
        }
        if (json.get(TyreKey.isCameraSelectedVisualDetail) != null) {
            TyreDetailCommonClass.isCameraSelectedVisualDetail =
                json.get(TyreKey.isCameraSelectedVisualDetail)?.asString?.toBoolean()!!
        }
        if (json.get(TyreKey.issueResolvedArr) != null) {
//            TyreDetailCommonClass.issueResolvedArr = json.get(TyreKey.issueResolvedArr)?.asJsonArray
        }
        if (json.get(TyreKey.chk1Make) != null) {
            TyreDetailCommonClass.chk1Make = json.get(TyreKey.chk1Make)?.asString
        }
        if (json.get(TyreKey.chk1Pattern) != null) {
            TyreDetailCommonClass.chk1Pattern = json.get(TyreKey.chk1Pattern)?.asString
        }
        if (json.get(TyreKey.chk1Size) != null) {
            TyreDetailCommonClass.chk1Size = json.get(TyreKey.chk1Size)?.asString
        }
        if (json.get(TyreKey.chk2Make) != null) {
            TyreDetailCommonClass.chk2Make = json.get(TyreKey.chk2Make)?.asString
        }
        if (json.get(TyreKey.chk2Pattern) != null) {
            TyreDetailCommonClass.chk2Pattern = json.get(TyreKey.chk2Pattern)?.asString
        }
        if (json.get(TyreKey.chk2Size) != null) {
            TyreDetailCommonClass.chk2Size = json.get(TyreKey.chk2Size)?.asString
        }
        if (json.get(TyreKey.chk3Make) != null) {
            TyreDetailCommonClass.chk3Make = json.get(TyreKey.chk3Make)?.asString
        }
        if (json.get(TyreKey.chk3Pattern) != null) {
            TyreDetailCommonClass.chk3Pattern = json.get(TyreKey.chk3Pattern)?.asString
        }
        if (json.get(TyreKey.chk3Size) != null) {
            TyreDetailCommonClass.chk3Size = json.get(TyreKey.chk3Size)?.asString
        }
        if (json.get(TyreKey.isCompleted) != null) {
            TyreDetailCommonClass.isCompleted =
                json.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
        }
    }
}