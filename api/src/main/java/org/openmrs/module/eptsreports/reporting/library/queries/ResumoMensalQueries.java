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
package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public class ResumoMensalQueries {

  private static final String FIND_PRE_TARV_PATIENT_A1 = "RM/FIND_PRE_TARV_PATIENT_A1.sql";
  private static final String FIND_PRE_TARV_PATIENT_A2 = "RM/FIND_PRE_TARV_PATIENT_A2.sql";

  /**
   * All patients with encounter type 53, and Pre-ART Start Date that is less than startDate
   *
   * @return String
   */
  public static String getAllPatientsWithPreArtStartDateLessThanReportingStartDateA1() {
    String query = EptsQuerysUtils.loadQuery(FIND_PRE_TARV_PATIENT_A1);
    return query;
  }

  /**
   * All patients with encounter type 53, and Pre-ART Start Date that falls between startDate and
   * enddate
   *
   * @return String
   */
  public static String getAllPatientsWithPreArtStartDateWithBoundariesA2() {
    String query = EptsQuerysUtils.loadQuery(FIND_PRE_TARV_PATIENT_A2);
    return query;
  }

  /**
   * Number of patients transferred-in from another HFs during the current month
   *
   * @return String
   */
  public static String getPatientsTransferredFromAnotherHealthFacilityDuringTheCurrentMonth() {

    String query =
        "SELECT trasferedPatients.patient_id FROM ( "
            + "select minState.patient_id,minState.minStateDate as initialDate from( "
            + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate FROM patient p  "
            + "inner join patient_program pg on p.patient_id=pg.patient_id  "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=1 and location_id=:location and ps.start_date<:startDate "
            + "GROUP BY pg.patient_program_id "
            + ") minState  "
            + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id  "
            + "where ps.start_date=minState.minStateDate and ps.state=28 and ps.voided=0 "
            + "union "
            + "select minState.patient_id,minState.minStateDate as initialDate from( "
            + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate  FROM patient p  "
            + "inner join patient_program pg on p.patient_id=pg.patient_id  "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  and ps.start_date<:startDate    "
            + "GROUP BY pg.patient_program_id) minState  "
            + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id "
            + "where ps.start_date=minState.minStateDate and ps.state=29 and ps.voided=0 UNION  "
            + "SELECT p.patient_id,MIN(obsData.value_datetime) AS initialDate  FROM patient p "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id "
            + "INNER JOIN obs obsPretarv on e.encounter_id=obsPretarv.encounter_id  "
            + "INNER JOIN obs obsData on e.encounter_id=obsData.encounter_id "
            + "WHERE e.voided=0 AND o.voided=0  AND e.encounter_type=53  AND obsPretarv.voided=0  AND obsPretarv.concept_id=6300  AND obsPretarv.value_coded in(6275,6276) AND obsData.concept_id=23891 "
            + "AND obsData.voided=0 AND e.location_id=:location   AND o.concept_id=1369  AND o.value_coded=1065  AND obsData.value_datetime< :startDate GROUP BY p.patient_id)trasferedPatients  GROUP BY trasferedPatients.patient_id ";

    return query;
  }

  public static String
      getPatientsTransferredFromAnotherHealthFacilityDuringTheCurrentStartDateEndDate() {

    String query =
        "SELECT trasferedPatients.patient_id FROM ( "
            + "select minState.patient_id,minState.minStateDate as initialDate from( "
            + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate FROM patient p  "
            + "inner join patient_program pg on p.patient_id=pg.patient_id  "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=1 and location_id=:location and ps.start_date<:endDate "
            + "GROUP BY pg.patient_program_id "
            + ") minState  "
            + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id  "
            + "where ps.start_date=minState.minStateDate and ps.state=28 and ps.voided=0 "
            + "union "
            + "select minState.patient_id,minState.minStateDate as initialDate from ( "
            + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate  FROM patient p  "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  and ps.start_date<:endDate "
            + "GROUP BY pg.patient_program_id) minState  "
            + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id "
            + "where ps.start_date=minState.minStateDate and ps.state=29 and ps.voided=0 "
            + "UNION  "
            + "SELECT p.patient_id,MIN(obsData.value_datetime) AS initialDate  FROM patient p "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id "
            + "INNER JOIN obs obsPretarv on e.encounter_id=obsPretarv.encounter_id  "
            + "INNER JOIN obs obsData on e.encounter_id=obsData.encounter_id "
            + "WHERE e.voided=0 AND o.voided=0  AND e.encounter_type=53  AND obsPretarv.voided=0  AND obsPretarv.concept_id=6300  AND obsPretarv.value_coded in(6275,6276) AND obsData.concept_id=23891 "
            + "AND obsData.voided=0 AND e.location_id=:location   AND o.concept_id=1369  AND o.value_coded=1065  AND obsData.value_datetime< :endDate GROUP BY p.patient_id)trasferedPatients  GROUP BY trasferedPatients.patient_id ";

    return query;
  }

  public static String getPatientsTransferredFromAnotherHealthFacilityB5() {
    String query =
        "select transferidopara.patient_id from ( "
            + "select patient_id,max(data_transferidopara) data_transferidopara from ( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara from ( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara "
            + "from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location group by p.patient_id "
            + ") maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 "
            + "and  e.location_id=:location group by p.patient_id "
            + "union "
            + "select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and "
            + "o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and  e.location_id=:location group by p.patient_id "
            + ") transferido group by patient_id "
            + ") transferidopara "
            + "inner join( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from "
            + "( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + ") consultaLev group by patient_id "
            + ") consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara and transferidopara.data_transferidopara between :startDate AND :endDate ";
    return query;
  }

  public static String getPatientsWhoSuspendTratmentB6() {

    String query =
        "select suspenso1.patient_id from ( "
            + "select patient_id,max(data_suspencao) data_suspencao from ( "
            + "select maxEstado.patient_id,maxEstado.data_suspencao from( "
            + "select pg.patient_id,max(ps.start_date) data_suspencao from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id )maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 "
            + "union "
            + " select p.patient_id,max(o.obs_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 "
            + "and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union "
            + "select  p.patient_id,max(e.encounter_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 "
            + "and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location group by p.patient_id ) suspenso group by patient_id) suspenso1 "
            + "inner join ( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and  "
            + "e.location_id=:location and e.encounter_type=18 group by p.patient_id "
            + "union "
            + "Select  p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where  p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 "
            + "and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id"
            + ") consultaLev group by patient_id) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao and suspenso1.data_suspencao between :startDate  AND :endDate ";

    return query;
  }

  public static String getPatientsWhoSuspendTratmentLastMonth() {

    String query =
        "select suspenso1.patient_id from  "
            + "( "
            + "select patient_id,max(data_suspencao) data_suspencao from ( "
            + "select maxEstado.patient_id,maxEstado.data_suspencao from "
            + "( "
            + "select pg.patient_id,max(ps.start_date) data_suspencao from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=(:endDate -interval 1 month) "
            + "and pg.location_id=:location "
            + "group by p.patient_id  "
            + ")maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=(:endDate -interval 1 month) "
            + "and o.voided=0 and o.concept_id=6272 "
            + "and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select  p.patient_id,max(e.encounter_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and p.voided=0 and e.encounter_datetime<=(:endDate -interval 1 month) "
            + "and o.voided=0 and o.concept_id=6273 "
            + "and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location "
            + "group by p.patient_id "
            + ") suspenso "
            + " group by patient_id "
            + ") suspenso1 "
            + "inner join "
            + "( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where p.voided=0 and e.voided=0 "
            + "and e.encounter_datetime<=(:endDate -interval 1 month) and  "
            + "e.location_id=:location and e.encounter_type=18 "
            + "group by p.patient_id "
            + "union "
            + "Select  p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where  p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 "
            + "and o.value_datetime is not null and o.value_datetime<=(:endDate -interval 1 month) "
            + "and e.location_id=:location "
            + "group by p.patient_id"
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao "
            + "and suspenso1.data_suspencao <=(:endDate -interval 1 month) ";

    return query;
  }

  public static String getPatientsWhoDiedTratmentLastMonth() {

    String query =
        "select obito.patient_id from ( "
            + "select patient_id,max(data_obito) data_obito from"
            + "( "
            + "select maxEstado.patient_id,maxEstado.data_obito from "
            + "( "
            + "select pg.patient_id,max(ps.start_date) data_obito from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=(:endDate -interval 1 month) "
            + " and pg.location_id=:location "
            + "group by p.patient_id  "
            + ") maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_obito from	patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=(:endDate -interval 1 month) "
            + "and o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "select p.patient_id,max(e.encounter_datetime) data_obito from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 "
            + "and e.encounter_datetime<=(:endDate -interval 1 month) "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "Select person_id,death_date from person p where p.dead=1 "
            + "and p.death_date<=(:endDate -interval 1 month) "
            + ")transferido "
            + "group by patient_id "
            + ") obito "
            + "inner join "
            + "( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from"
            + "( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=(:endDate -interval 1 month) "
            + "and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(value_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=(:endDate -interval 1 month) "
            + "and e.location_id=:location "
            + "group by p.patient_id  "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on obito.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=obito.data_obito "
            + "and obito.data_obito <=(:endDate -interval 1 month) ";

    return query;
  }

  public static String getPatientsTransferredFromAnotherHealthFacilityLastMonth() {
    String query =
        "select transferidopara.patient_id from ( "
            + "select patient_id,max(data_transferidopara) data_transferidopara from "
            + "( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara from "
            + "( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara "
            + "from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=(:endDate -interval 1 month) "
            + " and pg.location_id=:location "
            + "group by p.patient_id "
            + ") maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=(:endDate -interval 1 month) "
            + " and o.voided=0 and o.concept_id=6272 "
            + "and o.value_coded=1706 and e.encounter_type=53 "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and p.voided=0 and e.encounter_datetime<=(:endDate -interval 1 month) "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + ") transferido "
            + "group by patient_id "
            + ") transferidopara "
            + "inner join( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from "
            + "( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 "
            + "and e.encounter_datetime<=(:endDate -interval 1 month) "
            + "and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "Select p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 "
            + "and o.concept_id=23866 and o.value_datetime is not null "
            + "and o.value_datetime<=(:endDate -interval 1 month) "
            + "and e.location_id=:location group by p.patient_id "
            + ") consultaLev group by patient_id "
            + ") consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara "
            + "and transferidopara.data_transferidopara <= (:endDate -interval 1 month) ";
    return query;
  }

  public static String getPatientsWhoHaveDrugPickup() {

    String query =
        "select pickup.patient_id from ( "
            + "Select p.patient_id,value_datetime data_recepcao_levantou from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and  "
            + "o.value_datetime BETWEEN :startDate AND :endDate and e.location_id= :location  "
            + "union "
            + "Select p.patient_id,encounter_datetime data_fila from patient p  "
            + "inner join encounter e on e.patient_id=p.patient_id  "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=18 and  "
            + "e.location_id= :location and e.encounter_datetime BETWEEN :startDate AND :endDate "
            + ")pickup ";

    return query;
  }

  public static String getPatientsWhoAbandonedTratmentB7() {
    String query =
        "Select B7.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 60 day) data_proximo_levantamento60 "
            + "from(select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento "
            + "from patient p inner "
            + "join encounter e on p.patient_id = e.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "inner join obs obsLevantou on obsLevantou.encounter_id= e.encounter_id "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :endDate and o.voided = 0 and o.concept_id = 23866 and obsLevantou.concept_id=23865 and obsLevantou.value_coded=1065 and obsLevantou.voided=0 and e.encounter_type=52 and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from ( "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime <=:endDate and e.location_id=:location and e.voided=0 and p.voided=0 "
            + "group by p.patient_id)fila "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime "
            + ") maxFilaRecepcao "
            + "group by patient_id "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 60 day )< :endDate  "
            + ")B7 ";

    return query;
  }

  public static String getPatientsWhoAbandonedTratmentToBeExclude() {
    String query =
        "Select B7.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 60 day) data_proximo_levantamento60 "
            + "from( "
            + "select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento  "
            + "from patient p inner  "
            + "join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "inner join obs obsLevantou on obsLevantou.encounter_id= e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= (:endDate -interval 1 month) and o.voided = 0 and o.concept_id = 23866  "
            + "and obsLevantou.concept_id=23865  "
            + "and obsLevantou.value_coded=1065 and obsLevantou.voided=0 and e.encounter_type=52 and e.location_id=:location  "
            + "group by p.patient_id "
            + "union  "
            + "select fila.patient_id, fila.data_levantamento,proximoLevantamento.data_proximo_levantamento  from   "
            + "(  "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime <=(:endDate -interval 1 month)  "
            + "and e.location_id=:location and e.voided=0 and p.voided=0 "
            + "group by p.patient_id  "
            + ")fila  "
            + "left join "
            + "( "
            + "select fr.patient_id,fr.encounter_datetime data_levantamento,obs_fila.value_datetime data_proximo_levantamento,fr.encounter_id from ( "
            + "Select  p.patient_id,max(encounter_datetime) encounter_datetime, e.encounter_id from patient p   "
            + "inner join encounter e on e.patient_id=p.patient_id  "
            + "where   p.voided=0 and e.voided=0 and e.encounter_type=18 and   "
            + "e.location_id=:location and e.encounter_datetime<=(:endDate -interval 1 month)  "
            + "group by p.patient_id  "
            + ") fr  "
            + "inner join encounter e on e.patient_id=fr.patient_id "
            + "inner join obs obs_fila on obs_fila.encounter_id=e.encounter_id   "
            + "and  obs_fila.concept_id=5096  "
            + "and obs_fila.voided=0 and  e.encounter_type =18 and e.encounter_datetime=fr.encounter_datetime "
            + ") proximoLevantamento on proximoLevantamento.patient_id=fila.patient_id  "
            + ") maxFilaRecepcao  "
            + "group by patient_id  "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 60 day )< (:endDate -interval 1 month)   "
            + ")B7 ";

    return query;
  }

  public static String getPatientsWhoAbandonedTratmentB7Exclusion() {
    String query =
        "Select B7.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 60 day) data_proximo_levantamento60 "
            + "from(select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento "
            + "from patient p inner "
            + "join encounter e on p.patient_id = e.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "inner join obs obsLevantou on obsLevantou.encounter_id= e.encounter_id "
            + "where e.voided = 0 and p.voided = 0 and o.value_datetime < :startDate and o.voided = 0 and o.concept_id = 23866  and obsLevantou.concept_id=23865 and obsLevantou.value_coded=1065 and obsLevantou.voided=0 and e.encounter_type=52 and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from ( "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime < :startDate and e.location_id=:location and e.voided=0 and p.voided=0 "
            + "group by p.patient_id "
            + ")fila "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime "
            + ") maxFilaRecepcao "
            + "group by patient_id "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 60 day )< (:startDate - INTERVAL 1 day) "
            + ")B7 ";

    return query;
  }

  public static String getPatientsWhoSuspendAndDiedAndTransferedOutTratmentB7ExclusionEndDate() {
    String query =
        "select saida.patient_id from (select patient_id,max(data_estado) data_estado from ( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara data_estado from ( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara from  patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id ) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state in (7,8,10) "
            + "union "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and o.obs_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select person_id as patient_id,death_date as data_estado from person where dead=1 and death_date is not null and death_date<=:endDate "
            + ") allSaida "
            + "group by patient_id "
            + ") saida "
            + "inner join( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) group by p.patient_id "
            + "union Select p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on saida.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=saida.data_estado and saida.data_estado<=:endDate ";

    return query;
  }

  public static String getPatientsWhoSuspendAndDiedAndTransferedOutTratment() {
    String query =
        "select saida.patient_id from (select patient_id,max(data_estado) data_estado from ( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara data_estado from ( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara from  patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id ) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state in (7,8,10) "
            + "union "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and o.obs_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select person_id as patient_id,death_date as data_estado from person where dead=1 and death_date is not null and death_date<=:endDate "
            + ") allSaida "
            + "group by patient_id "
            + ") saida "
            + "inner join( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) group by p.patient_id "
            + "union Select p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on saida.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=saida.data_estado and saida.data_estado between :startDate AND :endDate ";

    return query;
  }

  public static String getPatientsWhoDiedTratmentB8() {

    String query =
        "select obito.patient_id from ( "
            + "select patient_id,max(data_obito) data_obito from ( "
            + "select maxEstado.patient_id,maxEstado.data_obito from ( "
            + "select pg.patient_id,max(ps.start_date) data_obito from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id ) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_obito from	patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and "
            + "o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union  "
            + "select p.patient_id,max(e.encounter_datetime) data_obito from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "Select person_id,death_date from person p where p.dead=1 and p.death_date<=:endDate )transferido "
            + "group by patient_id) obito "
            + "inner join ( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(value_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id ) consultaLev "
            + "group by patient_id ) "
            + "consultaOuARV on obito.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=obito.data_obito and obito.data_obito between :startDate AND :endDate ";

    return query;
  }

  public static final String findPatientsWithAProgramStateMarkedAsTransferedInInAPeriod =
      "select minState.patient_id from ( "
          + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate  FROM patient p  "
          + "inner join patient_program pg on p.patient_id=pg.patient_id  "
          + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
          + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  and ps.start_date<=:startDate "
          + "GROUP BY pg.patient_program_id) minState  "
          + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id  "
          + "where ps.start_date=minState.minStateDate and ps.state=29 and ps.voided=0 and ps.start_date <=:startDate ";

  public static final String
      findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCard =
          "SELECT tr.patient_id from  ("
              + "SELECT p.patient_id, MIN(obsData.value_datetime) from patient p  "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id  "
              + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
              + "INNER JOIN obs obsTarv ON e.encounter_id=obsTarv.encounter_id AND obsTarv.voided=0 AND obsTarv.concept_id=6300 AND obsTarv.value_coded=6276 "
              + "INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 "
              + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime <= :startDate AND e.location_id=:location GROUP BY p.patient_id "
              + ") tr GROUP BY tr.patient_id ";

  public static final String findPatientsWhoAreNewlyEnrolledOnARTB10 =
      "SELECT patient_id FROM "
          + "(SELECT patient_id, MIN(art_start_date) art_start_date FROM "
          + "( "
          + "SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM patient p "
          + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
          + "INNER JOIN obs o ON o.encounter_id=e.encounter_id "
          + "WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) "
          + "AND e.encounter_datetime<=:startDate AND e.location_id=:location "
          + "GROUP BY p.patient_id "
          + "UNION "
          + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
          + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
          + "INNER JOIN obs o ON e.encounter_id=o.encounter_id "
          + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 "
          + "AND o.concept_id=23866 AND o.value_datetime is NOT NULL AND o.value_datetime<=:startDate "
          + "AND e.location_id=:location "
          + "GROUP BY p.patient_id "
          + ") "
          + "art_start GROUP BY patient_id "
          + ") tx_new WHERE art_start_date <=:startDate  ";

  public static String findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12() {

    String query =
        "select patient_id from ( "
            + "select  inicio_fila_seg_prox.*,GREATEST(COALESCE(data_fila,data_seguimento),COALESCE(data_seguimento,data_fila),COALESCE(data_seguimento,data_fila))  data_usar_c, "
            + "GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar from (select inicio_fila_seg.*, "
            + "max(obs_fila.value_datetime) data_proximo_lev, "
            + "max(obs_seguimento.value_datetime) data_proximo_seguimento, "
            + "date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30 from "
            + "( "
            + "select inicio.*,saida.data_estado,max_fila.data_fila,max_consulta.data_seguimento, max_recepcao.data_recepcao_levantou from "
            + "( "
            + "Select patient_id,min(data_inicio) data_inicio from "
            + "( "
            + "SELECT e.patient_id, MIN(e.encounter_datetime) AS data_inicio  FROM  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "WHERE p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<:startDate and e.location_id=:location "
            + "GROUP BY p.patient_id "
            + "union Select p.patient_id,min(value_datetime) data_inicio from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<:startDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") inicio_real group by patient_id "
            + ")inicio "
            + "left join ( "
            + "select patient_id,max(data_estado) data_estado from ( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara data_estado from ( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<:startDate and pg.location_id=:location group by p.patient_id) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state in (7,8,10) "
            + "union "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and o.obs_datetime<:startDate and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select person_id as patient_id,death_date as data_estado from person  "
            + "where dead=1 and death_date is not null and death_date<:startDate "
            + ") allSaida "
            + "group by patient_id "
            + ") saida on inicio.patient_id=saida.patient_id "
            + "left join "
            + "( "
            + "Select p.patient_id,max(encounter_datetime) data_fila from patient p  "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<:startDate "
            + "group by p.patient_id "
            + ") max_fila on inicio.patient_id=max_fila.patient_id   "
            + "left join "
            + "(Select p.patient_id,max(encounter_datetime) data_seguimento from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<:startDate group by p.patient_id "
            + ") max_consulta on inicio.patient_id=max_consulta.patient_id "
            + "left join "
            + "( "
            + "Select p.patient_id,max(value_datetime) data_recepcao_levantou from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<:startDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") max_recepcao on inicio.patient_id=max_recepcao.patient_id "
            + "group by inicio.patient_id "
            + ") inicio_fila_seg "
            + "left join obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id and obs_fila.voided=0 and obs_fila.obs_datetime=inicio_fila_seg.data_fila and obs_fila.concept_id=5096 and obs_fila.location_id=:location "
            + "left join obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id and obs_seguimento.voided=0 and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento and obs_seguimento.concept_id=1410 and obs_seguimento.location_id=:location "
            + "group by inicio_fila_seg.patient_id) inicio_fila_seg_prox "
            + "group by patient_id "
            + ") coorte12meses_final where (data_estado is null or (data_estado is not null and  data_usar_c>data_estado)) and date_add(data_usar, interval 60 day) >=(:startDate - interval 1 day)";

    return query;
  }

  public static String findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13() {
    return "select patient_id                                                                                                                           "
        + "from(                                                                                                                                    "
        + " select inicio_fila_seg_prox.*,                                                                                                          "
        + "     GREATEST(COALESCE(data_fila,data_seguimento),                                                                "
        + "     COALESCE(data_seguimento,data_fila),                                                                         "
        + "     COALESCE(data_seguimento,data_fila)) data_usar_c,                                                            "
        + "     GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar         "
        + " from    (                                                                                                                               "
        + "     select inicio_fila_seg.*,                                                                                                           "
        + "         max(obs_fila.value_datetime) data_proximo_lev,                                                                                  "
        + "             max(obs_seguimento.value_datetime) data_proximo_seguimento,                                                                 "
        + "             date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30                                                  "
        + "     from (                                                                                                                              "
        + "         select inicio.*,                                                                                                                "
        + "             saida.data_estado,                                                                                                          "
        + "             max_fila.data_fila,                                                                                                         "
        + "             max_consulta.data_seguimento,                                                                                               "
        + "             max_recepcao.data_recepcao_levantou                                                                                         "
        + "         from (                                                                                                                          "
        + "                 Select patient_id, min(data_inicio) data_inicio                                                                         "
        + "                 from (                                                                                                                  "
        + "                     select e.patient_id, min(e.encounter_datetime) as data_inicio                                                       "
        + "                     from patient p                                                                                                      "
        + "                         inner join encounter e on p.patient_id=e.patient_id                                                             "
        + "                     where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location   "
        + "                         group by p.patient_id                                                                                           "
        + "                                                                                                                                         "
        + "                     union                                                                                                               "
        + "                                                                                                                                         "
        + "                     Select p.patient_id,min(value_datetime) data_inicio                                                                 "
        + "                     from patient p                                                                                                      "
        + "                         inner join encounter e on p.patient_id=e.patient_id                                                             "
        + "                         inner join obs o on e.encounter_id=o.encounter_id                                                               "
        + "                     where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866                      "
        + "                         and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location                    "
        + "                         group by p.patient_id                                                                                           "
        + "                     )                                                                                                                   "
        + "                     inicio_real group by patient_id                                                                                     "
        + "                 )inicio                                                                                                                 "
        + "                 left join                                                                                                               "
        + "             (                                                                                                                           "
        + "                     select patient_id,max(data_estado) data_estado                                                                      "
        + "                     from (                                                                                                              "
        + "                         select maxEstado.patient_id,maxEstado.data_transferidopara data_estado                                          "
        + "                         from(                                                                                                           "
        + "                             select pg.patient_id,max(ps.start_date) data_transferidopara                                                "
        + "                             from patient p                                                                                              "
        + "                                 inner join patient_program pg on p.patient_id=pg.patient_id                                             "
        + "                                 inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                              "
        + "                             where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location   "
        + "                                 group by p.patient_id                                                                                   "
        + "                             )                                                                                                           "
        + "                         maxEstado                                                                                                       "
        + "                             inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id                                       "
        + "                             inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id                               "
        + "                         where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2                                                        "
        + "                             and ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state in (7,8,10)   "
        + "                                                                                                                                         "
        + "                         union                                                                                                           "
        + "                                                                                                                                         "
        + "                         select p.patient_id, max(o.obs_datetime) data_estado                                                            "
        + "                         from patient p                                                                                                  "
        + "                             inner join encounter e on p.patient_id=e.patient_id                                                         "
        + "                             inner join obs  o on e.encounter_id=o.encounter_id                                                          "
        + "                         where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273)  "
        + "                             and o.value_coded in (1706,1366,1709) and o.obs_datetime<=:endDate and e.location_id=:location              "
        + "                             group by p.patient_id                                                                                       "
        + "                                                                                                                                         "
        + "                         union                                                                                                           "
        + "                                                                                                                                         "
        + "                         select person_id as patient_id,death_date as data_estado                                                        "
        + "                         from person                                                                                                     "
        + "                         where dead=1 and death_date is not null and death_date<=:endDate                                                "
        + "                         )                                                                                                               "
        + "                         allSaida                                                                                                        "
        + "                             group by patient_id                                                                                         "
        + "             )                                                                                                                           "
        + "                 saida on inicio.patient_id=saida.patient_id                                                                             "
        + "                                                                                                                                         "
        + "                 left join                                                                                                               "
        + "                 (                                                                                                                       "
        + "                     Select p.patient_id,max(encounter_datetime) data_fila                                                               "
        + "                     from patient p                                                                                                      "
        + "                         inner join encounter e on e.patient_id=p.patient_id                                                             "
        + "                     where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate   "
        + "                         group by p.patient_id                                                                                           "
        + "                 )                                                                                                                       "
        + "                 max_fila on inicio.patient_id=max_fila.patient_id                                                                       "
        + "                                                                                                                                         "
        + "                 left join                                                                                                               "
        + "                 (                                                                                                                       "
        + "                     Select p.patient_id,max(encounter_datetime) data_seguimento                                                         "
        + "                     from patient p                                                                                                      "
        + "                         inner join encounter e on e.patient_id=p.patient_id                                                             "
        + "                     where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=:endDate   "
        + "                         group by p.patient_id                                                                                           "
        + "                 )                                                                                                                       "
        + "                 max_consulta on inicio.patient_id=max_consulta.patient_id                                                               "
        + "                                                                                                                                         "
        + "                 left join                                                                                                               "
        + "                 (                                                                                                                       "
        + "                     Select p.patient_id,max(value_datetime) data_recepcao_levantou                                                      "
        + "                     from patient p                                                                                                      "
        + "                         inner join encounter e on p.patient_id=e.patient_id                                                             "
        + "                         inner join obs o on e.encounter_id=o.encounter_id                                                               "
        + "                     where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866                      "
        + "                         and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location                    "
        + "                         group by p.patient_id                                                                                           "
        + "                 )                                                                                                                       "
        + "                 max_recepcao on inicio.patient_id=max_recepcao.patient_id                                                               "
        + "                     group by inicio.patient_id                                                                                          "
        + "     ) inicio_fila_seg                                                                                                                   "
        + "             left join obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id                                                     "
        + "                 and obs_fila.voided=0                                                                                                   "
        + "                 and obs_fila.obs_datetime=inicio_fila_seg.data_fila                                                                     "
        + "                 and obs_fila.concept_id=5096                                                                                            "
        + "                 and obs_fila.location_id=:location                                                                                      "
        + "             left join obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id                                         "
        + "                 and obs_seguimento.voided=0                                                                                             "
        + "                 and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento                                                         "
        + "                 and obs_seguimento.concept_id=1410                                                                                      "
        + "                 and obs_seguimento.location_id=:location                                                                                "
        + "                 group by inicio_fila_seg.patient_id                                                                                     "
        + " ) inicio_fila_seg_prox                                                                                                                  "
        + "     group by patient_id                                                                                                                 "
        + ") coorte12meses_final                                                                                                                    "
        + " where (data_estado is null or (data_estado is not null and  data_usar_c>data_estado))                                                   "
        + "     and date_add(data_usar, interval 60 day) >=:endDate                                                                                 ";
  }

  public static String findPatientWhoHaveTbSymthomsC1() {

    String query =
        "select tb.patient_id from ( "
            + "select p.patient_id,min(e.encounter_datetime) data_tb from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate "
            + "group by p.patient_id )tb "
            + "inner join obs obstb on obstb.person_id=tb.patient_id "
            + "where obstb.concept_id=23758 and obstb.obs_datetime=tb.data_tb and obstb.voided=0 ";

    return query;
  }

  public static String getPatientsWhoMarkedINHC2() {
    String query =
        "select INH_3HP.patient_id,INH_3HP.dataInicioTPI,INH_3HP.value_coded from (  "
            + "select p.patient_id,obsEstado.obs_datetime dataInicioTPI,obsEstado.value_coded    "
            + "from patient p     "
            + "inner join encounter e on p.patient_id = e.patient_id    "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id    "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id  "
            + "where p.voided = 0 and e.voided = 0  and ultimaProfilaxiaIsoniazia.voided = 0 and obsEstado.voided = 0     "
            + "and  ultimaProfilaxiaIsoniazia.concept_id = 23985  and ultimaProfilaxiaIsoniazia.value_coded = 656   "
            + "and obsEstado.concept_id = 165308 and obsEstado.value_coded = 1256    "
            + "and e.encounter_type in (6) and e.location_id=:location   "
            + "union  "
            + "select p.patient_id, obsEstado.obs_datetime dataInicioTPI,obsEstado.value_coded  from patient p    "
            + "inner join encounter e on p.patient_id = e.patient_id    "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id    "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id   "
            + "where   e.encounter_type in(6) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954    "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256    "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location   "
            + ") INH_3HP  "
            + "inner join (  "
            + "SELECT p.patient_id,MIN(o.value_datetime) AS initialDate FROM patient p     "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id    "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id    "
            + "WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53 AND e.location_id= :location   "
            + "AND o.value_datetime IS NOT NULL AND o.concept_id=23808   "
            + "AND o.value_datetime<=:endDate "
            + "GROUP BY p.patient_id    "
            + "UNION    "
            + "SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p     "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id    "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id    "
            + "WHERE e.voided=0 AND o.voided=0 AND e.encounter_type IN(5, 7)    "
            + "AND e.location_id= :location AND e.encounter_datetime<=:endDate "
            + "GROUP BY p.patient_id    "
            + "UNION    "
            + "SELECT pg.patient_id, min(pg.date_enrolled)  AS initialDate FROM patient p    "
            + "INNER JOIN patient_program pg on pg.patient_id=p.patient_id    "
            + "WHERE pg.program_id=1 AND pg.location_id= :location AND pg.voided=0 AND pg.date_enrolled<=:endDate   "
            + "GROUP BY patient_id   "
            + ")preTarv on preTarv.patient_id=INH_3HP.patient_id  "
            + "where INH_3HP.dataInicioTPI>=preTarv.initialDate  "
            + "and preTarv.initialDate  between (:startDate - INTERVAL 1 MONTH) and :endDate "
            + "order by INH_3HP.patient_id, INH_3HP.dataInicioTPI ";

    return query;
  }

  public static String getPatientsWhoMarkedINHC2ToBeExclude() {
    String query =
        "select f.patient_id from (  "
            + "SELECT preTarvFinal.patient_id, preTarvFinal.initialDate FROM  (   "
            + "SELECT preTarv.patient_id, MIN(preTarv.initialDate) initialDate FROM (   "
            + "SELECT p.patient_id,min(o.value_datetime) AS initialDate FROM patient p    "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id   "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id  "
            + "WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53   "
            + "AND e.location_id=:location  AND o.value_datetime IS NOT NULL AND o.concept_id=23808 AND o.value_datetime<=:endDate  "
            + "GROUP BY p.patient_id   "
            + "UNION   "
            + "SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p   "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id   "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id WHERE e.voided=0 AND o.voided=0   "
            + "AND e.encounter_type IN(5,7) AND e.location_id=:location   "
            + "AND e.encounter_datetime<=:endDate "
            + "GROUP BY p.patient_id   "
            + "UNION   "
            + "SELECT pg.patient_id, MIN(pg.date_enrolled) AS initialDate FROM patient p   "
            + "INNER JOIN patient_program pg on pg.patient_id=p.patient_id   "
            + "WHERE pg.program_id=1 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<=:endDate   "
            + "GROUP BY patient_id   "
            + ") preTarv   "
            + "GROUP BY preTarv.patient_id   "
            + ") preTarvFinal   "
            + "inner join (  "
            + "select p.patient_id,obsEstado.obs_datetime dataInicioTPI,obsEstado.value_coded    "
            + "from patient p     "
            + "inner join encounter e on p.patient_id = e.patient_id    "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id    "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id  "
            + "where p.voided = 0 and e.voided = 0  and ultimaProfilaxiaIsoniazia.voided = 0 and obsEstado.voided = 0     "
            + "and  ultimaProfilaxiaIsoniazia.concept_id = 23985  and ultimaProfilaxiaIsoniazia.value_coded = 656   "
            + "and obsEstado.concept_id = 165308 and obsEstado.value_coded = 1256    "
            + "and e.encounter_type in (6) and e.location_id=:location   "
            + "union  "
            + "select p.patient_id, obsEstado.obs_datetime dataInicioTPI,obsEstado.value_coded  from patient p    "
            + "inner join encounter e on p.patient_id = e.patient_id    "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id    "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id   "
            + "where   e.encounter_type in(6) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954    "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256    "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location   "
            + " )tpt on tpt.patient_id=preTarvFinal.patient_id  "
            + "where tpt.dataInicioTPI BETWEEN preTarvFinal.initialDate and :startDate   "
            + ")f  ";

    return query;
  }

  public static String getPatientsWhoMarkedTbActiveC3() {
    String query =
        "SELECT tb.patient_id, tb.encounter_datetime,tb.concept_id FROM  (  "
            + "select encounter.patient_id, encounter.encounter_datetime, obs.concept_id from encounter  "
            + "inner join patient on patient.patient_id = encounter.patient_id  "
            + "left  join obs on (obs.encounter_id = encounter.encounter_id and obs.voided =0 and obs.concept_id = 23761 and obs.value_coded=1065)  "
            + "where encounter.encounter_type = 6 and encounter.voided = 0 and patient.voided = 0  "
            + "and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <=:endDate "
            + "and encounter.location_id =:location  "
            + "order by encounter.patient_id,encounter.encounter_datetime "
            + ") tb  "
            + "inner join  ( "
            + "SELECT p.patient_id,min(o.value_datetime) AS initialDate FROM patient p   "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id  "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53  "
            + "AND e.location_id=:location  AND o.value_datetime IS NOT NULL AND o.concept_id=23808 AND o.value_datetime<=:endDate  "
            + "GROUP BY p.patient_id  "
            + "UNION  "
            + "SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p  "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id  "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id WHERE e.voided=0 AND o.voided=0  "
            + "AND e.encounter_type IN(5,7) AND e.location_id=:location  "
            + "AND e.encounter_datetime<=:endDate GROUP BY p.patient_id  "
            + "UNION "
            + "SELECT pg.patient_id, MIN(pg.date_enrolled) AS initialDate FROM patient p  "
            + "INNER JOIN patient_program pg on pg.patient_id=p.patient_id  "
            + "WHERE pg.program_id=1 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<=:endDate   "
            + "GROUP BY patient_id  "
            + ") preTarv on preTarv.patient_id=tb.patient_id "
            + "where tb.encounter_datetime >=preTarv.initialDate "
            + "and preTarv.initialDate  between (:startDate - INTERVAL 1 MONTH) and :endDate ";

    return query;
  }

  public static String getPatientsWhoMarkedTbActiveC3ToBeExclude() {
    String query =
        "SELECT tb.patient_id FROM  (  "
            + "select encounter.patient_id, encounter.encounter_datetime, obs.concept_id from encounter  "
            + "inner join patient on patient.patient_id = encounter.patient_id  "
            + "left  join obs on (obs.encounter_id = encounter.encounter_id and obs.voided =0 and obs.concept_id = 23761 and obs.value_coded=1065)  "
            + "where encounter.encounter_type = 6 and encounter.voided = 0 and patient.voided = 0  "
            + "and encounter.encounter_datetime <=:startDate "
            + "and encounter.location_id =:location  "
            + "order by encounter.patient_id,encounter.encounter_datetime "
            + ") tb  "
            + "inner join  ( "
            + "SELECT p.patient_id,min(o.value_datetime) AS initialDate FROM patient p   "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id  "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53  "
            + "AND e.location_id=:location  AND o.value_datetime IS NOT NULL AND o.concept_id=23808 AND o.value_datetime<=:endDate  "
            + "GROUP BY p.patient_id  "
            + "UNION  "
            + "SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p  "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id  "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id WHERE e.voided=0 AND o.voided=0  "
            + "AND e.encounter_type IN(5,7) AND e.location_id=:location  "
            + "AND e.encounter_datetime<=:endDate GROUP BY p.patient_id  "
            + "UNION "
            + "SELECT pg.patient_id, MIN(pg.date_enrolled) AS initialDate FROM patient p  "
            + "INNER JOIN patient_program pg on pg.patient_id=p.patient_id  "
            + "WHERE pg.program_id=1 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<=:endDate   "
            + "GROUP BY patient_id  "
            + ") preTarv on preTarv.patient_id=tb.patient_id "
            + "where tb.encounter_datetime >=preTarv.initialDate and tb.encounter_datetime<=:startDate ";

    return query;
  }

  public static String findPatietWithRequestForVL(
      int encounterType, int questionConceptId, int answerConceptId) {

    String query =
        "SELECT pat.patient_id AS patient_id FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided = 0 AND enc.voided = 0 AND ob.voided = 0 AND enc.location_id = :location "
            + " AND enc.encounter_datetime BETWEEN :startDate AND :endDate AND enc.encounter_type=%d AND "
            + " ob.concept_id=%d AND ob.value_coded=%d ";

    return String.format(query, encounterType, questionConceptId, answerConceptId);
  }

  public static String findPatientWithVlResult() {
    String query =
        "SELECT pat.patient_id FROM patient pat "
            + "JOIN encounter enc ON pat.patient_id=enc.patient_id "
            + "JOIN obs ob "
            + " ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided=0 AND enc.voided=0 AND ob.voided=0 AND enc.location_id=:location AND ob.obs_datetime  "
            + " BETWEEN :startDate AND :endDate AND ob.concept_id IN(856,1305) AND enc.encounter_type in(6) ";

    return query;
  }

  public static String findPatientWithVlResulLessThan1000() {
    String query =
        "SELECT pat.patient_id FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided=0 AND enc.voided=0 AND ob.voided=0 AND enc.location_id=:location "
            + " AND ob.obs_datetime  BETWEEN :startDate AND :endDate AND ob.value_numeric IS NOT NULL "
            + " AND ob.concept_id=856 AND enc.encounter_type in(6) AND ob.value_numeric < 1000 "
            + " UNION "
            + " SELECT pat.patient_id FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided = 0 AND enc.voided = 0 AND ob.voided = 0 AND enc.location_id = :location AND "
            + " ob.obs_datetime BETWEEN :startDate AND :endDate AND enc.encounter_type in(6) AND ob.concept_id=1305 ";

    return query;
  }

  /**
   * E1 exclusions
   *
   * @return String
   */
  public static String getE1ExclusionCriteria(
      int encounterType, int questionConceptId, int answerConceptId) {
    String query =
        "SELECT p.patient_id FROM patient p JOIN encounter e ON p.patient_id=e.patient_id JOIN obs o ON e.encounter_id=o.encounter_id "
            + " JOIN ( "
            + " SELECT pat.patient_id AS patient_id, enc.encounter_datetime AS endDate FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided = 0 AND enc.voided = 0 AND ob.voided = 0 AND enc.location_id = :location "
            + " AND enc.encounter_datetime BETWEEN :startDate AND :endDate AND enc.encounter_type=%d AND "
            + " ob.concept_id=%d AND ob.value_coded=%d "
            + " ) ed "
            + " ON p.patient_id=ed.patient_id "
            + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 "
            + " AND e.location_id = :location AND e.encounter_datetime BETWEEN "
            + " IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) "
            + " AND (:startDate -interval 1 day) AND e.encounter_type=%d "
            + " AND o.concept_id=%d AND o.value_coded=%d";
    return String.format(
        query,
        encounterType,
        questionConceptId,
        answerConceptId,
        encounterType,
        questionConceptId,
        answerConceptId);
  }

  public static final String findPatientsWithAProgramStateMarkedAsTransferedInInAPeriodB2 =
      "SELECT p.patient_id FROM	patient p "
          + "inner join patient_program pg on p.patient_id=pg.patient_id "
          + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
          + "WHERE pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and pg.location_id=:location  and ps.state=29 "
          + "and ps.start_date BETWEEN :startDate and :endDate ";

  public static final String
      findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardB2 =
          "SELECT tr.patient_id from  ("
              + "SELECT p.patient_id,obsData.value_datetime from patient p  "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id  "
              + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
              + "INNER JOIN obs obsTarv ON e.encounter_id=obsTarv.encounter_id AND obsTarv.voided=0 AND obsTarv.concept_id=6300 AND obsTarv.value_coded=6276 "
              + "INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 "
              + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime BETWEEN :startDate AND :endDate AND e.location_id=:location GROUP BY p.patient_id "
              + ") tr GROUP BY tr.patient_id ";

  public static final String findPatientsWithAProgramStateMarkedAsTransferedInInAPeriodStartDateB2 =
      "SELECT p.patient_id FROM	patient p "
          + "inner join patient_program pg on p.patient_id=pg.patient_id "
          + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
          + "WHERE pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and pg.location_id=:location  and ps.state=29 and ps.start_date<:startDate ";

  public static final String
      findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardStartDateB2 =
          "SELECT tr.patient_id from  ( "
              + "SELECT p.patient_id, obsData.value_datetime from patient p  "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id  "
              + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
              + "INNER JOIN obs obsTarv ON e.encounter_id=obsTarv.encounter_id AND obsTarv.voided=0 AND obsTarv.concept_id=6300 AND obsTarv.value_coded=6276 "
              + "INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 "
              + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime<:startDate AND e.location_id=:location GROUP BY p.patient_id "
              + ") tr GROUP BY tr.patient_id ";

  public static final String findPatientsWithAProgramStateMarkedAsTransferedInEndDate =
      "select final.patient_id from  ( "
          + "select states.patient_id,states.patient_program_id,min(states.minStateDate) as minStateDate,states.program_id,states.state from  ( "
          + "SELECT p.patient_id, pg.patient_program_id, ps.start_date as minStateDate, pg.program_id, ps.state  FROM patient p   "
          + "inner join patient_program pg on p.patient_id=pg.patient_id  "
          + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
          + "WHERE pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  "
          + "and ps.start_date <= :endDate "
          + ")states "
          + "group by states.patient_id "
          + "order by states.minStateDate asc  "
          + ") final "
          + "inner join patient_state ps on ps.patient_program_id=final.patient_program_id  "
          + "where ps.start_date=final.minStateDate and ps.state=29 and ps.voided=0 ";

  public static final String
      findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardEndDate =
          "SELECT tr.patient_id from  ("
              + "SELECT p.patient_id, MIN(obsData.value_datetime) from patient p  "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id  "
              + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
              + "INNER JOIN obs obsTarv ON e.encounter_id=obsTarv.encounter_id AND obsTarv.voided=0 AND obsTarv.concept_id=6300 AND obsTarv.value_coded=6276 "
              + "INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 "
              + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime <= :endDate AND e.location_id=:location GROUP BY p.patient_id "
              + ") tr GROUP BY tr.patient_id ";

  public static final String findPatientsWhoWhereMarkedAsTransferedOutAPeriodB2 =
      "select transferidopara.patient_id from ( "
          + "select patient_id,max(data_transferidopara) data_transferidopara from ( "
          + "select pg.patient_id,max(ps.start_date) data_transferidopara from  patient p  "
          + "inner join patient_program pg on p.patient_id=pg.patient_id  "
          + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
          + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.state=7 and  ps.start_date<:startDate group by p.patient_id  "
          + "union  "
          + "select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p  "
          + "inner join encounter e on p.patient_id=e.patient_id  "
          + "inner join obs o on o.encounter_id=e.encounter_id  "
          + "where e.voided=0 and p.voided=0 and o.obs_datetime <:startDate and e.location_id=:location and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53  group by p.patient_id  "
          + "union "
          + "select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p  "
          + "inner join encounter e on p.patient_id=e.patient_id  "
          + "inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime <:startDate and e.location_id=:location  "
          + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 group by p.patient_id "
          + " ) transferido group by patient_id  "
          + " ) transferidopara  "
          + "inner join (  "
          + "select patient_id,max(encounter_datetime) encounter_datetime from(  "
          + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p  "
          + "inner join encounter e on e.patient_id=p.patient_id "
          + "where p.voided=0 and e.voided=0 and e.encounter_datetime <:startDate and e.location_id=:location and e.encounter_type in (18,6,9) group by p.patient_id  "
          + "union  "
          + "Select p.patient_id,max(value_datetime) encounter_datetime from  patient p  "
          + "inner join encounter e on p.patient_id=e.patient_id  "
          + "inner join obs o on e.encounter_id=o.encounter_id  "
          + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null "
          + "and o.value_datetime<:startDate and e.location_id=:location  group by p.patient_id"
          + ") consultaLev group by patient_id "
          + ") consultaOuARV "
          + "on transferidopara.patient_id=consultaOuARV.patient_id  "
          + "where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara  "
          + "and transferidopara.data_transferidopara<:startDate ";

  /**
   * E2 exclusions
   *
   * @param viralLoadConcept
   * @param encounterType
   * @param qualitativeConcept
   * @return String
   */
  public static String getE2ExclusionCriteria() {

    String query =
        "SELECT p.patient_id FROM patient p "
            + "JOIN encounter e ON p.patient_id=e.patient_id "
            + "JOIN obs o ON e.encounter_id=o.encounter_id "
            + "JOIN ("
            + "SELECT pat.patient_id AS patient_id,ob.obs_datetime AS endDate FROM patient pat "
            + "JOIN encounter enc ON pat.patient_id=enc.patient_id "
            + "JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided=0 AND enc.voided=0 AND ob.voided=0 AND enc.location_id=:location AND ob.obs_datetime "
            + " BETWEEN :startDate AND :endDate AND ob.concept_id IN(856,1305) AND enc.encounter_type in(6) "
            + " ) ed "
            + " ON p.patient_id=ed.patient_id "
            + " WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.location_id=:location "
            + " AND o.obs_datetime BETWEEN "
            + " IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) "
            + " AND (:startDate -interval 1 day) "
            + " AND o.concept_id IN (856,1305)"
            + " AND e.encounter_type in(6) ";
    return query;
  }

  public static String getE3ExclusionCriteria() {

    String query =
        "SELECT pat.patient_id FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided=0 AND enc.voided=0 AND ob.voided=0 AND enc.location_id=:location "
            + " AND ob.obs_datetime BETWEEN IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) AND (:startDate -interval 1 day) "
            + " AND ob.value_numeric IS NOT NULL "
            + " AND ob.concept_id=856 AND enc.encounter_type in(6) AND ob.value_numeric < 1000 "
            + " UNION "
            + " SELECT pat.patient_id FROM patient pat "
            + " JOIN encounter enc ON pat.patient_id=enc.patient_id JOIN obs ob ON enc.encounter_id=ob.encounter_id "
            + " WHERE pat.voided = 0 AND enc.voided = 0 AND ob.voided = 0 AND enc.location_id = :location AND "
            + " ob.obs_datetime BETWEEN IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) AND (:startDate -interval 1 day) "
            + " AND enc.encounter_type in(6) AND ob.concept_id=1305 ";

    return query;
  }

  public static String getNumberOfPatientsWhoHadClinicalAppointmentF1() {
    String query =
        "SELECT patient_id from (  "
            + "SELECT e.patient_id  FROM encounter e   "
            + "WHERE e.encounter_type=6 AND e.location_id=:location   "
            + "AND e.encounter_datetime BETWEEN :startDate AND :endDate AND e.voided=0   "
            + ")f ";

    return String.format(query);
  }

  /**
   * F3 exclusions
   *
   * @param encounterType
   * @return
   */
  public static String getF3Exclusion(int encounterType) {
    String query =
        " SELECT p.patient_id FROM patient p JOIN encounter e ON p.patient_id=e.patient_id JOIN ( "
            + " SELECT pat.patient_id AS patient_id, enc.encounter_datetime AS endDate FROM encounter enc JOIN patient pat "
            + " ON enc.patient_id=pat.patient_id WHERE enc.encounter_type=%d AND enc.location_id=:location "
            + " AND enc.encounter_datetime BETWEEN :startDate AND :endDate AND pat.voided=0 AND enc.voided=0) ed "
            + " ON p.patient_id=ed.patient_id"
            + " WHERE e.encounter_type=%d AND e.location_id=:location "
            + " AND e.encounter_datetime BETWEEN "
            + " IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) "
            + " AND (:startDate -interval 1 day) "
            + "AND p.voided=0 AND e.voided=0 ";
    return String.format(query, encounterType, encounterType);
  }

  public static String getF3ExclusionTransferedIn() {
    String query =
        "SELECT trasferedPatients.patient_id FROM ( "
            + "select minState.patient_id,minState.minStateDate as initialDate from  ("
            + "SELECT p.patient_id, pg.patient_program_id, MIN(ps.start_date) as minStateDate  FROM  patient p  "
            + "inner join patient_program pg on p.patient_id=pg.patient_id  "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "WHERE  pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location "
            + "and ps.start_date BETWEEN  IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) AND :endDate "
            + "GROUP BY pg.patient_program_id) minState  "
            + "inner join patient_state ps on ps.patient_program_id=minState.patient_program_id  "
            + "where ps.start_date=minState.minStateDate and ps.state=29 and ps.voided=0 "
            + "UNION  "
            + "SELECT p.patient_id,MIN(obsData.value_datetime) AS initialDate  FROM patient p   "
            + "INNER JOIN encounter e  ON e.patient_id=p.patient_id  "
            + "INNER JOIN obs o on o.encounter_id=e.encounter_id  "
            + "INNER JOIN obs obsPretarv on e.encounter_id=obsPretarv.encounter_id  "
            + "INNER JOIN obs obsData on e.encounter_id=obsData.encounter_id "
            + "WHERE e.voided=0 AND o.voided=0  AND e.encounter_type=53  AND obsPretarv.voided=0  "
            + "AND obsPretarv.concept_id=6300  "
            + "AND obsPretarv.value_coded=6276 "
            + "AND obsData.concept_id=23891 "
            + "AND obsData.voided=0 "
            + "AND e.location_id=:location   "
            + "AND o.concept_id=1369  "
            + "AND o.value_coded=1065  "
            + "AND obsData.value_datetime BETWEEN  IF(MONTH(:startDate) = 12  && DAY(:startDate) = 21, :startDate, CONCAT(YEAR(:startDate)-1, '-12','-21')) AND :endDate "
            + "GROUP BY p.patient_id)trasferedPatients  "
            + "GROUP BY trasferedPatients.patient_id ";

    return query;
  }

  /**
   * Get patients with encounters within start and end date F1: Number of patients who had clinical
   * appointment during the reporting month
   *
   * @return String
   */
  public static String getNumberOfPatientsWhoHadClinicalAppointmentDuringTheReportingMonthF1() {
    String query =
        "SELECT f.encounter_id  from (   "
            + "SELECT e.encounter_id,e.patient_id  FROM encounter e    "
            + "WHERE e.encounter_type=6 AND e.location_id=:location    "
            + "AND e.encounter_datetime BETWEEN :startDate AND :endDate AND e.voided=0  "
            + "order by e.patient_id, e.encounter_datetime   "
            + ")f ";

    return String.format(query);
  }

  public static String findPatientWhoHaveTbSymthomsF2() {

    String query =
        "SELECT f.encounter_id from   (   "
            + "SELECT e.encounter_id  FROM encounter e    "
            + "inner join obs o on o.encounter_id=e.encounter_id  "
            + "where e.voided=0  AND e.encounter_type=6 and o.concept_id=23758 and o.value_coded in (1065,1066) and e.location_id=:location  "
            + "and e.encounter_datetime between :startDate and :endDate   "
            + ")f where f.encounter_id not in "
            + "( "
            + "SELECT e.encounter_id  FROM encounter e    "
            + "inner join obs o on o.encounter_id=e.encounter_id  "
            + "where e.voided=0  AND e.encounter_type=6 and o.concept_id=1268 and o.value_coded is not null and e.location_id=:location  "
            + "and e.encounter_datetime between :startDate and :endDate  "
            + ") ";
    return query;
  }

  public static String findPatientWhoHaveTbActiveF2() {

    String query =
        "select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and o.concept_id=1268 and o.value_coded in (1256,1257,1267) and e.location_id=:location and e.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate ";

    return query;
  }

  public static String findPatientWhoHaveTbSymthomsAndTbActive() {

    String query =
        "select TbSynthoms.patient_id, TbSynthoms.data_tb,TbActive.tb_active_concept from ( "
            + "select p.patient_id,e.encounter_datetime as data_tb  from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and o.concept_id=23758 and  o.value_coded in (1065,1066) and e.location_id=:location "
            + "and e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_datetime between :startDate and :endDate)TbSynthoms "
            + "left join( "
            + "select p.patient_id,o.concept_id as tb_active_concept,e.encounter_datetime as data_tb from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and o.concept_id=1268 and o.value_coded in (1256,1257,1267) and e.location_id=:location and e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate) "
            + "TbActive on TbSynthoms.patient_id=TbActive.patient_id and TbSynthoms.data_tb=TbActive.data_tb ";

    return query;
  }
}
