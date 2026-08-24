package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory9CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets.MQAbstractDataSet;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory9DataSet extends MQAbstractDataSet {

  @Autowired private MICategory9CohortQueries miCategory9CohortQueries;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    // Adultos
    dataSetDefinition.addColumn(
        "CAT9ADULT91NUMERATOR",
        "9.1. % de adultos  (15/+anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_1(),
                "CAT9ADULT91NUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULT91DENOMINATOR",
        "9.1. % de adultos  (15/+anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+ - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1(),
                "CAT9ADULT91DENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULT92NUMERATOR",
        "9.2. % de adultos  (15/+anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_2(),
                "CAT9ADULT92NUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULT92DENOMINATOR",
        "9.2. % de adultos  (15/+anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_2(),
                "CAT9ADULT92DENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS93NUMERADOR",
        "9.3 % de adultos  (15/+anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3(),
                "CAT9ADULTOS93NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS93DENOMINADOR",
        "9.3 % de adultos  (15/+anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
                "CAT9ADULTOS93DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS94NUMERADOR",
        "9.4 % de adultos  (15/+anos) HIV+ que receberam o resultado do CD4 dentro de 33 dias  após  primeira consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4(),
                "CAT9ADULTOS94NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS94DENOMINADOR",
        "9.4 % de adultos  (15/+anos) HIV+ que receberam o resultado do CD4 dentro de 33 dias  após  primeira consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
                "CAT9ADULTOS94DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    // 9.5
    dataSetDefinition.addColumn(
        "CAT9ADULTOS95NUMERADOR",
        "9.5 % de adultos (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_5(),
                "CAT9ADULTOS95NUMERADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS95DENOMINADOR",
        "9.5 % de adultos (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_5(),
                "CAT9ADULTOS95DENOMINADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS96NUMERADOR",
        "9.6 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de TB LAM dentro de 33 dias após consulta clínica inicial do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_6(),
                "CAT9ADULTOS96NUMERADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS96DENOMINADOR",
        "9.6 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de TB LAM dentro de 33 dias após consulta clínica inicial do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_6(),
                "CAT9ADULTOS96DENOMINADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15+Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS97NUMERADOR",
        "9.7 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de CrAG Sérico dentro de 33 dias após consulta de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_7_9_15_9_16(),
                "CAT9ADULTOS97NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS97DENOMINADOR",
        "9.7 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de CrAG Sérico dentro de 33 dias após consulta de reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_7_9_8_9_15_9_16(),
                "CAT9ADULTOS97DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS98NUMERADOR",
        "9.8 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de TB LAM dentro de 33 dias após consulta de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_8(),
                "CAT9ADULTOS98NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS98DENOMINADOR",
        "9.8 % de adultos  (>=15 anos) com CD4 ≤ 200 cel/µl e com resultado de TB LAM dentro de 33 dias após consulta de reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_7_9_8_9_15_9_16(),
                "CAT9ADULTOS98DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=15+MI");

    // Criancas

    dataSetDefinition.addColumn(
        "CAT9CHILDREN99NUMERATOR",
        "9.9. % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+. - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_5(),
                "CAT9CHILDREN99NUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN99DENOMINATOR",
        "9.9. % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica depois do diagnóstico de HIV+. - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_5(),
                "CAT9ACHILDREN99DENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-Back3Months");

    dataSetDefinition.addColumn(
        "CAT9CHILDREN910NUMERATOR",
        "9.10 % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica. - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_6(),
                "CAT9CHILDREN910NUMERATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ACHILDREN910DENOMINATOR",
        "9.10. % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira consulta clínica - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6(),
                "CAT9ACHILDREN910DENOMINATOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=15-Back3Months");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS911NUMERADOR",
        "9.11 % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3(),
                "CAT9ADULTOS911NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=0-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS911DENOMINADOR",
        "9.11 % de crianças  (0-14 anos) com pedido de CD4 na primeira consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
                "CAT9ADULTOS911DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=0-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS912NUMERADOR",
        "9.12 % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro dentro de 33 dias  após  primeira consulta clínica de reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4(),
                "CAT9ADULTOS912NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=0-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS912DENOMINADOR",
        "9.12 % de crianças  (0-14 anos) HIV+ que receberam o resultado do primeiro dentro de 33 dias  após  primeira consulta clínica de reinício do TARV - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
                "CAT9ADULTOS912DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=0-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS913NUMERADOR",
        "9.13 % de crianças (10-14 anos) com CD4 ≤ 200 cel/µl e que receberam o resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_13(),
                "CAT9ADULTOS913NUMERADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=10-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS913DENOMINADOR",
        "9.13 % de crianças (10-14 anos) com CD4 ≤ 200 cel/µl e que receberam o resultado de CrAG Sérico dentro de 33 dias após consulta clínica inicial - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_13_9_14(),
                "CAT9ADULTOS913DENOMINADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=10-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS914NUMERADOR",
        "9.14 % de crianças (5-14 anos) com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após consulta clínica inicial - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_14(),
                "CAT9ADULTOS914NUMERADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=5-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS914DENOMINADOR",
        "9.14 % de crianças (5-14 anos) com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após consulta clínica inicial - Denomindaor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_13_9_14(),
                "CAT9ADULTOS914DENOMINADOR",
                mappings),
            mappings),
        "ageOnTheFirstConsultationDuringInclusionPeriod=5-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS915NUMERADOR",
        "9.15 % de crianças (10-14 anos de idade) com CD4 ≤ 200 cel/µl e que receberam o resultado de CrAG Sérico dentro de 33 dias após reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_7_9_15_9_16(),
                "CAT9ADULTOS915NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=10-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS915DENOMINADOR",
        "9.15 % de crianças (10-14 anos de idade) com CD4 ≤ 200 cel/µl e que receberam o resultado de CrAG Sérico dentro de 33 dias após reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_7_9_8_9_15_9_16(),
                "CAT9ADULTOS915DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=10-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS916NUMERADOR",
        "9.16 % de crianças (5-14 anos de idade) com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após consulta reinício do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_7_9_15_9_16(),
                "CAT9ADULTOS916NUMERADOR",
                mappings),
            mappings),
        "ageOnReinicio=5-14");

    dataSetDefinition.addColumn(
        "CAT9ADULTOS916DENOMINADOR",
        "9.16 % de crianças (5-14 anos de idade) com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após consulta reinício do TARV - Denomindor ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findDenominator_9_7_9_8_9_15_9_16(),
                "CAT9ADULTOS916DENOMINADOR",
                mappings),
            mappings),
        "ageOnReinicio=5-14");

    // Gravidas

    dataSetDefinition.addColumn(
        "CAT9PREGNANT917NUMERATOR",
        "9.17. % de MG  HIV+ com registo de pedido de CD4 na primeira CPN - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_9(),
                "CAT9PREGNANT917NUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT917DENOMINATOR",
        "9.17. % de MG  HIV+ com registo de pedido de CD4 na primeira CPN - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17(),
                "CAT9PREGNANT917DENOMINATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT918NUMERATOR",
        "9.18. % de MG  HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira CPN  - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries
                    .findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_18(),
                "CAT9PREGNANT918NUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT918DENOMINATOR",
        "9.18. % de MG  HIV+ que receberam o resultado do primeiro CD4 dentro de 33 dias  após a primeira CPN - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_18(),
                "CAT9PREGNANT918DENOMINATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT919NUMERATOR",
        "9.19. % de MG com CD4 ≤ 200 cel/µl e que recebera o resultado de CrAG Sérico dentro de 33 dias após primeira CPN  - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_19(),
                "CAT9PREGNANT919NUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT919DENOMINATOR",
        "9.19. % de MG com CD4 ≤ 200 cel/µl e que recebera o resultado de CrAG Sérico dentro de 33 dias após primeira CPN - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_19(),
                "CAT9PREGNANT919DENOMINATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT920NUMERATOR",
        "9.20. % de MG com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após primeira CPN - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory9CohortQueries.findNumerator_9_20(),
                "CAT9PREGNANT920NUMERATOR",
                mappings),
            mappings),
        "gender=F");

    dataSetDefinition.addColumn(
        "CAT9PREGNANT920DENOMINATOR",
        "9.20. % de MG com CD4 ≤ 200 cel/µl e que receberam o resultado de TB LAM dentro de 33 dias após primeira CPN - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                miCategory9CohortQueries
                    .findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_20(),
                "CAT9PREGNANT920DENOMINATOR",
                mappings),
            mappings),
        "gender=F");
  }
}
