package com.example.chatify.screenUi.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatify.R
import com.example.chatify.model.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Chatify(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val chatMessages = viewModel.chatMessages.collectAsState()

    Scaffold {

        // if Messages List is empty then we will show this

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            // Header

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "Chatify",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Content of Chatify

            if (chatMessages.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.SmartToy,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(Modifier.size(5.dp))
                    Text(
                        text = "What can I help with?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(chatMessages.value) { message ->
                        SingleChatMessage(message)
                    }

                }

            }



            ReplyBox(
                onSendClick = { msg ->
                    viewModel.sendMessage(msg)
                }
            )

        }


    }

}

@Composable
fun ReplyBox(onSendClick: (String) -> Unit) {

    var message = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message.value,
            onValueChange = {
                message.value = it
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                disabledBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            ),
            placeholder = {
                Text(text = "Message Chatify")
            },
            shape = RoundedCornerShape(50.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.size(7.dp))

        Image(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            modifier = Modifier
                .padding(3.dp)
                .clickable {
                    if (message.value.isNotEmpty()) {
                        onSendClick(message.value)
                        message.value = ""
                    }
                },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )

    }

}

@Composable
fun SingleChatMessage(message: ChatMessage) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start =
                    if (message.isUser) 40.dp else 0.dp,
                    end =
                    if (!message.isUser) 40.dp else 0.dp,
                ),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {


            if (!message.isUser) {
                Image(
                    painter = painterResource(R.drawable.chatify),
                    contentDescription = "Chatify",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Fit
                )

            }

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = message.message,
                modifier = Modifier
                    .background(
                        if (message.isUser) MaterialTheme.colorScheme.error.copy(alpha = .3f) else MaterialTheme.colorScheme.primary.copy(
                            alpha = .3f
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

        }


    }
}