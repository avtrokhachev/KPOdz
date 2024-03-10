package cs.hse.DzKPO

import cs.hse.DzKPO.features.users.db.entities.Role
import cs.hse.DzKPO.features.users.db.entities.User
import cs.hse.DzKPO.features.users.services.RoleService
import cs.hse.DzKPO.features.users.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder

@ConfigurationPropertiesScan
@SpringBootApplication
class DzKpoApplicationTests {
    @Bean
    fun onStart(userService: UserService, roleService: RoleService, passwordEncoder: PasswordEncoder) =
        CommandLineRunner { args ->
            resetUserAndRoleDb(
                roleService = roleService,
                userService = userService,
                passwordEncoder = passwordEncoder
            )
        }
}

fun resetUserAndRoleDb(
    roleService: RoleService,
    userService: UserService,
    passwordEncoder: PasswordEncoder
) {
    val userRole = roleService.createRole(Role(roleName = Role.RoleName.USER))
    userService.createUser(
        User(
            login = "user",
            passWord = passwordEncoder.encode("user"),
            roles = listOf(userRole)
        )
    )

    val adminRole = roleService.createRole(Role(roleName = Role.RoleName.ADMIN))
    userService.createUser(
        User(
            login = "admin",
            passWord = passwordEncoder.encode("admin"),
            roles = listOf(userRole, adminRole)
        )
    )
}

fun main(args: Array<String>) {
    runApplication<DzKpoApplicationTests>(*args)
}
