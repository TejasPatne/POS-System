package com.tejas.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearer-key")
                )
                .info(new Info()
                        .title("Inventory Management API")
                        .version("1.0")
                        .description("""
                                A multi-tenant SaaS Point of Sale (POS) system for managing retail businesses and their day-to-day operations.

                                ### Core Business Flow
                                                            
                                **Store → Branch → Employees → Products → Inventory → Orders → Refunds → Shift Reports**
                                                            
                                - **Stores & Branches**: Businesses can manage multiple physical locations and their employees.
                                - **Products & Inventory**: Maintain a centralized product catalogue with branch-specific stock.
                                - **Orders**: Cashiers process customer purchases and record payment methods.
                                - **Refunds**: Process returns against existing orders and track refunded amounts.
                                - **Shifts**: Cashiers operate within shifts, with sales and refunds tracked throughout the shift.
                                - **Reports**: Shift reports summarize sales, refunds, net sales, payment distribution, top-selling products, and recent transactions.
                                                            
                                ### Getting Started
                                                            
                                1. Register or use an existing account.
                                2. Authenticate through the login endpoint.
                                3. Copy the returned JWT.
                                4. Click **Authorize 🔒** and enter the token.
                                5. Explore the protected APIs.
                                                            
                                                            
                                ### How to use

                                1. Register (`/auth/signup`) or use an existing account.
                                2. Authenticate through the login endpoint (`/auth/login`).
                                3. Copy the returned JWT.
                                4. Click **Authorize 🔒** and enter the token.
                                5. Explore the protected APIs.

                                ## Tech Stack

                                Java · Spring Boot · Spring Security · JWT ·
                                PostgreSQL · JPA/Hibernate · Docker
                                """));
    }
}