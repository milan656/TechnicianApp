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
import com.jkadvantage.model.vehicleBrandModel.Data
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleModelAdapter
import com.walkins.technician.common.SpacesItemDecoration
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showLongToast
import com.walkins.technician.viewmodel.WarrantyViewModel

class VehiclePatternActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener {

    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehicleModelAdapter? = null
    private var gridviewRecycModel: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    var arrList: ArrayList<Data>? = ArrayList()
    private lateinit var mDb: DBClass
    private var btnNext: Button? = null
    private var llVehicleMakeselectedView: LinearLayout? = null

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

        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        tvTitle?.text = "Select Tyre Pattern - " + TyreConfigClass.selectedTyreConfigType

//        getVehicleMake()

        gridviewRecycModel?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        gridviewRecycModel?.addItemDecoration(
            SpacesItemDecoration(
                20
            )
        )

        adapter = VehicleModelAdapter(this, arrList, this)
        gridviewRecycModel?.adapter = adapter
        gridviewRecycModel?.visibility = View.GONE

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
                        mDb.daoClass().getAllVehicleType().get(i).vehicle_type
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
        prefManager.getAccessToken()?.let {
            warrantyViewModel.getVehicleBrandModel(
                "6cdb5eb6-fd92-4bf9-bc09-cf28c11b550c",
                it, this@VehiclePatternActivity

            )
        }

        warrantyViewModel.getVehicleBrand()
            ?.observe(this@VehiclePatternActivity, androidx.lifecycle.Observer {
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