package com.example.modelobase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.example.modelobase.data.LocalOccurrenceRepository
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.ui.components.DemoRibbon
import com.example.modelobase.ui.screens.AgentScreen
import com.example.modelobase.ui.screens.AreaScreen
import com.example.modelobase.ui.screens.DetailsScreen
import com.example.modelobase.ui.screens.HelpScreen
import com.example.modelobase.ui.screens.HomeScreen
import com.example.modelobase.ui.screens.MyReportsScreen
import com.example.modelobase.ui.screens.ReportWizardScreen
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Teal
import com.example.modelobase.ui.theme.VigiaSaudeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { VigiaSaudeTheme { VigiaSaudeApp() } }
    }
}

private data class MainTab(val route: String, val label: String)
private val tabs = listOf(
    MainTab("home", "Início"), MainTab("areas", "Regiões"),
    MainTab("mine", "Registros"), MainTab("help", "Ajuda")
)

@Composable
fun VigiaSaudeApp() {
    val context = LocalContext.current
    val repository = remember { LocalOccurrenceRepository(context.applicationContext) }
    val records = repository.records
    var route by rememberSaveable { mutableStateOf("home") }
    var selectedRecordId by rememberSaveable { mutableStateOf<String?>(null) }
    var draftId by rememberSaveable { mutableStateOf<String?>(null) }
    var initialCategoryCode by rememberSaveable { mutableStateOf<String?>(null) }
    var simulatedOffline by remember { mutableStateOf(repository.simulateOffline) }

    fun startReport(category: ReportCategory? = null, edit: String? = null) {
        initialCategoryCode = category?.code
        draftId = edit
        route = "report"
    }

    BackHandler(enabled = route == "details") { route = "mine" }

    val mainTab = tabs.any { it.route == route }
    Scaffold(
        containerColor = Cream,
        topBar = { DemoRibbon() },
        bottomBar = {
            if (mainTab) NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { index, tab ->
                    val icon = when (index) {
                        0 -> Icons.Outlined.Home
                        1 -> Icons.Outlined.Map
                        2 -> Icons.Outlined.ReceiptLong
                        else -> Icons.Outlined.HelpOutline
                    }
                    NavigationBarItem(
                        selected = route == tab.route,
                        onClick = { route = tab.route },
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(tab.label) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Teal,
                            selectedTextColor = Teal,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (route) {
                "home" -> HomeScreen(
                    onReport = { startReport(category = it) },
                    onMap = { route = "areas" },
                    onAgentDemo = { route = "agent" }
                )
                "areas" -> AreaScreen(records)
                "mine" -> MyReportsScreen(
                    records,
                    onDetail = { selectedRecordId = it; route = "details" },
                    onNew = { startReport() }
                )
                "help" -> HelpScreen(
                    isSimulatingOffline = simulatedOffline,
                    pendingCount = records.count { it.isPending && !it.isDraft },
                    onOfflineToggle = { simulatedOffline = it; repository.simulateOffline = it },
                    onSimulateSync = {
                        if (!simulatedOffline) repository.simulateReceipt(records.filter { it.isPending && !it.isDraft }.map { it.id })
                    },
                    onOpenAgent = { route = "agent" },
                    onReset = { repository.resetDemo(); simulatedOffline = false; route = "home" }
                )
                "report" -> {
                    val draft = records.firstOrNull { it.id == draftId && it.isDraft }
                    androidx.compose.runtime.key(draftId, initialCategoryCode) {
                        ReportWizardScreen(
                            initialCategory = initialCategoryCode?.let { ReportCategory.fromCode(it) },
                            initialDraft = draft,
                            simulatedOffline = simulatedOffline,
                            onStore = { report, simulateNow ->
                                repository.store(report)
                                if (simulateNow) repository.simulateReceipt(listOf(report.id))
                            },
                            onCancel = { route = "mine" },
                            onCompleted = { selectedRecordId = it; route = "details" }
                        )
                    }
                }
                "details" -> DetailsScreen(
                    record = records.firstOrNull { it.id == selectedRecordId },
                    onBack = { route = "mine" },
                    onEditDraft = { startReport(edit = it) },
                    onFeedback = { id, perception -> repository.addPerception(id, perception) }
                )
                "agent" -> AgentScreen(
                    records,
                    onBack = { route = "home" },
                    onAdvance = { id, status, note -> repository.advanceStatus(id, status, note) }
                )
                else -> HomeScreen(onReport = { startReport(it) }, onMap = { route = "areas" }, onAgentDemo = { route = "agent" })
            }
        }
    }
}
