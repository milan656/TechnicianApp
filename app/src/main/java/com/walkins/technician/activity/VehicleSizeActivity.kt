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
import com.walkins.technician.DB.DBClass
import com.walkins.technician.DB.VehiclePatternModelClass
import com.walkins.technician.DB.VehicleSizeModelClass
import com.walkins.technician.R
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
    private var ivEditVehicleMake: ImageView? = null
    private var chkRR: CheckBox? = null
    private var chkRF: CheckBox? = null
    private var chkLR: CheckBox? = null
    private var selectedPos = -1
    private var selectedId = -1
    private var selectedTyre = ""

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
        }

        ivEditVehicleMake = findViewById(R.id.ivEditVehicleMake)
//        getVehicleMake()
        ivEditVehicleMake?.setOnClickListener(this)
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

        var thread = Thread {

            Log.e("getsizee", "" + mDb.daoClass().getAllVehicleType().size)
            if (mDb.sizeDaoClass().getAllSize() != null && mDb.sizeDaoClass()
                    .getAllSize().size > 0
            ) {
                arrList?.addAll(mDb.sizeDaoClass().getAllSize())
                for (i in arrList?.indices!!) {

                    Log.e("getpattersize", "" + arrList?.get(i)?.sizeId)
                    Log.e("getpattersize", "" + arrList?.get(i)?.name)
                }

                /*if (selectedTyre.equals("LF")) {

                    if (mDb.daoLF().getAll().size > 0) {
                        for (i in mDb.daoLF().getAll().indices) {
                            Log.e("getdetailss", "" + mDb.daoLF().getAll().get(i).vehicleSizeId)
                            selectedId = mDb.daoLF().getAll().get(i).vehicleSizeId?.toInt()!!
                        }
                    }
                } else if (selectedTyre.equals("LR")) {
                    if (mDb.daoLR().getAll().size > 0) {
                        for (i in mDb.daoLR().getAll().indices) {
                            Log.e("getdetailss", "" + mDb.daoLR().getAll().get(i).vehicleSize)
                            Log.e("getdetailss", "" + mDb.daoLR().getAll().get(i).vehicleSizeId)
                            selectedId = mDb.daoLR().getAll().get(i).vehicleSizeId?.toInt()!!
                        }
                    }

                } else if (selectedTyre.equals("RF")) {
                    if (mDb.daoRF().getAll().size > 0) {
                        for (i in mDb.daoRF().getAll().indices) {
                            Log.e("getdetailss", "" + mDb.daoRF().getAll().get(i).vehicleSize)
                            Log.e("getdetailss", "" + mDb.daoRF().getAll().get(i).vehicleSizeId)
                            selectedId = mDb.daoRF().getAll().get(i).vehicleSizeId?.toInt()!!
                        }
                    }

                } else if (selectedTyre.equals("RR")) {
                    if (mDb.daoRR().getAll().size > 0) {
                        for (i in mDb.daoRR().getAll().indices) {
                            Log.e("getdetailss", "" + mDb.daoRR().getAll().get(i).vehicleSize)
                            Log.e("getdetailss", "" + mDb.daoRR().getAll().get(i).vehicleSizeId)
                            selectedId = mDb.daoRR().getAll().get(i).vehicleSizeId?.toInt()!!
                        }
                    }

                }
*/
            }

        }
        thread.start()

        var handler = Handler()
        handler.postDelayed(Runnable {
            adapter = VehicleSizeAdapter(this, arrList, this, selectedId)
            gridviewRecycModel?.adapter = adapter

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
    }

    override fun onPositionClick(variable: Int, check: Int) {

//        Log.e("getmake", "" + arrList?.get(variable)?.name)
//        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
//        intent.putExtra("which", "vehiclesize")
//        startActivityForResult(intent, 1005)

        Common.slideUp(gridviewRecycModel!!)
        Common.slideDown(llVehicleMakeselectedView!!, btnNext!!)

        tvSelectedModel?.text = arrList?.get(variable)?.name
        selectedPos = variable
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
                Common.slideDown(gridviewRecycModel!!, null)
            }

        }
    }

    private fun updateRecords() {
        var thread = Thread {
            var entity = VehicleSizeModelClass()
            entity.Id = arrList?.get(selectedPos)?.Id!!
            entity.name = arrList?.get(selectedPos)?.name
            entity.isSelected = true
            entity.isLRSelected = chkLR?.isChecked!!
            entity.isRFSelected = chkRF?.isChecked!!
            entity.isRRSelected = chkRR?.isChecked!!
            mDb.sizeDaoClass().update(entity)

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

        if (chkRF?.isChecked!!) {
            TyreDetailCommonClass.chk1Size = chkRF?.text.toString()
        } else {
            TyreDetailCommonClass.chk1Size = chkRF?.text.toString() + " " + chkRF?.isChecked
        }
        if (chkLR?.isChecked!!) {
            TyreDetailCommonClass.chk2Size = chkLR?.text.toString()
        } else {
            TyreDetailCommonClass.chk2Size = chkLR?.text.toString() + " " + chkLR?.isChecked
        }
        if (chkRR?.isChecked!!) {
            TyreDetailCommonClass.chk3Size = chkRR?.text.toString()
        } else {
            TyreDetailCommonClass.chk3Size = chkRR?.text.toString() + " " + chkRR?.isChecked
        }

        Log.e("getvalueee22", "" + selectedTyre + " " + TyreConfigClass.RFVehicleSize)
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