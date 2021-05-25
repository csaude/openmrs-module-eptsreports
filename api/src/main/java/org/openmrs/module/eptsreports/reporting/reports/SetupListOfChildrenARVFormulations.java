package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListOfChildrenARVFormulationsDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxRttDataset;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupListOfChildrenARVFormulations extends EptsDataExportManager {

  @Autowired protected GenericCohortQueries genericCohortQueries;
  @Autowired private TxRttDataset txRttDataset;
  @Autowired private ListOfChildrenARVFormulationsDataSet listOfChildrenARVFormulationsDataSet;

  @Override
  public String getExcelDesignUuid() {
    return "62655a74-bd87-11eb-ba08-1baa50006a23";
  }

  @Override
  public String getUuid() {
    return "5768351a-bd87-11eb-90f5-5768b48b1c69";
  }

  @Override
  public String getName() {
    return "LISTA DE CRIANCAS COM FORMULACOES DE ARV";
  }

  @Override
  public String getDescription() {
    return "LISTA DE CRIANCAS COM FORMULACOES DE ARV";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(txRttDataset.getParameters());

    rd.addDataSetDefinition(
        "FR",
        Mapped.mapStraightThrough(
            listOfChildrenARVFormulationsDataSet.costructDataSet(txRttDataset.getParameters())));
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
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "FORMULACOES_ARV.xls",
              "FORMULACOES ARV",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:4,dataset:FORMULACOES_ARV");

      props.put("sortWeight", "5000");
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }
}