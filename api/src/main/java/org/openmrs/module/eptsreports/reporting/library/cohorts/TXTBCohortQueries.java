package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Arrays;
import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.metadata.CommonMetadata;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.metadata.TbMetadata;
import org.openmrs.module.eptsreports.reporting.calculation.txtb.TxTBPatientsWhoAreTransferedOutCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.TXTBQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.BaseObsCohortDefinition.TimeModifier;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TXTBCohortQueries {

  @Autowired private TbMetadata tbMetadata;

  @Autowired private HivMetadata hivMetadata;

  @Autowired private CommonMetadata commonMetadata;

  @Autowired private GenericCohortQueries genericCohortQueries;

  private final String generalParameterMapping =
      "startDate=${startDate},endDate=${endDate},location=${location}";

  private final String codedObsParameterMapping =
      "onOrAfter=${startDate},onOrBefore=${endDate},locationList=${location}";

  private Mapped<CohortDefinition> map(final CohortDefinition cd, final String parameterMappings) {
    return EptsReportUtils.map(
        cd,
        EptsReportUtils.removeMissingParameterMappingsFromCohortDefintion(cd, parameterMappings));
  }

  private void addGeneralParameters(final CohortDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }

  /**
   * INICIO DE TRATAMENTO DE TUBERCULOSE DATA NOTIFICADA NAS FICHAS DE: SEGUIMENTO, RASTREIO E LIVRO
   * TB. codes: DATAINICIO
   */
  public CohortDefinition getTbDrugTreatmentStartDateWithinReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "startedTbTreatment",
            TXTBQueries.dateObs(
                this.tbMetadata.getTBDrugTreatmentStartDate().getConceptId(),
                Arrays.asList(
                    this.hivMetadata.getAdultoSeguimentoEncounterType().getId(),
                    this.hivMetadata.getARVPediatriaSeguimentoEncounterType().getId()),
                true));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getPulmonaryTBWithinReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "pulmonaryTBeWithinReportingDate",
            TXTBQueries.dateObsByObsDateTimeClausule(
                this.tbMetadata.getPulmonaryTB().getConceptId(),
                this.hivMetadata.getYesConcept().getConceptId(),
                this.hivMetadata.getMasterCardEncounterType().getEncounterTypeId()));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTuberculosisSymptoms(Integer... answerIds) {

    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tuberculosisSymptoms",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getHasTbSymptomsConcept().getConceptId()),
                Arrays.asList(answerIds)));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getActiveTuberculosis() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "activeTuberculosis",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getActiveTBConcept().getConceptId()),
                Arrays.asList(this.hivMetadata.getYesConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTbObservations() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbObservations",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbObservations().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getFeverLastingMoreThan3Weeks().getConceptId(),
                    this.tbMetadata.getWeightLossOfMoreThan3KgInLastMonth().getConceptId(),
                    this.tbMetadata.getNightsweatsLastingMoreThan3Weeks().getConceptId(),
                    this.tbMetadata.getCoughLastingMoreThan3Weeks().getConceptId(),
                    this.tbMetadata.getAsthenia().getConceptId(),
                    this.tbMetadata.getCohabitantBeingTreatedForTB().getConceptId(),
                    this.tbMetadata.getLymphadenopathy().getConceptId())));
    this.addGeneralParameters(definition);

    return definition;
  }

  private CohortDefinition getApplicationForLaboratoryResearch() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "applicationForLaboratoryResearch",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId(),
                    // ID of X-Ray Concept
                    12)));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getApplicationForLaboratoryResearchInSpecimentSentRequirements() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "applicationForLaboratoryResearch",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTbGenExpertORCultureTestOrTbLam() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbGenExpertORCultureTestOrTbLam",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId(),
                    // X-Ray concept id
                    12),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId(),
                    // Indeterminate result
                    1138)));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTbGenExpertORCultureTestOrTbLamForSpecimenSentRequirements() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbGenExpertORCultureTestOrTbLam",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTbGenExpertORCultureOrTbLamOrBKLabFormForSpecimenSentResults() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbGenExpertORCultureTestOrTbLam",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId(),
                    // A substituir pelo ID do xPERTMTB(presenca de MTB detectada) a ser criado no
                    // metadata
                    24063),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId(),
                    // A substituir pelo conceptID do conceito NAO ENCONTRADO a ser criado no
                    // metadata
                    24058)));
    this.addGeneralParameters(definition);
    return definition;
  }

  private CohortDefinition getTuberculosisTreatmentPlanWithinReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tuberculosisTreatmentPlanWithinReportingDate",
            TXTBQueries.dateObsByObsDateTimeClausule(
                this.tbMetadata.getTBTreatmentPlanConcept().getConceptId(),
                this.hivMetadata.getStartDrugsConcept().getConceptId(),
                this.hivMetadata.getAdultoSeguimentoEncounterType().getId()));
    this.addGeneralParameters(definition);
    return definition;
  }

  /** PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA DE TUBERCULOSE - NUM PERIODO */
  public CohortDefinition getInTBProgram() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "TBPROGRAMA",
            TXTBQueries.inTBProgramWithinReportingPeriodAtLocation(
                this.tbMetadata.getTBProgram().getProgramId()));
    this.addGeneralParameters(definition);
    return definition;
  }

  /** PACIENTES COM RASTREIO DE TUBERCULOSE NEGATIVO codes: RASTREIOTBNEG */
  public CohortDefinition codedNoTbScreening() {
    final CohortDefinition cd =
        this.genericCohortQueries.hasCodedObs(
            this.tbMetadata.getTbScreeningConcept(),
            TimeModifier.ANY,
            SetComparator.IN,
            Arrays.asList(
                this.hivMetadata.getAdultoSeguimentoEncounterType(),
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType(),
                this.hivMetadata.getMisauLaboratorioEncounterType()),
            Arrays.asList(this.commonMetadata.getNoConcept()));
    this.addGeneralParameters(cd);
    return cd;
  }

  /** PACIENTES COM RASTREIO DE TUBERCULOSE POSITIVO codes: RASTREIOTBPOS */
  public CohortDefinition codedYesTbScreening() {
    final CohortDefinition cd =
        this.genericCohortQueries.hasCodedObs(
            this.tbMetadata.getTbScreeningConcept(),
            TimeModifier.ANY,
            SetComparator.IN,
            Arrays.asList(
                this.hivMetadata.getAdultoSeguimentoEncounterType(),
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType(),
                this.hivMetadata.getMisauLaboratorioEncounterType()),
            Arrays.asList(this.commonMetadata.getYesConcept()));
    this.addGeneralParameters(cd);
    return cd;
  }

  public CohortDefinition artList() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - artList");
    cd.addSearch(
        "started-by-end-reporting-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtBeforeDate(false),
            "onOrBefore=${endDate},location=${location}"));

    cd.setCompositionString("started-by-end-reporting-period");
    this.addGeneralParameters(cd);
    return cd;
  }

  public CohortDefinition positiveInvestigationResult() {
    final CohortDefinition cd =
        this.genericCohortQueries.hasCodedObs(
            this.tbMetadata.getResearchResultConcept(),
            TimeModifier.ANY,
            SetComparator.IN,
            Arrays.asList(
                this.hivMetadata.getAdultoSeguimentoEncounterType(),
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType(),
                this.hivMetadata.getMisauLaboratorioEncounterType()),
            Arrays.asList(this.tbMetadata.getPositiveConcept()));
    this.addGeneralParameters(cd);
    return cd;
  }

  public CohortDefinition negativeInvestigationResult() {
    final CohortDefinition cd =
        this.genericCohortQueries.hasCodedObs(
            this.tbMetadata.getResearchResultConcept(),
            TimeModifier.ANY,
            SetComparator.IN,
            Arrays.asList(
                this.hivMetadata.getAdultoSeguimentoEncounterType(),
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType(),
                this.hivMetadata.getMisauLaboratorioEncounterType()),
            Arrays.asList(this.tbMetadata.getNegativeConcept()));
    this.addGeneralParameters(cd);
    return cd;
  }

  /**
   * at least one “POS” or “NEG” selected for “Resultado da Investigação para TB de BK e/ou RX?”
   * during the reporting period consultations; ( response 703: POS or 664: NEG for question: 6277)
   */
  @DocumentedDefinition(value = "positiveInvestigationResultComposition")
  public CohortDefinition positiveInvestigationResultComposition() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - positiveInvestigationResultComposition");
    final CohortDefinition P = this.positiveInvestigationResult();
    cd.addSearch("P", this.map(P, this.codedObsParameterMapping));
    cd.setCompositionString("P");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "findNegativeInvestigationResultAndAnyResultForTBScreening")
  public CohortDefinition negativeInvestigationResultAndAnyResultForTBScreeningComposition() {
    CohortDefinition cohortDefinition =
        this.genericCohortQueries.generalSql(
            "findNegativeInvestigationResultAndAnyResultForTBScreening",
            TXTBQueries.findNegativeInvestigationResultAndAnyResultForTBScreening(
                this.hivMetadata.getAdultoSeguimentoEncounterType(),
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType(),
                this.tbMetadata.getTbScreeningConcept(),
                this.commonMetadata.getYesConcept(),
                this.commonMetadata.getNoConcept(),
                tbMetadata.getResearchResultConcept(),
                this.tbMetadata.getNegativeConcept()));

    this.addGeneralParameters(cohortDefinition);
    return cohortDefinition;
  }

  /**
   * at least one “S” or “N” selected for TB Screening (Rastreio de TB) during the reporting period
   * consultations; (response 1065: YES or 1066: NO for question 6257: SCREENING FOR TB)
   */
  @DocumentedDefinition(value = "yesOrNoInvestigationResult")
  public CohortDefinition yesOrNoInvestigationResult() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - yesOrNoInvestigationResult");
    final CohortDefinition S = this.codedYesTbScreening();
    cd.addSearch("S", this.map(S, this.codedObsParameterMapping));
    final CohortDefinition N = this.codedNoTbScreening();
    cd.addSearch("N", this.map(N, this.codedObsParameterMapping));
    cd.setCompositionString("S OR N");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "txTbNumeratorA")
  public CohortDefinition txTbNumeratorA() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - txTbNumeratorA");
    final CohortDefinition i =
        this.genericCohortQueries.generalSql(
            "onTbTreatment",
            TXTBQueries.dateObs(
                this.tbMetadata.getTBDrugTreatmentStartDate().getConceptId(),
                Arrays.asList(
                    this.hivMetadata.getAdultoSeguimentoEncounterType().getId(),
                    this.hivMetadata.getARVPediatriaSeguimentoEncounterType().getId()),
                true));
    final CohortDefinition ii = this.getInTBProgram();
    this.addGeneralParameters(i);
    cd.addSearch("i", this.map(i, this.generalParameterMapping));
    cd.addSearch("ii", this.map(ii, this.generalParameterMapping));
    cd.addSearch(
        "iii", this.map(this.getPulmonaryTBWithinReportingDate(), this.generalParameterMapping));
    cd.addSearch(
        "iv",
        this.map(
            this.getTuberculosisTreatmentPlanWithinReportingDate(), this.generalParameterMapping));

    final CohortDefinition artList = this.artList();
    cd.addSearch("artList", this.map(artList, this.generalParameterMapping));
    cd.setCompositionString("(i OR ii OR iii OR iv) AND artList");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "txTbNumerator")
  public CohortDefinition txTbNumerator() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - txTbNumerator");
    final CohortDefinition A = this.txTbNumeratorA();
    cd.addSearch("A", this.map(A, this.generalParameterMapping));

    cd.addSearch(
        "started-tb-treatment-previous-period",
        EptsReportUtils.map(
            this.getTbDrugTreatmentStartDateWithinReportingDate(),
            "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));

    cd.setCompositionString("A NOT started-tb-treatment-previous-period");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "positiveScreening")
  public CohortDefinition positiveScreening() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - positiveScreening");

    cd.addSearch(
        "A", EptsReportUtils.map(this.codedYesTbScreening(), this.codedObsParameterMapping));
    cd.addSearch(
        "B",
        EptsReportUtils.map(
            this.positiveInvestigationResultComposition(), this.generalParameterMapping));
    cd.addSearch(
        "C",
        EptsReportUtils.map(
            this.negativeInvestigationResultAndAnyResultForTBScreeningComposition(),
            this.generalParameterMapping));
    cd.addSearch(
        "D",
        EptsReportUtils.map(
            this.getTbDrugTreatmentStartDateWithinReportingDate(), this.generalParameterMapping));
    cd.addSearch("E", EptsReportUtils.map(this.getInTBProgram(), this.generalParameterMapping));
    cd.addSearch(
        "F",
        EptsReportUtils.map(
            this.getPulmonaryTBWithinReportingDate(), this.generalParameterMapping));
    cd.addSearch(
        "G",
        EptsReportUtils.map(
            this.getTuberculosisTreatmentPlanWithinReportingDate(), this.generalParameterMapping));
    cd.addSearch(
        "H",
        this.map(
            this.getAllTBSymptomsForDisaggregationComposition(), this.generalParameterMapping));

    cd.setCompositionString("A OR B OR C OR D OR E OR F OR G OR H");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "newOnARTPositiveScreening")
  public CohortDefinition newOnARTPositiveScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - newOnARTPositiveScreening");
    definition.addSearch(
        "denominator", EptsReportUtils.map(this.getDenominator(), this.generalParameterMapping));
    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));
    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.positiveScreening(), this.generalParameterMapping));
    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator AND new-on-art AND positive-screening");
    return definition;
  }

  @DocumentedDefinition(value = "newOnARTNegativeScreening")
  public CohortDefinition newOnARTNegativeScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - newOnARTPositiveScreening");
    definition.addSearch(
        "denominator", EptsReportUtils.map(this.getDenominator(), this.generalParameterMapping));
    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));
    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.positiveScreening(), this.generalParameterMapping));
    this.addGeneralParameters(definition);
    definition.setCompositionString("(denominator AND new-on-art) NOT positive-screening");
    return definition;
  }

  @DocumentedDefinition(value = "previouslyOnARTPositiveScreening")
  public CohortDefinition previouslyOnARTPositiveScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - previouslyOnARTPositiveScreening");
    definition.addSearch(
        "denominator", EptsReportUtils.map(this.getDenominator(), this.generalParameterMapping));
    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));
    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.positiveScreening(), this.generalParameterMapping));
    this.addGeneralParameters(definition);
    definition.setCompositionString("(denominator AND positive-screening) NOT new-on-art");
    return definition;
  }

  @DocumentedDefinition(value = "previouslyOnARTNegativeScreening")
  public CohortDefinition previouslyOnARTNegativeScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - previouslyOnARTNegativeScreening");
    definition.addSearch(
        "denominator", EptsReportUtils.map(this.getDenominator(), this.generalParameterMapping));
    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));
    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.positiveScreening(), this.generalParameterMapping));
    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator NOT (new-on-art OR positive-screening)");
    return definition;
  }

  @DocumentedDefinition(value = "patientsNewOnARTNumerator")
  public CohortDefinition patientsNewOnARTNumerator() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - patientsNewOnARTNumerator");
    final CohortDefinition NUM = this.txTbNumerator();
    cd.addSearch("NUM", this.map(NUM, this.generalParameterMapping));
    cd.addSearch(
        "started-during-reporting-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtOnPeriod(false, true),
            "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    cd.setCompositionString("NUM AND started-during-reporting-period");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "patientsPreviouslyOnARTNumerator")
  public CohortDefinition patientsPreviouslyOnARTNumerator() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - patientsPreviouslyOnARTNumerator");
    final CohortDefinition NUM = this.txTbNumerator();
    cd.addSearch("NUM", this.map(NUM, this.generalParameterMapping));
    cd.addSearch(
        "started-before-start-reporting-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtBeforeDate(false),
            "onOrBefore=${startDate-1d},location=${location}"));
    cd.setCompositionString("NUM AND started-before-start-reporting-period");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "Denominator")
  public CohortDefinition getDenominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB - Denominator");
    definition.addSearch(
        "art-list",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtBeforeDate(false),
            "onOrBefore=${endDate},location=${location}"));
    definition.addSearch(
        "tb-screening",
        EptsReportUtils.map(this.yesOrNoInvestigationResult(), this.generalParameterMapping));
    definition.addSearch(
        "tb-investigation",
        EptsReportUtils.map(
            this.positiveInvestigationResultComposition(), this.generalParameterMapping));
    definition.addSearch(
        "started-tb-treatment",
        EptsReportUtils.map(
            this.getTbDrugTreatmentStartDateWithinReportingDate(), this.generalParameterMapping));
    definition.addSearch(
        "in-tb-program", EptsReportUtils.map(this.getInTBProgram(), this.generalParameterMapping));

    definition.addSearch(
        "started-tb-treatment-previous-period",
        EptsReportUtils.map(
            this.getTbDrugTreatmentStartDateWithinReportingDate(),
            "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));
    definition.addSearch(
        "in-tb-program-previous-period",
        EptsReportUtils.map(
            this.getInTBProgram(),
            "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));

    definition.addSearch(
        "transferred-out",
        EptsReportUtils.map(
            this.getPatientsWhoAreTransferredOut(),
            "startDate=${startDate},endDate=${endDate},location=${location}"));

    CohortDefinition fichaResumoMasterCard =
        this.genericCohortQueries.generalSql(
            "onFichaResumoMasterCard",
            TXTBQueries.dateObsByObsDateTimeClausule(
                this.tbMetadata.getPulmonaryTB().getConceptId(),
                this.hivMetadata.getYesConcept().getConceptId(),
                this.hivMetadata.getMasterCardEncounterType().getEncounterTypeId()));

    CohortDefinition fichaClinicaMasterCard =
        this.genericCohortQueries.generalSql(
            "fichaClinicaMasterCard",
            TXTBQueries.dateObsByObsValueDateTimeClausule(
                this.tbMetadata.getTBTreatmentPlanConcept().getConceptId(),
                this.hivMetadata.getStartDrugsConcept().getConceptId(),
                this.hivMetadata.getAdultoSeguimentoEncounterType().getId()));

    this.addGeneralParameters(fichaResumoMasterCard);
    this.addGeneralParameters(fichaClinicaMasterCard);
    definition.addSearch(
        "ficha-resumo-master-card", this.map(fichaResumoMasterCard, this.generalParameterMapping));
    definition.addSearch(
        "ficha-clinica-master-card",
        this.map(fichaClinicaMasterCard, this.generalParameterMapping));
    definition.addSearch(
        "all-tb-symptoms",
        this.map(this.getAllTBSymptomsForDemoninatorComposition(), this.generalParameterMapping));

    definition.setCompositionString(
        "(art-list AND "
            + " ( tb-screening OR tb-investigation OR started-tb-treatment OR in-tb-program OR ficha-resumo-master-card OR ficha-clinica-master-card OR all-tb-symptoms)) "
            + " NOT ((transferred-out NOT (started-tb-treatment OR in-tb-program)) OR started-tb-treatment-previous-period OR in-tb-program-previous-period)");

    return definition;
  }

  @DocumentedDefinition(value = "get New on Art")
  public CohortDefinition getNewOnArt() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - New on ART");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "started-on-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtOnPeriod(false, true),
            "onOrAfter=${startDate},onOrBefore=${endDate},location=${location}"));
    definition.setCompositionString("started-on-period");
    return definition;
  }

  @DocumentedDefinition(value = "get All TB Symptoms")
  private CohortDefinition getAllTBSymptomsForDisaggregationComposition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -All TB Symptoms");

    definition.addSearch(
        "tuberculosis-symptoms",
        this.map(
            this.getTuberculosisSymptoms(this.hivMetadata.getYesConcept().getConceptId()),
            this.generalParameterMapping));
    definition.addSearch(
        "active-tuberculosis",
        this.map(this.getActiveTuberculosis(), this.generalParameterMapping));
    definition.addSearch(
        "tb-observations", this.map(this.getTbObservations(), this.generalParameterMapping));
    definition.addSearch(
        "application-for-laboratory-research",
        this.map(this.getApplicationForLaboratoryResearch(), this.generalParameterMapping));
    definition.addSearch(
        "tb-genexpert-or-culture-test-or-lam-test",
        this.map(this.getTbGenExpertORCultureTestOrTbLam(), this.generalParameterMapping));
    definition.addSearch(
        "labform-test-result",
        this.map(
            this.getTbGenExpertORCultureOrTbLamOrBKLabFormForSpecimenSentResults(),
            this.generalParameterMapping));

    definition.setCompositionString(
        "tuberculosis-symptoms OR active-tuberculosis OR tb-observations OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-test OR labform-test-result");

    return definition;
  }

  @DocumentedDefinition(value = "get All TB Symptoms for Denominator")
  private CohortDefinition getAllTBSymptomsForDemoninatorComposition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB - All TB Symptoms for Denominator");

    definition.addSearch(
        "tuberculosis-symptoms",
        this.map(
            this.getTuberculosisSymptoms(
                this.hivMetadata.getYesConcept().getConceptId(),
                this.hivMetadata.getNoConcept().getConceptId()),
            this.generalParameterMapping));

    definition.addSearch(
        "active-tuberculosis",
        this.map(this.getActiveTuberculosis(), this.generalParameterMapping));

    definition.addSearch(
        "tb-observations", this.map(this.getTbObservations(), this.generalParameterMapping));

    definition.addSearch(
        "application-for-laboratory-research",
        this.map(this.getApplicationForLaboratoryResearch(), this.generalParameterMapping));

    definition.addSearch(
        "tb-genexpert-or-culture-test-or-lam-test",
        this.map(this.getTbGenExpertORCultureTestOrTbLam(), this.generalParameterMapping));

    definition.addSearch(
        "labform-test-result",
        this.map(
            this.getTbGenExpertORCultureOrTbLamOrBKLabFormForSpecimenSentResults(),
            this.generalParameterMapping));

    definition.setCompositionString(
        "tuberculosis-symptoms OR active-tuberculosis OR tb-observations OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-test OR labform-test-result");

    return definition;
  }

  @DocumentedDefinition(value = "get Specimen Sent")
  public CohortDefinition getSpecimenSentCohortDefinition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -specimen-sent");

    definition.addSearch(
        "labform-result-tests",
        EptsReportUtils.map(
            this.getTbGenExpertORCultureOrTbLamOrBKLabFormForSpecimenSentResults(),
            this.generalParameterMapping));
    definition.addSearch(
        "application-for-laboratory-research",
        this.map(
            this.getApplicationForLaboratoryResearchInSpecimentSentRequirements(),
            this.generalParameterMapping));
    definition.addSearch(
        "tb-genexpert-or-culture-test-or-lam-test",
        this.map(
            this.getTbGenExpertORCultureTestOrTbLamForSpecimenSentRequirements(),
            this.generalParameterMapping));

    definition.setCompositionString(
        "labform-result-tests OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-test");

    return definition;
  }

  @DocumentedDefinition(value = "get GeneXpert MTB Diagnostic Test")
  public CohortDefinition getGeneXpertMTBDiagnosticTestCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - GeneXpert MTB Diagnostic Test");
    this.addGeneralParameters(cd);

    final CohortDefinition applicationForLabResearch =
        this.genericCohortQueries.generalSql(
            "applicationForLabResearch",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId())));

    final CohortDefinition geneExpertTest =
        this.genericCohortQueries.generalSql(
            "geneExpertTest",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition geneExpertResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "geneExpertTest",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition xPertMTBResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "xPertMTBResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                // Este conceito deve ser substituido pelo conceito da presenca de MTB detectada
                // a ser criado no metadata
                Arrays.asList(24063),
                Arrays.asList(
                    this.tbMetadata.getYesConcept().getConceptId(),
                    this.tbMetadata.getNoConcept().getConceptId())));

    this.addGeneralParameters(applicationForLabResearch);
    this.addGeneralParameters(geneExpertTest);
    this.addGeneralParameters(geneExpertResultLaboratoryForm);
    this.addGeneralParameters(xPertMTBResultLaboratoryForm);
    cd.addSearch(
        "application-for-lab-research",
        this.map(applicationForLabResearch, this.generalParameterMapping));
    cd.addSearch("gen-expert-test", this.map(geneExpertTest, this.generalParameterMapping));
    cd.addSearch(
        "geneexpert-laboratoryform-result",
        this.map(geneExpertResultLaboratoryForm, this.generalParameterMapping));
    cd.addSearch(
        "xpertMTB-laboratoryform-result",
        this.map(xPertMTBResultLaboratoryForm, this.generalParameterMapping));

    cd.setCompositionString(
        "application-for-lab-research OR gen-expert-test OR geneexpert-laboratoryform-result OR xpertMTB-laboratoryform-result");

    return cd;
  }

  @DocumentedDefinition(value = "get Smear Microscopy Diagnostic Test")
  public CohortDefinition getSmearMicroscopyOnlyDiagnosticTestCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - Smear Microscopy Diagnostic Test");
    this.addGeneralParameters(cd);

    final CohortDefinition exameBasilosCopia =
        this.genericCohortQueries.generalSql(
            "exameBasilosCopia",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    // Substituir pelo ID do conceito NAO ENCONTRADO a ser criado no metadata
                    24058)));

    final CohortDefinition applicationForLabResearchForBK =
        this.genericCohortQueries.generalSql(
            "applicationForLabResearchForBK",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId())));

    final CohortDefinition baciloscopiaResultAnyResult =
        this.genericCohortQueries.generalSql(
            "baciloscopiaResultAnyResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    // A substituir pelo conceptID do NAO ENCONTRADO a ser criado no metadata
                    24058)));

    final CohortDefinition geneExpertResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "geneExpertResultLaboratoryForm",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition xpertMTBResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "xpertMTBResultLaboratoryForm",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                // A substituir pelo conceptid do XpertMTB(presenca de MTB detectada) a ser criado
                // no metadata
                Arrays.asList(24063),
                Arrays.asList(
                    this.tbMetadata.getYesConcept().getConceptId(),
                    this.tbMetadata.getNoConcept().getConceptId())));

    final CohortDefinition geneExpertResultFichaClinica =
        this.genericCohortQueries.generalSql(
            "geneExpertResultFichaClinica",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    this.addGeneralParameters(exameBasilosCopia);
    this.addGeneralParameters(applicationForLabResearchForBK);
    this.addGeneralParameters(baciloscopiaResultAnyResult);
    this.addGeneralParameters(geneExpertResultLaboratoryForm);
    this.addGeneralParameters(xpertMTBResultLaboratoryForm);
    this.addGeneralParameters(geneExpertResultFichaClinica);

    cd.addSearch("exame-basiloscopia", this.map(exameBasilosCopia, this.generalParameterMapping));
    cd.addSearch(
        "application-forlabresearch-forbk",
        this.map(applicationForLabResearchForBK, this.generalParameterMapping));
    cd.addSearch(
        "baciloscopia-result", this.map(baciloscopiaResultAnyResult, this.generalParameterMapping));
    cd.addSearch(
        "gene-expertresult-labform",
        this.map(geneExpertResultLaboratoryForm, this.generalParameterMapping));
    cd.addSearch(
        "xpert-result-labform",
        this.map(xpertMTBResultLaboratoryForm, this.generalParameterMapping));
    cd.addSearch(
        "geneexpert-result-ficha-clinica",
        this.map(geneExpertResultFichaClinica, this.generalParameterMapping));

    cd.setCompositionString(
        "(exame-basiloscopia OR application-forlabresearch-forbk OR baciloscopia-result) NOT (gene-expertresult-labform OR xpert-result-labform OR geneexpert-result-ficha-clinica)");

    return cd;
  }

  @DocumentedDefinition(value = "get Adittional Other Than GenExpert Test")
  public CohortDefinition getAdditionalOtherThanGenExpertTestCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - Adittional Other Than GenExpert Test");
    this.addGeneralParameters(cd);

    final CohortDefinition applicationForLabResearch =
        this.genericCohortQueries.generalSql(
            "applicationForLabResearch",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId())));

    final CohortDefinition cultureOrLamTest =
        this.genericCohortQueries.generalSql(
            "cultureOrLamTest",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition cultureOrLamTestResultLabForm =
        this.genericCohortQueries.generalSql(
            "cultureOrLamTestResultLabForm",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition geneExpertResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "geneExpertResultLaboratoryForm",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition xpertMTBResultLaboratoryForm =
        this.genericCohortQueries.generalSql(
            "xpertMTBResultLaboratoryForm",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                // A substituir pelo conceptid do XpertMTB(presenca de MTB detectada) a ser criado
                // no metadata
                Arrays.asList(24063),
                Arrays.asList(
                    this.tbMetadata.getYesConcept().getConceptId(),
                    this.tbMetadata.getNoConcept().getConceptId())));

    final CohortDefinition exameBasilosCopia =
        this.genericCohortQueries.generalSql(
            "exameBasilosCopia",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    // Substituir pelo ID do conceito NAO ENCONTRADO a ser criado no metadata
                    24058)));

    final CohortDefinition applicationForLabResearchForBK =
        this.genericCohortQueries.generalSql(
            "applicationForLabResearchForBK",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId(),
                    this.tbMetadata.getTbGenexpertTest().getConceptId())));

    final CohortDefinition geneExpertResultFichaClinica =
        this.genericCohortQueries.generalSql(
            "geneExpertResultFichaClinica",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition baciloscopiaResulFichaClinica =
        this.genericCohortQueries.generalSql(
            "baciloscopiaResultAnyResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    // A substituir pelo conceptID do NAO ENCONTRADO a ser criado no metadata
                    24058)));

    this.addGeneralParameters(applicationForLabResearch);
    this.addGeneralParameters(cultureOrLamTest);
    this.addGeneralParameters(cultureOrLamTestResultLabForm);
    this.addGeneralParameters(geneExpertResultLaboratoryForm);
    this.addGeneralParameters(xpertMTBResultLaboratoryForm);
    this.addGeneralParameters(exameBasilosCopia);
    this.addGeneralParameters(applicationForLabResearchForBK);
    this.addGeneralParameters(geneExpertResultFichaClinica);
    this.addGeneralParameters(baciloscopiaResulFichaClinica);

    cd.addSearch(
        "application-for-lab-research",
        this.map(applicationForLabResearch, this.generalParameterMapping));
    cd.addSearch("culture-or-lam-test", this.map(cultureOrLamTest, this.generalParameterMapping));
    cd.addSearch(
        "culture-lam-result-labform",
        this.map(cultureOrLamTestResultLabForm, this.generalParameterMapping));
    cd.addSearch(
        "geneexpert-result-lab-form",
        this.map(geneExpertResultLaboratoryForm, this.generalParameterMapping));
    cd.addSearch(
        "xpertMTB-result_labform",
        this.map(xpertMTBResultLaboratoryForm, this.generalParameterMapping));

    cd.addSearch("exame-baciloscopia", this.map(exameBasilosCopia, this.generalParameterMapping));
    cd.addSearch(
        "application-for-labresearchforBK",
        this.map(applicationForLabResearchForBK, this.generalParameterMapping));
    cd.addSearch(
        "geneexpert-result-fichaclinica",
        this.map(geneExpertResultFichaClinica, this.generalParameterMapping));
    cd.addSearch(
        "baciloscopia-result-ficha-clinica",
        this.map(baciloscopiaResulFichaClinica, this.generalParameterMapping));

    cd.setCompositionString(
        "(applicationForLabResearch OR culture-or-lam-test OR culture-lam-result-labform) "
            + "NOT (geneexpert-result-lab-form OR xpertMTB-result_labform OR exame-baciloscopia OR application-for-labresearchforBK OR geneexpert-result-fichaclinica"
            + "baciloscopia-result-ficha-clinica )");

    return cd;
  }

  @DocumentedDefinition(value = "get Positive Results")
  public CohortDefinition getPositiveResultCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    this.addGeneralParameters(cd);
    cd.setName("TxTB - Positive Results");

    final CohortDefinition tbPositiveResultReturned =
        this.genericCohortQueries.generalSql(
            "tbPositiveResultReturned",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(this.tbMetadata.getPositiveConcept().getConceptId())));

    final CohortDefinition baciloscopiaResult =
        this.genericCohortQueries.generalSql(
            "baciloscopiaResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId(),
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId()),
                Arrays.asList(this.tbMetadata.getPositiveConcept().getConceptId())));

    final CohortDefinition xPertMTBResult =
        this.genericCohortQueries.generalSql(
            "xPertMTBResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                // Este conceito deve ser substituido pelo conceito da presenca de MTB detectada a
                // ser criado no metadata
                Arrays.asList(24063),
                Arrays.asList(this.tbMetadata.getYesConcept().getConceptId())));

    this.addGeneralParameters(tbPositiveResultReturned);
    this.addGeneralParameters(baciloscopiaResult);
    this.addGeneralParameters(xPertMTBResult);

    cd.addSearch(
        "tb-positive-result", this.map(tbPositiveResultReturned, this.generalParameterMapping));
    cd.addSearch("baciloscopia-result", this.map(baciloscopiaResult, this.generalParameterMapping));
    cd.addSearch("xpertmtb-result", this.map(xPertMTBResult, this.generalParameterMapping));

    cd.setCompositionString("tb-positive-result OR baciloscopia-result OR xpertmtb-result");

    return cd;
  }

  @DocumentedDefinition(value = "patientsWhoAreTransferredOut")
  public CohortDefinition getPatientsWhoAreTransferredOut() {
    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "txTBPatientsWhoAreTransferedOutCalculation",
            Context.getRegisteredComponents(TxTBPatientsWhoAreTransferedOutCalculation.class)
                .get(0));
    cd.setName("TxTB - patientsWhoAreTransferredOut");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }
}
