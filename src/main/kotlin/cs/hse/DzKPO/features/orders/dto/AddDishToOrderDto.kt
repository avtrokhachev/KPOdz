package cs.hse.DzKPO.features.orders.dto

data class AddDishToOrderDto(
    var orderId: Long,
    var dishId: Long
)