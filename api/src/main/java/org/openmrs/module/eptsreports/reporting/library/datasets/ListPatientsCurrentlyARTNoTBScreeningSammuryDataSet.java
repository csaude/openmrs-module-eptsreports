package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListPatientsCurrentlyARTNoTBScreeningCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARTNoTBScreeningSammuryDataSet extends BaseDataSet {

  @Autowired EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  ListPatientsCurrentlyARTNoTBScreeningCohortQueries
      listPatientsCurrentlyARTNoTBScreeningCohortQueries;

  public DataSetDefinition getTotalPatientsWhoOnTB5() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setParameters(this.getParameters());

    final String mappings = "endDate=${endDate},location=${location}";

    final CohortDefinition TB5 =
        this.listPatientsCurrentlyARTNoTBScreeningCohortQueries
            .findPatientsWhoActiveOnARTAndNotHaveTBScreening();

    dataSetDefinition.addColumn(
        "TOTAL",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
                EptsReportUtils.map(TB5, mappings)),
            mappings),
        "");
    return dataSetDefinition;
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(new Parameter("endDate", "End Date", Date.class));
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
