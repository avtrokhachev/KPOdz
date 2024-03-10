package cs.hse.DzKPO.features.orders.dto

data class SpecificDishDto(
    val id: Long,
    var status: String,
    var orderId: Long,
    var dishId: Long
)