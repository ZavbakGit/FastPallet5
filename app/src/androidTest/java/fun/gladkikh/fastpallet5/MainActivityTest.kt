package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.ui.activity.MainActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)


    @Before
    fun setup() {
        activityTestRule.launchActivity(null)
    }


    @Test
    fun check_show_progress(){
        (activityTestRule as MainActivity).showProgress.value = true

        println("Yes")

//        onView(withId(R.id.rv_notes)).perform(scrollToPosition<NotesRVAdapter.ViewHolder>(1))
//        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }
}