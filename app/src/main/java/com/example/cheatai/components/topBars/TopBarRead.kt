package com.example.cheatai.components.topBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.R
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun TopBarRead(
    navController: NavController?,
    bookId: String,
    mapSelectionMode: Boolean,
    defSelectionMode: Boolean,
    onToggleMapSelection: () -> Unit,
    onToggleDefSelection: () -> Unit,
) {
    val buttonsBlockWidth = LocalConfiguration.current.screenWidthDp.dp  * 0.4f
    val dimensions = LocalAppDimensions.current
    Box(
    modifier = Modifier
    .fillMaxWidth()
    .height(dimensions.topBarHeight + dimensions.topButtonSize / 2)

    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.topBarHeight)
                .clip(RoundedCornerShape(
                    bottomStart = dimensions.cornerMedium,
                    bottomEnd = dimensions.cornerMedium
                ))
                .background(Gradients.bottomUpPanelGradient)
        ) {
            IconButton(
                onClick = { navController?.popBackStack() },
                modifier = Modifier
                    .size(dimensions.iconButtonSize)
                    .padding(start = dimensions.paddingSmall)
                    .align(Alignment.BottomStart)
                    .padding(bottom = dimensions.topBarBottomPadding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Назад",
                    tint = White,
                    modifier = Modifier.size(dimensions.iconSize)
                )
            }
        }


        Box(
            modifier = Modifier.width(buttonsBlockWidth)
                .align(Alignment.BottomEnd)
                .padding( horizontal = dimensions.paddingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,

                ) {
                Button(
                    onClick = { onToggleDefSelection() },
                    modifier = Modifier
                        .size(dimensions.topButtonSize)
                        .background(
                            brush = Gradients.circleButtonGradient,
                            shape = CircleShape
                        ),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (defSelectionMode) R.drawable.ic_check else R.drawable.ic_search),
                        tint = Color.Black,
                        contentDescription = stringResource(id = R.string.def_search),
                        modifier = Modifier.size(dimensions.topButtonSize * 0.55f)
                    )
                }
                Spacer(modifier = Modifier.width(dimensions.paddingLarge))
                Button(
                    onClick = {
                        onToggleMapSelection()
                    },
                    modifier = Modifier
                        .size(dimensions.topButtonSize)
                        .background(
                            brush = Gradients.circleButtonGradient,
                            shape = CircleShape
                        ),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (mapSelectionMode) R.drawable.ic_check else R.drawable.ic_map),
                        tint = Color.Black,
                        contentDescription = stringResource(id = R.string.map_search),
                        modifier = Modifier.size(dimensions.topButtonSize * 0.55f)
                    )}

            }}
    }
}
