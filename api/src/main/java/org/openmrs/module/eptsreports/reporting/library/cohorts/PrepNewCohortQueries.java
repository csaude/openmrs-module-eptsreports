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
import org.openmrs.module.eptsreports.reporting.library.queries.PrepNewQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.PrepPregnantBreasftfeedingQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants.PregnantOrBreastfeedingWomen;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** DataSet for PREP NEW */
@Component
public class PrepNewCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  @DocumentedDefinition(value = "findClientsNewlyEnrolledInPrepByOralPrepType")
  public CohortDefinition findClientsNewlyEnrolledInPrepByOralPrepType() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("findClientsNewlyEnrolledInPrepByOralPrepType");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = PrepNewQueries.QUERY.findClientsNewlyEnrolledInPrepByOralPrepType;
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findClientsNewlyEnrolledInPrepByInjectablePrepType")
  public CohortDefinition findClientsNewlyEnrolledInPrepByInjectablePrepType() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("findClientsNewlyEnrolledInPrepByOralPrepType");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = PrepNewQueries.QUERY.findClientsNewlyEnrolledInPrepByInjectablePrepType;
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findClientsNewlyEnrolledInPrepByOtherPrepType")
  public CohortDefinition findClientsNewlyEnrolledInPrepByOtherPrepType() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("findClientsNewlyEnrolledInPrepByOralPrepType");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = PrepNewQueries.QUERY.findClientsNewlyEnrolledInPrepByOtherPrepType;
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition getClientsNewlyEnrolledInPrep() {
    final CompositionCohortDefinition txNewCompositionCohort = new CompositionCohortDefinition();

    txNewCompositionCohort.setName("PREP NEW");
    txNewCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    txNewCompositionCohort.addSearch(
        "START-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsNewlyEnrolledInPrep",
                PrepNewQueries.QUERY.findClientsNewlyEnrolledInPrep),
            mappings));

    txNewCompositionCohort.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoWhereTransferredIn",
                PrepNewQueries.QUERY.findClientsWhoWhereTransferredIn),
            mappings));

    txNewCompositionCohort.setCompositionString("START-PREP NOT TRANSFERED-IN ");

    return txNewCompositionCohort;
  }

  public CohortDefinition getClientsWithPregnancyStatusDuringReportingPeriod() {
    final CompositionCohortDefinition prepNewCompositionCohort = new CompositionCohortDefinition();

    prepNewCompositionCohort.setName("PREGNANT");
    prepNewCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepNewCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepNewCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepNewCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepNewCompositionCohort.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithPregnancyStatusDuringReportingPeriod",
                PrepPregnantBreasftfeedingQueries.findPatientsWhoArePregnantOrBreastfeeding(
                    PregnantOrBreastfeedingWomen.PREGNANTWOMEN)),
            mappings));

    prepNewCompositionCohort.setCompositionString("START-PREP AND PREGNANT");

    return prepNewCompositionCohort;
  }

  public CohortDefinition getClientsWithBreastfeedingStatusDuringReportingPeriod() {
    final CompositionCohortDefinition prepNewCompositionCohort = new CompositionCohortDefinition();

    prepNewCompositionCohort.setName("BREASTFEEDING");
    prepNewCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    prepNewCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    prepNewCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    prepNewCompositionCohort.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));

    prepNewCompositionCohort.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWithBreastfeedingStatusDuringReportingPeriod",
                PrepPregnantBreasftfeedingQueries.findPatientsWhoArePregnantOrBreastfeeding(
                    PregnantOrBreastfeedingWomen.BREASTFEEDINGWOMEN)),
            mappings));

    prepNewCompositionCohort.setCompositionString("START-PREP AND BREASTFEEDING");

    return prepNewCompositionCohort;
  }

  public CohortDefinition findClientsNewlyEnrolledInPrepByOralPrepTypeDissagragation() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));
    definition.addSearch(
        "PREP-ORAL",
        EptsReportUtils.map(this.findClientsNewlyEnrolledInPrepByOralPrepType(), mappings));

    definition.setCompositionString("START-PREP AND PREP-ORAL");

    return definition;
  }

  public CohortDefinition findClientsNewlyEnrolledInPrepByInjectablePrepTypeDissagragation() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));
    definition.addSearch(
        "PREP-INJECTABLE",
        EptsReportUtils.map(this.findClientsNewlyEnrolledInPrepByInjectablePrepType(), mappings));

    definition.setCompositionString("START-PREP AND PREP-INJECTABLE");

    return definition;
  }

  public CohortDefinition findClientsNewlyEnrolledInPrepByOtherPrepTypeDissagragation() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "START-PREP", EptsReportUtils.map(this.getClientsNewlyEnrolledInPrep(), mappings));
    definition.addSearch(
        "PREP-OTHER",
        EptsReportUtils.map(this.findClientsNewlyEnrolledInPrepByOtherPrepType(), mappings));

    definition.setCompositionString("START-PREP AND PREP-OTHER");

    return definition;
  }
}
