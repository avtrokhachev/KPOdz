package cs.hse.DzKPO.features.users.controllers

import cs.hse.DzKPO.features.users.dto.SignInDto
import cs.hse.DzKPO.features.users.dto.SignUpDto
import cs.hse.DzKPO.features.users.services.AuthentificationService
import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthentificationController(
    private val authentificationService: AuthentificationService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody signUpDto: SignUpDto): ResponseEntity<String> {
        val token = authentificationService.signUp(signUpDto)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/signin")
    fun signIn(@RequestBody signInDto: SignInDto): ResponseEntity<String> {
        val token = authentificationService.signIn(signInDto)
        return ResponseEntity.ok(token)
    }
}