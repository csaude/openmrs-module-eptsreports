package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory13P4CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory11P2QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.ReportType;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P4CohortQueries {
  @Autowired private MQCohortQueries mqCohortQueries;
  @Autowired private MQCategory13P4CohortQueries mQCategory13P4CohortQueries;
  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(
      value = "findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory11P2QueriesInterface.QUERY
            .findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatietnsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator")
  public CohortDefinition
      findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoHaveLastFirstLineTerapeutic(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientWithCVOver1000CopiesCategory13B2(), mappings));

    definition.addSearch(
        "PREGNANT-B4",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoTransferedOutRF07Category7(ReportType.MI),
            mappings));

    definition.addSearch(
        "DEAD",
        EptsReportUtils.map(
            mQCohortQueries.findAllPatientWhoAreDeadByEndOfRevisonPeriod(), mappings));

    definition.setCompositionString(
        "(B1 AND B2) NOT (PREGNANT-B4 OR TRANSFERED-IN OR TRANSFERED-OUT OR DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorAdult")
  public CohortDefinition
      findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorAdult() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoHaveLastFirstLineTerapeutic(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientWithCVOver1000CopiesCategory13B2(), mappings));

    definition.addSearch(
        "PREGNANT-B4",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoTransferedOutRF07Category7(ReportType.MI),
            mappings));

    definition.addSearch(
        "DEAD",
        EptsReportUtils.map(
            mQCohortQueries.findAllPatientWhoAreDeadByEndOfRevisonPeriod(), mappings));

    definition.setCompositionString(
        "(B1 AND B2) NOT (PREGNANT-B4 OR TRANSFERED-IN OR TRANSFERED-OUT OR DEAD)");

    return definition;
  }

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
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveLastTerapeutcLineByQueryB1(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            mqCohortQueries
                .findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation(),
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
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoTransferedOutRF07Category7(ReportType.MI),
            mappings));

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
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINADOR-CAT13-3",
        EptsReportUtils.map(
            this
                .findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator(),
            mappings));

    definition.addSearch(
        "H-CAT-13-3",
        EptsReportUtils.map(
            this.mqCohortQueries.findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH(),
            mappings));

    definition.setCompositionString("(DENOMINADOR-CAT13-3 AND H-CAT-13-3)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReceivedResultMoreThan1000CVCategory13P3Numerator")
  public CohortDefinition findPatientsWhoReceivedResultMoreThan1000CVCategory13P3Numerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoReceivedResultMoreThan1000CVCategory13P3Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINADOR-CAT13-3",
        EptsReportUtils.map(
            this
                .findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorAdult(),
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
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-5m+1d},endInclusionDate=${endRevisionDate-4m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINADOR",
        EptsReportUtils.map(
            this.findPregnantWhoHaveRequestedCVCategory13P4Denumerator(), mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this.mqCohortQueries
                .findPatientsWhoHaveRequestedCV120DaysAfterCV50CopiesResultByQueryH(),
            mappingsMI));

    definition.setCompositionString("(DENOMINADOR AND H)");

    return definition;
  }
}
