package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DSDCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DsdD01CohortQuery;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DsdN02CohortQuery;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EriDSDDataset extends BaseDataSet {

  @Autowired private DSDCohortQueries dsdCohortQueries;
  @Autowired private DsdD01CohortQuery dsdD01CohortQuery;
  @Autowired private DsdN02CohortQuery dsdN02CohortQuery;
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;
  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructEriDSDDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.setName("DSD Data Set");
    dsd.addParameters(getParameters());
    dsd.addDimension(
        "age", EptsReportUtils.map(eptsCommonDimension.getEri2DsdDimension2(), mappings));
    dsd.setName("Total");

    // DSD: Denominator: Number of active patients on ART - (Non-pregnant and Non-Breastfeeding not
    // on TB treatment)
    dsd.addColumn(
        "D1T",
        "DSD D1 Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "DSD D1 Total",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtExcludingPregnantBreastfeedingAndTb(
                        "DSD D1 Total"),
                    mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D2T",
        "DSD D1 Sub Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "DSD D2 Total",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtEligibleForDsd("DSD D1 Sub Total"),
                    mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D1SNPNB",
        "Adults (>=15)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Adults (>=15)",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getAdultActiveOnArtElegibleDsd("Adults (>=15)"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D1SNPNBC-01",
        "2-4",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "2-4",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild2To4ActiveOnArtElegibleDsd("2-4"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D1SNPNBC-02",
        "5-9",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "5-9",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild5To9ActiveOnArtElegibleDsd("5-9"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D1SNPNBC-03",
        "10-14",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "10-14",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild10To14ActiveOnArtElegibleDsd("10-14"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "D2TA",
        "D2TA",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "D2TA Total",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotEligibleForDsd("D2TA Total"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "D2NPNB",
        "15+",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "15+",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getAdultActiveOnArtNotElegibleDsd("15+"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "D2NPNBC-D01",
        "<2",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "<2",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChildLessthan2ActiveOnArtNotElegibleDsd("<2"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "D2NPNBC-01",
        "2-4",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "2-4",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild2To4ActiveOnArtNotElegibleDsd("2-4"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D2NPNBC-02",
        "5-9",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "5-9",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild5To9ActiveOnNotArtElegibleDsd("5-9"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "D2NPNBC-03",
        "10-14",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "10-14",
                EptsReportUtils.map(
                    dsdD01CohortQuery.getChild10To14ActiveOnArtNotElegibleDsd("10-14"), mappings)),
            mappings),
        "");
    // DSD: Numerator: Number of active on ART whose next ART pick-up is schedule for 83-97 days
    // after the date of their last ART drug pick-up (DT) - (Non-pregnant and Non-Breastfeeding not
    // on TB treatment)
    dsd.addColumn(
        "N1T",
        "N1T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInDt("N1T Total"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1SST",
        "N1SST Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleDsdWhoInDt("N1SST Total"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1SNPNBA",
        "15+",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "15+",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02AdultActiveOnArtElegibleDsdWhoInDt("15+"), mappings)),
            mappings),
        "");
    dsd.addColumn(
        "N1SNPNBC-01",
        "2-4",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "2-4",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child2To4ActiveOnArtElegibleDsdWhoInDt("2-4"),
                    mappings)),
            mappings),
        "");
    dsd.addColumn(
        "N1SNPNBC-02",
        "5-9",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "5-9",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child5To9ActiveOnArtElegibleDsdWhoInDt("5-9"),
                    mappings)),
            mappings),
        "");
    dsd.addColumn(
        "N1SNPNBC-03",
        "10-14",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "10-14",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child10To14ActiveOnArtElegibleDsdWhoInDt("10-14"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1UST",
        "N1UST Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleDsdWhoInDt("N1UST Total"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1UNPNBA",
        "15+",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "15+",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02AdultActiveOnArtNotElegibleDsdWhoInDt("15+"),
                    mappings)),
            mappings),
        "");
    dsd.addColumn(
        "N1UNPNBC-D01",
        "<2",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "<2",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02LessThan2ActiveOnArtNotElegibleDsdWhoInDt("<2"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1UNPNBC-01",
        "2-4",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "2-4",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child2To4ActiveOnArtNotElegibleDsdWhoInDt("2-4"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1UNPNBC-02",
        "5-9",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "5-9",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child5To9ActiveOnArtNotElegibleDsdWhoInDt("5-9"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N1UNPNBC-03",
        "10-14",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "10-14",
                EptsReportUtils.map(
                    dsdN02CohortQuery.getN02Child10To14ActiveOnArtNotElegibleDsdWhoInDt("10-14"),
                    mappings)),
            mappings),
        "");

    // DSD: Numerator: Number of active patients on ART whose next clinical
    // consultation is scheduled 175-190 days after the date of the last
    // clinical consultation (FR) - (Non-pregnant and Non-Breastfeeding not
    // on TB treatment)

    dsd.addColumn(
        "N2T",
        "N2T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInFastFlow("N2T Total"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N2SST",
        "N2SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInFastFlow(
                        "N2SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //    Not Elegible
    dsd.addColumn(
        "N2UST",
        "N2UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInFastFlow(
                        "N2UST Sub Total"),
                    mappings)),
            mappings),
        "");

    // DSD: Numerator: Number of active patients on ART (Non-pregnant and
    // Non-Breastfeeding not on TB treatment) that are participating in GAAC
    // at the end of the month prior to month of results submission deadline
    // (GAAC)
    dsd.addColumn(
        "N3T",
        "N3T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInGaac("N3T Total"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N3SST",
        "N3SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInGaac(
                        "N3SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        Not Elegible
    dsd.addColumn(
        "N3UST",
        "N3UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInGacc(
                        "N2UST Sub Total"),
                    mappings)),
            mappings),
        "");

    //    DSD: Numerator: Number of active patients on ART (Non-pregnant and Non-Breastfeeding not
    // on TB treatment) on Abordagem Familiar (AF)
    dsd.addColumn(
        "N4T",
        "N4T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoFamilyApproach("N4T Total"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N4SST",
        "N4SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInFamilyApproach(
                        "N4SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        Not Elegible
    dsd.addColumn(
        "N4UST",
        "N4UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInFamilyApproach(
                        "N4UST Sub Total"),
                    mappings)),
            mappings),
        "");

    //    DSD: Numerator: Number of active patients on Clubes de Adesão (CA) - (Non-pregnant and
    // Non-Breastfeeding not on TB treatment)
    dsd.addColumn(
        "N5T",
        "N5T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInAdhesionClub("N5T Total"),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N5SST",
        "N5SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInAdhesionClub(
                        "N5SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        Not Elegible
    dsd.addColumn(
        "N5UST",
        "N5UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInAdhesionClub(
                        "N5UST Sub Total"),
                    mappings)),
            mappings),
        "");

    //    DSD: Numerator: Number of active patients on ART (Non-pregnant and Non-Breastfeeding not
    // on TB treatment) on Dispensa Semestral (DS)
    dsd.addColumn(
        "N6T",
        "N6T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInSemiannual("N6T Total"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N6SST",
        "N6SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInSemiannual(
                        "N6SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        Not Elegible
    dsd.addColumn(
        "N6UST",
        "N6UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInSemiannual(
                        "N6UST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        DSD: Numerator: Number of active patients on Dispensa Comunitária (DC) - (Non-pregnant
    // and Non-Breastfeeding not on TB treatment)

    dsd.addColumn(
        "N7T",
        "N7T Total",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtWhoInCummunity("N7T Total"), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "N7SST",
        "N7SST  SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtElegibleToDsdWhoInCummunity(
                        "N7SST Sub Total"),
                    mappings)),
            mappings),
        "");

    //        Not Elegible
    dsd.addColumn(
        "N7UST",
        "N7UST SubTotal",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "SUB TOTAL",
                EptsReportUtils.map(
                    dsdCohortQueries.getPatientsActiveOnArtNotElegibleToDsdWhoInCummunity(
                        "N7UST Sub Total"),
                    mappings)),
            mappings),
        "");
    // addRow(
    // dsd,
    // "D1SNPNBC",
    // "Non-pregnant and Non-Breastfeeding Adults",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "D1SNPNBC",
    // EptsReportUtils.map(
    // this.genericCohorts.generalSql("",
    // DsdQueriesInterface.QUERY.findPatientsAge15Plus),
    // mappings)),
    // mappings),
    // getChildrenColumn());

    // dsd.addColumn(
    // "D2T",
    // "DSD D2 Total",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "DSD D2 Total",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndUnstable(), mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "D2NPNB",
    // "Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "D2NPNB",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingD2(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "D2NPNBC",
    // "Non-pregnant and Non-Breastfeeding Children By age",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "D2NPNBC",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingD2(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "D2BNP",
    // "Breastfeeding (exclude pregnant)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "D2BNP",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreBreastFeedingAndNotPregnant(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "D2PNB",
    // "Pregnant (exclude breastfeeding)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "D2PNB",
    // EptsReportUtils.map(eriDSDCohortQueries.getPatientsWhoArePregnant(),
    // mappings)),
    // mappings),
    // "");
    // /*dsd.addColumn(
    // "NT",
    // "DSD N Total",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NT",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModel(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "");
    // dsd.addColumn(
    // "NSST",
    // "DSD N Stable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NSST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelStable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "");
    // dsd.addColumn(
    // "NSNPNB",
    // "Stable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NSNPNB",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelStable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "NSNPNBC",
    // "Stable Non-pregnant and Non-Breastfeeding Children By age",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NSNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelStable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "NUST",
    // "DSD N Unstable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NUST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelUnstable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "");
    // dsd.addColumn(
    // "NUNPNB",
    // "Unstable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NUNPNB",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelUnstable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "NUNPNBC",
    // "Unstable Non-pregnant and Non-Breastfeeding Children By age",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NUNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticipateInDsdModelUnstable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "NUBNP",
    // "N Unstable Breastfeeding (exclude pregnant)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NUBNP",
    // EptsReportUtils.map(
    // eriDSDCohortQueries
    //
    // .getPatientsWhoAreBreastFeedingAndNotPregnantAndParticipateInDsdModelUnstable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "");
    // dsd.addColumn(
    // "NUPB",
    // "N Unstable Pregnant (include breastfeeding)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "NUPB",
    // EptsReportUtils.map(
    // eriDSDCohortQueries
    //
    // .getPatientsWhoArePregnantAndNotBreastFeedingAndParticipateInDsdModelUnstable(),
    // "endDate=${endDate},location=${location}")),
    // mappings),
    // "");*/
    // dsd.addColumn(
    // "N1T",
    // "DSD N1 Total",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1T",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreActiveWithNextPickupAs3Months(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N1SST",
    // "DSD N1 Stable Subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1SST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN1Stable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N1SNPNBA",
    // "N1 Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1SNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN1Stable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N1SNPNBC",
    // "N1 Non-pregnant and Non-Breastfeeding Children (<15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1SNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN1Stable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N1UST",
    // "DSD N1 Unstable Subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1UST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveWithNextPickupAs3MonthsAndUnstable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N1UNPNBA",
    // "N1 Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1UNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN1Unstable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N1UNPNBC",
    // "N1 Non-pregnant and Non-Breastfeeding Children (<15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1UNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN1Unstable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N1UBNP",
    // "N1 Patients who are breastfeeding excluding pregnant patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1UBNP",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreBreastfeedingAndNotPregnantN1(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N1UPB",
    // "N1: Pregnant: includes breastfeeding patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N1UPB",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoArePregnantAndNotBreastfeedingN1(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N2T",
    // "DSD N2 Total",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2T",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWithNextConsultationScheduled175To190Days(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N2SST",
    // "DSD N2 Stable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2SST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN2Stable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N2SNPNBA",
    // "DSD N2 Stable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2SNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN2Stable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N2SNPNBC",
    // " DSD N2 Stable Non-pregnant and Non-Breastfeeding Children (2-4,
    // 5-9, 10-14)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2SNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN2Stable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N2UST",
    // "DSD N2 Unstable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2UST",
    // EptsReportUtils.map(
    // eriDSDCohortQueries
    // .getPatientsWithNextConsultationScheduled175To190DaysUnstable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N2UNPNBA",
    // "DSD N2 Unstable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2UNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN2Unstable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N2UNPNBC",
    // " DSD N2 Unstable Non-pregnant and Non-Breastfeeding Children (2-4,
    // 5-9, 10-14)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2UNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN2Unstable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N2UBNP",
    // "N2 Patients who are breastfeeding excluding pregnant patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2UBNP",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreBreastfeedingAndNotPregnantN2(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N2UPB",
    // "N2: Pregnant: includes breastfeeding patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N2UPB",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoArePregnantAndBreastfeedingN2(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N3T",
    // "DSD N3 Total",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3T",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticpatingInGaac(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N3SST",
    // "DSD N3 Stable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3SST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN3Stable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N3SNPNBA",
    // "DSD N3 Stable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3SNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN3Stable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N3SNPNBC",
    // " DSD N3 Stable Non-pregnant and Non-Breastfeeding Children (2-4,
    // 5-9, 10-14)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3SNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN3Stable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N3UST",
    // "DSD N3 Unstable subtotal",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3UST",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreActiveAndParticpatingInGaacUnstable(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N3UNPNBA",
    // "DSD N3 Unstable Non-pregnant and Non-Breastfeeding Adults (>=15)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3UNPNBA",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN3Unstable(),
    // mappings)),
    // mappings),
    // "age=15+");
    // addRow(
    // dsd,
    // "N3UNPNBC",
    // " DSD N3 Unstable Non-pregnant and Non-Breastfeeding Children (2-4,
    // 5-9, 10-14)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3UNPNBC",
    // EptsReportUtils.map(
    //
    // eriDSDCohortQueries.getPatientsWhoAreNotPregnantAndNotBreastfeedingN3Unstable(),
    // mappings)),
    // mappings),
    // getChildrenColumn());
    // dsd.addColumn(
    // "N3UBNP",
    // "N3 Patients who are breastfeeding excluding pregnant patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3UBNP",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoAreBreastfeedingAndNotPregnantN3(),
    // mappings)),
    // mappings),
    // "");
    // dsd.addColumn(
    // "N3UPB",
    // "N3: Pregnant: includes breastfeeding patients",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "N3UPB",
    // EptsReportUtils.map(
    // eriDSDCohortQueries.getPatientsWhoArePregnantAndBreastfeedingN3(),
    // mappings)),
    // mappings),
    // "");
    //
    return dsd;
  }

  private List<ColumnParameters> getChildrenColumn() {
    ColumnParameters twoTo4 = new ColumnParameters("twoTo4", "2-4", "age=2-4", "01");
    ColumnParameters fiveTo9 = new ColumnParameters("fiveTo9", "5-9", "age=5-9", "02");
    ColumnParameters tenTo14 = new ColumnParameters("tenTo14", "10-14", "age=10-14", "03");

    return Arrays.asList(twoTo4, fiveTo9, tenTo14);
  }
}
