package com.walkins.aapkedoorstep

import android.content.Context
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
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.walkins.aapkedoorstep.activity.LoginActivity
import com.walkins.aapkedoorstep.services.AllServiceSingleTyreSelection
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TestCasesForAllServiceSingleTyre {

    private var serviceSelection: AllServiceSingleTyreSelection? = null

   @Before
    fun setSharedPref() {
        serviceSelection = AllServiceSingleTyreSelection()
    }

    @get:Rule
    open val mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java
    )
    @Test
    fun loginFunctionality() {

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
        onView(withId(R.id.scrollVerify)).perform(closeSoftKeyboard(), swipeUp())
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

        Thread.sleep(2500)
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

//        fillUpAddServicedetail()

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())
        tyreLFTyreSelection()
        Thread.sleep(200)
        tyreRFTyreSelection()
        Thread.sleep(200)
        fillUpAddServicedetail()

        onView(withId(R.id.ivAddTyreConfig)).perform(customScrollTo, click())
        tyreRRTyreSelection()
        Thread.sleep(200)
        tyreLRTyreSelection()

        Thread.sleep(1000)

//        submitServiceAndGoToCompletedService()

        if (serviceSelection?.editFlowEnable!!) {
            editFlowPerform()
        }

//        if (serviceSelection?.vehicleWiseStoreData!!) {
//            serviceSelection?.vehicleWiseStoreData = false
//            AnotherVehicleDetailFillup()
//        }
    }

    private fun submitServiceAndGoToCompletedService() {
        BaseRobot().doOnView(withId(R.id.btnSubmitAndComplete), closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.btn_ok), closeSoftKeyboard(), click())

        BaseRobot().doOnView(withId(R.id.ivBack), closeSoftKeyboard(), click())

        BaseRobot().doOnView(withId(R.id.llCompleted), closeSoftKeyboard(), click())
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

        tyreLFTyreSelection()
        tyreRFTyreSelection()
        tyreLRTyreSelection()
        tyreRRTyreSelection()
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

    private fun tyreRRTyreSelection() {
//        BaseRobot().doOnView(withId(R.id.ivTyre4), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre4)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(4)
    }

    private fun tyreLRTyreSelection() {
//        BaseRobot().doOnView(withId(R.id.ivTyre2), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre2)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(3)
    }

    private fun tyreRFTyreSelection() {
//        BaseRobot().doOnView(withId(R.id.ivTyre3), closeSoftKeyboard(), click())
        onView(withId(R.id.ivTyre3)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(2)
    }

    private fun tyreLFTyreSelection() {
//        Assert.assertEquals("true","false")
        onView(withId(R.id.ivTyre1)).perform(customScrollTo, click())
        navigateToVehicleBrandScreeneSelection(0)
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

    private fun navigateToVehicleBrandScreeneSelection(number: Int) {

        try {
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycMake_), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
            onView(withId(R.id.gridviewRecycMake_))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        recyclerChildAction<TextView>(R.id.tvmakeName) {
                            print("vehicle_make-->" + this.text?.toString())
                            serviceSelection?.vehicleMake = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                        }
                    )
                )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), closeSoftKeyboard(), click())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                BaseRobot().doOnView(withId(R.id.ivEditVehicleMake), closeSoftKeyboard(), click())

                Thread.sleep(150)
                BaseRobot().doOnView(
                    withId(R.id.gridviewRecycMake_), closeSoftKeyboard(),
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        4,
                        click()
                    )
                )
                onView(withId(R.id.gridviewRecycMake_))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            2,
                            recyclerChildAction<TextView>(R.id.tvmakeName) {
                                print("vehicle_make-->" + this.text?.toString())
                                serviceSelection?.vehicleMake = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                            }
                        )
                    )

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(200)
        navigateToVehiclePatternScreen()
    }

    private fun navigateToVehiclePatternScreen() {
        try {
            val number = (0..4).random()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
            onView(withId(R.id.gridviewRecycModel))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        number,
                        recyclerChildAction<TextView>(R.id.ivVehicleImage) {
                            print("vehicle_make-->" + this.text?.toString())
                            serviceSelection?.vehiclePattern = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                        }
                    )
                )

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
                onView(withId(R.id.gridviewRecycModel))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            1,
                            recyclerChildAction<TextView>(R.id.ivVehicleImage) {
                                print("vehicle_make-->" + this.text?.toString())
                                serviceSelection?.vehiclePattern = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                            }
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(200)
        navigateToVehicleSizeScreen()
    }

    private fun navigateToVehicleSizeScreen() {
        try {
//            onView(withId(R.id.gridviewRecycModel)).isDisplayed()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
            onView(withId(R.id.gridviewRecycModel))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        recyclerChildAction<TextView>(R.id.ivVehicleImage) {
                            print("vehicle_make-->" + this.text?.toString())
                            serviceSelection?.vehicleSize = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                        }
                    )
                )
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
                onView(withId(R.id.gridviewRecycModel))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            1,
                            recyclerChildAction<TextView>(R.id.ivVehicleImage) {
                                print("vehicle_make-->" + this.text?.toString())
                                serviceSelection?.vehicleSize = this.text.toString()
//                            selects?.selected_vehicleMake = this.text.toString()
                            }
                        )
                    )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), closeSoftKeyboard(), click())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Thread.sleep(200)
        navigateToVisualDetailPage()
    }

    private fun navigateToVisualDetailPage() {

        Thread.sleep(1000)

        if (!serviceSelection?.editFlowEnable!!) {

            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            /*try {
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
                        typeText(serviceSelection?.twoletterManufacturingDate))
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        typeText(serviceSelection?.twoletterManufacturingDate))
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
                        typeText(serviceSelection?.inValidWeekOfYearManufacturingDate))
                } else {
                    onView(withId(R.id.edtManufaturingDate)).perform(
                        typeText(serviceSelection?.inValidWeekOfYearManufacturingDate))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
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
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                onView(withId(R.id.edtManufaturingDate)).perform(
                    clearText(),
                    typeText(serviceSelection?.validManufacturingDate))

                val manuDate = onView(withId(R.id.edtManufaturingDate))
                serviceSelection?.manuFacturingDate = getText(manuDate)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
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

            serviceSelection?.sidewell = "OK"
            serviceSelection?.shoulder = "SUG"
            serviceSelection?.treadWear = "REQ"
            serviceSelection?.treadDepth = "SUG"
            serviceSelection?.rimDamage = "OK"
            serviceSelection?.bubble = "REQ"

                BaseRobot().doOnView(withId(R.id.visualScroll), closeSoftKeyboard(),
                    swipeUp())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                val number = (1..3).random()
                BaseRobot().doOnView(
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
                            serviceSelection?.issueResolveArrayList?.add(this.text?.toString()!!)
//                             = this.text.toString()
                        }
                    )
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                BaseRobot().doOnView(withId(R.id.btnDone), closeSoftKeyboard(), click())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun fillUpAddServicedetail() {
        onView(withId(R.id.ivAddTechnicalSuggestion)).perform(customScrollTo, click());

        try {
            val number1 = (0..4).random()
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
}