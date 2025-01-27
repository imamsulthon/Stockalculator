package com.mamsky.stockalculator.android.screen.draft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.shared.ButtonAndClear
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputFieldDropDown
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.android.shared.rememberCurrencyVisualTransformation
import com.mamsky.stockalculator.utils.asString

@Composable
fun CreateStockScreen(
    navController: NavController,
    viewModel: CreateStockVM = hiltViewModel()
) {
    val fieldModel by viewModel.fieldModel.collectAsState()
    Content(
        navController = navController,
        model = fieldModel,
        event = viewModel::events
    )

}

@Composable
private fun Content(
    navController: NavController,
    model: StockModel,
    event: (CreateStockEvent) -> Unit,
) {

    var showModalSector by remember { mutableStateOf(false) }
    var showModalSubSector by remember { mutableStateOf(false) }
    val perXPbv by remember(model.eps, model.bookValue) {
        mutableFloatStateOf(model.eps * model.bookValue)
    }

    MainContent(title = "Create Stock",
        bottomContent = {
            ButtonAndClear(modifier = Modifier.fillMaxWidth().padding(10.dp),
                title = "Save",
                onClick = {event.invoke(CreateStockEvent.Save)},
                onClickIcon = {event.invoke(CreateStockEvent.Clear)}
            )
        }, onBack = navController::popBackStack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                item {
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Ticker Code",
                        value = model.code.uppercase(),
                        usePrefix = false,
                        onValueChange = {
                            event.invoke(CreateStockEvent.Code(it))
                        },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardType = KeyboardType.Text
                    )
                    VSpacer(5.dp)
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Company Name",
                        usePrefix = false,
                        value = model.companyName,
                        textStyle = MaterialTheme.typography.bodySmall,
                        onValueChange = {
                            event.invoke(CreateStockEvent.CompanyName(it))
                        },
                        keyboardType = KeyboardType.Text
                    )
                    VSpacer(5.dp)
                    InputFieldDropDown(
                        modifier = Modifier.fillMaxWidth(),
                        label =  "Sector",
                        onValueChange = {},
                        usePrefix = false,
                        value = model.sector,
                        textStyle = MaterialTheme.typography.bodySmall,
                        onClickIcon = {
                            showModalSector = true
                        }
                    )
                    VSpacer(5.dp)
                    InputFieldDropDown(
                        modifier = Modifier.fillMaxWidth(),
                        label =  "Sub Sector",
                        onValueChange = {},
                        usePrefix = false,
                        value = model.subSector,
                        textStyle = MaterialTheme.typography.bodySmall,
                        onClickIcon = {
                            showModalSubSector = true
                        }
                    )
                    VSpacer(5.dp)
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Current Price",
                        value = model.currentPrice.asString(),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        onValueChange = {
                            event.invoke(CreateStockEvent.CurrentPrice(it.toFloat()))
                        },
                        keyboardType = KeyboardType.Number
                    )

                    VSpacer(15.dp)
                    Text(text = "Per Share",  style = MaterialTheme.typography.titleSmall)
                    VSpacer(5.dp)
                    Row {
                        InputField(
                            modifier = Modifier.weight(1f),
                            label = "Earning Per Share",
                            value = model.per.asString(),
                            onValueChange = {
                                event.invoke(CreateStockEvent.EPS(it.toFloat()))
                            },
                            usePrefix = false,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            keyboardType = KeyboardType.Decimal,
                            visualTransformation = rememberCurrencyVisualTransformation()
                        )
                        HSpacer(5.dp)
                        InputField(
                            modifier = Modifier.weight(1f),
                            label = "Book Value Per Share",
                            value = model.bookValue.asString(),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            onValueChange = {
                                event.invoke(CreateStockEvent.BookValue(it.toFloat()))
                            },
                            usePrefix = false,
                            keyboardType = KeyboardType.Decimal
                        )
                    }
                }

                item {
                    VSpacer(15.dp)
                    Text(text = "Valuation", style = MaterialTheme.typography.titleSmall)
                    VSpacer(5.dp)
                    Row {
                        InputField4(
                            modifier = Modifier.weight(1f),
                            label = "PER",
                            value = model.per.asString(),
                            supportingText = {
                                Text(text = "Price to Earning Ratio")
                            },
                            onValueChange = {},
                            enabled = false,
                            keyboardType = KeyboardType.Text
                        )
                        HSpacer(5.dp)
                        InputField4(
                            modifier = Modifier.weight(1f),
                            label = "PBV",
                            value = model.pbv.asString(),
                            enabled = false,
                            supportingText = {
                                Text(text = "Price to Book Value")
                            },
                            onValueChange = {},
                            keyboardType = KeyboardType.Text
                        )
                    }
                    VSpacer(5.dp)
                    Row {
                        InputField4(
                            modifier = Modifier.weight(1f),
                            label = "PER x PBV",
                            value = perXPbv.asString(),
                            onValueChange = {},
                            enabled = false,
                            keyboardType = KeyboardType.Text
                        )
                        HSpacer(5.dp)
                        InputField4(
                            modifier = Modifier.weight(1f),
                            label = "Return on Equity",
                            value = "",
                            onValueChange = {},
                            enabled = false,
                            keyboardType = KeyboardType.Text
                        )
                    }

                    VSpacer(5.dp)
                }

                item {

                }
            }
        }
    }

    SectorModalAll(showModalSector, model.sector, onDismiss = { showModalSector = false }) { key, label ->
        event.invoke(CreateStockEvent.Sector(key))
    }

    SectorModalSub(showModalSubSector,
        parentKey = model.sector, selectedId = model.subSector,
        onDismiss = { showModalSubSector = false }
    ) { key, label ->
        event.invoke(CreateStockEvent.SubSector(key))
    }

}

@Composable
fun InputField4(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    usePrefix: Boolean = false,
    prefixLabel: String? = null,
    enabled: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = textStyle,
        label = {
            Text(text = label, fontSize = 12.sp)
        },
        placeholder = placeholder,
        prefix = {
            if (usePrefix) Text(text = prefixLabel.toString(), style = MaterialTheme.typography.labelSmall)
        },
        enabled = enabled,
        supportingText = supportingText,
        value = value, onValueChange = onValueChange::invoke,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
    )
}

@Composable
@Preview
private fun Preview() {
    Content(
        rememberNavController(),
        event = {},
        model = StockModel("abcd", "PT Bukit Asam", "Energi dan Gas",
            "Gas", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
    )
}