package org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta;

import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCETACohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.ResumoMensalCETADimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Indicator6DataSetDefinition extends BaseDataSet {

  @Autowired private ResumoMensalCETACohortQueries resumoMensalCETACohortQueries;
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;
  @Autowired private ResumoMensalCETADimensions resumoMensalCETADimensions;
  @Autowired private ResumoMensalCETADataSetDefinition resumoMensalCETADataSetDefinition;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  public DataSetDefinition constructDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.setName("Indicator 6");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    CohortDefinition indicator6 = this.resumoMensalCETACohortQueries.getPatientsIndicator6();

    dataSetDefinition.addDimension(
        "indicator6Dimension",
        EptsReportUtils.map(
            this.resumoMensalCETADimensions.getResumoMensalCETADimension(indicator6), mappings));

    final CohortIndicator rmCETAIndicator6 =
        this.eptsGeneralIndicator.getIndicator("RMCETA", EptsReportUtils.map(indicator6, mappings));

    addRow(
        dataSetDefinition,
        "I6",
        "Nr de Pacientes que iniciaram o tratamento de SM – CETA",
        EptsReportUtils.map(rmCETAIndicator6, mappings),
        resumoMensalCETADataSetDefinition.getColumns("indicator6Dimension"));

    return dataSetDefinition;
  }
}
