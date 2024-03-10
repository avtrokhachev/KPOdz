package cs.hse.DzKPO.features.orders.controllers

import cs.hse.DzKPO.features.orders.dto.*
import cs.hse.DzKPO.features.orders.services.OrderService
import cs.hse.DzKPO.features.orders.services.SpecificDishService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(val orderService: OrderService, val specificDishService: SpecificDishService) {
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<OrderDto>> {
        return ResponseEntity.ok(orderService.retrieveOrders())
    }

    @GetMapping("/get/{id}")
    fun getOrderById(@PathVariable("id") id: Long): ResponseEntity<OrderDto> {
        val order = orderService.retrieveOrderById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(order)
    }

    @GetMapping("/getStatus/{id}")
    fun getOrderStatusById(@PathVariable("id") id: Long): ResponseEntity<String> {
        val status = orderService.retrieveOrderById(id).status
        return ResponseEntity.ok(status)
    }

    @PostMapping("/create")
    fun createOrder(): ResponseEntity<String> {
        val resp = orderService.tryAddOrder()
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/addDish")
    fun addDish(@RequestBody addDishRequest: AddDishToOrderDto): ResponseEntity<String> {
        val resp = specificDishService.tryAddSpecificDish(addDishRequest.orderId, addDishRequest.dishId)
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/startCooking")
    fun startCookingOrder(@RequestBody orderIdDto: OrderIdDto): ResponseEntity<String> {
        val resp = orderService.tryCookOrder(orderIdDto.id)
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/cancel")
    fun cancelOrder(@RequestBody orderIdDto: OrderIdDto): ResponseEntity<String> {
        val resp = orderService.tryCancelOrder(orderIdDto.id)
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/pay")
    fun payOrder(@RequestBody orderIdDto: OrderIdDto): ResponseEntity<String> {
        val resp = orderService.tryPayOrder(orderIdDto.id)
        return ResponseEntity.ok(resp.toString())
    }

    @GetMapping("/getTotalMoney")
    fun getTotalMoney(): ResponseEntity<String> {
        val resp = orderService.getTotalMoney()
        return ResponseEntity.ok(resp.toString())
    }
}