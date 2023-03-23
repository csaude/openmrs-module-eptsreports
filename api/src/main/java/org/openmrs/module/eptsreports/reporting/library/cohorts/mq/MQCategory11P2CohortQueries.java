package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory11P2QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory11P2CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantDuringInclusionPeriodByB3")
  public CohortDefinition findPatientsWhoArePregnantDuringInclusionPeriodByB3() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringInclusionPeriodByB3");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory11P2QueriesInterface.QUERY.findPatientsWhoArePregnantDuringInclusionPeriodByB3;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAdultsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11NumeratorAdult")
  public CohortDefinition
      findPatientsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11Numerator() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findAdultsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11NumeratorAdult");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11Numerator;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH")
  public CohortDefinition
      findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQQueriesInterface.QUERY
            .findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11Numerator;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH")
  public CohortDefinition
      findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQQueriesInterface.QUERY
            .findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11Numerator;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation");
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
          "findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory11P2QueriesInterface.QUERY
            .findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInCategory11DenominatorVersion2")
  public CohortDefinition
      findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInCategory11DenominatorP2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInCategory11DenominatorVersion2");
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
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString(
        "(START-ART AND PREGNANT) NOT (BREASTFEEDING OR TRANSFERED-IN OR TRANSFERED-OUT)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorVersion2")
  public CohortDefinition
      findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorP2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorVersion2");

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
            this.mQCohortQueries.findPatientsWhoHaveLastFirstLineTerapeutic(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this
                .findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TRANSFERED-OUT",
        EptsReportUtils.map(this.mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("(B1 AND PREGNANT) NOT (BREASTFEEDING OR TRANSFERED-OUT)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTCategory11NumeratorP2")
  public CohortDefinition
      findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTCategory11NumeratorP2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTCategory11NUMERATORVersion2");

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
            this
                .findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInCategory11DenominatorP2(),
            mappings));

    definition.addSearch(
        "G-APSS-PP",
        EptsReportUtils.map(
            this
                .findPatientsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11Numerator(),
            mappings));

    definition.setCompositionString("(DENOMINADOR AND G-APSS-PP)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH50CVCategory11NUMERATOR")
  public CohortDefinition
      findPregnantOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH50CVCategory11NUMERATOR() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH50CVCategory11NUMERATOR");
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
            this
                .findPregnantOnARTStartedExcludingBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11DenominatorP2(),
            mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this
                .findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH(),
            mappings));

    definition.setCompositionString("(DENOMINADOR AND H)");

    return definition;
  }
}
