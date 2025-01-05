package com.example.snackbarmodalnavigationdrawer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val secondNotesList = rememberSaveable {
                mutableListOf<Note>()
            }
            val mainNotesList = intent.getSerializableExtra("notesList") as List<Note>
            mainNotesList?.let {
                secondNotesList.addAll(it)
            }
            val input = rememberSaveable { mutableStateOf("") }
            val inputTwo = rememberSaveable { mutableStateOf("") }
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(40.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Напишите что-нибудь:",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = input.value,
                            onValueChange = { input.value = it },
                            textStyle = TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black
                            ),
                            placeholder = {
                                Text(
                                    text = "Заголовок",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 22.sp
                                )
                            }
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = inputTwo.value,
                            onValueChange = { inputTwo.value = it },
                            textStyle = TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black
                            ),
                            placeholder = {
                                Text(
                                    text = "Основное содержимое",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 22.sp
                                )
                            }
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp), // добавим немного вертикального отступа
                            contentAlignment = Alignment.Center // Центрируем кнопку по горизонтали
                        ) {
                            Button(
                                onClick = {
                                    if (input.value.isBlank() || inputTwo.value.isBlank()) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Введите текст!")
                                        }
                                    } else {
                                        val newNote = Note(input.value, inputTwo.value)
                                        secondNotesList.add(newNote)
                                        val intent =
                                            Intent(this@SecondActivity, MainActivity::class.java)
                                        intent.putExtra(
                                            "secondNotesList",
                                            ArrayList(secondNotesList)
                                        )
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            ) {
                                Text("Сохранить", textAlign = TextAlign.Center, fontSize = 26.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}