package com.iorihinata.boom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() { override fun onCreate(state: Bundle?) { super.onCreate(state); setContent { BoomApp() } } }

@Composable fun BoomApp() {
 var running by remember { mutableStateOf(false) }
 var debug by remember { mutableStateOf(true) }
 MaterialTheme { Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("BOOM — Structural Physics Simulator", style=MaterialTheme.typography.headlineMedium)
  Text("Android runtime shell. Structural simulation stays data-driven and backend-independent.")
  Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
   Button(onClick={ running=!running }) { Text(if (running) "STOP" else "START") }
   Button(onClick={ debug=!debug }) { Text(if (debug) "DEBUG ON" else "DEBUG OFF") }
  }
  Text("Simulation: ${if (running) "running" else "stopped"}")
  Text("Debug: ${if (debug) "enabled" else "disabled"}")
  Text("No building HP • SI units • structural joints • runtime failure model")
 }
}
}
