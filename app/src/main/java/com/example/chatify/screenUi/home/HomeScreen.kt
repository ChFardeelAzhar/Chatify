package com.example.chatify.screenUi.home

import android.annotation.SuppressLint
import android.telephony.CellInfoCdma

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VerticalDistribute
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatify.Chatify
import com.example.chatify.R
import com.example.chatify.model.ChatMessage
import com.example.chatify.util.ResultState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Chatify(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val chatMessages = viewModel.chatMessages.collectAsState()
    val state = viewModel.chatState.collectAsState()
    val isTyping = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    LaunchedEffect(chatMessages.value.size) {
        if (chatMessages.value.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.value.size - 1)
        }
    }

    LaunchedEffect(state.value) {
        when (state.value) {
            is ResultState.Success -> {

            }

            is ResultState.Failure -> {

            }

            ResultState.Loading -> {
                isTyping.value = true
            }

            ResultState.Idle -> {

            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent() }
    ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        imageVector = Icons.Default.VerticalDistribute,
                        contentDescription = "slide bar trigger button ",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable {
                                // trigger navigation drawer for side ba
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                            .padding(3.dp)
                    )

                    Spacer(Modifier.size(2.dp))
                    Text(
                        text = "Chatify",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }

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
                    state = listState,
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
                },
            )

        }


    }
    }

}

@Composable
fun ReplyBox(onSendClick: (String) -> Unit, modifier: Modifier = Modifier) {
    var message = remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .windowInsetsPadding(WindowInsets.ime),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
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
                        if (!message.isUser) 20.dp else 0.dp,
                ),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {


            if (!message.isUser) {
                Image(
                    painter = painterResource(R.drawable.chatify),
                    contentDescription = "Chatify",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(30.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Fit
                )

            }

            Spacer(modifier = Modifier.size(5.dp))

            val backgroundColor =
                if (message.isUser) MaterialTheme.colorScheme.error.copy(alpha = .3f) else Color.Transparent


            Text(
                text = message.message,
                modifier = Modifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(10.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground
            )

        }


    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxHeight() // Drawer takes full height
            .width(LocalConfiguration.current.screenWidthDp.dp / 2) // ✅ Half of the screen width
            .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f)) // Customize background
            .padding(WindowInsets.statusBars.asPaddingValues()) // Fix status bar issue
    ) {
        Text(
            text = "Past Chats",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.background)

        LazyColumn (
            modifier = Modifier.weight(1f).padding(bottom = 26.dp)
        ){
            items(20){
                Text(
                    text = "Items List for dis..",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp).clickable {

                    }.padding(5.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun PreviewChatify(modifier: Modifier = Modifier) {
    Chatify()
}

