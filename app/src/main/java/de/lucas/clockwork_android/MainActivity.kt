package de.lucas.clockwork_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import de.lucas.clockwork_android.ui.Root
import de.lucas.clockwork_android.ui.theme.ClockWork_AndroidTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockWork_AndroidTheme(darkTheme = false) {
                val systemUiController = rememberSystemUiController()
                val primary = MaterialTheme.colors.primary
                SideEffect {
                    // set the color of the statusBar of the app
                    systemUiController.setStatusBarColor(primary)
                }
                Root()
            }
        }
    }
}