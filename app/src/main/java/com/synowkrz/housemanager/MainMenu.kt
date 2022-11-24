package com.synowkrz.housemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.synowkrz.housemanager.compose.CategoryCard
import com.synowkrz.housemanager.ui.theme.HouseManagerTheme

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    HouseManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
                CategoryCard(iconResource = R.drawable.shopping_cart, name = R.string.category_shop)
                CategoryCard(iconResource = R.drawable.shopping_cart, name = R.string.category_task)
                CategoryCard(iconResource = R.drawable.shopping_cart, name = R.string.category_calendar)
            }
        }
    }
}