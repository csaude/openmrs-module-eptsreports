package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory9CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory9DAHCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory9DataSet extends MQAbstractDataSet {

  @Autowired private MQCategory9CohortQueries mQCategory9CohortQueries;

  @Autowired private MQCategory9DAHCohortQueries mqCategory9DAHCohortQueries;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    // Adultos
    dataSetDefinition.addColumn(
        "CAT9ADUL01TNUMERATOR",
        "9.1 % de adultos  (15/+anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1ChildrenAdult(),
                "CAT9ADUL01TNUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+");

    dataSetDefinition.addColumn(
        "CAT9ADUL01TDENOMINATOR",
        "9.1 % de adultos (15/+anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
                "CAT9ADUL01TDENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+");

    dataSetDefinition.addColumn(
        "CAT9ADUL02TNUMERATOR",
        "9.2.% de adultos  (15/+anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9Section9_2(),
                "CAT9ADUL02TNUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+");

    dataSetDefinition.addColumn(
        "CAT9ADUL02TDENOMINATOR",
        "9.2. % de adultos  (15/+anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
                "CAT9ADUL02TDENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS93NUMERADOR",
        "9.3 % de adultos  (15/+anos) com pedido de CD4 na consulta clínica de reinício do TARV (1ª consulta clínica realizada após abandono) - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3(),
                "CAT9ADULTOS93NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS93DENOMINADOR",
        "9.3 % de adultos  (15/+anos) com pedido de CD4 na consulta clínica de reinício do TARV (1ª consulta clínica realizada após abandono) - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3(),
                "CAT9ADULTOS93DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS94NUMERADOR",
        "9.4 % de adultos (15/+anos) que receberam o resultado do CD4 dentro de 33 dias após  consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4(),
                "CAT9ADULTOS94NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS94DENOMINADOR",
        "9.4 % de adultos (15/+anos) que receberam o resultado do CD4 dentro de 33 dias após consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4(),
                "CAT9ADULTOS94DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+");

    // 9.5
    dataSetDefinition.addColumn(
        "CAT9ADULTOS95NUMERADOR",
        "9.5 % de adultos (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findNumerator_9_5(),
                "CAT9ADULTOS95NUMERADOR",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ADULTOS95DENOMINADOR",
        "9.5 % de adultos (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findDenominator_9_5(),
                "CAT9ADULTOS95DENOMINADOR",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.6
    dataSetDefinition.addColumn(
        "CAT9ADULTOS96NUMERADOR",
        "9.6 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de TB LAM dentro de 33 dias após consulta clínica inicial do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findNumerator_9_6(),
                "CAT9ADULTOS96NUMERADOR",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ADULTOS95DENOMINADOR",
        "9.6. % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de TB LAM dentro de 33 dias após consulta clínica inicial do TARV - Denomindor",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1Childrens(),
                "CAT9ADULTOS95DENOMINADOR",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.7
    dataSetDefinition.addColumn(
        "CAT9ADULT_97NUM",
        "9.7 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de CrAG Sérico dentro de 33 dias após consulta de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findNumeratorDahSerumCrAgResultAtRestartAdultCohort_9_7(),
                "CAT9ADULT_97NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ADULT_97DEN",
        "9.7 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de CrAG Sérico dentro de 33 dias após consulta de reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8(),
                "CAT9ADULT_97DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.8
    dataSetDefinition.addColumn(
        "CAT9ADULT_98NUM",
        "9.8 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de TB LAM dentro de 33 dias após consulta de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findNumeratorTBLAMResultAtRestartAdultCohort_9_8(),
                "CAT9ADULT_98NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ADULT_98DEN",
        "9.8 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/ml e com resultado de TB LAM dentro de 33 dias após consulta de reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8(),
                "CAT9ADULT_98DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    //// PEDIATRICO

    dataSetDefinition.addColumn(
        "CAT9CHILDREN01TNUMERATOR",
        "9.9. % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1Childrens(),
                "CAT9CHILDREN01TNUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-");

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN01TDENOMINATOR",
        "9.9 % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens(),
                "CAT9ACHILDREN01TDENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-");

    dataSetDefinition.addColumn(
        "CAT9CHILDREN02TNUMERATOR",
        "9.10 % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica  - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9SectionChildrens(),
                "CAT9CHILDREN02TNUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-");

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN02TDENOMINATOR",
        "9.10 % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens(),
                "CAT9ACHILDREN02TDENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS97NUMERADOR",
        "9.11  % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3(),
                "CAT9ADULTOS97NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15-");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS97DENOMINADOR",
        "9.11 % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3(),
                "CAT9ADULTOS97DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15-");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS98NUMERADOR",
        "9.12 % de crianças (0-14 anos) que receberam o resultado do CD4 dentro de 33 dias após consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4(),
                "CAT9ADULTOS98NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=CD4-15-");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS98DENOMINADOR",
        "9.12 % de crianças (0-14 anos) que receberam o resultado do CD4 dentro de 33 dias após consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4(),
                "CAT9ADULTOS98DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=CD4-15-");

    // 9.13
    dataSetDefinition.addColumn(
        "CAT9ACHILDREN10_14_913TNUM",
        "9.13 % de crianças (10-14 anos) com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findNumeratorCraigResultChildren_9_13(),
                "CAT9ACHILDREN10_14_913TNUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN10_14_913DEN",
        "9.13 % de crianças (10-14 anos) com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findDAHDenominatorCraigTblamChildren_9_13(),
                "CAT9ACHILDREN10_14_913DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.14
    dataSetDefinition.addColumn(
        "CAT9ACHILDREN10_14_914TNUM",
        "9.14 % de crianças (5-14 anos) com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após consulta clínica inicial - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findNumeratorTbLamPediatric_9_14(),
                "CAT9ACHILDREN10_14_914TNUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN10_14_914DEN",
        "9.14  % de crianças (5-14 anos) com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após consulta clínica inicial - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findDAHDenominatorDAHTBLAMResultPedriatic_9_14(),
                "CAT9ACHILDREN10_14_914DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.15
    dataSetDefinition.addColumn(
        "CAT9PEDIATRIC_915NUM",
        "9.15 % de crianças (10-14 anos de idade) com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findNumeratorDahSerumCrAgResultAtRestartChildrenCohort_9_15(),
                "CAT9PEDIATRIC_915NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9PEDIATRIC_915DEN",
        "9.15 % de crianças (10-14 anos de idade) com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findDenominatorSerumCraigResultAtRestartChildren_9_15(),
                "CAT9PEDIATRIC_915DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.16
    dataSetDefinition.addColumn(
        "CAT9PEDIATRIC_916NUM",
        "9.16  % de crianças (5-14 anos de idade) com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após consulta reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findNumeratorTBLAMResultAtRestartChildrenCohort_9_16(),
                "CAT9PEDIATRIC_916NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9PEDIATRIC_916DEN",
        "9.16 % de crianças (5-14 anos de idade) com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após consulta reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries.findDenominatorTBLAMResultAtRestartChildren_9_16(),
                "CAT9PEDIATRIC_916DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    ///// FIM Pediatrico

    // Inicio Pregnant
    dataSetDefinition.addColumn(
        "CAT9PREGNANT01TNUMERATOR",
        "9.17. % de MG  HIV+ com registo de pedido de CD4 na primeira CPN - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1(),
                "CAT9PREGNANT01TNUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT01TDENOMINATOR",
        "9.17. % de MG  HIV+ com registo de pedido de CD4 na primeira CPN. - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(),
                "CAT9PREGNANT01TDENOMINATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT02TNUMERATOR",
        "9.18 % de MG  HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira CPN  - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory9CohortQueries
                    .findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9Section9_2(),
                "CAT9PREGNANT02TNUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT02TDENOMINATOR",
        "9.18 % de MG  HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira CPN- Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(),
                "CAT9PREGNANT02TDENOMINATOR",
                mappings),
            mappings),
        "gender=F");

    // 9.19
    dataSetDefinition.addColumn(
        "CAT9PREGNANT_919NUM",
        "9.19 % de MG com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após primeira CPN - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findNumeratorDahSerumCrAgResultPregnantWomenCohort_9_19(),
                "CAT9PREGNANT_919NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9PREGNANT_919DEN",
        "9.19 % de MG com CD4 ≤ 200 cel/ml e que receberam o resultado de CrAG Sérico dentro de 33 dias após primeira CPN - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findDenominatorDAHCraigSericAndTblamPregnants_9_19(),
                "CAT9PREGNANT_919DEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // 9.20
    dataSetDefinition.addColumn(
        "CAT9PREGNANT_920NUM",
        "9.20 % de MG com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após primeira CPN - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findNumeratorDahTbLamResultPregnantWomenCohort_9_20(),
                "CAT9PREGNANT_920NUM",
                mappings),
            mappings),
        StringUtils.EMPTY);

    dataSetDefinition.addColumn(
        "CAT9PREGNANT_920TDEN",
        "9.20 % de MG com CD4 ≤ 200 cel/ml e que receberam o resultado de TB LAM dentro de 33 dias após primeira CPN - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory9DAHCohortQueries
                    .findDenominatorDAHCraigSericAndTblamPregnants_9_19(),
                "CAT9PREGNANT_920TDEN",
                mappings),
            mappings),
        StringUtils.EMPTY);

    // Fim pregnant
  }
}
