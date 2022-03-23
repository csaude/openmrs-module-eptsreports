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
import org.openmrs.module.eptsreports.reporting.library.queries.Eri4MonthsQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Defines @{@link org.openmrs.module.reporting.cohort.definition.CohortDefinition} for pepfar early
 * indicator report
 */
@Component
public class Eri4MonthsCohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private TxNewCohortQueries txNewCohortQueries;

  public CohortDefinition findPatientsWhoAreLostFollowUp() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Patients who are a Lost Follow Up");

    definition.addParameter(new Parameter("cohortStartDate", "Cohort Start Date", Date.class));
    definition.addParameter(new Parameter("cohortEndDate", "Cohort End Date", Date.class));
    definition.addParameter(new Parameter("reportingEndDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    definition.addSearch(
        "IART",
        EptsReportUtils.map(
            this.txNewCohortQueries.getTxNewCompositionCohort("patientNewlyEnrolledInART"),
            "startDate=${cohortStartDate},endDate=${cohortEndDate},location=${location}"));

    definition.addSearch(
        "LFU",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "patientsWhoAreLostFollowUp", Eri4MonthsQueries.findPatientsWhoAreLostFollowUp()),
            "endDate=${reportingEndDate},location=${location}"));

    definition.setCompositionString("IART AND LFU");

    return definition;
  }
}
