package com.jerry.request_shiro.shiro.init

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.jerry.request_core.Core
import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.register.ShiroConfigRegister
import com.tencent.mmkv.MMKV

class ShiroInit: ContentProvider() {
    override fun onCreate(): Boolean {
        Core.inject(mutableListOf(ShiroConfigRegister::class.java))
        context?.let {
            ShiroUtils.context = it
            MMKV.initialize(it)
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}