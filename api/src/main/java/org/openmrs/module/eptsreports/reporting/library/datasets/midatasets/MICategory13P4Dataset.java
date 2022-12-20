package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory13P4CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets.MQAbstractDataSet;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P4Dataset extends MQAbstractDataSet {

  @Autowired private MICategory13P4CohortQueries miCategory13P4CohortQueries;

  public void constructTMiDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13P4AdultDENUMINATOR",
        "13.3: Adultos (15/+anos) na 1ª linha de TARV com registo resultado de CV acima de 1000 ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator(),
                "CAT13P4AdultDENUMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13P4AdultNUMINATOR",
        "13.3: Adultos (15/+anos) na 1ª linha de TARV com registo de pedido de CV entre o 3º e o 4º mês "
            + "após terem recebido o último resultado de CV ≥1000 cps/ml",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Numerator(),
                "CAT13P4AdultNUMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13P4ChildrenDENUMINATOR",
        "13.12: Crianças (>2 anos de idade) na 1ª linha de TARV com registo de resultado de CV ≥1000 ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator(),
                "CAT13P4ChildrenDENUMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=2-14");

    dataSetDefinition.addColumn(
        "CAT13P4ChildrenNUMINATOR",
        "13.12: Crianças (>2 anos de idade) na 1ª linha de TARV com registo de pedido de CV entre o 3º e o 4º mês "
            + "após terem recebido  o último resultado de CV ≥1000 Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPatientsWhoReceivedResultMoreThan1000CVCategory13P4Numerator(),
                "CAT13P4ChildrenNUMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=2-14");

    dataSetDefinition.addColumn(
        "CAT13P4PregnantDENUMINATOR",
        "13.18: Mulheres Gravidas na 1ª linha de TARV com registo de pedido de CV entre o 3º e o 4º "
            + "mês após terem recebido  o último resultado de CV ≥ 1000 Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPregnantWhoHaveRequestedCVCategory13P4Denumerator(),
                "CAT13P4PregnantDENUMINATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13P4PregnantNUMINATOR",
        "13.18: Mulheres gravidas na 1ª linha de TARV com registo de pedido de CV entre o 3º e o 4º "
            + "mês após terem recebido  o último resultado de CVCV ≥1000",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.miCategory13P4CohortQueries
                    .findPatientsWhoPregnantReceivedResultMoreThan1000CVCategory13P4Numerator(),
                "CAT13P4PregnantNUMINATOR",
                mappings),
            mappings),
        "");
  }
}
