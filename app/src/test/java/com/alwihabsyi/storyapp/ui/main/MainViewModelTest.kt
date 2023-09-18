package com.alwihabsyi.storyapp.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.alwihabsyi.storyapp.data.Result
import androidx.recyclerview.widget.ListUpdateCallback
import com.alwihabsyi.storyapp.data.Repository
import com.alwihabsyi.storyapp.data.remote.ListStory
import com.alwihabsyi.storyapp.utils.DataDummy
import com.alwihabsyi.storyapp.utils.MainDispatcherRule
import com.alwihabsyi.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository
    private val dummyStoriesResponse = DataDummy.generateDummyStories()
    private val finalToken =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVZMSG5sWk9tckZsX0VBNEoiLCJpYXQiOjE2OTUwMTM4MTV9.GEFrTZlrrj3yVxVZWZs4-_AWxwze3KlDnRYmZJKK3XE"

    @SuppressLint("CheckResult")
    @Before
    fun setUp() {
        Mockito.mockStatic(Log::class.java)
    }

    @Test
    fun `when getAllStories Should Not Null and Return Success`() = runTest {
        val data: PagingData<ListStory> =
            StoryPagingSource.snapshot(dummyStoriesResponse.listStory!!)
        val expectedStories = MediatorLiveData<Result<PagingData<ListStory>>>()
        expectedStories.value = Result.Success(data)
        Mockito.`when`(repository.getAllUser(finalToken)).thenReturn(expectedStories)

        val viewModel = MainViewModel(repository)
        val actualStories: Result<PagingData<ListStory>> = viewModel.getAllStories(finalToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFFUTIL,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        val actualData = actualStories as Result.Success

        differ.submitData(actualData.data)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
        assertEquals(dummyStoriesResponse.listStory!!.size, differ.snapshot().size)
        assertEquals(dummyStoriesResponse.listStory!![0].id, differ.snapshot()[0]?.id)
    }

    @OptIn(ExperimentalPagingApi::class)
    class StoryPagingSource : RemoteMediator<Int, LiveData<List<ListStory>>>() {
        companion object {
            fun snapshot(items: List<ListStory>): PagingData<ListStory> {
                return PagingData.from(items)
            }

        }
        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, LiveData<List<ListStory>>>
        ): MediatorResult {
            return try {
                val endOfPaginationReached = true
                MediatorResult.Success(endOfPaginationReached)
            } catch (exception: Exception) {
                MediatorResult.Error(exception)
            }
        }
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}