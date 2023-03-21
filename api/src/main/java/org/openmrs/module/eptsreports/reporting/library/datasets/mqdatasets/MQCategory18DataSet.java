package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory18CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory18DataSet extends MQAbstractDataSet {
  @Autowired private MQCategory18CohortQueries mqCategory18CohortQueries;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT18CHILDREN89REVTOTALDENOMINATOR",
        "18.1: % de crianças com [8-9 anos de idade]  com registo da revelação  total do diagnóstico no primeiro ano do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory18CohortQueries
                    .findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodDenominator18(),
                "CAT18CHILDREN89REVTOTALDENOMINATOR",
                mappings),
            mappings),
        "ageMqNewART=8-9RD");

    dataSetDefinition.addColumn(
        "CAT18CHILDREN089REVTOTALNUMERATOR",
        "18.1: % de crianças com [8-9 anos de idade]  com registo da revelação  total do diagnóstico no primeiro ano do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory18CohortQueries
                    .findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodNumerator18(),
                "CAT18CHILDREN089REVTOTALNUMERATOR",
                mappings),
            mappings),
        "ageMqNewART=8-9RD");

    dataSetDefinition.addColumn(
        "CAT18CHILDREN1014REVTOTALDENOMINATOR",
        "18.1: % de crianças com [10-14 anos de idade]  com registo da revelação  total do diagnóstico no primeiro ano do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory18CohortQueries
                    .findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodDenominator18(),
                "CAT18CHILDREN1014REVTOTALDENOMINATOR",
                mappings),
            mappings),
        "ageMqNewART=10-14RD");

    dataSetDefinition.addColumn(
        "CAT18CHILDREN1014REVTOTALNUMERATOR",
        "18.1: % de crianças com [10-14 anos de idade]  com registo da revelação  total do diagnóstico no primeiro ano do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory18CohortQueries
                    .findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodNumerator18(),
                "CAT18CHILDREN1014REVTOTALNUMERATOR",
                mappings),
            mappings),
        "ageMqNewART=10-14RD");
  }
}
