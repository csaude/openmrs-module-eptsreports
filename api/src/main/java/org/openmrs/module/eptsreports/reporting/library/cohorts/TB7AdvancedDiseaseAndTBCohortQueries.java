package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries.QUERY.PositivityLevel;
import org.openmrs.module.eptsreports.reporting.library.queries.TxNewQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB7AdvancedDiseaseAndTBCohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  public CohortDefinition
      getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-Indicator 1- Cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXNEW", Mapped.mapStraightThrough(findPatientsWhoAreNewlyEnrolledOnArt()));

    definition.addSearch("PREGNANT", Mapped.mapStraightThrough(this.getPatientsWhoArePregnant()));

    definition.addSearch(
        "HIGH-VL", Mapped.mapStraightThrough(findPatientsWithConsecutiveViralLoadResults()));

    definition.addSearch(
        "ART-RESTART", Mapped.mapStraightThrough(findPatientsWhoReinitiatedARTTreatment()));

    definition.setCompositionString("TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 2 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4-TXNEW",
        Mapped.mapStraightThrough(
            this.findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days()));

    definition.addSearch(
        "PREGNANT", Mapped.mapStraightThrough(this.getPatientsWhoArePregnantsWithCountCD4()));

    definition.addSearch(
        "HIGH-VL",
        Mapped.mapStraightThrough(findPatientsWithConsecutiveViralLoadResultsWithCD4Count()));

    definition.addSearch(
        "ART-RESTART",
        Mapped.mapStraightThrough(findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count()));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));

    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4-TXNEW",
        Mapped.mapStraightThrough(
            this
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions()));

    definition.addSearch(
        "PREGNANT",
        Mapped.mapStraightThrough(
            this.getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression()));

    definition.addSearch(
        "HIGH-VL",
        Mapped.mapStraightThrough(
            findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions()));

    definition.addSearch(
        "ART-RESTART",
        Mapped.mapStraightThrough(
            findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression()));

    definition.setCompositionString("CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 4 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4-IMMUNOSUPPRESSION",
        Mapped.mapStraightThrough(
            this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3()));

    definition.addSearch(
        "TBLAM",
        Mapped.mapStraightThrough(this.findPatientsWithTBLAMResultsByreportGenereationDate()));

    definition.setCompositionString("CD4-IMMUNOSUPPRESSION and TBLAM");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 1 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "FR12", Mapped.mapStraightThrough(this.findPatientsWithImmunoSupression()));

    definition.setCompositionString("FR12");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2WithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 4 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DENOMINATOR",
        Mapped.mapStraightThrough(
            this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2()));

    definition.addSearch(
        "TBLAM", Mapped.mapStraightThrough(this.findPatientsWhoHaveTBLAMResults()));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("(DENOMINATOR and TBLAM) NOT (TRANSFERREDOUT OR DEAD)");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "IMMUNO-SUPRR-AND-TBLAM",
        Mapped.mapStraightThrough(
            this
                .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2WithTBLAMResults()));

    definition.addSearch(
        "POSITIVE-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with Negative TBLAM Results during the inclusion period",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithPositiveTBLAMResults),
            mappings));

    definition.setCompositionString("IMMUNO-SUPRR-AND-TBLAM and POSITIVE-RESULTS");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsAndInitiatedTBTreatment() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "POSITIVE-TB",
        Mapped.mapStraightThrough(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults()));

    definition.addSearch(
        "INITIATED-TB-TREATMENT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clientes Who Initated TB Treatment",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoInitiatedTBTreatment),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("POSITIVE-TB and INITIATED-TB-TREATMENT");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithoutGenexpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "POSITIVE-RESULTS",
        Mapped.mapStraightThrough(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults()));

    definition.addSearch(
        "NO-GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "No GeneXpert by report generation date",
                String.format(
                    TB7AdvancedDiseaseQueries.QUERY.eValuatePatientsCheckingGenExpertTest,
                    "is null",
                    "is null")),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "GeneXpert by report generation date",
                String.format(
                    TB7AdvancedDiseaseQueries.QUERY.eValuatePatientsCheckingGenExpertTest,
                    "is not null",
                    "is not null")),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("(NO-GENEXPERT and POSITIVE-RESULTS) not GENEXPERT");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "POSITIVE-RESULTS",
        Mapped.mapStraightThrough(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults()));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "GeneXpert by report generation date",
                String.format(
                    TB7AdvancedDiseaseQueries.QUERY.eValuatePatientsCheckingGenExpertTest,
                    "is not null",
                    "is not null")),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("GENEXPERT and POSITIVE-RESULTS");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpertPositiveResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 4 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "GENEXPERT",
        Mapped.mapStraightThrough(
            this
                .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpert()));

    definition.addSearch(
        "GEN-POSITIVE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "GeneXpert Positive Results by report generation date",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithPositiveGenExpert),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("GENEXPERT and GEN-POSITIVE");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNewlyEnrolledOnArt")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnArt() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsWhoAreNewlyEnrolledOnArt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who initiated ART during inclusion period",
                TxNewQueries.QUERY.findPatientsWhoAreNewlyEnrolledOnART),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("TXNEW not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsWhoAreNewlyEnrolledOnArt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who initiated ART during inclusion period with a CD4 count within 33 days - Newly on ART",
                TB7AdvancedDiseaseQueries.QUERY
                    .findPatientsWhoInitiatedARTAndHaveCD4CountWithin33Days),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("TXNEW not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions")
  public CohortDefinition
      findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4RESULT", Mapped.mapStraightThrough(this.findPatientsWhoHaveCD4ResultsTx_NEW()));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4RESULT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults")
  public CohortDefinition
      findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXNEW",
        Mapped.mapStraightThrough(
            this
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions()));

    definition.addSearch(
        "TBLAM",
        Mapped.mapStraightThrough(this.findPatientsWithTBLAMResultsByreportGenereationDate()));

    definition.setCompositionString("TXNEW and TBLAM");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResults")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Clients with Consecutive High VL Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                " Clients with two consecutive high viral load results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithHighViralLoad),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("HIGHVL not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResultsWithCD4Count")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResultsWithCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Clients with Consecutive High VL Results with CD4 count");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with two consecutive high viral load results with CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWIthHighVLWithCD4Count),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("HIGHVL not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions")
  public CohortDefinition
      findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4RESULT", Mapped.mapStraightThrough(this.findPatientsWhoHaveCD4ResultsHIGH_VL()));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4RESULT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults")
  public CohortDefinition
      findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "HIGHVL",
        Mapped.mapStraightThrough(
            this
                .findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions()));
    definition.addSearch(
        "TBLAM",
        Mapped.mapStraightThrough(this.findPatientsWithTBLAMResultsByreportGenereationDate()));

    definition.setCompositionString("HIGHVL and TBLAM");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTTreatment")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatment() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who reinitiated ART during inclusion period",
                TB7AdvancedDiseaseQueries.QUERY.findPatientWhoReinitiatedART),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("ART-RESTART not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTWhoHaveCD4Count")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who reinitiated ART during inclusion period and have CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoReinitiatedARTWhoHaveCD4Count),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("ART-RESTART not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression")
  public CohortDefinition
      findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "Clients who reinitiated ART during inclusion period and are ImmunoSuppression");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4RESULT", Mapped.mapStraightThrough(this.findPatientsWhoHaveCD4ResultsARTRESTART()));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4RESULT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults")
  public CohortDefinition
      findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "ART-RESTART",
        Mapped.mapStraightThrough(
            this
                .findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression()));

    definition.addSearch(
        "TBLAM",
        Mapped.mapStraightThrough(this.findPatientsWithTBLAMResultsByreportGenereationDate()));
    definition.setCompositionString("((ART-RESTART and TBLAM");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnant() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients With CD4",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_CD4_AFTER_FIRST_PREGNANT_REGISTRATION)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("PREGNANT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnantsWithCountCD4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Women Who are Pregnant and have CD4 Count",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANTS_WITH_COUNT_CD4)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("PREGNANT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4-with-immunosupression");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4RESULT", Mapped.mapStraightThrough(this.findPatientsWhoHaveCD4ResultsPREGNANT()));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4RESULT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition
      getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppressionWithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4-with-immunosupressionWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        Mapped.mapStraightThrough(
            this.getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression()));

    definition.addSearch(
        "TBLAM",
        Mapped.mapStraightThrough(this.findPatientsWithTBLAMResultsByreportGenereationDate()));

    definition.setCompositionString("PREGNANT and TBLAM");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithNegativeTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Negative TB LAM Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "NEGATIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with Negative TBLAM Results during the inclusion period",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithNegativeTBLAMResults),
            mappings));

    definition.addSearch(
        "POSITIVE-RESULTS", Mapped.mapStraightThrough(this.findPatientsWithPositiveTBLAMResults()));

    definition.addSearch(
        "INDICATOR-DENOMINATOR",
        Mapped.mapStraightThrough(
            this
                .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2WithTBLAMResults()));

    definition.setCompositionString(
        "(NEGATIVE-TBLAM-RESULTS not POSITIVE-RESULTS) and INDICATOR-DENOMINATOR");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_FOUR.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappingsGrades = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_THREE.name(),
                query),
            mappings));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappingsGrades));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT GRADE4");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappingsGrades = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_TWO.name(),
                query),
            mappings));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(), mappingsGrades));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT (GRADE3 OR GRADE4)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappingsGrades = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_ONE.name(),
                query),
            mappings));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(), mappingsGrades));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT (GRADE2 OR GRADE3 OR GRADE4)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade no Level");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappingsGrades = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_ZERO_GRADE_LEVEL));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.NO_GRADE.name(),
                query),
            mappings));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(), mappingsGrades));

    definition.addSearch(
        "GRADE1",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults(), mappingsGrades));

    definition.setCompositionString(
        "POSITIVE-TBLAM-RESULTS NOT (GRADE4 OR GRADE3 OR GRADE2 OR GRADE1)");

    return definition;
  }

  private CohortDefinition findPatientsWithPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "LEVEL4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL1",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "NO-LEVEL",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults(), mappings));

    definition.setCompositionString("LEVEL4 or LEVEL3 or LEVEL2 or LEVEL1 or NO-LEVEL");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreTransferrdOut")
  private CohortDefinition findPatientsWhoAreTransferredOutByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Clients Who Are TransferredOut by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(
            TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoAreDeadByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Clients Who Are Dead by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_ARE_DEAD));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsTx_NEW() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - TX_NEW",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_TX_NEW)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithImmunoSupression")
  private CohortDefinition findPatientsWithImmunoSupression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients ImmunoSupression ",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_CASCATE2)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsHIGH_VL() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - consecutive HighL",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_HIGH_VL)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsARTRESTART() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - ART Restart",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsARTRESTART_CASCATE2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - ART Restart",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART_CASCATE2)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsPREGNANT() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings =
        "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - PREGNANT",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoHaveCD4ResultsPREGNANT_CASCATE2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with CD4 Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings =
        "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4Results",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with CD4 Results during the inclusion period - PREGNANT",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY
                        .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT_CASCATE2)),
            mappings));

    definition.setCompositionString("CD4Results");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveTBLAMResults")
  private CohortDefinition findPatientsWhoHaveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with TBLAM Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with TBLAM Results By the end of  Report Generation Date",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            mappings));

    definition.setCompositionString("TBLAM-RESULTS");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithTBLAMResultsByreportGenereationDate")
  private CohortDefinition findPatientsWithTBLAMResultsByreportGenereationDate() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("clients with TBLAM Result");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";

    definition.addSearch(
        "TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with TBLAM Results By the end of  Report Generation Date",
                TB7AdvancedDiseaseQueries.QUERY
                    .findPatientsWithTBLAMResultsByreportGenereationDate),
            mappings));

    definition.setCompositionString("TBLAM-RESULTS");

    return definition;
  }
}
