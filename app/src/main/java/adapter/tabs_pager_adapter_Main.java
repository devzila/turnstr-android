package adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.Camera_fragment;

import Fragments.Explorer_fragment;
import Fragments.Like_fagment;
import Fragments.Photo;
import Fragments.Photo;
import Fragments.Profile_fragment;
import Fragments.Video_fragment;
import Fragments.home_fragment;

public class tabs_pager_adapter_Main extends FragmentPagerAdapter {
    public home_fragment homegragment = new home_fragment();
    public Explorer_fragment explorefragment = new Explorer_fragment();
    public Like_fagment likefragment = new Like_fagment();
    public Profile_fragment profilefragment = new Profile_fragment();

    public tabs_pager_adapter_Main(FragmentManager fm) {
        super(fm);
    }

        @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new home_fragment();
            case 1:
                // Games fragment activity
                return new Explorer_fragment();
            case 2:
                // Movies fragment activity
                return new Like_fagment();
            case 3:
                // Movies fragment activity
                return new Like_fagment();
            case 4:
                // Movies fragment activity
                return new Profile_fragment();

        }

        return null;
    }
//    @Override
//    public Fragment getItem(int index) {
//
//        switch (index) {
//            case 0:
//                // Top Rated fragment activity
//                return homegragment;
//            case 1:
//                // Games fragment activity
//                return explorefragment;
//            case 2:
//                // Movies fragment activity
//                return likefragment;
////            case 3:
////                // Movies fragment activity
////                return likefragment;
//            case 3:
//                // Movies fragment activity
//                return profilefragment;
//
//        }
//
//        return null;
//    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 5;
    }

}
