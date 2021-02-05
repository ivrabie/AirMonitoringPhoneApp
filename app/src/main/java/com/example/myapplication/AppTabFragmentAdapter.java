package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class AppTabFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();
    FragmentManager fm;
    public AppTabFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }


    public void addFragment(Fragment fragmentLayout, String title)
    {
        fragments.add(fragmentLayout);
        fragmentTitles.add(title);
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("AppFragAdapter", "get position " + String.valueOf(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
