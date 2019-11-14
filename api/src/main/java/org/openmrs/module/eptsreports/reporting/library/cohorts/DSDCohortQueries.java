package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.BreastfeedingQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.DsdQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.PregnantQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TxCurrQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DSDCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  @DocumentedDefinition(value = "patientsActiveOnArtExcludingPregnantBreastfeedingAndTb")
  public CohortDefinition getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb(
      final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "IN-ART", TxCurrQueries.QUERY.findPatientsWhoAreCurrentlyEnrolledOnART),
            mappings));

    dsd.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoArePregnantInAPeriod",
                PregnantQueries.findPatientsWhoArePregnantInAPeriod()),
            "startDate=${endDate-9m},endDate=${endDate},location=${location}"));
    dsd.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreBreastfeeding",
                BreastfeedingQueries.findPatientsWhoAreBreastfeeding()),
            "startDate=${endDate-18m},endDate=${endDate},location=${location}"));

    dsd.addSearch(
        "TB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "TB", DsdQueriesInterface.QUERY.findPatientsBeingOnTuberculosisTreatmentEndPeriod),
            mappings));

    dsd.setCompositionString("IN-ART NOT (PREGNANT OR BREASTFEEDING OR TB)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveInArtEligibleForDsd")
  public CohortDefinition getPatientsActiveOnArtEligibleForDsd(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "STABLE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsInArtWhoAreStable",
                DsdQueriesInterface.QUERY.findPatientsInArtWhoAreStable),
            mappings));

    dsd.addSearch(
        "SARCOMA-KAPOSI",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SARCOMA-KAPOSI",
                DsdQueriesInterface.QUERY.findPatientsWhoHaveBeenNotifiedOfKaposiSarcoma),
            mappings));

    dsd.addSearch(
        "ADVERSASE-REACTIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADVERSASE-REACTIONS",
                DsdQueriesInterface.QUERY
                    .findPatientsWithAdverseDrugReactionsRequiringRegularMonitoringNotifiedInLast6Months),
            mappings));

    dsd.setCompositionString("(IN-ART AND STABLE) NOT(ADVERSASE-REACTIONS OR SARCOMA-KAPOSI)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsWhoNotElegibleDSD")
  public CohortDefinition getPatientsActiveOnArtNotEligibleForDsd(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "STABLE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsInArtWhoAreStable",
                DsdQueriesInterface.QUERY.findPatientsInArtWhoAreStable),
            mappings));

    dsd.addSearch(
        "SARCOMA-KAPOSI",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SARCOMA-KAPOSI",
                DsdQueriesInterface.QUERY.findPatientsWhoHaveBeenNotifiedOfKaposiSarcoma),
            mappings));

    dsd.addSearch(
        "ADVERSASE-REACTIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADVERSASE-REACTIONS",
                DsdQueriesInterface.QUERY
                    .findPatientsWithAdverseDrugReactionsRequiringRegularMonitoringNotifiedInLast6Months),
            mappings));

    dsd.setCompositionString(
        "IN-ART NOT((STABLE AND IN-ART) NOT(ADVERSASE-REACTIONS OR SARCOMA-KAPOSI))");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInDt")
  public CohortDefinition getPatientsActiveOnArtWhoInDt(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "QUARTERLY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "QUARTERLY-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcQuarterlyDispensation),
            mappings));

    dsd.setCompositionString("(IN-ART AND QUARTERLY-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInDt")
  public CohortDefinition getPatientsActiveOnArtElegibleDsdWhoInDt(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE"), mappings));

    dsd.addSearch(
        "QUARTERLY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "QUARTERLY-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcQuarterlyDispensation),
            mappings));

    dsd.setCompositionString("(ELEGIBLE AND QUARTERLY-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleDsdWhoInDt")
  public CohortDefinition getPatientsActiveOnArtNotElegibleDsdWhoInDt(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("ELEGIBLE"), mappings));

    dsd.addSearch(
        "QUARTERLY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "QUARTERLY-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcQuarterlyDispensation),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE AND QUARTERLY-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInDt")
  public CohortDefinition getPatientsActiveOnArtWhoInFastFlow(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "FAST-FLOW",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAST-FLOW", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFastFlow),
            mappings));

    dsd.setCompositionString("(IN-ART AND FAST-FLOW)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInFastFlow")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInFastFlow(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("IN-ART"), mappings));

    dsd.addSearch(
        "FAST-FLOW",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAST-FLOW", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFastFlow),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND FAST-FLOW)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInFastFlow")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInFastFlow(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "FAST-FLOW",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAST-FLOW", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFastFlow),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND FAST-FLOW)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInGaac")
  public CohortDefinition getPatientsActiveOnArtWhoInGaac(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("GAAC", DsdQueriesInterface.QUERY.findPatientsWhoInGaac),
            mappings));

    dsd.setCompositionString("(IN-ART AND GAAC)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInGaac")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInGaac(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("GAAC", DsdQueriesInterface.QUERY.findPatientsWhoInGaac),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND GAAC)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInGacc")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInGacc(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("GAAC", DsdQueriesInterface.QUERY.findPatientsWhoInGaac),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND GAAC)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoFamilyApproach")
  public CohortDefinition getPatientsActiveOnArtWhoFamilyApproach(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "FAMILY-APPROACH",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAMILY-APPROACH", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFamilyApproach),
            mappings));

    dsd.setCompositionString("(IN-ART AND FAMILY-APPROACH)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInFamilyApproach")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInFamilyApproach(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "FAMILY-APPROACH",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAMILY-APPROACH", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFamilyApproach),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND FAMILY-APPROACH)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInFamilyApproach")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInFamilyApproach(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "FAMILY-APPROACH",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FAMILY-APPROACH", DsdQueriesInterface.QUERY.findPatientWhoAreMdcFamilyApproach),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND FAMILY-APPROACH)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInAdhesionClub")
  public CohortDefinition getPatientsActiveOnArtWhoInAdhesionClub(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "ADHESION-CLUB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADHESION-CLUB", DsdQueriesInterface.QUERY.findPatientWhoAreMdcAdhesionClub),
            mappings));

    dsd.setCompositionString("(IN-ART AND ADHESION-CLUB)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInAdhesionClub")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInAdhesionClub(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "ADHESION-CLUB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADHESION-CLUB", DsdQueriesInterface.QUERY.findPatientWhoAreMdcAdhesionClub),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND ADHESION-CLUB)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInAdhesionClub")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInAdhesionClub(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "ADHESION-CLUB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADHESION-CLUB", DsdQueriesInterface.QUERY.findPatientWhoAreMdcAdhesionClub),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND ADHESION-CLUB)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInSemiannual")
  public CohortDefinition getPatientsActiveOnArtWhoInSemiannual(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "SEMIANNUAL-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SEMIANNUAL-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcSemiannual),
            mappings));

    dsd.setCompositionString("(IN-ART AND SEMIANNUAL-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInSemiannual")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInSemiannual(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "SEMIANNUAL-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SEMIANNUAL-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcSemiannual),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND SEMIANNUAL-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInSemiannual")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInSemiannual(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "SEMIANNUAL-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SEMIANNUAL-DISPENSATION",
                DsdQueriesInterface.QUERY.findPatientWhoAreMdcSemiannual),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND SEMIANNUAL-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtWhoInCummunity")
  public CohortDefinition getPatientsActiveOnArtWhoInCummunity(final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "IN-ART",
        EptsReportUtils.map(
            getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"), mappings));

    dsd.addSearch(
        "CUMMUNITY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CUMMUNITY-DISPENSATION", DsdQueriesInterface.QUERY.findPatientWhoAreMdcCummunity),
            mappings));

    dsd.setCompositionString("(IN-ART AND CUMMUNITY-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtElegibleToDsdWhoInCummunity")
  public CohortDefinition getPatientsActiveOnArtElegibleToDsdWhoInCummunity(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtEligibleForDsd("ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "CUMMUNITY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CUMMUNITY-DISPENSATION", DsdQueriesInterface.QUERY.findPatientWhoAreMdcCummunity),
            mappings));

    dsd.setCompositionString("(ELEGIBLE-DSD AND CUMMUNITY-DISPENSATION)");

    return dsd;
  }

  @DocumentedDefinition(value = "patientsActiveOnArtNotElegibleToDsdWhoInCummunity")
  public CohortDefinition getPatientsActiveOnArtNotElegibleToDsdWhoInCummunity(
      final String cohortName) {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DSD",
        EptsReportUtils.map(getPatientsActiveOnArtNotEligibleForDsd("NOT-ELEGIBLE-DSD"), mappings));

    dsd.addSearch(
        "CUMMUNITY-DISPENSATION",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CUMMUNITY-DISPENSATION", DsdQueriesInterface.QUERY.findPatientWhoAreMdcCummunity),
            mappings));

    dsd.setCompositionString("(NOT-ELEGIBLE-DSD AND CUMMUNITY-DISPENSATION)");

    return dsd;
  }
}
