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

  public Mapped<CohortDefinition> map(final CohortDefinition cd, final String parameterMappings) {
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

  public CohortDefinition getPulmonaryTBWithinReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "pulmonaryTBeWithinReportingDate",
            TXTBQueries.dateObsByObsDateTimeClausule(
                this.hivMetadata.getOtherDiagnosis().getConceptId(),
                this.tbMetadata.getPulmonaryTB().getConceptId(),
                this.hivMetadata.getMasterCardEncounterType().getEncounterTypeId()));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getSputumForAcidFastBacilliWithinReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "SputumForAcidFastBacilli",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNotFoundConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getTuberculosisSymptoms(Integer... answerIds) {

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

  public CohortDefinition getActiveTuberculosis() {
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

  public CohortDefinition getTbObservations() {
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

  public CohortDefinition getApplicationForLaboratoryResearch() {
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
                    this.tbMetadata.getRaioXTorax().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getTbGenExpertORCultureTestOrTbLamOrBk() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbGenExpertORCultureTestOrTbLamOrBK",
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

  public CohortDefinition getGenExpertOrCulturaOnFichaLaboratorio() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "GenExpertOrCultura",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getTbLamOnFichaLaboratorio() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "TbLamOnFichaLaboratorio",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId(),
                    this.tbMetadata.getIndeterminateConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getXpertMTBOnFichaLaboratorio() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "XpertMTBOnFichaLaboratorio",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getXpertMtb().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getYesConcept().getConceptId(),
                    this.tbMetadata.getNoConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getTbRaioXTorax() {
    CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tbRaioXTorax",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getRaioXTorax().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId(),
                    this.tbMetadata.getIndeterminateConcept().getConceptId())));
    this.addGeneralParameters(definition);
    return definition;
  }

  public CohortDefinition getTuberculosisTreatmentPlanWithinReportingDate() {
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

  public CohortDefinition getTuberculosisTreatmentPlanWithinPreviousReportingDate() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "tuberculosisTreatmentPlanWithinReportingDate-Previous Reporting Period",
            TXTBQueries.dateObsByObsDateTimeClausuleInPreviousReportingPeriod(
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
            "TBPROGRAMA", TXTBQueries.inTBProgramWithinReportingPeriodAtLocation());
    this.addGeneralParameters(definition);
    return definition;
  }

  /** PROGRAMA: PACIENTES INSCRITOS NO PROGRAMA DE TUBERCULOSE - NO PERIODO ANTERIOR */
  public CohortDefinition getInTBProgramPreviousPeriod() {
    final CohortDefinition definition =
        this.genericCohortQueries.generalSql(
            "TBPROGRAMA-Previous Reporting Period",
            TXTBQueries.inTBProgramWithinPreviousReportingPeriodAtLocation(
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
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType()),
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
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType()),
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
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType()),
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
                this.hivMetadata.getARVPediatriaSeguimentoEncounterType()),
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
    cd.addSearch("i", this.map(i, generalParameterMapping));
    cd.addSearch("ii", this.map(ii, generalParameterMapping));
    cd.addSearch(
        "iii", this.map(this.getPulmonaryTBWithinReportingDate(), generalParameterMapping));
    cd.addSearch(
        "iv",
        this.map(this.getTuberculosisTreatmentPlanWithinReportingDate(), generalParameterMapping));

    final CohortDefinition artList = this.artList();
    cd.addSearch("artList", this.map(artList, generalParameterMapping));
    cd.setCompositionString("(i OR ii OR iii OR iv) AND artList");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "txTbNumerator")
  public CohortDefinition txTbNumerator() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - txTbNumerator");
    final CohortDefinition A = this.txTbNumeratorA();
    cd.addSearch("A", EptsReportUtils.map(A, this.generalParameterMapping));

    cd.addSearch(
        "A-PREVIOUS-PERIOD",
        EptsReportUtils.map(
            A, "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));

    cd.setCompositionString("A NOT A-PREVIOUS-PERIOD");

    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "txTbNumerator")
  public CohortDefinition getNumeratorForPreviousPeriod() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - Numerator for Previous Period");
    final CohortDefinition A = this.txTbNumeratorA();
    cd.addSearch("A", EptsReportUtils.map(A, this.generalParameterMapping));

    cd.setCompositionString("A");

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
    cd.addSearch(
        "I",
        EptsReportUtils.map(
            this.getSputumForAcidFastBacilliWithinReportingDate(), this.generalParameterMapping));

    cd.setCompositionString("A OR B OR C OR D OR E OR F OR G OR H OR I");
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
        "other-diagnosis-fichaResumo",
        EptsReportUtils.map(
            this.getPulmonaryTBWithinReportingDate(), this.generalParameterMapping));

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
        "other-diagnosis-FichaResumo-previousPeriod",
        EptsReportUtils.map(
            this.getPulmonaryTBWithinReportingDate(),
            "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));

    CohortDefinition fichaClinicaMasterCard =
        this.genericCohortQueries.generalSql(
            "fichaClinicaMasterCard",
            TXTBQueries.dateObsByObsValueDateTimeClausule(
                this.tbMetadata.getTBTreatmentPlanConcept().getConceptId(),
                this.hivMetadata.getStartDrugsConcept().getConceptId(),
                this.hivMetadata.getAdultoSeguimentoEncounterType().getId()));

    CohortDefinition fichaAdultoSeguimentoAndPediatriaSeguimento =
        this.genericCohortQueries.generalSql(
            "adultoandpediatriaseguimento", TXTBQueries.findTBScreeningResultInvestigationBkOrRX());

    CohortDefinition transferredOut =
        this.genericCohortQueries.generalSql(
            "transferred-out", TXTBQueries.findPatientWhoAreTransferedOut());

    CohortDefinition tbScreeningFC =
        this.genericCohortQueries.generalSql(
            "tbSreeningFC", TXTBQueries.findTBScreeningFcMasterCard());

    this.addGeneralParameters(fichaClinicaMasterCard);
    this.addGeneralParameters(fichaAdultoSeguimentoAndPediatriaSeguimento);
    this.addGeneralParameters(transferredOut);
    this.addGeneralParameters(tbScreeningFC);

    definition.addSearch(
        "A-PREVIOUS-PERIOD",
        EptsReportUtils.map(
            this.getNumeratorForPreviousPeriod(),
            "startDate=${startDate-6m},endDate=${startDate-1d},location=${location}"));

    definition.addSearch(
        "ficha-clinica-master-card",
        this.map(fichaClinicaMasterCard, this.generalParameterMapping));

    definition.addSearch(
        "ficha-adulto-and-pediatria-seguimento",
        this.map(fichaAdultoSeguimentoAndPediatriaSeguimento, this.generalParameterMapping));

    definition.addSearch("transferred-out", this.map(transferredOut, this.generalParameterMapping));

    definition.addSearch(
        "tb-screening-fc-master-card", this.map(tbScreeningFC, this.generalParameterMapping));

    definition.addSearch(
        "all-tb-symptoms",
        this.map(this.getAllTBSymptomsForDemoninatorComposition(), this.generalParameterMapping));
    definition.addSearch(
        "ficha-laboratorio-results",
        this.map(this.getResultsOnFichaLaboratorio(), this.generalParameterMapping));

    definition.addSearch(
        "ficha-laboratorio-results",
        this.map(this.getResultsOnFichaLaboratorio(), this.generalParameterMapping));

    definition.setCompositionString(
        "(art-list AND "
            + " ( tb-screening OR tb-screening-fc-master-card OR tb-investigation OR started-tb-treatment OR in-tb-program OR other-diagnosis-fichaResumo OR ficha-clinica-master-card OR all-tb-symptoms OR ficha-laboratorio-results OR ficha-adulto-and-pediatria-seguimento )) "
            + " NOT ((transferred-out NOT (started-tb-treatment OR in-tb-program)) OR started-tb-treatment-previous-period OR in-tb-program-previous-period OR other-diagnosis-FichaResumo-previousPeriod OR A-PREVIOUS-PERIOD )");

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
  public CohortDefinition getAllTBSymptomsForDisaggregationComposition() {

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
        "tb-genexpert-or-culture-test-or-lam-or-bk-test",
        this.map(this.getTbGenExpertORCultureTestOrTbLamOrBk(), this.generalParameterMapping));
    definition.addSearch(
        "tb-raioxtorax", this.map(this.getTbRaioXTorax(), this.generalParameterMapping));
    definition.addSearch(
        "lab-results", this.map(this.getResultsOnFichaLaboratorio(), this.generalParameterMapping));
    definition.setCompositionString(
        "tuberculosis-symptoms OR active-tuberculosis OR tb-observations OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-or-bk-test OR tb-raioxtorax OR lab-results");

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
        "tb-genexpert-or-culture-test-or-lam-or-bk-test",
        this.map(this.getTbGenExpertORCultureTestOrTbLamOrBk(), this.generalParameterMapping));

    definition.addSearch(
        "tb-raioxtorax", this.map(this.getTbRaioXTorax(), this.generalParameterMapping));

    definition.setCompositionString(
        "tuberculosis-symptoms OR active-tuberculosis OR tb-observations OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-or-bk-test OR tb-raioxtorax");

    return definition;
  }

  @DocumentedDefinition(value = "get Specimen Sent")
  public CohortDefinition getSpecimenSentCohortDefinition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -specimen-sent");

    CohortDefinition applicationForLaboratoryResearchDataset =
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

    this.addGeneralParameters(applicationForLaboratoryResearchDataset);

    definition.addSearch(
        "application-for-laboratory-research",
        this.map(applicationForLaboratoryResearchDataset, this.generalParameterMapping));
    definition.addSearch(
        "tb-genexpert-culture-lam-bk-test",
        this.map(this.getTbGenExpertORCultureTestOrTbLamOrBk(), this.generalParameterMapping));
    definition.addSearch(
        "lab-results", this.map(this.getResultsOnFichaLaboratorio(), this.generalParameterMapping));

    definition.addSearch(
        "DENOMINATOR", this.map(this.getDenominator(), this.generalParameterMapping));

    definition.setCompositionString(
        "(application-for-laboratory-research OR tb-genexpert-culture-lam-bk-test OR lab-results) AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "get Diagnóstico Laboratorial para TB")
  private CohortDefinition getResultsOnFichaLaboratorio() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -Diagnóstico Laboratorial para TB");

    definition.addSearch(
        "sputum-for-acid-fast-bacilli",
        EptsReportUtils.map(
            this.getSputumForAcidFastBacilliWithinReportingDate(), this.generalParameterMapping));

    definition.addSearch(
        "genexpert-culture",
        EptsReportUtils.map(
            this.getGenExpertOrCulturaOnFichaLaboratorio(), this.generalParameterMapping));

    definition.addSearch(
        "tblam",
        EptsReportUtils.map(this.getTbLamOnFichaLaboratorio(), this.generalParameterMapping));

    definition.addSearch(
        "xpert-mtb",
        EptsReportUtils.map(this.getXpertMTBOnFichaLaboratorio(), this.generalParameterMapping));

    definition.setCompositionString(
        "sputum-for-acid-fast-bacilli OR genexpert-culture OR tblam OR xpert-mtb");

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

    final CohortDefinition genXpertLabResults =
        this.genericCohortQueries.generalSql(
            "genXpertLabResults",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbGenexpertTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition xpertMTBLabResults =
        this.genericCohortQueries.generalSql(
            "xpertMTBLabResults",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getXpertMtb().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getYesConcept().getConceptId(),
                    this.tbMetadata.getNoConcept().getConceptId())));

    this.addGeneralParameters(applicationForLabResearch);
    this.addGeneralParameters(geneExpertTest);
    this.addGeneralParameters(genXpertLabResults);
    this.addGeneralParameters(xpertMTBLabResults);

    cd.addSearch(
        "application-for-lab-research",
        this.map(applicationForLabResearch, this.generalParameterMapping));
    cd.addSearch("gen-expert-test", this.map(geneExpertTest, this.generalParameterMapping));
    cd.addSearch("gen-expert-lab-test", this.map(genXpertLabResults, this.generalParameterMapping));
    cd.addSearch("xpert-mtb-lab-tests", this.map(xpertMTBLabResults, this.generalParameterMapping));

    cd.addSearch("DENOMINATOR", this.map(this.getDenominator(), this.generalParameterMapping));

    cd.setCompositionString(
        "(application-for-lab-research OR gen-expert-test OR gen-expert-lab-test OR xpert-mtb-lab-tests) AND DENOMINATOR");

    return cd;
  }

  @DocumentedDefinition(value = "get Smear Microscopy Diagnostic Test")
  public CohortDefinition getSmearMicroscopyOnlyDiagnosticTestCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - Smear Microscopy Diagnostic Test");
    this.addGeneralParameters(cd);

    final CohortDefinition exameBasilosCopiaOnFichaClinica =
        this.genericCohortQueries.generalSql(
            "exameBasilosCopiaOnFichaClinica",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId())));

    final CohortDefinition resultadoExameBasilosCopiaOnFichaClinica =
        this.genericCohortQueries.generalSql(
            "resultadoExameBasilosCopiaOnFichaClinica",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition exameBasilosCopiaOnFichaLaboratorio =
        this.genericCohortQueries.generalSql(
            "exameBasilosCopia",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getSputumForAcidFastBacilli().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNotFoundConcept().getConceptId())));

    this.addGeneralParameters(exameBasilosCopiaOnFichaLaboratorio);
    this.addGeneralParameters(exameBasilosCopiaOnFichaClinica);
    this.addGeneralParameters(resultadoExameBasilosCopiaOnFichaClinica);

    cd.addSearch(
        "exame-basiloscopia-fichaClinica",
        this.map(exameBasilosCopiaOnFichaClinica, this.generalParameterMapping));
    cd.addSearch(
        "resultad-exame-basiloscopia-ficha-clinica",
        this.map(resultadoExameBasilosCopiaOnFichaClinica, this.generalParameterMapping));
    cd.addSearch(
        "exame-basiloscopia-laboratorio",
        this.map(exameBasilosCopiaOnFichaLaboratorio, this.generalParameterMapping));
    cd.addSearch(
        "gen-expert-test",
        this.map(
            this.getGeneXpertMTBDiagnosticTestCohortDefinition(), this.generalParameterMapping));

    cd.addSearch("DENOMINATOR", this.map(this.getDenominator(), this.generalParameterMapping));

    cd.setCompositionString(
        "((exame-basiloscopia-fichaClinica OR resultad-exame-basiloscopia-ficha-clinica OR exame-basiloscopia-laboratorio) NOT gen-expert-test) AND DENOMINATOR");

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

    final CohortDefinition cultureLaboratoryResults =
        this.genericCohortQueries.generalSql(
            "cultureLabResults",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getCultureTest().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId())));

    final CohortDefinition tbLamLaboratoryResults =
        this.genericCohortQueries.generalSql(
            "tbLamLabResults",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getPositiveConcept().getConceptId(),
                    this.tbMetadata.getNegativeConcept().getConceptId(),
                    this.tbMetadata.getIndeterminateConcept().getConceptId())));

    this.addGeneralParameters(applicationForLabResearch);
    this.addGeneralParameters(cultureOrLamTest);
    this.addGeneralParameters(cultureLaboratoryResults);
    this.addGeneralParameters(tbLamLaboratoryResults);

    cd.addSearch(
        "application-for-lab-research",
        this.map(applicationForLabResearch, this.generalParameterMapping));
    cd.addSearch("culture-or-lam-test", this.map(cultureOrLamTest, this.generalParameterMapping));
    cd.addSearch(
        "culture-laboratory-results",
        this.map(cultureLaboratoryResults, this.generalParameterMapping));
    cd.addSearch(
        "tblam-laboratory-results", this.map(tbLamLaboratoryResults, this.generalParameterMapping));

    // exclusions
    cd.addSearch(
        "gen-expert-test",
        this.map(
            this.getGeneXpertMTBDiagnosticTestCohortDefinition(), this.generalParameterMapping));
    cd.addSearch(
        "smear-microscopy-only",
        this.map(
            this.getSmearMicroscopyOnlyDiagnosticTestCohortDefinition(),
            this.generalParameterMapping));

    cd.addSearch("DENOMINATOR", this.map(this.getDenominator(), this.generalParameterMapping));

    cd.setCompositionString(
        "((application-for-lab-research OR culture-or-lam-test OR culture-laboratory-results OR tblam-laboratory-results) NOT (gen-expert-test OR smear-microscopy-only)) AND DENOMINATOR");

    return cd;
  }

  @DocumentedDefinition(value = "get Positive Results")
  public CohortDefinition getPositiveResultCohortDefinition(
      CohortDefinition denominator, String generalParameterMapping) {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    this.addGeneralParameters(cd);
    cd.setName("TxTB - Positive Results");

    final CohortDefinition tbPositiveResultInFichaClinica =
        this.genericCohortQueries.generalSql(
            "tbPositiveResultReturned",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(this.tbMetadata.getPositiveConcept().getConceptId())));

    final CohortDefinition tbPositiveResultsInFichaLaboratorio =
        this.genericCohortQueries.generalSql(
            "baciloscopiaResult",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getMisauLaboratorioEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId()),
                Arrays.asList(this.tbMetadata.getPositiveConcept().getConceptId())));

    this.addGeneralParameters(tbPositiveResultInFichaClinica);
    this.addGeneralParameters(tbPositiveResultsInFichaLaboratorio);

    cd.addSearch(
        "tb-positive-result-ficha-clinica",
        EptsReportUtils.map(tbPositiveResultInFichaClinica, generalParameterMapping));
    cd.addSearch(
        "tb-positive-result-laboratorio",
        EptsReportUtils.map(tbPositiveResultsInFichaLaboratorio, generalParameterMapping));
    cd.addSearch("DENOMINATOR", EptsReportUtils.map(denominator, generalParameterMapping));

    cd.setCompositionString(
        "(tb-positive-result-ficha-clinica OR tb-positive-result-laboratorio) AND DENOMINATOR");

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
