package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.UnionType

@UnionType(["str", "func"])
sealed interface TFigureKind : TValue
