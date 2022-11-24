package com.synowkrz.housemanager.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.ui.theme.HouseManagerTheme

@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun CategoryName(@StringRes name: Int) {
    Text(text = stringResource(id = name), style = MaterialTheme.typography.headlineLarge)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    iconResource: Int,
    @StringRes name: Int,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick, modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = iconResource), modifier = Modifier
                    .size(120.dp)
                    .padding(top = 16.dp), contentDescription = null
            )
            VerticalSpacer(height = 8.dp)
            CategoryName(name = name)
            VerticalSpacer(height = 8.dp)
        }
    }
}

@Preview
@Composable
fun CategoryNamePreview() {
    HouseManagerTheme {
        Column {
            CategoryName(name = R.string.category_shop)
            VerticalSpacer(height = 16.dp)
            CategoryName(name = R.string.category_task)
            VerticalSpacer(height = 16.dp)
            CategoryName(name = R.string.category_calendar)
        }
    }
}

@Preview
@Composable
fun CategoryCardPreview() {
    HouseManagerTheme {
        CategoryCard(iconResource = R.drawable.shopping_cart, name = R.string.category_shop)
    }
}

