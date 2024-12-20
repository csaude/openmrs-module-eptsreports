package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13P2CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;
  @Autowired private MQCategory13P3CohortQueries mqCategory13P3CohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator")
  public CohortDefinition findPatientsWhoArePregnantWithCVInTARVCategory13P2Denominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "START-ART",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreNewlyEnrolledOnARTRF05(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(
            this.mqCategory13P3CohortQueries.findPatientsWhoTransferedOutRF07Category7(),
            mappings));

    definition.addSearch(
        "DROPPED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod(),
            mappings));

    definition.setCompositionString("(START-ART AND PREGNANT) NOT (TRANSFERED-OUT OR DROPPED-OUT)");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denumerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2(),
            mappings));

    definition.addSearch(
        "DROPPEDOUT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod(),
            mappings));

    definition.setCompositionString("B2 NOT DROPPEDOUT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "RF29",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantWithCVInTARVCategory13P2Numerator(), mappings));

    definition.addSearch(
        "RF31",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Numerator(),
            mappings));

    definition.setCompositionString("RF29 OR RF31");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantWithCVInTARVCategory13P2Numerator")
  public CohortDefinition findPatientsWhoArePregnantWithCVInTARVCategory13P2Numerator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoArePregnantWithCVInTARVCategory13P2Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantWithCVInTARVCategory13P2Denominator(), mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3(),
            mappings));

    definition.setCompositionString("DENOMINATOR AND H");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Numerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Numerator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    definition.addSearch(
        "DENOMINADOR",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denominator(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4(),
            mappings));

    definition.setCompositionString("DENOMINADOR AND J");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Numerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Numerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "START-ART",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreNewlyEnrolledOnARTRF05(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2(),
            mappings));

    definition.addSearch(
        "B3",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3(),
            mappings));

    definition.addSearch(
        "B4",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4(),
            mappings));

    definition.addSearch(
        "K",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoHaveCVResultAfter33DaysOfRequestByK(), mappings));

    definition.addSearch(
        "L",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoHaveCVResultAfeter33DaysOfRequestForPregnantWishRequestedCVByL(),
            mappings));

    definition.addSearch(
        "M",
        EptsReportUtils.map(
            this.mQCohortQueries.findAllPatientsWhoHaveCVResultAfter33DaysOfStarrDateByM(),
            mappings));

    definition.addSearch(
        "N",
        EptsReportUtils.map(
            this.mQCohortQueries.findAllPatientsWhoArePregantsWithResultAfter33DaysRequestedCVByN(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(), mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(
            this.mqCategory13P3CohortQueries.findPatientsWhoTransferedOutRF07Category7(),
            mappings));

    definition.setCompositionString(
        "((START-ART AND PREGNANT AND B3 AND (K OR M)) NOT (BREASTFEEDING OR TRANSFERED-OUT)) OR (B2 AND B4 AND (L OR N)) ");

    return definition;
  }
}
