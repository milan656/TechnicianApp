package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehiclePatternAdapter
import com.walkins.technician.common.SpacesItemDecoration
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showLongToast
import com.walkins.technician.model.login.patternmodel.PatternData
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.viewmodel.WarrantyViewModel

class VehiclePatternActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private var patternModel: PatternModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehiclePatternAdapter? = null
    private var gridviewRecycModel: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    var arrList: ArrayList<PatternData>? = ArrayList()
    private lateinit var mDb: DBClass
    private var btnNext: Button? = null
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var tvSelectedModel: TextView? = null
    private var ivEditVehicleMake: ImageView? = null

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
        llVehicleMakeselectedView = findViewById(R.id.llVehicleMakeselectedView)
        tvSelectedModel = findViewById(R.id.tvSelectedModel)
        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)

        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        ivEditVehicleMake?.setOnClickListener(this)
        tvTitle?.text = "Select Tyre Pattern - " + TyreConfigClass.selectedTyreConfigType


        gridviewRecycModel?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        gridviewRecycModel?.addItemDecoration(
            SpacesItemDecoration(
                20
            )
        )

        adapter = VehiclePatternAdapter(this, arrList, this)
        gridviewRecycModel?.adapter = adapter

//        getVehicleMake()

        var thread = Thread {

            Log.e("getsizee", "" + mDb.patternDaoClass().getAllPattern().size)
            if (mDb.patternDaoClass().getAllPattern() != null && mDb.patternDaoClass()
                    .getAllPattern().size > 0
            ) {
                for (i in mDb.patternDaoClass().getAllPattern().indices) {
                    var data = PatternData(
                        mDb.patternDaoClass().getAllPattern().get(i).patternId,
                        mDb.patternDaoClass().getAllPattern().get(i).name,
                        false,
                        false,
                        false,
                        false
                    )

                    arrList?.add(data)
                }

            }

        }
        thread.start()

        var handler = Handler()
        handler.postDelayed(Runnable {
            adapter?.notifyDataSetChanged()
            gridviewRecycModel?.visibility = View.VISIBLE
        }, 1000)
    }

    fun getVehicleMake() {
        Common.showLoader(this)

        warrantyViewModel.getVehiclePattern(3, this)

        warrantyViewModel.getVehiclePattern()
            ?.observe(this@VehiclePatternActivity, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        patternModel = it
                        Log.e("getmodel00::", "" + patternModel)

                        for (i in it.data?.indices!!) {
                            if (!it.data.get(i).name.equals("Other", ignoreCase = true)) {
                                arrList?.add(it.data.get(i))
                            }
                        }
                        adapter?.notifyDataSetChanged()

                        if (arrList?.size!! > 0) {
                            gridviewRecycModel?.visibility = View.VISIBLE
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

    override fun onPositionClick(variable: Int, check: Int) {

//        Log.e("getmake", "" + arrList?.get(variable)?.name)
//        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
//        intent.putExtra("which", "vehiclepattern")
//        startActivityForResult(intent, 1006)

        Common.slideUp(gridviewRecycModel!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        tvSelectedModel?.text = arrList?.get(variable)?.name


    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnNext -> {
                var intent = Intent(this, VehicleSizeActivity::class.java)
                startActivityForResult(intent, 1003)
            }
            R.id.ivEditVehicleMake -> {
                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                Common.slideDown(gridviewRecycModel!!, null)
            }

        }
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
}