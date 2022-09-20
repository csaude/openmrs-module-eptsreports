package org.openmrs.module.eptsreports.reporting.reports;

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureDataset;
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
public class SetupListOfChildrenAndAdolescentsOAartWithoutFullDisclosure
    extends EptsDataExportManager {

  @Autowired
  private ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureDataset
      listOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries;

  @Autowired private SismaCodeDataSet sismaCodeDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Override
  public String getUuid() {
    return "7edf7cfe-130b-11ed-a2bb-1fc22840416a";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getExcelDesignUuid() {
    return "a7d1c270-130b-11ed-bd9b-6b6577a8640e";
  }

  @Override
  public String getName() {
    return "Lista de Crianças e Adolescentes em TARV sem Revelação Diagnóstica Total";
  }

  @Override
  public String getDescription() {
    return "Lista de Crianças e Adolescentes em TARV sem Revelação Diagnóstica Total";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(this.getParameters());

    rd.addDataSetDefinition(
        "DR",
        Mapped.mapStraightThrough(
            listOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries.constructDataset(
                this.getParameters())));

    rd.addDataSetDefinition(
        "DRS",
        mapStraightThrough(
            listOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries.getTotaSuummary()));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "S",
        Mapped.mapStraightThrough(this.sismaCodeDataSet.constructDataset(this.getParameters())));

    return rd;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "Template_ListChildrenAdolescentARTWithoutFullDisclosure.xls",
              "Lista de Crianças e Adolescentes em TARV sem Revelação Diagnóstica Total",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:9,dataset:DR");
      props.put("sortWeight", "5000");
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
