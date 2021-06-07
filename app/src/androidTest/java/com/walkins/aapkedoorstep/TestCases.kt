package com.walkins.aapkedoorstep

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.walkins.aapkedoorstep.activity.LoginActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestCases {
    private val PREFERENCE_NAME = "MyPref"
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var preferences: SharedPreferences? = null

    //    private var serviceSelection: SingleServiceTyreSelection? = null
    private var serviceSelection: MultipleServiceMultipleTyreSelection? = null

    @Before
    fun setSharedPref() {
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

        preferences = targetContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//        serviceSelection = SingleServiceTyreSelection()
        serviceSelection = MultipleServiceMultipleTyreSelection()

        print("login_" + "" + preferences?.getBoolean("isLogin", false))
    }

    @get:Rule
    open val mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java
    )

    @Test
    fun loginFunctionality() {
        print("login_" + "" + preferences?.getBoolean("isLogin", false))
/*        if (preferences?.getBoolean("isLogin", false) == true) {
            mainScreenView()
        } else {
        }*/
        loginView()
    }

    private fun loginView() {

        Thread.sleep(500)
        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnLoginToDashBoard), ViewActions.closeSoftKeyboard(), click())

        onView(withId(R.id.edtLoginEmail)).perform(ViewActions.typeText(serviceSelection?.invalidNumber))

        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            click()
        )

        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), click())

        onView(withId(R.id.edtLoginEmail)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(serviceSelection?.validNumber)
        )

        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            click()
        )

        verifyOTP()
    }

    private fun verifyOTP() {

//        click when no OTP entered
        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnVerify), ViewActions.closeSoftKeyboard(), click())

        enterInvalidOTP()

        Espresso.onView(ViewMatchers.withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            click()
        )

        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), click())

        enterValidOTP()

        Espresso.onView(ViewMatchers.withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            click()
        )
//        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())
//
//        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())

//        Thread.sleep(1000)
//        Espresso.onView(withId(R.id.scrollVerify)).perform(
//            ViewActions.closeSoftKeyboard(),
//            ViewActions.swipeUp()
//        )
//
//        enterValidOTP()
//
//        Espresso.onView(ViewMatchers.withId(R.id.btnVerify)).perform(
//            ViewActions.closeSoftKeyboard(),
//            ViewActions.click()
//        )

        NavigateMainDashboard()
    }

    private fun enterValidOTP() {
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp1)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("1"),

            )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp2)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("2"),

            )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp3)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("1"),

            )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp4)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("2"), ViewActions.closeSoftKeyboard(),

            )

        Thread.sleep(1000)
    }

    private fun enterInvalidOTP() {
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp1)).perform(
            ViewActions.typeText("1"),
            click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp2)).perform(
            ViewActions.typeText("5"),
            click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp3)).perform(
            ViewActions.typeText("1"),
            click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp4)).perform(
            ViewActions.typeText("5"), ViewActions.closeSoftKeyboard(),
            click()
        )

        Espresso.onView(withId(R.id.scrollVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.swipeUp()
        )

    }

    private fun NavigateMainDashboard() {

        BaseRobot().doOnView(ViewMatchers.withId(R.id.llhome), ViewActions.closeSoftKeyboard(), click())

        Thread.sleep(4000)
        BaseRobot().doOnView(
            ViewMatchers.withId(R.id.recyclerView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        navigateToServiceListScreen()
    }

    private fun navigateReportScreen() {
        BaseRobot().doOnView(ViewMatchers.withId(R.id.llReport), ViewActions.closeSoftKeyboard(), click())
//        reportRecycView
        BaseRobot().doOnView(
            ViewMatchers.withId(R.id.reportRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

    }

    private fun navigateToServiceListScreen() {

        BaseRobot().doOnView(
            ViewMatchers.withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
//        BaseRobot().doOnView(
//            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
//            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                hasDescendant(withText(serviceSelection?.typeRotation_service)),
//                ViewActions.click()
//            )
//        )

        navigateToAddServiceDetailScreen()

    }

    private fun navigateToAddServiceDetailScreen() {

        skipServiceFlow()

//        select service message
        BaseRobot().doOnView(withId(R.id.cardtyreConfig), ViewActions.closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.ivAddServices), ViewActions.closeSoftKeyboard(), click())

//        if (!serviceSelection?.nitrogen_refill_service.equals("")) {
//            BaseRobot().doOnView(
//                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
//                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                    hasDescendant(withText(serviceSelection?.nitrogen_refill_service)),
//                    ViewActions.click()
//                )
//            )
//        }
//        if (!serviceSelection?.wheel_balancing_service.equals("")) {
//            BaseRobot().doOnView(
//                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
//                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                    hasDescendant(withText(serviceSelection?.wheel_balancing_service)),
//                    ViewActions.click()
//                )
//            )
//        }
//
//        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(),
//            ViewActions.swipeUp())
//
//        if (!serviceSelection?.nitrogen_topup_service.equals("")) {
//            BaseRobot().doOnView(
//                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
//                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                    hasDescendant(withText(serviceSelection?.nitrogen_topup_service)),
//                    ViewActions.click()
//                )
//            )
//        }
//        if (!serviceSelection?.typeRotation_service.equals("")) {
//            BaseRobot().doOnView(
//                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
//                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                    hasDescendant(withText(serviceSelection?.typeRotation_service)),
//                    ViewActions.click()
//                )
//            )
//        }

//        scroll
        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(),
            ViewActions.swipeUp())
        BaseRobot().doOnView(withId(R.id.ivAddTyreConfig), ViewActions.closeSoftKeyboard(), click())
//        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(),
//            ViewActions.swipeUp())

//        removeWheelBalancingService()

        tyreLFTyreSelection()
        tyreRFTyreSelection()
        tyreLRTyreSelection()
        tyreRRTyreSelection()

        editFlowPerform()
//
//        fillUpAddServicedetail()

    }

    private fun editFlowPerform() {

        tyreLFTyreSelection()
        tyreRFTyreSelection()
        tyreLRTyreSelection()
        tyreRRTyreSelection()
    }

    private fun removeWheelBalancingService() {
//        Remove service Flow
        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(), ViewActions.swipeDown())

        BaseRobot().doOnView(withId(R.id.ivAddServices), ViewActions.closeSoftKeyboard(), click())
        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

//        remove cancel
        BaseRobot().doOnView(withId(R.id.btn_cancel), ViewActions.closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

//        remove ok
        BaseRobot().doOnView(withId(R.id.btn_ok), ViewActions.closeSoftKeyboard(), click())

    }

    private fun tyreRRTyreSelection() {

        BaseRobot().doOnView(withId(R.id.ivTyre4), ViewActions.closeSoftKeyboard(), click())

        navigateToVehicleBrandScreeneSelection(4)
    }

    private fun tyreLRTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre2), ViewActions.closeSoftKeyboard(), click())

        navigateToVehicleBrandScreeneSelection(3)
    }

    private fun tyreRFTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre3), ViewActions.closeSoftKeyboard(), click())

        navigateToVehicleBrandScreeneSelection(2)
    }

    private fun tyreLFTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre1), ViewActions.closeSoftKeyboard(), click())

        navigateToVehicleBrandScreeneSelection(0)
    }

    private fun skipServiceFlow() {
        BaseRobot().doOnView(withId(R.id.tvSkipService), ViewActions.closeSoftKeyboard(), click())

        BaseRobot().doOnView(withId(R.id.btnConfirm), ViewActions.closeSoftKeyboard(), click())
//        pendingReasonRecycView
        BaseRobot().doOnView(
            withId(R.id.pendingReasonRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )

        BaseRobot().doOnView(withId(R.id.ivClose), ViewActions.closeSoftKeyboard(), click())
//        BaseRobot().doOnView(withId(R.id.btnConfirm), ViewActions.closeSoftKeyboard(), ViewActions.click())
    }

    private fun navigateToVehicleBrandScreeneSelection(number: Int) {


        try {
//            onView(withId(R.id.gridviewRecycMake_)).isDisplayed()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycMake_), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), click())
            }
            //view is displayed logic

        } catch (e: Exception) {
            e.printStackTrace()
            //view not displayed logic
        }

//        btnNext

        try {
            BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), click())
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


        navigateToVehiclePatternScreen()
    }

    private fun navigateToVehiclePatternScreen() {
        try {
            val number = (0..4).random()
//            onView(withId(R.id.gridviewRecycModel)).isDisplayed()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    number,
                    click()
                )
            )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), click())
            }
            //view is displayed logic
        } catch (e: Exception) {
            e.printStackTrace()
            //view not displayed logic
        }

//        btnNext
        try {
            BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), click())
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


        navigateToVehicleSizeScreen()
    }

    private fun navigateToVehicleSizeScreen() {
        try {
//            onView(withId(R.id.gridviewRecycModel)).isDisplayed()
            BaseRobot().doOnView(
                withId(R.id.gridviewRecycModel), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
            if (!serviceSelection?.singleTyreSelection!!) {
                BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), click())
                BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), click())
            }
            //view is displayed logic
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            //view not displayed logic
        }


//        btnNext
        try {
            BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), click())
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


        navigateToVisualDetailPage()
    }

    private fun navigateToVisualDetailPage() {
//        btnDone

//        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())

        try {
            Espresso.onView(withId(R.id.edtManufaturingDate)).perform(clearText(),ViewActions.typeText(serviceSelection?.blankManufacturingDate))
        } catch (e: Exception) {
            e.printStackTrace()
            //view not displayed logic
        }

        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(clearText(),ViewActions.typeText(serviceSelection?.zeroManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(serviceSelection?.twoletterManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(serviceSelection?.inValidWeekOfYearManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(serviceSelection?.inValidYearManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(serviceSelection?.validManufacturingDate))
//        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())


//        if (onView(withId(R.id.ivOkSideWell)).isDisplayed()) {
//            BaseRobot().doOnView(withId(R.id.ivOkSideWell), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        }

//        BaseRobot().doOnView(withId(R.id.ivSugShoulder), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.visualScroll), ViewActions.closeSoftKeyboard(),
            ViewActions.swipeUp())
//        BaseRobot().doOnView(withId(R.id.ivReqTreadWear), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.ivSugTreadDepth), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.ivOkRimDamage), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.ivReqbubble), ViewActions.closeSoftKeyboard(), ViewActions.click())

//        BaseRobot().doOnView(
//            withId(R.id.issueResolvedRecycView), ViewActions.closeSoftKeyboard(),
//            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                2,
//                ViewActions.click()
//            )
//        )

        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), click())


    }

    private fun fillUpAddServicedetail() {
//        BaseRobot().doOnView(withId(R.id.btnSubmitAndComplete), ViewActions.closeSoftKeyboard(), ViewActions.click())

        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(), ViewActions.swipeUp())
        BaseRobot().doOnView(withId(R.id.ivAddTechnicalSuggestion), ViewActions.closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(), ViewActions.swipeUp())

        val number = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.suggestionsRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number,
                click()
            )
        )
        val number1 = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.suggestionsRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number1,
                click()
            )
        )

//        BaseRobot().doOnView(withId(R.id.ivBack), ViewActions.closeSoftKeyboard(), ViewActions.click())

        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(), ViewActions.swipeDown())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        BaseRobot().doOnView(withId(R.id.btn_cancel), ViewActions.closeSoftKeyboard(), click())

        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        BaseRobot().doOnView(withId(R.id.btn_ok), ViewActions.closeSoftKeyboard(), click())

        BaseRobot().doOnView(withId(R.id.scroll), ViewActions.closeSoftKeyboard(), ViewActions.swipeDown())
        BaseRobot().doOnView(withId(R.id.ivAddTechnicalSuggestion), ViewActions.closeSoftKeyboard(), click())

        BaseRobot().doOnView(withId(R.id.ivDueDate), ViewActions.closeSoftKeyboard(), click())
        BaseRobot().doOnView(withId(R.id.btn_confirm), ViewActions.closeSoftKeyboard(), click())

//        navigateToServiceListScreen("2")
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
            waitMillis: Int = 15000,
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
}