package newborn

import java.io.File
import org.ldemetrios.kotlinpoet.*
import com.squareup.kotlinpoet.*


context(DeclarationsMetadata)
fun writeSemiauto(root: String) {
    val special = "$root/src/main/kotlin/org/ldemetrios/tyko/model/semiauto"

    strEnumsFile().writeToFile(File(special))

    modelFile("Type") {
        types.addInterface("TType") {
            annotations {
                add(TINTERFACETYPE) {
                    addMember("%S", "type")
                    addMember("[%S]", "type")
                    addMember("TTypeImpl::class")
                }
            }
            addSuperinterface(TVALUE)
            addUnionSupertypes(setOf("type"), listOf())
            functions {
                getTypeFunction("type")
            }
            typeCompanion("type")
        }

        types.addClass("TTypeImpl") {
            addSuperinterface(TTYPE)
            addModifiers(OPEN)
            setPrimaryConstructor {
                parameters {
                    "name"(STRING) { addAsVal() }
                }
            }
            functions {
                specialFormatFunction()
            }
            overriding.Any {
                equal {
                    appendLine("return this === other || other is TTypeImpl && this.name == other.name")
                }
                hashCode {
                    appendLine("return name.hashCode()")
                }
            }
        }

    }.writeToFile(File(special))

    modelFile("Function") {
        types.addInterface("TFunction") {
            addSuperinterface(TVALUE)
            addUnionSupertypes(setOf("function"), listOf())
            functions {
                getTypeFunction("function")
                specialFormatFunction()
            }
            typeCompanion("function")
        }
    }.writeToFile(File(special))

    modelFile("Content") {
        types.addInterface("TContent") {
            annotations {
                add(TINTERFACETYPE) {
                    addMember("%S", "content")
                    addMember("[%S]", "content")
                    addMember("TContent::class")
                }
            }
            addSuperinterface(TVALUE)
            addModifiers(ABSTRACT)
            addUnionSupertypes("content")
            properties {
                "label"(TLABEL.nullable(), ABSTRACT) {}
            }
            functions {
                "func" {
                    addModifiers(ABSTRACT)
                    returns = TELEMENT
                }
                getTypeFunction("content")
            }
            typeCompanion("content")
        }
    }.writeToFile(File(special))

    modelFile("Unions") {
        for (union in allUnions) {
            types.addInterface("T" + union.jts("Or") { it.upCam }) {
                addSuperinterface(TVALUE)
                addModifiers(SEALED)
                annotations {
                    add(TUNIONTYPE) {
                        addArrayMember(union.toList())
                    }
                }
                for (param in union.mapNotNull { allGenerics[it] }.flatten()) {
                    addTypeVariables(
                        TypeVariableName(param.name, listOf(TVALUE), param.poetVariance)
                    )
                }
                addUnionSupertypes(union, union.flatMap { allGenerics[it] ?: listOf() })
            }
        }
    }.writeToFile(File(special))

    modelFile("Dynamic") {
        types.addInterface("TDynamic") {
            addSuperinterface(TVALUE)
            addSuperinterface(ClassName(PACK, "DynamicEnum"))

            for (decl in declarations) when (decl) {
                is EnumDeclaration -> Unit
                is PrimitiveDeclaration -> {
                    val name = decl.name.toClassname()
                    val generics = decl.params.map {
                        when {
                            it.variance.trim() == "out" -> TDYNAMIC
                            it.variance.trim() == "in" -> TVALUE
                            else -> throw AssertionError()
                        }
                    }
                    addSuperinterface(name[generics])
                }

                is TypeDeclaration -> {
                    if (decl.kind == "synthetic") continue
                    if (decl.kind == "element" && decl.fields.any { it.settable }) {
                        addSuperinterface(ClassName(PACK, "TSet" + decl.name.upCam))
                    }
                    val name = decl.name.toClassname()
                    val generics = decl.params.map {
                        when {
                            it.variance.trim() == "out" -> TDYNAMIC
                            it.variance.trim() == "in" -> TVALUE
                            else -> throw AssertionError()
                        }
                    }
                    addSuperinterface(name[generics])
                }
            }
            addSuperinterface(ClassName(PACK, "TMargin")(TDYNAMIC))
            addSuperinterface(ClassName(PACK, "TSides")(TDYNAMIC))
            addSuperinterface(TELEMENT)

            properties {
                "elem"(STRING, OVERRIDE) {}
                "size"(INT, OVERRIDE) {}
                val fields = declarations
                    .filterIsInstance<TypeDeclaration>()
                    .filter{it.name != "location" && it.name != "point"}
                    .flatMap { it.fields }
                    .map { it.name.lwCam }
                    .toSet()
                for (f in fields) {
                    (f)(TDYNAMIC, OVERRIDE) {
                        setGetter { appendLine("return fieldAccess(%S)", f) }
                    }
                }
            }

            functions {
                "fieldAccess"(ABSTRACT) {
                    parameters { "field"(STRING) {} }
                    returns = TDYNAMIC
                }
                "type"(OVERRIDE, ABSTRACT) { returns = TTYPE }
                "format"(OVERRIDE, ABSTRACT) { returns = STRING }
                "isEmpty"(OVERRIDE, ABSTRACT) { returns = BOOLEAN }
                "func"(OVERRIDE, ABSTRACT) { returns = TELEMENT }
                "get"(OVERRIDE, ABSTRACT) {
                    returns = TDYNAMIC
                    parameters {
                        "index"(INT) {}
                    }
                }
            }

        }
    }.writeToFile(File(special))

    File("$special/SemiautoDeserialization.kt").writeByAnyMeans {
        """
            package org.ldemetrios.tyko.model

            import org.ldemetrios.tyko.operations.*
            import org.ldemetrios.js.*
            import kotlin.reflect.KType
            import kotlin.reflect.typeOf
            import org.ldemetrios.utilities.cast
            import org.ldemetrios.utilities.castUnchecked
        """.trimIndent().ln.invoke()

        """
       |internal val interfaceTypes = mutableMapOf<String, kotlin.reflect.KClass<out TValue>>(
        """.cl.ln()

        for (decl in declarations) if (decl is TypeDeclaration && !decl.abstract) {
            "    \"${decl.name}\" to T${decl.name.upCam}::class,".ln()
        }

        """
       |)
       |
       |internal val setInterfaceTypes = mutableMapOf<String, kotlin.reflect.KClass<out TSetRule>>(
        """.cl.ln()

        for (decl in declarations) if (decl is TypeDeclaration && decl.kind == "element" && decl.fields.any { it.settable }) {
            "    \"set-${decl.name}\" to TSet${decl.name.upCam}::class,".ln()
        }
        ")".ln.ln()

        "internal val genericTypes = mutableMapOf<String, Int>(".ln()
        for (decl in declarations) if (decl is GenericDeclaration) {
            "    \"${decl.name}\" to ${decl.params.size},".ln()
        }
        ")".ln.ln()

        "internal val implTypes = mutableMapOf<String, kotlin.reflect.KClass<out TValue>>(".ln()
        for (decl in declarations) if (decl is TypeDeclaration) {
            if (decl.kind == "element" && decl.fields.any { it.settable }) {
                "    \"set-${decl.name}\" to TSet${decl.name.upCam}Impl::class,".ln()
            }
            if (!decl.abstract && decl.allFields.isNotEmpty()) {
                "    \"${decl.name}\" to T${decl.name.upCam}Impl::class,".ln()
            }
        }
        ")".ln.ln()
    }
}

fun FunSpecHandlerScope.getTypeFunction(name: String) {
    val actual = "T" + name.upCam
    getTypeFunction0(actual)
}

private fun FunSpecHandlerScope.getTypeFunction0(name: String) {
    "type" {
        returns = TTYPE
        addModifiers(OVERRIDE)
        appendLine("return $name")
    }
}

fun TypeSpecBuilder.typeCompanion(name: String) {
    types.add(
        buildCompanionObjectTypeSpec {
            superclass = TTYPEIMPL
            addSuperclassConstructorParameter("%S", name)
        }
    )
}
