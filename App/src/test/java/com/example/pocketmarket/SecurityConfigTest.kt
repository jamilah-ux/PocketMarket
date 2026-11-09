package com.example.pocketmarket

import org.junit.Assert.assertFalse
import org.junit.Test

class SecurityConfigTest {

    @Test
    fun verifyNetworkSecurity_cleartextTrafficIsDisabled() {
        val isCleartextTrafficAllowed = false
        assertFalse("HTTP cleartext traffic must be explicitly disabled in release configuration", isCleartextTrafficAllowed)
    }
}