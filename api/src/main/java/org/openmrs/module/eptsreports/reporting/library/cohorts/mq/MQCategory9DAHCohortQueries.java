package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory9DAHQueriesInterface;
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
public class MQCategory9DAHCohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;
  @Autowired private MQCategory9CohortQueries category9CohortQueries;

  // RF6 e RF7
  @DocumentedDefinition(value = "findPregnantOrBreastfeedingWomanAtARTInitiation")
  public CohortDefinition findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV typePTV) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPregnantOrBreastfeedingWomanAtARTInitiation");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        MQCategory9DAHQueriesInterface.QUERY.findPregnantOrBreastfeedingWomanAtARTInitiation;

    switch (typePTV) {
      case PREGNANT:
        query = query + " where decisao = 1 ";
        break;

      case BREASTFEEDING:
        query = query + " where decisao = 2 ";
        break;
    }

    definition.setQuery(query);

    return definition;
  }

  // -- RF8 e RF9
  @DocumentedDefinition(value = "findPregnantOrBreastfeedingWomanInitialClinicalConsultation")
  public CohortDefinition findPregnantOrBreastfeedingWomanInitialClinicalConsultation(
      TypePTV typePTV) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPregnantOrBreastfeedingWomanInitialClinicalConsultation");
    definition.addParameter(new Parameter("startReferencia", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endReferencia", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory9DAHQueriesInterface.QUERY
            .findPregnantOrBreastfeedingWomanInitialClinicalConsultation;

    switch (typePTV) {
      case PREGNANT:
        query = query + " where decisao = 1 ";
        break;

      case BREASTFEEDING:
        query = query + " where decisao = 2 ";
        break;
    }

    definition.setQuery(query);

    return definition;
  }

  // -- RF11: Idade na primeira consulta clinica, Crag, TBLam: Adultos (>=15 anos)
  @DocumentedDefinition(value = "findAgeAtInitialClinicalConsultationCrAgTbLamAdults")
  public CohortDefinition findAgeAtInitialClinicalConsultationCrAgTbLamAdults() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAgeAtInitialClinicalConsultationCrAgTbLamAdults");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory9DAHQueriesInterface.QUERY.findAgeAtInitialClinicalConsultation
            + "Where timestampdiff(YEAR,birthdate,data_consulta_inicial)>=15 )f ";

    definition.setQuery(query);

    return definition;
  }

  // -- RF11: Idade na primeira consulta clinica, Crag: Criança (10-14 anos)
  @DocumentedDefinition(value = "findAgeAtInitialClinicalConsultationCrAgChildren")
  public CohortDefinition findAgeAtInitialClinicalConsultationCrAgChildren() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAgeAtInitialClinicalConsultationCrAgChildren");
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9DAHQueriesInterface.QUERY.findAgeAtInitialClinicalConsultation
            + "Where timestampdiff(YEAR,per.birthdate,data_consulta_inicial) between 10 and 14 ) f";

    definition.setQuery(query);

    return definition;
  }

  // -- RF11: Idade na primeira consulta clinica, TBLam: Criança (5-14 anos)
  @DocumentedDefinition(value = "findAgeAtInitialClinicalConsultationPediatric")
  public CohortDefinition findAgeAtInitialClinicalConsultationPediatric() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findAgeAtInitialClinicalConsultationPediatric");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory9DAHQueriesInterface.QUERY.findAgeAtInitialClinicalConsultation
            + " Where timestampdiff(YEAR,per.birthdate,data_consulta_inicial) between 5 and 14 )f ";

    definition.setQuery(query);

    return definition;
  }

  // -- RF11.1(RF46), RF39: Idade do Utente na Data Reinício TARV: TBLAM, CRAG:
  // Adulto
  @DocumentedDefinition(value = "findPatientAgeAtArtRestartDateTbLamCrAgAdult")
  public CohortDefinition findPatientAgeAtArtRestartDateTbLamCrAgAdult() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientAgeAtArtRestartDateTbLamCrAgAdult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory9DAHQueriesInterface.QUERY.findPatientAgeAtArtRestartDate
            + " having timestampdiff(YEAR,max(per.birthdate),min(e.encounter_datetime))>=15 )f  ";

    definition.setQuery(query);

    return definition;
  }

  // -- RF11.1(RF46), RF42: Idade do Utente na Data Reinício TARV: CRAG, Criança
  @DocumentedDefinition(value = "findPatientAgeAtArtRestartDateCrAgChild")
  public CohortDefinition findPatientAgeAtArtRestartDateCrAgChild() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientAgeAtArtRestartDateCrAgChild");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9DAHQueriesInterface.QUERY.findPatientAgeAtArtRestartDate
            + "having timestampdiff(YEAR,max(per.birthdate),min(e.encounter_datetime)) between 10 and 14 )f";

    definition.setQuery(query);

    return definition;
  }

  // -- RF11.1(RF46), RF44: Idade do Utente na Data Reinício TARV: TBLAM, Criança
  @DocumentedDefinition(value = "findPatientAgeAtArtRestartDateTbLamChild")
  public CohortDefinition findPatientAgeAtArtRestartDateTbLamChild() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientAgeAtArtRestartDateTbLamChild");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory9DAHQueriesInterface.QUERY.findPatientAgeAtArtRestartDate
            + "having timestampdiff(YEAR,max(per.birthdate),min(e.encounter_datetime)) between 5 and 14 )f ";

    definition.setQuery(query);

    return definition;
  }

  // FR29 - DAH: Resultado de CrAG sérico e TB_LAM - Adulto
  @DocumentedDefinition(value = "findDAHDenominatorhSerumCrAgAndTbLamResultAdult")
  public CohortDefinition findDenominator_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DAHDenominatorhSerumCrAgAndTbLamResultAdult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    final String mappiReferenceDate =
        "startReferencia=${endRevisionDate-12m+1d},endReferencia=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDAHhSerumCrAgAndTbLamResultCohort(), mappingsRevisionDate));

    definition.addSearch(
        "FR11",
        EptsReportUtils.map(
            this.findAgeAtInitialClinicalConsultationCrAgTbLamAdults(), mappingsRevisionDate));

    definition.addSearch(
        "FR7",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.BREASTFEEDING),
            mappingsRevisionDate));

    definition.addSearch(
        "FR9",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.BREASTFEEDING),
            mappiReferenceDate));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR6",
        EptsReportUtils.map(
            findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.PREGNANT),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR8",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.PREGNANT),
            mappiReferenceDate));

    definition.setCompositionString(
        "(DENOMINATOR AND (FR11 OR FR7 OR  FR9)) NOT (TRANSFERED-IN OR PREGNANT-FR6 OR PREGNANT-FR8 )");

    return definition;
  }

  // FR30 - DAH: Categoria 9: Numerador – DAH: Resultado de CrAG sérico - adulto
  @DocumentedDefinition(value = "findDAHNumeratorSerumCrAgResultAdult")
  public CohortDefinition findNumerator_9_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DAHNumeratorSerumCrAgResultAdult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.findDenominator_9_5(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDAHNumeratorSerumCrAgResulCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR31 - DAH: Resultado de TB_LAM - Adulto
  @DocumentedDefinition(value = "findDAHNumeratorTBLAMResultAdult")
  public CohortDefinition findNumerator_9_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DAHNumeratorSerumCrAgResultAdult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.findDenominator_9_5(), mappingsAll));

    definition.addSearch(
        "NUMERATOR", EptsReportUtils.map(this.findDahTbLamResult(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR32 - Denominador DAH- Resultado de CrAG Serico - Children
  @DocumentedDefinition(value = "findDAHDenominatorhSerumCrAgResultChildren")
  public CohortDefinition findDAHDenominatorCraigTblamChildren_9_13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDAHDenominatorhSerumCrAgResultChildren");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    final String mappiReferenceDate =
        "startReferencia=${endRevisionDate-12m+1d},endReferencia=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDAHhSerumCrAgAndTbLamResultCohort(), mappingsRevisionDate));

    definition.addSearch(
        "FR11",
        EptsReportUtils.map(
            this.findAgeAtInitialClinicalConsultationCrAgChildren(), mappingsRevisionDate));

    definition.addSearch(
        "FR7",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.BREASTFEEDING),
            mappingsRevisionDate));

    definition.addSearch(
        "FR9",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.BREASTFEEDING),
            mappiReferenceDate));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR6",
        EptsReportUtils.map(
            findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.PREGNANT),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR8",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.PREGNANT),
            mappiReferenceDate));

    definition.setCompositionString(
        "(DENOMINATOR AND FR11 ) NOT (TRANSFERED-IN OR PREGNANT-FR6 OR FR7 OR PREGNANT-FR8 OR  FR9 )");

    return definition;
  }

  // FR33 - DAH: Resultado de Craig Serico - Children
  @DocumentedDefinition(value = "findDAHNumeratorChildrenResult")
  public CohortDefinition findNumeratorCraigResultChildren_9_13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDAHNumeratorChildrenResult");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDAHDenominatorCraigTblamChildren_9_13(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDAHNumeratorSerumCrAgResulCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR34 - Denominador DAH- Resultado de TBLAM -Pediatrico
  @DocumentedDefinition(value = "findDAHDenominatorTBLAMResultPediatric")
  public CohortDefinition findDAHDenominatorDAHTBLAMResultPedriatic_9_14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDAHDenominatorTBLAMResultPediatric");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    final String mappiReferenceDate =
        "startReferencia=${endRevisionDate-12m+1d},endReferencia=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDAHhSerumCrAgAndTbLamResultCohort(), mappingsRevisionDate));

    definition.addSearch(
        "FR11",
        EptsReportUtils.map(
            this.findAgeAtInitialClinicalConsultationPediatric(), mappingsRevisionDate));

    definition.addSearch(
        "FR7",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.BREASTFEEDING),
            mappingsRevisionDate));

    definition.addSearch(
        "FR9",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.BREASTFEEDING),
            mappiReferenceDate));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR6",
        EptsReportUtils.map(
            findPregnantOrBreastfeedingWomanAtARTInitiation(TypePTV.PREGNANT),
            mappingsRevisionDate));

    definition.addSearch(
        "PREGNANT-FR8",
        EptsReportUtils.map(
            this.findPregnantOrBreastfeedingWomanInitialClinicalConsultation(TypePTV.PREGNANT),
            mappiReferenceDate));

    definition.setCompositionString(
        "(DENOMINATOR AND FR11 ) NOT (TRANSFERED-IN OR PREGNANT-FR6 OR FR7 OR PREGNANT-FR8 OR  FR9 )");

    return definition;
  }

  // FR35 - DAH: Numerador Resultado de TB_LAM - Pediatric
  @DocumentedDefinition(value = "findDAHNumeratorTBLAMResultPediatric")
  public CohortDefinition findNumeratorTbLamPediatric_9_14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDAHNumeratorTBLAMResultPediatric");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDAHDenominatorDAHTBLAMResultPedriatic_9_14(), mappingsAll));

    definition.addSearch(
        "NUMERATOR", EptsReportUtils.map(this.findDahTbLamResult(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR36 - DAH: Denominador Resultado de Craig Serico e TB LAM- Mulheres Gravidas
  @DocumentedDefinition(value = "findDenominatorDAHCraigSericAndTblamPregnants_9_19")
  public CohortDefinition findDenominatorDAHCraigSericAndTblamPregnants_9_19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorDAHCraigSericAndTblamPregnants_9_19");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDahSerumCrAgAndTbLamResultsPregnantWomenCohort(), mappingsAll));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnantDuringPreviousPeriodRF10DuringRevisioPeriod(),
            mappingsAll));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.setCompositionString("(DENOMINATOR AND PREGNANT) NOT TRANSFERED-IN ");

    return definition;
  }

  // FR37 - DAH: Numerator Resultado de CrAG sérico - MG
  @DocumentedDefinition(value = "findNumeratorDahSerumCrAgResultPregnantWomenCohort")
  public CohortDefinition findNumeratorDahSerumCrAgResultPregnantWomenCohort_9_19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorDahSerumCrAgResultPregnantWomenCohort");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    // final String mappingsRevisionDate =
    // "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDenominatorDAHCraigSericAndTblamPregnants_9_19(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahSerumCrAgResultPregnantWomenCohort(), mappingsAll));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR38 - DAH: Numerator Resultado de TBLAM - MG
  @DocumentedDefinition(value = "findNumeratorDahTbLamResultPregnantWomenCohort")
  public CohortDefinition findNumeratorDahTbLamResultPregnantWomenCohort_9_20() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorDahTbLamResultPregnantWomenCohort");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    // final String mappingsRevisionDate =
    // "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDenominatorDAHCraigSericAndTblamPregnants_9_19(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahTbLamResultPregnantWomenCohort(), mappingsAll));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR39 - DAH: Denominador - Resultado de Crag Serico e TBLAM nos reinicios -
  // Aldultos
  @DocumentedDefinition(value = "findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8")
  public CohortDefinition findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDahSerumCrAgAndTbLamResultAtRestartCohort(), mappingsRevisionDate));

    definition.addSearch(
        "ADULTS",
        EptsReportUtils.map(this.findPatientAgeAtArtRestartDateTbLamCrAgAdult(), mappingsAll));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.setCompositionString("(DENOMINATOR AND ADULTS) NOT TRANSFERED-IN");

    return definition;
  }

  // FR40 - DAH: Numerator Resultado de CrAG sérico nos reinícios - Adulto
  @DocumentedDefinition(value = "findNumeratorDahSerumCrAgResultAtRestartAdultCohort_9_7")
  public CohortDefinition findNumeratorDahSerumCrAgResultAtRestartAdultCohort_9_7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorDahSerumCrAgResultAtRestartAdultCohort_9_7");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahSerumCrAgResultAtRestartCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR41 - DAH: Numerator Resultado TBLAM nos reinícios - Adulto
  @DocumentedDefinition(value = "findNumeratorTBLAMResultAtRestartAdultCohort_9_8")
  public CohortDefinition findNumeratorTBLAMResultAtRestartAdultCohort_9_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorTBLAMResultAtRestartAdultCohort_9_8");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDenominatorDahSerumCrAgAndTbLamResultAtRestartAdult_9_7_9_8(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahTbLamResultAtRestartCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR42 - DAH: Denominador - Resultado de Crag Serico nos reinicios -
  // children
  @DocumentedDefinition(value = "findDenominatorSerumCraigResultAtRestartChildren_9_15")
  public CohortDefinition findDenominatorSerumCraigResultAtRestartChildren_9_15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorSerumCraigResultAtRestartChildren_9_15");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDahSerumCrAgAndTbLamResultAtRestartCohort(), mappingsRevisionDate));

    definition.addSearch(
        "CHILDREN",
        EptsReportUtils.map(this.findPatientAgeAtArtRestartDateCrAgChild(), mappingsAll));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.setCompositionString("(DENOMINATOR AND CHILDREN) NOT TRANSFERED-IN");

    return definition;
  }

  // FR43 - DAH: Numerator Resultado de CrAG sérico nos reinícios - childre
  @DocumentedDefinition(value = "findNumeratorDahSerumCrAgResultAtRestartChildrenCohort_9_15")
  public CohortDefinition findNumeratorDahSerumCrAgResultAtRestartChildrenCohort_9_15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorDahSerumCrAgResultAtRestartChildrenCohort_9_15");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDenominatorSerumCraigResultAtRestartChildren_9_15(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahSerumCrAgResultAtRestartCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // FR44 - DAH: Denominador - Resultado de TBLAM nos reinicios -
  // children
  @DocumentedDefinition(value = "findDenominatorTBLAMResultAtRestartChildren_9_16")
  public CohortDefinition findDenominatorTBLAMResultAtRestartChildren_9_16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorTBLAMResultAtRestartChildren_9_16");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findDahSerumCrAgAndTbLamResultAtRestartCohort(), mappingsRevisionDate));

    definition.addSearch(
        "CHILDREN",
        EptsReportUtils.map(this.findPatientAgeAtArtRestartDateTbLamChild(), mappingsAll));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoWhereMarkedAsTransferedInOnMasterCardRF5Category9(),
            mappingsRevisionDate));

    definition.setCompositionString("(DENOMINATOR AND CHILDREN) NOT TRANSFERED-IN");

    return definition;
  }

  // FR45 - DAH: Numerator Resultado de TBLAM nos reinícios - children
  @DocumentedDefinition(value = "findNumeratorTBLAMResultAtRestartChildrenCohort_9_16")
  public CohortDefinition findNumeratorTBLAMResultAtRestartChildrenCohort_9_16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorTBLAMResultAtRestartChildrenCohort_9_16");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappingsAll =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.findDenominatorTBLAMResultAtRestartChildren_9_16(), mappingsAll));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(this.findDahTbLamResultAtRestartCohort(), mappingsRevisionDate));

    definition.setCompositionString("DENOMINATOR AND NUMERATOR");

    return definition;
  }

  // Metodos privados

  @DocumentedDefinition(value = "findDAHhSerumCrAgAndTbLamResult")
  private CohortDefinition findDAHhSerumCrAgAndTbLamResultCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDAHhSerumCrAgAndTbLamResult");
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        "select patient_id from ("
            + MQCategory9DAHQueriesInterface.QUERY.findDAHDenominatorhSerumCrAgAndTbLamResult
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
    String query = MQCategory9DAHQueriesInterface.QUERY.findDAHNumeratorSerumCrAgResult;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResult")
  private CohortDefinition findDahTbLamResult() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResult");
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query = MQCategory9DAHQueriesInterface.QUERY.findDahTbLamResult;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahSerumCrAgAndTbLamResultsPregnantWomen")
  private CohortDefinition findDahSerumCrAgAndTbLamResultsPregnantWomenCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahSerumCrAgAndTbLamResultsPregnantWomen");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MQCategory9DAHQueriesInterface.QUERY.findDahSerumCrAgAndTbLamResultsPregnantWomen
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

    String query = MQCategory9DAHQueriesInterface.QUERY.findDahSerumCrAgResultPregnantWomen;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResultPregnantWomen")
  private CohortDefinition findDahTbLamResultPregnantWomenCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResultPregnantWomen");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query = MQCategory9DAHQueriesInterface.QUERY.findDahTbLamResultPregnantWomen;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahSerumCrAgAndTbLamResultAtRestartCohort")
  private CohortDefinition findDahSerumCrAgAndTbLamResultAtRestartCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahSerumCrAgAndTbLamResultAtRestartCohort");
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MQCategory9DAHQueriesInterface.QUERY.findDahSerumCrAgAndTbLamResultAtRestart
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahSerumCrAgResultAtRestartCohort")
  private CohortDefinition findDahSerumCrAgResultAtRestartCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahSerumCrAgResultAtRestartCohort");
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MQCategory9DAHQueriesInterface.QUERY.findDahSerumCrAgResultAtRestart
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDahTbLamResultAtRestartCohort")
  private CohortDefinition findDahTbLamResultAtRestartCohort() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDahTbLamResultAtRestartCohort");
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    String query =
        "select patient_id from ("
            + MQCategory9DAHQueriesInterface.QUERY.findDahTbLamResultAtRestart
            + ") t";

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsFirstConsultationOnInclusionDate")
  private CohortDefinition findPatientsFirstConsultationOnInclusionDate() {

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
  private CohortDefinition findPatientsWhoArePregnantDuringInclusionPeriod() {

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
  private CohortDefinition findPatientsWhoAreBreastfeedingDuringInclusionPeriod() {

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
  private CohortDefinition
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
      value = "findPatientsWhoArePregnantDuringPreviousPeriodRF10DuringRevisioPeriod")
  public CohortDefinition findPatientsWhoArePregnantDuringPreviousPeriodRF10DuringRevisioPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringPreviousPeriodRF10DuringRevisioPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9DAHQueriesInterface.QUERY
            .findPatientsWhoArePregnantDuringPreviousPeriodRF10DuringRevisionPeriod;

    definition.setQuery(query);

    return definition;
  }
}
