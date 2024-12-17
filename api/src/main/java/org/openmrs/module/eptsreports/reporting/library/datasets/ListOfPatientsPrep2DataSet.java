package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Date;
import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientsPrep2DataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  private static final String FIND_PATIENTS_ON_PREP2_LIST = "PREP2/LIST_PATIENTS_PREP2.sql";
  private static final String FIND_PATIENTS_ON_PREP2_TOTAL = "PREP2/LIST_PATIENTS_PREP2_TOTAL.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName(
        "Find list of patients on PrEP who did not attend their appointment for more than 90 days");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_ON_PREP2_LIST));
    return dsd;
  }

  public DataSetDefinition getTotalPatientsPrep2() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName(
        "Patients on PrEP who did not attend their appointment for more than 90 days");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "endDate=${endDate},location=${location}";
    final CohortDefinition totalPrep2 = this.findPatientsOnPrep2Total();
    dataSetDefinition.addColumn(
        "TOTALPREP2",
        "Total patients on PrEP who did not attend their appointment for more than 90 days",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "prep2total", EptsReportUtils.map(totalPrep2, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }

  @DocumentedDefinition(value = "findPatientsOnPrep2Total")
  private CohortDefinition findPatientsOnPrep2Total() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnPrep2Total");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_ON_PREP2_TOTAL));

    return definition;
  }
}
