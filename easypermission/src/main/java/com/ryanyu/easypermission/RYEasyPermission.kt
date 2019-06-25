package com.epiccomm.fsee.utils

import android.Manifest
import android.Manifest.permission_group.LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import java.util.*

/**
 * Created by Ryan Yu on 1/2/2019.
 */

object RYEasyPermission {
    var listener: RYEasyPermissionResult? = null

    val CAMERA: Int = 0
    val VIDEO: Int = 1
    val READ: Int = 2
    val WRITE: Int = 3
    val CALL_PHONE: Int = 4
    val LOCATION: Int = 5
    val RECORD_VOICE: Int = 6

    val CAMERA_PERMISSION: ArrayList<String> = ArrayList()
    val VIDEO_PERMISSION: ArrayList<String> = ArrayList()
    val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

    val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val RECORD_VOICE_PERMISSION: ArrayList<String> = ArrayList()
    val CALL_PERMISSION = Manifest.permission.CALL_PHONE
    val LOCATION_PERMISSION: ArrayList<String> = ArrayList()

    val REQUEST_CODE = 9133

    var requestPermission: Int? = null
    var key = 999
    init {
        LOCATION_PERMISSION.add(Manifest.permission.ACCESS_FINE_LOCATION)
        LOCATION_PERMISSION.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        CAMERA_PERMISSION.add(Manifest.permission.CAMERA)
        CAMERA_PERMISSION.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        RECORD_VOICE_PERMISSION.add(Manifest.permission.RECORD_AUDIO)
        RECORD_VOICE_PERMISSION.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        VIDEO_PERMISSION.add(Manifest.permission.CAMERA)
        VIDEO_PERMISSION.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * check permission
     *
     * @param activity Activity
     * @param permission String
     * @return Boolean
     */
    fun checkPermission(activity: Activity, permission: Int): Boolean {
        var result = false
        var realPermission = getRealPermission(permission)
        for (permission in realPermission) {
            result = ActivityCompat.checkSelfPermission(activity as Context, permission) == PackageManager.PERMISSION_GRANTED
            if (!result) break
        }
        return !result
    }

    fun getRealPermission(permission: Int): ArrayList<String> {
        var temp: ArrayList<String> = ArrayList()
        when (permission) {
            CAMERA -> temp = CAMERA_PERMISSION
            VIDEO -> temp = VIDEO_PERMISSION
            LOCATION -> temp = LOCATION_PERMISSION
            READ -> temp.add(WRITE_PERMISSION)
            WRITE -> temp.add(WRITE_PERMISSION)
            CALL_PHONE -> temp.add(CALL_PERMISSION)
            RECORD_VOICE -> temp = RECORD_VOICE_PERMISSION
        }

        return temp
    }

    /**
     * Check Permission and get Permission
     *
     * @param activity Activity
     * @param permission String
     * @param listener RYEasyPermissionResult
     */
    fun checkPermissionAndRequest(activity: Activity, permission: Int, listener: RYEasyPermissionResult,key : Int) {
        this.key = key
        var result = false
        requestPermission = permission
        this.listener = listener
        var permissions = getRealPermission(permission)

        for (permission in permissions) {
            result = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
            if (!result) break
        }

        if (result)
            listener.onPermissionResult(true, permission,key)
        else
            requestPermission(activity, permissions.toTypedArray()
        )

    }

    /**
     * Request Permission
     *
     * @param activity Activity
     * @param permissionList Array<String>
     */
    fun requestPermission(activity: Activity, permissionList: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissionList, REQUEST_CODE)
    }

    /**
     * must copy ->  'RYEasyPermission.onRequestPermissionsResult(requestCode,permissions,grantResults)' to activity onRequestPermissionsResult function
     *
     * @param requestCode Int
     * @param permissions Array<out String>
     * @param grantResults IntArray
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    listener?.onPermissionResult(false, requestPermission,key)
                    return
                }
            }
            listener?.onPermissionResult(true, requestPermission,key)

            listener = null
            requestPermission = null
        }
    }
}