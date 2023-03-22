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
public class MQCategory13P4CohortQueries {

  @Autowired private MQCohortQueries mqCohortQueries;

  @DocumentedDefinition(
      value = "findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Denumerator")
  public CohortDefinition findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Denumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveLastTerapeutcLineByQueryB1(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhohaveCVMoreThan1000CopiesByQueryB2(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(), mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mqCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString(
        "(B1 AND B2) NOT (PREGNANT OR BREASTFEEDING OR TRANSFERED-OUT)");
    return definition;
  }

  @DocumentedDefinition(value = "findPregnantWhoHaveRequestedCVCategory13P4Denumerator")
  public CohortDefinition findPregnantWhoHaveRequestedCVCategory13P4Denumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPregnantWhoHaveRequestedCVCategory13P4Denumerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveLastTerapeutcLineByQueryB1(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mqCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString(
        "(B1 AND PREGNANT) NOT (BREASTFEEDING OR TRANSFERED-IN OR TRANSFERED-OUT)");
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Numerator")
  public CohortDefinition findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Numerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINADOR-CAT13-3",
        EptsReportUtils.map(
            this
                .findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory13Denominator(),
            mappings));

    definition.addSearch(
        "H-CAT-13-3",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH(),
            mappings));

    definition.setCompositionString("(DENOMINADOR-CAT13-3 AND H-CAT-13-3)");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoPregnantReceivedResultMoreThan1000CVCategory13P4Numerator")
  public CohortDefinition
      findPatientsWhoPregnantReceivedResultMoreThan1000CVCategory13P4Numerator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoPregnantReceivedResultMoreThan1000CVCategory13P4Numerator");

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
            this.findPregnantWhoHaveRequestedCVCategory13P4Denumerator(), mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHaveRequestedCV120DaysAfterCV50CopiesResultByQueryH(),
            mappings));

    definition.setCompositionString("(DENOMINADOR AND H)");
    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatietnsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory13Denominator")
  public CohortDefinition
      findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory13Denominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory13Denominator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveLastFirstLineTerapeutic(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientWithCVOver1000CopiesCategory11B2(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mqCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString(
        "(B1 AND B2) NOT (PREGNANT OR BREASTFEEDING OR TRANSFERED-OUT OR TRANSFERED-IN)");

    return definition;
  }
}
