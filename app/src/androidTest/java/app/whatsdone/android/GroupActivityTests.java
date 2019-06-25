package app.whatsdone.android;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.whatsdone.android.ui.activity.GroupsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GroupActivityTests{

    @Rule public ActivityTestRule<GroupsActivity> activityScenarioRule =
            new ActivityTestRule<>(GroupsActivity.class);

    @Test
    public void listGoesOverTheFold() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
}