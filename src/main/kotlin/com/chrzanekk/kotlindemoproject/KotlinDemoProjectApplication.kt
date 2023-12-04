package com.chrzanekk.kotlindemoproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinDemoProjectApplication

fun main(args: Array<String>) {
	runApplication<KotlinDemoProjectApplication>(*args)
}
