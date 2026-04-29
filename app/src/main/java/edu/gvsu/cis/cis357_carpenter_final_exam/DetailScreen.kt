package edu.gvsu.cis.cis357_carpenter_final_exam.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.gvsu.cis.cis357_carpenter_final_exam.data.model.DisneyCharacter
import edu.gvsu.cis.cis357_carpenter_final_exam.ui.viewmodel.DisneyViewModel

@Composable
fun DetailScreen(
    character: DisneyCharacter,
    viewModel: DisneyViewModel,
    onBack: () -> Unit
) {
    val starredIds by viewModel.starredIds.collectAsState()
    val isStarred = character.id in starredIds

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(character.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${character.films.size} films, ${character.tvShows.size} TV shows", fontSize = 14.sp)
            Text("Allies: ${character.allies.size}, Enemies: ${character.enemies.size}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            SectionList(title = "Films", items = character.films)
            SectionList(title = "TV Shows", items = character.tvShows)
            SectionList(title = "Allies", items = character.allies)
            SectionList(title = "Enemies", items = character.enemies)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.toggleStar(character) }) {
                Text(if (isStarred) "Unstar" else "Star")
            }
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

@Composable
fun SectionList(title: String, items: List<String>) {
    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    if (items.isEmpty()) {
        Text("  (none)", fontSize = 13.sp)
    } else {
        items.forEachIndexed { i, item ->
            Text("  ${i + 1}. $item", fontSize = 13.sp)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}