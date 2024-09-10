package org.openmrs.module.eptsreports.reporting.reports;

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListMDSEvaluationReportDataSet;
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
public class SetupMDSEvaluationReport extends EptsDataExportManager {

  @Autowired private GenericCohortQueries genericCohortQueries;
  @Autowired private ListMDSEvaluationReportDataSet mdsEvaluationReportSetDataSet;
  @Autowired private SismaCodeDataSet sismaCodeDataSet;

  public static final String YEAR_PARAMETER = "year";

  public String getExcelDesignUuid() {
    return "c4280b6e-3dad-11ee-bb7a-47c3291ec302";
  }

  @Override
  public String getUuid() {
    return "e2713dca-3dad-11ee-a6ad-afd5a6f73795";
  }

  @Override
  public String getName() {
    return "Relatório de Avaliação de MDS";
  }

  @Override
  public String getDescription() {
    return "Relatório de Avaliação de MDS";
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
            mdsEvaluationReportSetDataSet.constructDatasetCoorte(this.getParameters())));

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
              "AMDS.xls",
              "Relatório de Avaliação de MDS",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:13,dataset:AMDS");

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
