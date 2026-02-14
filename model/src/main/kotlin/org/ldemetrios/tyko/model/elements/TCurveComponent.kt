package org.ldemetrios.tyko.model

/**
 * The element that can be a [TCurve] component.
 */
sealed class TCurveComponent(override val label: TLabel?): TContent()
