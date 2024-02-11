package org.co.notes.di

import org.co.notes.domain.DataSource

expect class AppModule {
    val dataSource: DataSource
}