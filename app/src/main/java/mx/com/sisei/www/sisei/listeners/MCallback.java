package mx.com.sisei.www.sisei.listeners;

import android.support.v4.app.Fragment;

/**
 * Created by manni on 10/19/2017.
 */

    public interface MCallback{
        void addFragment(Fragment toAdd);
        void removeFragment(Fragment toRemove);
    }

