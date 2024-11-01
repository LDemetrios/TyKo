package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TBibliographyStyle(override val value: String) : TStr {
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
        
        fun of(str: String) : TBibliographyStyle? = when(str) {
                    "alphanumeric" -> TBibliographyStyle.Alphanumeric
            "american-anthropological-association" -> TBibliographyStyle.AmericanAnthropologicalAssociation
            "american-chemical-society" -> TBibliographyStyle.AmericanChemicalSociety
            "american-geophysical-union" -> TBibliographyStyle.AmericanGeophysicalUnion
            "american-institute-of-aeronautics-and-astronautics" -> TBibliographyStyle.AmericanInstituteOfAeronauticsAndAstronautics
            "american-institute-of-physics" -> TBibliographyStyle.AmericanInstituteOfPhysics
            "american-medical-association" -> TBibliographyStyle.AmericanMedicalAssociation
            "american-meteorological-society" -> TBibliographyStyle.AmericanMeteorologicalSociety
            "american-physics-society" -> TBibliographyStyle.AmericanPhysicsSociety
            "american-physiological-society" -> TBibliographyStyle.AmericanPhysiologicalSociety
            "american-political-science-association" -> TBibliographyStyle.AmericanPoliticalScienceAssociation
            "american-psychological-association" -> TBibliographyStyle.AmericanPsychologicalAssociation
            "american-society-for-microbiology" -> TBibliographyStyle.AmericanSocietyForMicrobiology
            "american-society-of-civil-engineers" -> TBibliographyStyle.AmericanSocietyOfCivilEngineers
            "american-society-of-mechanical-engineers" -> TBibliographyStyle.AmericanSocietyOfMechanicalEngineers
            "american-sociological-association" -> TBibliographyStyle.AmericanSociologicalAssociation
            "angewandte-chemie" -> TBibliographyStyle.AngewandteChemie
            "annual-reviews" -> TBibliographyStyle.AnnualReviews
            "annual-reviews-author-date" -> TBibliographyStyle.AnnualReviewsAuthorDate
            "associacao-brasileira-de-normas-tecnicas" -> TBibliographyStyle.AssociacaoBrasileiraDeNormasTecnicas
            "association-for-computing-machinery" -> TBibliographyStyle.AssociationForComputingMachinery
            "biomed-central" -> TBibliographyStyle.BiomedCentral
            "bristol-university-press" -> TBibliographyStyle.BristolUniversityPress
            "british-medical-journal" -> TBibliographyStyle.BritishMedicalJournal
            "cell" -> TBibliographyStyle.Cell
            "chicago-author-date" -> TBibliographyStyle.ChicagoAuthorDate
            "chicago-fullnotes" -> TBibliographyStyle.ChicagoFullnotes
            "chicago-notes" -> TBibliographyStyle.ChicagoNotes
            "copernicus" -> TBibliographyStyle.Copernicus
            "council-of-science-editors" -> TBibliographyStyle.CouncilOfScienceEditors
            "council-of-science-editors-author-date" -> TBibliographyStyle.CouncilOfScienceEditorsAuthorDate
            "current-opinion" -> TBibliographyStyle.CurrentOpinion
            "deutsche-gesellschaft-für-psychologie" -> TBibliographyStyle.DeutscheGesellschaftFürPsychologie
            "deutsche-sprache" -> TBibliographyStyle.DeutscheSprache
            "elsevier-harvard" -> TBibliographyStyle.ElsevierHarvard
            "elsevier-vancouver" -> TBibliographyStyle.ElsevierVancouver
            "elsevier-with-titles" -> TBibliographyStyle.ElsevierWithTitles
            "frontiers" -> TBibliographyStyle.Frontiers
            "future-medicine" -> TBibliographyStyle.FutureMedicine
            "future-science" -> TBibliographyStyle.FutureScience
            "gb-7714-2005-numeric" -> TBibliographyStyle.Gb7714_2005Numeric
            "gb-7714-2015-author-date" -> TBibliographyStyle.Gb7714_2015AuthorDate
            "gb-7714-2015-note" -> TBibliographyStyle.Gb7714_2015Note
            "gb-7714-2015-numeric" -> TBibliographyStyle.Gb7714_2015Numeric
            "gost-r-705-2008-numeric" -> TBibliographyStyle.GostR705_2008Numeric
            "harvard-cite-them-right" -> TBibliographyStyle.HarvardCiteThemRight
            "institute-of-electrical-and-electronics-engineers" -> TBibliographyStyle.InstituteOfElectricalAndElectronicsEngineers
            "institute-of-physics-numeric" -> TBibliographyStyle.InstituteOfPhysicsNumeric
            "iso-690-author-date" -> TBibliographyStyle.Iso690AuthorDate
            "iso-690-numeric" -> TBibliographyStyle.Iso690Numeric
            "karger" -> TBibliographyStyle.Karger
            "mary-ann-liebert-vancouver" -> TBibliographyStyle.MaryAnnLiebertVancouver
            "modern-humanities-research-association" -> TBibliographyStyle.ModernHumanitiesResearchAssociation
            "modern-language-association" -> TBibliographyStyle.ModernLanguageAssociation
            "modern-language-association-8" -> TBibliographyStyle.ModernLanguageAssociation8
            "multidisciplinary-digital-publishing-institute" -> TBibliographyStyle.MultidisciplinaryDigitalPublishingInstitute
            "nature" -> TBibliographyStyle.Nature
            "pensoft" -> TBibliographyStyle.Pensoft
            "public-library-of-science" -> TBibliographyStyle.PublicLibraryOfScience
            "royal-society-of-chemistry" -> TBibliographyStyle.RoyalSocietyOfChemistry
            "sage-vancouver" -> TBibliographyStyle.SageVancouver
            "sist02" -> TBibliographyStyle.Sist02
            "spie" -> TBibliographyStyle.Spie
            "springer-basic" -> TBibliographyStyle.SpringerBasic
            "springer-basic-author-date" -> TBibliographyStyle.SpringerBasicAuthorDate
            "springer-fachzeitschriften-medizin-psychologie" -> TBibliographyStyle.SpringerFachzeitschriftenMedizinPsychologie
            "springer-humanities-author-date" -> TBibliographyStyle.SpringerHumanitiesAuthorDate
            "springer-lecture-notes-in-computer-science" -> TBibliographyStyle.SpringerLectureNotesInComputerScience
            "springer-mathphys" -> TBibliographyStyle.SpringerMathphys
            "springer-socpsych-author-date" -> TBibliographyStyle.SpringerSocpsychAuthorDate
            "springer-vancouver" -> TBibliographyStyle.SpringerVancouver
            "taylor-and-francis-chicago-author-date" -> TBibliographyStyle.TaylorAndFrancisChicagoAuthorDate
            "taylor-and-francis-national-library-of-medicine" -> TBibliographyStyle.TaylorAndFrancisNationalLibraryOfMedicine
            "the-institution-of-engineering-and-technology" -> TBibliographyStyle.TheInstitutionOfEngineeringAndTechnology
            "the-lancet" -> TBibliographyStyle.TheLancet
            "thieme" -> TBibliographyStyle.Thieme
            "trends" -> TBibliographyStyle.Trends
            "turabian-author-date" -> TBibliographyStyle.TurabianAuthorDate
            "turabian-fullnote-8" -> TBibliographyStyle.TurabianFullnote8
            "vancouver" -> TBibliographyStyle.Vancouver
            "vancouver-superscript" -> TBibliographyStyle.VancouverSuperscript
            else -> null
        }
    }
}


