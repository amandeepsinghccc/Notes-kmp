package org.co.notes

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import theme.NoteColorCyan
import theme.NoteColorGreen
import theme.NoteColorPink
import theme.NoteColorPurple
import theme.NoteColorSkin
import theme.NoteColorYellow

@Serializable
data class NotesModel(
    @SerialName("id")
    val id:Long?= null,
    @SerialName("title")
    val title:String,
    @SerialName("body")
    val body:String,
    @SerialName("created")
    val createdDate:Long,
    @SerialName("updated")
    val updatedDate:Long,
    @SerialName("colorHex")
    val colorHex:Long = generateRandomColor(),

){
    companion object {
        val colors = listOf(0xFF9EFFFF,0xFF91F48F,0xFFFD99FF,0xFFFFF599,0xFFFF9E9E,0xFFB69CFF)
        fun generateRandomColor() = colors.random()
    }
}
data class StateUi(
    val notesList : List<NotesModel> = listOf(),
    val selectedModel: NotesModel? = null
)