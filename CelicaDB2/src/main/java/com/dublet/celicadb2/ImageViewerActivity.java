package com.dublet.celicadb2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by dublet on 27/12/13.
 */

public class ImageViewerActivity extends FragmentActivity {
    public final static String BIG_PICTURES_MSG = "com.dublet.celicadb2.big_pictures";

    private ViewPager mPager;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_viewer_pager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        try {
            PagerAdapter pagerAdapter = new ImageViewerPagerAdapter(getSupportFragmentManager(),
                    (HashMap<Integer, String>)getIntent().getSerializableExtra(BIG_PICTURES_MSG));
            mPager.setAdapter(pagerAdapter);
        } catch (ClassCastException e) { Log.e("CCE", e.getMessage()); }

        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


}
