package newborn

import com.squareup.kotlinpoet.CHAR_SEQUENCE
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeVariableName
import org.ldemetrios.kotlinpoet.*
import org.ldemetrios.kotlinpoet.overriding.AnyImplHelper.equal
import org.ldemetrios.kotlinpoet.overriding.AnyImplHelper.hashCode
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.chars
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.codePoints
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.get
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.isEmpty
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.length
import org.ldemetrios.kotlinpoet.overriding.CharSequenceImplHelper.subSequence

context(DeclarationsMetadata)
fun FileSpecBuilder.writePrimitive(decl: PrimitiveDeclaration) {
    val typeVars: List<TypeVariableName> = decl.params.bounded

    types.addInterface(decl.name.toClassname()) {
        annotations {
            add(TINTERFACETYPE) {
                addMember("%S", decl.name)
                addArrayMember(listOf(decl.name))
                addMember("%T::class", decl.name.toImplClassname())
            }
        }
        properties {
            "${decl.name.lwCam}Value"(decl.impl.toDelegate()) {}
        }
        typeVariables += typeVars
        if (decl.delegate) {
            addSuperinterface(decl.impl.toDelegate()/*, delegate = CodeBlock.of("value")*/)
        }
        addSuperinterface(TVALUE)
        addUnionSupertypes(setOf(decl.name), decl.params)
        functions {
            getTypeFunction(decl.name)
            "format" {
                returns = STRING
                addModifiers(OVERRIDE)
                appendLine("return Representations.reprOf(${decl.name.lwCam}Value)")
            }
        }
        typeCompanion(decl.name)
        updatePrimitive(decl.name)
    }

    types.addClass(decl.name.toImplClassname()) {
        addModifiers(INTERNAL, DATA)
        setPrimaryConstructor {
            parameters {
                "${decl.name.lwCam}Value"(decl.impl.toDelegate()) {
                    addAsVal { addModifiers(OVERRIDE) }
                }
            }
        }
        typeVariables += typeVars
        addSuperinterface(decl.name.toClassname()[typeVars])
        updatePrimitiveImpl(decl.name)
    }

    when (decl.name) {
        "str" -> {
            functions {
                "TStr" {
                    parameters {
                        "value"(STRING) {}
                    }
                    returns = TSTR
                    appendLine("return allVariants[value] ?: TStrImpl(value)")
                }
            }
            // Handles enums
        }

        "dictionary" -> {
            // Supposedly nothing to handle dictionaries
        }

        else -> {
            functions {
                decl.name.toClassname().simpleName {
                    returns = decl.name.toClassname()[typeVars]
                    typeVariables += typeVars.map { TypeVariableName(it.name, it.bounds, null) }
                    parameters {
                        "value"(decl.impl.toDelegate()) {}
                    }
                    appendLine("return %T(value)", decl.name.toImplClassname()[typeVars])
                }
            }
        }
    }
}

context(DeclarationsMetadata)
fun TypeSpecBuilder.updatePrimitive(name: String): Unit = when (name) {
    // TODO substitute with reflection???
    "str" -> {
//        addSuperinterface(CHAR_SEQUENCE)
//        overriding.CharSequence {
//            length {
//                setGetter {  appendLine("return strValue.length")}
//            }
//            chars {
//                appendLine("return strValue.chars()")
//            }
//            codePoints {
//                appendLine("return strValue.codePoints()")
//            }
//            isEmpty {
//                appendLine("return strValue.isEmpty()")
//            }
//            get {
//                appendLine("return strValue.get(index)")
//            }
//            subSequence {
//                appendLine("return strValue.subSequence(startIndex, endIndex)")
//            }
//        }
    }

    "dictionary" -> {
        val v = typeVariables.single()
        overriding.Map(STRING, v) {
            entries {
                setGetter {
                    appendLine("return dictionaryValue.entries")
                }
            }
            keys {
                setGetter {
                    appendLine("return dictionaryValue.keys")
                }
            }
            size {
                setGetter {
                    appendLine("return dictionaryValue.size")
                }
            }
            values {
                setGetter {
                    appendLine("return dictionaryValue.values")
                }
            }
            containsKey {
                appendLine("return dictionaryValue.containsKey(key)")
            }
            containsValue {
                appendLine("return this.dictionaryValue.containsValue(value)")
            }
            get {
                appendLine("return dictionaryValue.get(key)")
            }
            isEmpty {
                appendLine("return dictionaryValue.isEmpty()")
            }
            forEach {
                appendLine("return dictionaryValue.forEach(action)")
            }
            getOrDefault {
                appendLine("return dictionaryValue.getOrDefault(key, defaultValue)")
            }
        }
    }

    "array" -> {
        val e = typeVariables.single()
        overriding.List(e) {
            forEach {
                appendLine("return arrayValue.forEach(action)")
            }

            iterator {
                appendLine("return arrayValue.iterator()")
            }

            spliterator {
                appendLine("return arrayValue.spliterator()")
            }

            size {
                setGetter { appendLine("return arrayValue.size") }
            }

            contains {
                appendLine("return arrayValue.contains(element)")
            }

            containsAll {
                appendLine("return arrayValue.containsAll(elements)")
            }

            get {
                appendLine("return arrayValue.get(index)")
            }

//            getFirst {
//                appendLine("return arrayValue.getFirst()")
//            }
//
//            getLast {
//                appendLine("return arrayValue.getLast()")
//            }

            indexOf {
                appendLine("return arrayValue.indexOf(element)")
            }

            isEmpty {
                appendLine("return arrayValue.isEmpty()")
            }

            lastIndexOf {
                appendLine("return arrayValue.lastIndexOf(element)")
            }

            listIterator {
                appendLine("return arrayValue.listIterator()")
            }

            listIteratorByIndex {
                appendLine("return arrayValue.listIterator(index)")
            }
//
//            reversed {
//                appendLine("return arrayValue.reversed()")
//            }

            subList {
                appendLine("return arrayValue.subList(fromIndex, toIndex)")
            }

            parallelStream {
                appendLine("return arrayValue.parallelStream()")
            }

            stream {
                appendLine("return arrayValue.stream()")
            }

//            toArray {
//                appendLine("return arrayValue.toArray(generator)")
//            }
        }
    }

    else -> Unit
}

context(DeclarationsMetadata)
fun TypeSpecBuilder.updatePrimitiveImpl(name: String): Unit = when (name) {
    "str" -> {

    }

    "dictionary" -> {
        overriding.Any {
            equal { appendLine("return this === other || other is TDictionary<*> && this.dictionaryValue == other.dictionaryValue") }
            hashCode { appendLine("return dictionaryValue.hashCode()") }
        }
    }

    else -> Unit
}
