package net.alhazmy13.mediagallery.library;


import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.MenuRes;

public interface MenuHandler {
    @MenuRes
    int getMenuId();

    void onMenuItemClick(MenuItem item);

    void onMenuCreated(Menu menu);
}
