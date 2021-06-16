package com.walkins.aapkedoorstep

import android.content.SharedPreferences
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
import com.walkins.aapkedoorstep.activity.LoginActivity
import com.walkins.aapkedoorstep.services.SingleServiceTyreSelection
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCasesForSingleServiceAndTyre {

    private val PREFERENCE_NAME = "MyPref"
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var preferences: SharedPreferences? = null

    //    private var serviceSelection: SingleServiceTyreSelection? = null
    private var serviceSelection: SingleServiceTyreSelection? = null

    @Before
    fun setModelClass() {
        serviceSelection = SingleServiceTyreSelection()
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

//        click when no OTP entered
        BaseRobot().doOnView(withId(R.id.btnVerify), closeSoftKeyboard(), click())

        enterInvalidOTP()

        Espresso.onView(withId(R.id.btnVerify)).perform(
            closeSoftKeyboard(),
            click()
        )

        BaseRobot().doOnView(withId(R.id.btnOk), closeSoftKeyboard(), click())

        enterValidOTP()

        Espresso.onView(withId(R.id.btnVerify)).perform(
            closeSoftKeyboard(),
            click()
        )
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

        Thread.sleep(4000)
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
        navigateToAddServiceDetailScreen()

    }

    private fun navigateToAddServiceDetailScreen() {

//        skipServiceFlow()

        Thread.sleep(2500)
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

        BaseRobot().doOnView(withId(R.id.scroll), closeSoftKeyboard(),
            swipeUp())

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

        fillUpAddServicedetail()

//        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())
//
//        editFlowPerform()

//        if (serviceSelection?.vehicleWiseStoreData!!) {
//            serviceSelection?.vehicleWiseStoreData = false
//            AnotherVehicleDetailFillup()
//        }
    }

    private fun AnotherVehicleDetailFillup() {
        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )
        navigateToAddServiceDetailScreen()
    }

    private fun editFlowPerform() {
        tyreLFTyreSelection("LF")
        tyreRFTyreSelection("RF")
        tyreLRTyreSelection("LR")
        tyreRRTyreSelection("RR")
    }

    private fun removeWheelBalancingService() {
//        Remove service Flow
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

    private fun tyreRRTyreSelection(type: String) {
        BaseRobot().doOnView(withId(R.id.ivTyre4), closeSoftKeyboard(), click())
        navigateToVehicleBrandScreeneSelection(4, type)
    }

    private fun tyreLRTyreSelection(type: String) {
        BaseRobot().doOnView(withId(R.id.ivTyre2), closeSoftKeyboard(), click())
        navigateToVehicleBrandScreeneSelection(3, type)
    }

    private fun tyreRFTyreSelection(type: String) {
        BaseRobot().doOnView(withId(R.id.ivTyre3), closeSoftKeyboard(), click())
        navigateToVehicleBrandScreeneSelection(2, type)
    }

    private fun tyreLFTyreSelection(type: String) {
        onView(withId(R.id.ivTyre1)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(0, type)
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
//                            if (type.equals("LF")) {
//                                serviceSelection?.vehicleMake_lf = this.text.toString()
//                            } else if (type.equals("RF")) {
//                                serviceSelection?.vehicleMake_rf = this.text.toString()
//                            } else if (type.equals("LF")) {
//                                serviceSelection?.vehicleMake_lr = this.text.toString()
//                            } else if (type.equals("RR")) {
//                                serviceSelection?.vehicleMake_rr = this.text.toString()
//                            }
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
//                                if (type.equals("LF")) {
//                                    serviceSelection?.vehicleMake_lf = this.text.toString()
//                                } else if (type.equals("RF")) {
//                                    serviceSelection?.vehicleMake_rf = this.text.toString()
//                                } else if (type.equals("LF")) {
//                                    serviceSelection?.vehicleMake_lr = this.text.toString()
//                                } else if (type.equals("RR")) {
//                                    serviceSelection?.vehicleMake_rr = this.text.toString()
//                                }
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
        navigateToVehiclePatternScreen(type)
    }

    private fun navigateToVehiclePatternScreen(type: String) {

        Thread.sleep(1000)
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
//                        recyclerChildAction<TextView>(R.id.tvmakeName) {
//                            print("vehicle_make-->" + this.text?.toString())
//                            if (type.equals("LF")) {
//                                serviceSelection?.vehiclePattern_lf = this.text.toString()
//                            } else if (type.equals("RF")) {
//                                serviceSelection?.vehiclePattern_rf = this.text.toString()
//                            } else if (type.equals("LF")) {
//                                serviceSelection?.vehiclePattern_lr = this.text.toString()
//                            } else if (type.equals("RR")) {
//                                serviceSelection?.vehiclePattern_rr = this.text.toString()
//                            }
////                            selects?.selected_vehiclePattern = this.text.toString()
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
//                            recyclerChildAction<TextView>(R.id.tvmakeName) {
//                                print("vehicle_make-->" + this.text?.toString())
//                                if (type.equals("LF")) {
//                                    serviceSelection?.vehiclePattern_lf = this.text.toString()
//                                } else if (type.equals("RF")) {
//                                    serviceSelection?.vehiclePattern_rf = this.text.toString()
//                                } else if (type.equals("LF")) {
//                                    serviceSelection?.vehiclePattern_lr = this.text.toString()
//                                } else if (type.equals("RR")) {
//                                    serviceSelection?.vehiclePattern_rr = this.text.toString()
//                                }
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

        navigateToVehicleSizeScreen(type)
    }

    private fun navigateToVehicleSizeScreen(type: String) {
        Thread.sleep(1000)
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
//                        recyclerChildAction<TextView>(R.id.tvmakeName) {
//                            print("vehicle_make-->" + this.text?.toString())
//                            if (type.equals("LF")) {
//                                serviceSelection?.vehicleSize_lf = this.text.toString()
//                            } else if (type.equals("RF")) {
//                                serviceSelection?.vehicleSize_rf = this.text.toString()
//                            } else if (type.equals("LF")) {
//                                serviceSelection?.vehicleSize_lr = this.text.toString()
//                            } else if (type.equals("RR")) {
//                                serviceSelection?.vehicleSize_rr = this.text.toString()
//                            }
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
                    ))

//                onView(withId(R.id.gridviewRecycModel))
//                    .perform(
//                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                            1,
//                            recyclerChildAction<TextView>(R.id.tvmakeName) {
//                                print("vehicle_make-->" + this.text?.toString())
//                                if (type.equals("LF")) {
//                                    serviceSelection?.vehicleSize_lf = this.text.toString()
//                                } else if (type.equals("RF")) {
//                                    serviceSelection?.vehicleSize_rf = this.text.toString()
//                                } else if (type.equals("LF")) {
//                                    serviceSelection?.vehicleSize_lr = this.text.toString()
//                                } else if (type.equals("RR")) {
//                                    serviceSelection?.vehicleSize_rr = this.text.toString()
//                                }
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

        navigateToVisualDetailPage(type)
    }

    private fun navigateToVisualDetailPage(type: String) {

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
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val date = onView(withId(R.id.edtManufaturingDate))
            if (!date.equals("")) {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    clearText(),
                    typeText(serviceSelection?.twoletterManufacturingDate))
            } else {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    typeText(serviceSelection?.twoletterManufacturingDate))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val date = onView(withId(R.id.edtManufaturingDate))
            if (!date.equals("")) {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    clearText(),
                    typeText(serviceSelection?.inValidWeekOfYearManufacturingDate))
            } else {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    typeText(serviceSelection?.inValidWeekOfYearManufacturingDate))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val date = onView(withId(R.id.edtManufaturingDate))
            if (!date.equals("")) {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    clearText(),
                    typeText(serviceSelection?.inValidYearManufacturingDate))
            } else {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    typeText(serviceSelection?.inValidYearManufacturingDate))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            onView(withId(R.id.edtManufaturingDate)).perform(
                clearText(),
                typeText(serviceSelection?.validManufacturingDate))

            val manuDate = onView(withId(R.id.edtManufaturingDate))
            serviceSelection?.manuFacturingDate_lf = getText(manuDate)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        try {
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            onView(withId(R.id.ivOkSideWell)).perform(customScrollTo, click());
            onView(withId(R.id.ivSugShoulder)).perform(customScrollTo, click());
            onView(withId(R.id.ivReqTreadWear)).perform(customScrollTo, click());

            onView(withId(R.id.ivSugTreadDepth)).perform(customScrollTo, click());
            onView(withId(R.id.ivOkRimDamage)).perform(customScrollTo, click());
            onView(withId(R.id.ivReqbubble)).perform(customScrollTo, click());

            val sideWell = onView(withId(R.id.ivOkSideWell))
            val shoulder = onView(withId(R.id.ivSugShoulder))
            val treadWear = onView(withId(R.id.ivReqTreadWear))
            val treadDepth = onView(withId(R.id.ivSugTreadDepth))
            val rimDamage = onView(withId(R.id.ivOkRimDamage))
            val bubble = onView(withId(R.id.ivReqbubble))

            if (type.equals("LF")) {
                serviceSelection?.sidewell_lf = getText(sideWell)
                serviceSelection?.shoulder_lf = getText(shoulder)
                serviceSelection?.treadDepth_lf = getText(treadDepth)
                serviceSelection?.treadWear_lf = getText(treadWear)
                serviceSelection?.rimDamage_lf = getText(rimDamage)
                serviceSelection?.bubble_lf = getText(bubble)
            } else if (type.equals("RF")) {
                serviceSelection?.sidewell_rf = getText(sideWell)
                serviceSelection?.shoulder_rf = getText(shoulder)
                serviceSelection?.treadDepth_rf = getText(treadDepth)
                serviceSelection?.treadWear_rf = getText(treadWear)
                serviceSelection?.rimDamage_rf = getText(rimDamage)
                serviceSelection?.bubble_rf = getText(bubble)
            } else if (type.equals("LR")) {
                serviceSelection?.sidewell_lr = getText(sideWell)
                serviceSelection?.shoulder_lr = getText(shoulder)
                serviceSelection?.treadDepth_lr = getText(treadDepth)
                serviceSelection?.treadWear_lr = getText(treadWear)
                serviceSelection?.rimDamage_lr = getText(rimDamage)
                serviceSelection?.bubble_lr = getText(bubble)
            } else if (type.equals("RR")) {
                serviceSelection?.sidewell_rr = getText(sideWell)
                serviceSelection?.shoulder_rr = getText(shoulder)
                serviceSelection?.treadDepth_rr = getText(treadDepth)
                serviceSelection?.treadWear_rr = getText(treadWear)
                serviceSelection?.rimDamage_rr = getText(rimDamage)
                serviceSelection?.bubble_rr = getText(bubble)
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.visualScroll), closeSoftKeyboard(),
            swipeUp())
        Thread.sleep(2000)
        try {
            val number = (1..3).random()
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(
                withId(R.id.issueResolvedRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
            onView(withId(R.id.issueResolvedRecycView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        recyclerChildAction<CheckBox>(R.id.chkTyreSuggestion) {
                            print("issueResolved-->" + this.text?.toString())
                            if (type.equals("LF")) {
                                serviceSelection?.issueResolveArrayList_lf?.add(this.text?.toString()!!)
                            } else if (type.equals("RF")) {
                                serviceSelection?.issueResolveArrayList_rf?.add(this.text?.toString()!!)
                            } else if (type.equals("LR")) {
                                serviceSelection?.issueResolveArrayList_lr?.add(this.text?.toString()!!)
                            } else if (type.equals("RR")) {
                                serviceSelection?.issueResolveArrayList_rr?.add(this.text?.toString()!!)
                            }
//                             = this.text.toString()
                        }
                    )
                )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        try {
            TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    private fun fillUpAddServicedetail() {
        onView(withId(R.id.ivAddTechnicalSuggestion)).perform(customScrollTo, click());

        try {
            BaseRobot().doOnView(
                withId(R.id.suggestionsRecycView), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    4,
                    click()
                )
            )
            onView(withId(R.id.suggestionsRecycView))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        4,
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
            val moresuggestion = onView(withId(R.id.edtMoreSuggestion))
            serviceSelection?.moreSuggestions = getText(moresuggestion)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        onView(withId(R.id.ivDueDate)).perform(customScrollTo, click())
        Thread.sleep(1000)
        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click());

        tyreLFTyreSelection("LF")
        Thread.sleep(1000)
        tyreRFTyreSelection("RF")
        Thread.sleep(1000)
        tyreLRTyreSelection("LR")
        Thread.sleep(1000)
        tyreRRTyreSelection("RR")

        submitServiceAndGoToCompletedService()

//        onView(withId(R.id.cardtyreConfig)).perform(customScrollTo, click())
//        onView(withId(R.id.ivAddServices)).perform(customScrollTo, click())
//
//        removeWheelBalancingService()
//
//        BaseRobot().doOnView(withId(R.id.scroll), closeSoftKeyboard(),
//            swipeUp())
//
//        removeNitrogenService()


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
        BaseRobot().doOnView(withId(R.id.scroll), closeSoftKeyboard(),
            swipeDown())

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
            return CoreMatchers.allOf(withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(ScrollView::class.java),
                isAssignableFrom(HorizontalScrollView::class.java),
                isAssignableFrom(NestedScrollView::class.java)))
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

        BaseRobot().doOnView(
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

        val patternLF = onView(withId(R.id.tvSelectedPattern))
        val sizeLF = onView(withId(R.id.tvSelectedSize))
        Assert.assertEquals(serviceSelection?.vehiclePattern_lf, getText(patternLF))
        Assert.assertEquals(serviceSelection?.vehicleSize_lf, getText(sizeLF))
        Assert.assertEquals(serviceSelection?.psi_in, serviceSelection?.psi_in)
        Assert.assertEquals(serviceSelection?.psi_out, serviceSelection?.psi_out)
        Assert.assertEquals(serviceSelection?.weight, serviceSelection?.weight)

        onView(withId(R.id.tvManufacturingDate)).perform(customScrollTo, scrollTo())

        val manuDate = onView(withId(R.id.tvManufacturingDate))
        Assert.assertEquals(serviceSelection?.manuFacturingDate_lf, getText(manuDate))
        Thread.sleep(3000)

        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre2)).perform(customScrollTo, click())
        Thread.sleep(3000)

        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre3)).perform(customScrollTo, click())
        Thread.sleep(3000)

        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre4)).perform(customScrollTo, click())
        Thread.sleep(3000)


        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        Thread.sleep(1000)
        TestCasesForAllServiceSingleTyre.BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())
        Thread.sleep(1000)

        print("data" + serviceSelection?.vehicleMake_lf)
        print("data" + serviceSelection?.vehicleMake_lr)
        print("data" + serviceSelection?.vehicleMake_rf)
        print("data" + serviceSelection?.vehicleMake_rr)

        print("data" + serviceSelection?.vehiclePattern_lf)
        print("data" + serviceSelection?.vehiclePattern_lr)
        print("data" + serviceSelection?.vehiclePattern_rf)
        print("data" + serviceSelection?.vehiclePattern_rr)

        print("data" + serviceSelection?.vehicleSize_lf)
        print("data" + serviceSelection?.vehicleSize_lr)
        print("data" + serviceSelection?.vehicleSize_rf)
        print("data" + serviceSelection?.vehicleSize_rr)

        print("data" + serviceSelection?.manuFacturingDate_lf)
        print("data" + serviceSelection?.manuFacturingDate_lr)
        print("data" + serviceSelection?.manuFacturingDate_rf)
        print("data" + serviceSelection?.manuFacturingDate_rr)

        print("data" + serviceSelection?.sidewell_lf)
        print("data" + serviceSelection?.sidewell_lr)
        print("data" + serviceSelection?.sidewell_rf)
        print("data" + serviceSelection?.sidewell_rr)

    }
}