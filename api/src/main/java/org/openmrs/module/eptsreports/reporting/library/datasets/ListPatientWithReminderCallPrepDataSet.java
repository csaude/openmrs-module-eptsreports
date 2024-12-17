package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListPatientRimenderCallPrepsCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListPatientWithReminderCallPrepDataSet extends BaseDataSet {

  private static final String PREP1_FIND_PATIENTS_WITH_REMINDER_PREP_CALL =
      "PREP1/PREP1_LIST_PATIENTS_WITH_REMINDER_PREP_PATIENTS.sql";

  @Autowired
  private ListPatientRimenderCallPrepsCohortQueries listPatientRimenderCallPrepsCohortQueries;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Chamadas de Lembrete para Utentes em PrEP");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(PREP1_FIND_PATIENTS_WITH_REMINDER_PREP_CALL));
    return dsd;
  }

  public DataSetDefinition getTotalEPrepTotalDataset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Total de Pacientes Com Consulta Prep Marcada");
    dataSetDefinition.addParameters(getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition prep1 =
        this.listPatientRimenderCallPrepsCohortQueries.findPatientsPrepTotal();

    dataSetDefinition.addColumn(
        "TOTALPREP1",
        "Total de Pacientes Com Consulta Prep Marcada",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "vlEligibleTotal", EptsReportUtils.map(prep1, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }
}
