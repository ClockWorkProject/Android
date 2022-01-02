package de.lucas.clockwork_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.InfoCategory

@Composable
internal fun InfoScreen(
    categories: List<InfoCategory>,
    onClickCategory: (InfoCategory) -> Unit
) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
        ) {
            CategoryList(categories, onClickCategory)
        }
    }
}

@Composable
private fun CategoryList(categories: List<InfoCategory>, onClickCategory: (InfoCategory) -> Unit) {
    LazyColumn {
        items(categories) { category ->
            CategoryItem(category) {
                onClickCategory(category)
            }
        }
    }
}

@Composable
private fun CategoryItem(category: InfoCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val title = stringResource(id = category.titleRes)
        Text(
            text = title,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            style = TextStyle(fontSize = 18.sp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right_dark),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        )
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
        thickness = 1.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    InfoScreen(
        categories = listOf(
            InfoCategory("imprint", R.string.imprint),
            InfoCategory("rightsAndLicenses", R.string.rights_and_licenses),
            InfoCategory("dataProtection", R.string.data_protection),
            InfoCategory("version", R.string.version_number)
        ),
        onClickCategory = { }
    )
}