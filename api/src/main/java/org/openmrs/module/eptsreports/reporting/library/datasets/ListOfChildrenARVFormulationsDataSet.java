package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListOfPatientsARVFormulationsCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.library.queries.ListOfChildrenARVFormulationsQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfChildrenARVFormulationsDataSet extends BaseDataSet {

  @Autowired
  private ListOfPatientsARVFormulationsCohortQueries listOfPatientsARVFormulationsCohortQueries;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  public DataSetDefinition costructDataSet(List<Parameter> parameterList) {
    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("LISTA DE CRIANÇAS COM FORMULAÇÕES DE ARV");
    dsd.addParameters(parameterList);
    dsd.setSqlQuery(ListOfChildrenARVFormulationsQueries.QUERY.findChildrenARVFormulations);
    return dsd;
  }

  public DataSetDefinition getTotalARVFormulationsDataSet() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("LISTA DE CRIANÇAS COM FORMULAÇÕES DE ARV");
    dataSetDefinition.addParameters(this.getParameters());
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition definition =
        listOfPatientsARVFormulationsCohortQueries.getListOfPatientsARVFormulations();

    dataSetDefinition.addColumn(
        "LARVFORMULATION",
        "LISTA DE CRIANÇAS COM FORMULAÇÕES DE ARV",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "LISTA DE CRIANÇAS COM FORMULAÇÕES DE ARV",
                EptsReportUtils.map(definition, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }
}
