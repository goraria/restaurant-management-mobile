package com.example.restaurantmanagementapp.model
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.OffsetDateTime

@Serializable
data class Bill(
    val bill_id: Long,
    val user_id: Long,
    @Contextual val total_amount: BigDecimal,
    @Contextual val discount_amount: BigDecimal,
    @Contextual val final_amount: BigDecimal,
    val payment_method: String,
    val payment_status: String,
    val notes: String? = null,
    @Contextual val created_at: OffsetDateTime,
    @Contextual val updated_at: OffsetDateTime,
    val table_id: Int,
)
