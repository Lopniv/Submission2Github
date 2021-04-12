package com.android.submission2github.helper

import android.database.Cursor
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.model.Item
import java.util.ArrayList

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Item> {
        val notesList = ArrayList<Item>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NAME))
                val email = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.EMAIL))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOCATION))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.COMPANY))
                val blog = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.BLOG))
                notesList.add(Item(login, id, avatarUrl, name, company, blog, location, email))
            }
        }
        return notesList
    }
}