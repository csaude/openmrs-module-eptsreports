package org.openmrs.module.eptsreports.reporting.reports;

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListAMDSCoorte12EvaluationReportDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListAMDSCoorte24EvaluationReportDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListAMDSCoorte36EvaluationReportDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.LocationDataSetDefinition;
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
public class SetupAMDSEvaluationReport extends EptsDataExportManager {

  @Autowired private ListAMDSCoorte12EvaluationReportDataSet mds12MesesEvaluationReportSetDataSet;
  @Autowired private ListAMDSCoorte24EvaluationReportDataSet mds24MesesEvaluationReportSetDataSet;
  @Autowired private ListAMDSCoorte36EvaluationReportDataSet mds36MesesEvaluationReportSetDataSet;
  @Autowired private SismaCodeDataSet sismaCodeDataSet;

  public static final String YEAR_PARAMETER = "year";

  public String getExcelDesignUuid() {
    return "b7d2a408-9cfe-11ef-8810-0b1ed6b89917";
  }

  @Override
  public String getUuid() {
    return "bfa106fc-9cfe-11ef-8990-cfbf78e76b5b";
  }

  @Override
  public String getName() {
    return "Relatório de Avaliação de MDS - VERSION 2";
  }

  @Override
  public String getDescription() {
    return "Relatório de Avaliação de MDS - VERSION 2";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(this.getParameters());

    rd.addDataSetDefinition(
        "AMDS",
        Mapped.mapStraightThrough(
            mds12MesesEvaluationReportSetDataSet.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "AMDS1",
        Mapped.mapStraightThrough(
            mds24MesesEvaluationReportSetDataSet.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "AMDS2",
        Mapped.mapStraightThrough(
            mds36MesesEvaluationReportSetDataSet.constructDataset(this.getParameters())));

    rd.addDataSetDefinition("HF", mapStraightThrough(new LocationDataSetDefinition()));
    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataSet.constructDataset(this.getParameters())));

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
              "AMDS1.xls",
              "Relatório de Avaliação de MDS",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put(
          "repeatingSections",
          "sheet:1,row:13,dataset:AMDS | sheet:2,row:13,dataset:AMDS1 | sheet:3,row:13,dataset:AMDS2");

      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public static List<Parameter> getCustomParameteres() {
    return Arrays.asList(getYearConfigurableParameter());
  }

  private static Parameter getYearConfigurableParameter() {
    final Parameter parameter = new Parameter();
    parameter.setName(YEAR_PARAMETER);
    parameter.setLabel("Ano");
    parameter.setType(String.class);
    parameter.setCollectionType(List.class);
    parameter.setRequired(Boolean.TRUE);

    Properties props = new Properties();
    Calendar currentDate = Calendar.getInstance();
    int currentYear = currentDate.get(Calendar.YEAR);

    String codedOptions = "";
    for (int i = 0; i < 10; i++) {
      int year = currentYear - i;
      if (i == 0) {
        codedOptions += year;

      } else {
        codedOptions += "," + year;
      }
    }

    props.put("codedOptions", codedOptions);
    parameter.setWidgetConfiguration(props);
    parameter.setDefaultValue(Arrays.asList(currentYear));
    return parameter;
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(getYearConfigurableParameter());
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
