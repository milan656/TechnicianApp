package com.walkins.technician.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.jkadvantage.model.vehicleBrandModel.Data
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.R
import com.walkins.technician.adapter.VehicleMakeAdapterNew
import com.walkins.technician.common.SpacesItemDecoration
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_make)
        prefManager = PrefManager(this)
        warrantyViewModel = ViewModelProviders.of(this).get(WarrantyViewModel::class.java)

        init()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)

        gridviewRecycMake_ = findViewById(R.id.gridviewRecycMake_)
        getVehicleMake()

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Select Tyre Make - LF"

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

        Log.e("getmake", "" + arrList?.get(variable)?.name)
        val intent = Intent(this, VehicleMakeApplyTyreActivty::class.java)
        intent.putExtra("which", "vehiclemake")
        startActivityForResult(intent,1001)

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            1001 -> {
                setResult(1000)
                finish()
            }

        }
    }
}