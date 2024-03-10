package cs.hse.DzKPO.features.orders.controllers

import cs.hse.DzKPO.features.orders.dto.*
import cs.hse.DzKPO.features.orders.services.DishService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dishes")
class DishController(private val dishService: DishService) {
    @GetMapping
    fun getAllDishes(): ResponseEntity<List<DishDto>> {
        return ResponseEntity.ok(dishService.retrieveDishes())
    }

    @GetMapping("/get/{id}")
    fun getDishById(@PathVariable id: Long): ResponseEntity<DishDto> {
        val dish = dishService.retrieveDishById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dish)
    }

    @PostMapping("/add")
    fun postDish(@RequestBody dish: DishDto): ResponseEntity<String> {
        val resp = dishService.tryAddDish(dish) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/delete")
    fun deleteDish(@RequestBody req: DishIdDto): ResponseEntity<String> {
        val resp = dishService.tryDeleteDish(req.id)
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/changeQuantity")
    fun changeQuantity(@RequestBody req: ChangeQuantityDto): ResponseEntity<String> {
        val resp =
            dishService.tryChangeQuantity(req.id, req.quantityDelta) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/changePrice")
    fun changePrice(@RequestBody req: ChangePriceDto): ResponseEntity<String> {
        val resp = dishService.tryChangePrice(req.id, req.price) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(resp.toString())
    }

    @PostMapping("/changeWaitTime")
    fun changeWaitTime(@RequestBody req: ChangeWaitTimeDto): ResponseEntity<String> {
        val resp = dishService.tryChangeWaitTime(req.id, req.waitTime) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(resp.toString())
    }
}