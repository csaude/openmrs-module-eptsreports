package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientRimenderCallPrepsCohortQueries {

  private static final String FIND_PATIENTS_RIMENDER_PREP_TOTAL =
      "PREP1/PREP1_LIST_PATIENTS_WITH_REMINDER_PREP_SUMMURY.sql";

  @DocumentedDefinition(value = "findPatientsPrepTotal")
  public CohortDefinition findPatientsPrepTotal() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsPrepTotal");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_RIMENDER_PREP_TOTAL));

    return definition;
  }
}
