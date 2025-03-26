package com.example.chatify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.chatify.screenUi.Chatify
import com.example.chatify.screenUi.HomeScreenViewModel
import com.example.chatify.ui.theme.ChatifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val viewModel = ViewModelProvider(this)[HomeScreenViewModel::class.java]

        setContent {
            ChatifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Chatify(modifier = Modifier.padding(innerPadding),
//                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

