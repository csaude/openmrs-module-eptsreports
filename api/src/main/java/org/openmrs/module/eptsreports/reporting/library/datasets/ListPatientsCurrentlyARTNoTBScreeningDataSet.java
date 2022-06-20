package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListPatientsCurrentlyARTNoTBScreeningCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARTNoTBScreeningDataSet extends BaseDataSet {

  private static final String TB5 = "TB5/ListPatientsCurrentlyARTNoTBScreening.sql";

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  private ListPatientsCurrentlyARTNoTBScreeningCohortQueries
      listPatientsCurrentlyARTNoTBScreeningCohortQueries;

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(TB5));

    return dsd;
  }

  public DataSetDefinition getTotalPatientsWhoOnTB5() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setParameters(this.getParameters());

    final String mappings = "endDate=${endDate},location=${location}";

    final CohortDefinition TB5 =
        this.listPatientsCurrentlyARTNoTBScreeningCohortQueries
            .findPatientsWhoActiveOnARTAndNotHaveTBScreening();

    final CohortDefinition TB5_WITH_CLINICAL_CONSULTATION =
        this.listPatientsCurrentlyARTNoTBScreeningCohortQueries
            .findPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithOneConsultation();

    final CohortDefinition TB5_WITHOUT_CLINICAL_CONSULTATION =
        this.listPatientsCurrentlyARTNoTBScreeningCohortQueries
            .getPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithoutOneConsultation();

    dataSetDefinition.addColumn(
        "TOTAL",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
                EptsReportUtils.map(TB5, mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TOTALTBFC",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Com consulta clinica nos ultimos 6 meses",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
                EptsReportUtils.map(TB5_WITH_CLINICAL_CONSULTATION, mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TOTALTBNOTFC",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Sem consulta clinica nos ultimos 6 meses",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Sem consulta clinica nos ultimos 6 meses",
                EptsReportUtils.map(TB5_WITHOUT_CLINICAL_CONSULTATION, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
