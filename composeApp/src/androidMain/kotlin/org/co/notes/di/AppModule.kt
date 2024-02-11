package org.co.notes.di

import android.content.Context
import org.co.notes.database.DataSourceImpl
import org.co.notes.database.DatabaseDriverFactory
import org.co.notes.database.NotesDatabase
import org.co.notes.domain.DataSource

actual class AppModule(private val context: Context) {
    actual val dataSource:DataSource by lazy {
        DataSourceImpl(NotesDatabase(driver = DatabaseDriverFactory(context).createDriver()))
    }
}