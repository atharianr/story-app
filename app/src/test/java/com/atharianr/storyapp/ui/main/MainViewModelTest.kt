package com.atharianr.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.atharianr.storyapp.data.repository.StoryRepository
import com.atharianr.storyapp.data.source.local.entity.StoryEntity
import com.atharianr.storyapp.data.source.remote.response.AddStoryResponse
import com.atharianr.storyapp.data.source.remote.response.StoriesResponse
import com.atharianr.storyapp.data.source.remote.response.StoryDetailResponse
import com.atharianr.storyapp.data.source.remote.response.vo.ApiResponse
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.ui.adapter.StoryPagingAdapter
import com.atharianr.storyapp.utils.DataDummy
import com.atharianr.storyapp.utils.LiveDataTestUtil.getOrAwaitValue
import com.atharianr.storyapp.utils.MainDispatcherRule
import com.atharianr.storyapp.utils.PagedDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStoryData = DataDummy.generateStoriesData()
    private val dummyStoryDetailData = DataDummy.generateStoryDetailData()
    private val dummyAddStoryData = DataDummy.generateAddStoryData()
    private val dummyToken = DataDummy.dummyToken
    private val dummyId = DataDummy.dummyId
    private val description = DataDummy.dummyDescription
    private val imageMultipart = DataDummy.dummyImageMultipart

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when GetAllStories Data Should Not Null, Return Success, and have same Data Size`() =
        runTest {
            val expected = MutableLiveData<ApiResponse<StoriesResponse>>()
            expected.value = ApiResponse.success(dummyStoryData)

            Mockito.`when`(storyRepository.getAllStories(dummyToken, 0)).thenReturn(expected)

            val actual = mainViewModel.getAllStories(dummyToken, 0).getOrAwaitValue()
            Mockito.verify(storyRepository).getAllStories(dummyToken, 0)

            Assert.assertNotNull(actual)
            Assert.assertTrue(actual.status == StatusResponse.SUCCESS)
            Assert.assertEquals(dummyStoryData.listStory.size, actual.body?.listStory?.size)
        }

    @Test
    fun `when GetAllStories Network Error, Response Data Should Not Null and Return Error`() =
        runTest {
            val expected = MutableLiveData<ApiResponse<StoriesResponse>>()
            expected.value = ApiResponse.error("Error")

            Mockito.`when`(storyRepository.getAllStories(dummyToken, 0)).thenReturn(expected)

            val actual = mainViewModel.getAllStories(dummyToken, 0).getOrAwaitValue()
            Mockito.verify(storyRepository).getAllStories(dummyToken, 0)

            Assert.assertNotNull(actual)
            Assert.assertTrue(actual.status == StatusResponse.ERROR)
        }

    @Test
    fun `when GetDetailStory Data Should Not Null, Return Success, and have same Data`() = runTest {
        val expected = MutableLiveData<ApiResponse<StoryDetailResponse>>()
        expected.value = ApiResponse.success(dummyStoryDetailData)

        Mockito.`when`(storyRepository.getStoryDetail(dummyToken, dummyId)).thenReturn(expected)

        val actual = mainViewModel.getDetailStory(dummyToken, dummyId).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryDetail(dummyToken, dummyId)

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == StatusResponse.SUCCESS)
        Assert.assertEquals(dummyStoryDetailData.story, actual.body?.story)
    }

    @Test
    fun `when GetDetailStory Network Error, Response Data Should Not Null and Return Error`() =
        runTest {
            val expected = MutableLiveData<ApiResponse<StoryDetailResponse>>()
            expected.value = ApiResponse.error("Error")

            Mockito.`when`(storyRepository.getStoryDetail(dummyToken, dummyId)).thenReturn(expected)

            val actual = mainViewModel.getDetailStory(dummyToken, dummyId).getOrAwaitValue()
            Mockito.verify(storyRepository).getStoryDetail(dummyToken, dummyId)

            Assert.assertNotNull(actual)
            Assert.assertTrue(actual.status == StatusResponse.ERROR)
        }

    @Test
    fun `when addNewStory Response Data Should Not Null, Return Success`() =
        runTest {
            val expected = MutableLiveData<ApiResponse<AddStoryResponse>>()
            expected.value = ApiResponse.success(dummyAddStoryData)

            Mockito.`when`(storyRepository.addNewStory(dummyToken, imageMultipart, description))
                .thenReturn(expected)

            val actual =
                mainViewModel.addNewStory(dummyToken, imageMultipart, description).getOrAwaitValue()
            Mockito.verify(storyRepository).addNewStory(dummyToken, imageMultipart, description)

            Assert.assertNotNull(actual)
            Assert.assertTrue(actual.status == StatusResponse.SUCCESS)
        }

    @Test
    fun `when addNewStory Network Error, Response Data Should Not Null and Return Error`() =
        runTest {
            val expected = MutableLiveData<ApiResponse<AddStoryResponse>>()
            expected.value = ApiResponse.error("Error")

            Mockito.`when`(storyRepository.addNewStory(dummyToken, imageMultipart, description))
                .thenReturn(expected)

            val actual =
                mainViewModel.addNewStory(dummyToken, imageMultipart, description).getOrAwaitValue()
            Mockito.verify(storyRepository).addNewStory(dummyToken, imageMultipart, description)

            Assert.assertNotNull(actual)
            Assert.assertTrue(actual.status == StatusResponse.ERROR)
        }

    @Test
    fun `when getAllStoriesPaging Data Should Not Null, Return Success, and have same Data size`() =
        runTest {
            val dummyStoryList = DataDummy.generateStoriesData().listStory
            val pagingData = PagedDataTestUtil.snapshot(dummyStoryList)

            val expected = MutableLiveData<PagingData<StoryEntity>>()
            expected.value = pagingData

            Mockito.`when`(mainViewModel.getAllStoriesPaging()).thenReturn(expected)

            val actual = mainViewModel.getAllStoriesPaging().getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
                updateCallback = object : ListUpdateCallback {
                    override fun onInserted(position: Int, count: Int) {}
                    override fun onRemoved(position: Int, count: Int) {}
                    override fun onMoved(fromPosition: Int, toPosition: Int) {}
                    override fun onChanged(position: Int, count: Int, payload: Any?) {}
                },
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            differ.submitData(actual)
            advanceUntilIdle()
            Mockito.verify(storyRepository).getAllStoriesPaging()

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStoryList.size, differ.snapshot().size)
        }
}