package org.victor5171.revoluttest.rateconversion.di

import javax.inject.Qualifier

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Qualifier
annotation class DecimalSeparator
