package com.example.tier.base

import java.io.IOException

class InternetException() : IOException() {
        override val message: String
            get() = "Please check your internet connection"
    }
