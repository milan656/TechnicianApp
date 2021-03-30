package com.walkins.technician.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramotion.fluidslider.FluidSlider
import com.walkins.technician.R
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.adapter.HomeListAdpater
import com.walkins.technician.common.onClickAdapter
import io.apptik.widget.MultiSlider

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), onClickAdapter {
    private var param1: String? = null
    private var param2: String? = null

    private var arrayList = arrayListOf("Gallery", "Camera")
    private var vehicleMakeList = arrayListOf("")
    private var dummyvaluestart: String? = "0"
    private var dummyvalueend: String? = "100"

    private var homeRecycView: RecyclerView? = null
    private var adapter: HomeListAdpater? = null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        val multiSlider1 = view.findViewById<MultiSlider>(R.id.multiSlider1)
//        val starting_point = view.findViewById<TextView>(R.id.starting_point)
//        val last_end = view.findViewById<TextView>(R.id.last_end)
//        multiSlider1?.min = dummyvaluestart?.toInt()!!
//        multiSlider1?.max = dummyvalueend?.toInt()!!
//
//        multiSlider1.setNumberOfThumbs(2)
//        multiSlider1.setOnThumbValueChangeListener(object : MultiSlider.SimpleChangeListener() {
//            override fun onValueChanged(
//                multiSlider: MultiSlider,
//                thumb: MultiSlider.Thumb,
//                thumbIndex: Int,
//                value: Int
//            ) {
//                if (thumbIndex == 0) {
//                    starting_point!!.text = value.toString()
//                    dummyvaluestart = "" + value
//                } else {
//
//                    dummyvalueend = "" + value
//
//                    if (value == 100) {
//                        last_end!!.text = "100"
//                    } else {
//                        last_end!!.text = value.toString()
//                    }
//                }
//            }
//        })
//
        // Kotlin
        val max = 45
        val min = 10
        val total = max - min

        val slider = view.findViewById<FluidSlider>(R.id.multiSlider1)
        slider.positionListener = { pos ->
            slider.bubbleText = "${min + (total * pos).toInt()}"
            Log.e("getvaluess", "" + pos)
        }
        slider.position = 0.3f
        slider.startText = "$min"
        slider.endText = "$max"
        slider.animation?.cancel()

        homeRecycView = view.findViewById(R.id.homeRecycView)
        homeRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        adapter = context?.let { HomeListAdpater(arrayList, it, this) }
        homeRecycView?.adapter = adapter
        adapter?.onclick = this

// Java


        return view

    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        val dialog =
            getContext()?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btn_send)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val dialogueRecycView = view.findViewById<RecyclerView>(R.id.dialogueRecycView)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        var arrayAdapter = context?.let { DialogueAdpater(array, it, this) }
        dialogueRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        dialogueRecycView.addItemDecoration(
            DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        dialogueRecycView.adapter = arrayAdapter
        arrayAdapter?.onclick = this

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }
        if (btnBg.equals(Common.btn_filled, ignoreCase = true)) {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_yellow))
            btnSend.setTextColor(context?.resources?.getColor(R.color.white)!!)
            btnSend?.text = "Submit"
        } else {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_white))
            btnSend.setTextColor(context?.resources?.getColor(R.color.header_title)!!)
            btnSend?.text = "Cancel"
        }


        btnSend.setOnClickListener {

            dialog?.dismiss()

        }

        dialog?.show()

    }

    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog =
            getContext()?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }
        if (isBtnVisible) {
            btnSend.visibility = View.VISIBLE
        } else {
            btnSend.visibility = View.GONE
        }
        if (btnBg.equals(Common.btn_filled, ignoreCase = true)) {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_yellow))
            btnSend.setTextColor(context?.resources?.getColor(R.color.white)!!)
            btnSend?.text = "Submit"
        } else {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_white))
            btnSend.setTextColor(context?.resources?.getColor(R.color.header_title)!!)
            btnSend?.text = "Cancel"
        }

        btnSend.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.show()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 1) {

            showBottomSheetdialogNormal(
                arrayList,
                "Address Details",
                context,
                Common.btn_filled,
                false
            )

        }
        Log.e("getclickpos", arrayList.get(variable))
    }
}