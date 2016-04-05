/*
 * Copyright 2016 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.flexbox.test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.PositionAssertions.isBottomAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.PositionAssertions.isRightAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.PositionAssertions.isTopAlignedWith;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.google.android.libraries.flexbox.FlexboxLayout;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration tests for {@link FlexboxLayout}.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class FlexboxAndroidTest {

    @Rule
    public ActivityTestRule<FlexboxTestActivity> mActivityRule =
            new ActivityTestRule<>(FlexboxTestActivity.class);

    @Test
    public void testLoadFromLayoutXml() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_simple);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        assertNotNull(flexboxLayout);
        assertThat(flexboxLayout.getFlexDirection(), is(FlexboxLayout.FLEX_DIRECTION_ROW_REVERSE));
        assertThat(flexboxLayout.getJustifyContent(), is(FlexboxLayout.JUSTIFY_CONTENT_CENTER));
        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_CENTER));
        assertThat(flexboxLayout.getAlignItems(), is(FlexboxLayout.ALIGN_ITEMS_CENTER));
        assertThat(flexboxLayout.getChildCount(), is(1));

        View child = flexboxLayout.getChildAt(0);
        FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) child.getLayoutParams();
        assertThat(lp.order, is(2));
        assertThat(lp.flexGrow, is(1));
        assertThat(lp.alignSelf, is(FlexboxLayout.LayoutParams.ALIGN_SELF_STRETCH));
    }

    @Test
    public void testOrderAttribute_fromLayoutXml() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_order_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        assertNotNull(flexboxLayout);
        assertThat(flexboxLayout.getChildCount(), is(4));
        // order: -1, index 1
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(0)).getText().toString(),
                is(String.valueOf(2)));
        // order: 0, index 2
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(1)).getText().toString(),
                is(String.valueOf(3)));
        // order: 1, index 3
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(2)).getText().toString(),
                is(String.valueOf(4)));
        // order: 2, index 0
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(3)).getText().toString(),
                is(String.valueOf(1)));
    }

    @Test
    public void testOrderAttribute_fromCode() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_order_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        TextView fifth = createTextView(activity, String.valueOf(5), 0);
        TextView sixth = createTextView(activity, String.valueOf(6), -10);
        flexboxLayout.addView(fifth);
        flexboxLayout.addView(sixth);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        assertThat(flexboxLayout.getChildCount(), is(6));
        // order: -10, index 5
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(0)).getText().toString(),
                is(String.valueOf(6)));
        // order: -1, index 1
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(1)).getText().toString(),
                is(String.valueOf(2)));
        // order: 0, index 2
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(2)).getText().toString(),
                is(String.valueOf(3)));
        // order: 0, index 4
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(3)).getText().toString(),
                is(String.valueOf(5)));
        // order: 1, index 3
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(4)).getText().toString(),
                is(String.valueOf(4)));
        // order: 2, index 0
        assertThat(((TextView) flexboxLayout.getReorderedChildAt(5)).getText().toString(),
                is(String.valueOf(1)));
    }

    @Test
    public void testFlexWrap_wrap() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flex_wrap_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        assertThat(flexboxLayout.getFlexWrap(), is(FlexboxLayout.FLEX_WRAP_WRAP));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // The third text view should be placed below the first one
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
    }

    @Test
    public void testFlexWrap_nowrap() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flex_wrap_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_NOWRAP);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        assertThat(flexboxLayout.getFlexWrap(), is(FlexboxLayout.FLEX_WRAP_NOWRAP));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // But the flexWrap attribute is set to FLEX_WRAP_NOWRAP, the third text view is placed
        // to the right of the second one and overflowing the parent FlexboxLayout.
        onView(withId(R.id.text3)).check(isRightOf(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
    }

    @Test
    public void testFlexWrap_wrap_reverse() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flex_wrap_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP_REVERSE);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        assertThat(flexboxLayout.getFlexWrap(), is(FlexboxLayout.FLEX_WRAP_WRAP_REVERSE));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // There should be two flex lines same as FLEX_WRAP_WRAP, but the layout starts from bottom
        // to top in FLEX_WRAP_WRAP_REVERSE
        onView(withId(R.id.text3)).check(isAbove(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isAbove(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
    }

    @Test
    public void testFlexboxLayout_wrapContent() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flexbox_wrap_content);
            }
        });
        // The parent FlexboxLayout's layout_width and layout_height are set to wrap_content
        // The size of the FlexboxLayout is aligned with three text views.

        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isBottomAlignedWith(withId(R.id.flexbox_layout)));

        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isBottomAlignedWith(withId(R.id.flexbox_layout)));

        onView(withId(R.id.text3)).check(isRightOf(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBottomAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightAlignedWith(withId(R.id.flexbox_layout)));
    }

    @Test
    public void testFlexboxLayout_wrapped_with_ScrollView() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flexbox_wrapped_with_scrollview);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));

        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));

        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));

        // The heightMode of the FlexboxLayout is set as MeasureSpec.UNSPECIFIED, the height of the
        // layout will be expanded to include the all children views
        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(flexboxLayout.getHeight(), is(textView1.getHeight() + textView3.getHeight()));
    }

    @Test
    public void testJustifyContent_flexStart() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_justify_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        assertThat(flexboxLayout.getJustifyContent(), is(FlexboxLayout.JUSTIFY_CONTENT_FLEX_START));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isRightOf(withId(R.id.text2)));
    }

    @Test
    public void testJustifyContent_flexEnd() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_justify_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_FLEX_END);

        assertThat(flexboxLayout.getJustifyContent(), is(FlexboxLayout.JUSTIFY_CONTENT_FLEX_END));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isLeftOf(withId(R.id.text3)));
        onView(withId(R.id.text1)).check(isLeftOf(withId(R.id.text2)));
    }

    @Test
    public void testJustifyContent_center() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_justify_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_CENTER);

        assertThat(flexboxLayout.getJustifyContent(), is(FlexboxLayout.JUSTIFY_CONTENT_CENTER));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightOf(withId(R.id.text2)));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        int space = flexboxLayout.getWidth() - textView1.getWidth() - textView2.getWidth() -
                textView3.getWidth();
        space = space / 2;
        assertThat(textView1.getLeft(), is(space));
        assertThat(flexboxLayout.getRight() - textView3.getRight(), is(space));
    }

    @Test
    public void testJustifyContent_spaceBetween() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_justify_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_SPACE_BETWEEN);

        assertThat(flexboxLayout.getJustifyContent(),
                is(FlexboxLayout.JUSTIFY_CONTENT_SPACE_BETWEEN));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightAlignedWith(withId(R.id.flexbox_layout)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        int space = flexboxLayout.getWidth() - textView1.getWidth() - textView2.getWidth() -
                textView3.getWidth();
        space = space / 2;
        assertThat(textView2.getLeft() - textView1.getRight(), is(space));
        assertThat(textView3.getLeft() - textView2.getRight(), is(space));
    }

    @Test
    public void testJustifyContent_spaceAround() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_justify_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_SPACE_AROUND);

        assertThat(flexboxLayout.getJustifyContent(),
                is(FlexboxLayout.JUSTIFY_CONTENT_SPACE_AROUND));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        int space = flexboxLayout.getWidth() - textView1.getWidth() - textView2.getWidth() -
                textView3.getWidth();
        space = space / 6; // Divide by the number of children * 2
        assertThat(textView1.getLeft(), is(space));
        assertThat(textView2.getLeft() - textView1.getRight(), is(space * 2));
        assertThat(textView3.getLeft() - textView2.getRight(), is(space * 2));
        assertThat(flexboxLayout.getRight() - textView3.getRight(), is(space));
    }

    @Test
    public void testFlexGrow_withExactParentLength() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_flex_grow_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        // the third TextView is expanded to the right edge of the FlexboxLayout
        onView(withId(R.id.text3)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isRightOf(withId(R.id.text2)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView3.getWidth(),
                is(flexboxLayout.getWidth() - textView1.getWidth() - textView2.getWidth()));
    }

    @Test
    public void testAlignContent_stretch() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_STRETCH));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        // the third TextView is wrapped to the next flex line
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));

        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        int flexLineCrossSize = flexboxLayout.getHeight() / 2;
        // Two flex line's cross sizes are expanded to the half of the height of the FlexboxLayout.
        // The third textView's top should be aligned witht the second flex line.
        assertThat(textView3.getTop(), is(flexLineCrossSize));
    }

    @Test
    public void testAlignContent_flexStart() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setAlignContent(FlexboxLayout.ALIGN_CONTENT_FLEX_START);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_FLEX_START));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        // the third TextView is wrapped to the next flex line
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView3.getTop(), is(textView1.getHeight()));
    }

    @Test
    public void testAlignContent_flexEnd() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setAlignContent(FlexboxLayout.ALIGN_CONTENT_FLEX_END);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_FLEX_END));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBottomAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isAbove(withId(R.id.text3)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isAbove(withId(R.id.text3)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView1.getBottom(), is(flexboxLayout.getBottom() - textView3.getHeight()));
    }

    @Test
    public void testAlignContent_center() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setAlignContent(FlexboxLayout.ALIGN_CONTENT_CENTER);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_CENTER));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));

        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        int spaceAboveAndBottom = flexboxLayout.getHeight() - textView1.getHeight() - textView3
                .getHeight();
        spaceAboveAndBottom /= 2;

        assertThat(textView1.getTop(), is(spaceAboveAndBottom));
        assertThat(textView3.getBottom(), is(flexboxLayout.getBottom() - spaceAboveAndBottom));
    }

    @Test
    public void testAlignContent_spaceBetween() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setAlignContent(FlexboxLayout.ALIGN_CONTENT_SPACE_BETWEEN);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_SPACE_BETWEEN));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBottomAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
    }

    @Test
    public void testAlignContent_spaceAround() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        flexboxLayout.setAlignContent(FlexboxLayout.ALIGN_CONTENT_SPACE_AROUND);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_SPACE_AROUND));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);

        int spaceAround = flexboxLayout.getHeight() - textView1.getHeight() - textView3.getHeight();
        spaceAround /= 4; // Divide by the number of flex lines * 2

        assertThat(textView1.getTop(), is(spaceAround));
        assertThat(textView3.getTop(), is(textView1.getBottom() + spaceAround * 2));
    }

    @Test
    public void testAlignContent_stretch_parentWrapContent() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_content_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        ViewGroup.LayoutParams parentLp = flexboxLayout.getLayoutParams();
        parentLp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        flexboxLayout.setLayoutParams(parentLp);

        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_STRETCH));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        // the third TextView is wrapped to the next flex line
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));

        // alignContent is only effective if the parent's height/width mode is MeasureSpec.EXACTLY.
        // The size of the flex lines don't change even if the alingContent is set to
        // ALIGN_CONTENT_STRETCH
        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView3.getTop(), is(textView1.getHeight()));
    }

    @Test
    public void testAlignItems_stretch() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_stretch_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        assertThat(flexboxLayout.getAlignItems(), is(FlexboxLayout.ALIGN_ITEMS_STRETCH));
        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));

        // There should be 2 flex lines in the layout with the given layout.
        int flexLineSize = flexboxLayout.getHeight() / 2;
        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView1.getHeight(), is(flexLineSize));
        assertThat(textView2.getHeight(), is(flexLineSize));
        assertThat(textView3.getHeight(), is(flexLineSize));
    }

    @Test
    public void testAlignSelf_stretch() throws Throwable {
        final FlexboxTestActivity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(R.layout.activity_align_self_stretch_test);
            }
        });
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);

        onView(withId(R.id.text1)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text1)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isTopAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text2)).check(isRightOf(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isLeftAlignedWith(withId(R.id.flexbox_layout)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text1)));
        onView(withId(R.id.text3)).check(isBelow(withId(R.id.text2)));

        // There should be 2 flex lines in the layout with the given layout.
        // Only the first TextView's alignSelf is set to ALIGN_SELF_STRETCH
        int flexLineSize = flexboxLayout.getHeight() / 2;
        TextView textView1 = (TextView) activity.findViewById(R.id.text1);
        TextView textView2 = (TextView) activity.findViewById(R.id.text2);
        TextView textView3 = (TextView) activity.findViewById(R.id.text3);
        assertThat(textView1.getHeight(), is(flexLineSize));
        assertThat(textView2.getHeight(), not(flexLineSize));
        assertThat(textView3.getHeight(), not(flexLineSize));
    }

    private TextView createTextView(Context context, String text, int order) {
        TextView textView = new TextView(context);
        textView.setText(text);
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.order = order;
        textView.setLayoutParams(lp);
        return textView;
    }
}
