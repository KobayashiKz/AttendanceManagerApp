package com.kk.attendancemanagerapp.attendancemanagerapp.data.resource

import android.content.SharedPreferences

interface DataSource {

    fun saveSettingData(pref: SharedPreferences, name: String?, attendanceDay: String, attendance: String?,
                        breakStart: String?, breakEnd: String?, unit: Int, salaried: String?)

    fun saveCurrentTime(pref: SharedPreferences, time: Long)

    fun getAttendanceTime(pref: SharedPreferences, time: Long): Long

    fun saveTodayAttendanceTime(pref: SharedPreferences, time: Long?)

    fun getTodayAttendanceTime(pref: SharedPreferences): Long

    fun setCompleteInitialSetting(pref: SharedPreferences)

    fun getCompleteInitialSetting(pref: SharedPreferences): Boolean
}