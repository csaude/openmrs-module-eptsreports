package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries.ARTSituation;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries.MCCTreatment;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries.PeriodoAbandono;
import org.openmrs.module.eptsreports.reporting.library.queries.TypesOfExams;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalDAHCohortQueries {

  private GenericCohortQueries genericCohortQueries;
  @Autowired ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired
  public ResumoMensalDAHCohortQueries(GenericCohortQueries genericCohortQueries) {
    this.genericCohortQueries = genericCohortQueries;
  }

  /** 0 - Número total de activos em DAH em TARV, até ao fim do mês anterior */
  public CohortDefinition
      getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicatorI0() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    final String mappings = "startDate=${startDate-1d},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "ACTIVEDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0()),
            mappings));

    definition.setCompositionString("ACTIVEDAH");

    return definition;
  }

  public CohortDefinition getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfMonthIndicatorI5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    final String mappings = "startDate=${startDate-1d},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "ACTIVEDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfMonthIndicator5",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfMonthIndicator5()),
            mappings));

    definition.setCompositionString("ACTIVEDAH");

    return definition;
  }

  /** RF31 - Utentes Novos Inscritos (Pré-TARV) ou Novos Inícios (TARV) */
  public CohortDefinition findPatientsNewEnrolledOnPreTarvOrNewEnrolledOnTarvRF31() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte3Months =
        "startDate=${startDate-6m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "INICIOTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.NEW_ENROLLED)),
            mappings));

    definition.addSearch(
        "PRE-TARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.PRE_TARV)),
            mappings));

    definition.addSearch(
        "DAH-WITHOUT-SITTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked",
                ResumoMensalDAHQueries.findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked()),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1(),
            mappingsCoorte3Months));

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2(),
            mappingsCoorte3Months));

    definition.setCompositionString(
        "INICIOTARV OR PRE-TARV OR (DAH-WITHOUT-SITTARV AND (B1 OR A2))");

    return definition;
  }

  /** RF32 - Utentes Reinícios TARV */
  public CohortDefinition findPatientsWhoReinitiatedTarvRF32() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "REINICIOTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsReinitiatedARTDuringReportPeriod2",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.RESTART)),
            mappings));

    definition.addSearch(
        "DAH-WITHOUT-SITTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked",
                ResumoMensalDAHQueries.findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked()),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.setCompositionString("REINICIOTARV OR (DAH-WITHOUT-SITTARV AND B3)");

    return definition;
  }

  /** RF33 - Utentes Activos TARV */
  public CohortDefinition findPatientsWhoAreActiveOnTarvRF33() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsMinus4Months =
        "startDate=${startDate-4m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "EMTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsActiveInARTDuringReportPeriod3",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.ACTIVE)),
            mappings));

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12(),
            mappingsMinus4Months));

    definition.addSearch(
        "DAH-WITHOUT-SITTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked",
                ResumoMensalDAHQueries.findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked()),
            mappings));

    definition.setCompositionString("EMTARV OR (DAH-WITHOUT-SITTARV AND B12)");

    return definition;
  }

  /** RF26 - Relatório Desagregação - Novos inícios TARV – Indicadores 10 a 19 */
  public CohortDefinition findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF31",
        EptsReportUtils.map(
            this.findPatientsNewEnrolledOnPreTarvOrNewEnrolledOnTarvRF31(), mappings));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.setCompositionString("RF31 NOT PREGNANT");

    return definition;
  }

  /** RF26.1 - Relatório Desagregação - Novos inícios TARV – Indicadores 8 e 9 */
  public CohortDefinition findPatientsWhoAreNovosIniciosTarvIndicator8And9RF26_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte3Months =
        "startDate=${startDate-6m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1(),
            mappingsCoorte3Months));

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2(),
            mappingsCoorte3Months));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.setCompositionString("(B1 OR A2) NOT PREGNANT");

    return definition;
  }

  /** RF27 - Relatório Desagregação - Reinícios TARV - Indicadores 10 a 19 */
  public CohortDefinition findPatientsWhoAreReiniciosTarvIndicator10Until19RF27() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoAreReiniciosTarvIndicator10Until19RF27");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAreReiniciosTarvIndicator10Until19RF27");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF32", EptsReportUtils.map(this.findPatientsWhoReinitiatedTarvRF32(), mappings));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.addSearch(
        "RF26",
        EptsReportUtils.map(
            this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

    definition.setCompositionString("RF32 NOT (PREGNANT OR RF26)");

    return definition;
  }

  /** RF27.1 - Relatório Desagregação - Reinícios TARV - Indicadores 8 e 9 */
  public CohortDefinition findPatientsWhoAreReiniciosTarvIndicator8And9RF27_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoArereiniciosTarvIndicator8And9RF27_1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.addSearch(
        "RF26-1",
        EptsReportUtils.map(
            this.findPatientsWhoAreNovosIniciosTarvIndicator8And9RF26_1(), mappings));

    definition.setCompositionString("B3 NOT (PREGNANT OR RF26-1)");

    return definition;
  }

  /** RF28 - Relatório Desagregação - Activos TARV – Indicadores 10 a 19 */
  public CohortDefinition findPatientsWhoAreActiveInTarvIndicator10Untill19RF28() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoArereiniciosTarvIndicator8And9RF27_1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF33", EptsReportUtils.map(this.findPatientsWhoAreActiveOnTarvRF33(), mappings));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.addSearch(
        "RF27",
        EptsReportUtils.map(
            this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

    definition.addSearch(
        "RF26",
        EptsReportUtils.map(
            this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

    definition.setCompositionString("RF33 NOT (PREGNANT OR RF27 OR RF26)");

    return definition;
  }

  /** RF28.1 - Relatório Desagregação - Activos TARV – Indicadores 8 a 9 */
  public CohortDefinition findPatientsWhoAreActiveInTarvIndicator8And9RF28_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoArereiniciosTarvIndicator8And9RF27_1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("findPatientsWhoReinitiatedTarvRF32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12(),
            mappingsCoorte4Months));

    definition.addSearch(
        "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

    definition.addSearch(
        "RF27-1",
        EptsReportUtils.map(this.findPatientsWhoAreReiniciosTarvIndicator8And9RF27_1(), mappings));

    definition.addSearch(
        "RF26-1",
        EptsReportUtils.map(
            this.findPatientsWhoAreNovosIniciosTarvIndicator8And9RF26_1(), mappings));

    definition.setCompositionString("B12 NOT (PREGNANT OR RF27-1 OR RF26-1)");

    return definition;
  }

  /**
   * 1 - Número de utentes novos inícios em TARV que iniciaram seguimento para Doença Avançada por
   * HIV durante o mês
   */
  public CohortDefinition getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "RF31",
        EptsReportUtils.map(
            this.findPatientsNewEnrolledOnPreTarvOrNewEnrolledOnTarvRF31(), mappings));

    definition.setCompositionString("DAHPERIOD AND RF31");

    return definition;
  }

  /**
   * 2 - Número de utentes reinícios em TARV que iniciaram seguimento para Doença Avançada por HIV
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWhoReinitiatedARTWhoInitiatedDAHDuringReportPeriodI2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "RF32", EptsReportUtils.map(this.findPatientsWhoReinitiatedTarvRF32(), mappings));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1(),
            mappings));

    definition.setCompositionString("(DAHPERIOD AND RF32) NOT I1");

    return definition;
  }

  /**
   * 3 - Número de utentes activos em TARV que iniciaram seguimento para Doença Avançada por HIV
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsCurrentInARTWhoInitiatedDAHDuringReportPeriodI3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "RF33", EptsReportUtils.map(this.findPatientsWhoAreActiveOnTarvRF33(), mappings));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.getNumberOfPatientsWhoReinitiatedARTWhoInitiatedDAHDuringReportPeriodI2(),
            mappings));

    definition.addSearch(
        "I2",
        EptsReportUtils.map(
            this.getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1(),
            mappings));

    definition.setCompositionString("(DAHPERIOD AND RF33) NOT (I1 OR I2)");

    return definition;
  }

  /**
   * 4 - Número de utentes em seguimento para Doença Avançada que sairam da abordagem durante o mês
   * por qualquer motivo (saída por alta, óbito, transferido, abandono)
   */
  public CohortDefinition getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHBEFOREENDDATE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithDAHBeforeEndDate",
                ResumoMensalDAHQueries.findPatientsWithDAHBeforeEndDate()),
            mappings));

    definition.addSearch(
        "SAIDA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithDAHBeforeEndDate",
                ResumoMensalDAHQueries
                    .findPatientsMarkedAsObitoOrAbandonoOrTransferredOutDuringPeriod()),
            mappings));

    definition.addSearch(
        "DATA-DE-SAIDA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithDatadeSaidaDuringPeriod",
                ResumoMensalDAHQueries.findPatientsWithDatadeSaidaDuringPeriod()),
            mappings));

    definition.setCompositionString("DAHBEFOREENDDATE AND (SAIDA OR DATA-DE-SAIDA)");

    return definition;
  }

  /** 6 - Número total de óbitos nos utentes com diagnóstico de Doença Avançada na coorte */
  public CohortDefinition getTotalNumberOfPatientsWhoAreDeadWithDAHDiagnosticI6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TotalNumberOfPatientsWhoAreDeadWithDAHDiagnosticI6");
    final String mappings6Months =
        "startDate=${startDate-7m},endDate=${startDate-6m-1d},location=${location}";
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHIN6MONTHSCOORTE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithDAHInSixMonthsCoorte",
                ResumoMensalDAHQueries.getNumberOfPatientsWithDAHInSixMonthsCoorte()),
            mappings6Months));

    definition.addSearch(
        "DEADDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6",
                ResumoMensalDAHQueries.getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6()),
            mappings));

    definition.addSearch(
        "B8",
        EptsReportUtils.map(resumoMensalCohortQueries.getPatientsWhoDiedTratmentB8(), mappings));

    definition.setCompositionString("DAHIN6MONTHSCOORTE AND (DEADDAH OR B8)");

    return definition;
  }

  /** 7 - Número total de novos inscritos em seguimento de DAH na coorte */
  public CohortDefinition getTotalNumberPatientsInDAHSixMonthsCoorteI7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    final String mappings6Months =
        "startDate=${startDate-7m},endDate=${startDate-6m-1d},location=${location}";

    definition.setName("getTotalNumberPatientsInDAHSixMonthsCoorteI7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAH6MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithDAHInSixMonthsCoorte",
                ResumoMensalDAHQueries.getNumberOfPatientsWithDAHInSixMonthsCoorte()),
            mappings6Months));

    definition.setCompositionString("DAH6MONTHS");

    return definition;
  }

  /** 8 - Número de utentes com pedido de CD4 de rastreio durante o mês */
  public CohortDefinition getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsMinus4Months =
        "startDate=${startDate-4m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8",
                ResumoMensalDAHQueries.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriod8()),
            mappingsMinus4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26-1",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator8And9RF26_1(), mappings));

      composition = "CD4 AND RF26-1";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27-1",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator8And9RF27_1(), mappings));

      composition = "CD4 AND RF27-1";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28-1",
          EptsReportUtils.map(this.findPatientsWhoAreActiveInTarvIndicator8And9RF28_1(), mappings));

      composition = "CD4 AND RF28-1";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "CD4 AND PREGNANT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 9 - Número de utentes com resultado de CD4 de rastreio disponivel durante o mês */
  public CohortDefinition getNumberOfPatientWithCD4ResultDuringThePeriodI9(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithCD4ResultDuringThePeriodI9");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte5Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithCD4ResultDuringThePeriodI9");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithCD4ResultDuringThePeriod9",
                ResumoMensalDAHQueries.getNumberOfPatientWithCD4ResultDuringThePeriod9()),
            mappingsCoorte5Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "I8-NEW",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
                  ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "CD4-RESULT AND I8-NEW";

    } else if (disagregationType == ARTSituation.RESTART) {
      definition.addSearch(
          "I8-RESTART",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(ARTSituation.RESTART),
              mappings));

      composition = "CD4-RESULT AND I8-RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "I8-ACTIVE",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(ARTSituation.ACTIVE),
              mappings));

      composition = "CD4-RESULT AND I8-ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "I8-PREGNANT",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
                  ARTSituation.PREGNANT),
              mappings));

      composition = "CD4-RESULT AND I8-PREGNANT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 10 - Número de utentes com resultado de CD4 baixo* durante o mês */
  public CohortDefinition getNumberOfPatientWithLowCD4ResultDuringThePeriodI10(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte5Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "SEGUIMENTO-DAH",
        EptsReportUtils.map(
            this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

    definition.addSearch(
        "CD4-BAIXO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCoorte5Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      definition.addSearch(
          "I8-NEW",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
                  ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND RF26 AND I8-NEW";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      definition.addSearch(
          "I8-RESTART",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(ARTSituation.RESTART),
              mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND RF27 AND I8-RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      definition.addSearch(
          "I8-ACTIVE",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(ARTSituation.ACTIVE),
              mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND RF28 AND I8-ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      definition.addSearch(
          "I8-PREGNANT",
          EptsReportUtils.map(
              this.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
                  ARTSituation.PREGNANT),
              mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND PREGNANT AND I8-PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 11 - Número de utentes elegíveis a pedido de TB-LAM com resultado de TB-LAM durante o mês */
  public CohortDefinition getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "SEGUIMENTO-DAH",
        EptsReportUtils.map(
            this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

    definition.addSearch(
        "CD4-BAIXO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCoorte4Months));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriod11",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.TBLAM)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND TBLAM AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND TBLAM AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND TBLAM AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND TBLAM AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND TBLAM";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 12) Número de utentes com resultado de TB-LAM positivo durante o mês */
  public CohortDefinition
      getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I11",
        EptsReportUtils.map(
            this.getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11(
                ARTSituation.SEGUIMENTO_DAH),
            mappings));

    definition.addSearch(
        "CD4-TBLAM-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.TBLAM_POS)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "I11 AND CD4-TBLAM-POS";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 13 - Número de utentes com CD4 baixo* com resultado de CrAg sérico durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "SEGUIMENTO-DAH",
        EptsReportUtils.map(
            this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

    definition.addSearch(
        "CD4-BAIXO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCoorte4Months));

    definition.addSearch(
        "CRAG-SORO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoResultDuringReportingPeriod13",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND CRAG-SORO AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND CRAG-SORO AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND CRAG-SORO AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND CRAG-SORO AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "SEGUIMENTO-DAH AND CD4-BAIXO AND CRAG-SORO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 14 - Número de utentes com CD4 baixo* com resultado de CrAg sérico positivo durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I13",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13(
                ARTSituation.SEGUIMENTO_DAH),
            mappings));

    definition.addSearch(
        "CRAG-SORO-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoPOSResultDuringReportingPeriod14",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO_POS)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "I13 AND CRAG-SORO-POS AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "I13 AND CRAG-SORO-POS AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "I13 AND CRAG-SORO-POS AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I13 AND CRAG-SORO-POS AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "I13 AND CRAG-SORO-POS";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 15) Número de utentes com CrAg sérico positivo e com o resultado de CrAg no LCR registrado
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithCragLCRResultDuringReportingPeriodI15");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(
                ARTSituation.SEGUIMENTO_DAH),
            mappings));

    definition.addSearch(
        "CRAG-LCR",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_LCR)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "I14 AND CRAG-LCR AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "I14 AND CRAG-LCR AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "I14 AND CRAG-LCR AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND CRAG-LCR AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "I14 AND CRAG-LCR";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 16) Número de utentes com CrAg sérico positivo que iniciaram tratamento preventivo de MCC
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(
                ARTSituation.SEGUIMENTO_DAH),
            mappings));

    definition.addSearch(
        "MCC-PREVENTIVE-TREATMENT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(
                    MCCTreatment.MCC_PREVENTIVE_TREATMENT)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 17) Número de utentes com CrAg no LCR positivo que iniciaram tratamento de 1a escolha de MCC
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "NumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(
                ARTSituation.SEGUIMENTO_DAH),
            mappings));

    definition.addSearch(
        "CRAG-LCR-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_LCR_POS)),
            mappingsCoorte4Months));

    definition.addSearch(
        "MCC-TREATMENT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(MCCTreatment.MCC_TREATMENT)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 18) Número de utentes com Sarcoma de Kaposi e com indicação para o tratamento de quimioterapia
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "NumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "SEGUIMENTO-DAH",
        EptsReportUtils.map(
            this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

    definition.addSearch(
        "SK",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriod18()),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "SEGUIMENTO-DAH AND SK AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "SEGUIMENTO-DAH AND SK AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "SEGUIMENTO-DAH AND SK AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "SEGUIMENTO-DAH AND SK AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "SEGUIMENTO-DAH AND SK";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 19) Número de utentes com sarcoma de kaposi que iniciaram Ciclo 1 de quimioterapia durante o
   * mês
   */
  public CohortDefinition getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte4Months =
        "startDate=${startDate-4m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "SEGUIMENTO-DAH",
        EptsReportUtils.map(
            this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

    definition.addSearch(
        "QUIMIOTHERAPHY",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(
                    MCCTreatment.QUIMIOTHERAPHY_CICLE_ONE)),
            mappingsCoorte4Months));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "RF26",
          EptsReportUtils.map(
              this.findPatientsWhoAreNovosIniciosTarvIndicator10Untill9RF26(), mappings));

      composition = "SEGUIMENTO-DAH AND QUIMIOTHERAPHY AND RF26";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RF27",
          EptsReportUtils.map(
              this.findPatientsWhoAreReiniciosTarvIndicator10Until19RF27(), mappings));

      composition = "SEGUIMENTO-DAH AND QUIMIOTHERAPHY AND RF27";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "RF28",
          EptsReportUtils.map(
              this.findPatientsWhoAreActiveInTarvIndicator10Untill19RF28(), mappings));

      composition = "SEGUIMENTO-DAH AND QUIMIOTHERAPHY AND RF28";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "SEGUIMENTO-DAH AND QUIMIOTHERAPHY AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      composition = "SEGUIMENTO-DAH AND QUIMIOTHERAPHY";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** Relatório Desagregação Utentes em Seguimento de DAH */
  public CohortDefinition getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF30",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsInDAHExcludingPatientsWithSaidaDAH",
                ResumoMensalDAHQueries.findPatientsInDAHExcludingPatientsWithSaidaDAH()),
            mappings));

    definition.setCompositionString("RF30");

    return definition;
  }

  /** RF29 Relatório Desagregação Mulheres Grávidas HIV+ */
  public CohortDefinition getNumberOfPatientsWhoArePregnantRF29() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings =
        "startDate=${startDate-7m},endDate=${startDate-3m-1d},location=${location}";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF29",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate",
                ResumoMensalDAHQueries.findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate()),
            mappings));

    definition.setCompositionString("RF29");

    return definition;
  }

  public CohortDefinition getSumB3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappingsAbandonoPeriod1 = "endDate=${startDate-5m-1d},location=${location}";
    final String mappingsPickUpPeriod1 =
        "startDate=${startDate-6m},endDate=${startDate-5m-1d},location=${location}";

    final String mappingsAbandonoPeriod2 = "endDate=${startDate-4m-1d},location=${location}";
    final String mappingsPickUpPeriod2 =
        "startDate=${startDate-5m},endDate=${startDate-4m-1d},location=${location}";

    final String mappingsAbandonoPeriod3 = "endDate=${startDate-3m-1d},location=${location}";
    final String mappingsPickUpPeriod3 =
        "startDate=${startDate-4m},endDate=${startDate-3m-1d},location=${location}";

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    // Suspenso e reinicio primeiro periodo
    definition.addSearch(
        "ABANDONED-PERIODO1",
        EptsReportUtils.map(this.getAbandonedPatient(PeriodoAbandono.PRIMEIRO), mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappingsAbandonoPeriod1));

    definition.addSearch(
        "PICKUP",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PICKUP", ResumoMensalDAHQueries.getPatientsWhoHaveDrugPickup()),
            mappingsPickUpPeriod1));

    // Suspenso e reinicio segundo periodo

    definition.addSearch(
        "ABANDONED-PERIODO2",
        EptsReportUtils.map(this.getAbandonedPatient(PeriodoAbandono.SEGUNDO), mappings));

    definition.addSearch(
        "SUSPEND-PERIOD2",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappingsAbandonoPeriod2));

    definition.addSearch(
        "PICKUP-PERIOD2",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PICKUP", ResumoMensalDAHQueries.getPatientsWhoHaveDrugPickup()),
            mappingsPickUpPeriod2));

    // Suspenso e reinicio terceiro periodo
    definition.addSearch(
        "ABANDONED-PERIODO3",
        EptsReportUtils.map(this.getAbandonedPatient(PeriodoAbandono.TERCEIRO), mappings));

    definition.addSearch(
        "SUSPEND-PERIOD3",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappingsAbandonoPeriod3));

    definition.addSearch(
        "PICKUP-PERIOD3",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PICKUP", ResumoMensalDAHQueries.getPatientsWhoHaveDrugPickup()),
            mappingsPickUpPeriod3));

    definition.setCompositionString(
        "((ABANDONED-PERIODO1 OR SUSPEND) AND PICKUP) OR ((ABANDONED-PERIODO2 OR SUSPEND-PERIOD2) AND PICKUP-PERIOD2) OR ((ABANDONED-PERIODO3 OR SUSPEND-PERIOD3) AND PICKUP-PERIOD3)");

    return definition;
  }

  public CohortDefinition getAbandonedPatient(PeriodoAbandono periodo) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String mappings = "";

    if (periodo == PeriodoAbandono.PRIMEIRO) {
      mappings = "endDate=${startDate-5m-1d},location=${location}";
    } else if (periodo == PeriodoAbandono.SEGUNDO) {
      mappings = "endDate=${startDate-4m-1d},location=${location}";
    } else if (periodo == PeriodoAbandono.TERCEIRO) {
      mappings = "endDate=${startDate-3m-1d},location=${location}";
    }

    definition.addSearch(
        "ABANDONED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ABANDONED", ResumoMensalDAHQueries.getPatientsWhoAbandonedTratmentToBeExclude()),
            mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "DIED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DIED", ResumoMensalDAHQueries.getPatientsWhoDiedTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "TRFOUT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRFOUT",
                ResumoMensalDAHQueries.getPatientsTransferredFromAnotherHealthFacilityLastMonth()),
            mappings));

    definition.setCompositionString("(ABANDONED NOT (SUSPEND OR DIED OR TRFOUT))");

    return definition;
  }
}
