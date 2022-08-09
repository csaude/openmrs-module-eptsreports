package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mi.MICategory15QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class MICategory15CohortQueries {

  @DocumentedDefinition(value = "findPatientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition findPatientsWhoAreActiveOnArtAndInAtleastOneDSD() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients who are active on ART and in at least one DSD");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithClinicalConsultationDuringRevisionPeriod")
  public CohortDefinition findPatientsWithClinicalConsultationDuringRevisionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with clinical consultation during revision period");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWithClinicalConsultationDuringRevisionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS")
  public CohortDefinition findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get patients registered as iniciar in at least one MDS");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS")
  public CohortDefinition findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get patients registered as fim in at least one MDS");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantSpecificForCategory15MI")
  public CohortDefinition findPatientsWhoArePregnantSpecificForCategory15MI() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get pregnat patients");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWhoArePregnantSpecificForCategory15MI;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingSpecificForCategory15MI")
  public CohortDefinition findPatientsWhoAreBreastfeedingSpecificForCategory15MI() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get Breastfeeding Patients");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY.findPatientsWhoAreBreastfeedingSpecificForCategory15MI;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientOnARTMarkedPregnantOnTheLastNineMonthsRF8")
  public CohortDefinition findPatientOnARTMarkedPregnantOnTheLastNineMonthsRF8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MI Category 15 - Get Patients who was Pregnant in The last 9 months");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(this.findPatientsWhoArePregnantSpecificForCategory15MI(), mappings));

    definition.setCompositionString("PREGNANT");
    return definition;
  }

  @DocumentedDefinition(value = "findPatientOnARTMarkedBreastfeedingOnTheLastEighTeenMonthsRF9")
  public CohortDefinition findPatientOnARTMarkedBreastfeedingOnTheLastEighTeenMonthsRF9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get Patients registered as breastfeeding in the last eighteen months");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.findPatientsWhoAreBreastfeedingSpecificForCategory15MI(), mappings));

    definition.setCompositionString("BREASTFEEDING");
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithClinicalConsultationAndARTStartDateBiggerThanThreeMonths")
  public CohortDefinition
      findPatientsWithClinicalConsultationDuringRevisionPeriodAndARTStartDateBiggerThanThreeMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with clinical consultation and ART start date bigger than 3 months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanThreeMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithRegularClinicalConsultationOrRegularArtPickUpInTheLastThreeMonths")
  public CohortDefinition
      findPatientsWithRegularClinicalConsultationOrRegularArtPickUpInTheLastThreeMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with clinical consultation or ART pickup in the last 3 months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationOrARTPickUpInTheLastThreeMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation")
  public CohortDefinition
      findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with the last CV>=1000 in the last clinical consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithTheLastCargaViralGreaterOrEqualThan1000InClinicalConsultation")
  public CohortDefinition
      findPatientsWithTheLastCargaViralGreaterOrEqualThan1000InClinicalConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with the last CV>1000 in clinical consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000InClinicalConsultation;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithTheLastCD4LessThan200OrEqualInClinicalConsultation")
  public CohortDefinition findPatientsWithTheLastCD4LessThan20O() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with the last CD4<=200 in clinical consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MICategory15QueriesInterface.QUERY.findPatientsWithTheLastCD4LessThan20O;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithTheLastCVBiggerThan1000")
  public CohortDefinition findPatientsWithTheLastCVBiggerThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with the last CD4<=200 in clinical consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MICategory15QueriesInterface.QUERY.findPatientsWithTheLastCVBiggerThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_1")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_1() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients elegible for MDS stable patients in cinical consultation in the revision period");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate-1m},location=${location}";

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsPregnant =
        "startInclusionDate=${endRevisionDate-20m},endInclusionDate=${endRevisionDate-2m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsCd4 = "endRevisionDate=${endRevisionDate-1m},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            this.findPatientsWithClinicalConsultationDuringRevisionPeriod(), mappingsMI));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this
                .findPatientsWithClinicalConsultationDuringRevisionPeriodAndARTStartDateBiggerThanThreeMonths(),
            mappingsMI));

    definition.addSearch(
        "E",
        EptsReportUtils.map(
            this
                .findPatientsWithRegularClinicalConsultationOrRegularArtPickUpInTheLastThreeMonths(),
            mappingsMI));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            this.findPatientOnARTMarkedPregnantOnTheLastNineMonthsRF8(), mappingsPregnant));

    definition.addSearch(
        "D",
        EptsReportUtils.map(
            this.findPatientOnARTMarkedBreastfeedingOnTheLastEighTeenMonthsRF9(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(this.findPatientsWithTheLastCD4LessThan20O(), mappingsCd4));

    definition.addSearch(
        "G", EptsReportUtils.map(this.findPatientsWithTheLastCVBiggerThan1000(), mappingsMI));

    definition.addSearch(
        "J",
        EptsReportUtils.map(this.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappingsMI));

    definition.setCompositionString("(A AND B1 AND E) NOT (C OR D OR F OR G OR J)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_1")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_1() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients elegible for MDS stable patients in clinical consultation in the revision period and registered at least one MDS");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappings =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-1",
        EptsReportUtils.map(
            this
                .findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_1(),
            mappings));

    definition.addSearch(
        "K",
        EptsReportUtils.map(
            this.findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS(), mappingsMI));

    definition.setCompositionString("(DENOMINATOR-15-1 AND K)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWithClinicalConsultationDuringRevisionPeriodAndAgeGreaterOrEqualTwoYears")
  public CohortDefinition
      findPatientsWithClinicalConsultationDuringRevisionPeriodAndAgeGreaterOrEqualTwoYears() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients with clinica consultation during revision period and age>= 2years");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationDuringRevisionPeriodAndAgeGreaterOrEqualTwoYears;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_2")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in MDS for stable patients with clinical consultation in the revision period who received CV>1000");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            this
                .findPatientsWithClinicalConsultationDuringRevisionPeriodAndAgeGreaterOrEqualTwoYears(),
            mappings));

    definition.addSearch(
        "J", EptsReportUtils.map(this.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            this
                .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation(),
            mappings));

    definition.setCompositionString("(A AND J AND H)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_2")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in MDS for stable patients with clinical consultaion in the revision period who received CV>1000 and suspended in the MDS");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappings =
        "startInclusionDate=${endRevisionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-2",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_2(),
            mappings));

    definition.addSearch(
        "MDC",
        EptsReportUtils.map(
            this
                .findPatientsWhoHasRegisteredAsFimInAtLeastOneMDCOnTheLastClinicalConsultationInInclusionPeriod(),
            mappingsMI));

    definition.addSearch(
        "FILA",
        EptsReportUtils.map(
            this.findPatientsWhoHasCVBiggerThan1000InLastClinicalConsultationAndHaveARTPickUp(),
            mappingsMI));

    definition.setCompositionString("(DENOMINATOR-15-2 AND MDC) OR (DENOMINATOR-15-2 AND FILA)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyOneMonths")
  public CohortDefinition
      findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyOneMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 -Get patients with clinical consultaion and ART start date> 21 Months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyOneMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyFourMonths")
  public CohortDefinition
      findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyFourMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Catefory 15 - Get patients with clinical consultation and ART start date> 21 months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyFourMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLast12Months")
  public CohortDefinition
      findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLast12Months() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get all patients who have laboratory investigations request and viral charge in the last consultation during the last 3 months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLast12Months;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_3")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_3() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in MDS for stable patients with clinical consultation in the revision period and in ART for more than 21 months - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            this
                .findPatientsWithClinicalConsultationDuringRevisionPeriodAndAgeGreaterOrEqualTwoYears(),
            mappings));

    definition.addSearch(
        "J", EptsReportUtils.map(this.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyOneMonths(),
            mappings));

    definition.addSearch(
        "P",
        EptsReportUtils.map(
            this
                .findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLast12Months(),
            mappings));

    definition.setCompositionString("(A AND J AND B2) NOT P");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBeforeTwentyMonthsOfLastClinicalConsultationThan1000")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBeforeTwentyMonthsOfLastClinicalConsultationThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in one MDS for stable patients and had CV before 20 Months of last clinical consultation after CV<1000");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBeforeTwentyMonthsOfLastClinicalConsultationThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in one MDS stable patients and had CV between 11 and 24 after CV<1000");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_3")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_3() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in one MDS for stable patients and had CV between 11 and 18 months after CV<1000 - Numerator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-3",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_3(),
            mappings));

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBeforeTwentyMonthsOfLastClinicalConsultationThan1000(),
            mappingsMI));

    definition.setCompositionString("(DENOMINATOR-15-3 AND I)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoHasRegisteredAsFimInAtLeastOneMDCOnTheLastClinicalConsultationInInclusionPeriod")
  public CohortDefinition
      findPatientsWhoHasRegisteredAsFimInAtLeastOneMDCOnTheLastClinicalConsultationInInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get patients registered as fim in at least one MDS");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWhoHasRegisteredAsFimInAtLeastOneMDCOnTheLastClinicalConsultationInInclusionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHasCVBiggerThan1000InLastClinicalConsultationAndHaveARTPickUp")
  public CohortDefinition
      findPatientsWhoHasCVBiggerThan1000InLastClinicalConsultationAndHaveARTPickUp() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MI Category 15 - Get patients registered as fim in at least one MDS");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MICategory15QueriesInterface.QUERY
            .findPatientsWhoHasCVBiggerThan1000InLastClinicalConsultationAndHaveARTPickUp;

    definition.setQuery(query);

    return definition;
  }
}
