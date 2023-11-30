/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.eptsreports.reporting.library.cohorts;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.metadata.TbMetadata;
import org.openmrs.module.eptsreports.reporting.calculation.resumomensal.ResumoMensalTBCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.resumomensal.ResumoMensalTbExclusionCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.SqlEncounterQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCohortQueries {

  private HivMetadata hivMetadata;
  private TbMetadata tbMetadata;
  private GenericCohortQueries genericCohortQueries;
  @Autowired private TxNewCohortQueries txNewCohortQueries;

  @Autowired
  public ResumoMensalCohortQueries(
      HivMetadata hivMetadata, TbMetadata tbMetadata, GenericCohortQueries genericCohortQueries) {
    this.hivMetadata = hivMetadata;
    this.setTbMetadata(tbMetadata);
    this.genericCohortQueries = genericCohortQueries;
  }

  /** A1 Number of patients who initiated Pre-TARV at this HF by end of previous month */
  public CohortDefinition getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PRETARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1",
                ResumoMensalQueries
                    .getAllPatientsWithPreArtStartDateLessThanReportingStartDateA1()),
            mappings));

    definition.addSearch(
        "TRASFERED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1",
                ResumoMensalQueries
                    .getPatientsTransferredFromAnotherHealthFacilityDuringTheCurrentMonth()),
            mappings));

    definition.setCompositionString("PRETARV NOT TRASFERED");

    return definition;
  }

  /**
   * A2 Number of patients who initiated Pre-TARV at this HF during the current month
   *
   * @return CohortDefinition
   */
  public CohortDefinition getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA2");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("patientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PRETARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "patientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2",
                ResumoMensalQueries.getAllPatientsWithPreArtStartDateWithBoundariesA2()),
            mappings));

    definition.addSearch(
        "TRASFERED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA2",
                ResumoMensalQueries
                    .getPatientsTransferredFromAnotherHealthFacilityDuringTheCurrentStartDateEndDate()),
            mappings));

    definition.setCompositionString("PRETARV NOT TRASFERED");

    return definition;
  }

  /**
   * A3 = A.1 + A.2
   *
   * @return CohortDefinition
   */
  public CohortDefinition getSumOfA1AndA2() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    cd.setName("Sum of A1 and A2");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
    cd.addSearch(
        "A1", map(getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA1(), mappings));
    cd.addSearch(
        "A2", map(getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2(), mappings));
    cd.setCompositionString("A1 OR A2");
    return cd;
  }

  // Start of the B queries
  /**
   * B1 Number of patientes who initiated TARV at this HF during the current month
   *
   * @return CohortDefinition
   */
  public CohortDefinition getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    cd.setName("Number of patientes who initiated TARV at this HF End Date");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    cd.addSearch(
        "B1",
        map(
            txNewCohortQueries.getTxNewCompositionCohortMISAU(
                "Number of patientes who initiated TARV"),
            mappings));
    cd.setCompositionString("B1");
    return cd;
  }

  /**
   * B.2: Number of patients transferred-in from another HFs during the current month
   *
   * @return Cohort
   * @return CohortDefinition
   */
  public CohortDefinition
      getNumberOfPatientsTransferredInFromOtherHealthFacilitiesDuringCurrentMonthB2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TRANSFERED-IN-1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithAProgramStateMarkedAsTransferedInInAPeriod",
                ResumoMensalQueries.findPatientsWithAProgramStateMarkedAsTransferedInInAPeriodB2),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN-2",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithAProgramStateMarkedAsTransferedInInAPeriod",
                ResumoMensalQueries
                    .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardB2),
            mappings));

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "B12",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12()),
            mappings));

    definition.setCompositionString("(TRANSFERED-IN-1 OR TRANSFERED-IN-2) NOT(B12)");

    return definition;
  }

  public CohortDefinition getSumB3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch("ABANDONED", EptsReportUtils.map(this.getAbandonedPatient(), mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "PICKUP",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PICKUP", ResumoMensalQueries.getPatientsWhoHaveDrugPickup()),
            mappings));

    definition.setCompositionString("(ABANDONED OR SUSPEND) AND PICKUP ");

    return definition;
  }

  public CohortDefinition getAbandonedPatient() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ABANDONED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ABANDONED", ResumoMensalQueries.getPatientsWhoAbandonedTratmentToBeExclude()),
            mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "DIED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DIED", ResumoMensalQueries.getPatientsWhoDiedTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "TRFOUT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRFOUT",
                ResumoMensalQueries.getPatientsTransferredFromAnotherHealthFacilityLastMonth()),
            mappings));

    definition.setCompositionString("ABANDONED NOT (SUSPEND OR DIED OR TRFOUT)");

    return definition;
  }

  /**
   * B.5: Number of patients transferred-out from another HFs during the current month
   *
   * @return Cohort
   * @return CohortDefinition
   */
  @DocumentedDefinition(value = "B5")
  public CohortDefinition
      getNumberOfPatientsTransferredOutFromOtherHealthFacilitiesDuringCurrentMonthB5() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("B5");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.getPatientsTransferredFromAnotherHealthFacilityB5();
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition getTrfOutB5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "B12",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12()),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.txNewCohortQueries.getTxNewCompositionCohortMISAU("TX_NEW_MISAU"), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.getNumberOfPatientsTransferredInFromOtherHealthFacilitiesDuringCurrentMonthB2(),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.addSearch(
        "TRFOUT",
        EptsReportUtils.map(
            this.getNumberOfPatientsTransferredOutFromOtherHealthFacilitiesDuringCurrentMonthB5(),
            mappings));

    definition.setCompositionString("(B12 OR B1 OR B2 OR B3) AND TRFOUT ");

    return definition;
  }

  /**
   * B.5: Number of patients transferred-out from another HFs during the current month
   *
   * @return Cohort
   * @return CohortDefinition
   */
  @DocumentedDefinition(value = "B6")
  public CohortDefinition getPatientsWhoSuspendTratmentB6() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Suspend Tratment B6");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.getPatientsWhoSuspendTratmentB6();
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition getSuspendB6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "B12",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12()),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.txNewCohortQueries.getTxNewCompositionCohortMISAU("TX_NEW_MISAU"), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.getNumberOfPatientsTransferredInFromOtherHealthFacilitiesDuringCurrentMonthB2(),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.addSearch(
        "SUSPEND", EptsReportUtils.map(this.getPatientsWhoSuspendTratmentB6(), mappings));

    definition.setCompositionString("(B12 OR B1 OR B2 OR B3) AND SUSPEND ");

    return definition;
  }

  @DocumentedDefinition(value = "getPatientsWhoAreAbandoned")
  public CohortDefinition getPatientsWhoAreAbandoned() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("patients who abandoned tratment B7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ABANDONED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ABANDONED", ResumoMensalQueries.getPatientsWhoAbandonedTratmentB7()),
            mappings));

    return definition;
  }

  @DocumentedDefinition(value = "B7")
  public CohortDefinition getPatientsWhoAbandonedTratmentUpB7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("patients who abandoned tratment B7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "B12",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12()),
            mappings));

    definition.addSearch(
        "ABANDONED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ABANDONED", ResumoMensalQueries.getPatientsWhoAbandonedTratmentB7()),
            mappings));

    definition.addSearch(
        "EXCLUSION",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "EXCLUSION",
                ResumoMensalQueries.getPatientsWhoSuspendAndDiedAndTransferedOutTratment()),
            mappings));

    definition.setCompositionString("(B12 AND ABANDONED) NOT (EXCLUSION)");

    return definition;
  }

  @DocumentedDefinition(value = "B8")
  public CohortDefinition getPatientsWhoDiedTratmentB8() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Patient Died B8");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.getPatientsWhoDiedTratmentB8();
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition getDiedB8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "B12",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12()),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.txNewCohortQueries.getTxNewCompositionCohortMISAU("TX_NEW_MISAU"), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            this.getNumberOfPatientsTransferredInFromOtherHealthFacilitiesDuringCurrentMonthB2(),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.addSearch(
        "DIED", EptsReportUtils.map(this.getPatientsWhoDiedTratmentB8(), mappings));

    definition.setCompositionString("(B12 OR B1 OR B2 OR B3) AND DIED ");

    return definition;
  }

  @DocumentedDefinition(value = "B9")
  public CohortDefinition getSumPatientsB9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    definition.setName("Sum B9");

    definition.addSearch(
        "B5",
        EptsReportUtils.map(
            getNumberOfPatientsTransferredOutFromOtherHealthFacilitiesDuringCurrentMonthB5(),
            mappings));

    definition.addSearch("B6", EptsReportUtils.map(getPatientsWhoSuspendTratmentB6(), mappings));

    definition.addSearch(
        "B7", EptsReportUtils.map(this.getPatientsWhoAbandonedTratmentUpB7(), mappings));

    definition.addSearch("B8", EptsReportUtils.map(getPatientsWhoDiedTratmentB8(), mappings));

    definition.setCompositionString("B5 OR B6 OR B7 OR B8 ");

    return definition;
  }

  @DocumentedDefinition(value = "B10")
  public CohortDefinition getTxNewEndDateB10() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Tx New End Date");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate-1d},location=${location}";

    definition.addSearch(
        "START-ART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWhoAreNewlyEnrolledOnART",
                ResumoMensalQueries.findPatientsWhoAreNewlyEnrolledOnARTB10),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWithAProgramStateMarkedAsTransferedInInAPeriod",
                ResumoMensalQueries.findPatientsWithAProgramStateMarkedAsTransferedInInAPeriod),
            mappings));

    definition.addSearch(
        "TRANSFERED-IN-AND-IN-ART-MASTER-CARD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCard",
                ResumoMensalQueries
                    .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCard),
            mappings));

    definition.setCompositionString(
        "START-ART NOT (TRANSFERED-IN OR TRANSFERED-IN-AND-IN-ART-MASTER-CARD)");

    return definition;
  }

  @DocumentedDefinition(value = "B11")
  public CohortDefinition getSumPatients11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1(), mappings));

    definition.addSearch("B10", EptsReportUtils.map(getTxNewEndDateB10(), mappings));

    definition.setCompositionString("B1 OR B10");

    return definition;
  }

  @DocumentedDefinition(value = "B12")
  public CohortDefinition findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Tx Curr B12");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "B13")
  public CohortDefinition findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Tx Curr B13");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "C1")
  public CohortDefinition findPatientWhoHaveTbSymthomsC1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("C1");

    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2(), mappings));

    definition.addSearch(
        "C1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "C1", ResumoMensalQueries.findPatientWhoHaveTbSymthomsC1()),
            mappings));

    definition.setCompositionString("A2 AND C1");

    return definition;
  }

  @DocumentedDefinition(value = "C2")
  public CohortDefinition getPatientsWhoMarkedINHC2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("C2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = ResumoMensalQueries.getPatientsWhoMarkedINHC2();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "C3")
  private CohortDefinition getPatientsWhoMarkedTbActiveC3() {

    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "C3", Context.getRegisteredComponents(ResumoMensalTBCalculation.class).get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }

  public CohortDefinition getPatientsWhoMarkedINHC2A2() {

    String mappingTPI = "startDate=${startDate},endDate=${endDate},location=${location}";

    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("INH");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    cd.addSearch("C2", EptsReportUtils.map(this.getPatientsWhoMarkedINHC2(), mappingTPI));

    cd.addSearch(
        "TOBEEXCLUDE",
        map(
            genericCohortQueries.generalSql(
                "TOBEEXCLUDE", ResumoMensalQueries.getPatientsWhoMarkedINHC2ToBeExclude()),
            mappingTPI));

    cd.addSearch(
        "TRASFERED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWhoInitiatedPreTarvByEndOfPreviousMonthA2",
                ResumoMensalQueries
                    .getPatientsTransferredFromAnotherHealthFacilityDuringTheCurrentStartDateEndDate()),
            mappingTPI));

    cd.setCompositionString("C2 NOT (TOBEEXCLUDE OR TRASFERED)");
    return cd;
  }

  public CohortDefinition getPatientsWhoMarkedTbActiveC3A2() {

    String mapping = "startDate=${startDate-1m},endDate=${endDate},location=${location}";
    String mappingTB = "startDate=${startDate},endDate=${endDate},location=${location}";

    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TB Active");

    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    cd.addSearch(
        "A2",
        EptsReportUtils.map(
            this.getPatientsWhoInitiatedPreTarvAtAfacilityDuringCurrentMonthA2(), mapping));

    cd.addSearch("C3", EptsReportUtils.map(this.getPatientsWhoMarkedTbActiveC3(), mappingTB));

    cd.addSearch(
        "TOBEEXCLUDE",
        map(
            genericCohortQueries.generalSql(
                "TOBEEXCLUDE", ResumoMensalQueries.getPatientsWhoMarkedTbActiveC3ToBeExclude()),
            mappingTB));

    cd.setCompositionString("(A2 AND C3)  NOT(TOBEEXCLUDE)");
    return cd;
  }

  public CohortDefinition findPatientsWhoAreCurrentlyEnrolledOnArtMOHWithRequestForVLE1() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Tx Curr E1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    definition.addSearch(
        "B13", EptsReportUtils.map(findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(), mappings));

    definition.addSearch(
        "VL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "VL",
                ResumoMensalQueries.findPatietWithRequestForVL(
                    hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                    hivMetadata.getApplicationForLaboratoryResearch().getConceptId(),
                    hivMetadata.getHivViralLoadConcept().getConceptId())),
            mappings));

    definition.addSearch(
        "E1x",
        map(
            genericCohortQueries.generalSql(
                "E1x",
                ResumoMensalQueries.getE1ExclusionCriteria(
                    hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                    hivMetadata.getApplicationForLaboratoryResearch().getConceptId(),
                    hivMetadata.getHivViralLoadConcept().getConceptId())),
            mappings));

    definition.setCompositionString("(B13 AND VL) NOT E1x");

    return definition;
  }

  public CohortDefinition findPatientsWhoAreCurrentlyEnrolledOnArtMOHWithVLResultE2() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Tx Curr Vl");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    definition.addSearch(
        "B13", EptsReportUtils.map(findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(), mappings));

    definition.addSearch(
        "VL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "VL", ResumoMensalQueries.findPatientWithVlResult()),
            mappings));

    definition.addSearch(
        "Ex2",
        map(
            genericCohortQueries.generalSql("Ex2", ResumoMensalQueries.getE2ExclusionCriteria()),
            mappings));

    definition.setCompositionString("(B13 AND VL) NOT Ex2");

    return definition;
  }

  public CohortDefinition findPatientWithVlResulLessThan1000E3() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Tx Curr Vl");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    definition.addSearch(
        "B13", EptsReportUtils.map(findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(), mappings));

    definition.addSearch(
        "VL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "VL", ResumoMensalQueries.findPatientWithVlResulLessThan1000()),
            mappings));

    definition.addSearch(
        "Ex3",
        map(
            genericCohortQueries.generalSql("Ex3", ResumoMensalQueries.getE3ExclusionCriteria()),
            mappings));

    definition.setCompositionString("(B13 AND VL) NOT Ex3");

    return definition;
  }

  @DocumentedDefinition(value = "F3")
  public CohortDefinition findPatientWhoHaveTbSymthomsAndTbActive() {

    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "F3", Context.getRegisteredComponents(ResumoMensalTbExclusionCalculation.class).get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }

  @DocumentedDefinition(value = "F1")
  public SqlEncounterQuery getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthF1() {

    SqlEncounterQuery cd = new SqlEncounterQuery();
    cd.setName("F1: Number of patients who had clinical appointment during the reporting month");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
    cd.setQuery(
        ResumoMensalQueries
            .getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthF1());
    return cd;
  }

  @DocumentedDefinition(value = "F2")
  public SqlEncounterQuery
      getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthTbF2() {
    SqlEncounterQuery cd = new SqlEncounterQuery();
    cd.setName("F1: Number of patients who had clinical appointment during the reporting month");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
    cd.setQuery(ResumoMensalQueries.findPatientWhoHaveTbSymthomsF2());
    return cd;
  }

  /**
   * F3: Number of patients who had at least one clinical appointment during the year
   *
   * @return CohortDefinition
   */
  public CohortDefinition getNumberOfPatientsWithAtLeastOneClinicalAppointmentDuringTheYearF3() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Number of patients who had at least one clinical appointment during the year");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    cd.addSearch(
        "F3",
        map(
            genericCohortQueries.generalSql(
                "F3", ResumoMensalQueries.getNumberOfPatientsWhoHadClinicalAppointmentF1()),
            mappings));

    cd.addSearch(
        "Fx3",
        map(
            genericCohortQueries.generalSql(
                "Fx3",
                ResumoMensalQueries.getF3Exclusion(
                    hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId())),
            mappings));

    cd.addSearch(
        "Fx4",
        map(
            genericCohortQueries.generalSql(
                "Fx4", ResumoMensalQueries.getF3ExclusionTransferedIn()),
            mappings));

    cd.setCompositionString("F3 NOT(Fx3 OR Fx4)");
    return cd;
  }

  public TbMetadata getTbMetadata() {
    return tbMetadata;
  }

  public void setTbMetadata(TbMetadata tbMetadata) {
    this.tbMetadata = tbMetadata;
  }
}
