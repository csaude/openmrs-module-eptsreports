package org.openmrs.module.eptsreports.reporting.library.datasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.HighViralLoadCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientWithHighViralLoadDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;
  @Autowired private HighViralLoadCohortQueries highViralLoadCohortQueries;

  @Autowired
  public DataSetDefinition constructHighVlDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("VL");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortIndicator highVl =
        this.eptsGeneralIndicator.getIndicator(
            "FIRST_HIGH_VL",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getFirstHighVl(), mappings));

    final CohortIndicator highVlFC =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(
                this.highViralLoadCohortQueries.getFirstConsultationWithHighVlInformed(),
                mappings));

    final CohortIndicator apss0AfterhighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getApss0AfterHighVl(), mappings));

    final CohortIndicator apss1AfterhighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getApss1AfterHighVl(), mappings));
    final CohortIndicator apss2AfterhighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getApss2AfterHighVl(), mappings));

    final CohortIndicator apss3AfterhighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getApss3AfterHighVl(), mappings));

    final CohortIndicator secondFchighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getFCWithSecondHighVl(), mappings));

    final CohortIndicator secondVlLabElab =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getSecondVlLabElab(), mappings));

    final CohortIndicator secondVlResult =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getSecondVlResult(), mappings));

    final CohortIndicator newTerapeuticLine =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getNewTerapeuticLine(), mappings));

    final CohortIndicator newApss0AfterCV2 =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getTApss0AfterCV2(), mappings));

    final CohortIndicator newApss1AfterCV2 =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getTApss1AfterCV2(), mappings));

    final CohortIndicator newApss2AfterCV2 =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getTApss2AfterCV2(), mappings));

    final CohortIndicator newApss3AfterCV2 =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getTApss3AfterCV2(), mappings));

    final CohortIndicator thirdVlRequest =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getThirdVlRequest(), mappings));

    final CohortIndicator thirdVlRepeat =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getThirdVlRepeat(), mappings));

    final CohortIndicator thirdVlLabElab =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getThirdVlLabElab(), mappings));

    final CohortIndicator thirdHighVl =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(this.highViralLoadCohortQueries.getThirdHighVl(), mappings));

    final CohortIndicator thirdVlNewTerapeuticLine =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(
                this.highViralLoadCohortQueries.getThirdHighVlForNewTerapeuticLine(), mappings));

    final CohortIndicator thirdVlTerapeuticLine =
        this.eptsGeneralIndicator.getIndicator(
            "highVlFC",
            EptsReportUtils.map(
                this.highViralLoadCohortQueries.getThirdHighVlForTerapeuticLine(), mappings));

    dataSetDefinition.addColumn(
        "FIRST_HIGH_VL", "1ª Carga Viral Alta ", EptsReportUtils.map(highVl, mappings), "");

    dataSetDefinition.addColumn(
        "FIRST_HIGH_VL_FC",
        "Consulta Clínica com CV registada na Ficha Clínica e comunicada ao Paciente",
        EptsReportUtils.map(highVlFC, mappings),
        "");
    dataSetDefinition.addColumn(
        "APSS0_AFTER_HIGH_VL",
        "Sessão 0 de APSS/PP após CV alta",
        EptsReportUtils.map(apss0AfterhighVl, mappings),
        "");

    dataSetDefinition.addColumn(
        "APSS1_AFTER_HIGH_VL",
        "Sessão 1 de APSS/PP após CV alta",
        EptsReportUtils.map(apss1AfterhighVl, mappings),
        "");
    dataSetDefinition.addColumn(
        "APSS2_AFTER_HIGH_VL",
        "Sessão 2 de APSS/PP após CV alta",
        EptsReportUtils.map(apss2AfterhighVl, mappings),
        "");

    dataSetDefinition.addColumn(
        "APSS3_AFTER_HIGH_VL",
        "Sessão 3 de APSS/PP após CV alta",
        EptsReportUtils.map(apss3AfterhighVl, mappings),
        "");

    dataSetDefinition.addColumn(
        "FC_SECOND_HIGH_VL",
        "Consulta Clínica para Repetição da 2ª CV (Pedido)",
        EptsReportUtils.map(secondFchighVl, mappings),
        "");

    dataSetDefinition.addColumn(
        "SECOND_VL_REPEAT",
        "Colheita de Repetição da 2ª CV",
        EptsReportUtils.map(secondFchighVl, mappings),
        "");

    dataSetDefinition.addColumn(
        "SECOND_VL_LAB_ELAB",
        "Resultado da 2ª CV (Laboratório ou e-Lab)",
        EptsReportUtils.map(secondVlLabElab, mappings),
        "");

    dataSetDefinition.addColumn(
        "SECOND_VL_RESULT",
        "2ª Carga Viral Alta",
        EptsReportUtils.map(secondVlResult, mappings),
        "");

    dataSetDefinition.addColumn(
        "NEW_TERAPEUTIC_LINE",
        "Consulta Clínica para Mudança de Linha após 2ª CV alta",
        EptsReportUtils.map(newTerapeuticLine, mappings),
        "");

    dataSetDefinition.addColumn(
        "TERAPEUTIC_LINE",
        "Nova LINHA Terapêutica",
        EptsReportUtils.map(newTerapeuticLine, mappings),
        "");

    dataSetDefinition.addColumn(
        "APSS0_AFTER_CV2",
        "Sessão 0 da APSS/PP após segunda CV alta",
        EptsReportUtils.map(newApss0AfterCV2, mappings),
        "");

    dataSetDefinition.addColumn(
        "APSS1_AFTER_CV2",
        "1ª Consulta de APSS após segunda CV alta (se aplicável)",
        EptsReportUtils.map(newApss1AfterCV2, mappings),
        "");
    dataSetDefinition.addColumn(
        "APSS2_AFTER_CV2",
        "2ª Consulta de APSS após segunda CV alta (se aplicável)",
        EptsReportUtils.map(newApss2AfterCV2, mappings),
        "");

    dataSetDefinition.addColumn(
        "APSS3_AFTER_CV2",
        "3ª Consulta de APSS após segunda CV alta (se aplicável)",
        EptsReportUtils.map(newApss3AfterCV2, mappings),
        "");
    dataSetDefinition.addColumn(
        "THIRD_VL_REQUEST",
        "Consulta Clínica para Repetição da 3ª CV (Pedido)",
        EptsReportUtils.map(thirdVlRequest, mappings),
        "");

    dataSetDefinition.addColumn(
        "THIRD_VL_REPEAT",
        "Colheita de Repetição da 3ª CV",
        EptsReportUtils.map(thirdVlRepeat, mappings),
        "");

    dataSetDefinition.addColumn(
        "THIRD_VL_RESULT_LAB_ELAB",
        "Resultado da 3ª CV (laboratório ou e-Lab)",
        EptsReportUtils.map(thirdVlLabElab, mappings),
        "");

    dataSetDefinition.addColumn(
        "THIRD_HIGH_VL", "3ª Carga Viral Alta", EptsReportUtils.map(thirdHighVl, mappings), "");

    dataSetDefinition.addColumn(
        "THIRD_VL_NEW_TERAPEUTIC_LINE",
        "Consulta Clínica para Mudança de Linha após 3ª CV alta",
        EptsReportUtils.map(thirdVlNewTerapeuticLine, mappings),
        "");

    dataSetDefinition.addColumn(
        "THIRD_VL_TERAPEUTIC_LINE",
        "Nova LINHA Terapêutica",
        EptsReportUtils.map(thirdVlTerapeuticLine, mappings),
        "");

    return dataSetDefinition;
  }
}
