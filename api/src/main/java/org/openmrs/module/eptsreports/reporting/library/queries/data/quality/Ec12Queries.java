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
package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class Ec12Queries {

  /**
   * Get the query for EC12 patient listing
   *
   * @return String
   */
  public static String getEc12CombinedQuery(int programId, int year) {
    String query =
        " SELECT "
            + " DISTINCT(pa.patient_id) AS patient_id "
            + " ,pi.identifier AS NID "
            + " ,CONCAT(pn.given_name, ' ', pn.family_name ) AS Name "
            + " ,DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate "
            + " ,IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob "
            + " ,pe.gender AS Sex "
            + " ,DATE_FORMAT(pa.date_created, '%d-%m-%Y') AS First_entry_date "
            + " ,DATE_FORMAT(pa.date_changed, '%d-%m-%Y') AS Last_updated "
            + " ,DATE_FORMAT(pg.date_enrolled, '%d-%m-%Y') AS date_enrolled "
            + " ,case when ps.state = 9 then 'DROPPED FROM TREATMENT' "
            + " when ps.state = 6 then 'ACTIVE ON PROGRAM' "
            + " when ps.state = 10 then 'PATIENT HAS DIED' "
            + " when ps.state = 8 then 'SUSPENDED TREATMENT' "
            + " when ps.state = 7 then 'TRANSFERED OUT TO ANOTHER FACILITY' "
            + " when ps.state = 29 then 'TRANSFERRED FROM OTHER FACILTY' end AS state "
            + " , l.name AS location_name, ps.start_date as ps_start_date "
            + " FROM patient pa "
            + " INNER JOIN person pe ON pa.patient_id = pe.person_id "
            + " INNER JOIN patient_identifier pi ON pa.patient_id = pi.patient_id "
            + " INNER JOIN person_name pn ON pa.patient_id = pn.person_id "
            + " LEFT  JOIN patient_program pg ON pa.patient_id = pg.patient_id AND pg.program_id = "
            + programId
            + " LEFT JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id "
            + " INNER JOIN location l ON pg.location_id = l.location_id "
            + " WHERE pe.birthdate IS NOT NULL AND "
            + " YEAR(pe.birthdate) < "
            + year
            + " AND ps.end_date is null ";

    return query;
  }
}
