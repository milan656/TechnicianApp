package com.walkins.aapkedoorstep

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import androidx.test.rule.ActivityTestRule
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.blankManufacturingDate
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.invalidNumber
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.invalidNumber_2
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.select_1_service
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.select_2_service
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.select_3_service
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.select_4_service
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.singleSelection
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.twoletterManufacturingDate
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.validManufacturingDate
import com.walkins.aapkedoorstep.UnitTestVariables.Companion.validNumber
import com.walkins.aapkedoorstep.activity.MainActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class TechnicianUnitTest {

    @get: Rule
    open val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java
    )

    @Test
    open fun login() {
        loginOnTechnician()
    }

    private fun loginOnTechnician() {
        Espresso.onView(withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        Espresso.onView(withId(R.id.edtLoginEmail)).perform(ViewActions.typeText(invalidNumber))

        Espresso.onView(withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        BaseRobot().doOnView(withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())

//        Espresso.onView(withId(R.id.edtLoginEmail)).perform(ViewActions.typeText(invalidNumber_2))

        Espresso.onView(withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        Espresso.onView(withId(R.id.edtLoginEmail)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(validNumber)
        )

        Espresso.onView(withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )
        Thread.sleep(5000)

        verifyOTP()
    }

    private fun verifyOTP() {

//        click when no OTP entered
        Espresso.onView(withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        enterInvalidOTP()

        Espresso.onView(withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        BaseRobot().doOnView(withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())


        enterValidOTP()

        Espresso.onView(withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        NavigateMainDashboard()
    }

    private fun enterValidOTP() {
        Espresso.onView(withId(R.id.edtOtp1)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp2)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("2"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp3)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp4)).perform(
            ViewActions.clearText(),
            ViewActions.typeText("2"),
            ViewActions.click()
        )
    }

    private fun enterInvalidOTP() {
        Espresso.onView(withId(R.id.edtOtp1)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp2)).perform(
            ViewActions.typeText("5"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp3)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp4)).perform(
            ViewActions.typeText("5"),
            ViewActions.click()
        )
    }

    private fun NavigateMainDashboard() {

        BaseRobot().doOnView(withId(R.id.llhome), ViewActions.closeSoftKeyboard(), ViewActions.click())

        BaseRobot().doOnView(
            withId(R.id.recyclerView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        navigateToServiceListScreen()
    }

    private fun navigateToServiceListScreen() {
        BaseRobot().doOnView(
            withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        navigateToAddServiceDetailScreen()

    }

    private fun navigateToAddServiceDetailScreen() {

        skipServiceFlow()

//        select service message
        BaseRobot().doOnView(withId(R.id.cardtyreConfig), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivAddServices), ViewActions.closeSoftKeyboard(), ViewActions.click())

        if (select_1_service) {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        }
        if (select_2_service) {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    ViewActions.click()
                )
            )
        }
        if (select_3_service) {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    ViewActions.click()
                )
            )
        }
        if (select_4_service) {
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    ViewActions.click()
                )
            )
            BaseRobot().doOnView(
                withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    3,
                    ViewActions.click()
                )
            )
        }

        BaseRobot().doOnView(withId(R.id.cardtyreConfig), ViewActions.closeSoftKeyboard(), ViewActions.click())

        tyreLFTyreSelection()

        tyreRFTyreSelection()

        tyreLRTyreSelection()

        tyreRRTyreSelection()

    }

    private fun tyreRRTyreSelection() {

        BaseRobot().doOnView(withId(R.id.ivTyre1), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehicleBrandScreeneSelection()
    }

    private fun tyreLRTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre2), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehicleBrandScreeneSelection()
    }

    private fun tyreRFTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre3), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehicleBrandScreeneSelection()
    }

    private fun tyreLFTyreSelection() {
        BaseRobot().doOnView(withId(R.id.ivTyre1), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehicleBrandScreeneSelection()
    }

    private fun skipServiceFlow() {
        BaseRobot().doOnView(withId(R.id.tvSkipService), ViewActions.closeSoftKeyboard(), ViewActions.click())

        BaseRobot().doOnView(withId(R.id.btnConfirm), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        pendingReasonRecycView
        BaseRobot().doOnView(
            withId(R.id.pendingReasonRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                ViewActions.click()
            )
        )

        BaseRobot().doOnView(withId(R.id.ivClose), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        BaseRobot().doOnView(withId(R.id.btnConfirm), ViewActions.closeSoftKeyboard(), ViewActions.click())
    }

    private fun navigateToVehicleBrandScreeneSelection() {
        val number = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.gridviewRecycMake_), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number,
                ViewActions.click()
            )
        )

        if (!singleSelection) {
            BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), ViewActions.click())
        }
//        btnNext
        BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehiclePatternScreen()
    }

    private fun navigateToVehiclePatternScreen() {
        val number = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.gridviewRecycModel), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number,
                ViewActions.click()
            )
        )
        if (!singleSelection) {
            BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), ViewActions.click())
        }

//        btnNext
        BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVehicleSizeScreen()
    }

    private fun navigateToVehicleSizeScreen() {
        BaseRobot().doOnView(
            withId(R.id.gridviewRecycModel), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        if (!singleSelection) {
            BaseRobot().doOnView(withId(R.id.chkRF), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkLR), ViewActions.closeSoftKeyboard(), ViewActions.click())
            BaseRobot().doOnView(withId(R.id.chkRR), ViewActions.closeSoftKeyboard(), ViewActions.click())
        }
//        btnNext
        BaseRobot().doOnView(withId(R.id.btnNext), ViewActions.closeSoftKeyboard(), ViewActions.click())

        navigateToVisualDetailPage()
    }

    private fun navigateToVisualDetailPage() {
//        btnDone
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(ViewActions.typeText(blankManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(ViewActions.typeText(twoletterManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
//        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(ViewActions.typeText(inva))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        Espresso.onView(withId(R.id.edtManufaturingDate)).perform(ViewActions.typeText(validManufacturingDate))
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivOkSideWell), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivSugShoulder), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivReqTreadWear), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivSugTreadDepth), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivOkRimDamage), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivOkRimDamage), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.ivReqbubble), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.btnDone), ViewActions.closeSoftKeyboard(), ViewActions.click())

        fillUpAddServicedetail()

    }

    private fun fillUpAddServicedetail() {
        BaseRobot().doOnView(withId(R.id.btnSubmitAndComplete), ViewActions.closeSoftKeyboard(), ViewActions.click())
        BaseRobot().doOnView(withId(R.id.cardtechinicalSuggestion), ViewActions.closeSoftKeyboard(), ViewActions.click())

        val number = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.suggestionsRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number,
                ViewActions.click()
            )
        )
        val number1 = (0..4).random()
        BaseRobot().doOnView(
            withId(R.id.suggestionsRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                number1,
                ViewActions.click()
            )
        )


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
}