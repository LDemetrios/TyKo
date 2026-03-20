package org.ldemetrios.tyko.model

data class TJvmObject(val value: Any?) : TValue {
    override fun type(): TType = TType("jvm-object")
    override fun repr(sb: StringBuilder, registrar: (Any) -> Pair<Long, Long>) {
        val (session, id) = if (value == null) 0 to 0 else registrar(value)
        sb.append("jvm-object(").append(session).append(", ").append(id).append(")")
    }
}