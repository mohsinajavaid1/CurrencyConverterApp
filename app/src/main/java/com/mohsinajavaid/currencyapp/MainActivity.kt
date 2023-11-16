package com.mohsinajavaid.currencyapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohsinajavaid.currencyapp.ui.theme.CurrencyConverterAppTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.*
import androidx.compose.material.TopAppBar


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.mohsinajavaid.currencyapp.ui.theme.Purple700

import com.mohsinajavaid.currencyapp.utils.Constants.Companion.CURRENCY_CODES_LIST
import com.mohsinajavaid.currencyapp.utils.NetworkResult
import com.mohsinajavaid.currencyapp.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private lateinit var viewModel: ViewModel

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterAppTheme {
                // A surface container using the 'background' color from the theme

                viewModel = ViewModelProvider(this).get(ViewModel::class.java)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                    ConverterScreen()
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }


    @Composable
    @ExperimentalMaterialApi
    fun ConverterScreen() {

        val fromCurrencyCode = remember { mutableStateOf("USD") }
        val toCurrencyCode = remember { mutableStateOf("PKR") }
        val amountValue = remember { mutableStateOf("") }

        val convertedAmount = remember { mutableStateOf("") }
        val singleConvertedAmount = remember { mutableStateOf("") }

        val scaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()


        var isFromSelected = true

        BottomSheetScaffold(
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)

                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(CURRENCY_CODES_LIST) { item ->
                            Text(
                                text = "${item.currencyCode}\t ${item.countryName}",
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .clickable {
                                        if (isFromSelected) {
                                            fromCurrencyCode.value = item.currencyCode
                                        } else {
                                            toCurrencyCode.value = item.currencyCode
                                        }
                                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                                    }
                            )

                        }
                    }
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getString(R.string.title),
                            color = Purple700,
                            fontSize = 24.sp,
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 30.sp
                            )
                        )
                    },
                    backgroundColor =  Color.White,
                    elevation = 0.dp
                )
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetElevation = 6.dp,
            sheetBackgroundColor = Color(0xFFF1F1F1),
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize()
            ) {
                Text(text = "From", color = Purple700)
                Spacer(modifier = Modifier.padding(3.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        isFromSelected = true
                        scope.launch { scaffoldState.bottomSheetState.expand() }

                    }
                    .border(1.dp, Color.Gray, RoundedCornerShape(5.dp)),

                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = fromCurrencyCode.value, modifier = Modifier.padding(10.dp))
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text(text = "To", color  = Purple700)
                Spacer(modifier = Modifier.padding(3.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        isFromSelected = false
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }
                    .border(1.dp, Color.Gray, RoundedCornerShape(5.dp)),

                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = toCurrencyCode.value, modifier = Modifier.padding(10.dp))
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Amount", color =    Purple700)
                    Text(text = fromCurrencyCode.value, color = Color.White)
                }

                Spacer(modifier = Modifier.padding(3.dp))
                OutlinedTextField(
                    value = amountValue.value,
                    onValueChange = { amountValue.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("0.00", style = MaterialTheme.typography.body1)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.padding(20.dp))

                Button(
                    onClick = {

                        viewModel.getExchangeRates()


                        viewModel.exchangeRatesResponse.observe(
                            this@MainActivity
                        ) { response ->
                            Log.d("Test", response.data.toString())
                            when (response) {
                                is NetworkResult.Success -> {
                                    response.data?.let {
                                        if (amountValue.value.isEmpty()) {
                                            amountValue.value = "1.00"
                                        }

                                        val toValue =
                                            viewModel.getToValue(toCurrencyCode.value, it.rates)
                                        val amount = amountValue.value.toDouble()

                                        convertedAmount.value =
                                            "${getOutputString(amount * toValue)} ${toCurrencyCode.value}"
                                        singleConvertedAmount.value =
                                            "1 ${fromCurrencyCode.value} = ${getOutputString(toValue)} ${toCurrencyCode.value}"
                                    }
                                }
                                is NetworkResult.Error -> {
                                    Toast.makeText(
                                        this@MainActivity,
                                        response.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                is NetworkResult.Loading -> {
                                    Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("CONVERT", fontSize = 20.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.padding(30.dp))
                Text(
                    text = convertedAmount.value,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    color =    Purple700,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = singleConvertedAmount.value,
                    modifier = Modifier.fillMaxWidth(),
                    color = Purple700,
                    style = TextStyle(textAlign = TextAlign.Center)
                )


            }

        }
    }

    private fun getOutputString(value: Double): String {
        return "%.2f".format(value)
    }

    @Preview(showBackground = true)
    @ExperimentalMaterialApi
    @Composable
    fun DefaultPreview() {
        CurrencyConverterAppTheme {
            ConverterScreen()
        }
    }
}