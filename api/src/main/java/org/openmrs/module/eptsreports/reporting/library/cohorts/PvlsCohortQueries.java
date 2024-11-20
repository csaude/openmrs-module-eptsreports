package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TxPvlsQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.TxPvlsQueriesInterface.QUERY.WomanState;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class PvlsCohortQueries {

  @DocumentedDefinition(
      value = "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months);

    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsTarget")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsTarget() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsTarget");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsTarget);

    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsRotine")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsRotine() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MOTHSVL12MONTHSVL",
        EptsReportUtils.map(
            this.findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "TARGET",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsTarget(),
            mappings));

    definition.setCompositionString("IART3MOTHSVL12MONTHSVL NOT TARGET");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine")
  public CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MOTHSVL12MONTHSVL1000",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "TARGET1000",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget(),
            mappings));

    definition.setCompositionString("IART3MOTHSVL12MONTHSVL1000 NOT TARGET1000");

    return definition;
  }

  @DocumentedDefinition(
      value = "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                WomanState.PREGNANT));

    return definition;
  }

  @DocumentedDefinition(
      value =
          "BreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  public CohortDefinition
      findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "BreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                WomanState.BREASTFEEDING));

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "PREGNANT3MONTHSVL12",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("PREGNANT3MONTHSVL12 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "PREGNANT3MONTHSVL12",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("PREGNANT3MONTHSVL12 AND NUMERATOR");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator")
  public CohortDefinition
      findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsDenominator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "BREASTEFEEDING3MONTHSVL12",
        EptsReportUtils.map(
            this
                .findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("BREASTEFEEDING3MONTHSVL12 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator")
  public CohortDefinition
      findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsNumerator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "BREASTEFEEDING3MONTHSVL12",
        EptsReportUtils.map(
            this
                .findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.addSearch(
        "NUMERATOR",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("BREASTEFEEDING3MONTHSVL12 AND NUMERATOR");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Rotine")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Rotine() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MONTHSVL12MONTHSROTINE",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine(),
            mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("IART3MONTHSVL12MONTHSROTINE AND PREGNANT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Target")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Target() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MONTHSVL12MONTHSTARGET",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget(),
            mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this
                .findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("IART3MONTHSVL12MONTHSTARGET AND PREGNANT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Rotine")
  public CohortDefinition
      findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Rotine() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MONTHSVL12MONTHSROTINE",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this
                .findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("IART3MONTHSVL12MONTHSROTINE AND BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Target")
  public CohortDefinition
      findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Target() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsRotine");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "IART3MONTHSVL12MONTHSTARGET",
        EptsReportUtils.map(
            this
                .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12MonthsTarget(),
            mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this
                .findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            mappings));

    definition.setCompositionString("IART3MONTHSVL12MONTHSTARGET AND BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(value = "findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominators")
  public CohortDefinition findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominators() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominators");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPregnantAdnBreastfeedingWomenSuplementalCoverageDenominators(
                WomanState.BREASTFEEDING));

    return definition;
  }

  @DocumentedDefinition(value = "findPregnantWomanForTxPvlsSupplementalCoverageDenominators")
  public CohortDefinition findPregnantWomanForTxPvlsSupplementalCoverageDenominators() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPregnantWomanForTxPvlsSupplementalCoverageDenominators");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        TxPvlsQueriesInterface.QUERY
            .findPregnantAdnBreastfeedingWomenSuplementalCoverageDenominators(WomanState.PREGNANT));

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Target")
  public CohortDefinition
      findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominatorsPatients() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominatorsPatients");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominators(), mappings));

    definition.setCompositionString("BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPregnantWomanForTxPvlsSupplementalCoverageDenominatorsPatients")
  public CohortDefinition findPregnantWomanForTxPvlsSupplementalCoverageDenominatorsPatients() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findPregnantWomanForTxPvlsSupplementalCoverageDenominatorsPatients");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.findPregnantWomanForTxPvlsSupplementalCoverageDenominators(), mappings));

    definition.setCompositionString("PREGNANT");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPregnantAndBreastfeedingWomenForTxPvlsSupplementalCoverageDenominatorsPatients")
  public CohortDefinition
      findPregnantAndBreastfeedingWomenForTxPvlsSupplementalCoverageDenominatorsPatients() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPregnantAndBreastfeedingWomenForTxPvlsSupplementalCoverageDenominatorsPatients");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.findPregnantWomanForTxPvlsSupplementalCoverageDenominators(), mappings));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominators(), mappings));

    definition.setCompositionString("PREGNANT OR BREASTFEEDING");

    return definition;
  }
}
