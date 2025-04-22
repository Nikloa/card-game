package com.kokodi.service

import com.kokodi.entity.User
import com.kokodi.exception.UserAlreadyExistsException
import com.kokodi.exception.UserNotFoundException
import com.kokodi.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class UserService @Autowired constructor(
    private val repository: UserRepository
) {

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    fun save(user: User): User {
        return repository.save(user)
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    fun create(user: User): User {
        if (repository.existsByLogin(user.login)) {
            throw UserAlreadyExistsException("User ${user.login} already exists")
        }

        return save(user)
    }

    /**
     * Получение пользователя по логину
     *
     * @return пользователь
     */
    fun getByLogin(login: String): User {
        return repository.findByLogin(login)?:
            throw UserNotFoundException("User not found")
    }
}

