package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListOfPatientsPrep2DataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupListPatientsPrep2 extends EptsDataExportManager {

  @Autowired private GenericCohortQueries genericCohortQueries;
  @Autowired private ListOfPatientsPrep2DataSet listOfPatientsPrep2DataSet;

  @Autowired private DatimCodeDataSet datimCodeDataset;
  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Override
  public String getExcelDesignUuid() {
    return "0c1305a4-a8b6-11ef-b15f-3b41742db8be";
  }

  @Override
  public String getUuid() {
    return "0361adac-a8b6-11ef-8e6c-8ba6079f64b5";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getName() {
    return "PrEP2: Lista Utentes em PrEP que não compareceram à consulta por mais de 90 dias";
  }

  @Override
  public String getDescription() {
    return "Esta lista gera o número agregado e lista de todos os utentes PrEP que não compareceram à consulta  por mais de 90 dias até a data fim do relatório.";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.setParameters(this.getParameters());
    rd.addDataSetDefinition(
        "PREP2",
        Mapped.mapStraightThrough(listOfPatientsPrep2DataSet.constructDataset(getParameters())));

    rd.addDataSetDefinition(
        "PREP2TOTAL",
        Mapped.mapStraightThrough(this.listOfPatientsPrep2DataSet.getTotalPatientsPrep2()));
    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataset.constructDataset(this.getParameters())));
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
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "MASC_PrEP2_Lista_90_dias.xls",
              "MASC PrEP2 Lista 90 dias",
              getExcelDesignUuid(),
              null);

      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:11,dataset:PREP2");
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
