package cs.hse.DzKPO.features.users.services

import cs.hse.DzKPO.features.users.db.entities.Role
import cs.hse.DzKPO.features.users.db.entities.User
import cs.hse.DzKPO.features.users.db.repository.RoleRepository
import cs.hse.DzKPO.features.users.db.repository.UserRepository
import cs.hse.DzKPO.features.users.dto.SignInDto
import cs.hse.DzKPO.features.users.dto.SignUpDto
import cs.hse.DzKPO.security.TokenProvider
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthentificationService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder
) {
    fun getToken(id: Long, login: String, roles: List<String> = emptyList()): String {
        return tokenProvider.generateTokenForUser(login, roles)
    }

    fun getTokenFromClaims(claims: Claims): String {
        return tokenProvider.generateToken(claims)
    }

    fun signUp(signUpDto: SignUpDto): String {
        if (userRepository.existsByLogin(signUpDto.login)) {
            throw Exception("User with this login already exists")
        }
        val role = roleRepository.findByRoleName(Role.RoleName.USER)!!
        val user = userRepository.save(
            User(
                login = signUpDto.login,
                passWord = passwordEncoder.encode(signUpDto.password),
                roles = listOf(role)
            )
        )
        val token = getToken(user.id!!, signUpDto.login, user.roles.map { it.authority })
        return token
    }

    fun signIn(signInDto: SignInDto): String {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(signInDto.login, signInDto.password)
        )
        SecurityContextHolder.getContext().authentication = auth
        val user = userRepository.findByLogin(auth.name) ?: throw Exception("User not found")
        val token = getToken(user.id!!, user.login, user.roles.map { it.roleName.name })
        return token
    }
}