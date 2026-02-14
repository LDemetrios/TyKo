package org.ldemetrios.tyko.compiler

import kotlinx.serialization.Serializable

/**
 * In-development Typst features that may be changed or removed at any time
 */
@Serializable
enum class Feature {
    /**
     * Export to HTML.
     */
    Html,

    /**
     * Extra accessibility features.
     */
    A11yExtras;

    companion object {
        fun all() : Set<Feature> = Feature.entries.toSet()
        fun none() : Set<Feature> = setOf()
    }
}
