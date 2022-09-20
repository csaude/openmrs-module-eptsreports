package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.dsd.DSDPatientsWhoExperiencedIITCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface.QUERY.DSDDispensationInterval;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface.QUERY.DSDModeTypeLevel1;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface.QUERY.DSDModelTypeLevel2;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TbQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DSDCohortQueries {

  private static final String FIND_DSD_DENOMINATOR_1 = "DSD/DSD_DENOMINATOR_D1.sql";

  @Autowired private GenericCohortQueries genericCohorts;

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getNumerator1() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD- Numerator 1");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("D1", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DT",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.QUARTERLY)),
            mappings));

    definition.addSearch(
        "DS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DS",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.SEMI_ANNUAL)),
            mappings));

    definition.addSearch(
        "DA",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DA",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.ANNUAL)),
            mappings));

    definition.addSearch(
        "DD",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DD",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DD)),
            mappings));

    definition.addSearch(
        "DC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DCA_APE)),
            mappings));

    definition.addSearch("FR", EptsReportUtils.map(this.getPatientsWhoAreFastTracked(), mappings));

    definition.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "GAAC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.GAAC)),
            mappings));

    definition.setCompositionString("D1 AND (DT OR DS OR DA OR DD OR DC OR  FR OR GAAC)");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDT")
  public CohortDefinition getDSDEligibleNumerator2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 2");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("D1", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DT",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.QUARTERLY)),
            mappings));

    definition.setCompositionString("D1 AND DT");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator2")
  public CohortDefinition getDSDNotEligibleNumerator2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 2");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("D2", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "DT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DT",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.QUARTERLY)),
            mappings));

    definition.setCompositionString("D2 AND DT");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDS")
  public CohortDefinition getDSDEligibleNumerator3() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 3");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DS",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.SEMI_ANNUAL)),
            mappings));

    definition.setCompositionString("IART AND DS");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator3")
  public CohortDefinition getDSDNotEligibleNumerator3() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 3");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "DS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DS",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.SEMI_ANNUAL)),
            mappings));

    definition.setCompositionString("IART AND DS");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDA")
  public CohortDefinition getDSDEligibleNumerator4() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 4");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DA",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DA",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.ANNUAL)),
            mappings));

    definition.setCompositionString("IART AND DA");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator4")
  public CohortDefinition getDSDNotEligibleNumerator4() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 4");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "DA",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DA",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDDispensationInterval.ANNUAL)),
            mappings));

    definition.setCompositionString("IART AND DA");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDD")
  public CohortDefinition getDSDEligibleNumerator5() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 5");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DD",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DD",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DD)),
            mappings));

    definition.setCompositionString("IART AND DD");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator5")
  public CohortDefinition getDSDNotEligibleNumerator5() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 5");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "DD",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DD",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DD)),
            mappings));

    definition.setCompositionString("IART AND DD");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDC")
  public CohortDefinition getDSDEligibleNumerator6() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 6");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "DC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DCA_APE)),
            mappings));

    definition.setCompositionString("IART AND DC");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator6")
  public CohortDefinition getDSDNotEligibleNumerator6() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 6");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "DC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DCA_APE)),
            mappings));

    definition.setCompositionString("IART AND DC");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDFR")
  public CohortDefinition getDSDEligibleNumerator7() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 7");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch("FR", EptsReportUtils.map(this.getPatientsWhoAreFastTracked(), mappings));

    definition.setCompositionString("IART AND FR");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator7")
  public CohortDefinition getDSDNotEligibleNumerator7() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 7");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch("FR", EptsReportUtils.map(this.getPatientsWhoAreFastTracked(), mappings));

    definition.setCompositionString("IART AND FR");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDGAAC")
  public CohortDefinition getDSDEligibleNumerator8() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 8");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    definition.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "GAAC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.GAAC)),
            mappings));

    definition.setCompositionString("IART AND GAAC");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNotStableNumerator8")
  public CohortDefinition getDSDNotEligibleNumerator8() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Not Eligible - Numerator 8");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator2(), mappings));

    definition.addSearch(
        "GAAC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "GAAC",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.GAAC)),
            mappings));

    definition.setCompositionString("IART AND GAAC");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDDCP")
  public CohortDefinition getDSDNumerator9() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD - Numerator 9");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "DCP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DCP",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.DCP)),
            mappings));

    definition.setCompositionString("IART AND DCP");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInDSDBM")
  public CohortDefinition getDSDNumerator10() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD - Numerator 10");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "BM",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "BM",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.BM)),
            mappings));

    definition.setCompositionString("IART AND BM");

    return definition;
  }

  @DocumentedDefinition(value = "DSD- Denominator 1")
  public CohortDefinition getDSDDenominator1() {
    final CompositionCohortDefinition dataSetDefinitio = new CompositionCohortDefinition();

    dataSetDefinitio.setName("DSD- Denominator 1");
    dataSetDefinitio.addParameter(new Parameter("endDate", "End Date", Date.class));
    dataSetDefinitio.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dataSetDefinitio.addSearch(
        "DENOMINATOR-1",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding DSD- Denominator 1 by Reporting Period",
                EptsQuerysUtils.loadQuery(FIND_DSD_DENOMINATOR_1)),
            mappings));

    dataSetDefinitio.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoArePregnantInAPeriod",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.PREGNANT)),
            "endDate=${endDate},location=${location}"));

    dataSetDefinitio.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreBreastfeeding",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.BREASTFEEDING)),
            "endDate=${endDate},location=${location}"));

    dataSetDefinitio.addSearch(
        "TB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreInTbTreatment",
                TbQueries.QUERY.findPatientsWhoAreInTbTreatmentFor7MonthsPriorEndReportingPeriod),
            mappings));

    dataSetDefinitio.addSearch(
        "IIT-PREVIOUS-PERIOD", EptsReportUtils.map(this.getPatientsOnRTTDSD(), mappings));

    dataSetDefinitio.addSearch(
        "SARCOMA-KAPOSI",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SARCOMA-KAPOSI",
                DSDQueriesInterface.QUERY.findPatientsWhoHaveBeenNotifiedOfKaposiSarcoma),
            mappings));

    dataSetDefinitio.addSearch(
        "ADVERSASE-REACTIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADVERSASE-REACTIONS",
                DSDQueriesInterface.QUERY
                    .findPatientsWithAdverseDrugReactionsRequiringRegularMonitoringNotifiedInLast6Months),
            mappings));

    dataSetDefinitio.setCompositionString(
        "DENOMINATOR-1 NOT (PREGNANT OR BREASTFEEDING OR TB OR IIT-PREVIOUS-PERIOD OR SARCOMA-KAPOSI OR ADVERSASE-REACTIONS )");

    return dataSetDefinitio;
  }

  @DocumentedDefinition(value = "DSD- Denominator 2")
  public CohortDefinition getDSDDenominator2() {
    final CompositionCohortDefinition dataSetDefinitio = new CompositionCohortDefinition();

    dataSetDefinitio.setName("DSD- Denominator 2");
    dataSetDefinitio.addParameter(new Parameter("endDate", "End Date", Date.class));
    dataSetDefinitio.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dataSetDefinitio.addSearch(
        "DENOMINATOR-2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding DSD- Denominator 1 by Reporting Period",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13()),
            mappings));

    dataSetDefinitio.addSearch(
        "DENOMINATOR-1", EptsReportUtils.map(this.getDSDDenominator1(), mappings));

    dataSetDefinitio.setCompositionString("DENOMINATOR-2 NOT DENOMINATOR-1");

    return dataSetDefinitio;
  }

  @DocumentedDefinition(value = "DSD- Denominator 3")
  public CohortDefinition getDSDDenominator3() {
    final CompositionCohortDefinition dataSetDefinitio = new CompositionCohortDefinition();

    dataSetDefinitio.setName("DSD- Denominator 3");
    dataSetDefinitio.addParameter(new Parameter("endDate", "End Date", Date.class));
    dataSetDefinitio.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dataSetDefinitio.addSearch(
        "DENOMINATOR-3",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding DSD- Denominator 3 by Reporting Period",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13()),
            mappings));

    dataSetDefinitio.setCompositionString("DENOMINATOR-3");

    return dataSetDefinitio;
  }

  @DocumentedDefinition(value = "PatientsOnRTTDSD")
  public CohortDefinition getPatientsOnRTTDSD() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();

    compositionDefinition.setName("DSD - Patients on RTT 3 months before reporting endDate");
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    compositionDefinition.addSearch(
        "IIT-PREVIOUS-PERIOD",
        EptsReportUtils.map(this.getPatientsWhoExperiencedIITCalculation(), mappings));

    compositionDefinition.addSearch(
        "TRF-IN",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreInTbTreatment",
                DSDQueriesInterface.QUERY.findPatientsWhoWhereTransferredInPriorReportingPeriod),
            "startDate=${endDate-3m},endDate=${endDate},location=${location}"));

    compositionDefinition.setCompositionString("IIT-PREVIOUS-PERIOD NOT TRF-IN");

    return compositionDefinition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator11() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 11");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "CM",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CM",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.CM)),
            mappings));

    definition.setCompositionString("IART AND CM");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator12() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 12");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "AF",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "AF",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.AF)),
            mappings));

    definition.setCompositionString("IART AND AF");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator13() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 13");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "CA",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CA",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.CA)),
            mappings));

    definition.setCompositionString("IART AND CA");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator14() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 14");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "EH",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "EH",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModeTypeLevel1.EH)),
            mappings));

    definition.setCompositionString("IART AND EH");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator15() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 15");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "TB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "TB",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.TB)),
            mappings));

    definition.setCompositionString("IART AND TB");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator16() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 16");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "CT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "CT",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.CT)),
            mappings));

    definition.setCompositionString("IART AND CT");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator17() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 17");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "SAAJ",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SAAJ",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.SAAJ)),
            mappings));

    definition.setCompositionString("IART AND SAAJ");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator18() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 18");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "SMI",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SMI",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.SMI)),
            mappings));

    definition.setCompositionString("IART AND SMI");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreActiveOnArtAndInAtleastOneDSD")
  public CohortDefinition getDSDEligibleNumerator19() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("DSD Eligible - Numerator 19");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch("IART", EptsReportUtils.map(this.getDSDDenominator3(), mappings));

    definition.addSearch(
        "DAH",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "DAH",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.DAH)),
            mappings));

    definition.setCompositionString("IART AND DAH");

    return definition;
  }

  @DocumentedDefinition(value = "DSD- Indicator excluding pregnants And breastfeedding")
  public CohortDefinition getDSDDenominatorExcludingPregnantsAndBreastFeeding(
      CohortDefinition denominator) {
    final CompositionCohortDefinition dataSetDefinitio = new CompositionCohortDefinition();

    dataSetDefinitio.setName("DSD- Indicator  Exclunding pregnant and BreastFeeding");
    dataSetDefinitio.addParameter(new Parameter("endDate", "End Date", Date.class));
    dataSetDefinitio.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dataSetDefinitio.addSearch("DENOMINATOR", EptsReportUtils.map(denominator, mappings));

    dataSetDefinitio.addSearch(
        "EXCLUSION", EptsReportUtils.map(this.getPregnantsAndBreastFeeding(), mappings));

    dataSetDefinitio.setCompositionString("DENOMINATOR NOT EXCLUSION");

    return dataSetDefinitio;
  }

  @DocumentedDefinition(value = "DSDPatientsWhoExperiencedIITCalculation")
  private CohortDefinition getPatientsWhoExperiencedIITCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "DSDPatientsWhoExperiencedIITCalculation",
            Context.getRegisteredComponents(DSDPatientsWhoExperiencedIITCalculation.class).get(0));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "getPatientsWhoAreFastTracked")
  private CohortDefinition getPatientsWhoAreFastTracked() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "patients currently on ART who are included in DSD model: Fluxo Rápido (FR)");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    definition.addSearch(
        "FR",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FR", DSDQueriesInterface.QUERY.findPatientsWhoAreFastTrack),
            mappings));

    definition.addSearch(
        "FR-MDC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FR",
                DSDQueriesInterface.QUERY.findPatientsWhoAreIncludedInDSDModel(
                    DSDModelTypeLevel2.FR)),
            mappings));

    definition.setCompositionString("FR OR FR-MDC");

    return definition;
  }

  @DocumentedDefinition(value = "DSD- Pregnants And Breastfeeding")
  private CohortDefinition getPregnantsAndBreastFeeding() {
    final CompositionCohortDefinition dataSetDefinitio = new CompositionCohortDefinition();

    dataSetDefinitio.setName("DSD- Pregnant and Breastfeeding");
    dataSetDefinitio.addParameter(new Parameter("endDate", "End Date", Date.class));
    dataSetDefinitio.addParameter(new Parameter("location", "location", Location.class));

    dataSetDefinitio.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoArePregnantInAPeriod",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.PREGNANT)),
            "endDate=${endDate},location=${location}"));

    dataSetDefinitio.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreBreastfeeding",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.BREASTFEEDING)),
            "endDate=${endDate},location=${location}"));

    dataSetDefinitio.setCompositionString("PREGNANT OR BREASTFEEDING");

    return dataSetDefinitio;
  }
}
