package com.zen.nottwitter.presentation.ui.editor

import app.cash.turbine.test
import com.zen.nottwitter.core.BaseTest
import com.zen.nottwitter.data.repository.ConfigRepository
import com.zen.nottwitter.domain.usecase.CreatePostRequest
import com.zen.nottwitter.domain.usecase.CreatePostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EditorViewModelTest : BaseTest() {

    private lateinit var viewModel: EditorViewModel
    private lateinit var state: StateFlow<EditorUIState>

    @MockK
    private lateinit var configRepository: ConfigRepository

    @MockK
    private lateinit var createPostUseCase: CreatePostUseCase

    @Before
    override fun setUp() {
        super.setUp()
        every { configRepository.getPostConfig() } returns stubTestPostConfig
        viewModel = EditorViewModel(configRepository, createPostUseCase, testDispatchers)
        state = viewModel.state
    }

    @Test
    fun `EditorViewModel on init will update state for characterLimit, isOverCharacterLimit, isPostButtonEnable`() {
        assertEquals("", state.value.message)
        assertEquals("0 / 280", state.value.characterLimit)
        assertEquals(false, state.value.isOverCharacterLimit)
        assertEquals(false, state.value.isPostButtonEnable)
    }

    @Test
    fun `onBackButtonClick with blank message and imageUriString will emit NavigateBack effect`() =
        runTest {
            viewModel.effect.test {
                viewModel.onBackButtonClick()
                assertEquals(EditorUIEffect.NavigateBack, awaitItem())
            }
        }

    @Test
    fun `onBackButtonClick with message or imageUriString will update showBackDialog state`() {
        // with message
        viewModel.onMessageChange(stubTestMessage)
        viewModel.onBackButtonClick()
        assertEquals(true, state.value.showBackDialog)
        viewModel.onBackDialogDismiss()
        assertEquals(false, state.value.showBackDialog)

        // with imageUriString
        viewModel.onMessageChange("")
        viewModel.onImageSelected(stubTestImageUrl)
        viewModel.onBackButtonClick()
        assertEquals(true, state.value.showBackDialog)
        viewModel.onBackDialogDismiss()
        assertEquals(false, state.value.showBackDialog)
    }

    @Test
    fun `onPostClick will update showPostDialog state`() {
        viewModel.onPostClick()
        assertEquals(true, state.value.showPostDialog)
    }

    @Test
    fun `onMessageChange will update state for message, characterLimit, isOverCharacterLimit, isPostButtonEnable`() {
        viewModel.onMessageChange(stubTestMessage)
        assertEquals(stubTestMessage, state.value.message)
        assertEquals("5 / 280", state.value.characterLimit)
        assertEquals(false, state.value.isOverCharacterLimit)
        assertEquals(true, state.value.isPostButtonEnable)

        val superLongText = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tristique ex id quam accumsan, et viverra tellus venenatis. Phasellus sollicitudin sit amet erat in sagittis. Aliquam ac est placerat, tincidunt risus et, feugiat risus. Pellentesque dictum sollicitudin augue. Suspendisse libero in.
        """.trimIndent()
        viewModel.onMessageChange(superLongText)
        assertEquals(superLongText, state.value.message)
        assertEquals("300 / 280", state.value.characterLimit)
        assertEquals(true, state.value.isOverCharacterLimit)
        assertEquals(false, state.value.isPostButtonEnable)
    }

    @Test
    fun `onAddImageClick will update showImagePicker state`() {
        viewModel.onAddImageClick()
        assertEquals(true, state.value.showImagePicker)
    }

    @Test
    fun `onRemoveImageClick will update imageUriString to empty string`() {
        viewModel.onImageSelected(stubTestImageUrl)
        assertEquals(stubTestImageUrl, state.value.imageUriString)
        viewModel.onRemoveImageClick()
        assertEquals("", state.value.imageUriString)
    }

    @Test
    fun `onImageClick will emit ViewImage effect`() = runTest {
        viewModel.effect.test {
            viewModel.onImageSelected(stubTestImageUrl)
            viewModel.onImageClick()
            val latestEffect = awaitItem()
            assertTrue(latestEffect is EditorUIEffect.ViewImage && latestEffect.imageUriString == stubTestImageUrl)
        }
    }

    @Test
    fun `onBackDialogDismiss will update showBackDialog state to false`() {
        viewModel.onMessageChange(stubTestMessage)
        viewModel.onBackButtonClick()
        assertEquals(true, state.value.showBackDialog)
        viewModel.onBackDialogDismiss()
        assertEquals(false, state.value.showBackDialog)
    }

    @Test
    fun `onBackDialogPositiveCtaClick will emit NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackDialogPositiveCtaClick()
            assertEquals(EditorUIEffect.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onPostDialogDismiss will update showPostDialog state to false`() {
        viewModel.onPostClick()
        assertEquals(true, state.value.showPostDialog)
        viewModel.onPostDialogDismiss()
        assertEquals(false, state.value.showPostDialog)
    }

    @Test
    fun `onPostDialogPositiveCtaClick positive flow will emit CreatePostSuccess effect`() =
        runTest {
            coEvery {
                createPostUseCase.execute(
                    CreatePostRequest(
                        stubTestMessage,
                        stubTestImageUrl
                    )
                )
            } returns stubTestPost

            viewModel.effect.test {
                viewModel.onMessageChange(stubTestMessage)
                viewModel.onImageSelected(stubTestImageUrl)
                viewModel.onPostDialogPositiveCtaClick()
                assertEquals(EditorUIEffect.CreatePostSuccess, awaitItem())
                coVerify {
                    createPostUseCase.execute(
                        CreatePostRequest(
                            stubTestMessage,
                            stubTestImageUrl
                        )
                    )
                }
            }
        }

    @Test
    fun `onPostDialogPositiveCtaClick negative flow will update errorMessage state`() {
        val stubTestException = spyk<Exception>()
        val stubTestErrorMessage = "Oops"
        coEvery {
            createPostUseCase.execute(
                CreatePostRequest(
                    stubTestMessage,
                    stubTestImageUrl
                )
            )
        } throws stubTestException
        every { configRepository.getFallbackErrorMessage() } returns stubTestFallbackErrorMessage
        every { stubTestException.localizedMessage } returns stubTestErrorMessage andThen null

        viewModel.onMessageChange(stubTestMessage)
        viewModel.onImageSelected(stubTestImageUrl)

        runTest {
            viewModel.effect.test {
                viewModel.onPostDialogPositiveCtaClick()
                assertEquals(stubTestErrorMessage, state.value.errorMessage)

                viewModel.onPostDialogPositiveCtaClick()
                assertEquals(stubTestFallbackErrorMessage, state.value.errorMessage)

                expectNoEvents()
                coVerify(exactly = 2) {
                    createPostUseCase.execute(CreatePostRequest(stubTestMessage, stubTestImageUrl))
                }
            }
        }
    }

    @Test
    fun `onPostErrorDialogDismiss will update errorMessage state to empty string`() {
        val stubTestException = spyk<Exception>()
        val stubTestErrorMessage = "Oops"
        coEvery {
            createPostUseCase.execute(
                CreatePostRequest(
                    stubTestMessage,
                    stubTestImageUrl
                )
            )
        } throws stubTestException
        every { configRepository.getFallbackErrorMessage() } returns stubTestFallbackErrorMessage
        every { stubTestException.localizedMessage } returns stubTestErrorMessage

        viewModel.onMessageChange(stubTestMessage)
        viewModel.onImageSelected(stubTestImageUrl)
        viewModel.onPostDialogPositiveCtaClick()
        assertEquals(stubTestErrorMessage, state.value.errorMessage)

        viewModel.onPostErrorDialogDismiss()
        assertEquals("", state.value.errorMessage)
    }

    @Test
    fun `onImageSelected will update state for imageUriString, showImagePicker, isPostButtonEnable`() {
        viewModel.onAddImageClick()
        assertEquals("", state.value.imageUriString)
        assertEquals(true, state.value.showImagePicker)
        assertEquals(false, state.value.isPostButtonEnable)

        viewModel.onImageSelected(stubTestImageUrl)
        assertEquals(stubTestImageUrl, state.value.imageUriString)
        assertEquals(false, state.value.showImagePicker)
        assertEquals(true, state.value.isPostButtonEnable)
    }
}