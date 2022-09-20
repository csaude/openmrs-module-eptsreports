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
public class ListOfPatientsWithPositiveTbScreeningDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  private static final String FIND_PATIENTS_WITH_POSITIVE_TB_SCREENING_LIST =
      "LIST-PATIENTS-WITH-POSITIVE-TB-SCREENING/PATIENTS_WITH_POSITIVE_TB_SCREENING_LIST.sql";
  private static final String FIND_PATIENTS_WITH_POSITIVE_TB_SCREENING_TOTAL =
      "LIST-PATIENTS-WITH-POSITIVE-TB-SCREENING/PATIENTS_WITH_POSITIVE_TB_SCREENING_TOTAL.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Find list of patients with positive tb screening");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_POSITIVE_TB_SCREENING_LIST));
    return dsd;
  }

  public DataSetDefinition getTotalPatientsWithPositiveTbScreeningDataset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Patients with positive tb screening Total");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final CohortDefinition positiveScreeningTotal = this.findPatientsWithPositiveTbScreeningTotal();
    dataSetDefinition.addColumn(
        "TOTAL",
        "Total de Pacientes com rastreio positivo de TB",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "positiveScreeningTotal", EptsReportUtils.map(positiveScreeningTotal, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }

  @DocumentedDefinition(value = "findPatientsWithPositiveTbScreeningTotal")
  private CohortDefinition findPatientsWithPositiveTbScreeningTotal() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWithPositiveTbScreeningTotal");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_POSITIVE_TB_SCREENING_TOTAL));

    return definition;
  }
}
