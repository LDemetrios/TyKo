package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TPagePaper(override val value: String) : TStr {
    A0("a0"),
    A1("a1"),
    A2("a2"),
    A3("a3"),
    A4("a4"),
    A5("a5"),
    A6("a6"),
    A7("a7"),
    A8("a8"),
    A9("a9"),
    A10("a10"),
    A11("a11"),
    IsoB1("iso-b1"),
    IsoB2("iso-b2"),
    IsoB3("iso-b3"),
    IsoB4("iso-b4"),
    IsoB5("iso-b5"),
    IsoB6("iso-b6"),
    IsoB7("iso-b7"),
    IsoB8("iso-b8"),
    IsoC3("iso-c3"),
    IsoC4("iso-c4"),
    IsoC5("iso-c5"),
    IsoC6("iso-c6"),
    IsoC7("iso-c7"),
    IsoC8("iso-c8"),
    DinD3("din-d3"),
    DinD4("din-d4"),
    DinD5("din-d5"),
    DinD6("din-d6"),
    DinD7("din-d7"),
    DinD8("din-d8"),
    SisG5("sis-g5"),
    SisE5("sis-e5"),
    AnsiA("ansi-a"),
    AnsiB("ansi-b"),
    AnsiC("ansi-c"),
    AnsiD("ansi-d"),
    AnsiE("ansi-e"),
    ArchA("arch-a"),
    ArchB("arch-b"),
    ArchC("arch-c"),
    ArchD("arch-d"),
    ArchE1("arch-e1"),
    ArchE("arch-e"),
    JisB0("jis-b0"),
    JisB1("jis-b1"),
    JisB2("jis-b2"),
    JisB3("jis-b3"),
    JisB4("jis-b4"),
    JisB5("jis-b5"),
    JisB6("jis-b6"),
    JisB7("jis-b7"),
    JisB8("jis-b8"),
    JisB9("jis-b9"),
    JisB10("jis-b10"),
    JisB11("jis-b11"),
    SacD0("sac-d0"),
    SacD1("sac-d1"),
    SacD2("sac-d2"),
    SacD3("sac-d3"),
    SacD4("sac-d4"),
    SacD5("sac-d5"),
    SacD6("sac-d6"),
    IsoId1("iso-id-1"),
    IsoId2("iso-id-2"),
    IsoId3("iso-id-3"),
    AsiaF4("asia-f4"),
    JpShirokuBan4("jp-shiroku-ban-4"),
    JpShirokuBan5("jp-shiroku-ban-5"),
    JpShirokuBan6("jp-shiroku-ban-6"),
    JpKiku4("jp-kiku-4"),
    JpKiku5("jp-kiku-5"),
    JpBusinessCard("jp-business-card"),
    CnBusinessCard("cn-business-card"),
    EuBusinessCard("eu-business-card"),
    FrTellière("fr-tellière"),
    FrCouronneÉcriture("fr-couronne-écriture"),
    FrCouronneÉdition("fr-couronne-édition"),
    FrRaisin("fr-raisin"),
    FrCarré("fr-carré"),
    FrJésus("fr-jésus"),
    UkBrief("uk-brief"),
    UkDraft("uk-draft"),
    UkFoolscap("uk-foolscap"),
    UkQuarto("uk-quarto"),
    UkCrown("uk-crown"),
    UkBookA("uk-book-a"),
    UkBookB("uk-book-b"),
    UsLetter("us-letter"),
    UsLegal("us-legal"),
    UsTabloid("us-tabloid"),
    UsExecutive("us-executive"),
    UsFoolscapFolio("us-foolscap-folio"),
    UsStatement("us-statement"),
    UsLedger("us-ledger"),
    UsOficio("us-oficio"),
    UsGovLetter("us-gov-letter"),
    UsGovLegal("us-gov-legal"),
    UsBusinessCard("us-business-card"),
    UsDigest("us-digest"),
    UsTrade("us-trade"),
    NewspaperCompact("newspaper-compact"),
    NewspaperBerliner("newspaper-berliner"),
    NewspaperBroadsheet("newspaper-broadsheet"),
    Presentation16_9("presentation-16-9"),
    Presentation4_3("presentation-4-3"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TPagePaper? = when(str) {
                    "a0" -> TPagePaper.A0
            "a1" -> TPagePaper.A1
            "a2" -> TPagePaper.A2
            "a3" -> TPagePaper.A3
            "a4" -> TPagePaper.A4
            "a5" -> TPagePaper.A5
            "a6" -> TPagePaper.A6
            "a7" -> TPagePaper.A7
            "a8" -> TPagePaper.A8
            "a9" -> TPagePaper.A9
            "a10" -> TPagePaper.A10
            "a11" -> TPagePaper.A11
            "iso-b1" -> TPagePaper.IsoB1
            "iso-b2" -> TPagePaper.IsoB2
            "iso-b3" -> TPagePaper.IsoB3
            "iso-b4" -> TPagePaper.IsoB4
            "iso-b5" -> TPagePaper.IsoB5
            "iso-b6" -> TPagePaper.IsoB6
            "iso-b7" -> TPagePaper.IsoB7
            "iso-b8" -> TPagePaper.IsoB8
            "iso-c3" -> TPagePaper.IsoC3
            "iso-c4" -> TPagePaper.IsoC4
            "iso-c5" -> TPagePaper.IsoC5
            "iso-c6" -> TPagePaper.IsoC6
            "iso-c7" -> TPagePaper.IsoC7
            "iso-c8" -> TPagePaper.IsoC8
            "din-d3" -> TPagePaper.DinD3
            "din-d4" -> TPagePaper.DinD4
            "din-d5" -> TPagePaper.DinD5
            "din-d6" -> TPagePaper.DinD6
            "din-d7" -> TPagePaper.DinD7
            "din-d8" -> TPagePaper.DinD8
            "sis-g5" -> TPagePaper.SisG5
            "sis-e5" -> TPagePaper.SisE5
            "ansi-a" -> TPagePaper.AnsiA
            "ansi-b" -> TPagePaper.AnsiB
            "ansi-c" -> TPagePaper.AnsiC
            "ansi-d" -> TPagePaper.AnsiD
            "ansi-e" -> TPagePaper.AnsiE
            "arch-a" -> TPagePaper.ArchA
            "arch-b" -> TPagePaper.ArchB
            "arch-c" -> TPagePaper.ArchC
            "arch-d" -> TPagePaper.ArchD
            "arch-e1" -> TPagePaper.ArchE1
            "arch-e" -> TPagePaper.ArchE
            "jis-b0" -> TPagePaper.JisB0
            "jis-b1" -> TPagePaper.JisB1
            "jis-b2" -> TPagePaper.JisB2
            "jis-b3" -> TPagePaper.JisB3
            "jis-b4" -> TPagePaper.JisB4
            "jis-b5" -> TPagePaper.JisB5
            "jis-b6" -> TPagePaper.JisB6
            "jis-b7" -> TPagePaper.JisB7
            "jis-b8" -> TPagePaper.JisB8
            "jis-b9" -> TPagePaper.JisB9
            "jis-b10" -> TPagePaper.JisB10
            "jis-b11" -> TPagePaper.JisB11
            "sac-d0" -> TPagePaper.SacD0
            "sac-d1" -> TPagePaper.SacD1
            "sac-d2" -> TPagePaper.SacD2
            "sac-d3" -> TPagePaper.SacD3
            "sac-d4" -> TPagePaper.SacD4
            "sac-d5" -> TPagePaper.SacD5
            "sac-d6" -> TPagePaper.SacD6
            "iso-id-1" -> TPagePaper.IsoId1
            "iso-id-2" -> TPagePaper.IsoId2
            "iso-id-3" -> TPagePaper.IsoId3
            "asia-f4" -> TPagePaper.AsiaF4
            "jp-shiroku-ban-4" -> TPagePaper.JpShirokuBan4
            "jp-shiroku-ban-5" -> TPagePaper.JpShirokuBan5
            "jp-shiroku-ban-6" -> TPagePaper.JpShirokuBan6
            "jp-kiku-4" -> TPagePaper.JpKiku4
            "jp-kiku-5" -> TPagePaper.JpKiku5
            "jp-business-card" -> TPagePaper.JpBusinessCard
            "cn-business-card" -> TPagePaper.CnBusinessCard
            "eu-business-card" -> TPagePaper.EuBusinessCard
            "fr-tellière" -> TPagePaper.FrTellière
            "fr-couronne-écriture" -> TPagePaper.FrCouronneÉcriture
            "fr-couronne-édition" -> TPagePaper.FrCouronneÉdition
            "fr-raisin" -> TPagePaper.FrRaisin
            "fr-carré" -> TPagePaper.FrCarré
            "fr-jésus" -> TPagePaper.FrJésus
            "uk-brief" -> TPagePaper.UkBrief
            "uk-draft" -> TPagePaper.UkDraft
            "uk-foolscap" -> TPagePaper.UkFoolscap
            "uk-quarto" -> TPagePaper.UkQuarto
            "uk-crown" -> TPagePaper.UkCrown
            "uk-book-a" -> TPagePaper.UkBookA
            "uk-book-b" -> TPagePaper.UkBookB
            "us-letter" -> TPagePaper.UsLetter
            "us-legal" -> TPagePaper.UsLegal
            "us-tabloid" -> TPagePaper.UsTabloid
            "us-executive" -> TPagePaper.UsExecutive
            "us-foolscap-folio" -> TPagePaper.UsFoolscapFolio
            "us-statement" -> TPagePaper.UsStatement
            "us-ledger" -> TPagePaper.UsLedger
            "us-oficio" -> TPagePaper.UsOficio
            "us-gov-letter" -> TPagePaper.UsGovLetter
            "us-gov-legal" -> TPagePaper.UsGovLegal
            "us-business-card" -> TPagePaper.UsBusinessCard
            "us-digest" -> TPagePaper.UsDigest
            "us-trade" -> TPagePaper.UsTrade
            "newspaper-compact" -> TPagePaper.NewspaperCompact
            "newspaper-berliner" -> TPagePaper.NewspaperBerliner
            "newspaper-broadsheet" -> TPagePaper.NewspaperBroadsheet
            "presentation-16-9" -> TPagePaper.Presentation16_9
            "presentation-4-3" -> TPagePaper.Presentation4_3
            else -> null
        }
    }
}


