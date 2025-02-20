package com.example.todo

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TodoPage(viewModel: TodoViewModel) {


//    val todoList by viewModel.todoList.observeAsState()
    val todoList by viewModel.todoList.observeAsState()
    var inputText by remember {
        mutableStateOf("")
    }

   var context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text(text = "Add todo") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .width(100.dp)
                ) {
                    TextButton(
                        onClick = {
                            if(inputText.trim().isNotEmpty()) {
                                viewModel.addTodo(inputText.trim())
                                Toasty.success(context, "Added Todo Successfully", Toasty.LENGTH_SHORT).show()
                                inputText = ""
                            }else{
                                Toasty.error(context, "Field can not be empty", Toasty.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color(0xFF7DDC81))
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "ADD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                        )
                    }
                }

            }

            todoList?.let { list ->
                if (list.isNotEmpty()) {
                    LazyColumn(content = {
                        itemsIndexed(list) { index: Int, item: Todo ->
                            TodoItem(item = item, onDelete= {
                                viewModel.deleteTodo(item.id)
                            }, context = context)

                        }
                    })
                }else{
                    EmptyTodo()
                }
            }
        }
    }
}

@Composable
fun TodoItem(item: Todo, onDelete: () -> Unit, context : Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SimpleDateFormat(
                    "HH:mm:aa, dd/MM",
                    Locale.ENGLISH
                ).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.LightGray

            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.White,
            )
        }

        IconButton(
            onClick = {
                onDelete()
                Toasty.success(context, "Deleted Successfully", Toasty.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}


@Composable
fun EmptyTodo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.empty),
            contentDescription = "Empty Todo",
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
        )
        Text(
            text = "No todo found!",
            fontSize = 24.sp,
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TodoPagePreview() {
    TodoPage(viewModel = TodoViewModel())
}