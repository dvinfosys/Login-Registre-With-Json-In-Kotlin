package com.dvinfosys.loginregistrationforkotlin

import android.content.Context
import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear


class SessionManager {
    private val TAG = SessionManager::class.java.simpleName
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var _context: Context? = null
    var PRIVATE_MODE = 0
    private val PREF_NAME = "MyPrefs"
    private val KEY_IS_LOGGEDIN = "IsLogin"
    private val KEY_RegistrationID = "RegistrationID"
    var cartListName: ArrayList<String> = ArrayList()
    var cartListPrice: ArrayList<Int> = ArrayList()
    var cartListQty: ArrayList<String> = ArrayList()
    var cartListImageUri: ArrayList<String> = ArrayList()

    constructor(_context: Context?) {
        this._context = _context
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    public fun setLogin(boolean: Boolean) {
        editor!!.putBoolean(KEY_IS_LOGGEDIN, boolean)
        editor!!.commit()
    }

    public fun IsLogin(): Boolean {
        return pref!!.getBoolean(KEY_IS_LOGGEDIN, false)
    }

    public fun getRegistrationID(): String {
        return pref!!.getString(KEY_RegistrationID, "")
    }

    public fun setRegistrationID(RegisterID: String) {
        editor!!.putString(KEY_RegistrationID, RegisterID)
        editor!!.commit()
    }

    var Registration: String
        get() = pref!!.getString(KEY_RegistrationID, "")
        set(value) = pref!!.edit().putString(KEY_RegistrationID, value).apply()
    var IsLogin:Boolean
        get() = pref!!.getBoolean(KEY_RegistrationID,false)
        set(value) = pref!!.edit().putBoolean(KEY_IS_LOGGEDIN,value).apply()


}