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

public class Ec13Queries {

  /**
   * Get the query for EC13 patient listing
   *
   * @return String
   */
  public static String getEc13CombinedQuery(int programId) {
    String query =
        " SELECT  DISTINCT(pa.patient_id), pi.identifier AS NID, "
            + " CONCAT(pn.given_name, ' ', pn.family_name ) AS Name, "
            + " DATE_FORMAT(pe.birthdate, '%d/%m/%Y') AS birthdate, "
            + " IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob, "
            + " pe.gender AS Sex, DATE_FORMAT(pa.date_created, '%d/%m/%Y') AS First_entry_date, "
            + " DATE_FORMAT(pa.date_changed, '%d/%m/%Y') AS Last_updated, "
            + " DATE_FORMAT(pg.date_enrolled, '%d/%m/%Y') AS date_enrolled, "
            + " case "
            + " when ps.state = 9 then 'ABANDONO' "
            + " when ps.state = 6 then 'ACTIVO NO PROGRAMA' "
            + " when ps.state = 10 then 'OBITOU' "
            + " when ps.state = 8 then 'SUSPENDER TRATAMENTO' "
            + " when ps.state = 7 then 'TRANSFERIDO PARA' "
            + " when ps.state = 29 then 'TRANSFERIDO DE' "
            + "end AS state, "
            + " DATE_FORMAT(ps.start_date, '%d/%m/%Y') AS state_date, "
            + " l.name AS location_name FROM patient pa "
            + " INNER JOIN patient_identifier pi ON pa.patient_id=pi.patient_id "
            + " INNER JOIN person pe ON pa.patient_id=pe.person_id "
            + " INNER JOIN person_name pn ON pa.patient_id=pn.person_id "
            + " INNER JOIN patient_program pg ON pa.patient_id=pg.patient_id "
            + "	LEFT JOIN ( "
            + " select max_estado.patient_id, max_estado.start_date, ps.state, ps.patient_state_id "
            + "	from( "
            + "			select pp.patient_id, max(max_estado.start_date) start_date,ps.patient_state_id,ps.state "
            + "			from( "
            + "				select pg.patient_id, ps.start_date,ps.voided,ps.state "
            + "				from patient p "
            + "						inner join patient_program pg on p.patient_id = pg.patient_id "
            + "				  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "				where pg.voided=0  and p.voided=0 and  pg.program_id = 2 and ps.voided=0 "
            + "					and ps.start_date  <= :endDate  and pg.location_id=:location "
            + "                    order by pg.patient_id, ps.patient_state_id desc, ps.start_date desc "
            + "			)max_estado "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "			 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.start_date and max_estado.state=ps.state "
            + "	  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location "
            + "			group by pp.patient_id "
            + "	) max_estado "
            + "	inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "	inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "	) AS ps ON pe.person_id = ps.patient_id "
            + " INNER JOIN encounter e ON pa.patient_id=e.patient_id "
            + " INNER JOIN location l ON e.location_id=l.location_id "
            + " WHERE "
            + " pg.program_id= 2 "
            + " and ps.patient_state_id = (select max(ps1.patient_state_id) from patient_state ps1 inner join patient_program pp on ps1.patient_program_id = pp.patient_program_id inner join patient p "
            + " on p.patient_id = pp.patient_id where pp.patient_id = pa.patient_id and pp.program_id = 2 "
            + " ) "
            + " AND pa.patient_id IN( "
            + " SELECT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id=pe.person_id "
            + " WHERE TIMESTAMPDIFF(MONTH,pe.birthdate,CURRENT_TIMESTAMP)<0) ";
    return query;
  }
}
