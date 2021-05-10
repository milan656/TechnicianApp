package com.walkins.aapkedoorstep.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.technician.common.Common
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.adapter.NotificationAdpater
import com.walkins.aapkedoorstep.common.onClickAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment(), onClickAdapter, View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var notiRecycView: RecyclerView? = null
    private var notificationRecycView: RecyclerView? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_notification, container, false)

        init(view)
        return view
    }

    private fun init(view: View?) {
        notificationRecycView = view?.findViewById(R.id.notificationRecycView)
        tvTitle = view?.findViewById(R.id.tvTitle)
        ivBack = view?.findViewById(R.id.ivBack)
        ivBack?.visibility = View.GONE
        notificationRecycView = view?.findViewById(R.id.notificationRecycView)
        var arrayAdapter =
            context?.let { NotificationAdpater(Common.commonPhotoChooseArr, it, this) }
        notificationRecycView?.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        notificationRecycView?.adapter = arrayAdapter
        arrayAdapter?.onclick = this

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "Notification"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {

            }
        }
    }

}