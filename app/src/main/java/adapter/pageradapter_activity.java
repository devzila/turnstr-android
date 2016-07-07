package adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.Camera_fragment;

import Fragments.Photo;
import Fragments.Photo;
import Fragments.Profile_fragment;
import Fragments.Video_fragment;

public class pageradapter_activity extends FragmentPagerAdapter {

    public pageradapter_activity(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new Profile_fragment();
            case 1:
                // Games fragment activity
                return new Camera_fragment();
            case 2:
                // Movies fragment activity
                return new Video_fragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
