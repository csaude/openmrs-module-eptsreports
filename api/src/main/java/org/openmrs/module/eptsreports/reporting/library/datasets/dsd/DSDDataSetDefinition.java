/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.datasets.dsd;

import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ADULT;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIVE_TO_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TEN_TO_FOURTEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWO_TO_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.UNDER_TWO;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DSDCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.dimensions.DSDCommonDimensions;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.AgeRange;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DSDDataSetDefinition extends BaseDataSet {

  private EptsCommonDimension eptsCommonDimension;

  private EptsGeneralIndicator eptsGeneralIndicator;

  private DSDCommonDimensions dsdCommonDimensions;

  private DSDCohortQueries dsdCohortQueries;

  @Autowired
  public DSDDataSetDefinition(
      EptsCommonDimension eptsCommonDimension,
      EptsGeneralIndicator eptsGeneralIndicator,
      DSDCohortQueries dsdCohortQueries,
      DSDCommonDimensions dsdCommonDimensions) {
    this.eptsCommonDimension = eptsCommonDimension;
    this.eptsGeneralIndicator = eptsGeneralIndicator;
    this.dsdCohortQueries = dsdCohortQueries;
    this.dsdCommonDimensions = dsdCommonDimensions;
  }

  public DataSetDefinition constructDSDDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    dsd.setName("DSD Dataset");
    dsd.addParameters(getParameters());
    final String mappings = "endDate=${endDate},location=${location}";

    boolean addWonaState = true;

    this.addAgeDimensions(dsd, UNDER_TWO, TWO_TO_FOUR, FIVE_TO_NINE, TEN_TO_FOURTEEN, ADULT);

    dsd.addDimension(
        "state", EptsReportUtils.map(this.dsdCommonDimensions.getDimensions(), mappings));

    this.addColumns(
        "D1",
        "D1: Number of active patients on ART, Non-Pregnant and Non-Breastfeeding and not on TB Treatment, Eligible for DSD for Stable Patients",
        dsd,
        dsdCohortQueries.getDSDDenominator1(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "D2",
        "D2: Number of active patients on ART Not Eligible for DSD for Stable Patients Number of Patients Number of  active patients on ART (Non-pregnant and Non-Breastfeeding not on TB treatment) Not Eligible  for DSD (Unstable)",
        dsd,
        dsdCohortQueries.getDSDDenominator2(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "D3",
        "D3: Número de Pacientes Activos em TARV(Excluindo grávidas d e lactantes)",
        dsd,
        this.dsdCohortQueries.getDSDDenominator3(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N1",
        "N1: N1: Número de Pacientes Não Grávidas, Não Lactantes e Não em Tratamento TB e Elegíveis a MDS que encontram-se em pelo menos um MDS para Pacietnes Estáveis (GA, DT, DS, DA, FR, DCA, DD)",
        dsd,
        dsdCohortQueries.getNumerator1(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N2-E",
        "N2: Número de Pacientes Activos em TARV que encontram-se inscritos no MDS: Dispensa Trimestral (DT) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator2(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N2-NE",
        "N2: Número de Pacientes Activos em TARV que encontram-se inscritos no MDS: Dispensa Trimestral (DT) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator2(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N3-E",
        "N3:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Semestral (DS) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator3(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N3-NE",
        "N3:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Semestral (DS) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator3(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N4-E",
        "N4:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Annual (DA) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator4(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N4-NE",
        "N4:  Número de Pacientes activos em TARV que encontram-sgetDSDDenominator1e inscritos no MDS: Dispensa Annual (DA) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator4(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N5-E",
        "N5: Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Descentralizada (DD) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator5(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N5-NE",
        "N5: Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Descentralizada (DD) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator5(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N6-E",
        "N6:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Comunitaria atraves do APE (DCA) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator6(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N6-NE",
        "N6:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Comunitaria atraves do APE (DCA) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator6(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N7-E",
        "N7:  Número de Pacientes activos em TARV qu encontram-se inscritos no MDS: Fluxo Rapido (FR) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator7(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N7-NE",
        "N7:  Número de Pacientes activos em TARV qu encontram-se inscritos no MDS: Fluxo Rapido (FR) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator7(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N8-E",
        "N8:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: GAAC  (GA) - Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator8(),
        !addWonaState,
        mappings,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N8-NE",
        "N8:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: GAAC  (GA) - Não-Elegíveis para MDS Estáveis",
        dsd,
        dsdCohortQueries.getDSDNotEligibleNumerator8(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N9",
        "N9:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Dispensa Comunitaria pelo Provedor (DCP)",
        dsd,
        dsdCohortQueries.getDSDNumerator9(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N10",
        "N10:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Brigada Movel (BM)",
        dsd,
        dsdCohortQueries.getDSDNumerator10(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "N11-E",
        "N11: Número de Pacientes activos em TARV qu encontram-se inscritos no MDS: Clinica Movel (CM)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator11(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 12

    this.addColumns(
        "N12-E",
        "N12:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Abordagem Familiar (AF)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator12(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 13

    this.addColumns(
        "N13-E",
        "N13:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Clube de Adesao (CA)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator13(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 14

    this.addColumns(
        "N14-E",
        "N14:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Extensao de Horario (EH)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator14(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 15

    this.addColumns(
        "N15-E",
        "N15:  Número de Pacientes activos em TARV qu encontram-se inscritos no MDS: Paragem Unica no Sector de Tuberculose (TB)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator15(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 16

    this.addColumns(
        "N16-E",
        "N16:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Paragem Unica nos Servicos TARV (CT)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator16(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 17

    this.addColumns(
        "N17-E",
        "N17:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Paragem Unica nos Servicos Amigos De Adolescentes e Jovens (SAAJ)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator17(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 18

    this.addColumns(
        "N18-E",
        "N18:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Paragem Unica Saude Materno-Infantil (SMI)",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator18(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    // add Numerato 19

    this.addColumns(
        "N19-E",
        "N19:  Número de Pacientes activos em TARV que encontram-se inscritos no MDS: Doenca Avancada por HIV (DAH) ",
        dsd,
        dsdCohortQueries.getDSDEligibleNumerator19(),
        addWonaState,
        mappings,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    return dsd;
  }

  private void addAgeDimensions(
      final CohortIndicatorDataSetDefinition definition, final AgeRange... ranges) {

    for (final AgeRange range : ranges) {
      definition.addDimension(
          range.getName(),
          EptsReportUtils.map(
              this.eptsCommonDimension.findPatientsByRange(range.getName(), range),
              "endDate=${endDate}"));
    }
  }

  private void addColumns(
      final String name,
      String label,
      final CohortIndicatorDataSetDefinition definition,
      final CohortDefinition cohortDefinition,
      Boolean addWomanState,
      String mappings,
      final AgeRange... ranges) {

    Mapped<CohortIndicator> totalIndicator =
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "patientsActiveOnArtEligibleForDsd",
                EptsReportUtils.map(cohortDefinition, mappings)),
            mappings);

    definition.addColumn(name, label, totalIndicator, "");

    if (addWomanState) {
      definition.addColumn(
          name + "-pregnant", label + "- Pergnant", totalIndicator, "state=PREGNANT");
      definition.addColumn(
          name + "-breastfeeding",
          label + "- breastfeeding",
          totalIndicator,
          "state=BREASTFEEDING");
    }

    Mapped<CohortIndicator> indicatorExcludingPregnantsAndBreestfeeding =
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "patientsActiveOnArtEcluindingPregnantsAndBreastfeeding",
                EptsReportUtils.map(
                    this.dsdCohortQueries.getDSDDenominatorExcludingPregnantsAndBreastFeeding(
                        cohortDefinition),
                    mappings)),
            mappings);

    label += " (Excluindo gravidas e lactantes) ";

    for (final AgeRange range : ranges) {

      String columnName = getColumnNameByRange(name, range);

      definition.addColumn(
          columnName,
          name + " - (" + range.getName() + ")",
          indicatorExcludingPregnantsAndBreestfeeding,
          range.getName() + "=" + range.getName());
    }
  }

  private String getColumnNameByRange(String columnNamePrefix, AgeRange range) {
    StringBuilder sb = new StringBuilder(columnNamePrefix);
    sb.append("-");
    sb.append(range.getName());
    return sb.toString();
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
