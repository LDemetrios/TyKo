package org.ldemetrios.tyko.driver.api

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is fragile API, which involves manual memory management and other fun things related to FII. Use with caution."
)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPEALIAS, AnnotationTarget.CONSTRUCTOR)
annotation class TyKoFFIEntity

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is internal API, usually made public for some magic tools to works. If you are see this message, you're probably doing something wrong"
)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPEALIAS, AnnotationTarget.CONSTRUCTOR)
annotation class TyKoInternalApi

@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is delicate API, usually meaning that incorrectness of Typst code handling here may cause native panic, and render a driver unusable"
)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPEALIAS, AnnotationTarget.CONSTRUCTOR)
annotation class TyKoCorrectnessAlert

