package cs.hse.DzKPO.features.users.services

import cs.hse.DzKPO.features.users.db.entities.User
import cs.hse.DzKPO.features.users.db.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun createUser(user: User): User {
        if (userRepository.existsByLogin(user.login)) {
            return userRepository.findByLogin(user.login)!!
        } else {
            return userRepository.save(user)
        }
    }
}