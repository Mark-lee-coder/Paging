package com.afzaalahmadzeeshan.android.paging.datasources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.afzaalahmadzeeshan.android.paging.models.Country
import com.afzaalahmadzeeshan.android.paging.utils.CountriesDb

class PositionalCountryDataSource: PositionalDataSource<Country>() {
    private val TAG: String = "PositionalCountryDataSource"
    private val source = CountriesDb.getCountries()
    private var batchSize = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Country>) {
        Log.v(TAG, "loadInitial called")
        batchSize = params.requestedLoadSize

        val list = mutableListOf<Country>()
        for (i in params.requestedStartPosition..params.requestedLoadSize) {
            if (i == source.size) {
                break
            }
            list.add(source.get(i))
        }

        Log.v(TAG, "loadInitial created a list of ${list.size} items...")
        callback.onResult(list.orEmpty(), params.requestedStartPosition, list.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Country>) {
        Log.v(TAG, "loadInitial called")
        val list = mutableListOf<Country>()
        for (i in params.startPosition..(params.startPosition + batchSize)) {
            if (i == source.size) {
                break
            }
            list.add(source.get(i))
        }

        Log.v(TAG, "loadInitial created a list of ${list.size} items...")
        callback.onResult(list.orEmpty())
    }
}

class PositionalCountryDataSourceFactory: DataSource.Factory<Int, Country>() {
    var dataSource = MutableLiveData<PositionalCountryDataSource>()
    lateinit var latestSource: PositionalCountryDataSource

    override fun create(): DataSource<Int, Country> {
        latestSource = PositionalCountryDataSource()
        dataSource.postValue(latestSource)

        return latestSource
    }
}