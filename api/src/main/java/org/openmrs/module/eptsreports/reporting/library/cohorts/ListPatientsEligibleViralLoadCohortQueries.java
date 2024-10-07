package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrColumnsQuantity;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrQuery;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsEligibleViralLoadCohortQueries {

  private static final String FIND_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION_TOTAL =
      "LIST_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION/PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION_TOTAL.sql";

  @DocumentedDefinition(value = "findPatientsEligibleToViralLoad")
  public CohortDefinition findPatientsEligibleToViralLoad() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsEligibleToViralLoad");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION_TOTAL),
            TxCurrQuery.findPatientsInTxCurr(TxCurrColumnsQuantity.PATIENT_ID));

    definition.setQuery(query);

    return definition;
  }
}
