package com.walkins.aapkedoorstep.activity

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
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.DB.VehicleMakeModelClass
import com.walkins.aapkedoorstep.DB.VehiclePatternModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.VehicleMakeAdapterNew
import com.walkins.aapkedoorstep.adapter.VehiclePatternAdapter
import com.walkins.aapkedoorstep.common.*
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.networkApi.WarrantyApi
import com.walkins.aapkedoorstep.viewmodel.WarrantyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class VehiclePatternActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
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
    private var selectedIdMake: Int = -1
    private var tvNoDataFound: TextView? = null
    private var tvSelectTyre: TextView? = null

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
        tvSelectTyre = findViewById(R.id.tvSelectTyre)
//        tvNoDataFound = findViewById(R.id.tvNoDataFound)

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
            tvSelectTyre?.text = "Select tyre to apply tyre pattern"
        }

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

        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                            ""
                        )
                    ) {

                        selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    }

                    if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals("")) {
                        selectedIdMake = json.get(TyreKey.vehicleMakeId)?.asString?.toInt()!!
                    }
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
                    if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                            ""
                        )
                    ) {

                        selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    }
                    if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals("")) {
                        selectedIdMake = json.get(TyreKey.vehicleMakeId)?.asString?.toInt()!!
                    }
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
                    if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                            ""
                        )
                    ) {

                        selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    }
                    if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals("")) {
                        selectedIdMake = json.get(TyreKey.vehicleMakeId)?.asString?.toInt()!!
                    }
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
                    if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                            ""
                        )
                    ) {

                        selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    }
                    if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals("")) {
                        selectedIdMake = json.get(TyreKey.vehicleMakeId)?.asString?.toInt()!!
                    }
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        Log.e("getid", "" + selectedId)
        Log.e("selectedpatt00", "" + TyreDetailCommonClass.vehicleMakeId + " " + selectedIdMake)

        if (selectedIdMake != -1) {
            if (!TyreDetailCommonClass.vehicleMakeId.equals("") && TyreDetailCommonClass.vehicleMakeId?.toInt() == selectedIdMake) {
                llVehicleMakeselectedView?.visibility = View.VISIBLE
                btnNext?.visibility = View.VISIBLE
                gridviewRecycModel?.visibility = View.GONE

                val thread = Thread {
                    if (mDb.patternDaoClass().getAllPattern() != null && mDb.patternDaoClass().getAllPattern().size > 0) {
                        arrList?.clear()
                        arrList?.addAll(mDb.patternDaoClass().getAllPattern())
                        Log.e("getSizeVehiclePattern", "" + arrList?.size)
                    }
                }
                thread.start()

                if (arrList != null && arrList?.size!! > 0) {
                    for (i in arrList?.indices!!) {

                        if (selectedId == arrList?.get(i)?.patternId) {
                            arrList?.get(i)?.isSelected = true
                        }
                    }
                }

                tvSelectedModel?.text = TyreDetailCommonClass.vehiclePattern

                Common.hideLoader()
            } else {
                val thread = Thread {
                    if (mDb.patternDaoClass().getAllPattern() != null && mDb.patternDaoClass().getAllPattern().size > 0) {
                        arrList?.clear()
                        arrList?.addAll(mDb.patternDaoClass().getAllPattern())
                        Log.e("getSizeVehiclePattern", "" + arrList?.size)
                    }

                    runOnUiThread {
                        gridviewRecycModel?.layoutManager =
                            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                        adapter = VehiclePatternAdapter(this, arrList, this, selectedId)
                        gridviewRecycModel?.adapter = adapter
                    }
                }
                thread.start()
            }
        } else {
            val thread = Thread {
                if (mDb.patternDaoClass().getAllPattern() != null && mDb.patternDaoClass().getAllPattern().size > 0) {
                    arrList?.clear()
                    arrList?.addAll(mDb.patternDaoClass().getAllPattern())
                    Log.e("getSizeVehiclePattern", "" + arrList?.size)
                }

                runOnUiThread {
                    gridviewRecycModel?.layoutManager =
                        GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                    adapter = VehiclePatternAdapter(this, arrList, this, selectedId)
                    gridviewRecycModel?.adapter = adapter
                }
            }
            thread.start()
        }
    }


    override fun onPositionClick(variable: Int, check: Int) {

        Common.slideUp(gridviewRecycModel!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        tvSelectedModel?.text = arrList?.get(variable)?.name
        selectedPosition = variable
        selectedPos = variable
        selectedId = arrList?.get(variable)?.patternId!!
        Log.e("selectedpatt", "" + selectedId)

        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehiclePatternId")?.asString?.toInt()!!
                    Log.e("getpatterlf", "" + json + " " + selectedId)

                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false

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
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false

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
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false

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
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false

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

//        ==============================================================

        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                if (json.get("vehiclePatternId") != null && !json.get("vehiclePatternId")?.asString.equals(
                        ""
                    )
                ) {
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
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
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
                Common.showLoader(this)
                GlobalScope.launch(Dispatchers.Main) {
                    launch(Dispatchers.Main) {
                        try {
                            gridviewRecycModel?.layoutManager =
                                GridLayoutManager(this@VehiclePatternActivity, 3, RecyclerView.VERTICAL, false)
                            adapter = VehiclePatternAdapter(this@VehiclePatternActivity, arrList, this@VehiclePatternActivity, -1)
                            gridviewRecycModel?.adapter = adapter

                            Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                            Common.slideDown(gridviewRecycModel!!, null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                Common.hideLoader()
            }

        }
    }

    /*fun getVehiclePattern(isExpand: Boolean) {
        Common.showLoader(this)
        var selectedMakeId: String? = null
        if (intent != null) {
            selectedMakeId = intent?.getStringExtra("selectedMakeId")
        }

        if (selectedMakeId == null || selectedMakeId.equals("")) {
            selectedMakeId = TyreDetailCommonClass.vehicleMakeId
        }

        Log.e("makeiddd", "" + TyreDetailCommonClass.vehicleMakeId + " " + selectedMakeId)

        warrantyViewModel.getVehiclePattern(
            if (!selectedMakeId.equals("")) selectedMakeId?.toInt()!! else 0,
            prefManager.getAccessToken()!!,
            this
        )

        warrantyViewModel.getVehiclePattern()
            ?.observe(this@VehiclePatternActivity, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        patternModel = it
                        Log.e("getmodel00::", "" + patternModel)

                        for (i in it.data.indices) {
                            val model = VehiclePatternModelClass()
                            model.patternId = it.data.get(i).patternId
                            model.name = it.data.get(i).name
                            model.isSelected = false
                            model.isLRSelected = it.data.get(i).isLRSelected
                            model.isRFSelected = it.data.get(i).isRFSelected
                            model.isRRSelected = it.data.get(i).isRRSelected

                            arrList?.add(model)
                        }

                        runOnUiThread {
                            gridviewRecycModel?.layoutManager =
                                GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                            adapter = VehiclePatternAdapter(this, arrList, this, selectedId)
                            gridviewRecycModel?.adapter = adapter
                            gridviewRecycModel?.visibility = View.VISIBLE

                            if (isExpand) {
                                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                                Common.slideDown(gridviewRecycModel!!, null)
                            }
                        }

                        if (arrList?.size!! > 0) {
//                            tvNoDataFound?.visibility = View.GONE
                            savePatternData(patternModel!!)
                        } else {
//                            tvNoDataFound?.visibility = View.VISIBLE
                        }


                    } else {
                        if (it.error != null && it.error.size > 0) {
                            if (it.error.get(0).statusCode != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager.clearAll()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    this@VehiclePatternActivity.let { it1 ->
                                        Common.showShortToast(
                                            it.error.get(0).message,
                                            it1
                                        )
                                    }
                                }

                            } else {
                                this@VehiclePatternActivity.let { it1 ->
                                    Common.showShortToast(
                                        it.error.get(0).message,
                                        it1
                                    )
                                }
                            }
                        }
                    }
                } else {
                    showLongToast("Something Went Wrong", this@VehiclePatternActivity)
                }
            })
    }
*/

    private fun updateRecords() {

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
        if (selectedPos != -1) {
            TyreDetailCommonClass.vehiclePattern = arrList?.get(selectedPos)?.name
        }
        if (selectedPos != -1) {
            TyreDetailCommonClass.vehiclePatternId = arrList?.get(selectedPos)?.patternId?.toString()
        }
        TyreDetailCommonClass.chk1Pattern = chkRF?.text.toString() + "," + chkRF?.isChecked
        TyreDetailCommonClass.chk2Pattern = chkLR?.text.toString() + "," + chkLR?.isChecked
        TyreDetailCommonClass.chk3Pattern = chkRR?.text.toString() + "," + chkRR?.isChecked

        Log.e("getvalueee11", "" + selectedTyre + " " + TyreConfigClass.RFVehiclePattern)
        var intent = Intent(this, VehicleSizeActivity::class.java)
        intent.putExtra("selectedId", selectedId)
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
        Log.e("getvalselected", "" + json)
        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }

        if (TyreDetailCommonClass.vehicleMake.equals("")) {

            if (json.get(TyreKey.vehicleMake) != null && !json.get(TyreKey.vehicleMake)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
            }
        }
        if (TyreDetailCommonClass.vehicleMakeId.equals("")) {

            if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals(
                    ""
                )
            ) {
                TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
            }
        }
        if (json.get(TyreKey.vehiclePattern) != null && !json.get(TyreKey.vehiclePattern)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehiclePattern = json.get(TyreKey.vehiclePattern)?.asString
        }
        if (json.get(TyreKey.vehiclePatternId) != null && !json.get(TyreKey.vehiclePatternId)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehiclePatternId = json.get(TyreKey.vehiclePatternId)?.asString
        }
        if (json.get(TyreKey.vehicleSize) != null && !json.get(TyreKey.vehicleSize)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehicleSize = json.get(TyreKey.vehicleSize)?.asString
        }
        if (json.get(TyreKey.vehicleSizeId) != null && !json.get(TyreKey.vehicleSizeId)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehicleSizeId = json.get(TyreKey.vehicleSizeId)?.asString
        }

        if (json.get(TyreKey.manufaturingDate) != null && !json.get(TyreKey.manufaturingDate)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.manufaturingDate = json.get(TyreKey.manufaturingDate)?.asString
        }
        if (json.get(TyreKey.psiInTyreService) != null && !json.get(TyreKey.psiInTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString
        }
        if (json.get(TyreKey.psiOutTyreService) != null && !json.get(TyreKey.psiOutTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString
        }
        if (json.get(TyreKey.weightTyreService) != null && !json.get(TyreKey.weightTyreService)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.weightTyreService = json.get(TyreKey.weightTyreService)?.asString
        }
        if (json.get(TyreKey.sidewell) != null && !json.get(TyreKey.sidewell)?.asString.equals("")) {
            TyreDetailCommonClass.sidewell = json.get(TyreKey.sidewell)?.asString
        }
        if (json.get(TyreKey.shoulder) != null && !json.get(TyreKey.shoulder)?.asString.equals("")) {
            TyreDetailCommonClass.shoulder = json.get(TyreKey.shoulder)?.asString
        }
        if (json.get(TyreKey.treadDepth) != null && !json.get(TyreKey.treadDepth)?.asString.equals("")) {
            TyreDetailCommonClass.treadDepth = json.get(TyreKey.treadDepth)?.asString
        }
        if (json.get(TyreKey.treadWear) != null && !json.get(TyreKey.treadWear)?.asString.equals("")) {
            TyreDetailCommonClass.treadWear = json.get(TyreKey.treadWear)?.asString
        }
        if (json.get(TyreKey.rimDamage) != null && !json.get(TyreKey.rimDamage)?.asString.equals("")) {
            TyreDetailCommonClass.rimDamage = json.get(TyreKey.rimDamage)?.asString
        }
        if (json.get(TyreKey.bubble) != null && !json.get(TyreKey.bubble)?.asString.equals("")) {
            TyreDetailCommonClass.bubble = json.get(TyreKey.bubble)?.asString
        }
        if (json.get(TyreKey.visualDetailPhotoUrl) != null && !json.get(TyreKey.visualDetailPhotoUrl)?.asString.equals(
                ""
            )
        ) {
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
        /*if (json.get(TyreKey.chk1Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk1Make = json.get(TyreKey.chk1Make)?.asString
        }
        if (json.get(TyreKey.chk1Pattern) != null && !json.get(TyreKey.chk1Pattern)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.chk1Pattern = json.get(TyreKey.chk1Pattern)?.asString
        }
        if (json.get(TyreKey.chk1Size) != null && !json.get(TyreKey.chk1Size)?.asString.equals("")) {
            TyreDetailCommonClass.chk1Size = json.get(TyreKey.chk1Size)?.asString
        }
        if (json.get(TyreKey.chk2Make) != null && !json.get(TyreKey.chk2Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk2Make = json.get(TyreKey.chk2Make)?.asString
        }
        if (json.get(TyreKey.chk2Pattern) != null && !json.get(TyreKey.chk2Pattern)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.chk2Pattern = json.get(TyreKey.chk2Pattern)?.asString
        }
        if (json.get(TyreKey.chk2Size) != null && !json.get(TyreKey.chk2Size)?.asString.equals("")) {
            TyreDetailCommonClass.chk2Size = json.get(TyreKey.chk2Size)?.asString
        }
        if (json.get(TyreKey.chk3Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk3Make = json.get(TyreKey.chk3Make)?.asString
        }
        if (json.get(TyreKey.chk3Pattern) != null && !json.get(TyreKey.chk3Pattern)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.chk3Pattern = json.get(TyreKey.chk3Pattern)?.asString
        }
        if (json.get(TyreKey.chk3Size) != null && !json.get(TyreKey.chk3Size)?.asString.equals("")) {
            TyreDetailCommonClass.chk3Size = json.get(TyreKey.chk3Size)?.asString
        }*/
        if (json.get(TyreKey.isCompleted) != null) {
            TyreDetailCommonClass.isCompleted =
                json.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
        }
    }


}