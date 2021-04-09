package com.walkins.technician.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.jkadvantage.model.vehicleBrandModel.Data
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleMakeAdapterNew
import com.walkins.technician.common.SpacesItemDecoration
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showLongToast
import com.walkins.technician.viewmodel.WarrantyViewModel

class VehicleMakeActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {
    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehicleMakeAdapterNew? = null
    private var gridviewRecycMake_: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    var arrList: ArrayList<Data>? = ArrayList()
    private lateinit var mDb: DBClass
    private var tyreConfigType: String = ""
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var btnNext: Button? = null
    private var ivSelectedCar: ImageView? = null
    private var ivEditVehicleMake: ImageView? = null

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

        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        ivEditVehicleMake?.setOnClickListener(this)

        if (intent != null) {
            if (intent.getStringExtra("title") != null) {
                tvTitle?.text = intent.getStringExtra("title")
            }
            if (intent.getStringExtra("tyreConfigType") != null) {
                TyreConfigClass.selectedTyreConfigType = intent.getStringExtra("tyreConfigType")!!
            }
        }
//        tvTitle?.text = "Select Tyre Make - LF"
        gridviewRecycMake_?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        gridviewRecycMake_?.addItemDecoration(
            SpacesItemDecoration(
                20
            )
        )
        Log.e("getsizee", "" + arrList?.size)

        adapter = VehicleMakeAdapterNew(this, arrList, this)
        gridviewRecycMake_?.adapter = adapter
        gridviewRecycMake_?.visibility = View.GONE

        var thread = Thread {

            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size)
            if (mDb.daoClass().getAllVehicleType() != null && mDb.daoClass()
                    .getAllVehicleType().size > 0
            ) {
                for (i in mDb.daoClass().getAllVehicleType().indices) {
                    var data = Data(
                        mDb.daoClass().getAllVehicleType().get(i).brand_id,
                        mDb.daoClass().getAllVehicleType().get(i).image_url,
                        mDb.daoClass().getAllVehicleType().get(i).name,
                        mDb.daoClass().getAllVehicleType().get(i).short_number,
                        false,
                        mDb.daoClass().getAllVehicleType().get(i).quality,
                        mDb.daoClass().getAllVehicleType().get(i).vehicle_type,
                        mDb.daoClass().getAllVehicleType().get(i).concat
                    )

                    arrList?.add(data)
                }

            }

        }
        thread.start()

        var handler = Handler()
        handler.postDelayed(Runnable {
            adapter?.notifyDataSetChanged()
            gridviewRecycMake_?.visibility = View.VISIBLE
        }, 1000)
    }

    fun getVehicleMake() {
        Common.showLoader(this)
        prefManager.getAccessToken()?.let {
            warrantyViewModel.getVehicleBrandModel(
                "6cdb5eb6-fd92-4bf9-bc09-cf28c11b550c",
                it, this@VehicleMakeActivity

            )
        }

        warrantyViewModel.getVehicleBrand()
            ?.observe(this@VehicleMakeActivity, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        vehicleBrandModel = it
                        Log.e("getmodel00::", "" + vehicleBrandModel)

                        for (i in it.data?.indices!!) {
                            if (!it.data.get(i).name.equals("Other", ignoreCase = true)) {
                                arrList?.add(it.data.get(i))
                            }
                        }

                        gridviewRecycMake_?.layoutManager =
                            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
                        gridviewRecycMake_?.addItemDecoration(
                            SpacesItemDecoration(
                                20
                            )
                        )

                        adapter = VehicleMakeAdapterNew(this, arrList, this)
                        gridviewRecycMake_?.adapter = adapter


                    } else {
                        if (it.error != null && it.error.size > 0) {
                            if (it.error.get(0).statusCode != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager.clearAll()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    this@VehicleMakeActivity.let { it1 ->
                                        Common.showShortToast(
                                            it.error.get(0).message,
                                            it1
                                        )
                                    }
                                }

                            } else {
                                this@VehicleMakeActivity.let { it1 ->
                                    Common.showShortToast(
                                        it.error.get(0).message,
                                        it1
                                    )
                                }
                            }
                        }
                    }
                } else {
                    showLongToast("Something Went Wrong", this@VehicleMakeActivity)
                }
            })
    }

    override fun onPositionClick(variable: Int, check: Int) {

        /*Log.e("getmake", "" + arrList?.get(variable)?.name)
        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
        intent.putExtra("which", "vehiclemake")

        startActivityForResult(intent, 1001)*/

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

/*
        var model = arrList?.get(variable)
        var entity = VehicleMakeModelClass()

        entity.vehicle_type =
            model?.vehicle_type!!
        entity.name = model.name
        entity.image_url = model.image_url
        entity.quality = model.quality
        entity.brand_id = model.brand_id
        entity.isSelected = false
        entity.short_number = model.short_number
        mDb.daoClass().update(entity)
*/

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnNext -> {
                var intent = Intent(this, VehiclePatternActivity::class.java)
                startActivityForResult(intent, 1002)
            }
            R.id.ivEditVehicleMake -> {
                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)

                Common.slideDown(gridviewRecycMake_!!, null)

            }
        }
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