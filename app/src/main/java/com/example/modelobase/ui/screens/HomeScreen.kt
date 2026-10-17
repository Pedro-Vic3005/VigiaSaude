package com.example.modelobase.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.modelobase.ui.theme.VigiaSaudeTheme
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.ui.components.CategoryTile
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Mint
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Orange
import com.example.modelobase.ui.theme.Teal
import com.example.modelobase.ui.theme.Wine

@Composable
fun HomeScreen(onReport: (ReportCategory?) -> Unit, onMap: () -> Unit, onAgentDemo: () -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 19.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("CUIDADO COMEÇA NO BAIRRO", fontSize = 11.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.ExtraBold, color = Teal)
            Text("Olhe ao redor.\nA gente cuida junto.", style = MaterialTheme.typography.headlineLarge, color = Navy)
            Text("Um registro simples pode ajudar a entender melhor os riscos da nossa cidade.", color = Muted, style = MaterialTheme.typography.bodyMedium)
        }

        Box(
            Modifier.fillMaxWidth().background(Teal, RoundedCornerShape(28.dp))
        ) {
            Canvas(Modifier.matchParentSize()) {
                drawCircle(Color.White.copy(alpha = 0.09f), radius = size.minDimension * .41f, center = Offset(size.width * .92f, size.height * .04f))
                drawCircle(Orange.copy(alpha = .75f), radius = size.minDimension * .13f, center = Offset(size.width * .91f, size.height * .78f))
                drawCircle(Color.White.copy(alpha = .10f), radius = size.minDimension * .19f, center = Offset(size.width * .72f, size.height * .62f))
            }
            Column(Modifier.padding(21.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text("✳", color = Color(0xFFFFCE93), fontSize = 22.sp)
                    Text("VIGIASAÚDE RECIFE", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 12.sp)
                }
                Text("Encontrou algo\nno seu bairro?", color = Color.White, style = MaterialTheme.typography.headlineMedium)
                Text("Mosquitos, ratos, escorpiões e outras situações.", color = Color.White.copy(alpha = .94f), fontSize = 14.sp)
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable(onClick = { onReport(null) }),
                    color = Color.White,
                    shape = RoundedCornerShape(17.dp)
                ) {
                    Row(Modifier.padding(horizontal = 18.dp, vertical = 15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Registrar ocorrência", fontWeight = FontWeight.Bold, color = Teal, fontSize = 16.sp)
                        Icon(Icons.Outlined.ArrowForward, contentDescription = null, tint = Teal)
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Text("O que você encontrou?", color = Navy, style = MaterialTheme.typography.titleLarge)
            Text("Escolha uma categoria para começar", color = Muted, fontSize = 13.sp)
            ReportCategory.entries.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                    pair.forEach { category ->
                        CategoryTile(category, selected = false, onClick = { onReport(category) }, modifier = Modifier.weight(1f))
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        Surface(onClick = onMap, color = Color.White, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(50.dp).background(Mint, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Place, null, tint = Teal)
                }
                Column(Modifier.weight(1f)) {
                    Text("Meu Recife, meu bairro", color = Navy, fontWeight = FontWeight.Bold)
                    Text("Explore uma visão comunitária por região", color = Muted, fontSize = 12.sp)
                }
                Icon(Icons.Outlined.ArrowForward, contentDescription = "Ver regiões", tint = Teal)
            }
        }
        InformationCard("Segurança em primeiro lugar", "Nunca tente pegar, tocar ou se aproximar de um animal perigoso para fotografar.", Icons.Outlined.Security)
        Surface(onClick = onAgentDemo, shape = RoundedCornerShape(19.dp), color = Color(0xFFF0E8EF), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AutoAwesome, null, tint = Wine)
                Column(Modifier.weight(1f)) {
                    Text("Explorar painel de agentes", color = Wine, fontWeight = FontWeight.Bold)
                    Text("Ambiente simulado para a apresentação", color = Muted, fontSize = 12.sp)
                }
                Icon(Icons.Outlined.ArrowForward, null, tint = Wine)
            }
        }
    }
}

@Preview(name = "Início • VigiaSaúde", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenPreview() {
    VigiaSaudeTheme { HomeScreen(onReport = {}, onMap = {}, onAgentDemo = {}) }
}
