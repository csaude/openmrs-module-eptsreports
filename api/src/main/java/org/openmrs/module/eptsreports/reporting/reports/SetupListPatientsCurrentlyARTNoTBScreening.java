package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListPatientsCurrentlyARTNoTBScreeningDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupListPatientsCurrentlyARTNoTBScreening extends EptsDataExportManager {

  @Autowired
  private ListPatientsCurrentlyARTNoTBScreeningDataSet listPatientsCurrentlyARTNoTBScreeningDataSet;

  @Autowired private SismaCodeDataSet sismaCodeDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Override
  public String getExcelDesignUuid() {
    return "0b84ddf0-d6a5-11ec-ad88-93bfbff56529";
  }

  @Override
  public String getUuid() {
    return "152c31d2-d6a5-11ec-a81f-375490cc20c1";
  }

  @Override
  public String getName() {
    return "TB5: Lista Pacientes Activos em TARV sem Rastreio de TB";
  }

  @Override
  public String getDescription() {
    return "Este relatório gera os números agregados e lista todos os pacientes atualmente em TARV que não têm uma Triagem de TB documentada nos últimos 6 meses a partir da data final do relatório";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());

    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(this.getParameters());

    rd.addDataSetDefinition(
        "TB5",
        Mapped.mapStraightThrough(
            listPatientsCurrentlyARTNoTBScreeningDataSet.constructDataset(this.getParameters())));
    rd.addDataSetDefinition(
        "RG",
        Mapped.mapStraightThrough(
            this.listPatientsCurrentlyARTNoTBScreeningDataSet.getTotalPatientsWhoOnTB5()));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

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
              "Template_List_Patients_Currently_ART_No_TB_Screening.xls",
              "TB5: Lista Pacientes Activos em TARV sem Rastreio de TB",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:1,row:9,dataset:TB5");
      props.put("sortWeight", "8000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public List<Parameter> getParameters() {
    return Arrays.asList(
        new Parameter("endDate", "  End Date", Date.class),
        new Parameter("location", "Unidade Sanitária", Location.class));
  }
}
