/*
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.AgeRange;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IMR1BCohortQueries {

  private static final String IMR1B = "IMR1B/PATIENTS_WHO_ARE_NEWLY_ENROLLED_ON_ART_IMR1B.sql";
  private static final String IMR1B2 =
      "IMR1B/PATIENTS_WHO_ARE_NEWLY_ENROLLED_ON_ART_CHILDREN_IMR1B2.sql";

  private static final String IMR1B3 =
      "IMR1B/PATIENTS_WHO_ARE_NEWLY_ENROLLED_ON_ART_WITH_ENROLLMENT_DATE_GREATHER_THAN_ARTSTART_DATE.sql";

  @Autowired private IMR1CohortQueries iMR1CohortQueries;

  @DocumentedDefinition(value = "PatientsNewlyEnrolledOnArtCare")
  public CohortDefinition getPatientsNewlyEnrolledOnArtCare() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "NEWLY-ENROLLED",
        EptsReportUtils.map(
            this.iMR1CohortQueries.getAllPatientsEnrolledInArtCareDuringReportingPeriod(),
            mappings));

    compsitionDefinition.addSearch(
        "TRANSFERRED-IN",
        EptsReportUtils.map(
            iMR1CohortQueries.getAllPatientsTransferredInByEndReportingDate(), mappings));

    compsitionDefinition.addSearch(
        "ENROLLMENT-GREATER-THAN-START-DATE",
        EptsReportUtils.map(
            this
                .getPatientsNewlyEnrolledOnArtTreatmentAndInitiatedTreatmentWithEnrollmentDateGreaterThanArtStartDateAMonthPriorToTheReporingPeriod(),
            mappings));

    compsitionDefinition.setCompositionString(
        "NEWLY-ENROLLED NOT (TRANSFERRED-IN OR ENROLLMENT-GREATER-THAN-START-DATE)");

    return compsitionDefinition;
  }

  public CohortDefinition getChildrenNewlyEnrolledOnArtCare() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.getPatientsNewlyEnrolledOnArtCare(), mappings));

    compsitionDefinition.addSearch(
        "CHILDREN",
        EptsReportUtils.map(
            iMR1CohortQueries.findPatientsAgeByTheDateOfEnrollmentOnARTCare(AgeRange.CHILDREN),
            mappings));

    compsitionDefinition.addSearch(
        "ADULT",
        EptsReportUtils.map(
            iMR1CohortQueries.findPatientsAgeByTheDateOfEnrollmentOnARTCare(AgeRange.ADULT),
            mappings));

    compsitionDefinition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(iMR1CohortQueries.getAllPatientsWhoArePregnantInAPeriod(), mappings));

    compsitionDefinition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(iMR1CohortQueries.getAllPatientsWhoAreBreastfeeding(), mappings));

    compsitionDefinition.setCompositionString(
        "((DENOMINATOR AND CHILDREN) NOT(PREGNANT OR BREASTFEEDING)) NOT ADULT ");

    return compsitionDefinition;
  }

  @DocumentedDefinition(
      value = "PatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastFeedingDenominator")
  public CohortDefinition
      getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastfeedingDenominator() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care excluding Pregnants");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.getPatientsNewlyEnrolledOnArtCare(), mappings));

    compsitionDefinition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(iMR1CohortQueries.getAllPatientsWhoArePregnantInAPeriod(), mappings));

    compsitionDefinition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(iMR1CohortQueries.getAllPatientsWhoAreBreastfeeding(), mappings));

    compsitionDefinition.addSearch(
        "CHILDREN",
        EptsReportUtils.map(
            iMR1CohortQueries.findPatientsAgeByTheDateOfEnrollmentOnARTCare(AgeRange.CHILDREN),
            mappings));

    compsitionDefinition.setCompositionString(
        "(DENOMINATOR NOT ((PREGNANT OR BREASTFEEDING))) NOT CHILDREN ");

    return compsitionDefinition;
  }

  @DocumentedDefinition(
      value = "PatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastFeedingNumerator")
  public CohortDefinition
      getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreasFeedingNumerator() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care excluding Pregnants");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(
            this.getPatientsNewlyEnrolledOnArtWhoInitiatedArtTreatment(), mappings));

    compsitionDefinition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            iMR1CohortQueries
                .getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastfeedingDenominator(),
            mappings));

    compsitionDefinition.setCompositionString("NUMERATOR AND DENOMINATOR");

    return compsitionDefinition;
  }

  @DocumentedDefinition(value = "PatientsNewlyEnrolledOnArtCareNumerator")
  public CohortDefinition getPatientsNewlyEnrolledOnArtWhoInitiatedArtTreatment() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "NEWLY-ENROLLED",
        EptsReportUtils.map(
            this
                .getPatientsNewlyEnrolledOnArtTreatmentAndInitiatedTreatmentAMonthPriorToTheReporingPeriod(),
            mappings));

    compsitionDefinition.addSearch(
        "TRANSFERRED-IN",
        EptsReportUtils.map(
            this.iMR1CohortQueries.getAllPatientsTransferredInByEndReportingDate(), mappings));

    compsitionDefinition.setCompositionString("NEWLY-ENROLLED NOT TRANSFERRED-IN");

    return compsitionDefinition;
  }

  @DocumentedDefinition(value = "ChildrenNewlyEnrolledOnArtWhoInitiatedArtTreatment")
  public CohortDefinition getChildrenNewlyEnrolledOnArtWhoInitiatedArtTreatment() {

    final CompositionCohortDefinition compsitionDefinition = new CompositionCohortDefinition();
    compsitionDefinition.setName("Patients newly enrolled on ART Care");
    final String mappings = "endDate=${endDate},location=${location}";

    compsitionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compsitionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compsitionDefinition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(
            this.getPatientsNewlyEnrolledOnArtWhoInitiatedArtTreatment(), mappings));

    compsitionDefinition.addSearch(
        "CHILDREN",
        EptsReportUtils.map(
            iMR1CohortQueries.findPatientsAgeByTheDateOfEnrollmentOnARTCare(AgeRange.CHILDREN),
            mappings));

    compsitionDefinition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(iMR1CohortQueries.getChildrenNewlyEnrolledOnArtCare(), mappings));

    compsitionDefinition.setCompositionString("NUMERATOR AND CHILDREN AND DENOMINATOR  ");

    return compsitionDefinition;
  }

  @DocumentedDefinition(value = "PatientsNewlyEnrolledOnArtTreatmentAMonthPriorToTheReporingPeriod")
  private CohortDefinition
      getPatientsNewlyEnrolledOnArtTreatmentAndInitiatedTreatmentAMonthPriorToTheReporingPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("PatientsNewlyEnrolledOnArtTreatmentAMonthPriorToTheReporingPeriod Cohort");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(IMR1B));

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsNewlyEnrolledOnArtTreatmentAndInitiatedTreatmentWithEnrollmentDateGreaterThanArtStartDateAMonthPriorToTheReporingPeriod")
  private CohortDefinition
      getPatientsNewlyEnrolledOnArtTreatmentAndInitiatedTreatmentWithEnrollmentDateGreaterThanArtStartDateAMonthPriorToTheReporingPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName(
        "PatientsNewlyEnrolledOnArtTreatmentAMonthPriorToTheReporingPeriod with enrollment date greater than start date Cohort");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(IMR1B3));

    return definition;
  }

  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnArtByAgeRange(final AgeRange ageRange) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("patientsPregnantEnrolledOnARTCare");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = EptsQuerysUtils.loadQuery(IMR1B2);
    query = String.format(query, ageRange.getMin(), ageRange.getMax());

    definition.setQuery(query);

    return definition;
  }
}
