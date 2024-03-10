package cs.hse.DzKPO.features.orders.services

import cs.hse.DzKPO.features.orders.db.entities.Order
import cs.hse.DzKPO.features.orders.db.entities.SpecificDish
import cs.hse.DzKPO.features.orders.db.repository.DishRepository
import cs.hse.DzKPO.features.orders.db.repository.OrderRepository
import cs.hse.DzKPO.features.orders.db.repository.SpecificDishRepository
import cs.hse.DzKPO.features.orders.dto.OrderDto
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.concurrent.thread
import kotlin.jvm.optionals.getOrNull


@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val dishRepository: DishRepository,
) {
    @Autowired
    val specificDishService: SpecificDishService? = null

    @Autowired
    val dishService: DishService? = null

    fun retrieveOrders() = orderRepository.findAll().map { it ->
        OrderDto(it.status.toString(), it.price)
    }.toList()

    fun retrieveOrderById(id: Long) = orderRepository.findById(id).map { it ->
        OrderDto(it.status.toString(), it.price)
    }.orElseThrow()

    fun getTotalMoney(): Long {
        val consumptions =
            orderRepository.findAll().filter { it.status == Order.OrderStatus.PAID }.map { it -> it.price }
        var sum: Long = 0
        for (consumption in consumptions) {
            sum += consumption
        }
        return sum
    }

    @Transactional
    fun tryAddOrder() =
        orderRepository.save(Order(status = Order.OrderStatus.CREATED, price = 0)).id

    fun markedOrderStartCooking(id: Long) {
        var order = orderRepository.findById(id).orElseThrow()
        if (order.status != Order.OrderStatus.CREATED) {
            throw Exception("Incorrect order status")
        }
        order.status = Order.OrderStatus.COOKING
        orderRepository.save(order)
    }

    @Transactional
    fun markedOrderDone(id: Long) {
        var order = orderRepository.findById(id).orElseThrow()
        if (order.status != Order.OrderStatus.COOKING) {
            throw Exception("Incorrect order status")
        }
        order.status = Order.OrderStatus.PAYMENT_REQUIRED
        orderRepository.save(order)
    }

    @Transactional
    fun cookSpecificDishInOrder(specificDish: SpecificDish): Long {
        var price: Long = 0
        specificDishService?.tryChangeSpecificDishStatus(specificDish.id!!, SpecificDish.SpecificDishStatus.COOKING)
        val dish = dishRepository.findById(specificDish.dishId).getOrNull()
        var waitTime: Long = dish?.waitTime ?: 0
        var status = SpecificDish.SpecificDishStatus.DONE
        if (dish != null) {
            if (dish.count == 0L) {
                waitTime = 0
                status = SpecificDish.SpecificDishStatus.CANCELED
            } else {
                dishService?.tryChangeQuantity(specificDish.dishId, -1)
                price = dish.cost
            }
        }
        Thread.sleep(waitTime * 1000)
        specificDishService?.tryChangeSpecificDishStatus(specificDish.id!!, status)
        return price
    }

    @Transactional
    fun tryCookOrder(id: Long) {
        var order = orderRepository.findById(id).orElseThrow()
        markedOrderStartCooking(id)
        val dishesToCook = specificDishService?.retrieveUncookedDishesByOrderId(id)
        var price: Long = 0
        thread {
            if (dishesToCook != null) {
                for (specificDish in dishesToCook) {
                    price += cookSpecificDishInOrder(
                        SpecificDish(
                            id = specificDish.id,
                            status = SpecificDish.SpecificDishStatus.valueOf(specificDish.status),
                            orderId = specificDish.orderId,
                            dishId = specificDish.dishId
                        )
                    )
                }
            }
            markedOrderDone(id)
            tryChangeOrderPrice(id, price)
        }
    }

    @Transactional
    fun tryCancelOrder(id: Long): Long? {
        var order = orderRepository.findById(id).orElseThrow()
        if (order.status != Order.OrderStatus.CREATED) {
            throw Exception("Order is in progress or required payment, thus can't be canceled.")
        }
        order.status = Order.OrderStatus.CANCELED
        orderRepository.save(order)
        return order.id
    }

    @Transactional
    fun tryPayOrder(id: Long): Long? {
        var order = orderRepository.findById(id).orElseThrow()
        if (order.status != Order.OrderStatus.PAYMENT_REQUIRED) {
            throw Exception("Order is in progress or required payment, thus can't be canceled.")
        }
        order.status = Order.OrderStatus.PAID
        orderRepository.save(order)
        return order.id
    }

    @Transactional
    fun tryChangeOrderPrice(id: Long, priceDelta: Long): Long? {
        var order = orderRepository.findById(id).orElseThrow()
        order.price += priceDelta
        orderRepository.save(order)
        return order.id
    }
}