//package newborn
//
//import org.ldemetrios.kotlinpoet.*
//import com.squareup.kotlinpoet.*
//import java.io.File
//import kotlin.collections.plusAssign
//

//const val supertype = "TValue"
//

//

//context(DeclarationsMetadata)
//fun FileSpecBuilder.writeClass(decl: TypeDeclaration) {
//    types.addClassOrObject(decl) {
//        writeDataClassFields(decl)
//
//        if (decl.parent != null) {
//            addSuperinterface(decl.parent.ident.toClassname())
//        }
//
//        functions {
//            if (decl.sprepr) {
//                specialFormatFunction()
//            } else {
//                structFormatFunction(decl)
//            }
//        }
//        if (decl.fields.isNotEmpty()) companion(decl, null)
//    }
//}
//
//fun TypeSpecHandler.addClassOrObject(decl: TypeDeclaration, function: TypeSpecBuilder.() -> Unit) {
//    if (decl.fields.isEmpty()) {
//        addObject(decl.name.toClassname(), function)
//    } else {
//        addClass(decl.name.toClassname(), function)
//    }
//}
//
//context(DeclarationsMetadata)
//fun FileSpecBuilder.writeType0(decl: TypeDeclaration) {
//    types.addInterface(decl.name.toClassname()) {
//        typeVariables += decl.params.bounded
//
//        if (decl.parent == null) {
//            addSuperinterface(TVALUE)
//        } else {
//            superclass = decl.parent.ident.toClassname()
//        }
//
////        unionSupertypes(setOf(declaration.name), listOf()) TODO why commented out?
//
//        properties {
//            writeFields(decl)
//        }
//
//        functions {
//            if (decl.sprepr) {
//                specialFormatFunction()
//            } else {
//                structFormatFunction(decl)
//            }
//        }
//
//
//        functions {
//            getTypeFunction(decl.name)
//        }
//        companion(decl, "type")
//    }
//
//    types.addClassOrObject(decl) {
//        addModifiers(DATA)
//
//        typeVariables += decl.params.bounded
//        if (decl.fields.isNotEmpty()) {
//            writeDataClassFields(decl)
//        }
//
//        if (decl.parent == null) {
//            addSuperinterface(TVALUE)
//        } else {
//            addSuperinterface(decl.parent.ident.toClassname())
//        }
//
////        unionSupertypes(setOf(declaration.name), listOf()) TODO again, why is it commented out
//
//        functions {
//            if (decl.sprepr) {
//                specialFormatFunction()
//            } else {
//                structFormatFunction(decl)
//            }
//            getTypeFunction(decl)
//        }
//
//        if (decl.fields.isNotEmpty()) {
//            companion(decl, "type")
//        }
//
//        if (decl.fields.isEmpty()) {
//            types.addObject("T" + decl.name.upCam + "Type") {
//                superclass = TTYPE
//                addSuperclassConstructorParameter("%S", decl.name)
//            }
//        }
//    }
//}
//
//
//context(DeclarationsMetadata)
//fun FileSpecBuilder.writeElement(decl: TypeDeclaration) {
//    val variad = decl.fields.filter { it.variadic }.singleOrNull()
//
//    fun maybeContent(type: Type) = type.ident == "content" || type.ident == "any" || type.ident == "*"
//
//    val body = if (variad != null && variad.type.options.any(::maybeContent)) null else decl.fields.filter {
//        it.type.options.any(::maybeContent) && !it.variadic
//    }.let { fields ->
//        val it = fields.filter { it.name == "body" }.singleOrNull() ?: fields.singleOrNull()
//        if (it == null && fields.size > 1) {
//            println("- - ${decl.name}")
//        }
//        it
//    }
//
//    types.addClass(decl.name.toClassname()) {
//        typeVariables += decl.params.bounded
//
//        writeDataClassFields(decl) {
//            parameters {
//                "label"(TLABEL.nullable()) {
//                    setDefaultValue("null")
//                }
//            }
//        }
//        properties { "label"(TLABEL.nullable()) { } }
//
//        superclass = TCONTENT
//
//        functions {
//            if (decl.sprepr) {
//                specialFormatFunction()
//            } else {
//                structFormatFunction(decl, function = "elementRepr") { // TODO fails here
//                    append("\tlabel,")
//                }
//            }
//
//            "func" {
//                addModifiers(OVERRIDE)
//                returns = TELEMENT
//                appendLine("return %T.Elem", decl.name.toClassname())
//            }
//        }
//
//        companion(decl, null) {
//            properties {
//                "Elem"(TELEMENT) {
//                    setInitializer("TElement(%S)", decl.name)
//                }
//            }
//        }
//
//        /* TODO Later
//        val seqBuilder = if (decl.name.startsWith("math.")) {
//            "MathSequenceBuilder"
//        } else {
//            "SequenceBuilder"
//        }
//
//        if (body != null) {
//            val rest = decl.fields.filter { it != body }
//
//            "\tconstructor(".ln()
//
//            constructorFields(rest)
//
//            """
//       |        label: TLabel? = null,
//       |        ${body.name.lwCam}: $seqBuilder.() -> Unit,
//       |    ) : this(
//        """.cl.ln()
//            for (it in decl.fields.positionalFirst()) {
//                if (it.name == body.name) "\t\t$seqBuilder().apply(${body.name.lwCam}).create(),".ln()
//                else "\t\t${it.name.lwCam},".ln()
//            }
//            """
//       |        label,
//       |    )
//        """.cl.ln()
//        }
//
//        if (variad != null) {
//            val rest = decl.fields.filter { it != variad }
//
//            "\tconstructor(".ln()
//
//            constructorFields(rest.filter { it.positional })
//            "\t\tvararg ${variad.name.lwCam}: "()
//            fieldType(variad, ignoreVariadic = true)
//            ",".ln()
//            constructorFields(rest.filter { !it.positional })
//            "\t\tlabel: TLabel? = null,".ln()
//            "\t) : this(".ln()
//            for (it in decl.fields.positionalFirst()) {
//                if (it.name == variad.name) "\t\tTArray(*${variad.name.lwCam}),".ln()
//                else "\t\t${it.name.lwCam},".ln()
//            }
//            """
//       |        label,
//       |    )
//        """.cl.ln()
//        }
//
//        if (body != null && variad != null) {
//            val rest = decl.fields.filter { it != variad && it != body }
//
//            "\tconstructor(".ln()
//
//            constructorFields(rest.filter { it.positional })
//            "\t\tvararg ${variad.name.lwCam}: "()
//            fieldType(variad, ignoreVariadic = true)
//            ",".ln()
//            constructorFields(rest.filter { !it.positional })
//            "\t\tlabel: TLabel? = null,".ln()
//
//            "\t\t${body.name.lwCam}: $seqBuilder.() -> Unit,".ln()
//            "\t) : this(".ln()
//            for (it in decl.fields.positionalFirst()) {
//                when (it.name) {
//                    variad.name -> "\t\tTArray(*${variad.name.lwCam}),".ln()
//                    body.name -> "\t\t$seqBuilder().apply(${body.name.lwCam}).create(),".ln()
//                    else -> "\t\t${it.name.lwCam},".ln()
//                }
//            }
//            """
//       |        label,
//       |    )
//        """.cl.ln()
//        }
//
//        append("}\n\n")
//         */
//    }
//
//    if (decl.fields.any { it.settable }) {
//        types.addClass("TSet" + decl.name.upCam) {
//            writeDataClassFields(decl, settableOnly = true)
//            superclass = TSETRULE
//            addSuperclassConstructorParameter("%S", decl.name)
//            functions {
//                if (decl.spreprset) {
//                    specialFormatFunction()
//                } else {
//                    structFormatFunction(decl, settableOnly = true)
//                }
//            }
//        }
//    }
//}
///*
//
//context(DeclarationsMetadata)
//fun FileSpecBuilder.constructorFields(fields: List<ClassElem>) {
//    for (field in fields.positionalFirst()) {
//        append("\t\t${field.name.lwCam}: ")
//        fieldType(field)
//        if (!field.required) append(" = null")
//        ",".ln()
//    }
//}
//*/
//
//context(DeclarationsMetadata)
//fun FileSpecBuilder.writeSynthetic(decl: TypeDeclaration) {
//    val dictionarySuper = UnionType(decl.fields.flatMap { it.type.options }.distinct())
//
//    types.addClass(decl.name.toClassname()) {
//        typeVariables += decl.params.bounded
//        writeDataClassFields(decl)
//        superclass = TDICTIONARY(concreteType(dictionarySuper))
//
//        properties {
//            "value"(MAP(STRING, concreteType(dictionarySuper)), OVERRIDE) {
//                initializer = org.ldemetrios.kotlinpoet.buildCodeBlock {
//                    appendLine("return mapOf(")
//                    for (field in decl.fields) {
//                        appendLine("\t%S to ${field.name.lwCam},", field.name)
//                    }
//                    appendLine(")")
//                    if (decl.fields.any { !it.required }) appendLine(".filterValues { it != null }.castUnchecked()")
//                }
//            }
//        }
//
//        if (decl.sprepr) {
//            functions { specialFormatFunction() }
//        }
//
//        companion(decl, null)
//    }
//}
//
//fun fieldTypeStr(field: Field, removeGenerics: Boolean = false) = StringBuilder().apply {
//    fieldType(field, removeGenerics = removeGenerics).toString()
//}.toString()
//
//context(DeclarationsMetadata)
//fun TypeSpecBuilder.companion(decl: TypeDeclaration, kind: String?, func: TypeSpecBuilder.() -> Unit = { Unit }) {
//    val generic = decl.params.isNotEmpty()
//
//    types.addCompanionObject {
//        if (kind != null) {
//            superclass = kind.toClassname()
//            addSuperclassConstructorParameter("%S", decl.name)
//        }
//        for (field in decl.fields) {
//            if (!generic || decl.params.all { field.type.hasNoReferencesOf(it) }) {
//                properties {
//                    "${field.name.lwCam}Type"(INTERNAL_TYPE, INTERNAL) {
//                        setInitializer(field.actualType.toInternalDeclaration())
//                    }
//                }
//            } else {
//                functions {
//                    "${field.name.lwCam}Type" {
//                        addModifiers(INTERNAL)
//                        parameters {
//                            for (param in decl.params) param.name(INTERNAL_TYPE) {}
//                        }
//                        returns = INTERNAL_TYPE
//                        appendLine("return " + field.actualType.toInternalDeclaration())
//                    }
//                }
//            }
//        }
//        this.func()
//    }
//}
//


//

//
//// Synthetic, Class, Type, Element
