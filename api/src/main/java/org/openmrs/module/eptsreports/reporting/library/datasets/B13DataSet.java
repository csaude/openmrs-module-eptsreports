package org.openmrs.module.eptsreports.reporting.library.datasets;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;
import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.disaggregations.ResumoMensalAandBdisaggregations;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class B13DataSet extends BaseDataSet {
  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired ResumoMensalAandBdisaggregations resumoMensalAandBdisaggregations;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructResumoMensalDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    dsd.setName("Resumo Mensal Data set B");
    dsd.addParameters(getParameters());

    dsd.addDimension("gender", map(eptsCommonDimension.gender(), ""));
    dsd.addDimension(
        "age", map(eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dsd.addColumn(
        "B13TP", "Total patients - Total Geral", getPatientsWhoAreCurrentlyEnrolledOnARTB13(), "");

    addRow(
        dsd,
        "B13TC",
        "Patients under 15 years",
        getPatientsWhoAreCurrentlyEnrolledOnARTB13(),
        resumoMensalAandBdisaggregations.getUnder14YearsColumns());

    addRow(
        dsd,
        "B13TA",
        "Patients over 15 years - adults",
        getPatientsWhoAreCurrentlyEnrolledOnARTB13(),
        resumoMensalAandBdisaggregations.getAdultPatients());

    addRow(
        dsd,
        "B13TAD",
        "Adolescentes patients",
        getPatientsWhoAreCurrentlyEnrolledOnARTB13(),
        resumoMensalAandBdisaggregations.getAdolescentesColumns());

    return dsd;
  }

  private Mapped<CohortIndicator> getPatientsWhoAreCurrentlyEnrolledOnARTB13() {
    String name = "Patients who abandoned the ART during the current month";
    final String mappings = "endDate=${endDate},location=${location}";
    Mapped<CohortDefinition> cohort =
        map(resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(), mappings);
    return mapStraightThrough(eptsGeneralIndicator.getIndicator(name, cohort));
  }
}
