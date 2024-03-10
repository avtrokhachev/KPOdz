package cs.hse.DzKPO.security

import cs.hse.DzKPO.features.users.db.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails {
        return userRepository.findByLogin(login) ?: throw Exception("User not found")
    }
}