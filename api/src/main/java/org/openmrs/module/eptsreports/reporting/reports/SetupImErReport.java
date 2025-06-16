package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.B13DataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.Eri2MonthsDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.Eri4MonthsDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.IMR1BDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.IMR1Dataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxCurrDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxNewDataset;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupImErReport extends EptsPeriodIndicatorDataExportManager {

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private Eri2MonthsDataset eri2MonthsDataset;

  @Autowired private Eri4MonthsDataset eri4MonthsDataset;

  @Autowired private TxNewDataset txNewDataset;

  @Autowired private TxCurrDataset txCurrDataset;

  @Autowired private IMR1Dataset imr1Dataset;

  @Autowired private IMR1BDataset imr1BDataset;

  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Autowired private B13DataSet b13DataSet;

  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Override
  public String getExcelDesignUuid() {
    return "f6a597ba-5fa2-47d4-ab45-da128cabe7ac";
  }

  @Override
  public String getUuid() {
    return "c6743d00-7107-4760-b19e-a9c1b4432ac0";
  }

  @Override
  public String getName() {
    return "IM-ER-Report";
  }

  @Override
  public String getDescription() {
    return "PEPFAR Early Retention Indicators";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition rd =
        SetupResumoMensalReport.getDefaultPeriodIndicatorReportDefinition();

    rd.setUuid(this.getUuid());
    rd.setName(this.getName());
    rd.setDescription(this.getDescription());
    rd.setParameters(this.txNewDataset.getParameters());

    rd.addDataSetDefinition(
        "N", Mapped.mapStraightThrough(this.txNewDataset.constructTxNewDatasetForIMER()));

    rd.addDataSetDefinition(
        "C", Mapped.mapStraightThrough(this.txCurrDataset.constructTxCurrDataset(true)));

    rd.addDataSetDefinition(
        "I", Mapped.mapStraightThrough(this.imr1Dataset.constructIMR1DataSet()));

    rd.addDataSetDefinition(
        "I-1B", Mapped.mapStraightThrough(this.imr1BDataset.constructIMR1BDataSet()));

    rd.addDataSetDefinition(
        "ERI2", Mapped.mapStraightThrough(this.eri2MonthsDataset.constructEri2MonthsDatset()));

    rd.addDataSetDefinition(
        "ERI4", Mapped.mapStraightThrough(this.eri4MonthsDataset.constructEri4MonthsDataset()));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "R", Mapped.mapStraightThrough(this.b13DataSet.constructResumoMensalDataset()));

    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataset.constructDataset(this.getParameters())));

    rd.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));

    return rd;
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public List<ReportDesign> constructReportDesigns(final ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          this.createXlsReportDesign(
              reportDefinition, "IM_ER_Report.xls", "ERI-Report", this.getExcelDesignUuid(), null);
      final Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (final IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }
}
