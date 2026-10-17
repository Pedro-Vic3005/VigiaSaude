package com.example.modelobase.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.OccurrenceStatus
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.ui.theme.Border
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Ink
import com.example.modelobase.ui.theme.Mint
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Orange
import com.example.modelobase.ui.theme.Teal
import com.example.modelobase.ui.theme.Warning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PrimaryAction(title: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = Color.White),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) { Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
}

@Composable
fun OutlineAction(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick, modifier = modifier.fillMaxWidth().height(52.dp),
        border = BorderStroke(1.dp, Border), shape = RoundedCornerShape(17.dp)
    ) { Text(title, color = Navy, fontWeight = FontWeight.SemiBold) }
}

@Composable
fun PageTitle(eyebrow: String, headline: String, helper: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(eyebrow.uppercase(), style = MaterialTheme.typography.labelLarge, fontSize = 12.sp, letterSpacing = 1.4.sp, color = Teal)
        Text(headline, style = MaterialTheme.typography.headlineMedium, color = Navy)
        if (helper != null) Text(helper, style = MaterialTheme.typography.bodyMedium, color = Muted)
    }
}

@Composable
fun InformationCard(title: String, content: String, icon: ImageVector = Icons.Outlined.Info) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Mint, RoundedCornerShape(19.dp)).padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Teal, modifier = Modifier.size(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Navy)
            Text(content, style = MaterialTheme.typography.bodyMedium, color = Ink)
        }
    }
}

@Composable
fun DemoRibbon() {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFFFF3DD)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Outlined.Info, null, tint = Warning, modifier = Modifier.size(17.dp))
        Text("MVP de demonstração • dados locais e fictícios • sem envio real", fontSize = 11.sp, color = Color(0xFF6C4706), fontWeight = FontWeight.SemiBold, lineHeight = 15.sp)
    }
}

@Composable
fun CategoryTile(category: ReportCategory, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (selected) Mint else Color.White,
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) Teal else Border),
        shadowElevation = if (selected) 2.dp else 0.dp,
        modifier = modifier.clickable(role = Role.Button, onClickLabel = "Escolher ${category.label}", onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier.size(56.dp).background(if (selected) Color(0xFFDAEDE8) else Cream, RoundedCornerShape(17.dp)),
                contentAlignment = Alignment.Center
            ) { Text(category.pictogram, fontSize = 30.sp) }
            Text(category.label, style = MaterialTheme.typography.titleMedium, color = Navy, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(category.example, style = MaterialTheme.typography.bodyMedium, color = Muted, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (selected) Text("Selecionado ✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Teal)
        }
    }
}

@Composable
fun StatusPill(occurrence: Occurrence) {
    val label = when {
        occurrence.isDraft -> "Rascunho local"
        occurrence.isPending -> "Pendente no aparelho"
        else -> occurrence.status.visible
    }
    val bg = when {
        occurrence.isPending || occurrence.isDraft -> Color(0xFFFFF3DF)
        occurrence.status == OccurrenceStatus.FINISHED -> Color(0xFFE2F3E9)
        occurrence.status == OccurrenceStatus.DISCARDED -> Color(0xFFF8E8ED)
        else -> Mint
    }
    val fg = when {
        occurrence.isPending || occurrence.isDraft -> Warning
        occurrence.status == OccurrenceStatus.FINISHED -> Color(0xFF146241)
        occurrence.status == OccurrenceStatus.DISCARDED -> Color(0xFF952944)
        else -> Teal
    }
    Surface(color = bg, shape = RoundedCornerShape(40.dp)) {
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = fg)
    }
}

fun dateLabel(time: Long): String = SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale("pt", "BR")).format(Date(time))

@Composable
fun OccurrenceCard(occurrence: Occurrence, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(21.dp), color = Color.White,
        border = BorderStroke(1.dp, Border),
        modifier = modifier.fillMaxWidth().clickable(role = Role.Button, onClickLabel = "Ver detalhes do registro", onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(48.dp).background(Cream, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Text(occurrence.category.pictogram, fontSize = 26.sp) }
                Column(Modifier.weight(1f)) {
                    Text(occurrence.category.label, style = MaterialTheme.typography.titleMedium, color = Navy)
                    Text(occurrence.neighborhood, style = MaterialTheme.typography.bodyMedium, color = Muted)
                }
            }
            Text(occurrence.description, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium, color = Ink)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                StatusPill(occurrence)
                Text(if (occurrence.isExample) "Exemplo fictício" else dateLabel(occurrence.createdMillis), fontSize = 11.sp, color = Muted)
            }
        }
    }
}

@Composable
fun EmptyState(title: String, description: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 34.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Icon(Icons.Outlined.Campaign, contentDescription = null, tint = Teal, modifier = Modifier.size(40.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, color = Navy)
        Text(description, style = MaterialTheme.typography.bodyMedium, color = Muted)
    }
}
