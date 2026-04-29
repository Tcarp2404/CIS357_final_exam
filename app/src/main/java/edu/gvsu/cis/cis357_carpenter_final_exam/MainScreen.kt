package edu.gvsu.cis.cis357_carpenter_final_exam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import coil.compose.AsyncImage
import edu.gvsu.cis.cis357_carpenter_final_exam.data.model.DisneyCharacter
import edu.gvsu.cis.cis357_carpenter_final_exam.ui.viewmodel.DisneyViewModel

val StarredColor = Color(0xFFFFF176)
val UnstarredColor = Color(0xFFE3F2FD)

@Composable
fun MainScreen(
    viewModel: DisneyViewModel,
    onCharacterClick: (DisneyCharacter) -> Unit
) {
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val starredIds by viewModel.starredIds.collectAsState()
    val nextPage by viewModel.nextPage.collectAsState()
    val prevPage by viewModel.prevPage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(8.dp)) {

        // Search bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search by name or film...") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.search(searchQuery) }) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage ?: "", color = Color.Red)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(characters) { character ->
                    val isStarred = character.id in starredIds
                    CharacterListItem(
                        character = character,
                        isStarred = isStarred,
                        onClick = { onCharacterClick(character) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Prev / Next page buttons (extra credit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { prevPage?.let { viewModel.fetchCharacters(it) } },
                enabled = prevPage != null
            ) { Text("← Prev") }

            Button(
                onClick = { nextPage?.let { viewModel.fetchCharacters(it) } },
                enabled = nextPage != null
            ) { Text("Next →") }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Sort buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.sortByName() }) { Text("Sort: Name") }
            Button(onClick = { viewModel.sortByFilmCountDesc() }) { Text("Sort: Films ↓") }
        }
    }
}

@Composable
fun CharacterListItem(
    character: DisneyCharacter,
    isStarred: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isStarred) StarredColor else UnstarredColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = character.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(character.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "Feature: ${character.films.size} films, ${character.tvShows.size} TV shows",
                fontSize = 13.sp
            )
            Text(
                "Allies: ${character.allies.size}, Enemies: ${character.enemies.size}",
                fontSize = 13.sp
            )
        }
    }
}