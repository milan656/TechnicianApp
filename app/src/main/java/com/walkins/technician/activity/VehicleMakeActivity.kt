package com.walkins.technician.activity

import android.content.Context
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
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.DB.TyreRRDetail
import com.walkins.technician.DB.VehicleMakeModelClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleMakeAdapterNew
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.viewmodel.WarrantyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private var selectedPos = -1

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

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
            if (intent.getStringExtra("selectedTyre") != null) {
                selectedTyre = intent.getStringExtra("selectedTyre")!!
            }
        }
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

        adapter = VehicleMakeAdapterNew(this, arrList, this)
        gridviewRecycMake_?.adapter = adapter
        gridviewRecycMake_?.visibility = View.GONE

        var thread = Thread {
            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size)
            if (mDb.daoClass().getAllVehicleType() != null && mDb.daoClass()
                    .getAllVehicleType().size > 0
            ) {

                arrList?.addAll(mDb.daoClass().getAllVehicleType())
            }

        }
        thread.start()

        var handler = Handler()
        handler.postDelayed(Runnable {
            Log.e("getsizee00", "" + arrList?.size)
            adapter?.notifyDataSetChanged()
            gridviewRecycMake_?.visibility = View.VISIBLE
        }, 1000)
    }


    override fun onPositionClick(variable: Int, check: Int) {


        var arr = listOf("one", 1, "two", 2, 10)
        var filter =

            Common.slideUp(gridviewRecycMake_!!)

        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        try {
            Glide.with(this)
                .load(arrList?.get(variable)?.concat)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(ivSelectedCar!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        selectedPos = variable
        Log.e("getselected", "" + arrList?.get(selectedPos)?.Id)

        chkRF?.isChecked = arrList?.get(variable)?.isRFSelected!!
        chkLR?.isChecked = arrList?.get(variable)?.isLRSelected!!
        chkRR?.isChecked = arrList?.get(variable)?.isRRSelected!!

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
                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)

                Common.slideDown(gridviewRecycMake_!!, null)

            }
        }
    }

    private fun updateRecords() {
        var thread = Thread {
            var entity = VehicleMakeModelClass()
            entity.Id = arrList?.get(selectedPos)?.Id!!
            entity.name = arrList?.get(selectedPos)?.name
            entity.isSelected = true
            entity.isLRSelected = chkLR?.isChecked!!
            entity.brand_id = arrList?.get(selectedPos)?.brand_id
            entity.vehicle_type = arrList?.get(selectedPos)?.vehicle_type
            entity.short_number = arrList?.get(selectedPos)?.short_number
            entity.concat = arrList?.get(selectedPos)?.concat
            entity.image_url = arrList?.get(selectedPos)?.image_url
            entity.quality = arrList?.get(selectedPos)?.quality
            entity.isRFSelected = chkRF?.isChecked!!
            entity.isRRSelected = chkRR?.isChecked!!

            mDb.daoClass().update(entity)

        }
        thread.start()

        TyreConfigClass.selectedMakeURL = arrList?.get(selectedPos)?.concat!!

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

            if (chkRF?.isChecked!!) {
                TyreConfigClass.LFVehicleMake = true
            }
            if (chkLR?.isChecked!!) {
                TyreConfigClass.RFVehicleMake = true
            }
            if (chkRR?.isChecked!!) {
                TyreConfigClass.LRVehicleMake = true
            }
            Log.e("pendingArr54654", "" + TyreConfigClass.LFVehicleMake)
        }

        TyreDetailCommonClass.tyreType = selectedTyre
        TyreDetailCommonClass.vehicleMake = arrList?.get(selectedPos)?.concat
        TyreDetailCommonClass.vehicleMakeId = arrList?.get(selectedPos)?.Id?.toString()


        Log.e("getvalueee", "" + selectedTyre + " " + TyreConfigClass.RFVehicleMake)
        var intent = Intent(this, VehiclePatternActivity::class.java)
        intent.putExtra("selectedTyre", selectedTyre)
        intent.putExtra("selectedVehicleMake", "" + arrList?.get(selectedPos)?.Id)
        startActivityForResult(intent, 1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("getresult11t", "" + resultCode)
        when (resultCode) {
            1001 -> {
                setResult(1000)
                finish()
            }
            1002 -> {
                setResult(1000)
                finish()
            }

        }
    }
}