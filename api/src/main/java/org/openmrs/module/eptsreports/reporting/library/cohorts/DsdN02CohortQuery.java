package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.DsdQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DsdN02CohortQuery {

  @Autowired private GenericCohortQueries genericCohorts;
  @Autowired private DSDCohortQueries dsdCohortQueries;

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
            dsdCohortQueries.getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb("IN-ART"),
            mappings));

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
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtEligibleForDsd("ELEGIBLE"), mappings));

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

  public CohortDefinition getN02LessThan2ActiveOnArtNotElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DT",
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt("NOT-ELEGIBLE-DT"),
            mappings));

    dsd.addSearch(
        "ADULT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADULT", DsdQueriesInterface.QUERY.findPatientsAgeLessThan2),
            mappings));

    dsd.setCompositionString("NOT-ELEGIBLE-DT AND ADULT");

    return dsd;
  }

  public CohortDefinition getN02AdultActiveOnArtNotElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DT",
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt("NOT-ELEGIBLE-DT"),
            mappings));

    dsd.addSearch(
        "ADULT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADULT", DsdQueriesInterface.QUERY.findPatientsAge15Plus),
            mappings));

    dsd.setCompositionString("NOT-ELEGIBLE-DT AND ADULT");

    return dsd;
  }

  public CohortDefinition getN02Child2To4ActiveOnArtNotElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "NOT-ELEGIBLE-DT",
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt("NOT-ELEGIBLE-DT"),
            mappings));

    dsd.addSearch(
        "CHILD2TO4",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD2TO4", DsdQueriesInterface.QUERY.findPatientsAge2to4),
            mappings));

    dsd.setCompositionString("NOT-ELEGIBLE-DT AND CHILD2TO4");

    return dsd;
  }

  public CohortDefinition getN02Child5To9ActiveOnArtNotElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT",
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt(""), mappings));

    dsd.addSearch(
        "CHILD5TO9",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD5TO9", DsdQueriesInterface.QUERY.findPatientsAge5to9),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND CHILD5TO9");

    return dsd;
  }

  public CohortDefinition getN02Child10To14ActiveOnArtNotElegibleDsdWhoInDt(
      final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT",
        EptsReportUtils.map(
            dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt("ELEGIBLE-DT"), mappings));

    dsd.addSearch(
        "CHILD10T14",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD10T14", DsdQueriesInterface.QUERY.findPatientsAge10to14),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND CHILD10T14");

    return dsd;
  }

  public CohortDefinition getN02AdultActiveOnArtElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT",
        EptsReportUtils.map(getPatientsActiveOnArtElegibleDsdWhoInDt("ELEGIBLE-DT"), mappings));

    dsd.addSearch(
        "ADULT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADULT", DsdQueriesInterface.QUERY.findPatientsAge15Plus),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND ADULT");

    return dsd;
  }

  public CohortDefinition getN02Child2To4ActiveOnArtElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT",
        EptsReportUtils.map(getPatientsActiveOnArtElegibleDsdWhoInDt("ELEGIBLE-DT"), mappings));

    dsd.addSearch(
        "CHILD2TO4",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD2TO4", DsdQueriesInterface.QUERY.findPatientsAge2to4),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND CHILD2TO4");

    return dsd;
  }

  public CohortDefinition getN02Child5To9ActiveOnArtElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT", EptsReportUtils.map(getPatientsActiveOnArtElegibleDsdWhoInDt(""), mappings));

    dsd.addSearch(
        "CHILD5TO9",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD5TO9", DsdQueriesInterface.QUERY.findPatientsAge5to9),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND CHILD5TO9");

    return dsd;
  }

  public CohortDefinition getN02Child10To14ActiveOnArtElegibleDsdWhoInDt(final String cohortName) {

    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName(cohortName);
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ELEGIBLE-DT",
        EptsReportUtils.map(getPatientsActiveOnArtElegibleDsdWhoInDt("ELEGIBLE-DT"), mappings));

    dsd.addSearch(
        "CHILD10T14",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CHILD10T14", DsdQueriesInterface.QUERY.findPatientsAge10to14),
            mappings));

    dsd.setCompositionString("ELEGIBLE-DT AND CHILD10T14");

    return dsd;
  }
}
