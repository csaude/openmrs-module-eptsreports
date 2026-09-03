package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory19CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory9CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mi.MICategory9DAHQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MICategory9QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory9CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;
  @Autowired private MQCategory9CohortQueries mQCategory9CohortQueries;
  @Autowired private MQCategory19CohortQueries mqCategory19CohortQueries;

  @DocumentedDefinition(value = "findPatientsFirstConsultationOnInclusionDate")
  public CohortDefinition findPatientsFirstConsultationOnInclusionDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MICategory9QueriesInterface.QUERY.findPatientsFirstConsultationOnInclusionDate;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantDuringInclusionPeriod")
  public CohortDefinition findPatientsWhoArePregnantDuringInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.PREGNANT);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingInFirstConsultation")
  public CohortDefinition findPatientsWhoAreBreastfeedingInFirstConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingInFirstConsultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.BREASTFEEDING);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingDuringInclusionPeriod")
  public CohortDefinition findPatientsWhoAreBreastfeedingDuringInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.BREASTFEEDING);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9")
  public CohortDefinition
      findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY
            .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9")
  public CohortDefinition
      findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY
            .findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9")
  public CohortDefinition
      findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY
            .findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9")
  public CohortDefinition
      findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY
            .findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9")
  public CohortDefinition
      findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory9QueriesInterface.QUERY
            .findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantDuringPreviousPeriod")
  public CohortDefinition findPatientsWhoArePregnantDuringPreviousPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringPreviousPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MICategory9QueriesInterface.QUERY.findPatientsWhoArePregnantDuringPreviousPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionPeriodDenominatorCategory9")
  public CohortDefinition
      findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionPeriodDenominatorCategory9() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionPeriodDenominatorCategory9");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        "select patient_id from ("
            + MICategory9DAHQueriesInterface.QUERY
                .findPatientsWithCD4ResultInClinicalConsultationBetweenTheFirstCCPlus33Days
            + ") t ";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDAHNumeratorSerumCrAgResultCohort")
  private CohortDefinition findDAHNumeratorSerumCrAgResulCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDAHNumeratorSerumCrAgResult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query = MICategory9DAHQueriesInterface.QUERY.findDAHNumeratorSerumCrAgResult;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findCd4Result33DaysAfterRestartClinicalConsultation")
  private CohortDefinition findCd4Result33DaysAfterRestartClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findCd4Result33DaysAfterRestartClinicalConsultation");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MICategory9DAHQueriesInterface.QUERY
                .findCd4Result33DaysAfterRestartClinicalConsultation
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahSerumCrAgResultAtRestart")
  private CohortDefinition findDahSerumCrAgResultAtRestart() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahSerumCrAgResultAtRestart");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MICategory9DAHQueriesInterface.QUERY.findDahSerumCrAgResultAtRestart
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResultAtRestart")
  private CohortDefinition findDahTbLamResultAtRestart() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResultAtRestart");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MICategory9DAHQueriesInterface.QUERY.findDahTbLamResultAtRestart
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResult")
  private CohortDefinition findDahTbLamResult() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query = MICategory9DAHQueriesInterface.QUERY.findDahTbLamResult;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(this.findPatientsFirstConsultationOnInclusionDate(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappings));

    definition.setCompositionString(
        "FIRST-CONSULTATION NOT (PREGNANT OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_2")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_2");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsBackThreeMonths =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(
            this.findPatientsFirstConsultationOnInclusionDate(), mappingsBackThreeMonths));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappingsBackThreeMonths));

    definition.setCompositionString(
        "FIRST-CONSULTATION NOT (PREGNANT OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4")
  public CohortDefinition
      findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsBackThreeMonths =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate-2m},location=${location}";

    definition.addSearch(
        "REINICIO",
        EptsReportUtils.map(
            mQCategory9CohortQueries.findPatientsWhoReinitiatedTreatmentCat9RF29(),
            mappingsBackThreeMonths));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE-RF10-1",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.setCompositionString(
        "REINICIO NOT (TRANSFERED-IN OR PREGNANT-INCLUSION-DATE-RF10-1)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3")
  public CohortDefinition
      findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioAfterAbandonedTreatmentNumerator9_3");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsBackThreeMonths =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate-2m},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-3",
        EptsReportUtils.map(
            this
                .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
            mappings));

    definition.addSearch(
        "REINICIO-PEDIDO-CD4",
        EptsReportUtils.map(
            mQCategory9CohortQueries.findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica(),
            mappingsBackThreeMonths));

    definition.setCompositionString("(DENOMINATOR-9-3 AND REINICIO-PEDIDO-CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4")
  public CohortDefinition
      findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultPatientsWithCD4Result33DaysAfterClinicalConsultationMarkedWithReinicioARTAndPedidoCd4Numerator9_4");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsBackThreeMonths =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate-2m},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-4",
        EptsReportUtils.map(
            this
                .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3_9_4(),
            mappings));

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(
            mQCategory9CohortQueries
                .findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio(),
            mappingsBackThreeMonths));

    definition.setCompositionString("DENOMINATOR-9-4 AND CD4-RESULT");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_5")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_5");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(this.findPatientsFirstConsultationOnInclusionDate(), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(),
            mappingsFirstConsultation));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(),
            mappingsFirstConsultation));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsFirstConsultation));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappings));

    definition.addSearch(
        "BREASTFEEDING-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoAreBreastfeedingDuringInclusionPeriod(), mappings));

    definition.setCompositionString(
        "FIRST-CONSULTATION NOT (PREGNANT OR BREASTFEEDING OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE OR BREASTFEEDING-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsBackThreeMonths =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(
            this.findPatientsFirstConsultationOnInclusionDate(), mappingsBackThreeMonths));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(),
            mappingsFirstConsultation));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(),
            mappingsFirstConsultation));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsFirstConsultation));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappingsBackThreeMonths));

    definition.addSearch(
        "BREASTFEEDING-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoAreBreastfeedingDuringInclusionPeriod(), mappingsBackThreeMonths));

    definition.setCompositionString(
        "FIRST-CONSULTATION NOT (PREGNANT OR BREASTFEEDING OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE OR BREASTFEEDING-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_5")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_5");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_5(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_1")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_1");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_9")
  public CohortDefinition
      findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9_9_9");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17(),
            mappingsForDenominator));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPregnantWomanInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1")
  public CohortDefinition
      findPregnantWomanInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_1(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_2")
  public CohortDefinition
      findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_2");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_2(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4-33-DAYS)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_6")
  public CohortDefinition
      findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_6");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4-33-DAYS)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_4_Numerator")
  public CohortDefinition
      findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_4_Numerator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9_9_6(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4-33-DAYS)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_18")
  public CohortDefinition
      findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9_9_18");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_18(),
            mappingsForDenominator));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4-33-DAYS)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.setCompositionString("PREGNANT-INCLUSION-DATE NOT TRANSFERED-IN ");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_18")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_18");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.setCompositionString("PREGNANT-INCLUSION-DATE NOT TRANSFERED-IN ");

    return definition;
  }

  @DocumentedDefinition(value = "findPragnantWomanWhoHaveAreFirstConsultationNumeratorCategory9")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationNumeratorCategory9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationNumeratorCategory9");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoHaveAreCd433DaysAfterFirstConsultationNumeratorCategory9")
  public CohortDefinition
      findPragnantWomanWhoHaveAreCd433DaysAfterFirstConsultationNumeratorCategory9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationNumeratorCategory9");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_17(),
            mappingsFirstConsultation));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappings));

    definition.setCompositionString("(DENOMINATOR AND CD4-33-DAYS)");

    return definition;
  }

  // FR29 - DAH: Resultado de CrAG sérico e TB_LAM - Adulto
  @DocumentedDefinition(value = "DenominatorCategory9_9_5")
  public CohortDefinition findDenominator_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DenominatorCategory9_9_5");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(this.findPatientsFirstConsultationOnInclusionDate(), mappings));

    definition.addSearch(
        "CD4-RESULT-IN-33DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionPeriodDenominatorCategory9(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-IN-ART-START",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "PREGNANT-IN-INITIAL-CONSULTATION",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappings));

    definition.setCompositionString(
        "(FIRST-CONSULTATION AND CD4-RESULT-IN-33DAYS) NOT (TRANSFERED-IN OR PREGNANT-IN-ART-START OR  PREGNANT-IN-INITIAL-CONSULTATION)");

    return definition;
  }

  @DocumentedDefinition(value = "findDAHNumeratorSerumCrAgResultAdult")
  public CohortDefinition findNumerator_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DAHNumeratorSerumCrAgResultAdult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.findDenominator_9_5(), mappingsForDenominator));

    definition.addSearch(
        "NUMERATOR", EptsReportUtils.map(this.findDAHNumeratorSerumCrAgResulCohort(), mappings));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominator_9_6")
  public CohortDefinition findDenominator_9_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominator_9_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate-2m},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-5", EptsReportUtils.map(this.findDenominator_9_5(), mappingsForDenominator));

    definition.addSearch(
        "TB-ACTIVE",
        EptsReportUtils.map(
            mqCategory19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActive(), mappings));

    definition.setCompositionString("DENOMINATOR-9-5 NOT TB-ACTIVE");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_6")
  public CohortDefinition findNumerator_9_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-6", EptsReportUtils.map(this.findDenominator_9_6(), mappingsForDenominator));

    definition.addSearch("TB-LAM", EptsReportUtils.map(this.findDahTbLamResult(), mappings));

    definition.setCompositionString("DENOMINATOR-9-6 AND TB-LAM");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominator_9_7_9_8_9_15_9_16")
  public CohortDefinition findDenominator_9_7_9_8_9_15_9_16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominator_9_7_9_8_9_15_9_16");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(this.findCd4Result33DaysAfterRestartClinicalConsultation(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE-RF10-1",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.setCompositionString(
        "CD4-RESULT NOT (TRANSFERED-IN OR PREGNANT-INCLUSION-DATE-RF10-1)");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_7_9_15")
  public CohortDefinition findNumerator_9_7_9_15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_7_9_15");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-7-9-8",
        EptsReportUtils.map(this.findDenominator_9_7_9_8_9_15_9_16(), mappingsForDenominator));

    definition.addSearch(
        "CRAG-RESULT", EptsReportUtils.map(this.findDahSerumCrAgResultAtRestart(), mappings));

    definition.setCompositionString("DENOMINATOR-9-7-9-8 AND CRAG-RESULT");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_8_9_16")
  public CohortDefinition findNumerator_9_8_9_16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_8_9_16");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-9-7-9-8",
        EptsReportUtils.map(this.findDenominator_9_7_9_8_9_15_9_16(), mappingsForDenominator));

    definition.addSearch(
        "TB-LAM", EptsReportUtils.map(this.findDahTbLamResultAtRestart(), mappings));

    definition.setCompositionString("DENOMINATOR-9-7-9-8 AND TB-LAM");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominator_9_13_9_14")
  public CohortDefinition findDenominator_9_13_9_14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominator_9_13_9_14");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "FIRST-CONSULTATION",
        EptsReportUtils.map(this.findPatientsFirstConsultationOnInclusionDate(), mappings));

    definition.addSearch(
        "CD4-RESULT-IN-33DAYS",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionPeriodDenominatorCategory9(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-IN-ART-START",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "BREASTFEEDING-IN-ART-START",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInARTInitiation(), mappings));

    definition.addSearch(
        "PREGNANT-IN-INITIAL-CONSULTATION",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappings));

    definition.addSearch(
        "BREASTFEEDING-IN-INITIAL-CONSULTATION",
        EptsReportUtils.map(this.findPatientsWhoAreBreastfeedingInFirstConsultation(), mappings));

    definition.setCompositionString(
        "(FIRST-CONSULTATION AND CD4-RESULT-IN-33DAYS) NOT (TRANSFERED-IN OR PREGNANT-IN-ART-START OR BREASTFEEDING-IN-ART-START OR PREGNANT-IN-INITIAL-CONSULTATION OR BREASTFEEDING-IN-INITIAL-CONSULTATION)");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_13")
  public CohortDefinition findNumerator_9_13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_13");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13",
        EptsReportUtils.map(this.findDenominator_9_13_9_14(), mappingsForDenominator));

    definition.addSearch(
        "CRAG-RESULT", EptsReportUtils.map(this.findDAHNumeratorSerumCrAgResulCohort(), mappings));

    definition.setCompositionString("DENOMINATOR-13 AND CRAG-RESULT");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_14")
  public CohortDefinition findNumerator_9_14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_14");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13",
        EptsReportUtils.map(this.findDenominator_9_13_9_14(), mappingsForDenominator));

    definition.addSearch("TBLAM-RESULT", EptsReportUtils.map(this.findDahTbLamResult(), mappings));

    definition.setCompositionString("DENOMINATOR-13 AND TBLAM-RESULT");

    return definition;
  }

  @DocumentedDefinition(value = "findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN")
  private CohortDefinition findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MICategory9DAHQueriesInterface.QUERY
                .findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahSerumCrAgResultPregnantWomen")
  private CohortDefinition findDahSerumCrAgResultPregnantWomenCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahSerumCrAgResultPregnantWomen");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query = MICategory9DAHQueriesInterface.QUERY.findDahSerumCrAgResultPregnantWomen;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResultPregnantWomen")
  private CohortDefinition findDahTbLamResultPregnantWomen() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResultPregnantWomen");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query = MICategory9DAHQueriesInterface.QUERY.findDahTbLamResultPregnantWomen;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_19")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_19");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.addSearch(
        "CD4-RESULT-IN-33DAYS",
        EptsReportUtils.map(
            this.findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.setCompositionString(
        "(PREGNANT-INCLUSION-DATE AND CD4-RESULT-IN-33DAYS) NOT TRANSFERED-IN");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_20")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_20() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_20");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappings));

    definition.addSearch(
        "CD4-RESULT-IN-33DAYS",
        EptsReportUtils.map(
            this.findCD4ResultOcurredInClinicalConsultation33DaysAfterTheFirstCPN(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "TB-ACTIVE",
        EptsReportUtils.map(
            mqCategory19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActive(), mappings));

    definition.setCompositionString(
        "(PREGNANT-INCLUSION-DATE AND CD4-RESULT-IN-33DAYS) NOT (TRANSFERED-IN OR TB-ACTIVE)");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_19")
  public CohortDefinition findNumerator_9_19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_19");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-19",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_19(),
            mappingsForDenominator));

    definition.addSearch(
        "CRAG-RESULT",
        EptsReportUtils.map(this.findDahSerumCrAgResultPregnantWomenCohort(), mappings));

    definition.setCompositionString("DENOMINATOR-19 AND CRAG-RESULT");

    return definition;
  }

  @DocumentedDefinition(value = "findNumerator_9_20")
  public CohortDefinition findNumerator_9_20() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumerator_9_20");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-3m+1d},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsForDenominator =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-20",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9_9_20(),
            mappingsForDenominator));

    definition.addSearch(
        "TBLAM-RESULT", EptsReportUtils.map(this.findDahTbLamResultPregnantWomen(), mappings));

    definition.setCompositionString("DENOMINATOR-20 AND TBLAM-RESULT");

    return definition;
  }
}
