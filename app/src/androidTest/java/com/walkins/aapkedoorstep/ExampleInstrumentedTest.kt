package com.walkins.aapkedoorstep

import android.R
import android.content.Context
import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.walkins.aapkedoorstep.activity.MainActivity
import org.hamcrest.Matcher

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    var invalidNumber: String? = "0900222414"
    var validNumber: String? = "8469838953"
    var targetContext: Context?=null

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.walkins.aapkedoorstep", appContext?.packageName)
//    }

    /*@Test
    fun login() {
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

        Espresso.onView(withId(R.id.edtLoginEmail)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(validNumber)
        )

        Espresso.onView(withId(R.id.btnLoginToDashBoard)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.edtOtp1)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp2)).perform(
            ViewActions.typeText("2"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp3)).perform(
            ViewActions.typeText("1"),
            ViewActions.click()
        )
        Espresso.onView(withId(R.id.edtOtp4)).perform(
            ViewActions.typeText("2"),
            ViewActions.click()
        )

        Espresso.onView(withId(R.id.btnVerify)).perform(
            ViewActions.closeSoftKeyboard(),
            ViewActions.click()
        )

    }*/

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