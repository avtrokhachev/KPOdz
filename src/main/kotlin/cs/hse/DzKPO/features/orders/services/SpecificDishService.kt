package cs.hse.DzKPO.features.orders.services

import cs.hse.DzKPO.features.orders.db.entities.SpecificDish
import cs.hse.DzKPO.features.orders.db.repository.SpecificDishRepository
import cs.hse.DzKPO.features.orders.dto.SpecificDishDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SpecificDishService(private val specificDishRepository: SpecificDishRepository) {
    fun retrieveUncookedDishesByOrderId(orderId: Long) = specificDishRepository.findAll()
        .filter { it.orderId == orderId && it.status == SpecificDish.SpecificDishStatus.WAITING }.map { it ->
            SpecificDishDto(it.id!!, it.status.toString(), it.orderId, it.dishId)
        }

    fun retrieveSpecificDishById(id: Long) = specificDishRepository.findById(id).map { it ->
        SpecificDishDto(it.id!!, it.status.toString(), it.orderId, it.dishId)
    }.orElseThrow()

    @Transactional
    fun tryAddSpecificDish(orderId: Long, dishId: Long) =
        specificDishRepository.save(
            SpecificDish(
                status = SpecificDish.SpecificDishStatus.WAITING,
                orderId = orderId,
                dishId = dishId
            )
        ).id

    @Transactional
    fun tryChangeSpecificDishStatus(id: Long, status: SpecificDish.SpecificDishStatus) {
        var specificDish = specificDishRepository.findById(id).orElseThrow()
        specificDish.status = status
        specificDishRepository.save(specificDish)
    }
}