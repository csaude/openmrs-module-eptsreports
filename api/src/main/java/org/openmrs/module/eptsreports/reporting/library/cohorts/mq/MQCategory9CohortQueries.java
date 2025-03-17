package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory9QueriesInterface;
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
public class MQCategory9CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(value = "findPatientsFirstConsultationOnInclusionDate")
  public CohortDefinition findPatientsFirstConsultationOnInclusionDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory9QueriesInterface.QUERY.findPatientsFirstConsultationOnInclusionDate;

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
        MQCategory9QueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(
            TypePTV.PREGNANT);

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
        MQCategory9QueriesInterface.QUERY.getPatientsWhoArePregnantOrBreastfeeding(
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
        MQCategory9QueriesInterface.QUERY
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
        MQCategory9QueriesInterface.QUERY
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
        MQCategory9QueriesInterface.QUERY
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
        MQCategory9QueriesInterface.QUERY
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
        MQCategory9QueriesInterface.QUERY
            .findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica")
  public CohortDefinition findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY
            .findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica")
  public CohortDefinition findPatientsWhoReinitiatedTreatmentCat9RF29() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoReinitiatedTreatmentCat9RF29");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory9QueriesInterface.QUERY.findPatientsWhoReinitiatedTreatmentCat9RF29;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReinitiatedTreatmentForMoreThan30DaysBeforeEndRevisionDate")
  public CohortDefinition
      findPatientsWhoReinitiatedTreatmentForMoreThan30DaysBeforeEndRevisionDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoReinitiatedTreatmentForMoreThan30DaysBeforeEndRevisionDate");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY
            .findPatientsWhoReinitiatedTreatmentForMoreThan30DaysBeforeEndRevisionDate;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReceivedCD4ResultBetweenReinitiatedConsultationAndEnRevisionDate")
  public CohortDefinition
      findPatientsWhoReceivedCD4ResultBetweenReinitiatedConsultationAndEnRevisionDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoReceivedCD4ResultBetweenReinitiatedConsultationAndEnRevisionDate");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY
            .findPatientsWhoReceivedCD4ResultBetweenReinitiatedConsultationAndEnRevisionDate;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio")
  public CohortDefinition
      findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY
            .findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio;

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

    String query = MQCategory9QueriesInterface.QUERY.findPatientsWhoArePregnantDuringPreviousPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantDuringPreviousPeriodRF10")
  public CohortDefinition findPatientsWhoArePregnantDuringPreviousPeriodRF10() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringPreviousPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY.findPatientsWhoArePregnantDuringPreviousPeriodRF10;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1");

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
        "FIRST-CONSULTATION",
        EptsReportUtils.map(
            this.findPatientsFirstConsultationOnInclusionDate(), mappingsFirstConsultation));

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
            this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappingsFirstConsultation));

    definition.setCompositionString(
        "FIRST-CONSULTATION NOT(PREGNANT OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1");

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
        "FIRST-CONSULTATION",
        EptsReportUtils.map(
            this.findPatientsFirstConsultationOnInclusionDate(), mappingsFirstConsultation));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoArePregnantInclusionDateRF08(), mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreBreastfeedingInclusionDateRF09(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantDuringInclusionPeriod(), mappingsFirstConsultation));

    definition.addSearch(
        "BREASTFEEDING-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoAreBreastfeedingDuringInclusionPeriod(),
            mappingsFirstConsultation));
    definition.setCompositionString(
        "FIRST-CONSULTATION NOT(PREGNANT OR BREASTFEEDING OR TRANSFERED-IN OR PREGNANT-INCLUSION-DATE OR BREASTFEEDING-INCLUSION-DATE)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1Childrens")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1Childrens() {

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
            this
                .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens(),
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
      value =
          "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1ChildrenAdult")
  public CohortDefinition
      findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1ChildrenAdult() {

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
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
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
      value = "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1")
  public CohortDefinition
      findPragnantWomanWhoARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1() {

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

    final String mappingsNumerator =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate-9m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(), mappings));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappingsNumerator));

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
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
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
      value = "findPatientsInARTWhoHaveAreFirstConsultationNumeratorAdultCategory9Section9_1")
  public CohortDefinition
      findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9Section9_2() {

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
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
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
          "findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9SectionChildrens")
  public CohortDefinition
      findPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9SectionChildrens() {

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
            this
                .findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1Childrens(),
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
          "findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9Section9_2")
  public CohortDefinition
      findPregnantWomanPatientsWhoHaveAreFirstConsultationAndHaveNumeratorAdultCategory9Section9_2() {

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
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(),
            mappingsFirstConsultation));

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
      value = "findPragnantWomanWhoHaveAreFirstConsultationDenominatorPregnantCategory9")
  public CohortDefinition findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPatientsWhoHaveAreFirstConsultationDenominatorPregnantCategory9");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate-9m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(this.findPatientsWhoArePregnantDuringPreviousPeriodRF10(), mappings));

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
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(),
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
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(),
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

  @DocumentedDefinition(
      value =
          "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3")
  public CohortDefinition
      findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "REINICIO",
        EptsReportUtils.map(this.findPatientsWhoReinitiatedTreatmentCat9RF29(), mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.setCompositionString("(REINICIO NOT TRANSFERED-IN)");

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

    definition.addSearch(
        "DENOMINATOR-9-3",
        EptsReportUtils.map(
            this
                .findAdultPatientsWithRequestCD4InTheSameClinicalConsultationMarkedAsReinicioDenominator9_3(),
            mappings));

    definition.addSearch(
        "REINICIO-PEDIDO-CD4",
        EptsReportUtils.map(
            this.findPatientsWithReinicioAndPedidoDeCD4InTheSameFichaClinica(), mappings));

    definition.setCompositionString("(DENOMINATOR-9-3 AND REINICIO-PEDIDO-CD4)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4")
  public CohortDefinition
      findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "REINICIO",
        EptsReportUtils.map(
            this.findPatientsWhoReinitiatedTreatmentForMoreThan30DaysBeforeEndRevisionDate(),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappings));

    definition.setCompositionString("(REINICIO NOT TRANSFERED-IN)");

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

    definition.addSearch(
        "DENOMINATOR-9-4",
        EptsReportUtils.map(
            this
                .findAdultPatientsWhoReceivedCd4Result33daysAfterReinitiatedTreatmentDenominator9_4(),
            mappings));

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(
            this
                .findPatientsWhoReceivedCD4ResultIn33DaysAfterTheClinicalConsultationMarkedAsReinicio(),
            mappings));

    definition.setCompositionString("DENOMINATOR-9-4 AND CD4-RESULT");

    return definition;
  }
}
