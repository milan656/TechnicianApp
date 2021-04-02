package com.walkins.technician.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.activity.CompletedServiceDetailActivity
import com.walkins.technician.adapter.ReportAdpater
import com.walkins.technician.common.onClickAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivFilterImg: ImageView? = null
    private var reportRecycView: RecyclerView? = null

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
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        init(view)
        return view
    }

    private fun init(view: View?) {
        reportRecycView = view?.findViewById(R.id.reportRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        ivBack = view?.findViewById(R.id.ivBack)
        ivFilterImg = view?.findViewById(R.id.ivFilterImg)
        ivFilterImg?.setOnClickListener(this)

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Your Report"

        ivBack?.visibility = View.GONE
        val arrayAdapter = context?.let { ReportAdpater(Common.commonPhotoChooseArr, it, this) }
        reportRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        /* reportRecycView?.addItemDecoration(
             DividerItemDecoration(
                 this,
                 DividerItemDecoration.VERTICAL
             )
         )*/
        reportRecycView?.adapter = arrayAdapter
        arrayAdapter?.onclick = this
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        var intent = Intent(context, CompletedServiceDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
//                onBackPressed()
            }
            R.id.ivFilterImg -> {
                openReportFilterDialogue("Choose Filter")
            }
        }
    }

    private fun openReportFilterDialogue(titleStr: String) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_report_filter, null)
        val dialog =
            context.let {
                it?.let { it1 ->
                    BottomSheetDialog(
                        it1,
                        R.style.CustomBottomSheetDialogTheme
                    )
                }
            }

        dialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
//        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setContentView(view)

        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        tvTitleText?.text = titleStr

        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog?.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }
}