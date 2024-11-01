package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TCiteStyle(override val value: String) : TStr, 
    TAutoOrCiteStyle {
    Alphanumeric("alphanumeric"),
    AmericanAnthropologicalAssociation("american-anthropological-association"),
    AmericanChemicalSociety("american-chemical-society"),
    AmericanGeophysicalUnion("american-geophysical-union"),
    AmericanInstituteOfAeronauticsAndAstronautics("american-institute-of-aeronautics-and-astronautics"),
    AmericanInstituteOfPhysics("american-institute-of-physics"),
    AmericanMedicalAssociation("american-medical-association"),
    AmericanMeteorologicalSociety("american-meteorological-society"),
    AmericanPhysicsSociety("american-physics-society"),
    AmericanPhysiologicalSociety("american-physiological-society"),
    AmericanPoliticalScienceAssociation("american-political-science-association"),
    AmericanPsychologicalAssociation("american-psychological-association"),
    AmericanSocietyForMicrobiology("american-society-for-microbiology"),
    AmericanSocietyOfCivilEngineers("american-society-of-civil-engineers"),
    AmericanSocietyOfMechanicalEngineers("american-society-of-mechanical-engineers"),
    AmericanSociologicalAssociation("american-sociological-association"),
    AngewandteChemie("angewandte-chemie"),
    AnnualReviews("annual-reviews"),
    AnnualReviewsAuthorDate("annual-reviews-author-date"),
    AssociacaoBrasileiraDeNormasTecnicas("associacao-brasileira-de-normas-tecnicas"),
    AssociationForComputingMachinery("association-for-computing-machinery"),
    BiomedCentral("biomed-central"),
    BristolUniversityPress("bristol-university-press"),
    BritishMedicalJournal("british-medical-journal"),
    Cell("cell"),
    ChicagoAuthorDate("chicago-author-date"),
    ChicagoFullnotes("chicago-fullnotes"),
    ChicagoNotes("chicago-notes"),
    Copernicus("copernicus"),
    CouncilOfScienceEditors("council-of-science-editors"),
    CouncilOfScienceEditorsAuthorDate("council-of-science-editors-author-date"),
    CurrentOpinion("current-opinion"),
    DeutscheGesellschaftFürPsychologie("deutsche-gesellschaft-für-psychologie"),
    DeutscheSprache("deutsche-sprache"),
    ElsevierHarvard("elsevier-harvard"),
    ElsevierVancouver("elsevier-vancouver"),
    ElsevierWithTitles("elsevier-with-titles"),
    Frontiers("frontiers"),
    FutureMedicine("future-medicine"),
    FutureScience("future-science"),
    Gb7714_2005Numeric("gb-7714-2005-numeric"),
    Gb7714_2015AuthorDate("gb-7714-2015-author-date"),
    Gb7714_2015Note("gb-7714-2015-note"),
    Gb7714_2015Numeric("gb-7714-2015-numeric"),
    GostR705_2008Numeric("gost-r-705-2008-numeric"),
    HarvardCiteThemRight("harvard-cite-them-right"),
    InstituteOfElectricalAndElectronicsEngineers("institute-of-electrical-and-electronics-engineers"),
    InstituteOfPhysicsNumeric("institute-of-physics-numeric"),
    Iso690AuthorDate("iso-690-author-date"),
    Iso690Numeric("iso-690-numeric"),
    Karger("karger"),
    MaryAnnLiebertVancouver("mary-ann-liebert-vancouver"),
    ModernHumanitiesResearchAssociation("modern-humanities-research-association"),
    ModernLanguageAssociation("modern-language-association"),
    ModernLanguageAssociation8("modern-language-association-8"),
    MultidisciplinaryDigitalPublishingInstitute("multidisciplinary-digital-publishing-institute"),
    Nature("nature"),
    Pensoft("pensoft"),
    PublicLibraryOfScience("public-library-of-science"),
    RoyalSocietyOfChemistry("royal-society-of-chemistry"),
    SageVancouver("sage-vancouver"),
    Sist02("sist02"),
    Spie("spie"),
    SpringerBasic("springer-basic"),
    SpringerBasicAuthorDate("springer-basic-author-date"),
    SpringerFachzeitschriftenMedizinPsychologie("springer-fachzeitschriften-medizin-psychologie"),
    SpringerHumanitiesAuthorDate("springer-humanities-author-date"),
    SpringerLectureNotesInComputerScience("springer-lecture-notes-in-computer-science"),
    SpringerMathphys("springer-mathphys"),
    SpringerSocpsychAuthorDate("springer-socpsych-author-date"),
    SpringerVancouver("springer-vancouver"),
    TaylorAndFrancisChicagoAuthorDate("taylor-and-francis-chicago-author-date"),
    TaylorAndFrancisNationalLibraryOfMedicine("taylor-and-francis-national-library-of-medicine"),
    TheInstitutionOfEngineeringAndTechnology("the-institution-of-engineering-and-technology"),
    TheLancet("the-lancet"),
    Thieme("thieme"),
    Trends("trends"),
    TurabianAuthorDate("turabian-author-date"),
    TurabianFullnote8("turabian-fullnote-8"),
    Vancouver("vancouver"),
    VancouverSuperscript("vancouver-superscript"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TCiteStyle? = when(str) {
                    "alphanumeric" -> TCiteStyle.Alphanumeric
            "american-anthropological-association" -> TCiteStyle.AmericanAnthropologicalAssociation
            "american-chemical-society" -> TCiteStyle.AmericanChemicalSociety
            "american-geophysical-union" -> TCiteStyle.AmericanGeophysicalUnion
            "american-institute-of-aeronautics-and-astronautics" -> TCiteStyle.AmericanInstituteOfAeronauticsAndAstronautics
            "american-institute-of-physics" -> TCiteStyle.AmericanInstituteOfPhysics
            "american-medical-association" -> TCiteStyle.AmericanMedicalAssociation
            "american-meteorological-society" -> TCiteStyle.AmericanMeteorologicalSociety
            "american-physics-society" -> TCiteStyle.AmericanPhysicsSociety
            "american-physiological-society" -> TCiteStyle.AmericanPhysiologicalSociety
            "american-political-science-association" -> TCiteStyle.AmericanPoliticalScienceAssociation
            "american-psychological-association" -> TCiteStyle.AmericanPsychologicalAssociation
            "american-society-for-microbiology" -> TCiteStyle.AmericanSocietyForMicrobiology
            "american-society-of-civil-engineers" -> TCiteStyle.AmericanSocietyOfCivilEngineers
            "american-society-of-mechanical-engineers" -> TCiteStyle.AmericanSocietyOfMechanicalEngineers
            "american-sociological-association" -> TCiteStyle.AmericanSociologicalAssociation
            "angewandte-chemie" -> TCiteStyle.AngewandteChemie
            "annual-reviews" -> TCiteStyle.AnnualReviews
            "annual-reviews-author-date" -> TCiteStyle.AnnualReviewsAuthorDate
            "associacao-brasileira-de-normas-tecnicas" -> TCiteStyle.AssociacaoBrasileiraDeNormasTecnicas
            "association-for-computing-machinery" -> TCiteStyle.AssociationForComputingMachinery
            "biomed-central" -> TCiteStyle.BiomedCentral
            "bristol-university-press" -> TCiteStyle.BristolUniversityPress
            "british-medical-journal" -> TCiteStyle.BritishMedicalJournal
            "cell" -> TCiteStyle.Cell
            "chicago-author-date" -> TCiteStyle.ChicagoAuthorDate
            "chicago-fullnotes" -> TCiteStyle.ChicagoFullnotes
            "chicago-notes" -> TCiteStyle.ChicagoNotes
            "copernicus" -> TCiteStyle.Copernicus
            "council-of-science-editors" -> TCiteStyle.CouncilOfScienceEditors
            "council-of-science-editors-author-date" -> TCiteStyle.CouncilOfScienceEditorsAuthorDate
            "current-opinion" -> TCiteStyle.CurrentOpinion
            "deutsche-gesellschaft-für-psychologie" -> TCiteStyle.DeutscheGesellschaftFürPsychologie
            "deutsche-sprache" -> TCiteStyle.DeutscheSprache
            "elsevier-harvard" -> TCiteStyle.ElsevierHarvard
            "elsevier-vancouver" -> TCiteStyle.ElsevierVancouver
            "elsevier-with-titles" -> TCiteStyle.ElsevierWithTitles
            "frontiers" -> TCiteStyle.Frontiers
            "future-medicine" -> TCiteStyle.FutureMedicine
            "future-science" -> TCiteStyle.FutureScience
            "gb-7714-2005-numeric" -> TCiteStyle.Gb7714_2005Numeric
            "gb-7714-2015-author-date" -> TCiteStyle.Gb7714_2015AuthorDate
            "gb-7714-2015-note" -> TCiteStyle.Gb7714_2015Note
            "gb-7714-2015-numeric" -> TCiteStyle.Gb7714_2015Numeric
            "gost-r-705-2008-numeric" -> TCiteStyle.GostR705_2008Numeric
            "harvard-cite-them-right" -> TCiteStyle.HarvardCiteThemRight
            "institute-of-electrical-and-electronics-engineers" -> TCiteStyle.InstituteOfElectricalAndElectronicsEngineers
            "institute-of-physics-numeric" -> TCiteStyle.InstituteOfPhysicsNumeric
            "iso-690-author-date" -> TCiteStyle.Iso690AuthorDate
            "iso-690-numeric" -> TCiteStyle.Iso690Numeric
            "karger" -> TCiteStyle.Karger
            "mary-ann-liebert-vancouver" -> TCiteStyle.MaryAnnLiebertVancouver
            "modern-humanities-research-association" -> TCiteStyle.ModernHumanitiesResearchAssociation
            "modern-language-association" -> TCiteStyle.ModernLanguageAssociation
            "modern-language-association-8" -> TCiteStyle.ModernLanguageAssociation8
            "multidisciplinary-digital-publishing-institute" -> TCiteStyle.MultidisciplinaryDigitalPublishingInstitute
            "nature" -> TCiteStyle.Nature
            "pensoft" -> TCiteStyle.Pensoft
            "public-library-of-science" -> TCiteStyle.PublicLibraryOfScience
            "royal-society-of-chemistry" -> TCiteStyle.RoyalSocietyOfChemistry
            "sage-vancouver" -> TCiteStyle.SageVancouver
            "sist02" -> TCiteStyle.Sist02
            "spie" -> TCiteStyle.Spie
            "springer-basic" -> TCiteStyle.SpringerBasic
            "springer-basic-author-date" -> TCiteStyle.SpringerBasicAuthorDate
            "springer-fachzeitschriften-medizin-psychologie" -> TCiteStyle.SpringerFachzeitschriftenMedizinPsychologie
            "springer-humanities-author-date" -> TCiteStyle.SpringerHumanitiesAuthorDate
            "springer-lecture-notes-in-computer-science" -> TCiteStyle.SpringerLectureNotesInComputerScience
            "springer-mathphys" -> TCiteStyle.SpringerMathphys
            "springer-socpsych-author-date" -> TCiteStyle.SpringerSocpsychAuthorDate
            "springer-vancouver" -> TCiteStyle.SpringerVancouver
            "taylor-and-francis-chicago-author-date" -> TCiteStyle.TaylorAndFrancisChicagoAuthorDate
            "taylor-and-francis-national-library-of-medicine" -> TCiteStyle.TaylorAndFrancisNationalLibraryOfMedicine
            "the-institution-of-engineering-and-technology" -> TCiteStyle.TheInstitutionOfEngineeringAndTechnology
            "the-lancet" -> TCiteStyle.TheLancet
            "thieme" -> TCiteStyle.Thieme
            "trends" -> TCiteStyle.Trends
            "turabian-author-date" -> TCiteStyle.TurabianAuthorDate
            "turabian-fullnote-8" -> TCiteStyle.TurabianFullnote8
            "vancouver" -> TCiteStyle.Vancouver
            "vancouver-superscript" -> TCiteStyle.VancouverSuperscript
            else -> null
        }
    }
}


