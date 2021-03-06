package com.walkins.aapkedoorstep.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.technician.common.Common
import com.example.technician.common.Common.Companion.showLongToast
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.LoginActivity
import com.walkins.aapkedoorstep.activity.MainActivity
import com.walkins.aapkedoorstep.adapter.DialogueAdpater
import com.walkins.aapkedoorstep.common.onClickAdapter
import com.walkins.aapkedoorstep.viewmodel.CommonViewModel
import com.walkins.aapkedoorstep.viewmodel.LoginActivityViewModel
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@SuppressLint("SetTextI18n")
class ProfileFragment : Fragment(), onClickAdapter {
    var image_uri: Uri? = null
    private var param1: String? = null
    private var param2: String? = null
    private var arrayList = arrayListOf("Gallery", "Camera")
    private var ivCamera: ImageView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var loginViewModel: LoginActivityViewModel? = null
    private var prefManager: PrefManager? = null

    var ivProfileImg: ImageView? = null
    private var imageDialog: BottomSheetDialog? = null
    private var mainAct: MainActivity? = null

    private var tvusername: TextView? = null
    private var tvMobilenumber: TextView? = null
    private var tvLogout: TextView? = null

    private var commonViewModel: CommonViewModel? = null

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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        prefManager = context?.let { PrefManager(it) }
        mainAct = activity as MainActivity?
        init(view)

//        requestPermissionForImage()
        return view
    }


    private fun init(view: View?) {
        tvMobilenumber = view?.findViewById(R.id.tvMobilenumber)!!
        tvusername = view.findViewById(R.id.tvusername)!!
        tvLogout = view.findViewById(R.id.tvLogout)!!
        ivCamera = view.findViewById(R.id.ivCamera)!!
        ivProfileImg = view.findViewById(R.id.ivProfileImg)!!
        tvTitle = view.findViewById(R.id.tvTitle)
        ivBack = view.findViewById(R.id.ivBack)
        ivCamera?.setOnClickListener {

            showBottomSheetdialog(
                Common.commonPhotoChooseArr,
                "Choose From",
                context,
                Common.btn_not_filled
            )
        }
        ivBack?.visibility = View.GONE
        tvTitle?.text = "Your Profile"

        tvLogout?.setOnClickListener {

            showLogOutDialogue()

//            callLogout api
        }

        getUserInfo()
    }

    private fun getUserInfo() {

        mainAct?.let {
            Common.showLoader(it)
            commonViewModel?.callApiGetUserInfo(prefManager?.getAccessToken()!!, it)
            commonViewModel?.getUserInfo()?.observe(it, Observer {
                if (it != null) {
                    if (it.success) {

                        if (it.data.mobile != null) {
                            tvMobilenumber?.text = it.data.mobile
                        }
                        var firstName: String? = ""
                        var lastName: String? = ""
                        if (it.data.firstName != null) {
                            firstName = it.data.firstName.capitalize(Locale.getDefault())
                        }
                        if (it.data.lastName != null) {
                            lastName = it.data.lastName.capitalize(Locale.getDefault())
                        }
                        tvusername?.text = "" + firstName + " " + lastName
                        try {
                            context?.let { it1 ->
                                Glide.with(it1)
                                    .load(it.data.image)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .into(ivProfileImg!!)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        Common.hideLoader()
                    } else {
                        Common.hideLoader()
                        if (it.error != null) {
                            if (it.error?.get(0).message != null) {
                                Toast.makeText(context, "" + it.error.get(0).message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Common.hideLoader()
                }
            })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showBottomSheetdialog(
        array: ArrayList<String>,
        titleStr: String,
        context: Context?,
        btnBg: String

    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialogue_profile_edit_req, null)
        imageDialog =
            getContext()?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        imageDialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        imageDialog?.window?.setLayout(width, height)
        imageDialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        imageDialog?.setContentView(view)

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
            imageDialog?.dismiss()
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
            imageDialog?.dismiss()
        }
        imageDialog?.show()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 10) {
            if (imageDialog != null && imageDialog?.isShowing!!) {
                imageDialog?.dismiss()
            }
            if (Common.commonPhotoChooseArr.get(variable).equals("Gallery")) {
                mainAct?.openGallery()
            }
            if (Common.commonPhotoChooseArr.get(variable).equals("Camera")) {
                mainAct?.openCamera()
            }
        }
    }

    private fun showLogOutDialogue() {
        val builder = AlertDialog.Builder(context).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        val root = LayoutInflater.from(context)
            .inflate(R.layout.common_dialogue_layout_two_button, null)

        val btn_cancel = root.findViewById<Button>(R.id.btn_cancel)
        val btn_save = root.findViewById<Button>(R.id.btn_save)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val chkLogOutFromAllDevice =
            root.findViewById<CheckBox>(R.id.chkLogOutFromAllDevice)

        btn_save.setOnClickListener {
            builder.dismiss()

            if (chkLogOutFromAllDevice?.isChecked!!) {
                callLogoutFromAllDeviceWebService()
            } else {
                RemoveOrAddTokenForApi("")
            }

        }

        btn_cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    private fun callLogoutFromAllDeviceWebService() {
        activity?.let {
            Common.showLoader(it)
            commonViewModel?.callApiLogoutFromAll(prefManager?.getAccessToken()!!, it)
            commonViewModel?.getUserInfo()?.observe(it, Observer {
                Common.hideLoader()
                if (it != null) {
                    if (it.success) {
                        try {
                            prefManager?.clearAll()
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        if (it.error != null) {
                            if (it.error?.get(0).message != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager?.clearAll()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun RemoveOrAddTokenForApi(token: String) {

        var jsonObject: JsonObject
        jsonObject = JsonObject()
        jsonObject.addProperty("token", token)

        activity?.let {
            commonViewModel?.callApiToSaveToken(jsonObject, prefManager?.getAccessToken()!!, it)
            commonViewModel?.getSaveToken()?.observe(it, Observer {
                if (it != null) {
                    if (it.success) {

                        prefManager?.clearAll()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finish()


                    } else {
                        if (it.error != null && it.error.size > 0) {
                            if (it.error.get(0).statusCode != null) {
                                if (it.error.get(0).statusCode == 400) {
                                    prefManager?.clearAll()
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
    }

}

