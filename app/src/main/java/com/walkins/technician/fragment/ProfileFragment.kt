package com.walkins.technician.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.technician.common.PrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.adapter.DialogueAdpater
import com.walkins.technician.common.TyreDetailCommonClass
import com.walkins.technician.common.onClickAdapter
import com.walkins.technician.common.showShortToast
import com.walkins.technician.viewmodel.CommonViewModel
import com.walkins.technician.viewmodel.LoginActivityViewModel
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    open var ivProfileImg: ImageView? = null
    private var imageDialog: BottomSheetDialog? = null
    private var mainAct: MainActivity? = null

    private var tvusername: TextView? = null
    private var tvMobilenumber: TextView? = null
    private var tvLogout: TextView? = null

    private var commonViewModel: CommonViewModel? = null

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGE_REQUEST = 100
    private lateinit var mCurrentPhotoPath: String
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011

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
        tvusername = view?.findViewById(R.id.tvusername)!!
        tvLogout = view.findViewById(R.id.tvLogout)!!
        ivCamera = view?.findViewById(R.id.ivCamera)!!
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

//            callLogout api
        }

        getUserInfo()
    }

    private fun getUserInfo() {
        mainAct?.let {
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
                            firstName = it.data.firstName
                        }
                        if (it.data.lastName != null) {
                            lastName = it.data.lastName
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

                    } else {
                        if (it.error != null) {
                            if (it.error?.get(0).message != null) {
                                Toast.makeText(context, "" + it.error.get(0).message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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
}

