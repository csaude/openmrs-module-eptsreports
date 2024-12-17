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
package org.openmrs.module.eptsreports.reporting.library.datasets.resumo;

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCCRCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCCRDataSetDefinition extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired ResumoMensalCCRCohortQueries resumoMensalCCRCohortQueries;

  public DataSetDefinition constructResumoMensalDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    dsd.setName("Resumo Mensal Data set B");
    dsd.addParameters(getParameters());

    dsd.addColumn(
        "I1",
        "Total de 1as consultas",
        getChildrenWithFirstConsultationCCRDuringPeriodIndicator1(),
        "");

    dsd.addColumn(
        "I2",
        "Total de crianças com contacto com tuberculose",
        getChildrenWithTbHivContactIndicator2(),
        "");

    dsd.addColumn(
        "I3",
        "Total de crianças com desnutrição aguda moderada",
        getNumberOfChildrenWithModerateAcuteMalnutritionIndicator3(),
        "");

    dsd.addColumn(
        "I4",
        "Total de crianças com desnutrição aguda grave",
        getNumberOfChildrenWithSevereAcuteMalnutritionIndicator4(),
        "");

    dsd.addColumn(
        "I5",
        "Total de crianças com exposição ao HIV",
        getChildrenWithExposureToHIVIndicator5(),
        "");

    dsd.addColumn(
        "I6",
        "Total de crianças com outra condição de Risco",
        getChildrenWithOtherRiskConditionIndicator6(),
        "");

    dsd.addColumn(
        "I7",
        "Total de crianças que iniciaram Isoniazida na CCR",
        getNumberOfChildrenWhoInitiatedINHTreatmentIndicator7(),
        "");

    dsd.addColumn(
        "I8", "Total de crianças que receberam ATPU", getNumberOfChildrenWhithATPUIndicator8(), "");

    dsd.addColumn(
        "I9",
        "Total de  crianças que receberam CSB/suplemento nutricional",
        getNumberOfChildrenWhoReceivedCSBIndicator9(),
        "");

    dsd.addColumn(
        "I10",
        "Total de crianças que iniciaram CTZ  < 2 meses de idade",
        getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator10(),
        "");

    dsd.addColumn(
        "I11",
        "Total de crianças que iniciaram CTZ  ≥ 2 meses de idade",
        getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator11(),
        "");

    dsd.addColumn(
        "I12",
        "Total de 1º PCR colhido < 2 meses de idade",
        getNumberOfChildrenWhithTheFirstPCRCollectedIndicator12(),
        "");

    dsd.addColumn(
        "I13",
        "Total de 1º PCR colhido ≥ 2 meses de idade",
        getNumberOfChildrenWhithTheFirstPCRCollectedIndicator13(),
        "");

    dsd.addColumn(
        "I14",
        "Total de crianças expostas ≥9 meses testadas com Teste Rápido de HIV",
        getNumberOfChildrenExposedIndicator14(),
        "");

    dsd.addColumn(
        "I15",
        "Total de crianças não expostas ao HIV testadas com Teste Rápido de HIV",
        getNumberOfChildrenNotExposedToHIVTestedIndicator15(),
        "");

    dsd.addColumn(
        "I16",
        "Total de crianças não expostas ao HIV, testadas com Teste Rápido que tiveram resultado positivo",
        getNumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16(),
        "");

    dsd.addColumn(
        "I20",
        "Total de crianças com contacto com TB",
        getChildrenWithTbHivContact9MonthsAgoIndicator20(),
        "");

    dsd.addColumn(
        "I21",
        "Total de crianças que completaram Isoniazida",
        getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21(),
        "");

    dsd.addColumn(
        "I22", "Total de crianças referidas para PNCT", getChildrenReferredToPNCTIndicator22(), "");

    dsd.addColumn(
        "I23", "Total de crianças que abandonaram", getChildrenWhoAbandonedCCRIndicator23(), "");

    dsd.addColumn(
        "I24",
        "Total de crianças com DAM",
        getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24(),
        "");

    dsd.addColumn(
        "I25",
        "Total de crianças com DAM recuperadas",
        getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25(),
        "");

    dsd.addColumn(
        "I26",
        "Total de crianças com DAM que abandonaram",
        getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26(),
        "");

    dsd.addColumn(
        "I27",
        "Total de crianças com com DAG",
        getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27(),
        "");

    dsd.addColumn(
        "I28",
        "Total de crianças com DAG que foram referidas para internamento",
        getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28(),
        "");

    dsd.addColumn(
        "I29",
        "Total de crianças com DAG recuperadas",
        getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29(),
        "");

    dsd.addColumn(
        "I30",
        "Total de crianças com DAG que abandonaram",
        getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30(),
        "");

    dsd.addColumn(
        "I31",
        "Total de crianças com DAG que foram óbito",
        getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31(),
        "");

    dsd.addColumn(
        "I32",
        "Total de crianças expostas",
        getChildrenWithExposureToHIVCoorte9MonthsIndicator32(),
        "");

    dsd.addColumn(
        "I33",
        "Total de crianças expostas com 5 meses de idade e com mãe em TARV",
        getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33(),
        "");

    dsd.addColumn(
        "I34",
        "Total de  crianças expostas com aleitamento materno exclusivo aos 5 meses",
        getChildrenExposedToExclusiveBreastfeedingIndicator34(),
        "");

    dsd.addColumn(
        "I35",
        "Total de crianças expostas em Aleitamento Misto aos 5 meses",
        getChildrenExposedToMixedBreastfeedingIndicator35(),
        "");

    dsd.addColumn(
        "I36",
        "Total de crianças expostas que receberam ARV aos 5 meses",
        getChildrenExposedWhoReceivedARVIndicator36(),
        "");

    dsd.addColumn(
        "I37",
        "Total de PCR colhido <2 meses de idade",
        getTotalChildrenWhoReceivedPCRIndicator37(),
        "");

    dsd.addColumn(
        "I38",
        "Total de PCR colhido ≥2 meses de idade",
        getTotalChildrenWhoReceivedPCRIndicator38(),
        "");

    dsd.addColumn(
        "I39",
        "Total de crianças com resultados PCR positivo <2 meses de idade ",
        getTotalChildrenWhoReceivedPCRResultPositiveIndicator39(),
        "");

    dsd.addColumn(
        "I40",
        "Total de crianças com resultados PCR positivo ≥2 meses de idade",
        getTotalChildrenWhoReceivedPCRResultPositiveIndicator40(),
        "");

    dsd.addColumn(
        "I41",
        "Total de crianças expostas",
        getChildrenWithExposureToHIVCoorte18MonthsIndicator41(),
        "");

    dsd.addColumn(
        "I42",
        "Total de crianças expostas com resultado definitivo de HIV positivo",
        getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42(),
        "");

    dsd.addColumn(
        "I43",
        "Total de crianças expostas com resultado definitivo de HIV negativo",
        getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43(),
        "");

    dsd.addColumn(
        "I44",
        "Total de crianças expostas transferidas para a Consulta Criança Sadia",
        getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44(),
        "");

    dsd.addColumn(
        "I45",
        "Total de crianças expostas transferidas para as Consultas Integradas",
        getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45(),
        "");

    dsd.addColumn(
        "I46",
        "Total de crianças expostas que abandonaram",
        getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46(),
        "");

    dsd.addColumn(
        "I47",
        "Total de crianças expostas que foram óbito",
        getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47(),
        "");

    return dsd;
  }

  private Mapped<CohortIndicator> getChildrenWithFirstConsultationCCRDuringPeriodIndicator1() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I1",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWithFirstConsultationCCRDuringPeriodIndicator1())));
  }

  private Mapped<CohortIndicator> getChildrenWithTbHivContactIndicator2() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I2",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenWithTbHivContactIndicator2())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWithModerateAcuteMalnutritionIndicator3() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I3",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithModerateAcuteMalnutritionIndicator3())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWithSevereAcuteMalnutritionIndicator4() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I4",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithSevereAcuteMalnutritionIndicator4())));
  }

  private Mapped<CohortIndicator> getChildrenWithExposureToHIVIndicator5() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I5",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenWithExposureToHIVIndicator5())));
  }

  private Mapped<CohortIndicator> getChildrenWithOtherRiskConditionIndicator6() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I6",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenWithOtherRiskConditionIndicator6())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhoInitiatedINHTreatmentIndicator7() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I7",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWhoInitiatedINHTreatmentIndicator7())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhithATPUIndicator8() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I8",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getNumberOfChildrenWhithATPUIndicator8())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhoReceivedCSBIndicator9() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I9",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getNumberOfChildrenWhoReceivedCSBIndicator9())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator10() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I10",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator10())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator11() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I11",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator11())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhithTheFirstPCRCollectedIndicator12() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I12",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWhithTheFirstPCRCollectedIndicator12())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenWhithTheFirstPCRCollectedIndicator13() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I13",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWhithTheFirstPCRCollectedIndicator13())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenExposedIndicator14() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I14",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getNumberOfChildrenExposedIndicator14())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenNotExposedToHIVTestedIndicator15() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I15",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenNotExposedToHIVTestedIndicator15())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I16",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16())));
  }

  private Mapped<CohortIndicator> getChildrenWithTbHivContact9MonthsAgoIndicator20() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I20",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenWithTbHivContact9MonthsAgoIndicator20())));
  }

  private Mapped<CohortIndicator> getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I21",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21())));
  }

  private Mapped<CohortIndicator> getChildrenReferredToPNCTIndicator22() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I22",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenReferredToPNCTIndicator22())));
  }

  private Mapped<CohortIndicator> getChildrenWhoAbandonedCCRIndicator23() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I23",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenWhoAbandonedCCRIndicator23())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I24",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I25",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I26",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I27",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I28",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I29",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I30",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I31",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31())));
  }

  private Mapped<CohortIndicator> getChildrenWithExposureToHIVCoorte9MonthsIndicator32() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I32",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWithExposureToHIVCoorte9MonthsIndicator32())));
  }

  private Mapped<CohortIndicator>
      getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I33",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33())));
  }

  private Mapped<CohortIndicator> getChildrenExposedToExclusiveBreastfeedingIndicator34() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I34",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenExposedToExclusiveBreastfeedingIndicator34())));
  }

  private Mapped<CohortIndicator> getChildrenExposedToMixedBreastfeedingIndicator35() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I35",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenExposedToMixedBreastfeedingIndicator35())));
  }

  private Mapped<CohortIndicator> getChildrenExposedWhoReceivedARVIndicator36() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I36",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getChildrenExposedWhoReceivedARVIndicator36())));
  }

  private Mapped<CohortIndicator> getTotalChildrenWhoReceivedPCRIndicator37() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I37",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getTotalChildrenWhoReceivedPCRIndicator37())));
  }

  private Mapped<CohortIndicator> getTotalChildrenWhoReceivedPCRIndicator38() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I38",
            mapStraightThrough(
                resumoMensalCCRCohortQueries.getTotalChildrenWhoReceivedPCRIndicator38())));
  }

  private Mapped<CohortIndicator> getTotalChildrenWhoReceivedPCRResultPositiveIndicator39() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I39",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getTotalChildrenWhoReceivedPCRResultPositiveIndicator39())));
  }

  private Mapped<CohortIndicator> getTotalChildrenWhoReceivedPCRResultPositiveIndicator40() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I40",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getTotalChildrenWhoReceivedPCRResultPositiveIndicator40())));
  }

  private Mapped<CohortIndicator> getChildrenWithExposureToHIVCoorte18MonthsIndicator41() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I41",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWithExposureToHIVCoorte18MonthsIndicator41())));
  }

  private Mapped<CohortIndicator>
      getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I42",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42())));
  }

  private Mapped<CohortIndicator>
      getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I43",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43())));
  }

  private Mapped<CohortIndicator> getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I44",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I45",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I46",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46())));
  }

  private Mapped<CohortIndicator>
      getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I47",
            mapStraightThrough(
                resumoMensalCCRCohortQueries
                    .getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47())));
  }
}
