package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.GenericMQQueryIntarface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13Section1QueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.stereotype.Component;

@Component
public class MQAgeDimensions {
  // Dimension for last clinical consultation

  public CohortDefinitionDimension getDimensionForLastClinicalConsultation() {

    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation15Plus(15),
            mappings));

    dimension.addCohortDefinition(
        "15PlusOrBreastfeeding",
        EptsReportUtils.map(
            this
                .findPatientsWithLastClinicalConsultationAdultOrPregnantOrBreatfeedingDenominatorB1AgeCalculation15Plus(
                    15),
            mappings));

    dimension.addCohortDefinition(
        "0-4",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(0, 4),
            mappings));

    dimension.addCohortDefinition(
        "5-9",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(5, 9),
            mappings));

    dimension.addCohortDefinition(
        "10-14",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(10, 14),
            mappings));

    dimension.addCohortDefinition(
        "2-14",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(2, 14),
            mappings));

    dimension.addCohortDefinition(
        "0-14",
        EptsReportUtils.map(
            this.findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(0, 14),
            mappings));

    return dimension;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation15Plus")
  public CohortDefinition findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation15Plus(
      int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation15Plus(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWithLastClinicalConsultationAdultOrPregnantOrBreatfeedingDenominatorB1AgeCalculation15Plus")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationAdultOrPregnantOrBreatfeedingDenominatorB1AgeCalculation15Plus(
          int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section1QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationAdultOrPregnantOrBreatfeedingDenominatorB1AgeCalculation15Plus(
                age);

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

  // Dimension Age for new enrrolment on ART

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAdulOrChildren")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAdult(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTBiggerThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAdultOrBreastfeeding")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAdultOrBreastfeeding(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTBiggerThanParamOrBreastfeeding(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnART12MonthsAgoOrBreastfeeding")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnART12MonthsAgoOrBreastfeeding(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreNewlyEnrolledOnART12MonthsAgoOrBreastfeeding");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnART12MonthsAgoOrBreastfeeding(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAdulOrChildren")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTChildren(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTLessThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(
            startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonth(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonth(
            startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonthEndRevisionDate(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonthEndRevisionDate(
                startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension getDimensionForPatientsWhoAreNewlyEnrolledOnART() {

    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15+", EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAdult(15), mappings));

    dimension.addCohortDefinition(
        "15PlusOrBreastfeeding",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnARTByAdultOrBreastfeeding(15), mappings));

    dimension.addCohortDefinition(
        "15PlusOnART12mAgoOrBreastfeeding",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnART12MonthsAgoOrBreastfeeding(15), mappings));

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTChildren(15), mappings));

    dimension.addCohortDefinition(
        "1-14",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(1, 14), mappings));

    dimension.addCohortDefinition(
        "2-14",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(2, 14), mappings));

    dimension.addCohortDefinition(
        "0-4",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(0, 4), mappings));

    dimension.addCohortDefinition(
        "5-9",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(5, 9), mappings));

    dimension.addCohortDefinition(
        "3-14",
        EptsReportUtils.map(this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(3, 14), mappings));

    dimension.addCohortDefinition(
        "<9MONTHS",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonth(0, 9), mappings));

    dimension.addCohortDefinition(
        "0-18M",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonth(0, 18), mappings));

    dimension.addCohortDefinition(
        "8-9RD",
        EptsReportUtils.map(
            this.calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(8, 9), mappings));

    dimension.addCohortDefinition(
        "10-14RD",
        EptsReportUtils.map(
            this.calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(10, 14), mappings));

    return dimension;
  }

  // Dimension Age for new enrrolment on ART Until RevisionDate

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAdulOrChildren")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateByAdult(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateBiggerThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAdulOrChildren")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateChildren(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateLessThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateByAgeRenge(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateByAgeRenge(startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension
      getDimensionForPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDate() {

    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateByAdult(14), mappings));

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(
            this.findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateChildren(15), mappings));

    return dimension;
  }

  // Dimension Age for Viral Load Result

  @DocumentedDefinition(value = "findPAtientWithCVOver1000CopiesAdult")
  public CohortDefinition findPAtientWithCVOver1000CopiesAdult(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPAtientWithCVOver1000CopiesBiggerThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedTreatmentInClinicalConsultation")
  public CohortDefinition findAdultPatientsWhoReinitiatedTreatmentInClinicalConsultation(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoReinitiatedTreatmentInClinicalConsultation(
            age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedTreatmentInClinicalConsultation")
  public CohortDefinition findAdultPatientsWhoReinitiatedTreatmentInClinicalConsultation(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoReinitiatedTreatmentInClinicalConsultation(
            startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4")
  public CohortDefinition
      findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(
                age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4")
  public CohortDefinition
      findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(
          int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(
                startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedTreatmentInClinicalConsultation")
  public CohortDefinition findChildrenPatientsWhoReinitiatedTreatmentInClinicalConsultation(
      int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoReinitiatedTreatmentInClinicalConsultation(
            age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedTreatmentInClinicalConsultation")
  public CohortDefinition findChildrenPatientsWhoReinitiatedTreatmentInClinicalConsultation(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPatientsWhoReinitiatedTreatmentInClinicalConsultation(
            startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPAtientWithCVOver1000CopiesBiggerThanParamOrBreastfeeding")
  public CohortDefinition findPAtientWithCVOver1000CopiesBiggerThanParamOrBreastfeeding(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPAtientWithCVOver1000CopiesBiggerThanParamOrBreastfeeding(
            age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPAtientWithCVOver1000CopiesChildren")
  public CohortDefinition findPAtientWithCVOver1000CopiesChildren(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = GenericMQQueryIntarface.QUERY.findPAtientWithCVOver1000CopiesLessThanParam(age);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPAtientWithCVOver1000CopiesByAgeRenge")
  public CohortDefinition findPAtientWithCVOver1000CopiesByAgeRenge(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.findPAtientWithCVOver1000CopiesAgeRenge(startAge, endAge);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeByAgeRenge")
  public CohortDefinition calculateDefaulteAgeByAgeRenge(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsAgeRange;

    String finalQuery = String.format(query, startAge, endAge);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeByAgeRenge")
  public CohortDefinition findPatientsAgeRangeEndRevisionDate(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsAgeRangeEndRevisionDate;

    String finalQuery = String.format(query, startAge, endAge);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeBiggerThan")
  public CohortDefinition calculateDefaulteAgeBiggerThanEndRevisionDate(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsBiggerThanRevisionDate;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeLessThan")
  public CohortDefinition calculateDefaulteAgeLessThanEndRevisionDate(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsLessThanRevisionDate;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeBiggerThan")
  public CohortDefinition calculateDefaulteAgeBiggerThan(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsBiggerThan;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeBiggerThanParamSecondLine")
  public CohortDefinition calculateDefaulteAgeBiggerThanParamSecondLine(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorBiggerThan;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodOrAdultPatientsInART")
  public CohortDefinition
      findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodOrAdultPatientsInART(
          int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodOrAdultPatientsInART;

    String finalQuery = String.format(query, age, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeBiggerThanParamSecondLine")
  public CohortDefinition calculateDefaulteAgeLessThanParamSecondLine(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQQueriesInterface.QUERY
            .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorLessThan;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

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

  @DocumentedDefinition(value = "calculateDefaulteAgeBiggerThan")
  public CohortDefinition calculateDefaulteAgeBiggerThanBreastfeeding(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsBiggerThanBreastfeeding;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateDefaulteAgeLessThan")
  public CohortDefinition calculateDefaulteAgeLessThan(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQQueriesInterface.QUERY.findPatientsLessThan;

    String finalQuery = String.format(query, age);

    definition.setQuery(finalQuery);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTheFirstConsultationDateBiggerThanParam")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateBiggerThanParam(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTheFirstConsultationDateBiggerThanParam(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTheFirstConsultationDateBiggerThanParam")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateBiggerThanParamFC(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTheFirstConsultationDateBiggerThanParamFC(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding(
      int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTheFirstConsultationDateLessThanParam")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateLessThanParam(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateLessThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTheFirstConsultationDateLessThanParam(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTheFirstConsultationDateLessThanParamFC")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateLessThanParamFC(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateLessThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY
            .findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateLessThanParamFC(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge")
  public CohortDefinition calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(
            startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension getDimensionAgeOnTheFirstConsultation() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings = "endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(
            this.calculateAgeOnTheFirstConsultationDateLessThanParamFC(15), mappings));

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(
            this.calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding(15),
            mappings));

    return dimension;
  }

  public CohortDefinitionDimension getDimensionAgeEndInclusionDate() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "<15", EptsReportUtils.map(this.calculateDefaulteAgeLessThan(15), mappings));

    dimension.addCohortDefinition(
        "15+", EptsReportUtils.map(this.calculateDefaulteAgeBiggerThan(15), mappings));

    dimension.addCohortDefinition(
        "15PlusSecondLine",
        EptsReportUtils.map(this.calculateDefaulteAgeBiggerThanParamSecondLine(15), mappings));

    dimension.addCohortDefinition(
        "AdultsOr15PlusSecondLine",
        EptsReportUtils.map(
            this
                .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodOrAdultPatientsInART(
                    15),
            mappings));

    dimension.addCohortDefinition(
        "15PlusOrBreastfeeding",
        EptsReportUtils.map(this.calculateDefaulteAgeBiggerThanBreastfeeding(15), mappings));

    dimension.addCohortDefinition(
        "2+", EptsReportUtils.map(this.calculateDefaulteAgeBiggerThan(2), mappings));

    dimension.addCohortDefinition(
        "0-14", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(0, 14), mappings));

    dimension.addCohortDefinition(
        "0-14SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(0, 14), mappings));

    dimension.addCohortDefinition(
        "2-14", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(2, 14), mappings));

    dimension.addCohortDefinition(
        "2-14SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(2, 14), mappings));

    dimension.addCohortDefinition(
        "5-9", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(5, 9), mappings));
    dimension.addCohortDefinition(
        "5-9SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(5, 9), mappings));

    dimension.addCohortDefinition(
        "3-14", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(3, 14), mappings));
    dimension.addCohortDefinition(
        "3-14SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(3, 14), mappings));

    dimension.addCohortDefinition(
        "0-4", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(0, 4), mappings));
    dimension.addCohortDefinition(
        "0-4SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(0, 4), mappings));

    dimension.addCohortDefinition(
        "10-14", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(10, 14), mappings));
    dimension.addCohortDefinition(
        "10-14SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(10, 14), mappings));

    dimension.addCohortDefinition(
        "2-9", EptsReportUtils.map(this.calculateDefaulteAgeByAgeRenge(2, 9), mappings));
    dimension.addCohortDefinition(
        "2-9SL",
        EptsReportUtils.map(this.calculateDefaulteAgeSecondLineByAgeRenge(2, 9), mappings));

    return dimension;
  }

  public CohortDefinitionDimension getDimensionAgeEndInclusionDateEndRevisionDate() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "<1", EptsReportUtils.map(this.calculateDefaulteAgeLessThanEndRevisionDate(1), mappings));

    dimension.addCohortDefinition(
        "0-4", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(0, 4), mappings));

    dimension.addCohortDefinition(
        "1-4", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(1, 4), mappings));

    dimension.addCohortDefinition(
        "2-14", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(2, 14), mappings));

    dimension.addCohortDefinition(
        "5-9", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(5, 9), mappings));
    dimension.addCohortDefinition(
        "10-14", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(10, 14), mappings));

    dimension.addCohortDefinition(
        "15-19", EptsReportUtils.map(this.findPatientsAgeRangeEndRevisionDate(15, 19), mappings));

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(this.calculateDefaulteAgeBiggerThanEndRevisionDate(15), mappings));

    dimension.addCohortDefinition(
        "20+",
        EptsReportUtils.map(this.calculateDefaulteAgeBiggerThanEndRevisionDate(20), mappings));

    return dimension;
  }

  public CohortDefinitionDimension getDimensionForPatientsPatientWithCVOver1000Copies() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15+", EptsReportUtils.map(this.findPAtientWithCVOver1000CopiesAdult(15), mappings));

    dimension.addCohortDefinition(
        "15PlusBreastfeeding",
        EptsReportUtils.map(
            this.findPAtientWithCVOver1000CopiesBiggerThanParamOrBreastfeeding(15), mappings));

    dimension.addCohortDefinition(
        "15-", EptsReportUtils.map(this.findPAtientWithCVOver1000CopiesChildren(15), mappings));

    return dimension;
  }

  public CohortDefinitionDimension
      getDimensionForPatientsWhoReinitiatedTreatmentInClinicalConsultation() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("patientsPregnantEnrolledOnART");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(
            this.findAdultPatientsWhoReinitiatedTreatmentInClinicalConsultation(15), mappings));

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(
            this.findChildrenPatientsWhoReinitiatedTreatmentInClinicalConsultation(0, 14),
            mappings));

    dimension.addCohortDefinition(
        "CD4-15+",
        EptsReportUtils.map(
            this.findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(
                15),
            mappings));

    dimension.addCohortDefinition(
        "CD4-15-",
        EptsReportUtils.map(
            this.findPatientsWhoReinitiatedTreatmentInTheSameClinicalConsultationMarkedAsRequestCD4(
                0, 14),
            mappings));

    return dimension;
  }

  @DocumentedDefinition(value = "calculateAgeOnPrensutiveTBLessThanParamByAgeRenge")
  public CohortDefinition calculateAgeOnPrensutiveTBLessThanParamByAgeRenge(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnPrensutiveTBLessThanParamByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnPrensutiveTBLessThanParamByAgeRenge(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnPrensutiveBiggerThanParam")
  public CohortDefinition calculateAgeOnPrensutiveBiggerThanParam(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnPrensutiveBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = GenericMQQueryIntarface.QUERY.calculateAgeOnPrensutiveBiggerThanParam(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnPrensutiveTBByAgeRenge")
  public CohortDefinition calculateAgeOnPrensutiveTBByAgeRenge(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnPrensutiveTBByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnPrensutiveTBByAgeRenge(startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension getDimensionAgeOnThePresuntiveTB() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("getDimensionAgeOnThePresuntiveTB");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(this.calculateAgeOnPrensutiveTBLessThanParamByAgeRenge(15), mappings));

    dimension.addCohortDefinition(
        "15+", EptsReportUtils.map(this.calculateAgeOnPrensutiveBiggerThanParam(15), mappings));

    dimension.addCohortDefinition(
        "0-14", EptsReportUtils.map(this.calculateAgeOnPrensutiveTBByAgeRenge(0, 14), mappings));

    return dimension;
  }

  @DocumentedDefinition(value = "calculateAgeOnGeneXpertRequestLessThanParamByAgeRenge")
  public CohortDefinition calculateAgeOnGeneXpertRequestLessThanParamByAgeRenge(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnGeneXpertRequestLessThanParamByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnGeneXpertRequestLessThanParamByAgeRenge(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnGeneXpertRequestBiggerThanParam")
  public CohortDefinition calculateAgeOnGeneXpertRequestBiggerThanParam(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnGeneXpertRequestBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = GenericMQQueryIntarface.QUERY.calculateAgeOnGeneXpertRequestBiggerThanParam(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnGeneXpertRequestByAgeRange")
  public CohortDefinition calculateAgeOnGeneXpertRequestByAgeRange(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnPrensutiveTBByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnGeneXpertRequestByAgeRange(startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension getDimensionAgeOnGeneXpertRequest() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("getDimensionAgeOnGeneXpertRequest");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(
            this.calculateAgeOnGeneXpertRequestLessThanParamByAgeRenge(15), mappings));

    dimension.addCohortDefinition(
        "15+",
        EptsReportUtils.map(this.calculateAgeOnGeneXpertRequestBiggerThanParam(15), mappings));

    dimension.addCohortDefinition(
        "0-14",
        EptsReportUtils.map(this.calculateAgeOnGeneXpertRequestByAgeRange(0, 14), mappings));

    return dimension;
  }

  @DocumentedDefinition(value = "calculateAgeOnTBDiagnosticLessThanParamByAgeRenge")
  public CohortDefinition calculateAgeOnTBDiagnosticLessThanParamByAgeRenge(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTBDiagnosticLessThanParamByAgeRenge");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTBDiagnosticLessThanParamByAgeRenge(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTBDiagnosticBiggerThanParam")
  public CohortDefinition calculateAgeOnTBDiagnosticBiggerThanParam(int age) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTBDiagnosticBiggerThanParam");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = GenericMQQueryIntarface.QUERY.calculateAgeOnTBDiagnosticBiggerThanParam(age);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "calculateAgeOnTBDiagnosticByAgeRange")
  public CohortDefinition calculateAgeOnTBDiagnosticByAgeRange(int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("calculateAgeOnTBDiagnosticByAgeRange");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        GenericMQQueryIntarface.QUERY.calculateAgeOnTBDiagnosticByAgeRange(startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinitionDimension getDimensionAgeOnTBDiagnostic() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("getDimensionAgeOnGeneXpertRequest");
    dimension.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    dimension.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    dimension.addCohortDefinition(
        "15-",
        EptsReportUtils.map(this.calculateAgeOnTBDiagnosticLessThanParamByAgeRenge(15), mappings));

    dimension.addCohortDefinition(
        "15+", EptsReportUtils.map(this.calculateAgeOnTBDiagnosticBiggerThanParam(15), mappings));

    dimension.addCohortDefinition(
        "0-14", EptsReportUtils.map(this.calculateAgeOnTBDiagnosticByAgeRange(0, 14), mappings));

    return dimension;
  }
}
