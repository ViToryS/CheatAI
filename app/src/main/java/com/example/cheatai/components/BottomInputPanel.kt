package com.example.cheatai.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cheatai.R
import com.example.cheatai.data.Book
import com.example.cheatai.data.StaticBooksRepository
import com.example.cheatai.ui.theme.Blue
import com.example.cheatai.ui.theme.Gradients
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkGray
import com.example.cheatai.ui.theme.TintGray
import com.example.cheatai.ui.theme.White
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomInputPanel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp, top = 90.dp, start = 35.dp, end = 35.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Название книги", color = TintGray,
                    style = CheatAITypography.labelMedium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_book),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(30.dp)

        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        "Описание",
                        color = TintGray,
                        style = CheatAITypography.labelMedium
                    )
                }
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_description),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(30.dp),
            textStyle = CheatAITypography.labelMedium.copy(
                color = Color.Black,
                lineHeight = 24.sp
            )
        )
    }
}