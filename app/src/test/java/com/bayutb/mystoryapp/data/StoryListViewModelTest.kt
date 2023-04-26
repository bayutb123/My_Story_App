package com.bayutb.mystoryapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.bayutb.mystoryapp.utils.DataDummy
import com.bayutb.mystoryapp.utils.MainDispatcherRule
import com.bayutb.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private val dataDummy = DataDummy.generateDummy(15)
    private val emptyData = ArrayList<StoryList>()

    @Test
    fun `Stories Loaded Successful`() = runTest {
        val data: PagingData<StoryList> = StoryPagingSource.snapshot(dataDummy.items)
        val expected = MutableLiveData<PagingData<StoryList>>()
        expected.value = data

        Mockito.`when`(storyRepository.fetchStories()).thenReturn(expected)

        val storyListViewModel = StoryListViewModel(storyRepository)
        val actualData : PagingData<StoryList> = storyListViewModel.storyList.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualData)

        assertNotNull(differ.snapshot())

        assertEquals(dataDummy.items, differ.snapshot())
        assertEquals(dataDummy.items.size, differ.snapshot().size)
        assertEquals(dataDummy.items[0], differ.snapshot()[0])
    }

    @Test
    fun `Stories Not Loaded, return size must be 0`() = runTest {
        val data: PagingData<StoryList> = StoryPagingSource.snapshot(emptyData)
        val expected = MutableLiveData<PagingData<StoryList>>()
        expected.value = data

        Mockito.`when`(storyRepository.fetchStories()).thenReturn(expected)

        val storyListViewModel = StoryListViewModel(storyRepository)
        val actualData : PagingData<StoryList> = storyListViewModel.storyList.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualData)

        assertNotNull(differ.snapshot())

        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryList>>>() {
    companion object {
        fun snapshot(items: List<StoryList>) : PagingData<StoryList> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryList>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryList>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}