package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeviceAppFragmentAdapter extends FragmentStatePagerAdapter {

    public List<Fragment> fragments = new ArrayList<>();
    public List<String> fragmentTitles = new ArrayList<>();
    FragmentManager fm;
    public DeviceAppFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.fragmentTitles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment f = (Fragment)object;
        for(int i = 0; i < getCount(); i++) {

            Fragment item = (Fragment) getItem(i);
            if(item.equals(f)) {
                // item still exists in dataset; return position
                return i;
            }
        }
        return  super.getItemPosition(object);
    }

    public void addFragment(Fragment fragmentLayout, String title)
    {
        fragments.add(fragmentLayout);
        fragmentTitles.add(title);
    }

    public void deleteFragment(Fragment fragmentLayout, String title)
    {
        fragments.remove(fragmentLayout);
        fragmentTitles.remove(title);
    }
}
