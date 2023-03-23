package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13Section1QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13Section1CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantCAT13Part1")
  public CohortDefinition findPatientsWhoArePregnantCAT13Part1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantCAT13Part1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory13Section1QueriesInterface.QUERY.findPatientsWhoArePregnantCAT13Part1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingCAT13Part1")
  public CohortDefinition findPatientsWhoAreBreastfeedingCAT13Part1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingCAT13Part1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY.findPatientsWhoAreBreastfeedingCAT13Part1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithLastClinicalConsultationDenominatorB1")
  public CohortDefinition findPatientsWithLastClinicalConsultationDenominatorB1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationDenominatorB1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithCVDenominatorB4E")
  public CohortDefinition findPatientsWithCVDenominatorB4E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory13Section1QueriesInterface.QUERY.findPatientsWithCVDenominatorB4E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithRequestCVDenominatorB5E")
  public CohortDefinition findPatientsWithRequestCVDenominatorB5E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY.findPatientsWithRequestCVDenominatorB5E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithRequestCVInTheLast12MonthsBeforeLastConsultation")
  public CohortDefinition findPatientsWithRequestCVInTheLast12MonthsBeforeLastConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Patients with request CV in the last 12 months before last consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithRequestCVInTheLast12MonthsBeforeLastConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findNumeratorCategory13Section1C")
  public CohortDefinition findNumeratorCategory13Section1C() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory13Section1QueriesInterface.QUERY.findNumeratorC;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorCategory13SectionIB")
  public CohortDefinition findDenominatorCategory13SectionIB() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIB");
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
            this.findPatientsWithLastClinicalConsultationDenominatorB1(), mappings));

    definition.addSearch(
        "B2NEW",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW(),
            mappings));

    definition.addSearch(
        "B3",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3(),
            mappings));

    definition.addSearch(
        "B3E",
        EptsReportUtils.map(
            this
                .findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E(),
            mappings));

    definition.addSearch(
        "B5E", EptsReportUtils.map(this.findPatientsWithRequestCVDenominatorB5E(), mappings));

    definition.addSearch(
        "C", EptsReportUtils.map(this.findPatientsWhoArePregnantCAT13Part1(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(this.findPatientsWhoAreBreastfeedingCAT13Part1(), mappings));

    definition.addSearch(
        "DROPPEDOUT",
        EptsReportUtils.map(
            mQCohortQueries
                .findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation(),
            mappings));

    definition.addSearch(
        "REINITIATED-ART",
        EptsReportUtils.map(
            this
                .findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation(),
            mappings));

    definition.setCompositionString(
        "(B1 AND ((B2NEW NOT DROPPEDOUT) OR (REINITIATED-ART NOT DROPPEDOUT) OR (B3 NOT (B3E OR DROPPEDOUT)))) NOT B5E NOT C NOT D");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionIC")
  public CohortDefinition findFinalNumeratorCategory13SectionIC() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findFinalNumeratorCategory13SectionIC");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "Denominator", EptsReportUtils.map(this.findDenominatorCategory13SectionIB(), mappings));

    definition.addSearch(
        "G", EptsReportUtils.map(this.findNumeratorCategory13Section1C(), mappings));

    definition.setCompositionString("(Denominator AND G)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAbandonedARTInTheFirstSixMonthsOfARTStart")
  public CohortDefinition findPatientsWhoAbandonedARTInTheFirstSixMonthsOfARTStart() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Patients who dropped out ART in the first six months after ART start");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWhoAbandonedARTInTheFirstSixMonthsOfARTStart;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation")
  public CohortDefinition
      findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who reinitiated ART 6 months before the last clinical consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterChangeFirstLineRegimenART")
  public CohortDefinition
      findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterChangeFirstLineRegimenART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out ART in the first six months of change regimen in first line");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterChangeFirstLineRegimenART;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterInitiatedSecondLineRegimenART")
  public CohortDefinition
      findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterInitiatedSecondLineRegimenART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out ART in the first six months after initiated second line regimen ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterInitiatedSecondLineRegimenART;

    definition.setQuery(query);

    return definition;
  }
}
