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
import org.openmrs.module.eptsreports.reporting.library.queries.TxCurrQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TxCurrQueries.QUERY.DispensationIntervalType;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

/** Defines all of the TxCurrCohortQueries we want to expose for EPTS */
@Component
public class TxCurrCohortQueries {

  @DocumentedDefinition(value = "patientsWhoAreActiveOnART")
  public CohortDefinition findPatientsWhoAreActiveOnART() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsWhoAreActiveOnART");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(TxCurrQueries.QUERY.findPatientsWhoAreCurrentlyEnrolledOnART);

    return definition;
  }

  public CohortDefinition getPatientsOnArtOnArvDispenseForLessThan3Months() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients On Art On ARV Dispensation less than 3 months");
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String mapping = "endDate=${endDate},location=${location}";
    cd.addSearch(
        "patientsWhoAreActiveOnART",
        EptsReportUtils.map(this.findPatientsWhoAreActiveOnART(), mapping));
    cd.addSearch(
        "arvDispenseForLessThan3Months",
        EptsReportUtils.map(this.findPatientsOnArtOnArvDispenseForLessThan3Months(), mapping));

    cd.setCompositionString("patientsWhoAreActiveOnART AND arvDispenseForLessThan3Months");
    return cd;
  }

  public CohortDefinition getPatientsOnArtOnArvDispenseBetween3And5Months() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients On Art On ARV Dispensation Between 3 and 5 Months");
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String mapping = "endDate=${endDate},location=${location}";

    cd.addSearch(
        "patientsWhoAreActiveOnART",
        EptsReportUtils.map(this.findPatientsWhoAreActiveOnART(), mapping));

    cd.addSearch(
        "arvDispenseBetween3And5Months",
        EptsReportUtils.map(this.findPatientsOnArtOnArvDispenseBetween3And5Months(), mapping));

    cd.setCompositionString("patientsWhoAreActiveOnART AND arvDispenseBetween3And5Months");
    return cd;
  }

  public CohortDefinition getPatientsOnArtOnArvDispenseFor6OrMoreMonths() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients On Art On ARV Dispensation For 6 Or More Months");
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String mapping = "endDate=${endDate},location=${location}";

    cd.addSearch(
        "patientsWhoAreActiveOnART",
        EptsReportUtils.map(this.findPatientsWhoAreActiveOnART(), mapping));

    cd.addSearch(
        "arvDispenseFor6OrMoreMonths",
        EptsReportUtils.map(this.findPatientsOnArtOnArvDispenseFor6OrMoreMonths(), mapping));

    cd.setCompositionString("patientsWhoAreActiveOnART AND arvDispenseFor6OrMoreMonths");
    return cd;
  }

  @DocumentedDefinition(value = "findPatientsOnArtOnArvDispenseFor6OrMoreMonths")
  public CohortDefinition findPatientsOnArtOnArvDispenseFor6OrMoreMonths() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnArtOnArvDispenseFor6OrMoreMonths");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxCurrQueries.QUERY.findPatientsWhoAreInDispenseType(DispensationIntervalType.SEMI_ANNUAL));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsOnArtOnArvDispenseBetween3And5Months")
  public CohortDefinition findPatientsOnArtOnArvDispenseBetween3And5Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnArtOnArvDispenseBetween3And5Months");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxCurrQueries.QUERY.findPatientsWhoAreInDispenseType(DispensationIntervalType.QUARTERLY));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsOnArtOnArvDispenseForLessThan3Months")
  public CohortDefinition findPatientsOnArtOnArvDispenseForLessThan3Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnArtOnArvDispenseForLessThan3Months");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxCurrQueries.QUERY.findPatientsWhoAreInDispenseType(DispensationIntervalType.MONTHLY));

    return definition;
  }
}
