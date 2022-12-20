package org.openmrs.module.eptsreports.reporting.library.datasets.resumo;

import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SimpleIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalEncounterDataSetDefinition extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructResumoMensalDataset() {

    SimpleIndicatorDataSetDefinition cd = new SimpleIndicatorDataSetDefinition();
    cd.setName("F1");
    cd.addParameters(getParameters());
    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    cd.addColumn(
        "F1",
        "F1",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "F2",
                resumoMensalCohortQueries
                    .getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthF1()),
            mappings));

    cd.addColumn(
        "F2",
        "F2",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "F2",
                resumoMensalCohortQueries
                    .getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthTbF2()),
            mappings));

    return cd;
  }
}
