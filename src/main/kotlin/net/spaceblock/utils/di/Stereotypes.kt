package net.spaceblock.utils.di

import com.google.inject.BindingAnnotation
import com.google.inject.Singleton

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@BindingAnnotation
@Singleton
annotation class MinecraftController

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@BindingAnnotation
@Singleton
annotation class Service

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@BindingAnnotation
@Singleton
annotation class Repository
