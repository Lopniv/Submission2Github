package com.android.submission2github.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.TABLE_NAME
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.AVATAR_URL
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.LOGIN
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.NODE_ID
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.TYPE
import com.android.submission2github.model.Item
import java.util.ArrayList

class UserFavoriteHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserFavoriteHelper? = null

        fun getInstance(context: Context): UserFavoriteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: UserFavoriteHelper(context)
                }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /**
     * Ambil data dari semua favorite yang ada di dalam database
     *
     * @return cursor hasil queryAll
     */
    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "${BaseColumns._ID} ASC"
        )
    }

    /**
     * Ambil data dari favorite berdasarakan parameter id
     *
     * @param id id favorite yang dicari
     * @return cursor hasil queryAll
     */
    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "${BaseColumns._ID} = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }

    /**
     * Simpan data ke dalam database
     *
     * @param values nilai data yang akan di simpan
     * @return long id dari data yang baru saja di masukkan
     */
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    /**
     * Update data dalam database
     *
     * @param id     data dengan id berapa yang akan di update
     * @param values nilai data baru
     * @return int jumlah data yang ter-update
     */
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${BaseColumns._ID} = ?", arrayOf(id))
    }

    /**
     * Delete data dalam database
     *
     * @param id data dengan id berapa yang akan di delete
     * @return int jumlah data yang ter-delete
     */
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${BaseColumns._ID} = '$id'", null)
    }

    /**
     * Gunakan method ini untuk ambil semua favorite yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil getGetAllNotes berbentuk array model favorite
     */
    fun getAllNotes(): ArrayList<Item> {
        val arrayList = ArrayList<Item>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
                "${BaseColumns._ID} ASC", null)
        cursor.moveToFirst()
        var favorite: Item
        if (cursor.count > 0) {
            do {
                favorite = Item()
                favorite.id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                favorite.login = cursor.getString(cursor.getColumnIndexOrThrow(LOGIN))
                favorite.nodeId = cursor.getString(cursor.getColumnIndexOrThrow(NODE_ID))
                favorite.avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_URL))
                favorite.type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))

                arrayList.add(favorite)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    /**
     * Gunakan method ini untuk insertNote
     *
     * @param favorite model item yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    fun insertNote(favorite: Item): Long {
        val args = ContentValues()
        args.put(LOGIN, favorite.login)
        args.put(NODE_ID, favorite.nodeId)
        args.put(AVATAR_URL, favorite.avatarUrl)
        args.put(TYPE, favorite.type)
        return database.insert(DATABASE_TABLE, null, args)
    }


    /**
     * Gunakan method ini untuk updateNote
     *
     * @param favorite model item yang akan diubah
     * @return int jumlah dari row yang ter-updateNote, jika tidak ada yang diupdate maka nilainya 0
     */
    fun updateNote(favorite: Item): Int {
        val args = ContentValues()
        args.put(LOGIN, favorite.login)
        args.put(NODE_ID, favorite.nodeId)
        args.put(AVATAR_URL, favorite.avatarUrl)
        args.put(TYPE, favorite.type)
        return database.update(DATABASE_TABLE, args, BaseColumns._ID + "= '" + favorite.id + "'", null)
    }

    /**
     * Gunakan method ini untuk deleteNote
     *
     * @param id id yang akan di deleteNote
     * @return int jumlah row yang di deleteNote
     */
    fun deleteNote(id: Int): Int {
        return database.delete(TABLE_NAME, "${BaseColumns._ID} = '$id'", null)
    }
}