package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13Section1QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
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
  @Autowired private MQCategory13Section2CohortQueries mQCategory13Section2CohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantCAT13Part1")
  public CohortDefinition findPatientsWhoArePregnantCAT13Part1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantCAT13Part1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY.findPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.PREGNANT);

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
        MQCategory13Section1QueriesInterface.QUERY.findPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.BREASTFEEDING);

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

  @DocumentedDefinition(value = "findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod")
  public CohortDefinition findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod")
  public CohortDefinition
      findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod;

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
  public CohortDefinition findDenominatorCategory13SectionIB(boolean excludeTbActiveDiagnostic) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String compositionString = "";

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

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

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            this.findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    if (excludeTbActiveDiagnostic) {
      compositionString =
          "(B1 AND ((B2NEW NOT DROPPEDOUT) OR (REINITIATED-ART NOT DROPPEDOUT) OR (B3 NOT (B3E OR DROPPEDOUT)))) NOT (B5E OR C OR TRANSFERED-IN OR TB-ACTIVA)";
    } else {
      compositionString =
          "(B1 AND ((B2NEW NOT DROPPEDOUT) OR (REINITIATED-ART NOT DROPPEDOUT) OR (B3 NOT (B3E OR DROPPEDOUT)))) NOT (B5E OR C OR TRANSFERED-IN)";
    }

    definition.setCompositionString(compositionString);

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorCategory13SectionIBChildrens")
  public CohortDefinition findDenominatorCategory13SectionIBChildrens() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

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

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            this.findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    definition.setCompositionString(
        "(B1 AND ((B2NEW NOT DROPPEDOUT) OR (REINITIATED-ART NOT DROPPEDOUT) OR (B3 NOT (B3E OR DROPPEDOUT)))) NOT (B5E OR C OR D OR TB-ACTIVA)");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionIC")
  public CohortDefinition findFinalNumeratorCategory13SectionIC(boolean excludeTbActiveDiagnostic) {

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
        "Denominator",
        EptsReportUtils.map(
            this.findDenominatorCategory13SectionIB(excludeTbActiveDiagnostic), mappings));

    definition.addSearch(
        "G", EptsReportUtils.map(this.findNumeratorCategory13Section1C(), mappings));

    definition.setCompositionString("(Denominator AND G)");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionICChildrens")
  public CohortDefinition findFinalNumeratorCategory13SectionICChildrens() {

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
        "Denominator",
        EptsReportUtils.map(this.findDenominatorCategory13SectionIBChildrens(), mappings));

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
      value = "findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation")
  public CohortDefinition findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(startAge, endAge);

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

  @DocumentedDefinition(value = "findDenominatorPedidoCVRF16_Indicator_13_1")
  public CohortDefinition findDenominatorPedidoCVRF16_Indicator_13_1(
      boolean excludeTbActiveDiagnostic) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorPedidoCVRF16_Indicator_13_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-1-OLD",
        EptsReportUtils.map(
            this.findDenominatorCategory13SectionIB(excludeTbActiveDiagnostic), mappings));

    definition.addSearch(
        "DENOMINATOR-13-4-OLD",
        EptsReportUtils.map(
            mQCategory13Section2CohortQueries.findDenominatorCategory13SectionIIB(
                excludeTbActiveDiagnostic),
            mappings));

    definition.setCompositionString("(DENOMINATOR-13-1-OLD OR DENOMINATOR-13-4-OLD)");

    return definition;
  }

  @DocumentedDefinition(value = "findNumeratorPedidoCVRF16_Indicator_13_1")
  public CohortDefinition findNumeratorPedidoCVRF16_Indicator_13_1(
      boolean excludeTbActiveDiagnostic) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorPedidoCVRF16_Indicator_13_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "NUMERATOR-13-1-OLD",
        EptsReportUtils.map(
            this.findFinalNumeratorCategory13SectionIC(excludeTbActiveDiagnostic), mappings));

    definition.addSearch(
        "NUMERATOR-13-4-OLD",
        EptsReportUtils.map(
            mQCategory13Section2CohortQueries.findFinalNumeratorCategory13SectionIIC(
                excludeTbActiveDiagnostic),
            mappings));

    definition.setCompositionString("(NUMERATOR-13-1-OLD OR NUMERATOR-13-4-OLD)");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorPedidoCVRF24_Indicator_13_8")
  public CohortDefinition findDenominatorPedidoCVRF24_Indicator_13_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorPedidoCVRF24_Indicator_13_8");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-8-OLD",
        EptsReportUtils.map(this.findDenominatorCategory13SectionIBChildrens(), mappings));

    definition.addSearch(
        "TEN-FOURTEEN",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(10, 14),
            mappings));

    definition.addSearch(
        "DENOMINATOR-13-13-OLD",
        EptsReportUtils.map(
            mQCategory13Section2CohortQueries.findDenominatorCategory13SectionIIBChildrens(),
            mappings));

    definition.addSearch(
        "TWO-FOURTEEN",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(2, 14),
            mappings));

    definition.setCompositionString(
        "(DENOMINATOR-13-8-OLD AND TEN-FOURTEEN) OR (DENOMINATOR-13-13-OLD AND TWO-FOURTEEN)");

    return definition;
  }

  @DocumentedDefinition(value = "findNumeratorPedidoCVRF16_Indicator_13_1")
  public CohortDefinition findNumeratorPedidoCVRF25_Indicator_13_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorPedidoCVRF16_Indicator_13_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "NUMERATOR-13-8-OLD",
        EptsReportUtils.map(this.findFinalNumeratorCategory13SectionICChildrens(), mappings));

    definition.addSearch(
        "TEN-FOURTEEN",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(10, 14),
            mappings));

    definition.addSearch(
        "NUMERATOR-13-13-OLD",
        EptsReportUtils.map(
            mQCategory13Section2CohortQueries.findFinalNumeratorCategory13SectionIICChildrens(),
            mappings));

    definition.addSearch(
        "TWO-FOURTEEN",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(2, 14),
            mappings));

    definition.setCompositionString(
        "(NUMERATOR-13-8-OLD AND TEN-FOURTEEN) OR (NUMERATOR-13-13-OLD AND TWO-FOURTEEN)");

    return definition;
  }
}
