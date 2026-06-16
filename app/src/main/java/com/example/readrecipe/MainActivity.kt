package com.example.readrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.readrecipe.ui.navigation.NavGraph
import com.example.readrecipe.ui.theme.ReadRecipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReadRecipeTheme {
                NavGraph()
            }
        }
    }
}
