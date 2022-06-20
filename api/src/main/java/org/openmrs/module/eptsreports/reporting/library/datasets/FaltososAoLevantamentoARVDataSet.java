package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.FaltososAoLevantamentoARVCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.FaltososAoLevantamentoARVDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FaltososAoLevantamentoARVDataSet extends BaseDataSet {

  @Autowired private EptsCommonDimension eptsCommonDimension;
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;
  @Autowired private FaltososAoLevantamentoARVCohortQueries faltososAoLevantamentoARVCohortQueries;
  @Autowired private FaltososAoLevantamentoARVDimension faltososAoLevantamentoARVDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructDatset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    dataSetDefinition.setParameters(getParameters());

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
    dataSetDefinition.addDimension(
        "df",
        EptsReportUtils.map(
            faltososAoLevantamentoARVDimension.getDefaultersDimentions(), mappings));

    dataSetDefinition.addColumn(
        "FD-TOTAL",
        "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período  (total)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período  (total)",
                EptsReportUtils.map(
                    this.faltososAoLevantamentoARVCohortQueries.getTotalDenominator(), mappings)),
            mappings),
        "");

    addRow(
        dataSetDefinition,
        "FD",
        "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período",
                EptsReportUtils.map(
                    this.faltososAoLevantamentoARVCohortQueries.getTotalDenominator(), mappings)),
            mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "FN-TOTAL",
        "Numerador: Nº de pacientes faltosos ao levantamento de ARV durante o período (total)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período  (total)",
                EptsReportUtils.map(
                    this.faltososAoLevantamentoARVCohortQueries.getTotalNumerator(), mappings)),
            mappings),
        "");

    addRow(
        dataSetDefinition,
        "FN",
        "Numerador: Nº de pacientes faltosos ao levantamento de ARV durante o período",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Denominador: Nº de pacientes marcados para levantamento de ARV durante o período",
                EptsReportUtils.map(
                    this.faltososAoLevantamentoARVCohortQueries.getTotalNumerator(), mappings)),
            mappings),
        getColumns());

    return dataSetDefinition;
  }

  private List<ColumnParameters> getColumns() {

    ColumnParameters a1 = new ColumnParameters("15-", "15-", "age=0-14", "01");

    ColumnParameters a2 = new ColumnParameters("10-", "10-", "age=0-9", "02");

    ColumnParameters a3 =
        new ColumnParameters("0-9 female", "0-9 female", "gender=F|age=0-9", "03");

    ColumnParameters a4 = new ColumnParameters("0-9 male", "0-9 male", "gender=M|age=0-9", "04");

    ColumnParameters a5 = new ColumnParameters("10-14", "10-14 years", "age=10-14", "05");

    ColumnParameters a6 =
        new ColumnParameters("10-14 famale", "10-14 years famele", "gender=F|age=10-14", "06");

    ColumnParameters a7 =
        new ColumnParameters("10-14 famale", "10-14 years famele", "gender=M|age=10-14", "07");

    ColumnParameters a8 = new ColumnParameters("15+", "15+", "age=15+", "08");

    ColumnParameters a9 = new ColumnParameters("15-24", "15-24", "age=15-24", "09");

    ColumnParameters a10 =
        new ColumnParameters("15-24 famele", "15-24 famele", "gender=F|age=15-24", "010");

    ColumnParameters a11 =
        new ColumnParameters("15-24 male", "15-24 male", "gender=M|age=15-24", "011");

    ColumnParameters a12 = new ColumnParameters("25-49", "25-49", "age=25-49", "012");

    ColumnParameters a13 =
        new ColumnParameters("25-49  famele", "25-49 famele", "age=25-49|gender=F", "013");

    ColumnParameters a14 =
        new ColumnParameters("25-49  male", "25-49 male", "age=25-49|gender=M", "014");

    ColumnParameters a15 = new ColumnParameters("50+", "50+", "age=50+", "015");

    ColumnParameters a16 = new ColumnParameters("50+", "50+", "age=50+|gender=F", "016");

    ColumnParameters a17 = new ColumnParameters("50+", "50+", "age=50+|gender=M", "017");

    ColumnParameters a18 =
        new ColumnParameters("pregnant", "pregnant", "gender=F|df=PREGNANT", "018");

    ColumnParameters a19 =
        new ColumnParameters("breastfeeding", "breastfeeding", "gender=F|df=BREASTFEEDING", "019");

    ColumnParameters a20 = new ColumnParameters("cv", "cv", "df=CV", "020");

    ColumnParameters a21 = new ColumnParameters("apss", "apss", "df=APSS", "021");

    return Arrays.asList(
        a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20,
        a21);
  }
}
