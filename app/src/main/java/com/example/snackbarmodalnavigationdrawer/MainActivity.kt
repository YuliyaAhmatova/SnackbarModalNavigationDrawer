package com.example.snackbarmodalnavigationdrawer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.io.Serializable

@Suppress("OVERRIDE_DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notesList = rememberSaveable {
                mutableListOf(Note("Добро пожаловать!", "Напишите свою первую заметку!"))
            }
            var secondNotesList by rememberSaveable { mutableStateOf<List<Note>?>(null) }

            if (secondNotesList == null) {
                secondNotesList = intent?.getSerializableExtra("secondNotesList") as? List<Note>
                secondNotesList?.let { notes ->
                    notesList.clear()
                    notesList.addAll(notes)
                }
            }
            val notesStateList = remember { mutableStateListOf<Note>() }
            notesStateList.clear()
            notesStateList.addAll(notesList)
            val selectedItem = remember {
                mutableStateOf(notesStateList[0])
            }
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                ModalNavigationDrawer(
                    modifier = Modifier.padding(it),
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.padding(end = 40.dp)
                        ) {
                            notesStateList.forEach { item ->
                                NavigationDrawerItem(
                                    label = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    if (notesList.size > 1) {
                                                        notesList.remove(item)
                                                        notesStateList.clear()
                                                        notesStateList.addAll(notesList)
                                                        selectedItem.value = notesStateList[0]
                                                    } else {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Добавьте хотя бы одну заметку!")
                                                        }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    "Delete",
                                                    tint = Color.DarkGray
                                                )
                                            }
                                            Text(item.title, fontSize = 18.sp)
                                        }
                                    },
                                    selected = selectedItem.value == item,
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        selectedItem.value = item
                                    },
                                )
                            }
                        }
                    },
                    content = {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        },
                                        content = {
                                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                        }
                                    )
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = selectedItem.value.title,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp),
                                        text = selectedItem.value.content,
                                        fontSize = 24.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            FloatingActionButton(
                                modifier = Modifier
                                    .align(alignment = Alignment.BottomEnd)
                                    .padding(20.dp),
                                onClick = {
                                    val intent =
                                        Intent(this@MainActivity, SecondActivity::class.java)
                                    intent.putExtra("notesList", ArrayList(notesList))
                                    startActivity(intent)
                                    finish()
                                    secondNotesList=null
                                },
                                content = { Icon(Icons.Filled.Add, "Добавить") }
                            )
                        }
                    }
                )
            }
        }
    }
}

data class Note(val title: String, val content: String) : Serializable