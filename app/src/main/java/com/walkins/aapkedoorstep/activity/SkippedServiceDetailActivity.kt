package com.walkins.aapkedoorstep.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.TyreSuggestionAdpater
import com.walkins.aapkedoorstep.common.TyreConfigClass
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.common.showShortToast
import com.walkins.aapkedoorstep.custom.BoldButton
import com.walkins.aapkedoorstep.model.login.IssueResolveModel
import com.walkins.aapkedoorstep.model.login.comment.CommentListData
import com.walkins.aapkedoorstep.model.login.servicemodel.servicedata.ServiceDataByIdModel
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.viewmodel.ServiceViewModel
import com.walkins.aapkedoorstep.viewmodel.common.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.common.CommonViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class SkippedServiceDetailActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var skipList: ArrayList<IssueResolveModel>? = null
    private var commentList: ArrayList<CommentListData>? = ArrayList()
    private var serviceViewModel: ServiceViewModel? = null
    private var serviceDateByIdModel: ServiceDataByIdModel? = null
    private var tvChange: TextView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private lateinit var commonRepo: CommonRepo
    private lateinit var commonViewModelFactory: CommonViewModelFactory
    private var commonViewModel: CommonViewModel? = null

    private lateinit var prefManager: PrefManager
    private var color: String = ""
    private var colorCode: String = ""
    private var makeModel: String = ""
    private var regNumber: String = ""
    private var carImage: String = ""
    private var uuid: String = ""
    private var address: String = ""
    private var ischange: String = ""
    private var formatedDate: String = ""
    private var comment_id: String = ""
    private var servicelist: String = ""
    private var which: String = ""
    private var reasonId: String = ""
    private var comment_id_by_service = -1

    private var tvCurrentDateTime: TextView? = null

    private var tvRegNumber: TextView? = null
    private var tvMakeModel: TextView? = null
    private var tvReason: TextView? = null
    private var tvcolor: TextView? = null
    private var llbg: LinearLayout? = null
    private var ivCarImage: ImageView? = null
    private var ivInfoAddService: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        commonRepo = CommonRepo()
        commonViewModelFactory = CommonViewModelFactory(commonRepo)
        commonViewModel = ViewModelProvider(this, commonViewModelFactory).get(CommonViewModel::class.java)

        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        prefManager = PrefManager(this)
        init()


    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        tvChange = findViewById(R.id.tvChange)
        tvReason = findViewById(R.id.tvReason)
        ivBack = findViewById(R.id.ivBack)
        ivInfoAddService = findViewById(R.id.ivInfoAddService)

        tvcolor = findViewById(R.id.tvColor)
        tvCurrentDateTime = findViewById(R.id.tvCurrentDateTime)
        tvMakeModel = findViewById(R.id.tvMakeModel)
        llbg = findViewById(R.id.llColor)
        tvRegNumber = findViewById(R.id.tvRegNumber)
        ivCarImage = findViewById(R.id.ivCarImage)

        ivBack?.setOnClickListener(this)
        ivInfoAddService?.setOnClickListener(this)
        tvTitle?.text = "Service Details"

        if (intent != null) {
            if (intent.getStringExtra("color") != null) {
                color = intent.getStringExtra("color")!!
            }
            if (intent.getStringExtra("makeModel") != null) {
                makeModel = intent.getStringExtra("makeModel")!!
            }
            if (intent.getStringExtra("regNumber") != null) {
                regNumber = intent.getStringExtra("regNumber")!!
            }
            if (intent.getStringExtra("carImage") != null) {
                carImage = intent.getStringExtra("carImage")!!
            }
            if (intent.getStringExtra("uuid") != null) {
                uuid = intent.getStringExtra("uuid")!!
            }
            if (intent.getStringExtra("colorcode") != null) {
                colorCode = intent.getStringExtra("colorcode")!!
            }
            if (intent.getStringExtra("reason") != null) {
//                colorCode = intent.getStringExtra("reason")!!
                tvReason?.text = intent.getStringExtra("reason")!!
            }
            if (intent.getStringExtra("reasonId") != null) {
                reasonId = intent.getStringExtra("reasonId")!!
            }
            if (intent.getStringExtra("address") != null) {
//                colorCode = intent.getStringExtra("reason")!!
                address = intent.getStringExtra("address")!!
            }
            if (intent.getStringExtra("ischange") != null) {
                ischange = intent.getStringExtra("ischange")!!
            }
            if (intent.getStringExtra("formatedDate") != null) {
                formatedDate = intent.getStringExtra("formatedDate")!!
            }
            if (intent.getStringExtra("comment_id") != null) {
                comment_id = intent.getStringExtra("comment_id")!!
            }
            if (intent.getStringExtra("servicelist") != null) {
                servicelist = intent.getStringExtra("servicelist")!!
            }
            if (intent.getStringExtra("which") != null) {
                which = intent.getStringExtra("which")!!
            }

        }

        Log.e("getregno", "" + regNumber)
        Log.e("getregno", "" + which)
        tvcolor?.text = color
        tvMakeModel?.text = makeModel
        tvRegNumber?.text = regNumber

        llbg?.setBackgroundColor(Color.parseColor(colorCode))

        try {
            Glide.with(this)
                .load(carImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_no_car_image)
                .into(ivCarImage!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tvChange?.setOnClickListener(this)

        if (!formatedDate.equals("")) {

            formatedDate = Common.addHour(formatedDate, 5, 30)!!
            Log.e("getdated0", "" + formatedDate)
            Log.e("getdated0", "" + Common.datefrom(formatedDate))
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val output = SimpleDateFormat("hh:mm aa, dd MMMM yyyy")

            var d: Date? = null
            try {
                d = input.parse(formatedDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formatted: String = output.format(d)
            Log.e("DATE", "" + formatted)
            try {
                tvCurrentDateTime?.text = formatted
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            tvCurrentDateTime?.text = Common.getCurrentDateTime()
        }

        if (servicelist != "" && servicelist == "true") {
            getServiceDataById()
        } else {
            if (ischange == "false") {
                tvChange?.visibility = View.GONE
                getCommentList(comment_id)
            }
        }

        tvChange?.visibility = View.VISIBLE
    }

    private fun getCommentList(): ArrayList<CommentListData> {
        if (prefManager.getCommentList(TyreConfigClass.commentList) != null && prefManager.getCommentList(TyreConfigClass.commentList).size > 0) {
            commentList?.clear()
            for (i in prefManager.getCommentList(TyreConfigClass.commentList).indices) {
                val model = CommentListData(
                    prefManager.getServiceList(TyreConfigClass.commentList)[i].id,
                    prefManager.getServiceList(TyreConfigClass.commentList)[i].name
                )
                commentList?.add(model)
            }
        }
        return commentList!!
    }

    private fun openSkipServiceDialogue(titleStr: String, stringExtra: String?, s: String) {
        val builder = AlertDialog.Builder(this).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder.window?.setLayout(width, height)
        builder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val root =
            LayoutInflater.from(this).inflate(R.layout.dialogue_service_skip, null)

        val btnConfirm = root.findViewById<BoldButton>(R.id.btnConfirm)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val pendingReasonRecycView = root.findViewById<RecyclerView>(R.id.pendingReasonRecycView)

        try {
            skipList = arrayListOf<IssueResolveModel>()

            if (commentList?.size!! > 0) {
                for (i in commentList?.indices!!) {
                    skipList?.add(IssueResolveModel(commentList?.get(i)?.name!!, commentList?.get(i)?.id!!, false))
                }
            } else {
                if (prefManager.getCommentList(TyreConfigClass.commentList) != null && prefManager.getCommentList(TyreConfigClass.commentList).size > 0) {
                    commentList?.clear()
                    for (i in prefManager.getCommentList(TyreConfigClass.commentList).indices) {
                        val model = CommentListData(
                            prefManager.getServiceList(TyreConfigClass.serviceList)[i].id,
                            prefManager.getServiceList(TyreConfigClass.serviceList)[i].name
                        )
                        commentList?.add(model)
                    }
                }
                for (i in commentList?.indices!!) {
                    val add = skipList?.add(IssueResolveModel(commentList?.get(i)?.name!!, commentList?.get(i)?.id!!, false))
                    Log.e("getdta", "" + commentList?.get(i)?.name!! + " " + commentList?.get(i)?.id!! + " " + false)
                }
            }

            Log.e("getreasonid", "" + stringExtra)

            for (i in skipList?.indices!!) {
                if (!stringExtra.equals("")) {
                    if (stringExtra?.toInt() == skipList?.get(i)?.id) {
                        skipList?.get(i)?.isSelected = true
                    }
                }
            }

            val tyreSuggestionAdapter = TyreSuggestionAdpater(skipList!!, this, this, true, false)
            tyreSuggestionAdapter.onclick = this
            pendingReasonRecycView?.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            pendingReasonRecycView?.adapter = tyreSuggestionAdapter

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)

        tvTitleText?.text = titleStr
        ivClose?.setOnClickListener {
            builder.dismiss()
        }
        btnConfirm.setOnClickListener {
            builder.dismiss()

            Common.showLoader(this)
            val jsonObject = JsonObject()
            val jsonArr = JsonArray()

            for (i in skipList?.indices!!) {
                if (skipList?.get(i)?.isSelected!!) {
                    Log.e("getselected", "" + skipList?.get(i)?.issueName)
                    jsonArr.add(skipList?.get(i)?.id)
                }
            }
            jsonObject.addProperty("uuid", uuid)
            jsonObject.add("comment_id", jsonArr)

            if (s == "") {
                jsonObject.addProperty("status", "skip")
            }

            if (s == "update") {
                serviceViewModel?.callApiUpdateService(
                    jsonObject,
                    prefManager.getAccessToken()!!,
                    this
                )
            } else {
                serviceViewModel?.callApiAddService(
                    jsonObject,
                    prefManager.getAccessToken()!!,
                    this
                )
            }

            serviceViewModel?.getAddService()?.observe(this, {
                if (it != null) {
                    if (it.success) {
                        Common.hideLoader()

                        var reason: String? = ""
                        if (skipList?.size!! > 0) {
                            for (i in skipList?.indices!!) {
                                if (skipList?.get(i)?.isSelected!!) {
                                    reason = skipList?.get(i)?.issueName
                                    reasonId = skipList?.get(i)?.id.toString()
                                    Log.e("getreason", "" + reason)
                                }
                            }
                        }

                        tvReason?.text = reason

                    } else {
                        Common.hideLoader()
                        showShortToast("Something Went Wrong", this)
                    }
                } else {
                    Common.hideLoader()
                    showShortToast("Something Went Wrong", this)
                }
            })

        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun getServiceDataById() {

        Common.showLoader(this)

        val jsonObject = JsonObject()
        jsonObject.addProperty("id", uuid)
        commonViewModel?.callApiGetServiceById(jsonObject, prefManager.getAccessToken()!!, this)
        commonViewModel?.serviceByIdModel?.observe(this, {
            if (it != null) {
                if (it.success) {

                    serviceDateByIdModel = it

                    if (serviceDateByIdModel?.data?.get(0)?.service_scheduled_date != null &&
                        !serviceDateByIdModel?.data?.get(0)?.service_scheduled_date.equals("")
                    ) {

                        try {
                            val formatedDate = Common.addHour(serviceDateByIdModel?.data?.get(0)?.service_scheduled_date, 5, 30)!!
                            Log.e("getdated0", "" + formatedDate)
                            Log.e("getdated0", "" + Common.datefrom(formatedDate))
                            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            val output = SimpleDateFormat("hh:mm aa, dd MMMM yyyy")

                            var d: Date? = null
                            try {
                                d = input.parse(formatedDate)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                            val formatted: String = output.format(d)
                            Log.e("DATE", "" + formatted)
                            try {
                                tvCurrentDateTime?.text = formatted
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    try {
                        comment_id_by_service = serviceDateByIdModel?.data?.get(0)?.comment_id?.get(0)!!
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (ischange == "false") {
                        tvChange?.visibility = View.GONE
                        try {
                            if (comment_id_by_service != -1) {
                                getCommentList(comment_id_by_service.toString())
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                    tvChange?.visibility = View.VISIBLE
                } else {
                    Common.hideLoader()
                    try {
                        showShortToast(it.error[0].message, this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else {
                Common.hideLoader()
            }
        })
    }

    private fun getCommentList(comment: String) {
        Common.showLoader(this)
        commonViewModel?.callApiGetComments(prefManager.getAccessToken()!!, this)
        commonViewModel?.commentListModel?.observe(this, {
            if (it != null) {
                if (it.success) {

                    if (it.data != null && it.data.isNotEmpty()) {
                        for (i in it.data.indices) {
                            if (comment != "") {
                                Log.e("getcomment", "" + comment + " " + it.data[i].id)
                                if (comment.toInt() == it.data[i].id) {
                                    tvReason?.text = it.data[i].name
                                    reasonId = it.data[i].id.toString()
                                }
                            }
                        }
                    }
                    Common.hideLoader()

                } else {
                    Common.hideLoader()
                    try {
                        showShortToast(it.error[0].message, this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } else {
                Common.hideLoader()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                if (which.equals("skip_dialogue")) {
                    val intent = Intent()
                    intent.putExtra("back", "" + true)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    onBackPressed()
                }
            }
            R.id.tvChange -> {
                if (which.equals("skip_screen") || which.equals("report_screen")) {

                    if (getCommentList().size > 0) {
                        Log.e("getreasonid", "" + reasonId)
                        openSkipServiceDialogue("Change Pending Reason", reasonId, "update")
                    }
                } else {
                    Log.e("getreasonid0", "" + reasonId)
                    val intent = Intent()
                    intent.putExtra("reason", "" + tvReason?.text?.toString())
                    intent.putExtra("reasonId", "" + reasonId)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            R.id.ivInfoAddService -> {
                showBottomSheetdialogNormal(
                    Common.commonPhotoChooseArr,
                    "Address Detail",
                    this,
                    Common.btn_filled,
                    false, Common.getStringBuilder(address)
                )

            }

        }
    }

    @SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
    private fun showBottomSheetdialogNormal(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String,
        isBtnVisible: Boolean,
        stringBuilder: StringBuilder,
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout, null)
        val dialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).also {

            it.setCancelable(false)
        }
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(view)

        val btnSend = view.findViewById<Button>(R.id.btnOk)
        val tvTitleText = view.findViewById<TextView>(R.id.tvTitleText)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvTitleText?.text = titleStr
        val str = stringBuilder.toString().replace(", ", "" + "\n").replace(",", "" + "\n")
        tv_message?.text = str

        if (str.isNotEmpty()) {
            tv_message.visibility = View.VISIBLE
        }

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }
        if (isBtnVisible) {
            btnSend.visibility = View.VISIBLE
        } else {
            btnSend.visibility = View.GONE
        }
        if (btnBg.equals(Common.btn_filled, ignoreCase = true)) {
            with(btnSend) {
                setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_yellow))
                setTextColor(context?.resources?.getColor(R.color.white)!!)
            }
            btnSend?.text = "Submit"
        } else {
            btnSend.setBackgroundDrawable(context?.resources?.getDrawable(R.drawable.round_corner_button_white))
            btnSend.setTextColor(context?.resources?.getColor(R.color.header_title)!!)
            btnSend?.text = "Cancel"
        }

        btnSend.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onBackPressed() {
        if (which.equals("skip_dialogue")) {
            val intent = Intent()
            intent.putExtra("back", "" + true)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}