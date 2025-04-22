package com.kokodi.security.config

import com.kokodi.security.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.util.StringUtils
import java.io.IOException

@Component
class JwtAuthenticationFilter @Autowired constructor(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    companion object {
        const val BEARER_PREFIX = "Bearer "
        const val HEADER_NAME = "Authorization"
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader(HEADER_NAME)

        if (!isTokenCorrect(authHeader)) {
            filterChain.doFilter(request, response)
            return
        }

        authHeader?.let { header ->
            val jwt = header.substring(BEARER_PREFIX.length)
            val login = jwtService.extractLogin(jwt)

            if (login.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(login)
                
                if (jwtService.isTokenValid(jwt, userDetails.username)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        filterChain.doFilter(request, response)
    }


    private fun isTokenCorrect(authHeader: String?): Boolean {
        return !authHeader.isNullOrEmpty()
                && StringUtils.hasLength(authHeader)
                && authHeader.startsWith(BEARER_PREFIX)
    }

}
