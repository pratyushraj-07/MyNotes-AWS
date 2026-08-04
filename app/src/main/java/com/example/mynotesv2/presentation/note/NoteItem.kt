package com.example.mynotesv2.presentation.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotesv2.domain.model.Note
import com.example.mynotesv2.util.toFormattedDate

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        onClick = onNoteClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = note.title,
                    modifier = Modifier.weight(1f).padding(end = 6.dp),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE57373)
                    )
                }
            }

            Text(
                modifier = Modifier.padding(6.dp),
                text = note.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(start = 6.dp).fillMaxWidth(),
                text = note.timestamp.toFormattedDate(),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    MaterialTheme {
        NoteItem(
            note = Note(
                id = 1,
                title = "Shopping List",
                description = "Buy milk, eggs, and breaafsfasfsfsfsafasfsfasfsdfsdfsafsdfsfsafdafsdfsfd",
                timestamp = System.currentTimeMillis()
            ),
            onNoteClick = {},
            onDeleteClick = {}
        )
    }
}