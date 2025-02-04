package com.mamsky.stockalculator.android.screen.draft

data class StockModel(
    val code: String,
    val companyName: String,
    val description: String?,
    val sector: String? = null,
    val subSector: String? = null,
    val eps: String? = null,
    val bookValue: String? = null,
    val currentPrice: String? = null,
    val per: String? = null,
    val pbv: String? = null,
    val roa: String? = null,
    val roe: String? = null,
    val der: String? = null,
)

fun StockModel.perXPbv(): Double? {
    if (this.currentPrice?.toDoubleOrNull() == null) return null
    if (this.eps?.toDoubleOrNull() == null) return null
    if (this.bookValue?.toDoubleOrNull() == null) return null
    val eps = this.eps.toDoubleOrNull() ?: return null
    val bv = this.bookValue.toDoubleOrNull() ?: return null
    return eps * bv
}

data class StockEntity(
    val id: String,
    val code: String,
    val companyName: String,
    val description: String?,
    val sector: String? = null,
    val subSector: String? = null,
    val eps: Double,
    val bookValue: Double,
    val currentPrice: Long?,
    val per: Double? = null,
    val pbv: Double? = null,
    val roa: Double? = null,
    val roe: Double? = null,
    val der: Double? = null,
)
