package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P2CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator")
  public CohortDefinition findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-4m+1d},endInclusionDate=${endRevisionDate-3m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "START-ART",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreNewlyEnrolledOnARTRF05(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "DROPPED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod(),
            mappings));

    definition.setCompositionString(
        "((START-ART AND PREGNANT) NOT (TRANSFERED-OUT OR TRANSFERED-IN OR DROPPED-OUT)");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denumerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVInFirstConsultationTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2(),
            mappings));

    definition.addSearch(
        "DROPPED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod(),
            mappings));

    definition.setCompositionString("B2 NOT DROPPED-OUT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator")
  public CohortDefinition
      findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriod(),
            mappings));

    definition.setCompositionString("PREGNANT");

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

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-4m+1d},endInclusionDate=${endRevisionDate-3m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-15",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantWithCVInTARVCategory13P2Denumerator(), mappingsMI));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3(),
            mappingsMI));

    definition.setCompositionString("(DENOMINATOR-13-15 AND H)");
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
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4(),
            mappings));

    definition.addSearch(
        "DROPPED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod(),
            mappings));

    definition.setCompositionString("((B2 NOT DROPPED-OUT) AND J)");
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
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-17",
        EptsReportUtils.map(
            this
                .findPatientsWhoArePregnantWithCVIn33DaysAfterInclusionDateTARVCategory13P2Denumerator(),
            mappings));

    definition.addSearch(
        "REQUEST-RESULT-VL",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriodAnd33DaysAfterRequestLaboratoryInvestigations(),
            mappings));

    definition.setCompositionString("(DENOMINATOR-13-17 AND REQUEST-RESULT-VL)");

    return definition;
  }
}
