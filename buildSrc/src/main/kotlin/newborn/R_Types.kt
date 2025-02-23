package newborn

import org.ldemetrios.kotlinpoet.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ClassName
import java.io.File
import kotlin.collections.plusAssign

data class DeclarationsMetadata(
    val allUnions: Set<Set<String>>,
    val allGenerics: Map<String, List<TypeParam>>,
    val kinds: Map<String, String>,
    val declarations: List<Declaration>,
//    val serializer: MutableMap<String, String> = mutableMapOf(),
)

// Synthetic, Class, Type, Element

context(DeclarationsMetadata)
fun FileSpecBuilder.writeType(decl: TypeDeclaration) {
    // In general, create an interface and an implementation.

    val name = if (decl.kind == "synthetic") {
        ClassName(PACK, decl.name.toClassname().simpleName + "Super")
    } else {
        decl.name.toClassname()
    }
    types.addInterface(name) {
        typeVariables += decl.params.bounded

        addUnionSupertypes(decl.name)

        properties {
            val fields = decl.fields.positionalFirst()
            for (field in fields) (field.name.lwCam)(fieldType(field)) { }
        }

        if (decl.kind == "synthetic") {
//            annotations {
//                add(TSYNTHETICTYPE)
//            }
            return@addInterface
        }

        if (!decl.requiresSeparateType && !decl.abstract) {
            annotations {
                add(TINTERFACETYPE) {
                    addMember("%S", decl.name)
                    addArrayMember(generateSequence(decl) { it.parentDecl() as TypeDeclaration? }.map { it.name }
                        .toList())
                    addMember("%T::class", decl.name.toImplClassname())
                }
            }
        }

        if (decl.parent != null) addSuperinterface(decl.parent.toDelegate())
        else addSuperinterface(TVALUE)

        if (decl.kind == "type") {
            typeFunction {
                appendLine("return ${decl.name.toClassname().simpleName}" + "Type".takeIfOrEmpty(decl.requiresSeparateType))
            }
        }

        if (decl.sealed) {
            funcFunction { addModifiers(ABSTRACT) }
        }

        if (decl.kind == "element") {
            elementFunction {
                appendLine("return ${decl.name.toClassname().simpleName}")
            }
        }

        if ((decl.parentDecl() as? TypeDeclaration)?.sealed == true) {
            funcFunction {
                addModifiers(OVERRIDE)
                appendLine("return %T", decl.name.toClassname())
            }
        }


        val companionSuper = when {
            decl.sealed || decl.abstract -> TTYPEIMPL
            decl.isObject() -> decl.name.toClassname()
            decl.kind == "type" -> TTYPEIMPL
            decl.kind == "element" -> TELEMENTIMPL
            decl.parentDecl()?.castOrNull<TypeDeclaration>()?.sealed == true -> TFUNCTION
            else -> {
                println("WARNING: Is it possible that `${decl.name}` doesn't require a companion object?")
                null
            }
        }

        if (companionSuper != null) types.addCompanionObject {
            when (companionSuper) {
                TTYPEIMPL, TELEMENTIMPL -> {
                    superclass = companionSuper
                    addSuperclassConstructorParameter("%S", decl.name)
                }
                TFUNCTION -> {
                    addSuperinterface(companionSuper)
                    functions {
                        exactlyFormatFunction(decl.name)
                    }
                }
                else -> {
                    addSuperinterface(companionSuper)
                    functions {
                        specialFormatFunction()
                    }
                }
            }


            val generic = decl.params.isNotEmpty()

            for (field in decl.fields) {
                if (!generic || decl.params.all { field.type.hasNoReferencesOf(it) }) {
                    properties {
                        "${field.name.lwCam}Type"(INTERNAL_TYPE, INTERNAL) {
                            setInitializer(field.actualType.toInternalDeclaration())
                        }
                    }
                } else {
                    functions {
                        "${field.name.lwCam}Type" {
                            addModifiers(INTERNAL)
                            parameters {
                                for (param in decl.params) param.name(INTERNAL_TYPE) {}
                            }
                            returns = INTERNAL_TYPE
                            appendLine("return " + field.actualType.toInternalDeclaration())
                        }
                    }
                }
            }
        }


//        when (decl.kind) {
//            "class" -> this.writeClass(declaration)
//            "type" -> this.writeType0(declaration)
//            "element" -> this.writeElement(declaration)
//            "synthetic" -> this.writeSynthetic(declaration)
//        }
    }

    if (!decl.requiresSeparateType && !decl.abstract && decl.kind != "synthetic") {
        types.addClass(decl.name.toImplClassname()) {
            typeVariables += decl.params.bounded

            addModifiers(INTERNAL, DATA)

            addSuperinterface(decl.name.toClassname()[decl.params.bounded])

            writeDataClassFields(decl, onEach = {
                addModifiers(OVERRIDE)
                annotations {
                    add(SERIALNAME) { addMember("%S", it.name) }
                }
            })

            functions {
                when {
                    decl.sprepr -> {
                        specialFormatFunction()
                    }
                    decl.kind == "element" -> {
                        structFormatFunction(decl, function = "elementRepr")
                    }
                    decl.kind == "type" || decl.kind == "class" -> {
                        structFormatFunction(decl, function = "structRepr")
                    }
                }
            }
        }

        functions {
            (decl.name.toClassname().simpleName) {
                annotations {
                    add(TYPSTOVERLOADS)
                }

                typeVariables += decl.params.map { TypeVariableName(it.name, listOf(TVALUE) /*No variance*/) }

                returns = decl.name.toClassname()[decl.params.bounded]

                val fields = decl.allFields.positionalFirst()
                parameters {
                    for (field in fields) {
                        (field.name.lwCam)(fieldType(field)) {
                            if (!field.required) setDefaultValue("null")
                            annotations {
                                if (field.name == "body" && field.type.options.singleOrNull()?.let {
                                        it.ident == "content"
                                    } == true) {
                                    add(TCONTENTBODY)
                                }
                                if (field.variadic) add(TVARARG)
                            }
                        }
                    }
                }
                appendLine(
                    "return %T(${fields.joinToString(", ") { "`" + it.name.lwCam + "`" }})",
                    decl.name.toImplClassname()
                )
            }
        }
    }

    if (decl.requiresSeparateType) {
        types.addObject("T" + decl.name.upCam + "Type") {
            superclass = TTYPEIMPL
            addSuperclassConstructorParameter("%S", decl.name)
        }
    }

    if (decl.kind == "element" && decl.fields.any { it.settable }) {
        types.addInterface("TSet" + decl.name.upCam) {
            annotations {
                add(TSETRULETYPE) {
                    addMember("%S", decl.name)
                    addMember("TSet" + decl.name.upCam + "Impl::class")
                }
            }

            addSuperinterface(TSETRULE)

            properties {
                "elem"(STRING) {
                    addModifiers(OVERRIDE)
                    setGetter { appendLine("return %S", decl.name) }
                }
                writeFields(decl, settableOnly = true)
            }

            functions {
                if (decl.spreprset) {
                    specialFormatFunction()
                } else {
                    structFormatFunction(decl, settableOnly = true, function = "setRepr")
                }
            }
        }

        types.addClass("TSet" + decl.name.upCam + "Impl") {
            addModifiers(INTERNAL)
            addSuperinterface(ClassName(PACK, "TSet" + decl.name.upCam))
            writeDataClassFields(decl, onEach = {
                annotations {
                    add(SERIALNAME) { addMember("%S", it.name) }
                }
                addModifiers(OVERRIDE)
            }, settableOnly = true)
        }

        functions {
            ("TSet" + decl.name.upCam) {
                annotations {
                    add(TYPSTOVERLOADS)
                }

                returns = ClassName(PACK, "TSet" + decl.name.upCam)

                val fields = decl.allFields.positionalFirst()
                parameters {
                    for (field in fields) if (field.settable) {
                        (field.name.lwCam)(fieldType(field)) {
                            if (!field.required) setDefaultValue("null")
                        }
                    }
                }
                appendLine(
                    "return TSet${decl.name.upCam}Impl(${
                        fields.filter { it.settable }.joinToString(", ") { "`" + it.name.lwCam + "`" }
                    })",
                )
            }
        }
    }
}

context(DeclarationsMetadata)
val TypeDeclaration.requiresSeparateType
    get() = !sealed && !abstract && this.isObject()

private fun TypeSpecBuilder.typeFunction(setup: FunSpecBuilder.() -> Unit) {
    functions {
        "type" {
            returns = TTYPE
            addModifiers(OVERRIDE)
            setup()
        }
    }
}

private fun TypeSpecBuilder.elementFunction(setup: FunSpecBuilder.() -> Unit) {
    functions {
        "func" {
            addModifiers(OVERRIDE)
            returns = TELEMENT
            setup()
        }
    }
}

private fun TypeSpecBuilder.funcFunction(setup: FunSpecBuilder.() -> Unit) {
    functions {
        "func" {
            returns = TFUNCTION
            setup()
        }
    }
}


context(DeclarationsMetadata)
private tailrec fun TypeDeclaration.isObject(): Boolean {
    return when {
//        this == null -> return true
        fields.isNotEmpty() -> false
        parent == null -> true
        else -> (parentDecl()!! as? TypeDeclaration
            ?: throw AssertionError("Non-type parent, is it possible?!")).isObject()
    }
}

private fun String.takeIfOrEmpty(cond: Boolean): String = if (cond) this else ""

context(DeclarationsMetadata)
fun TypeDeclaration.parentDecl(): Declaration? {
    if (parent == null) return null
    return declarations.find { it.name == parent.ident }!!
}

//val syntheticSupertypes = mapOf(
//    "location" to setOf("int", "length").unionToClassname(),
//    "point" to "length".toClassname(),
//    "margin" to TypeVariableName("M"),
//    "sides" to TypeVariableName("S"),
//)
