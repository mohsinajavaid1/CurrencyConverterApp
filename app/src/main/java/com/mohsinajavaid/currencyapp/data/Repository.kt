package com.mohsinajavaid.currencyapp.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(dataSource: CurrencyDataSource) {
    val remote = dataSource
}
