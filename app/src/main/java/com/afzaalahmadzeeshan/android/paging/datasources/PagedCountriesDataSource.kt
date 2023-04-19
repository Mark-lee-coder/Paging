package com.afzaalahmadzeeshan.android.paging.datasources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.afzaalahmadzeeshan.android.paging.models.Country
import com.afzaalahmadzeeshan.android.paging.utils.CountriesDb

class PagedCountriesDataSource: PageKeyedDataSource<Int, Country>() {
    private val TAG: String = "PagedCountriesDataSource"
    private val source = CountriesDb.getCountries()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Country>) {
        Log.v(TAG, "loadInitial called")
        val pagedCountries = source.filter { country ->
            country.page == 1
        }
        Log.v(TAG, "loadInitial created a list of ${pagedCountries.size} size...")

        callback.onResult(pagedCountries, null, 2)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Country>) {
        Log.v(TAG, "loadBefore called with key ${params.key}")
        val list = source.filter { country ->
            country.page == params.key
        }
        Log.v(TAG, "loadBefore returning list for page ${params.key} with ${list.size} items...")

        callback.onResult(list, params.key - 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Country>) {
        Log.v(TAG, "loadBefore called with key ${params.key}")
        val list = source.filter { country ->
            country.page == params.key
        }
        Log.v(TAG, "loadBefore returning list for page ${params.key} with ${list.size} items...")

        callback.onResult(list, params.key + 1)
    }
}

class PagedCountriesDataSourceFactory: DataSource.Factory<Int, Country>() {
    private var dataSource = MutableLiveData<PagedCountriesDataSource>()
    private lateinit var latestSource: PagedCountriesDataSource

    override fun create(): DataSource<Int, Country> {
        latestSource = PagedCountriesDataSource()
        dataSource.postValue(latestSource)

        return latestSource
    }
}