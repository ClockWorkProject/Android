package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.ui.theme.Purple500
import java.util.*

@Composable
internal fun StatisticScreen() {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.monthly_hours_title),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            MonthPicker()
            Text(
                text = stringResource(id = R.string.monthly_hours_value, 167, 23),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Modified NumberPicker to show months
 */
@Composable
internal fun MonthPicker() {
    val calendar = Calendar.getInstance()
    // State with default value of current month
    var pickerValue by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    // List of all months to set as label in the NumberPicker
    val monthList = arrayListOf(
        "Januar",
        "Februar",
        "März",
        "April",
        "Mai",
        "Juni",
        "Juli",
        "August",
        "September",
        "Oktober",
        "November",
        "Dezember"
    )

    NumberPicker(
        label = { monthList[it] },
        value = pickerValue,
        onValueChange = {
            /* TODO get total hours from server */
            pickerValue = it
        },
        range = 0..11,
        dividersColor = Purple500,
        modifier = Modifier.width(150.dp),
        textStyle = TextStyle(fontSize = 20.sp)
    )
}

@Preview
@Composable
internal fun PreviewStatisticScreen() {
    StatisticScreen()
}