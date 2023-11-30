/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.PrepCtQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.PrepReasonInterruptionType;
import org.openmrs.module.eptsreports.reporting.library.queries.ReasonsOfPrepInterruptionQuery;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** DataSet for PREP CT */
@Component
public class PrepCtCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  @Autowired private PrepNewCohortQueries prepNewCohortQueries;

  /**
   * Build PrepCt composition cohort definition
   *
   * @param cohortName Cohort name
   * @return CompositionQuery
   */
  public CohortDefinition getClientsNewlyEnrolledInPrep() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("PREP CT");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsNewlyEnrolledInPrep",
                PrepCtQueries.QUERY.findClientsNewlyEnrolledInPrep),
            mappings));

    prepCtCompositionCohort.addSearch(
        "ATLEAST-ONE-FOLLOWUP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithAtLeastOneFollowUpVisitInFichaSeguimento",
                PrepCtQueries.QUERY.findClientsWithAtLeastOneFollowUpVisitInFichaSeguimento),
            mappings));

    prepCtCompositionCohort.addSearch(
        "TRANSFERED-IN-BEFORE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoWhereTransferredInBeforeReportingPeriod",
                PrepCtQueries.QUERY.findClientsWhoWhereTransferredInBeforeReportingPeriod),
            mappings));

    prepCtCompositionCohort.addSearch(
        "TRANSFERED-IN-DURING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoWhereTransferredInDuringReportingPeriod",
                PrepCtQueries.QUERY.findClientsWhoWhereTransferredInDuringReportingPeriod),
            mappings));

    prepCtCompositionCohort.addSearch(
        "REINITIATED-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoReinitiatedPrep", PrepCtQueries.QUERY.findClientsWhoReinitiatedPrep),
            mappings));

    prepCtCompositionCohort.addSearch(
        "CONTINUE-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoReinitiatedPrep", PrepCtQueries.QUERY.findClientsWhoContinuePrep),
            mappings));

    prepCtCompositionCohort.addSearch(
        "PREP-NEW",
        EptsReportUtils.map(prepNewCohortQueries.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.setCompositionString(
        "(((START-PREP OR TRANSFERED-IN-BEFORE) AND ATLEAST-ONE-FOLLOWUP) OR TRANSFERED-IN-DURING OR REINITIATED-PREP OR CONTINUE-PREP) NOT PREP-NEW");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPositiveTestResult() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("POSITIVE TEST RESULT");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "POSITIVE-TEST",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPositiveTestResult",
                PrepCtQueries.QUERY.findClientsWithPositiveTestResult),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND POSITIVE-TEST");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithNegativeTestResult() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("POSITIVE TEST RESULT");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "NEGATIVE-TEST",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPositiveTestResult",
                PrepCtQueries.QUERY.findClientsWithNegativeTestResult),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND NEGATIVE-TEST");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithIndeterminateTestResult() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("INDETERMINATE TEST RESULT");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "POSITIVE", EptsReportUtils.map(this.getClientsWithPositiveTestResult(), mappings));

    prepCtCompositionCohort.addSearch(
        "NEGATIVE", EptsReportUtils.map(this.getClientsWithNegativeTestResult(), mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP NOT (POSITIVE OR NEGATIVE)");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPregnancyStatusDuringReportingPeriod() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("PREGNANT");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPregnancyStatusDuringReportingPeriod",
                PrepCtQueries.QUERY.findClientsWithPregnancyStatusDuringReportingPeriod),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND PREGNANT");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithBreastfeedingStatusDuringReportingPeriod() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("BREASTFEEDING");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithBreastfeedingStatusDuringReportingPeriod",
                PrepCtQueries.QUERY.findClientsWithBreastfeedingStatusDuringReportingPeriod),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND BREASTFEEDING");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPrepInterruptionReasonInfected() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("INFECTED");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "INFECTED",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPrepInterruptionReasonInfected",
                ReasonsOfPrepInterruptionQuery.findPatientsWhoInterruptPrep(
                    PrepReasonInterruptionType.INFECTED)),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND INFECTED");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPrepInterruptionReasonSideEffects() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("SIDE-EFFECTS");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "SIDE-EFFECTS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPrepInterruptionReasonSideEffects",
                ReasonsOfPrepInterruptionQuery.findPatientsWhoInterruptPrep(
                    PrepReasonInterruptionType.COLATERAL_DAMAGES)),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND SIDE-EFFECTS");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPrepInterruptionReasonNoRisks() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("NO-RISKS");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "NO-RISKS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPrepInterruptionReasonNoRisks",
                ReasonsOfPrepInterruptionQuery.findPatientsWhoInterruptPrep(
                    PrepReasonInterruptionType.NO_RISKS)),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND NO-RISKS");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPrepInterruptionReasonUserPreference() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("USER-PREFERENCE");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "USER-PREFERENCE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPrepInterruptionReasonUserPreference",
                ReasonsOfPrepInterruptionQuery.findPatientsWhoInterruptPrep(
                    PrepReasonInterruptionType.USER_PREFERENCE)),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND USER-PREFERENCE");

    return prepCtCompositionCohort;
  }

  public CohortDefinition getClientsWithPrepInterruptionReasonUserOther() {
    final CompositionCohortDefinition prepCtCompositionCohort = new CompositionCohortDefinition();

    prepCtCompositionCohort.setName("OTHER");
    prepCtCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepCtCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepCtCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepCtCompositionCohort.addSearch(
        "OTHER",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPrepInterruptionReasonUserOther",
                ReasonsOfPrepInterruptionQuery.findPatientsWhoInterruptPrep(
                    PrepReasonInterruptionType.OTHER)),
            mappings));

    prepCtCompositionCohort.setCompositionString("START-PREP AND OTHER");

    return prepCtCompositionCohort;
  }
}
