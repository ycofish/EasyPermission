package com.epiccomm.fsee.utils

import android.Manifest
import android.Manifest.permission_group.LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat


/**
 * Created by Ryan Yu on 1/2/2019.
 */

interface RYEasyPermissionResult {
    fun onPermissionResult(result: Boolean, permission: Int?,requestCode:Int)

}