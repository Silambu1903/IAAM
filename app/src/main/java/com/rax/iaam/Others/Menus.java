package com.rax.iaam.Others;

import android.util.SparseIntArray;

import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class Menus {
    public static SparseIntArray menus = new SparseIntArray();

    public static void addMenus() {
        menus.append(1, R.id.nav_dash_asset);
        menus.append(2, R.id.nav_dash_workSche);
        menus.append(3, R.id.nav_dash_clockTime);
        menus.append(4, R.id.nav_dash_maintenance);
        menus.append(5, R.id.nav_organization);
        menus.append(6, R.id.nav_shift);
        menus.append(7, R.id.nav_inventory);
        menus.append(8, R.id.nav_generate_qr);
        menus.append(9, R.id.nav_settings);
        menus.append(10, R.id.nav_user);
        menus.append(11, R.id.nav_user_role);
        menus.append(12, R.id.nav_user_assign);
        menus.append(13, R.id.nav_installation);
        menus.append(14, R.id.nav_dash_ambience);
        menus.append(15, R.id.nav_events);
    }

    public static int[] getMenus(int[] menuIds) {
        List<Integer> tempList = new ArrayList<>();
        for (int menuId : menuIds) {
            tempList.add(getMenuAt(menuId));
        }
        int[] menus = new int[tempList.size()];
        int i = 0;
        while (i < menus.length) {
            menus[i] = tempList.get(i);
            i++;
        }
        return menus;
    }

    public static int getMenuAt(int menuId) {
        return menus.get(menuId);
    }
}
