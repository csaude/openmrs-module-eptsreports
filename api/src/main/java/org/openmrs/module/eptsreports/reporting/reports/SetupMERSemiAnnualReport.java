package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.CxCaSCRNDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.CxCaTXDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.TbPrevDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxMlDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxTBDataset;
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
public class SetupMERSemiAnnualReport extends EptsPeriodIndicatorDataExportManager {

  @Autowired private TxMlDataset txMlDataset;

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private TxTBDataset txTBDataset;

  @Autowired private TbPrevDataset tbPrevDataset;

  @Autowired private CxCaSCRNDataSet cxCaSCRNDataSet;

  @Autowired private CxCaTXDataSet cxCaTXDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Override
  public String getExcelDesignUuid() {
    return "61fea06a-472b-11e9-8b42-876961a472ef";
  }

  @Override
  public String getUuid() {
    return "6febad76-472b-11e9-a41e-db8c77c788cd";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getName() {
    return "PEPFAR MER 2.7 Semi-Annual";
  }

  @Override
  public String getDescription() {
    return "PEPFAR MER 2.7 Semi-Annual";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition rd =
        SetupResumoMensalReport.getDefaultPeriodIndicatorReportDefinition();

    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.setParameters(txMlDataset.getParameters());
    rd.addDataSetDefinition("T", Mapped.mapStraightThrough(txTBDataset.constructTxTBDataset()));
    rd.addDataSetDefinition("TBPREV", Mapped.mapStraightThrough(tbPrevDataset.constructDatset()));
    rd.addDataSetDefinition("CX", Mapped.mapStraightThrough(cxCaSCRNDataSet.constructDatset()));
    rd.addDataSetDefinition("CXT", Mapped.mapStraightThrough(cxCaTXDataSet.constructDatset()));
    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

    rd.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));
    return rd;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "PEPFAR_MER_2.7_Semiannual.xls",
              "PEPFAR MER 2.7 Semi-Annual Report",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }
}
