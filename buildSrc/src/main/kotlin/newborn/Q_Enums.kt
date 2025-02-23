package newborn

import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.STRING
import org.ldemetrios.kotlinpoet.*
import org.ldemetrios.parsers.format

context(DeclarationsMetadata)
fun strEnumsFile() = modelFile("Enums") {
    val enums: List<EnumDeclaration> = declarations.filterIsInstance<EnumDeclaration>()
        .filter { !it.literal }
        .sortedBy { it.name }

    val equalEnums = enums.groupBy { it.variants.toSet() }.filterValues { it.size > 1 }.values
    if (equalEnums.isNotEmpty()) {
        println("WARNING: There are enums with the same set of variants. The generation system might be not prepared for it.")
        for (set in equalEnums) {
            println("\t= " + set.dropLast(1).jts(", ") { it.name } + " and " + set.last().name)
        }
    }
    for (i in enums) for (j in enums) {
        if (i == j) continue
        if (i.variants.toSet() == j.variants.toSet()) continue
        if (i.variants.toSet().isSubsetOf(j.variants.toSet())) {
            println("WARNING: ${i.name} is a subset of ${j.name}. The generation system might be not prepared for it.")
        }
    }

    val allVariants = enums.flatMap { it.variants }.toSet()

    val intersections = allVariants.map { vari ->
        enums.filter { vari in it.variants }.toSet()
    }.toSet() + enums.map{setOf(it)}.toSet()

    for (inter in intersections) {
        types.addInterface(inter.map { it.name }.toSet().sectToClassname()) {
            addSuperinterface(TSTR)
            annotations {
                add(TENUMTYPE) {
                    addArrayMember(inter.map{it.name})
                }
            }

            if (inter.size == 1) {
                val decl = inter.single()
                addUnionSupertypes(decl.name)

                if (decl.parent != null) {
                    addSuperinterface(decl.parent.ident.toClassname())
                    functions {
                        "type" {
                            addModifiers(OVERRIDE)
                            returns = TTYPE
                            appendLine("return super<T${decl.parent.ident.upCam}>.type()")
                        }
                    }
                }

                types.addCompanionObject {
                    properties {
                        for (option in decl.variants) {
                            (option.upCam)(decl.name.toClassname()) {
                                setInitializer(option.lwCam)
                            }
                        }
                    }
                }

                if (decl.sprepr) {
                    functions {
                        specialFormatFunction()
                    }
                }
            } else {
//                addModifiers(INTERNAL) // I think
            }

            val superintersections = intersections.filter { it.isSubsetOf(inter) && it != inter }

            for (superinter in superintersections) {
                addSuperinterface(superinter.map { it.name }.toSet().sectToClassname())
            }
        }

        types.addClass(inter.map { it.name }.toSet().sectToImplClassname()) {
            addSuperinterface(inter.map { it.name }.toSet().sectToClassname())

//            annotations {
//                add(TENUMTYPE)
//            }

            addModifiers(INTERNAL)

            setPrimaryConstructor {
                parameters {
                    "strValue"(STRING) {
                        addAsVal {
                            addModifiers(OVERRIDE)
                        }
                    }
                }
            }

            overriding.Any {
                equal {
                    appendLine("return this === other || other is TStr && this.strValue == other.strValue")
                }
                hashCode {
                    appendLine("return strValue.hashCode()")
                }
            }
        }
    }

    types.addInterface("DynamicEnum") {
        annotations {
            add(TENUMTYPE) {
                val names = declarations.filterIsInstance<EnumDeclaration>().map { it.name }
                addArrayMember(names)
            }
        }

        functions {
            "type"(OVERRIDE, ABSTRACT) { returns = TTYPE }
        }

        for (inter in intersections) {
            addSuperinterface(inter.map { it.name }.toSet().sectToClassname())
        }

        for (literal in declarations.filterIsInstance<EnumDeclaration>().filter { it.literal }) {
            addSuperinterface(literal.name.toClassname())
        }
    }

    properties {
        for (variant in allVariants) {
            val ty = enums.filter { variant in it.variants }.map { it.name }.toSet()
            variant.lwCam(ty.sectToClassname()) {
                addModifiers(PRIVATE)
                setInitializer("%T(%S)", ty.sectToImplClassname(), variant)
            }
        }

        "allVariants"(MAP(STRING, TSTR)) {
            initializer = buildCodeBlock {
                this.appendLine("mapOf(")
                for (vari in allVariants) {
                    appendLine("\t%S to %L,", vari, vari.lwCam)
                }
                appendLine(")")
            }
        }
    }
}


context(DeclarationsMetadata)
fun FileSpecBuilder.writeLiteralEnum(decl: EnumDeclaration) {
    //TODO Needs separation interface/impl?

    types.addInterface(decl.name.toClassname()) {
        addUnionSupertypes(decl.name)

        if (decl.parent != null) {
            addSuperinterface(decl.parent.ident.toClassname())
            functions {
                "type" {
                    addModifiers(OVERRIDE)
                    returns = TTYPE
                    appendLine("return super<T${decl.parent.ident.upCam}>.type()")
                }
            }
        }

        types.addCompanionObject {
            functions {
                "of" {
                    parameters { "str"(TSTR) {} }
                    returns = decl.name.toClassname().nullable()
                    appendLine("return of(str.strValue)")
                }

                "of" {
                    parameters { "str"(STRING) {} }
                    returns = decl.name.toClassname().nullable()
                    beginControlFlow("return when(str) {")
                    for (option in decl.variants) {
                        appendLine("%S -> %T.${option.upCam}", option, decl.name.toClassname())
                    }
                    appendLine("else -> null")
                    endControlFlow()
                }
            }

            properties {
                for (option in decl.variants) {
                    (option.upCam)(decl.name.toClassname()) {
                        setInitializer("%T(%S)", decl.name.toImplClassname(), option)
                    }
                }
            }
        }
    }

    types.addClass(decl.name.toImplClassname()) {
        addSuperinterface(decl.name.toClassname())
        setPrimaryConstructor {
            addModifiers(INTERNAL)
            parameters { "strValue"(STRING) { addAsVal { /*addModifiers(OVERRIDE)*/ } } }
        }

        addModifiers(INTERNAL)

        if (decl.sprepr) {
            functions {
                specialFormatFunction()
            }
        } else {
            functions {
                "format" {
                    returns = STRING
                    addModifiers(OVERRIDE)
                    appendLine("return strValue")
                }
            }
        }

        overriding.Any {
            equal { appendLine("return this === other || other is TStr && this.strValue == other.strValue") }
            hashCode { appendLine("return strValue.hashCode()") }
        }
    }
}
