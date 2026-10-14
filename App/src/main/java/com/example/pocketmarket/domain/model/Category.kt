package com.example.pocketmarket.domain.model

    @JvmInline
    value class Category(val value: String) {
        init {
            require(value.isNotBlank()) { "Category name cannot be blank." }
        }
    }
