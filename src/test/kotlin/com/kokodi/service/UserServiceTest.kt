package com.kokodi.service

import com.kokodi.exception.UserAlreadyExistsException
import com.kokodi.exception.UserNotFoundException
import com.kokodi.repository.UserRepository
import com.kokodi.util.TestData
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Тест для [UserService].
 */
@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `When creating a user expect a saved user`() {
        val user = TestData.createFirstUser()
        `when`(userRepository.existsByLogin(user.login)).thenReturn(false)
        `when`(userRepository.save(user)).thenReturn(user)

        val savedUser = userService.create(user)

        assertAll("Check created user properties",
            { assertNotNull(savedUser) },
            { assertEquals(user.login, savedUser.login) }
        )
        verify(userRepository, times(1)).existsByLogin(user.login)
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `When creating an existing user expect UserAlreadyExistsException`() {
        val existingUser = TestData.createFirstUser()
        `when`(userRepository.existsByLogin(existingUser.login)).thenReturn(true)

        val exception = assertThrows<UserAlreadyExistsException> {
            userService.create(existingUser)
        }

        assertEquals("User ${existingUser.login} already exists", exception.message)
    }

    @Test
    fun `When getting a user by login expect the correct user`() {
        val expectedUser = TestData.createFirstUser()
        `when`(userRepository.findByLogin(expectedUser.login)).thenReturn(expectedUser)

        val actualUser = userService.getByLogin(expectedUser.login)

        assertAll(
            { assertNotNull(actualUser) },
            { assertEquals(expectedUser.login, actualUser.login) }
        )
    }

    @Test
    fun `When getting a non-existing user by login expect UserNotFoundException`() {
        val nonExistingLogin = "NON_EXISTING_LOGIN"
        `when`(userRepository.findByLogin(nonExistingLogin)).thenReturn(null)

        val exception = assertThrows<UserNotFoundException> {
            userService.getByLogin(nonExistingLogin)
        }

        assertEquals("User not found", exception.message)
    }
}