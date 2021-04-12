package com.walkins.technician.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.showLongToast
import com.example.technician.common.PrefManager
import com.jkadvantage.model.vehicleBrandModel.Data
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.R
import com.walkins.technician.activity.LoginActivity
import com.walkins.technician.activity.VehicleSizeActivity
import com.walkins.technician.adapter.VehicleMakeAdapterNew
import com.walkins.technician.common.SpacesItemDecoration
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.viewmodel.WarrantyViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VehicleMakeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VehicleMakeFragment : Fragment(), onClickAdapter {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var prefManager: PrefManager
    private var vehicleBrandModel: VehicleBrandModel? = null
    private lateinit var warrantyViewModel: WarrantyViewModel
    private var adapter: VehicleMakeAdapterNew? = null
    private var gridviewRecycMake_: RecyclerView? = null
    var arrList: ArrayList<Data>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_vehicle_make, container, false)
        prefManager = context?.let { PrefManager(it) }!!
        warrantyViewModel = ViewModelProviders.of(this).get(WarrantyViewModel::class.java)
        gridviewRecycMake_ = view?.findViewById(R.id.gridviewRecycMake_)
        getVehicleMake()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VehicleMakeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VehicleMakeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getVehicleMake() {
        prefManager.getAccessToken()?.let {
            context?.let { it1 ->
                warrantyViewModel.getVehicleBrandModel(
                    "6cdb5eb6-fd92-4bf9-bc09-cf28c11b550c",
                    it, it1

                )
            }
        }

        warrantyViewModel.getVehicleBrand()
            ?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                            GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
                        gridviewRecycMake_?.addItemDecoration(
                            SpacesItemDecoration(
                                20
                            )
                        )

//                        adapter = context?.let { it1 -> VehicleMakeAdapterNew(it1, arrList, this) }
//                        gridviewRecycMake_?.adapter = adapter


                    } else {
                        if (it.error != null && it.error.size > 0) {
                            if (it.error.get(0).statusCode != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager.clearAll()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                } else {
                                    context?.let { it1 ->
                                        Common.showShortToast(
                                            it.error.get(0).message,
                                            it1
                                        )
                                    }
                                }

                            } else {
                                context?.let { it1 ->
                                    Common.showShortToast(
                                        it.error.get(0).message,
                                        it1
                                    )
                                }
                            }
                        }
                    }
                } else {
                    context?.let { it1 -> showLongToast("Something Went Wrong", it1) }
                }
            })
    }

    override fun onPositionClick(variable: Int, check: Int) {

        Log.e("getmake", "" + arrList?.get(variable)?.name)
        val intent = Intent(context, VehicleSizeActivity::class.java)
        startActivity(intent)

    }

}

