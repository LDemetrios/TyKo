package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.Option
import org.ldemetrios.tyko.model.Positional
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.Settable
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TStr

//!https://typst.app/docs/reference/pdf/attach/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
 * 
 * A file that will be attached to the output PDF.
 * 
 * This can be used to distribute additional files associated with the PDF within it. PDF readers will display the files in a file listing.
 * 
 * Some international standards use this mechanism to attach machine-readable data (e.g., ZUGFeRD/Factur-X for invoices) that mirrors the visual content of the PDF.
 * 
 * **_Example_**
 * 
 * ```typ
 * #pdf.attach(
 *   "experiment.csv",
 *   relationship: "supplement",
 *   mime-type: "text/csv",
 *   description: "Raw Oxygen readings from the Arctic experiment",
 * )
 * ```
 * 
 * **_Notes_**
 * 
 * - This element is ignored if exporting to a format other than PDF.
 * - File attachments are not currently supported for PDF/A-2, even if the attached file conforms to PDF/A-1 or PDF/A-2.
 */
@SerialName("pdf.attach")
data class TPdfAttach(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
     * 
     * The [path](https://typst.app/docs/reference/syntax/#paths) of the file to be attached.
     * 
     * Must always be specified, but is only read from if no data is provided in the following argument.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val path: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
     * 
     * Raw file data, optionally.
     * 
     * If omitted, the data is read from the specified path.
     * 
     * Required, positional; Typst type: bytes
     */
    @all:Positional val data: TBytes? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
     * 
     * The relationship of the attached file to the document.
     * 
     * Ignored if export doesn't target PDF/A-3.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"source"` | The PDF document was created from the source file. |
     * | `"data"` | The file was used to derive a visual presentation in the PDF. |
     * | `"alternative"` | An alternative representation of the document. |
     * | `"supplement"` | Additional resources for the document. |
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val relationship: Option<TPdfAttachRelationship>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
     * 
     * The MIME type of the attached file.
     * 
     * Settable; Typst type: none|str
     */
    @all:SerialName("mime-type") @all:Settable val mimeType: Option<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/attach/](https://typst.app/docs/reference/pdf/attach/)
     * 
     * A description for the attached file.
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val description: Option<TStr>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("pdf.attach")
    }
}

@SerialName("str")
enum class TPdfAttachRelationship : IntoStr, Option<TPdfAttachRelationship> {
    SOURCE, DATA, ALTERNATIVE, SUPPLEMENT;

    override fun intoValue(): TStr = toString().lowercase().t

    companion object {
        fun fromValue(value: TValue): TPdfAttachRelationship {
            val candidate = (value as? TStr)?.value?.uppercase()
            return entries.find { it.name == candidate }
                ?: throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("set-pdf.attach")
data class TSetPdfAttach(
    override val internals: SetRuleInternals? = null,
    val relationship: Option<TPdfAttachRelationship>? = null,
    val mimeType: Option<TStr>? = null,
    val description: Option<TStr>? = null,
) : TSetRule()
