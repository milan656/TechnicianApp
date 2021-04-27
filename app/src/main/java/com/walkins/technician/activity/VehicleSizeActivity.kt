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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.walkins.technician.DB.DBClass
import com.walkins.technician.DB.VehiclePatternModelClass
import com.walkins.technician.DB.VehicleSizeModelClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehiclePatternAdapter
import com.walkins.technician.adapter.VehicleSizeAdapter
import com.walkins.technician.common.*
import com.walkins.technician.model.login.sizemodel.SizeData
import com.walkins.technician.model.login.sizemodel.SizeModel
import com.walkins.technician.viewmodel.WarrantyViewModel

class VehicleSizeActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
    private var sizeModel: SizeModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehicleSizeAdapter? = null
    private var gridviewRecycModel: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    var arrList: ArrayList<VehicleSizeModelClass>? = ArrayList()
    private lateinit var mDb: DBClass
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var btnNext: Button? = null
    private var tvSelectedModel: TextView? = null
    private var tvSelectTyre: TextView? = null
    private var ivEditVehicleMake: ImageView? = null
    private var chkRR: CheckBox? = null
    private var chkRF: CheckBox? = null
    private var chkLR: CheckBox? = null
    private var selectedPos = -1
    private var selectedId = -1
    private var selectedTyre = ""
    private var selectedPatternId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_model)
        prefManager = PrefManager(this)
        warrantyViewModel = ViewModelProviders.of(this).get(WarrantyViewModel::class.java)
        mDb = DBClass.getInstance(applicationContext)
        init()
    }

    private fun init() {
        btnNext = findViewById(R.id.btnNext)
        llVehicleMakeselectedView = findViewById(R.id.llVehicleMakeselectedView)

        chkRR = findViewById(R.id.chkRR)
        chkRF = findViewById(R.id.chkRF)
        chkLR = findViewById(R.id.chkLR)

        gridviewRecycModel = findViewById(R.id.gridviewRecycModel)
        tvSelectTyre = findViewById(R.id.tvSelectTyre)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        tvSelectedModel = findViewById(R.id.tvSelectedModel)
        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        if (intent != null) {
            if (intent.hasExtra("selectedTyre")) {

                tvTitle?.text =
                    "Select Tyre Size - " + intent.getStringExtra("selectedTyre")
                selectedTyre = intent.getStringExtra("selectedTyre")!!
            }
            if (intent.hasExtra("selectedId")) {
                selectedPatternId = intent.getIntExtra("selectedId", 0)
            }
            tvSelectTyre?.text="Select tyre to apply tyre size"
        }

        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)
        ivEditVehicleMake?.setOnClickListener(this)


//        getVehicleMake()

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
        //        Common.setBlankAllValues()
        var thread = Thread {

            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size)
            if (mDb.sizeDaoClass().getAllSize() != null && mDb.sizeDaoClass()
                    .getAllSize().size > 0
            ) {
                arrList?.addAll(mDb.sizeDaoClass().getAllSize())

                if (selectedTyre.equals("LF")) {
                    if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                        !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
                    ) {
                        var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                        try {
                            var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                            selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!

                            setData(json)

                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Size) != null && json.get(TyreKey.chk1Size).asString.equals(
                                            "RF,true"
                                        )
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Size) != null && json.get(TyreKey.chk2Size).asString.equals(
                                            "LR,true"
                                        )
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Size) != null && json.get(TyreKey.chk3Size).asString.equals(
                                            "RR,true"
                                        )
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
                            selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!

                            setData(json)
                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Size) != null && json.get(
                                            TyreKey.chk1Size
                                        ).asString.equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Size) != null && json.get(
                                            TyreKey.chk2Size
                                        ).asString.equals("RF,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Size) != null && json.get(
                                            TyreKey.chk3Size
                                        ).asString.equals("RR,true")
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
                            selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!
                            Log.e("getobjrfche", "" + json + " " + selectedId)
                            setData(json)
                            runOnUiThread {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Size) != null && json.get(
                                            TyreKey.chk1Size
                                        )?.asString.equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Size) != null && json.get(
                                            TyreKey.chk2Size
                                        )?.asString.equals("LR,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Size) != null && json.get(
                                            TyreKey.chk3Size
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
                            selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!

                            setData(json)
                            runOnUiThread {

                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Size) != null && json.get(TyreKey.chk1Size)?.asString
                                            .equals("LF,true")
                                    ) true else false
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Size) != null && json.get(TyreKey.chk2Size)?.asString
                                            .equals("RF,true")
                                    ) true else false
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Size) != null && json.get(
                                            TyreKey.chk3Size
                                        )?.asString.equals("LR,true")
                                    ) true else false

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }

//              ======================================================================

                if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                    !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        Log.e("getobjrfche00", "" + json + " " + selectedId + " ")

                        if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                                ""
                            )
                        ) {
                            if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                        if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                                ""
                            )
                        ) {

                            if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                        if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                                ""
                            )
                        ) {

                            if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                        if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                                ""
                            )
                        ) {

                            if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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

        }
        thread.start()

        var handler = Handler()
        handler.postDelayed(Runnable {
            gridviewRecycModel?.layoutManager =
                GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)

            adapter = VehicleSizeAdapter(this, arrList, this, selectedId)
            gridviewRecycModel?.adapter = adapter
            Common.hideLoader()
        }, 1000)


        Log.e("getid", "" + selectedId)


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

    private fun setData(json: JsonObject) {
        Log.e("getvalselected", "" + json)
        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }
        if (json.get(TyreKey.vehicleMake) != null && !json.get(TyreKey.vehicleMake)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
        }
        if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals(
                ""
            )
        ) {
            TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
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
        if (json.get(TyreKey.chk1Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
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
        }
        if (json.get(TyreKey.isCompleted) != null) {
            TyreDetailCommonClass.isCompleted =
                json.get(TyreKey.isCompleted)?.asString?.toBoolean()!!
        }
    }


    /* fun getVehicleMake() {
         Common.showLoader(this)

         warrantyViewModel.getVehicleSize(460, 41, prefManager.getAccessToken()!!, this)

         warrantyViewModel.getVehicleSize()
             ?.observe(this@VehicleSizeActivity, androidx.lifecycle.Observer {
                 Common.hideLoader()
                 if (it != null) {
                     if (it.success) {
                         sizeModel = it
                         Log.e("getmodel00::", "" + sizeModel)

                         for (i in it.data?.indices!!) {
                             if (!it.data.get(i).name.equals("Other", ignoreCase = true)) {
 //                                arrList?.add(it.data.get(i))
                             }
                         }

                         gridviewRecycModel?.layoutManager =
                             GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                         gridviewRecycModel?.addItemDecoration(
                             SpacesItemDecoration(
                                 20
                             )
                         )

                         adapter?.notifyDataSetChanged()


                     } else {
                         if (it.error != null && it.error.size > 0) {
                             if (it.error.get(0).statusCode != null) {
                                 if (it.error.get(0).statusCode == 400) {
                                     prefManager.clearAll()
                                     val intent = Intent(this, LoginActivity::class.java)
                                     startActivity(intent)
                                     finish()
                                 } else {
                                     this@VehicleSizeActivity.let { it1 ->
                                         Common.showShortToast(
                                             it.error.get(0).message,
                                             it1
                                         )
                                     }
                                 }

                             } else {
                                 this@VehicleSizeActivity.let { it1 ->
                                     Common.showShortToast(
                                         it.error.get(0).message,
                                         it1
                                     )
                                 }
                             }
                         }
                     }
                 } else {
                     showLongToast("Something Went Wrong", this@VehicleSizeActivity)
                 }
             })
     }*/

    override fun onPositionClick(variable: Int, check: Int) {

        Common.slideUp(gridviewRecycModel!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        tvSelectedModel?.text = arrList?.get(variable)?.name
        selectedPos = variable

        selectedId = arrList?.get(variable)?.sizeId!!
        Log.e("getsizeid", "" + selectedId)

        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
//                    selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!

                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleSizeId) != null) {
                        if (selectedId == json.get(TyreKey.vehicleSizeId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Size) != null && json.get(TyreKey.chk1Size).asString.equals(
                                        "RF,true"
                                    )
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Size) != null && json.get(TyreKey.chk2Size).asString.equals(
                                        "LR,true"
                                    )
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Size) != null && json.get(TyreKey.chk3Size).asString.equals(
                                        "RR,true"
                                    )
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
//                    selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleSizeId) != null) {
                        if (selectedId == json.get(TyreKey.vehicleSizeId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Size) != null && json.get(
                                        TyreKey.chk1Size
                                    ).asString.equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Size) != null && json.get(
                                        TyreKey.chk2Size
                                    ).asString.equals("RF,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Size) != null && json.get(
                                        TyreKey.chk3Size
                                    ).asString.equals("RR,true")
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
//                    selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleSizeId) != null) {
                        if (selectedId == json.get(TyreKey.vehicleSizeId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Size) != null && json.get(
                                        TyreKey.chk1Size
                                    )?.asString.equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Size) != null && json.get(
                                        TyreKey.chk2Size
                                    )?.asString.equals("LR,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Size) != null && json.get(
                                        TyreKey.chk3Size
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
//                    selectedId = json.get("vehicleSizeId")?.asString?.toInt()!!

                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleSizeId) != null) {
                        if (selectedId == json.get(TyreKey.vehicleSizeId)?.asString?.toInt()) {
                            chkRF?.isChecked =
                                if (json.get(TyreKey.chk1Size) != null && json.get(TyreKey.chk1Size)?.asString
                                        .equals("LF,true")
                                ) true else false
                            chkLR?.isChecked =
                                if (json.get(TyreKey.chk2Size) != null && json.get(TyreKey.chk2Size)?.asString
                                        .equals("RF,true")
                                ) true else false
                            chkRR?.isChecked =
                                if (json.get(TyreKey.chk3Size) != null && json.get(
                                        TyreKey.chk3Size
                                    )?.asString.equals("LR,true")
                                ) true else false


                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

//        ===============================================================

        if (prefManager?.getValue(TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.TyreLFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.TyreLFObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjrfche00", "" + json + " " + selectedId + " ")

                if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                        ""
                    )
                ) {
                    if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                        ""
                    )
                ) {

                    if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                        ""
                    )
                ) {

                    if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                if (json.get("vehicleSizeId") != null && !json.get("vehicleSizeId")?.asString.equals(
                        ""
                    )
                ) {

                    if (selectedId == json.get("vehicleSizeId")?.asString?.toInt()) {
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
                gridviewRecycModel?.layoutManager =
                    GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                adapter = VehicleSizeAdapter(this, arrList, this, -1)
                gridviewRecycModel?.adapter = adapter
                gridviewRecycModel?.visibility = View.VISIBLE

                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                Common.slideDown(gridviewRecycModel!!, null)
            }

        }
    }

    private fun updateRecords() {
        var thread = Thread {
//            var entity = VehicleSizeModelClass()
//            entity.Id = arrList?.get(selectedPos)?.Id!!
//            entity.name = arrList?.get(selectedPos)?.name
//            entity.isSelected = true
//            entity.isLRSelected = chkLR?.isChecked!!
//            entity.isRFSelected = chkRF?.isChecked!!
//            entity.isRRSelected = chkRR?.isChecked!!
//            mDb.sizeDaoClass().update(entity)

        }
        thread.start()

        if (selectedTyre.equals("LF")) {

            TyreConfigClass.LFVehicleSize = true
        } else if (selectedTyre.equals("LR")) {
            TyreConfigClass.LRVehicleSize = true
        } else if (selectedTyre.equals("RF")) {
            TyreConfigClass.RFVehicleSize = true
        } else if (selectedTyre.equals("RR")) {
            TyreConfigClass.RRVehicleSize = true
        }

        if (selectedTyre.equals("LF")) {
            chkRF?.text = "RF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"

            if (chkRF?.isChecked!!) {
                TyreConfigClass.RFVehicleSize = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehicleSize = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleSize = true
            }
        } else if (selectedTyre.equals("RF")) {
            chkRF?.text = "LF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"

            TyreConfigClass.LFVehicleSize = false
            TyreConfigClass.LRVehicleSize = false
            TyreConfigClass.RRVehicleSize = false

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleSize = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehicleSize = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleSize = true
            }

        } else if (selectedTyre.equals("LR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "RR"

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleSize = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehicleSize = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleSize = true
            }

        } else if (selectedTyre.equals("RR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "LR"

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleSize = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehicleSize = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.LRVehicleSize = true
            }

        }

        TyreDetailCommonClass.tyreType = selectedTyre
        TyreDetailCommonClass.vehicleSize = arrList?.get(selectedPos)?.name
        TyreDetailCommonClass.vehicleSizeId = arrList?.get(selectedPos)?.sizeId?.toString()

        TyreDetailCommonClass.chk1Size = chkRF?.text.toString() + "," + chkRF?.isChecked
        TyreDetailCommonClass.chk2Size = chkLR?.text.toString() + "," + chkLR?.isChecked
        TyreDetailCommonClass.chk3Size = chkRR?.text.toString() + "," + chkRR?.isChecked

        Log.e("getvalueee22", "" + selectedTyre + " " + TyreConfigClass.RFVehicleSize)
        Log.e("getvalueee22", "" + TyreDetailCommonClass.vehicleSize)
        Log.e("getvalueee22", "" + TyreDetailCommonClass.vehicleSizeId)
        val intent = Intent(this, VisualDetailsActivity::class.java)
        intent.putExtra("selectedTyre", selectedTyre)
        startActivityForResult(intent, 1005)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getresult22", "" + resultCode)
        when (resultCode) {

            1001 -> {
                setResult(1002)
                finish()
            }
            1004 -> {
                setResult(1003)
                finish()
            }
            1003 -> {
                finish()
            }
            1005 -> {
                setResult(1003)
                finish()
            }
        }
    }


}