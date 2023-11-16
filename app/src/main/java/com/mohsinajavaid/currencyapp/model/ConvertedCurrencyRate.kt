package com.mohsinajavaid.currencyapp.model

class ConvertedCurrencyRate(
    val rates: Map<String,String>,
    val base: String,
    val from: String,
    val to: String,
    val givenAmount: String,
    val convertedAmount: String,

) {


}