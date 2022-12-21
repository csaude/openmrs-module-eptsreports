package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.ListPatientWithHighViralLoadDataSet;
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
public class SetupListPatientWithHighViralLoad extends EptsDataExportManager {

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private ListPatientWithHighViralLoadDataSet listPatientWithHighViralLoadDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataset;
  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Override
  public String getExcelDesignUuid() {
    return "8216abcd-f321-455a-a7be-3d1609c7052f";
  }

  @Override
  public String getUuid() {
    return "8216abcd-f321-455a-a7be-3d1609c7052f";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getName() {
    return "Lista De Seguimento De Pacientes Com Carga Viral Não Suprimida";
  }

  @Override
  public String getDescription() {
    return "Este relatório gera a lista de pacientes em TARV que tem CV Alta documentada durante o período do relatório na planilha Visão Geral e lista o subconjunto destes pacientes que necessitam acções acompanhamento de acordo com as orientações existentes especificados com alertas prospectivos durante a semana corrente listadas na planilha Semana Corrente";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.setParameters(getParameters());
    rd.addDataSetDefinition(
        "PHVL",
        Mapped.mapStraightThrough(
            listPatientWithHighViralLoadDataSet.constructDataset(getParameters())));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "PHVLEvaluationDate",
        Mapped.mapStraightThrough(
            this.listPatientWithHighViralLoadDataSet.getEndDatePlus7Days(this.getParameters())));

    rd.addDataSetDefinition(
        "PHVLCURRWEEK",
        Mapped.mapStraightThrough(
            this.listPatientWithHighViralLoadDataSet
                .getAlertMessagesForListsPatientsEligibleViralLoadDataset(this.getParameters())));

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
              "ListPatientWithHighViralLoad.xls",
              "Lista de Seguimento De Pacientes Com Carga Viral Não Suprimida",
              getExcelDesignUuid(),
              null);

      Properties props = new Properties();
      props.put(
          "repeatingSections", "sheet:1,row:12,dataset:PHVL | sheet:2,row:10,dataset:PHVLCURRWEEK");
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
