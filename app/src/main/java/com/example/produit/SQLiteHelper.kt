package com.example.produit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
data class Product(
    val id: Int = 0,
    val name: String,
    val price: String,
    val description: String
)
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PRICE TEXT,
                $COLUMN_DESCRIPTION TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
        }
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun getProducts(): List<Product> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS, null, null, null, null, null, null
        )
        val products = mutableListOf<Product>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            products.add(Product(id, name, price, description))
        }
        cursor.close()
        db.close()
        return products
    }

    fun updateProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
        }
        db.update(TABLE_PRODUCTS, values, "$COLUMN_ID = ?", arrayOf(product.id.toString()))
        db.close()
    }

    fun deleteProduct(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_PRODUCTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "products.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
    }
}
