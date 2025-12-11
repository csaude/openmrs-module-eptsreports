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
public class Indicator20DataSetDefinition extends BaseDataSet {

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

    dataSetDefinition.setName("Indicator 20");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    CohortDefinition indicator20 = this.resumoMensalCETACohortQueries.getPatientsIndicator20();

    dataSetDefinition.addDimension(
        "indicator20Dimension",
        EptsReportUtils.map(
            this.resumoMensalCETADimensions.getResumoMensalCETADimension(indicator20), mappings));

    final CohortIndicator rmCETAIndicator20 =
        this.eptsGeneralIndicator.getIndicator(
            "RMCETA", EptsReportUtils.map(indicator20, mappings));

    addRow(
        dataSetDefinition,
        "I20",
        "Nr de pacientes reintegrados",
        EptsReportUtils.map(rmCETAIndicator20, mappings),
        resumoMensalCETADataSetDefinition.getColumns("indicator20Dimension"));

    return dataSetDefinition;
  }
}
