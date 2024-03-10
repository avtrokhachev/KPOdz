package cs.hse.DzKPO.features.orders.services

import cs.hse.DzKPO.features.orders.db.entities.Dish
import cs.hse.DzKPO.features.orders.dto.DishDto
import cs.hse.DzKPO.features.orders.db.repository.DishRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class DishService(private val dishRepository: DishRepository) {

    fun retrieveDishes() = dishRepository.findAll().map { it ->
        DishDto(it.name, it.count, it.cost, it.waitTime)
    }.toList()

    fun retrieveDishById(id: Long) = dishRepository.findById(id).map { it ->
        DishDto(it.name, it.count, it.cost, it.waitTime)
    }.orElseThrow()

    @Transactional
    fun tryAddDish(dishDto: DishDto) =
        dishRepository.save(
            Dish(
                name = dishDto.name,
                count = dishDto.count,
                cost = dishDto.cost,
                waitTime = dishDto.waitTime
            )
        ).id

    @Transactional
    fun tryDeleteDish(id: Long): Long {
        dishRepository.findById(id).orElseThrow()
        dishRepository.deleteById(id)
        return id
    }

    @Transactional
    fun tryChangeQuantity(id: Long, quantityDelta: Long): Long? {
        val dish = dishRepository.findById(id).orElseThrow()
        dish.count = dish.count + quantityDelta
        dish.count = max(dish.count, 0)
        return dishRepository.save(dish).id
    }

    @Transactional
    fun tryChangePrice(id: Long, price: Long): Long? {
        val dish = dishRepository.findById(id).orElseThrow()
        dish.cost = price
        return dishRepository.save(dish).id
    }

    @Transactional
    fun tryChangeWaitTime(id: Long, waitTime: Long): Long? {
        val dish = dishRepository.findById(id).orElseThrow()
        dish.waitTime = max(waitTime, 0)
        return dishRepository.save(dish).id
    }
}