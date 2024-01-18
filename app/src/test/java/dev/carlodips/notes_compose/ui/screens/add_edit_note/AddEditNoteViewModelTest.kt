package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.carlodips.notes_compose.MainDispatcherRule
import dev.carlodips.notes_compose.data.local.repository.FakeNoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AddEditNoteViewModelTest {

    // private val savedStateHandle = SavedStateHandle(mapOf(NavigationItem.AddEditNote.NOTE_ID to 1))
    private val savedStateHandle: SavedStateHandle = Mockito.mock(SavedStateHandle::class.java)

    private lateinit var viewModel: AddEditNoteViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeNoteRepository()

    @Before
    fun setup() {
        viewModel = AddEditNoteViewModel(
            repository = repository,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `onSaveNoteClick() while title and body are blank, returns hasMessage = true`() =
        runTest {
            viewModel.onSaveNote()

            val uiState = viewModel.uiState

            assertThat(uiState.value.hasMessage).isTrue()
        }

    @Test
    fun `onSaveNoteClick() while title is not blank and body is blank, returns hasMessage = false`() =
        runTest {
            viewModel.onTitleChange("title")
            viewModel.onSaveNote()

            assertThat(!viewModel.uiState.value.hasMessage).isTrue()
        }

    @Test
    fun `onSaveNoteClick() while title is blank and body is not blank, returns hasMessage = false`() =
        runTest {
            viewModel.onBodyChange("body")
            viewModel.onSaveNote()

            assertThat(!viewModel.uiState.value.hasMessage).isTrue()
        }

    @Test
    fun `onSaveNoteClick() while title and body are not blank, returns hasMessage = false`() =
        runTest {
            viewModel.onTitleChange("title")
            viewModel.onBodyChange("body")

            viewModel.onSaveNote()

            assertThat(viewModel.uiState.value.hasMessage).isFalse()
        }

    @Test
    fun `onSaveNoteClick() while title or body is blank, returns message == -1`() =
        runTest {
            viewModel.onTitleChange("title")
            viewModel.onBodyChange(" ")

            viewModel.onSaveNote()

            assertThat(viewModel.uiState.value.message == -1).isTrue()
        }

    @Test
    fun `onSaveNoteClick() while title and body is not blank, returns message == -1`() =
        runTest {
            viewModel.onTitleChange("title")
            viewModel.onBodyChange("body")

            viewModel.onSaveNote()

            assertThat(viewModel.uiState.value.message == -1).isTrue()
        }

    @Test
    fun `onSaveNoteClick() while title and body is not blank, returns isDoneSaving = true`() =
        runTest {
            viewModel.onTitleChange("title")
            viewModel.onBodyChange("body")

            viewModel.onSaveNote()

            assertThat(viewModel.uiState.value.isDoneSaving).isTrue()
        }

    @Test
    fun `Note should be added inside database after onSaveNoteClick()`() = runTest {
        val title = "Sample title"
        val body = "lorem ipsum"

        viewModel.onTitleChange(title)
        viewModel.onBodyChange(body)

        viewModel.onSaveNote()

        val addedNote = repository.getNotes().first().first()

        assertThat(addedNote.noteTitle == title && addedNote.noteBody == body).isTrue()
    }
}
