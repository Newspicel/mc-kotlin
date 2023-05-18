package net.spaceblock.utils.di.annotations

import org.springframework.stereotype.Controller
import javax.inject.Singleton

@Controller
@Singleton
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class MinecraftController()
