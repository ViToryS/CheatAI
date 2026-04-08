package com.example.cheatai



import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import com.example.cheatai.navigation.AppNavigation
import com.example.cheatai.ui.theme.CheatAITheme
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.provideAppDimensions
import com.example.cheatai.ui.theme.rememberWindowSizeClass

class MainActivity :  FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheatAITheme {
                val sizeClass = rememberWindowSizeClass()
                val dimensions = provideAppDimensions(sizeClass)
                CompositionLocalProvider(
                    LocalAppDimensions provides dimensions
                ) {
                    AppNavigation()
                }
            }
        }
    }
}