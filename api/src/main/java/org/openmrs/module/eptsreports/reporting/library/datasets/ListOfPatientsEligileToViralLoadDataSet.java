package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListPatientsEligibleViralLoadCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrColumnsQuantity;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrQuery;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientsEligileToViralLoadDataSet extends BaseDataSet {

  @Autowired
  private ListPatientsEligibleViralLoadCohortQueries listPatientsEligibleViralLoadCohortQueries;

  private static final String FIND_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION_LIST =
      "LIST_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION/PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION.sql";

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Find list of patients who are eligible for a viral load sample collection");
    dsd.addParameters(list);

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_ELEGIBLE_VIRAL_LOAD_SAMPLE_COLLECTION_LIST),
            TxCurrQuery.findPatientsInTxCurr(TxCurrColumnsQuantity.PATIENT_ID));

    dsd.setSqlQuery(query);

    return dsd;
  }

  public DataSetDefinition getTotalEligibleViralLoadDataset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Patients Eligible Viral Load Total");
    dataSetDefinition.addParameters(getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition vlEligibleTotal =
        this.listPatientsEligibleViralLoadCohortQueries.findPatientsEligibleToViralLoad();

    dataSetDefinition.addColumn(
        "TOTALVL",
        "Total de Pacientes Elegiveis a Carga Viral",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "vlEligibleTotal", EptsReportUtils.map(vlEligibleTotal, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }
}
