package org.co.notes.di

import org.co.notes.database.DataSourceImpl
import org.co.notes.database.DatabaseDriverFactory
import org.co.notes.database.NotesDatabase
import org.co.notes.domain.DataSource

actual class AppModule {
    actual val dataSource:DataSource by lazy {
        DataSourceImpl(NotesDatabase(DatabaseDriverFactory().createDriver()))
    }
}