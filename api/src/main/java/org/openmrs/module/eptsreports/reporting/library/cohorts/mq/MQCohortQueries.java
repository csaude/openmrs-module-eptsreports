package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mi.MICategory12QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory11P2QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory12QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13P2QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13P3QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13P4QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.ReportType;
import org.openmrs.module.eptsreports.reporting.utils.TPTType;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class MQCohortQueries {

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTRF05")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTRF05() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTRF05;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06")
  public CohortDefinition
      findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9")
  public CohortDefinition findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoTransferedOutRF07")
  public CohortDefinition findPatientsWhoTransferedOutRF07() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoTransferedOutRF07");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsWhoTransferedOutRF07;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoTransferedOutRF07Category7")
  public CohortDefinition findPatientsWhoTransferedOutRF07Category7(ReportType reportType) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoTransferedOutRF07Category7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsWhoTransferedOutRF07Category7(reportType);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "getPatientsWhoDiedEndRevisioDate")
  public CohortDefinition getPatientsWhoDiedEndRevisioDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoTransferedOutRF07");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.getPatientsWhoDiedEndRevisioDate;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantInclusionDateRF08")
  public CohortDefinition findPatientsWhoArePregnantInclusionDateRF08() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantInclusionDateRF08");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(TypePTV.PREGNANT);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeSecondLineByAgeRenge")
  public CohortDefinition calculateDefaulteAgeSecondLineByAgeRenge(int firstAge, int secondeAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorByAgeRenge;

    String finalQuery = String.format(query, firstAge, secondeAge);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingInARTInitiation")
  public CohortDefinition findPatientsWhoAreBreastfeedingInARTInitiation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingInARTInitiation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(TypePTV.BREASTFEEDING);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingInclusionDateRF09")
  public CohortDefinition findPatientsWhoAreBreastfeedingInclusionDateRF09() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingInclusionDateRF09");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(TypePTV.BREASTFEEDING);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingForMQCat7AndMQCat12")
  public CohortDefinition findPatientsWhoAreBreastfeedingForMQCat7AndMQCat12() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingForMQCat7AndMQCat12");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeedingForMQCat7AndMQCat12(
            TypePTV.BREASTFEEDING);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearAdulyAndHaveFirstConsultInclusionPeriodCategory3FR12Numerator")
  public CohortDefinition
      findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearAdulyAndHaveFirstConsultInclusionPeriodCategory3FR12Numerator() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearAdulyAndHaveFirstConsultInclusionPeriodCategory3FR12Numerator");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearAdulyAndHaveFirstConsultInclusionPeriodCategory3FR12Numerator;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasNutritionalAssessmentInLastConsultation")
  public CohortDefinition findPatientsWhoHasNutritionalAssessmentInLastConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHasNutritionalAssessmentInLastConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientsWhoHasNutritionalAssessmentInLastConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGInclusionPeriod")
  public CohortDefinition
      findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGInclusionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriod")
  public CohortDefinition
      findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHasNutritionalAssessmentDAMandDAGInLastConsultation")
  public CohortDefinition findPatientsWhoHasNutritionalAssessmentDAMandDAGInLastConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHasNutritionalAssessmentDAMandDAGInLastConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientsWhoHasNutritionalAssessmentDAMandDAGInLastConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHasNutritionalAssessmentDAMandDAGAndATPUInLastConsultation")
  public CohortDefinition
      findPatientsWhoHasNutritionalAssessmentDAMandDAGAndATPUInLastConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHasNutritionalAssessmentDAMandDAGAndATPUInLastConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoHasNutritionalAssessmentDAMandDAGAndATPUInLastConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoDiagnosedWithTBActiveInTheLastConsultationIThePeriodCatetory6")
  public CohortDefinition
      findPatientsWhoDiagnosedWithTBActiveInTheLastConsultationIThePeriodCatetory6() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoDiagnosedWithTBActiveInTheLastConsultationIThePeriodCatetory6");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhoDiagnosedWithTBActiveInTheLastConsultationIThePeriodCatetory6;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientWwithTBScreeningAtTheLastConsultationOfThePeriodCategory6")
  public CohortDefinition findPatientWwithTBScreeningAtTheLastConsultationOfThePeriodCategory6() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWwithTBScreeningAtTheLastConsultationOfThePeriodCategory6");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientWwithTBScreeningAtTheLastConsultationOfThePeriodCategory6;
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsDiagnosedWithActiveTBDuringDuringPeriodCategory7")
  public CohortDefinition findPatientsDiagnosedWithActiveTBDuringDuringPeriodCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsDiagnosedWithActiveTBDuringDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientsDiagnosedWithActiveTBDuringDuringPeriodCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithPositiveTBScreeningInDurindPeriodCategory7")
  public CohortDefinition findPatientsWithPositiveTBScreeningInDurindPeriodCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWithPositiveTBScreeningInDurindPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientsWithPositiveTBScreeningInDurindPeriodCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "finPatientHaveTBTreatmentDuringPeriodCategory7")
  public CohortDefinition finPatientHaveTBTreatmentDuringPeriodCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("finPatientHaveTBTreatmentDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.finPatientHaveTBTreatmentDuringPeriodCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWhoStartTPINHDuringPeriodCategory7")
  public CohortDefinition findPatientWhoStartTPINHDuringPeriodCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPIDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWhoStartTPINHDuringPeriodCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWhoStartTPI3HPDuringPeriodCategory7")
  public CohortDefinition findPatientWhoStartTPI3HPDuringPeriodCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPIDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWhoStartTPI3HPDuringPeriodCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWhoCompleteTPIINHCategory7")
  public CohortDefinition findPatientWhoCompleteTPIINHCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPIDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWhoCompleteTPIINHCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "finPatientsWhoCompletedINHBetween170And297DaysAfterInitiatedTreatment")
  public CohortDefinition finPatientsWhoCompletedINHBetween170And297DaysAfterInitiatedTreatment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPIDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .finPatientsWhoCompletedINHBetween170And297DaysAfterInitiatedTreatment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWhoCompleteTPI3HPCategory7")
  public CohortDefinition findPatientWhoCompleteTPI3HPCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPIDuringPeriodCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWhoCompleteTPI3HPCategory7;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "finPatientsWhoCompleted3HPBetween80And190DaysAfterInitiatedTreatment")
  public CohortDefinition finPatientsWhoCompleted3HPBetween80And190DaysAfterInitiatedTreatment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("finPatientsWhoCompleted3HPBetween80And190DaysAfterInitiatedTreatment");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .finPatientsWhoCompleted3HPBetween80And190DaysAfterInitiatedTreatment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWhoStartTPI4MonthsAfterDateOfInclusionCategory7")
  public CohortDefinition findPatientWhoStartTPI4MonthsAfterDateOfInclusionCategory7() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWhoStartTPI4MonthsAfterDateOfInclusionCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientWhoStartTPI4MonthsAfterDateOfInclusionCategory7;

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
          "findPatientsWhoAreNewlyEnrolledOnARTByAge14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12A")
  public CohortDefinition
      findPatientsWhoAreNewlyEnrolledOnARTByAge14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12A() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNewlyEnrolledOnARTByAge14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12A");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTByAge14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12A;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1")
  public CohortDefinition
      findPatientsWhoAreInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoAreInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreNotInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1E")
  public CohortDefinition
      findPatientsWhoAreNotInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNotInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1E");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoAreNotInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B2")
  public CohortDefinition
      findPatientsWhoAreInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoAreInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreNotInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateB2E")
  public CohortDefinition
      findPatientsWhoAreNotInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateB2E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreNotInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateB2E");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoAreNotInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateB2E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12")
  public CohortDefinition
      findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdult");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12MI")
  public CohortDefinition
      findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12MI() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdult");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory12QueriesInterface.QUERY
            .findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12MI;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12")
  public CohortDefinition
      findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdult");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory12QueriesInterface.QUERY
            .findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12MI")
  public CohortDefinition
      findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12MI() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12MI");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory12QueriesInterface.QUERY
            .findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12MI;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWithCVOver1000CopiesCategory11B2")
  public CohortDefinition findPatientWithCVOver1000CopiesCategory11B2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAdultWithCVOver1000CopiesCategory11B2");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWithCVOver1000CopiesCategory11B2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWithCVOver1000CopiesCategory13B2")
  public CohortDefinition findPatientWithCVOver1000CopiesCategory13B2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAdultWithCVOver1000CopiesCategory13B2");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWithCVOver1000CopiesCategory13B2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWithCVOver1000CopiesAndPregnatCategory11B4")
  public CohortDefinition findPatientWithCVOver1000CopiesAndPregnatCategory11B4() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAdultWithCVOver1000CopiesCategory11B2");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientWithCVOver1000CopiesAndPregnatCategory11B4;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientWithCVOver1000CopiesAndPregnatCategory11B4")
  public CohortDefinition findPatientWithCVOver1000CopiesAndBreastfeedingCategory11B5() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientWithCVOver1000CopiesAndBreastfeedingCategory11B5");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY.findPatientWithCVOver1000CopiesAndBreastfeedingCategory11B5;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveLastFirstLineTerapeutic")
  public CohortDefinition findPatientsWhoHaveLastFirstLineTerapeutic() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHaveLastFirstLineTerapeutic");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsWhoHaveLastFirstLineTerapeutic;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdultH")
  public CohortDefinition
      findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11Numerator() {

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
          "findChildrenOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11NumeratorChildren")
  public CohortDefinition
      findChildrenOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11Numerator() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11NumeratorAdult");

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

  @DocumentedDefinition(value = "findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13")
  public CohortDefinition findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13");

    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13();

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory11P2QueriesInterface.QUERY
            .findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory11P2QueriesInterface.QUERY
            .findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

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
          "findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation");
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

  @DocumentedDefinition(value = "childrenPatientsWithAtLeast9APSSConsultations")
  public CohortDefinition findChildrenPatientsWithAtLeast9APSSConsultationByInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findFirstPatientChildrenAPSSConsultationWithinInclusionReportingPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4")
  public CohortDefinition
      findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodCAT13DenumeratorP2ByB4");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPatientsWhoAreRequestForLaboratoryInvestigationAndPregnantInclusionPeriodByB4;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3")
  public CohortDefinition
      findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodCAT13DenumeratorP2ByB3");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPatientsWhoAreRequestForLaboratoryInvestigationsInclusionPeriodByB3;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantInclusionPeriodCAT13DenumeratorP2ByB1")
  public CohortDefinition findPatientsWhoArePregnantInclusionPeriodCAT13DenumeratorP2ByB1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantInclusionPeriodCAT13DenumeratorP2ByB1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY.findPatientsWhoArePregnantInclusionPeriodByB1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2")
  public CohortDefinition findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPatientsWhoArePregnantInFirstConsultationInclusionPeriodByB2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveCVResultAfter33DaysOfRequestByK")
  public CohortDefinition findPatientsWhoHaveCVResultAfter33DaysOfRequestByK() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHaveCVResultAfter33DaysOfRequestByK");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY.findPatientsWhoHaveCVResultAfter33DaysOfRequestByK;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findAllPatientsWhoHaveCVResultAfter33DaysOfStarrDateByM")
  public CohortDefinition findAllPatientsWhoHaveCVResultAfter33DaysOfStarrDateByM() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAllPatientsWhoHaveCVResultAfter33DaysOfStarrDateByM");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findAllPatientsWhoHaveCVResultAfter33DaysOfStarrDateByM;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHaveCVResultAfeter33DaysOfRequestForPregnantWishRequestedCVByL")
  public CohortDefinition
      findPatientsWhoHaveCVResultAfeter33DaysOfRequestForPregnantWishRequestedCVByL() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoHaveCVResultAfeter33DaysOfRequestForPregnantWishRequestedCVByL");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPatientsWhoHaveCVResultAfeter33DaysOfRequestForPregnantWishRequestedCVByL;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findAllPatientsWhoArePregantsWithResultAfter33DaysRequestedCVByN")
  public CohortDefinition findAllPatientsWhoArePregantsWithResultAfter33DaysRequestedCVByN() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAllPatientsWhoArePregantsWithResultAfter33DaysRequestedCVByN");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findAllPatientsWhoArePregantsWithResultAfter33DaysRequestedCVByN;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveLastTerapeutcLineByQueryB1")
  public CohortDefinition findPatientsWhoHaveLastTerapeutcLineByQueryB1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHaveLastTerapeutcLineByQueryB1");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P4QueriesInterface.QUERY.findPatientsWhoHaveLastTerapeutcLineByQueryB1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhohaveCVMoreThan1000CopiesByQueryB2")
  public CohortDefinition findPatientsWhohaveCVMoreThan1000CopiesByQueryB2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhohaveCVMoreThan1000CopiesByQueryB2");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P4QueriesInterface.QUERY.findPatientsWhohaveCVMoreThan1000CopiesByQueryB2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHave3APSSPPConsultationInSameDayOfCVByQueryG")
  public CohortDefinition findPatientsWhoHave3APSSPPConsultationInSameDayOfCVByQueryG() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHave3APSSPPConsultationInSameDayOfCVByQueryG");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P4QueriesInterface.QUERY
            .findPatientsWhoHave3APSSPPConsultationInSameDayOfCVByQueryG;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH")
  public CohortDefinition findPatientsWhoHaveRequestedCV120DaysAfterCV50CopiesResultByQueryH() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHaveRequestedCV120DaysAfterCV50CopiesResultByQueryH");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P4QueriesInterface.QUERY
            .findPatientsWhoHaveRequestedCV120DaysAfterCV50CopiesResultByQueryH;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH")
  public CohortDefinition findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P4QueriesInterface.QUERY
            .findPatientsWhoHaveRequestedCV120DaysAfterCVResultByQueryH;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation")
  public CohortDefinition
      findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findAllPatientsWhoDroppedOutARTInFirstThreeMonthsAfterInitiatedTreatment")
  public CohortDefinition
      findAllPatientsWhoDroppedOutARTInFirstThreeMonthsAfterInitiatedTreatment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Patients who dropped out ART in first three months after initiated ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findAllPatientsWhoDroppedOutARTInFirstThreeMonthsAfterInitiatedTreatment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findAllPatientsWhoDroppedOutARTInFirstThreeMonthsBeforeInitiatedTreatment")
  public CohortDefinition
      findAllPatientsWhoDroppedOutARTInFirstThreeMonthsBeforeInitiatedTreatment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Patients who dropped out ART in first three months after initiated ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findAllPatientsWhoDroppedOutARTInFirstThreeMonthsBeforeInitiatedTreatment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findAllPatientsWhoDroppedOutARTInFirstSixMonthsBeforeInitiatedTreatment")
  public CohortDefinition
      findAllPatientsWhoDroppedOutARTInFirstSixMonthsBeforeInitiatedTreatment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Patients who dropped out ART in first three months after initiated ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findAllPatientsWhoDroppedOutARTInFirstSixMonthsBeforeInitiatedTreatment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findAllPatientsWhoDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedART")
  public CohortDefinition
      findAllPatientsWhoDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out ART during the first six months after initiated ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findAllPatientsWhoDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedART;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation")
  public CohortDefinition
      findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out ART during the last six months after initiated ART First Line");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsDeclaredDeadAtTheEndOfSixMonthsAfterChangeRegimenInFirstLineART")
  public CohortDefinition
      findPatientsDeclaredDeadDuringTheFirstSixMonthsAfterChangeRegimenInFirstLineART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dead during the first six months after change regimen in First Line ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findPatientsDeclaredDeadDuringTheFirstSixMonthsAfterChangeRegimenInFirstLineART;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoDroppedOutARTDuringTheFirstSixMonthsOfRegimenChangeInFirstLine")
  public CohortDefinition
      findPatientsWhoDroppedOutARTDuringTheFirstSixMonthsOfRegimenChangeInFirstLine() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out in the first six months after change regimen in First Line ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findPatientsWhoDroppedOutARTDuringTheFirstSixMonthsOfRegimenChangeInFirstLine;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoDeadARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART")
  public CohortDefinition
      findPatientsWhoDeadARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dead during the first six months after change regimen in Second Line ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findPatientsWhoDeadARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART")
  public CohortDefinition
      findPatientsDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who dropped out during the first six months after change regimen in Second Line ART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P3QueriesInterface.QUERY
            .findPatientsDroppedOutARTDuringTheFirstSixMonthsAfterInitiatedSecondLineART;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod")
  public CohortDefinition findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Patients who abandoned ART three months before last clinical consultation of period");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPatientsWhoDroppedOutARTThreeMonthsBeforeLastConsultationPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeridod")
  public CohortDefinition findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Pregnant patients who received CV result in the avaliation period");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriodAnd33DaysAfterRequestLaboratoryInvestigations")
  public CohortDefinition
      findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriodAnd33DaysAfterRequestLaboratoryInvestigations() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Pregnant patients who received CV result in the avaliation period and request laboratory investigations in 33 days before the result");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13P2QueriesInterface.QUERY
            .findPregnantPatientsWhoReceivedViralLoadResultInTheAvaliationPeriodAnd33DaysAfterRequestLaboratoryInvestigations;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsDiagnosedWithActiveTBDuring7MonthsAfterInitiatedTPIINHOr3HPCategory7")
  public CohortDefinition
      findPatientsDiagnosedWithActiveTBDuring7MonthsAfterInitiatedTPIINHOr3HPCategory7(
          TPTType tptType) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsDiagnosedWithActiveTBDuring7MonthsAfterInitiatedTPIINHOr3HPCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsDiagnosedWithActiveTBDuring7MonthsAfterInitiatedTPIINHOr3HPCategory7(
                tptType);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithPositiveTBScreeningDuring6MonthsAfterInitiatedINHOr3HPCategory7")
  public CohortDefinition
      findPatientsWithPositiveTBScreeningDuring6MonthsAfterInitiatedINHOr3HPCategory7(
          TPTType tptType) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWithPositiveTBScreeningDuring6MonthsAfterInitiatedINHOr3HPCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findPatientsWithPositiveTBScreeningDuring6MonthsAfterInitiatedINHOr3HPCategory7(
                tptType);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "finPatientsWhoHadTBTreatmentDuring6MonthsAfterInitiatedINHOr3HPCategory7")
  public CohortDefinition finPatientsWhoHadTBTreatmentDuring6MonthsAfterInitiatedINHOr3HPCategory7(
      TPTType tptType) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("finPatientsWhoHadTBTreatmentDuring6MonthsAfterInitiatedINHOr3HPCategory7");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .finPatientsWhoHadTBTreatmentDuring6MonthsAfterInitiatedINHOr3HPCategory7(tptType);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findAllPatientWhoAreDeadByEndOfRevisonPeriod")
  public CohortDefinition findAllPatientWhoAreDeadByEndOfRevisonPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findAllPatientWhoAreDeadByEndOfRevisonPeriod;

    definition.setQuery(query);

    return definition;
  }
}
