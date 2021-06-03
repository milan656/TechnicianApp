package com.walkins.aapkedoorstep

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.walkins.aapkedoorstep.activity.LoginActivity
import com.walkins.aapkedoorstep.activity.MainActivity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.delay
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCases {

    @get:Rule
    open val mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java
    )

    @Test
    fun loginFunctionality() {
        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        Espresso.onView(ViewMatchers.withId(R.id.edtLoginEmail)).perform(ViewActions.typeText(UnitTestVariables.invalidNumber))

        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())

//        Espresso.onView(withId(R.id.edtLoginEmail)).perform(ViewActions.typeText(invalidNumber_2))

        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        BaseRobot().doOnView(ViewMatchers.withId(R.id.btnOk), ViewActions.closeSoftKeyboard(), ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.edtLoginEmail)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(UnitTestVariables.validNumber)
        )

        Espresso.onView(ViewMatchers.withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )
        Thread.sleep(5000)

        verifyOTP()
    }

    private fun verifyOTP() {

//        click when no OTP entered
        Espresso.onView(ViewMatchers.withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

//        enterInvalidOTP()

        enterValidOTP()

//        Espresso.onView(ViewMatchers.withId(R.id.btnVerify)).perform(
//            ViewActions.closeSoftKeyboard(),
//            ViewActions.click()
//        )
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
            ViewActions.typeText("1"),

        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp2)).perform(
            ViewActions.typeText("2"),

        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp3)).perform(
            ViewActions.typeText("1"),

        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp4)).perform(
            ViewActions.typeText("2"),ViewActions.closeSoftKeyboard(),

        )

        Thread.sleep(1000)
    }

    private fun enterInvalidOTP() {
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp1)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp2)).perform(
            ViewActions.typeText("5"),
            ViewActions.click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp3)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(ViewMatchers.withId(R.id.edtOtp4)).perform(
            ViewActions.typeText("5"),ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

        Espresso.onView(withId(R.id.scrollVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.swipeUp()
        )

    }

    private fun NavigateMainDashboard() {

        BaseRobot().doOnView(ViewMatchers.withId(R.id.llhome), ViewActions.closeSoftKeyboard(), ViewActions.click())

        BaseRobot().doOnView(
            ViewMatchers.withId(R.id.recyclerView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        navigateToServiceListScreen()
    }

    private fun navigateToServiceListScreen() {
        BaseRobot().doOnView(
            ViewMatchers.withId(R.id.serviceRecycView), ViewActions.closeSoftKeyboard(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

//        navigateToAddServiceDetailScreen()

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