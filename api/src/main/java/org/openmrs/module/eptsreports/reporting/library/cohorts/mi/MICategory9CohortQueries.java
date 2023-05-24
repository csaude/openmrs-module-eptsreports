package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
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
public class MICategory9CohortQueries {

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
        "startInclusionDate=${ndRevisionDate-2m+1d},endInclusionDate=${ndRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate-2m+1d},location=${location}";

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
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
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

    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisindRevisionDate-2m+1d},location=${location}";

    final String mappingsFirstConsultationD =
        "endRevisionDate=${endRevisindRevisionDate-2m+1d},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsInARTWhoHaveAreFirstConsultationDenominatorAdultCategory9Section9_1(),
            mappingsFirstConsultationD));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappingsFirstConsultation));

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
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate-2m+1d},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(), mappings));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappingsFirstConsultation));

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

    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsFirstConsultationN =
        "endRevisionDate=${endRevisionDate-2m+1d},location=${location}";

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
            mappingsFirstConsultationN));

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
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate-2m+1d},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPragnantWomanWhoHaveAreFirstConsultationDenominatorCategory9(), mappings));

    definition.addSearch(
        "CD4-33-DAYS",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9(),
            mappingsFirstConsultation));

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
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsFirstConsultation =
        "endRevisionDate=${endRevisionDate-2m+1d},location=${location}";

    definition.addSearch(
        "PREGNANT-INCLUSION-DATE",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantDuringPreviousPeriod(), mappingsFirstConsultation));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
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
}
