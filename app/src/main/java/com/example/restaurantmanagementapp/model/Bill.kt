package com.example.restaurantmanagementapp.model
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.OffsetDateTime

@Serializable
class Bill{
    var bill_id: Long? = null
    var user_id: Long? = null
    var table_id: Long? = null
    var total_amount: Double? = null
    var discount_amount: Double? = null
    var final_amount: Double? = null
    var payment_method: String? = null
    var payment_status: Boolean? = null
    var notes: String? = null

    constructor()

    constructor(
        bill_id: Long?,
        user_id: Long?,
        table_id: Long?,
        total_amount: Double?,
        discount_amount: Double?,
        final_amount: Double?,
        payment_method: String?,
        payment_status: Boolean?,
        notes: String?,

    ) {
        this.bill_id = bill_id
        this.user_id = user_id
        this.table_id = table_id
        this.total_amount = total_amount
        this.discount_amount = discount_amount
        this.final_amount = final_amount
        this.payment_method = payment_method
        this.payment_status = payment_status
        this.notes = notes

    }

    override fun toString(): String {
        return "Bill(bill_id=$bill_id, user_id=$user_id, total_amount=$total_amount, discount_amount=$discount_amount, final_amount=$final_amount, payment_method=$payment_method, payment_status=$payment_status, notes=$notes)"
    }
}