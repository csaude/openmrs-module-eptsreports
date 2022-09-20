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
public class ListPatientsWhoPickedUpARVDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  private static final String FIND_PATIENTS_WHO_PICKED_UP_ARV_LIST =
      "LIST-PATIENTS-WHO-PICKED-UP-ARV/PATIENTS_WHO_PICKED_UP_ARV_LIST.sql";
  private static final String FIND_PATIENTS_WHO_PICKED_UP_ARV_TOTAL =
      "LIST-PATIENTS-WHO-PICKED-UP-ARV/PATIENTS_WHO_PICKED_UP_ARV_TOTAL.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Find list of patients who picked up ARV during period");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_PICKED_UP_ARV_LIST));
    return dsd;
  }

  public DataSetDefinition getTotalPatientsWhoPickedUpARVDataset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Patients who picked up ARV Total");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final CohortDefinition pickedupARVTotal = this.findPatientsWhoPickedUpARVTotal();
    dataSetDefinition.addColumn(
        "PICKEDUPTOTAL",
        "Total de pacientes que levantaram ARV",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "tptEligibleTotal", EptsReportUtils.map(pickedupARVTotal, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }

  @DocumentedDefinition(value = "findPatientsWhoPickedUpARVTotal")
  private CohortDefinition findPatientsWhoPickedUpARVTotal() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoPickedUpARVTotal");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_PICKED_UP_ARV_TOTAL));

    return definition;
  }
}
