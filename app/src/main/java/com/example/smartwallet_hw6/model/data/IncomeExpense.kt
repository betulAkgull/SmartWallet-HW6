package com.example.smartwallet_hw6.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeExpense(
    val documentId: String? = null,
    val title: String? = null,
    val cost: Double? = null,
    val date: String? = null,
    val type: Boolean? = false
): Parcelable
