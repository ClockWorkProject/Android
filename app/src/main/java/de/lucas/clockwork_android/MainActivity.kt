package de.lucas.clockwork_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.lucas.clockwork_android.ui.Root
import de.lucas.clockwork_android.ui.RootViewModel
import de.lucas.clockwork_android.ui.theme.ClockWork_AndroidTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockWork_AndroidTheme(darkTheme = false) {
                val systemUiController = rememberSystemUiController()
                val primary = MaterialTheme.colors.primary
                SideEffect {
                    systemUiController.setStatusBarColor(primary)
                }
                Root(rootViewModel = RootViewModel())
            }
        }
    }
}