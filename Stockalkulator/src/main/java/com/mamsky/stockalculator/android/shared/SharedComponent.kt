package com.mamsky.stockalculator.android.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(
    title: String = "",
    onBackPress: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = { BackIcon { onBackPress?.invoke() } },
        title = { Text(text = title) },
    )
}

@Composable
fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = { onClick.invoke()} ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ic_settings")
    }
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    usePrefix: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = textStyle,
        label = {
            Text(text = label)
        },
        prefix = {
            if (usePrefix) Text(text = "Rp ", style = MaterialTheme.typography.labelSmall)
        },
        value = value, onValueChange = onValueChange::invoke,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
    )
}

@Composable
fun RowScope.Container(
    alignment: Alignment.Horizontal = Alignment.Start,
    weight: Float = 1f,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .heightIn(min = 50.dp, max = 120.dp)
            .weight(weight)
            .padding(vertical = 5.dp),
        horizontalAlignment = alignment,
    ) {
        content()
    }
}

@Composable
fun HSpacer(height: Dp = 20.dp) {
    Spacer(modifier = Modifier.width(height))
}

@Composable
fun VSpacer(height: Dp = 20.dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun IconText(
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: ImageVector = Icons.Default.Settings,
    label: String,
    style: TextStyle = MaterialTheme.typography.labelSmall
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = "icons_label")
        Text(text = label, style = style)
    }
}

@Composable
fun ButtonIcon(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    OutlinedIconButton(
        modifier = Modifier.size(24.dp),
        onClick = { onClick.invoke() }) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = icon, contentDescription = "outlined_button")
    }
}