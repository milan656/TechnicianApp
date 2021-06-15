package com.walkins.aapkedoorstep

import android.view.View
import android.widget.CheckBox
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ramotion.fluidslider.FluidSlider
import com.walkins.aapkedoorstep.activity.LoginActivity
import com.walkins.aapkedoorstep.services.AllServiceSingleTyreSelection
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestCasesForAllServiceSingleTyreEditFlow {

    private var serviceSelection: AllServiceSingleTyreSelection? = null

    @Before
    fun setModelClass() {
        serviceSelection = AllServiceSingleTyreSelection()
//        val resultData = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intending(not(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultData))
    }

    @get:Rule
    val mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java
    )

    @Test
    fun startTechnicianTestingFlow() {

        loginView()

    }

    private fun loginView() {


        onView(withId(R.id.btnLoginToDashBoard)).perform(customScrollTo, click())
        onView(withId(R.id.edtLoginEmail)).perform(typeText(serviceSelection?.invalidNumber))
        onView(withId(R.id.btnLoginToDashBoard)).perform(
            closeSoftKeyboard(),
            click()
        )

        BaseRobot().doOnView(withId(R.id.btnOk), closeSoftKeyboard(), click())

        onView(withId(R.id.edtLoginEmail)).perform(
            clearText(),
            typeText(serviceSelection?.validNumber)
        )

        onView(withId(R.id.btnLoginToDashBoard)).perform(
            closeSoftKeyboard(),
            click()
        )

        verifyOTP()
    }

    private fun verifyOTP() {

        Thread.sleep(1000)
//        click when no OTP entered
        BaseRobot().doOnView(withId(R.id.scrollVerify), closeSoftKeyboard(), swipeUp())
        BaseRobot().doOnView(withId(R.id.btnVerify), closeSoftKeyboard(), click())
//        onView(withId(R.id.btnVerify)).perform(customScrollTo, click())

        enterInvalidOTP()

        BaseRobot().doOnView(withId(R.id.btnVerify), closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.btnOk), closeSoftKeyboard(), click())

        enterValidOTP()

        onView(withId(R.id.btnVerify)).perform(customScrollTo, click())
        NavigateMainDashboard()
    }

    private fun enterValidOTP() {
        onView(withId(R.id.edtOtp1)).perform(
            clearText(),
            typeText("1"),

            )
        onView(withId(R.id.edtOtp2)).perform(
            clearText(),
            typeText("2"),

            )
        onView(withId(R.id.edtOtp3)).perform(
            clearText(),
            typeText("1"),

            )
        onView(withId(R.id.edtOtp4)).perform(
            clearText(),
            typeText("2"), closeSoftKeyboard(),

            )

        Thread.sleep(1000)
    }

    private fun enterInvalidOTP() {
        onView(withId(R.id.edtOtp1)).perform(
            typeText("1"),
            click()
        )
        onView(withId(R.id.edtOtp2)).perform(
            typeText("5"),
            click()
        )
        onView(withId(R.id.edtOtp3)).perform(
            typeText("1"),
            click()
        )
        onView(withId(R.id.edtOtp4)).perform(
            typeText("5"), closeSoftKeyboard(),
            click()
        )

        onView(withId(R.id.scrollVerify)).perform(
            closeSoftKeyboard(),
            swipeUp()
        )

    }

    private fun NavigateMainDashboard() {

        BaseRobot().doOnView(withId(R.id.llhome), closeSoftKeyboard(), click())

        Thread.sleep(2000)
        BaseRobot().doOnView(
            withId(R.id.recyclerView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        navigateToServiceListScreen()
    }

    private fun navigateToServiceListScreen() {

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        navigateToAddServiceDetailScreen(false)

    }

    private fun navigateToAddServiceDetailScreen(isSecondTime:Boolean) {
        serviceSelection?.editFlowEnable = false
        Thread.sleep(2000)
        onView(withId(R.id.ivAddServices)).perform(customScrollTo, click())

        if (!serviceSelection?.nitrogen_refill_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_refill_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.wheel_balancing_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        BaseRobot().doOnView(
            withId(R.id.scroll), closeSoftKeyboard(),
            swipeUp()
        )

        if (!serviceSelection?.nitrogen_topup_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.typeRotation_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.typeRotation_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                onView(withId(R.id.radioLF_LR)).perform(customScrollTo, click())
                onView(withId(R.id.radioRF_LR)).perform(customScrollTo, click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

//        fillUpAddServicedetail()

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())

        if (serviceSelection?.anotherTyre!!) {
            tyreLFTyreSelection()
            Thread.sleep(500)
//            tyreRFTyreSelection()
//            Thread.sleep(500)

        } else {
            tyreLRTyreSelection()
            Thread.sleep(500)
//            tyreRRTyreSelection()
//            Thread.sleep(500)

        }

        fillUpAddServicedetail()

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())

        editFlowPerform(isSecondTime)
    }


    private fun AnotherVehicleDetailFillup() {

        serviceSelection?.anotherTyre = true
        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )
        navigateToAddServiceDetailScreen(true)
    }

    private fun editFlowPerform(secondTime: Boolean) {
        serviceSelection?.editFlowEnable = true
        Thread.sleep(500)
        if (serviceSelection?.anotherTyre!!) {
//            tyreLFTyreSelection()
//            Thread.sleep(500)
//            tyreRFTyreSelection()
//            Thread.sleep(500)
        } else {
            tyreLRTyreSelection()
            Thread.sleep(500)
//            tyreRRTyreSelection()
//            Thread.sleep(500)
        }
        Thread.sleep(500)

        if (!secondTime){
            AnotherVehicleDetailFillup()

        }

        goToPreviousFilledService()

    }

    private fun goToPreviousFilledService() {
        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        Thread.sleep(2000)
        onView(withId(R.id.ivAddServices)).perform(customScrollTo, click())

        if (!serviceSelection?.nitrogen_refill_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_refill_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.wheel_balancing_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        BaseRobot().doOnView(
            withId(R.id.scroll), closeSoftKeyboard(),
            swipeUp()
        )

        if (!serviceSelection?.nitrogen_topup_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.typeRotation_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.typeRotation_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                onView(withId(R.id.radioLF_LR)).perform(customScrollTo, click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        Thread.sleep(2000)
        onView(withId(R.id.ivAddServices)).perform(customScrollTo, click())

        if (!serviceSelection?.nitrogen_refill_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_refill_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.wheel_balancing_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        BaseRobot().doOnView(
            withId(R.id.scroll), closeSoftKeyboard(),
            swipeUp()
        )

        if (!serviceSelection?.nitrogen_topup_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        if (!serviceSelection?.typeRotation_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.typeRotation_service)),
                        recyclerChildAction<CheckBox>(R.id.chkNitrogenTopup) {
                            if (!this.isChecked) {
                                this.performClick()
                                click()
                            }
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                onView(withId(R.id.radioLF_LR)).perform(customScrollTo, click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

//        fillUpAddServicedetail()

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())
    }

    private fun removeWheelBalancingService() {
//        Remove service Flow
        onView(withId(R.id.ivAddServices)).perform(customScrollTo, click())
        if (!serviceSelection?.wheel_balancing_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
                        click()
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        BaseRobot().doOnView(withId(R.id.btn_cancel), closeSoftKeyboard(), click())

        try {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
                    click()
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

//        remove ok
        BaseRobot().doOnView(withId(R.id.btn_ok), closeSoftKeyboard(), click())

    }

    private fun tyreRRTyreSelection() {
        onView(withId(R.id.ivTyre4)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(4, "RR")
    }

    private fun tyreLRTyreSelection() {
        onView(withId(R.id.ivTyre2)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(3, "LR")
    }

    private fun tyreRFTyreSelection() {
        onView(withId(R.id.ivTyre3)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(2, "RF")
    }

    private fun tyreLFTyreSelection() {
        onView(withId(R.id.ivTyre1)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(0, "LF")
    }

    private fun skipServiceFlow() {
        BaseRobot().doOnView(withId(R.id.tvSkipService), closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.btnConfirm), closeSoftKeyboard(), click())
//        pendingReasonRecycView
        BaseRobot().doOnView(
            withId(R.id.pendingReasonRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )

        BaseRobot().doOnView(withId(R.id.ivClose), closeSoftKeyboard(), click())
//        BaseRobot().doOnView(withId(R.id.btnConfirm), ViewActions.closeSoftKeyboard(), ViewActions.click())
    }

    private fun navigateToVehicleBrandScreeneSelection(number: Int, type: String) {

        try {
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycMake_), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
//            onView(withId(R.id.gridviewRecycMake_))
//                .perform(
//                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                        number,
//                        recyclerChildAction<TextView>(R.id.tvmakeName) {
//                            print("vehicle_make-->" + this.text?.toString())
//                            serviceSelection?.vehicleMake = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                        }
//                    )
//                )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), closeSoftKeyboard(), click())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                BaseRobot().doOnView(withId(R.id.ivEditVehicleMake), closeSoftKeyboard(), click())

                Thread.sleep(200)
                BaseRobot().doOnView(
                    withId(R.id.gridviewRecycMake_), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        click()
                    )
                )
//                onView(withId(R.id.gridviewRecycMake_))
//                    .perform(
//                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                            2,
//                            recyclerChildAction<TextView>(R.id.tvmakeName) {
//                                print("vehicle_make-->" + this.text?.toString())
//                                serviceSelection?.vehicleMake = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                            }
//                        )
//                    )

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(500)
        navigateToVehiclePatternScreen(type)
    }

    private fun navigateToVehiclePatternScreen(type: String) {
        try {
            val number = (0..4).random()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
//            onView(withId(R.id.gridviewRecycModel))
//                .perform(
//                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                        number,
//                        recyclerChildAction<TextView>(R.id.ivVehicleImage) {
//                            print("vehicle_make-->" + this.text?.toString())
//                            serviceSelection?.vehiclePattern = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                        }
//                    )
//                )

            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), closeSoftKeyboard(), click())
            }
        } catch (e: Exception) {
            e.printStackTrace()

            try {
                BaseRobot().doOnView(withId(R.id.ivEditVehicleMakeModel), closeSoftKeyboard(), click())
                Thread.sleep(150)
                BaseRobot().doOnView(
                    withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1,
                        click()
                    )
                )
//                onView(withId(R.id.gridviewRecycModel))
//                    .perform(
//                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                            1,
//                            recyclerChildAction<TextView>(R.id.ivVehicleImage) {
//                                print("vehicle_make-->" + this.text?.toString())
//                                serviceSelection?.vehiclePattern = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                            }
//                        )
//                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(500)
        navigateToVehicleSizeScreen(type)
    }

    private fun navigateToVehicleSizeScreen(type: String) {
        try {
//            onView(withId(R.id.gridviewRecycModel)).isDisplayed()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
//            onView(withId(R.id.gridviewRecycModel))
//                .perform(
//                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                        0,
//                        recyclerChildAction<TextView>(R.id.ivVehicleImage) {
//                            print("vehicle_make-->" + this.text?.toString())
//                            serviceSelection?.vehicleSize = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                        }
//                    )
//                )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), closeSoftKeyboard(), click())
            }
            //view is displayed logic
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            //view not displayed logic
            try {
                BaseRobot().doOnView(withId(R.id.ivEditVehicleMakeModel), closeSoftKeyboard(), click())

                Thread.sleep(150)
                BaseRobot().doOnView(
                    withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        1,
                        click()
                    )
                )
//                onView(withId(R.id.gridviewRecycModel))
//                    .perform(
//                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                            1,
//                            recyclerChildAction<TextView>(R.id.ivVehicleImage) {
//                                print("vehicle_make-->" + this.text?.toString())
//                                serviceSelection?.vehicleSize = this.text.toString()
////                            selects?.selected_vehicleMake = this.text.toString()
//                            }
//                        )
//                    )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(500)
        navigateToVisualDetailPage(type)
    }

    private fun navigateToVisualDetailPage(type: String) {

        Thread.sleep(1000)

        if (!serviceSelection?.editFlowEnable!!) {

            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val date = onView(withId(R.id.edtManufaturingDate))
                if (!date.equals("")) {
                    onView(withId(R.id.edtManufaturingDate)).perform(clearText(), typeText(serviceSelection?.zeroManufacturingDate))
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(typeText(serviceSelection?.zeroManufacturingDate))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val date = onView(withId(R.id.edtManufaturingDate))
                if (!date.equals("")) {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        clearText(),
                        typeText(serviceSelection?.twoletterManufacturingDate)
                    )
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        typeText(serviceSelection?.twoletterManufacturingDate)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val date = onView(withId(R.id.edtManufaturingDate))
                if (!date.equals("")) {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        clearText(),
                        typeText(serviceSelection?.inValidWeekOfYearManufacturingDate)
                    )
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        typeText(serviceSelection?.inValidWeekOfYearManufacturingDate)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val date = onView(withId(R.id.edtManufaturingDate))
                if (!date.equals("")) {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        clearText(),
                        typeText(serviceSelection?.inValidYearManufacturingDate)
                    )
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        typeText(serviceSelection?.inValidYearManufacturingDate)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                var date: String? = ""
                if (type.equals("LF")) {
                    date = "2121"
                } else if (type.equals("RF")) {
                    date = "1111"
                } else if (type.equals("LR")) {
                    date = "1520"
                } else if (type.equals("RR")) {
                    date = "2020"
                }
                onView(withId(R.id.edtManufaturingDate)).perform(
                    clearText(),
                    typeText(date)
                )

                val manuDate = onView(withId(R.id.edtManufaturingDate))
//            serviceSelection?.manuFacturingDate_lf = getText(manuDate)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (type.equals("LF")) {
                onView(withId(R.id.multiSlider1)).perform(setValue(35F))
                onView(withId(R.id.multiSliderPsiOut)).perform(setValue(40F))
                onView(withId(R.id.multiSliderWeight)).perform(setValue(39F))
            } else if (type.equals("RF")) {

            } else if (type.equals("LR")) {
                onView(withId(R.id.multiSlider1)).perform(setValue(35F))
                onView(withId(R.id.multiSliderPsiOut)).perform(setValue(40F))
                onView(withId(R.id.multiSliderWeight)).perform(setValue(39F))
            } else if (type.equals("RR")) {

            }

            try {
                if (type.equals("LF")) {
                    onView(withId(R.id.ivOkSideWell)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugShoulder)).perform(customScrollTo, click());
                    onView(withId(R.id.ivReqTreadWear)).perform(customScrollTo, click());

                    onView(withId(R.id.ivSugTreadDepth)).perform(customScrollTo, click());
                    onView(withId(R.id.ivOkRimDamage)).perform(customScrollTo, click());
                    onView(withId(R.id.ivReqbubble)).perform(customScrollTo, click());
                } else if (type.equals("RF")) {
                    onView(withId(R.id.ivSugSideWell)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugShoulder)).perform(customScrollTo, click());
                    onView(withId(R.id.ivOkTreadWear)).perform(customScrollTo, click());

                    onView(withId(R.id.ivSugTreadDepth)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugRimDamage)).perform(customScrollTo, click());
                    onView(withId(R.id.ivReqbubble)).perform(customScrollTo, click());
                } else if (type.equals("LR")) {
                    onView(withId(R.id.ivReqSideWell)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugShoulder)).perform(customScrollTo, click());
                    onView(withId(R.id.ivReqTreadWear)).perform(customScrollTo, click());

                    onView(withId(R.id.ivSugTreadDepth)).perform(customScrollTo, click());
                    onView(withId(R.id.ivOkRimDamage)).perform(customScrollTo, click());
                    onView(withId(R.id.ivReqbubble)).perform(customScrollTo, click());
                } else if (type.equals("RR")) {
                    onView(withId(R.id.ivOkSideWell)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugShoulder)).perform(customScrollTo, click());
                    onView(withId(R.id.ivOkTreadWear)).perform(customScrollTo, click());

                    onView(withId(R.id.ivOkTreadDepth)).perform(customScrollTo, click());
                    onView(withId(R.id.ivSugRimDamage)).perform(customScrollTo, click());
                    onView(withId(R.id.ivOkbubble)).perform(customScrollTo, click());
                }


                val sideWell = onView(withId(R.id.ivOkSideWell))
                val shoulder = onView(withId(R.id.ivSugShoulder))
                val treadWear = onView(withId(R.id.ivReqTreadWear))
                val treadDepth = onView(withId(R.id.ivSugTreadDepth))
                val rimDamage = onView(withId(R.id.ivOkRimDamage))
                val bubble = onView(withId(R.id.ivReqbubble))

//            if (type.equals("LF")) {
//                serviceSelection?.sidewell_lf = getText(sideWell)
//                serviceSelection?.shoulder_lf = getText(shoulder)
//                serviceSelection?.treadDepth_lf = getText(treadDepth)
//                serviceSelection?.treadWear_lf = getText(treadWear)
//                serviceSelection?.rimDamage_lf = getText(rimDamage)
//                serviceSelection?.bubble_lf = getText(bubble)
//            } else if (type.equals("RF")) {
//                serviceSelection?.sidewell_rf = getText(sideWell)
//                serviceSelection?.shoulder_rf = getText(shoulder)
//                serviceSelection?.treadDepth_rf = getText(treadDepth)
//                serviceSelection?.treadWear_rf = getText(treadWear)
//                serviceSelection?.rimDamage_rf = getText(rimDamage)
//                serviceSelection?.bubble_rf = getText(bubble)
//            } else if (type.equals("LR")) {
//                serviceSelection?.sidewell_lr = getText(sideWell)
//                serviceSelection?.shoulder_lr = getText(shoulder)
//                serviceSelection?.treadDepth_lr = getText(treadDepth)
//                serviceSelection?.treadWear_lr = getText(treadWear)
//                serviceSelection?.rimDamage_lr = getText(rimDamage)
//                serviceSelection?.bubble_lr = getText(bubble)
//            } else if (type.equals("RR")) {
//                serviceSelection?.sidewell_rr = getText(sideWell)
//                serviceSelection?.shoulder_rr = getText(shoulder)
//                serviceSelection?.treadDepth_rr = getText(treadDepth)
//                serviceSelection?.treadWear_rr = getText(treadWear)
//                serviceSelection?.rimDamage_rr = getText(rimDamage)
//                serviceSelection?.bubble_rr = getText(bubble)
//            }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            BaseRobot().doOnView(
                withId(R.id.visualScroll), closeSoftKeyboard(),
                swipeUp()
            )
            Thread.sleep(2000)
            try {
                val number = (1..3).random()
                BaseRobot().doOnView(
                    withId(R.id.issueResolvedRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        click()
                    )
                )
//            onView(withId(R.id.issueResolvedRecycView))
//                .perform(
//                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                        number,
//                        recyclerChildAction<CheckBox>(R.id.chkTyreSuggestion) {
//                            print("issueResolved-->" + this.text?.toString())
//                            if (type.equals("LF")) {
//                                serviceSelection?.issueResolveArrayList_lf?.add(this.text?.toString()!!)
//                            } else if (type.equals("RF")) {
//                                serviceSelection?.issueResolveArrayList_rf?.add(this.text?.toString()!!)
//                            } else if (type.equals("LR")) {
//                                serviceSelection?.issueResolveArrayList_lr?.add(this.text?.toString()!!)
//                            } else if (type.equals("RR")) {
//                                serviceSelection?.issueResolveArrayList_rr?.add(this.text?.toString()!!)
//                            }
////                             = this.text.toString()
//                        }
//                    )
//                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            BaseRobot().doOnView(
                withId(R.id.visualScroll), closeSoftKeyboard(),
                swipeUp()
            )
            Thread.sleep(1000)
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun fillUpAddServicedetail() {
        onView(withId(R.id.ivAddTechnicalSuggestion)).perform(customScrollTo, click());
        onView(withId(R.id.suggestionsRecycView)).perform(customScrollTo, scrollTo());

        try {
            val number = (1..3).random()
            BaseRobot().doOnView(
                withId(R.id.suggestionsRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    recyclerChildAction<CheckBox>(R.id.chkTyreSuggestion) {
                        if (!this.isChecked) {
                            this.performClick()
                            click()
                        }
                    }
                )
            )

            onView(withId(R.id.suggestionsRecycView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        recyclerChildAction<CheckBox>(R.id.chkTyreSuggestion) {
                            print("suggestions-->" + this.text?.toString())
                            serviceSelection?.suggestionsArrayList?.add(this.text?.toString()!!)
//                             = this.text.toString()
                        }
                    )
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val suggestion = getText(onView(withId(R.id.edtMoreSuggestion)))
            if (suggestion.equals("")) {
                BaseRobot().doOnView(withId(R.id.edtMoreSuggestion), closeSoftKeyboard(), typeText("suggestion for this service."))
            } else {
                BaseRobot().doOnView(withId(R.id.edtMoreSuggestion), closeSoftKeyboard(), clearText(), typeText("suggestion for this service."))
            }
            val moreSuggestion = onView(withId(R.id.edtMoreSuggestion))
            serviceSelection?.moreSuggestions = getText(moreSuggestion)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun removeNitrogenService() {
        if (!serviceSelection?.nitrogen_topup_service.equals("")) {
            try {
                BaseRobot().doOnView(
                    withId(R.id.serviceRecycView), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
                        click()
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        BaseRobot().doOnView(withId(R.id.btn_cancel), closeSoftKeyboard(), click())

        try {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
                    click()
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        BaseRobot().doOnView(
            withId(R.id.scroll), closeSoftKeyboard(),
            swipeDown()
        )

        try {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(serviceSelection?.nitrogen_refill_service)),
                    click()
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

//        remove ok
        BaseRobot().doOnView(withId(R.id.btn_ok), closeSoftKeyboard(), click())

    }

    open class BaseRobot {

        fun doOnView(matcher: Matcher<View>, vararg actions: ViewAction) {
            actions.forEach {
                waitForView(matcher).perform(it)
            }
        }

        fun assertOnView(matcher: Matcher<View>, vararg assertions: ViewAssertion) {
            assertions.forEach {
                waitForView(matcher).check(it)
            }
        }

        fun waitForView(
            viewMatcher: Matcher<View>,
            waitMillis: Int = 20000,
            waitMillisPerTry: Long = 100,
        ): ViewInteraction {

            // Derive the max tries
            val maxTries = waitMillis / waitMillisPerTry.toInt()
            var tries = 0
            for (i in 0..maxTries)
                try {
                    // Track the amount of times we've tried
                    tries++
                    // Search the root for the view
                    Espresso.onView(ViewMatchers.isRoot()).perform(searchFor(viewMatcher))
                    // If we're here, we found our view. Now return it
                    return Espresso.onView(viewMatcher)
                } catch (e: Exception) {
                    if (tries == maxTries) {
                        throw e
                    }
                    Thread.sleep(waitMillisPerTry)
                }
            throw Exception("Error finding a view matching $viewMatcher")
        }

        fun searchFor(matcher: Matcher<View>): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isRoot()
                }

                override fun getDescription(): String {
                    return "searching for view $matcher in the root view"
                }

                override fun perform(uiController: UiController, view: View) {
                    var tries = 0
                    val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)

                    // Look for the match in the tree of childviews
                    childViews.forEach {
                        tries++
                        if (matcher.matches(it)) {
                            // found the view
                            return
                        }
                    }
                    throw NoMatchingViewException.Builder()
                        .withRootView(view)
                        .withViewMatcher(matcher)
                        .build()
                }
            }
        }
    }

    fun <T : View> recyclerChildAction(@IdRes id: Int, block: T.() -> Unit): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.any(View::class.java)
            }

            override fun getDescription(): String {
                return "Performing action on RecyclerView child item"
            }

            override fun perform(
                uiController: UiController,
                view: View,
            ) {
                view.findViewById<T>(id).block()
            }
        }

    }

    fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    fun ViewInteraction.isDisplayed(): Boolean {
        try {
            check(matches(ViewMatchers.isDisplayed()))
            return true
        } catch (e: NoMatchingViewException) {
            return false
        }
    }

    var customScrollTo: ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.allOf(
                withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(
                    anyOf(
                        isAssignableFrom(ScrollView::class.java),
                        isAssignableFrom(HorizontalScrollView::class.java),
                        isAssignableFrom(NestedScrollView::class.java)
                    )
                )
            )
        }

        override fun getDescription(): String {
            return null.toString()
        }

        override fun perform(uiController: UiController?, view: View?) {
            ScrollToAction().perform(uiController, view)
        }
    }

    private fun submitServiceAndGoToCompletedService() {
        BaseRobot().doOnView(withId(R.id.btnSubmitAndComplete), closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.btn_ok), closeSoftKeyboard(), click())

        Thread.sleep(4000)
        BaseRobot().doOnView(withId(R.id.btnOk), closeSoftKeyboard(), click())

        Thread.sleep(2000)
        BaseRobot().doOnView(withId(R.id.llCompleted), closeSoftKeyboard(), click())

        TestCasesForSingleServiceAndTyre.BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        navigateToCompletedServiceDetailScreen()
    }

    private fun navigateToCompletedServiceDetailScreen() {

        Thread.sleep(2500)
        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())

        onView(withId(R.id.ivTyre1)).perform(customScrollTo, click())

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre2)).perform(customScrollTo, click())

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre3)).perform(customScrollTo, click())

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre4)).perform(customScrollTo, click())

        Thread.sleep(3000)

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        Thread.sleep(1000)
        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        Thread.sleep(1000)
    }

    fun setValue(value: Float): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(FluidSlider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as FluidSlider
                seekBar.position = value
                seekBar.positionListener = {
                    seekBar.bubbleText = value.toString()
                }
                seekBar.animate()
//                seekBar.value = value
            }

        }
    }
}