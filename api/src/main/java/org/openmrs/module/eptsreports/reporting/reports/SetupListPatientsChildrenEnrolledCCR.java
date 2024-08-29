package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListOfPatientsChildreenEnrolledInCCRDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupListPatientsChildrenEnrolledCCR extends EptsDataExportManager {

  @Autowired
  private ListOfPatientsChildreenEnrolledInCCRDataSet listOfPatientsChildreenEnrolledInCCRDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataset;
  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Override
  public String getExcelDesignUuid() {
    return "46c85a8c-24ae-11ef-8fef-f35830b9a08e";
  }

  @Override
  public String getUuid() {
    return "5252482c-24ae-11ef-8ffb-f760ce154d73";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getName() {
    return "CCR1: Lista de Crianças Inscritas em CCR";
  }

  @Override
  public String getDescription() {
    return "This report generates the aggregate number and lists all children enrolled in CCR services in the HF between reporting start and end dates.";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.setParameters(getParameters());
    rd.addDataSetDefinition(
        "CCR",
        Mapped.mapStraightThrough(
            listOfPatientsChildreenEnrolledInCCRDataSet.constructDataset(getParameters())));

    rd.addDataSetDefinition(
        "CCRTOTAL",
        Mapped.mapStraightThrough(
            this.listOfPatientsChildreenEnrolledInCCRDataSet.getTotaLOfChildrenEnrolledInCCR()));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataset.constructDataset(this.getParameters())));
    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataset.constructDataset(this.getParameters())));

    return rd;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "List_CCR_Patients.xls",
              "Lista de Crianças Inscritas em CCR",
              getExcelDesignUuid(),
              null);

      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:11,dataset:CCR");
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.START_DATE_PARAMETER);
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
