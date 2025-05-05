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

public class Ec10Queries {
  /**
   * Get the query to be used to display the EC10 patient listing
   *
   * @return String
   */
  public static String getEc10CombinedQuery() {
    String query =
        " SELECT pe.person_id As patient_id "
            + " ,pid.identifier AS NID "
            + " ,concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name "
            + " ,DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate "
            + " ,IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob "
            + " ,pe.gender AS Sex "
            + " ,DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date "
            + " ,DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated "
            + " ,DATE_FORMAT(pg.date_enrolled, '%d-%m-%Y') AS date_enrolled "
            + " ,case when ps.state = 9 then 'DROPPED FROM TREATMENT' "
            + " when ps.state = 6 then 'ACTIVE ON PROGRAM' "
            + " when ps.state = 10 then 'PATIENT HAS DIED' "
            + " when ps.state = 8 then 'SUSPENDED TREATMENT' "
            + " when ps.state = 7 then 'TRANSFERED OUT TO ANOTHER FACILITY' "
            + " when ps.state = 29 then 'TRANSFERRED FROM OTHER FACILTY' end AS state "
            + " ,DATE_FORMAT(ps.start_date, '%d-%m-%Y') AS state_date "
            + " ,DATE_FORMAT(consultation.encounter_datetime, '%d-%m-%Y') AS encounter_date "
            + " ,DATE_FORMAT(consultation.date_created, '%d-%m-%Y') AS encounter_date_created "
            + " ,consultation.location_name AS location_name "
            + " FROM "
            + " person pe "
            + " inner join "
            + " ( "
            + "	select max_estado.patient_id, max_estado.data_abandono "
            + "	from( "
            + "			select pp.patient_id, ps.patient_state_id, ps.state, max(max_estado.data_estado) data_abandono "
            + "			from( "
            + " "
            + "				select pg.patient_id, ps.start_date data_estado "
            + "				from patient p "
            + "						inner join patient_program pg on p.patient_id = pg.patient_id "
            + "				  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "				where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id = 2 "
            + "					and ps.start_date <= :endDate  and pg.location_id=:location "
            + "			)max_estado "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "			 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado "
            + "	  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location "
            + "	    			group by pp.patient_id "
            + "	) max_estado "
            + "		inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "		inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "		where ps.state = 9 "
            + " ) abandonedPrograma on pe.person_id = abandonedPrograma.patient_id "
            + " inner join "
            + " (	select pn1.* "
            + " from person_name pn1 "
            + " inner join "
            + " ( "
            + " select person_id, min(person_name_id) id "
            + " from person_name "
            + " where voided = 0 "
            + " group by person_id "
            + " ) pn2 "
            + " where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id "
            + " ) pn on pn.person_id = pe.person_id "
            + " inner join "
            + " (   select pid1.* "
            + " from patient_identifier pid1 "
            + " inner join "
            + " ( "
            + " select patient_id, min(patient_identifier_id) id "
            + " from patient_identifier "
            + " where voided = 0 "
            + " group by patient_id "
            + " ) pid2 "
            + " where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id "
            + " ) pid on pid.patient_id = pe.person_id "
            + " inner join "
            + " ( "
            + " Select 	p.patient_id, e.encounter_datetime, l.name  location_name, e.date_created "
            + " from 	patient p "
            + " inner join encounter e on p.patient_id = e.patient_id "
            + " inner join location l on l.location_id = e.location_id "
            + " where 	p.voided = 0 and e.voided = 0 and e.encounter_type IN (9,6 "
            + " ) and e.location_id IN (:location) "
            + " AND e.encounter_datetime between :startDate AND :endDate "
            + " order by e.encounter_datetime desc "
            + " ) consultation 	on consultation.patient_id = pe.person_id "
            + " inner join patient_program pg ON pe.person_id = pg.patient_id and pg.program_id = 2 and pg.location_id IN (:location) "
            + "	LEFT JOIN ( "
            + "	select max_estado.patient_id, max_estado.start_date, max_estado.state "
            + "	from( "
            + "			select pp.patient_id, ps.patient_state_id, ps.state, max(max_estado.data_estado) start_date "
            + "			from( "
            + " "
            + "				select pg.patient_id, ps.start_date data_estado "
            + "				from patient p "
            + "						inner join patient_program pg on p.patient_id = pg.patient_id "
            + "				  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "				where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id = 2 "
            + "					and ps.start_date <= :endDate  and pg.location_id=:location "
            + "			)max_estado "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "			 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado "
            + "	  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location "
            + "	    			group by pp.patient_id "
            + "	) max_estado "
            + "		inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "		inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "	) AS ps ON pe.person_id = ps.patient_id "
            + " where "
            + " pe.voided = 0 "
            + " and abandonedPrograma.patient_id is not null "
            + " and consultation.encounter_datetime >= abandonedPrograma.data_abandono "
            + " GROUP BY pe.person_id ";
    return query;
  }
}
