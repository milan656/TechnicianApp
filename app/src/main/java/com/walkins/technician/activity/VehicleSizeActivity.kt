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
import com.walkins.technician.DB.DBClass
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleSizeAdapter
import com.walkins.technician.common.SpacesItemDecoration
import com.walkins.technician.common.TyreConfigClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showLongToast
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
    var arrList: ArrayList<SizeData>? = ArrayList()
    private lateinit var mDb: DBClass
    private var llVehicleMakeselectedView: LinearLayout? = null
    private var btnNext: Button? = null
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
        btnNext = findViewById(R.id.btnNext)
        llVehicleMakeselectedView = findViewById(R.id.llVehicleMakeselectedView)

        gridviewRecycModel = findViewById(R.id.gridviewRecycModel)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        tvSelectedModel = findViewById(R.id.tvSelectedModel)
        ivBack?.setOnClickListener(this)
        btnNext?.setOnClickListener(this)
        tvTitle?.text = "Select Tyre Size - " + TyreConfigClass.selectedTyreConfigType
        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)
//        getVehicleMake()
        ivEditVehicleMake?.setOnClickListener(this)
        gridviewRecycModel?.layoutManager =
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        gridviewRecycModel?.addItemDecoration(
            SpacesItemDecoration(
                20
            )
        )

        adapter = VehicleSizeAdapter(this, arrList, this)
        gridviewRecycModel?.adapter = adapter

//        getVehicleMake()

        var thread = Thread {

            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size)
            if (mDb.sizeDaoClass().getAllSize() != null && mDb.sizeDaoClass()
                    .getAllSize().size > 0
            ) {
                for (i in mDb.sizeDaoClass().getAllSize().indices) {
                    var data = SizeData(
                        mDb.sizeDaoClass().getAllSize().get(i).sizeId,
                        mDb.sizeDaoClass().getAllSize().get(i).name,
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

        warrantyViewModel.getVehicleSize(460, 41, this)

        warrantyViewModel.getVehicleSize()
            ?.observe(this@VehicleSizeActivity, androidx.lifecycle.Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        sizeModel = it
                        Log.e("getmodel00::", "" + sizeModel)

                        for (i in it.data?.indices!!) {
                            if (!it.data.get(i).name.equals("Other", ignoreCase = true)) {
                                arrList?.add(it.data.get(i))
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
    }

    override fun onPositionClick(variable: Int, check: Int) {

//        Log.e("getmake", "" + arrList?.get(variable)?.name)
//        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
//        intent.putExtra("which", "vehiclesize")
//        startActivityForResult(intent, 1005)

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
                val intent = Intent(this, VisualDetailsActivity::class.java)
//                intent.putExtra("which", "vehiclesize")
                startActivityForResult(intent, 1005)
            }
            R.id.ivEditVehicleMake -> {
                Common.slideUp(llVehicleMakeselectedView!!, btnNext!!)
                Common.slideDown(gridviewRecycModel!!, null)
            }

        }
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