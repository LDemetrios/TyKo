package org.ldemetrios.tyko.compiler

import kotlinx.serialization.Serializable

@Serializable
enum class Feature {
    Html,
    A11yExtras;

    companion object {
        fun all() : Set<Feature> = Feature.entries.toSet()
        fun none() : Set<Feature> = setOf()
    }
}
