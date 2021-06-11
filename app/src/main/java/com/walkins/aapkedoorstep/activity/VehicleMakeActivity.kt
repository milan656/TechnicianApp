package com.walkins.aapkedoorstep.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.DB.DBClass
import com.walkins.aapkedoorstep.DB.VehicleMakeModelClass
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.VehicleMakeAdapterNew
import com.walkins.aapkedoorstep.common.TyreConfigClass
import com.walkins.aapkedoorstep.common.TyreDetailCommonClass
import com.walkins.aapkedoorstep.common.TyreKey
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.viewmodel.WarrantyViewModel

@SuppressLint("SetTextI18n")
class VehicleMakeActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehicleMakeAdapterNew? = null
    private var gridviewRecycMake_: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var selectedTyre: String = ""
    var arrList: ArrayList<VehicleMakeModelClass>? = ArrayList()
    private lateinit var mDb: DBClass
    private var tyreConfigType: String = ""
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var btnNext: Button? = null
    private var ivSelectedCar: ImageView? = null
    private var ivEditVehicleMake: ImageView? = null

    private var chkRR: CheckBox? = null
    private var chkRF: CheckBox? = null
    private var chkLR: CheckBox? = null
    private var llRFView: LinearLayout? = null
    private var tvSelectTyre: TextView? = null
    private var llLRView: LinearLayout? = null
    private var llRRView: LinearLayout? = null

    private var selectedPos = -1

    private var selectedName: String = ""
    private var selectedImage: String = ""
    private var tvmakeName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_make)
        prefManager = PrefManager(this)
        warrantyViewModel = ViewModelProviders.of(this).get(WarrantyViewModel::class.java)
        mDb = DBClass.getInstance(applicationContext)
        init()
    }

    private fun init() {

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        ivSelectedCar = findViewById(R.id.ivSelectedCar)
        tvmakeName = findViewById(R.id.tvmakeName)
        llRRView = findViewById(R.id.llRRView)
        llRFView = findViewById(R.id.llRFView)
        llLRView = findViewById(R.id.llLRView)
        tvSelectTyre = findViewById(R.id.tvSelectTyre)
        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)
        gridviewRecycMake_ = findViewById(R.id.gridviewRecycMake_)
        btnNext = findViewById(R.id.btnNext)
        llVehicleMakeselectedView = findViewById(R.id.llVehicleMakeselectedView)
        chkRR = findViewById(R.id.chkRR)
        chkRF = findViewById(R.id.chkRF)
        chkLR = findViewById(R.id.chkLR)

        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        ivEditVehicleMake?.setOnClickListener(this)

//        Common.setClearAllValues()

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
            if (intent.getStringExtra("selectedTyre") != null) {
                selectedTyre = intent.getStringExtra("selectedTyre")!!
            }
        }

        TyreDetailCommonClass.tyreType = selectedTyre
//        tvTitle?.text = "Select Tyre Make - LF"
        gridviewRecycMake_?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
//        gridviewRecycMake_?.addItemDecoration(
//            SpacesItemDecoration(
//                20
//            )
//        )
        Log.e("getsizee121", "" + arrList?.size)

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
            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size + " " + selectedTyre)
            if (mDb.daoClass().getAllVehicleType() != null && mDb.daoClass()
                    .getAllVehicleType().size > 0
            ) {
                arrList?.addAll(mDb.daoClass().getAllVehicleType())
                Log.e("getSizeVehicleBrand", "" + arrList?.size)
            }

            if (selectedTyre.equals("LF")) {
                if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject) != null &&
                    !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        selectedName = json.get("vehicleMake")?.asString!!
                        if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals("")) {
                            selectedImage = json.get(TyreKey.vehicleMakeURL)?.asString!!
                            TyreDetailCommonClass.vehicleMakeURL = selectedImage
                        }

                        setData(json)

                        runOnUiThread {
                            chkRF?.isChecked = false
                            chkLR?.isChecked = false
                            chkRR?.isChecked = false
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString.equals("RF,true")) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("LR,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("RR,true")
                                    ) true else false
                            }
                        }

                        Log.e("getsizee", "lf")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (selectedTyre.equals("LR")) {
                if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject) != null &&
                    !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        selectedName = json.get("vehicleMake")?.asString!!
                        if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals("")) {
                            selectedImage = json.get(TyreKey.vehicleMakeURL)?.asString!!
                            TyreDetailCommonClass.vehicleMakeURL = selectedImage
                        }
                        setData(json)

                        runOnUiThread {
                            chkRF?.isChecked = false
                            chkLR?.isChecked = false
                            chkRR?.isChecked = false
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString
                                            .equals("LF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("RF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("RR,true")
                                    ) true else false
                            }
                        }

                        Log.e("getsizee", "lr")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (selectedTyre.equals("RF")) {
                if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject) != null &&
                    !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        selectedName = json.get("vehicleMake")?.asString!!
                        if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals("")) {
                            selectedImage = json.get(TyreKey.vehicleMakeURL)?.asString!!
                            TyreDetailCommonClass.vehicleMakeURL = selectedImage
                        }
                        setData(json)

                        Log.e("getvalselected", "" + selectedName)
                        Log.e("getval", "" + json.get(TyreKey.chk1Make)?.asString)
                        Log.e("getval", "" + json.get(TyreKey.chk2Make)?.asString)
                        Log.e("getval", "" + json.get(TyreKey.chk3Make)?.asString)
                        runOnUiThread {
                            chkRF?.isChecked = false
                            chkLR?.isChecked = false
                            chkRR?.isChecked = false
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make)?.asString.equals("LF,true")) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make)?.asString.equals("LR,true")) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make)?.asString.equals("RR,true")) true else false
                            }

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
                if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject) != null &&
                    !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject).equals("")
                ) {
                    var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject)
                    try {
                        var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                        selectedName = json.get("vehicleMake")?.asString!!
                        if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals("")) {
                            selectedImage = json.get(TyreKey.vehicleMakeURL)?.asString!!
                            TyreDetailCommonClass.vehicleMakeURL = selectedImage
                        }
                        setData(json)

                        runOnUiThread {
                            chkRF?.isChecked = false
                            chkLR?.isChecked = false
                            chkRR?.isChecked = false
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString
                                            .equals("LF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("RF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("LR,true")
                                    ) true else false
                            }
                        }

                        Log.e("getsizee", "rr")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
//=========================================================================================================
            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    Log.e("getobjjlf", "" + json + " " + selectedName)
                    if (selectedName != null && !selectedName.equals("") &&
                        selectedName.equals(json.get("vehicleMake")?.asString!!)
                    ) {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("LF")) {
                                    chkRF?.isChecked = true

                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("LF")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("LF")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }

                        }
                    } else {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("LF")) {
                                    chkRF?.isChecked = true

                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("LF")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("LF")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }

                        }
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                    Log.e("getobjj", "" + json + " " + selectedName)
                    if (selectedName != null && !selectedName.equals("") &&
                        selectedName.equals(json.get("vehicleMake")?.asString!!)
                    ) {
                        Log.e("getobjj", "" + json.get("vehicleMake")?.asString)
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("LR")) {
                                    chkRF?.isChecked = true
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("LR")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("LR")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("LR")) {
                                    chkRF?.isChecked = true
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("LR")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("LR")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                    Log.e("getobjrf", "" + json + " ")
                    Log.e("getobjrf", "" + selectedName + " ")
                    Log.e("getobjrf", "" + json.get("vehicleMake")?.asString + " ")

                    if (selectedName != null && !selectedName.equals("") &&
                        selectedName.equals(json.get("vehicleMake")?.asString!!)
                    ) {

                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("RF")) {
                                    chkRF?.isChecked = true
                                    Log.e("getobjrf", "0" + " ")
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("RF")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                    Log.e("getobjrf", "1" + " ")
                                }
                                if (chkRR?.text?.toString().equals("RF")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                    Log.e("getobjrf", "2" + " ")
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("RF")) {
                                    chkRF?.isChecked = true
                                    Log.e("getobjrf", "0" + " ")
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("RF")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                    Log.e("getobjrf", "1" + " ")
                                }
                                if (chkRR?.text?.toString().equals("RF")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                    Log.e("getobjrf", "2" + " ")
                                }
                            }
                        }
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                    if (selectedName != null && !selectedName.equals("") &&
                        selectedName.equals(json.get("vehicleMake")?.asString!!)
                    ) {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("RR")) {
                                    chkRF?.isChecked = true
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("RR")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("RR")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            if (json.get("vehicleMake") != null && !json.get("vehicleMake")?.asString.equals("")) {
                                if (chkRF?.text?.toString().equals("RR")) {
                                    chkRF?.isChecked = true
                                    llRFView?.visibility = View.GONE
                                }
                                if (chkLR?.text?.toString().equals("RR")) {
                                    chkLR?.isChecked = true
                                    llLRView?.visibility = View.GONE
                                }
                                if (chkRR?.text?.toString().equals("RR")) {
                                    chkRR?.isChecked = true
                                    llRRView?.visibility = View.GONE
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            runOnUiThread {
                if (llRFView?.visibility == View.GONE && llLRView?.visibility == View.GONE && llRRView?.visibility == View.GONE) {
                    tvSelectTyre?.visibility = View.GONE
                }
            }
            runOnUiThread {
                Log.e("selectedname", "" + selectedName)
                if (!selectedName.equals("")) {
                    llVehicleMakeselectedView?.visibility = View.VISIBLE
                    btnNext?.visibility = View.VISIBLE
                    gridviewRecycMake_?.visibility = View.GONE

                    if (arrList != null && arrList?.size!! > 0) {
                        for (i in arrList?.indices!!) {

                            if (selectedName.equals(arrList?.get(i)?.name)) {
                                arrList?.get(i)?.isSelected = true
                            }
                        }
                    }
                    try {
                        Glide.with(this)
                            .load(selectedImage)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .into(ivSelectedCar!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    tvmakeName?.text = selectedName
                    Common.hideLoader()

                } else {
                    adapter = VehicleMakeAdapterNew(this, arrList, this, selectedName)
                    gridviewRecycMake_?.adapter = adapter
                    gridviewRecycMake_?.visibility = View.VISIBLE
                    Common.hideLoader()
                }
            }
        }
        thread.start()


        /* var handler = Handler()
         handler.postDelayed(Runnable {
             Log.e("getsizee00", "" + arrList?.size + " " + selectedName)
             adapter = VehicleMakeAdapterNew(this, arrList, this, selectedName)
             gridviewRecycMake_?.adapter = adapter
             gridviewRecycMake_?.visibility = View.VISIBLE
             Common.hideLoader()
         }, 1000)*/



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

        Common.slideUp(gridviewRecycMake_!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        selectedPos = variable
        Log.e("getselected", "" + arrList?.get(selectedPos)?.name)
        Log.e("getselected", "" + arrList?.get(selectedPos)?.brand_id)
        Log.e("getselected", "" + arrList?.get(selectedPos)?.concat)
        TyreDetailCommonClass.vehicleMakeURL = arrList?.get(selectedPos)?.concat
        selectedName = arrList?.get(selectedPos)?.name!!

        tvmakeName?.text = selectedName

        try {
            Glide.with(this)
                .load(arrList?.get(variable)?.concat)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(ivSelectedCar!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (selectedTyre.equals("LF")) {
            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    //                    selectedName = json.get("vehicleMake")?.asString!!
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    Log.e("getobjrf", "5" + " ")
                    if (json.get(TyreKey.vehicleMake) != null) {
                        if (selectedName.equals(
                                json.get(TyreKey.vehicleMake)?.asString,
                                ignoreCase = true
                            )
                        ) {
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString.equals("RF,true")) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("LR,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("RR,true")
                                    ) true else false
                            }

                        }
                    }
                    Log.e("getsizee", "lf")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (selectedTyre.equals("LR")) {
            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    //                    selectedName = json.get("vehicleMake")?.asString!!
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleMake) != null) {
                        if (selectedName.equals(
                                json.get(TyreKey.vehicleMake)?.asString,
                                ignoreCase = true
                            )
                        ) {
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString
                                            .equals("LF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("RF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("RR,true")
                                    ) true else false
                            }

                        }
                    }


                    Log.e("getsizee", "lr")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (selectedTyre.equals("RF")) {
            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    //                    selectedName = json.get("vehicleMake")?.asString!!

                    Log.e("getvalselected", "" + selectedName)
                    Log.e("getval", "" + json.get(TyreKey.chk1Make)?.asString)
                    Log.e("getval", "" + json.get(TyreKey.chk2Make)?.asString)
                    Log.e("getval", "" + json.get(TyreKey.chk3Make)?.asString)
                    Log.e("getval", "" + json.get(TyreKey.vehicleMake)?.asString)

                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false

                    if (json.get(TyreKey.vehicleMake) != null) {
                        if (selectedName.equals(
                                json.get(TyreKey.vehicleMake)?.asString,
                                ignoreCase = true
                            )
                        ) {
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make)?.asString.equals("LF,true")) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make)?.asString.equals("LR,true")) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make)?.asString.equals("RR,true")) true else false
                            }

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
            if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject) != null &&
                !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject).equals("")
            ) {
                var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject)
                try {
                    var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                    //                    selectedName = json.get("vehicleMake")?.asString!!
                    chkRF?.isChecked = false
                    chkLR?.isChecked = false
                    chkRR?.isChecked = false
                    if (json.get(TyreKey.vehicleMake) != null) {
                        if (selectedName.equals(
                                json.get(TyreKey.vehicleMake)?.asString,
                                ignoreCase = true
                            )
                        ) {
                            if (json.get(TyreKey.chk1Make) != null) {
                                chkRF?.isChecked =
                                    if (json.get(TyreKey.chk1Make).asString
                                            .equals("LF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk2Make) != null) {
                                chkLR?.isChecked =
                                    if (json.get(TyreKey.chk2Make).asString
                                            .equals("RF,true")
                                    ) true else false
                            }
                            if (json.get(TyreKey.chk3Make) != null) {
                                chkRR?.isChecked =
                                    if (json.get(TyreKey.chk3Make).asString
                                            .equals("LR,true")
                                    ) true else false
                            }

                        }
                    }


                    Log.e("getsizee", "rr")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        //=========================================================================================================
        if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject) != null &&
            !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLFObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()
                Log.e("getobjjlf", "" + json + " " + selectedName)
                if (selectedName != null && !selectedName.equals("") &&
                    selectedName.equals(json.get("vehicleMake")?.asString!!)
                ) {
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

        if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject) != null &&
            !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreLRObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                Log.e("getobjj", "" + json + " " + selectedName)
                if (selectedName != null && !selectedName.equals("") &&
                    selectedName.equals(json.get("vehicleMake")?.asString!!)
                ) {
                    Log.e("getobjj", "" + json.get("vehicleMake")?.asString)
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

        if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject) != null &&
            !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRFObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                Log.e("getobjrf", "" + json + " ")
                Log.e("getobjrf", "" + selectedName + " ")
                Log.e("getobjrf", "" + json.get("vehicleMake")?.asString + " ")

                if (selectedName != null && !selectedName.equals("") &&
                    selectedName.equals(json.get("vehicleMake")?.asString!!)
                ) {

                    runOnUiThread {

                        if (chkRF?.text?.toString().equals("RF")) {
                            chkRF?.isChecked = true
                            Log.e("getobjrf", "0" + " ")
                        }
                        if (chkLR?.text?.toString().equals("RF")) {
                            chkLR?.isChecked = true
                            Log.e("getobjrf", "1" + " ")
                        }
                        if (chkRR?.text?.toString().equals("RF")) {
                            chkRR?.isChecked = true
                            Log.e("getobjrf", "2" + " ")
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        if (prefManager?.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject) != null &&
            !prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject).equals("")
        ) {
            var str = prefManager.getValue(TyreConfigClass.serviceId + TyreConfigClass.TyreRRObject)
            try {
                var json: JsonObject = JsonParser().parse(str).getAsJsonObject()

                if (selectedName != null && !selectedName.equals("") &&
                    selectedName.equals(json.get("vehicleMake")?.asString!!)
                ) {
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
                try {
                    gridviewRecycMake_?.layoutManager =
                        GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                    adapter = VehicleMakeAdapterNew(this, arrList, this, "")
                    gridviewRecycMake_?.adapter = adapter

                    Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)

                    Common.slideDown(gridviewRecycMake_!!, null)

                    Common.hideLoader()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateRecords() {

        if (selectedTyre.equals("LF")) {
            TyreConfigClass.LFVehicleMake = true
        } else if (selectedTyre.equals("LR")) {
            TyreConfigClass.LRVehicleMake = true
        } else if (selectedTyre.equals("RF")) {
            TyreConfigClass.RFVehicleMake = true
        } else if (selectedTyre.equals("RR")) {
            TyreConfigClass.RRVehicleMake = true
        }
        Log.e("pendingArr87979", "" + TyreConfigClass.LFVehicleMake)

        if (selectedTyre.equals("LF")) {
            chkRF?.text = "RF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"

            TyreConfigClass.RFVehicleMake = false
            TyreConfigClass.LRVehicleMake = false
            TyreConfigClass.RRVehicleMake = false

            if (chkRF?.isChecked!!) {
                TyreConfigClass.RFVehicleMake = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehicleMake = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleMake = true
            }


            Log.e("pendingArr8969", "" + TyreConfigClass.LFVehicleMake)
        } else if (selectedTyre.equals("RF")) {
            chkRF?.text = "LF"
            chkLR?.text = "LR"
            chkRR?.text = "RR"

            TyreConfigClass.LFVehicleMake = false
            TyreConfigClass.LRVehicleMake = false
            TyreConfigClass.RRVehicleMake = false

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleMake = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.LRVehicleMake = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleMake = true
            }


            Log.e("pendingArr67575", "" + TyreConfigClass.LFVehicleMake)

        } else if (selectedTyre.equals("LR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "RR"

            TyreConfigClass.LFVehicleMake = false
            TyreConfigClass.RFVehicleMake = false
            TyreConfigClass.RRVehicleMake = false

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleMake = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehicleMake = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.RRVehicleMake = true
            }


            Log.e("pendingArr0", "" + TyreConfigClass.LFVehicleMake)

        } else if (selectedTyre.equals("RR")) {
            chkRF?.text = "LF"
            chkLR?.text = "RF"
            chkRR?.text = "LR"

            TyreConfigClass.LFVehicleMake = false
            TyreConfigClass.RFVehicleMake = false
            TyreConfigClass.LRVehicleMake = false

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleMake = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehicleMake = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.LRVehicleMake = true
            }
        }

        TyreDetailCommonClass.tyreType = selectedTyre
        if (selectedPos != -1) {
            TyreDetailCommonClass.vehicleMake = arrList?.get(selectedPos)?.name
        }
        if (selectedPos != -1) {
            TyreDetailCommonClass.vehicleMakeId = arrList?.get(selectedPos)?.brand_id
        }
//        TyreConfigClass.selectedMakeURL = arrList?.get(selectedPos)?.concat!!
//        TyreDetailCommonClass.vehicleMakeURL = arrList?.get(selectedPos)?.concat!!

        TyreDetailCommonClass.chk1Make = chkRF?.text.toString() + "," + chkRF?.isChecked
        TyreDetailCommonClass.chk2Make = chkLR?.text.toString() + "," + chkLR?.isChecked
        TyreDetailCommonClass.chk3Make = chkRR?.text.toString() + "," + chkRR?.isChecked
        Log.e("pendingArr54654", "" + TyreDetailCommonClass.vehicleMake)
        Log.e("pendingArr54654", "" + TyreDetailCommonClass.vehicleMakeId)
        Log.e("pendingArr54654", "" + TyreDetailCommonClass.vehicleMakeURL)
        Log.e("getvalueee", "" + selectedTyre + " " + TyreConfigClass.RFVehicleMake)

        if (llRFView?.visibility == View.VISIBLE) {
            TyreDetailCommonClass.chk1MakeVisible = true
        } else {
            TyreDetailCommonClass.chk1MakeVisible = false
        }
        if (llLRView?.visibility == View.VISIBLE) {
            TyreDetailCommonClass.chk2MakeVisible = true
        } else {
            TyreDetailCommonClass.chk2MakeVisible = false
        }
        if (llRRView?.visibility == View.VISIBLE) {
            TyreDetailCommonClass.chk3MakeVisible = true
        } else {
            TyreDetailCommonClass.chk3MakeVisible = false
        }
        Log.e("getvisiblemake", "" + TyreDetailCommonClass.chk1MakeVisible)
        Log.e("getvisiblemake", "" + TyreDetailCommonClass.chk2MakeVisible)
        Log.e("getvisiblemake", "" + TyreDetailCommonClass.chk3MakeVisible)
        val handler=Handler()
        handler.postDelayed(Runnable {
            super.onResume()
            val intent = Intent(this, VehiclePatternActivity::class.java)
            intent.putExtra("selectedTyre", selectedTyre)
            if (TyreDetailCommonClass.vehicleMakeId.equals("")) {
                intent.putExtra("selectedMakeId", arrList?.get(selectedPos)?.brand_id)
            } else {
                intent.putExtra("selectedMakeId", TyreDetailCommonClass.vehicleMakeId)
            }
            startActivityForResult(intent, 1002)
        },1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getresult11t", "" + resultCode)
        when (resultCode) {
            1001 -> {
                setResult(1000)
                if (!this.isFinishing) {
                    finish()
                }
            }
            1002 -> {
                setResult(1000)
                if (!this.isFinishing) {
                    finish()
                }
            }

        }
    }

    private fun setData(json: JsonObject) {
        Log.e("getvalselected", "" + json)
        if (selectedTyre != null && !selectedTyre.equals("")) {
            TyreDetailCommonClass.tyreType = selectedTyre
        }
        if (json.get(TyreKey.vehicleMake) != null && !json.get(TyreKey.vehicleMake)?.asString.equals("")) {
            TyreDetailCommonClass.vehicleMake = json.get(TyreKey.vehicleMake)?.asString
        }
        if (json.get(TyreKey.vehicleMakeId) != null && !json.get(TyreKey.vehicleMakeId)?.asString.equals("")) {
            TyreDetailCommonClass.vehicleMakeId = json.get(TyreKey.vehicleMakeId)?.asString
        }
        if (json.get(TyreKey.vehicleMakeURL) != null && !json.get(TyreKey.vehicleMakeURL)?.asString.equals("")) {
            TyreDetailCommonClass.vehicleMakeURL = json.get(TyreKey.vehicleMakeURL)?.asString
        }

        if (json.get(TyreKey.vehiclePattern) != null && !json.get(TyreKey.vehiclePattern)?.asString.equals("")) {
            TyreDetailCommonClass.vehiclePattern = json.get(TyreKey.vehiclePattern)?.asString
        }
        if (json.get(TyreKey.vehiclePatternId) != null && !json.get(TyreKey.vehiclePatternId)?.asString.equals("")) {
            TyreDetailCommonClass.vehiclePatternId = json.get(TyreKey.vehiclePatternId)?.asString
        }
        if (json.get(TyreKey.vehicleSize) != null && !json.get(TyreKey.vehicleSize)?.asString.equals("")) {
            TyreDetailCommonClass.vehicleSize = json.get(TyreKey.vehicleSize)?.asString
        }
        if (json.get(TyreKey.vehicleSizeId) != null && !json.get(TyreKey.vehicleSizeId)?.asString.equals("")) {
            TyreDetailCommonClass.vehicleSizeId = json.get(TyreKey.vehicleSizeId)?.asString
        }

        if (json.get(TyreKey.manufaturingDate) != null && !json.get(TyreKey.manufaturingDate)?.asString.equals("")) {
            TyreDetailCommonClass.manufaturingDate = json.get(TyreKey.manufaturingDate)?.asString
        }
        if (json.get(TyreKey.psiInTyreService) != null && !json.get(TyreKey.psiInTyreService)?.asString.equals("")) {
            TyreDetailCommonClass.psiInTyreService = json.get(TyreKey.psiInTyreService)?.asString
        }
        if (json.get(TyreKey.psiOutTyreService) != null && !json.get(TyreKey.psiOutTyreService)?.asString.equals("")) {
            TyreDetailCommonClass.psiOutTyreService = json.get(TyreKey.psiOutTyreService)?.asString
        }
        if (json.get(TyreKey.weightTyreService) != null && !json.get(TyreKey.weightTyreService)?.asString.equals("")) {
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
        if (json.get(TyreKey.visualDetailPhotoUrl) != null && !json.get(TyreKey.visualDetailPhotoUrl)?.asString.equals("")) {
            TyreDetailCommonClass.visualDetailPhotoUrl =
                json.get(TyreKey.visualDetailPhotoUrl)?.asString
        }
        if (json.get(TyreKey.isCameraSelectedVisualDetail) != null) {
            TyreDetailCommonClass.isCameraSelectedVisualDetail =
                json.get(TyreKey.isCameraSelectedVisualDetail)?.asString?.toBoolean()!!
        }
        if (json.get(TyreKey.issueResolvedArr) != null) {
            val list: ArrayList<String>? = ArrayList()
            if (json.get(TyreKey.issueResolvedArr)?.asJsonArray?.size()!! > 0) {

                for (i in json.get(TyreKey.issueResolvedArr)?.asJsonArray!!) {
                    list?.add(i?.asString!!)
                }

            }
            TyreDetailCommonClass.issueResolvedArr = list
        }
        if (json.get(TyreKey.chk1Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk1Make = json.get(TyreKey.chk1Make)?.asString
        }
        if (json.get(TyreKey.chk1Pattern) != null && !json.get(TyreKey.chk1Pattern)?.asString.equals("")) {
            TyreDetailCommonClass.chk1Pattern = json.get(TyreKey.chk1Pattern)?.asString
        }
        if (json.get(TyreKey.chk1Size) != null && !json.get(TyreKey.chk1Size)?.asString.equals("")) {
            TyreDetailCommonClass.chk1Size = json.get(TyreKey.chk1Size)?.asString
        }
        if (json.get(TyreKey.chk2Make) != null && !json.get(TyreKey.chk2Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk2Make = json.get(TyreKey.chk2Make)?.asString
        }
        if (json.get(TyreKey.chk2Pattern) != null && !json.get(TyreKey.chk2Pattern)?.asString.equals("")) {
            TyreDetailCommonClass.chk2Pattern = json.get(TyreKey.chk2Pattern)?.asString
        }
        if (json.get(TyreKey.chk2Size) != null && !json.get(TyreKey.chk2Size)?.asString.equals("")) {
            TyreDetailCommonClass.chk2Size = json.get(TyreKey.chk2Size)?.asString
        }
        if (json.get(TyreKey.chk3Make) != null && !json.get(TyreKey.chk1Make)?.asString.equals("")) {
            TyreDetailCommonClass.chk3Make = json.get(TyreKey.chk3Make)?.asString
        }
        if (json.get(TyreKey.chk3Pattern) != null && !json.get(TyreKey.chk3Pattern)?.asString.equals("")) {
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

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }

    override fun onResume() {
        super.onResume()
    }

}