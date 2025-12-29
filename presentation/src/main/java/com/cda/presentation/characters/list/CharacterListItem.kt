package com.cda.presentation.characters.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cda.domain.model.Character
import com.cda.domain.model.LocationRef
import com.cda.presentation.common.formatInstant
import java.time.Instant

@Composable
fun CharacterListItem(
    character: Character,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(18.dp)
    val clickable = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(clickable),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = CardDefaults.outlinedCardBorder(enabled = true),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                        StatusBadge(status = character.status)
                    }

                    Spacer(modifier = Modifier.size(6.dp))

                    Text(
                        text = character.species.ifBlank { "Unknown species" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.size(10.dp))

            MetaRow(label = "Origin", value = character.origin.name.ifBlank { "#${character.origin.id}" })
            Spacer(modifier = Modifier.size(6.dp))
            MetaRow(label = "Location", value = character.lastKnownLocation.name.ifBlank { "#${character.lastKnownLocation.id}" })
            Spacer(modifier = Modifier.size(6.dp))
            MetaRow(label = "Created", value = formatInstant(character.created))
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (bg, fg) = when (status.lowercase()) {
        "alive" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        "dead" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    }

    Surface(
        color = bg,
        contentColor = fg,
        shape = RoundedCornerShape(999.dp),
        tonalElevation = 0.dp,
    ) {
        Text(
            text = status.ifBlank { "unknown" },
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            maxLines = 1,
        )
    }
}

@Composable
private fun MetaRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(72.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = value.ifBlank { "Unknown" },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewCharacterListItemAlive() {
    CharacterListItem(
        character = Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = LocationRef(id = 1, name = "Earth (C-137)"),
            lastKnownLocation = LocationRef(id = 3, name = "Citadel of Ricks"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episodeIds = emptyList(),
            created = Instant.parse("2017-11-04T18:48:46.250Z"),
        ),
    )
}

@PreviewLightDark
@Composable
private fun PreviewCharacterListItemDead() {
    CharacterListItem(
        character = Character(
            id = 2,
            name = "Birdperson",
            status = "Dead",
            species = "Bird-Person",
            type = "",
            gender = "Male",
            origin = LocationRef(id = 99, name = "Bird World"),
            lastKnownLocation = LocationRef(id = 21, name = "Planet Squanch"),
            image = "https://rickandmortyapi.com/api/character/avatar/47.jpeg",
            episodeIds = emptyList(),
            created = Instant.parse("2017-11-04T18:48:46.250Z"),
        ),
    )
}


