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
public class Indicator9DataSetDefinition extends BaseDataSet {

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

    dataSetDefinition.setName("Indicator 9");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    CohortDefinition indicator9 = this.resumoMensalCETACohortQueries.getPatientsIndicator9();

    dataSetDefinition.addDimension(
        "indicator9Dimension",
        EptsReportUtils.map(
            this.resumoMensalCETADimensions.getResumoMensalCETADimension(indicator9), mappings));

    final CohortIndicator rmCETAIndicator9 =
        this.eptsGeneralIndicator.getIndicator("RMCETA", EptsReportUtils.map(indicator9, mappings));

    addRow(
        dataSetDefinition,
        "I9",
        "Nr de Pacientes com Tentativa de Suicídio a entrada",
        EptsReportUtils.map(rmCETAIndicator9, mappings),
        resumoMensalCETADataSetDefinition.getColumns("indicator9Dimension"));

    return dataSetDefinition;
  }
}
