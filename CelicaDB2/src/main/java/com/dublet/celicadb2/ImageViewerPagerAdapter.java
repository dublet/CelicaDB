package com.dublet.celicadb2;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

/**
 * Created by dublet on 27/12/13.
 */
class ImageViewerPagerAdapter extends FragmentStatePagerAdapter {
    private HashMap<Integer, String> _pictureResources;
    public ImageViewerPagerAdapter(FragmentManager fm, HashMap<Integer, String> pictureResources) {
        super(fm);
        _pictureResources = pictureResources;
    }

    @Override
    public Fragment getItem(int position) {
        Integer key = _pictureResources.keySet().toArray(new Integer[_pictureResources.size()])[position];
        return ImageViewerFragment.create(key, _pictureResources.get(key));
    }

    @Override
    public int getCount() {
        return _pictureResources.size();
    }
}
