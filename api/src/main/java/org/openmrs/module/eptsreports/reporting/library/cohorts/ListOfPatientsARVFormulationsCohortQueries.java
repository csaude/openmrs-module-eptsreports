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
public class ListOfPatientsARVFormulationsCohortQueries {
  final String LIST_OF_CHILDREN_ARV_FORMULATIONS_TOTAL =
      "FORMULATIONS/LIST_OF_CHILDREN_ARV_FORMULATIONS_TOTAL.sql";

  @DocumentedDefinition(value = "C1")
  public CohortDefinition getListOfPatientsARVFormulations() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("LIST OF CHILDREN ARV FORMULATIONS");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(LIST_OF_CHILDREN_ARV_FORMULATIONS_TOTAL));

    return definition;
  }
}
