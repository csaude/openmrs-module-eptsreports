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

public interface TB7AdvancedDiseaseQueries {

  public class QUERY {

    public static final String FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT = "TB7/TR_OUT_CURR_DATE.sql";

    public static final String FIND_PATIENTS_WHO_ARE_DEAD = "TB7/DEAD_CURR_DATE.sql";

    public static final String FIND_PATIENTS_WITH_CD4_AFTER_FIRST_PREGNANT_REGISTRATION =
        "TB7/PATIENTS_WITH_CD4_AFTER_FIRST_PREGNANT_REGISTRATION.sql";

    public static final String FIND_PREGNANTS_WITH_COUNT_CD4 =
        "TB7/PATIENTS_PREGNANT_WITH_COUNT_CD4.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_TX_NEW =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_TX_NEW.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_CASCATE2 =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_CASCATE2.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_HIGH_VL =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_HIGH_VL.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART_CASCATE2 =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_ART_RESTART_CASCATE2.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT_CASCATE2 =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_PREGNANT_CASCATE2.sql";

    public static final String FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL =
        "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_GRADE_LEVEL.sql";

    public static final String
        FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_ZERO_GRADE_LEVEL =
            "TB7/PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_ZERO_GRADE_LEVEL.sql";

    public enum PositivityLevel {
      NO_GRADE(null),

      GRADE_ONE(165186),

      GRADE_TWO(165187),

      GRADE_THREE(165188),

      GRADE_FOUR(165348);

      private final Integer value;

      PositivityLevel(final Integer value) {
        this.value = value;
      }

      public Integer getValue() {
        return this.value;
      }
    }

    public static String findPatientsWithHighViralLoad =
        "select penultimaCV.patient_id from "
            + "( "
            + " select ultima_cv.patient_id, max(date(o.obs_datetime)) dataPenultimaCV "
            + "from( "
            + "	select p.patient_id ,max(date(o.obs_datetime)) data_cv "
            + "	from patient p "
            + "		inner join encounter e on p.patient_id=e.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + "	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric > 1000 "
            + "	and o.obs_datetime between :startDate and :endDate and e.location_id=:location "
            + "	group by p.patient_id "
            + ") ultima_cv "
            + "inner join encounter e on ultima_cv.patient_id=e.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + "		where e.voided=0 and o.voided=0 and concept_id in (856,1305) and  e.encounter_type in (13,51) "
            + "	and date(o.obs_datetime) < ultima_cv.data_cv and e.location_id=:location "
            + "	group by ultima_cv.patient_id "
            + "	) penultimaCV "
            + "	inner join encounter e on penultimaCV.patient_id=e.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + "		where e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric > 1000 "
            + "	and date(o.obs_datetime) = penultimaCV.dataPenultimaCV and e.location_id=:location "
            + "	group by penultimaCV.patient_id ";

    public static String findPatientWhoReinitiatedART =
        "select patient_id 																		"
            + "from (																 															"
            + "		select p.patient_id, max(o.obs_datetime) data_reinicio, e.encounter_id 														"
            + " 	from patient p 																												"
            + "			inner join encounter e on e.patient_id=p.patient_id 																	"
            + "			inner join obs o on o.encounter_id=e.encounter_id 																		"
            + "  	where e.voided=0 and e.encounter_type in (6,53) and o.concept_id in(6272,6273) and o.value_coded=1705 and o.voided=0 		"
            + "   		and  e.location_id=:location and o.obs_datetime between :startDate and :endDate 										"
            + "  		group by p.patient_id																									"
            + " ) reinicio																														";

    public static final String findPatientsWhoReinitiatedARTWhoHaveCD4Count =
        "  															"
            + "select cd4_eligible.patient_id                                                                                                "
            + "from(                                                                                                                         "
            + "                                                                                                                              "
            + "    select p.patient_id, max(o.obs_datetime) data_reinicio, e.encounter_id                                                    "
            + "    from patient p                                                                                                            "
            + "            inner join encounter e on e.patient_id=p.patient_id                                                               "
            + "            inner join obs o on o.encounter_id=e.encounter_id                                                                 "
            + "    where e.voided=0 and e.encounter_type in (6,53) and o.concept_id in(6272,6273) and o.value_coded=1705 and o.voided=0      "
            + "        and  e.location_id= :location and o.obs_datetime between  :startDate and :endDate                                     "
            + "        group by p.patient_id                                                                                                 "
            + ")cd4_eligible                                                                                                                 "
            + "left join(                                                                                                                    "
            + "                                                                                                                              "
            + "    select p.patient_id, o.obs_datetime                                                                                       "
            + "    from patient p                                                                                                            "
            + "        inner join encounter e on e.patient_id=p.patient_id                                                                   "
            + "        inner join obs o on o.encounter_id=e.encounter_id                                                                     "
            + "    where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0                "
            + "        and  e.location_id= :location and o.obs_datetime <= date_add( :endDate, interval  33 day)                             "
            + ")cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id                                                            "
            + "where cd4_absolute.obs_datetime between cd4_eligible.data_reinicio and date_add(cd4_eligible.data_reinicio, interval 33 day)  ";

    public static final String findPatientsWhoInitiatedARTAndHaveCD4CountWithin33Days =
        " select cd4_eligible.patient_id  		"
            + "from( 																								"
            + " 																										"
            + "  select patient_id, art_start_date  																	"
            + "  from( 											                                                    "
            + "   																									"
            + "    select patient_id, min(art_start_date) art_start_date  											"
            + "    from( 																							"
            + "       																								"
            + "      select p.patient_id, min(e.encounter_datetime) art_start_date  									"
            + "      from patient p  																				"
            + "              inner join encounter e on p.patient_id=e.patient_id  									"
            + "                inner join obs o on o.encounter_id=e.encounter_id  									"
            + "            where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9)  			"
            + "              and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id= :location  "
            + "                group by p.patient_id  																"
            + "         																								"
            + "          union  																						"
            + "             																							"
            + "            select p.patient_id, min(value_datetime) art_start_date  									"
            + "            from patient p  																			"
            + "              inner join encounter e on p.patient_id=e.patient_id  									"
            + "                inner join obs o on e.encounter_id=o.encounter_id  									"
            + "      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 				"
            + "               and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id= :location  "
            + "               group by p.patient_id  																"
            + "           																							"
            + "            union  																					"
            + "             																							"
            + "            select pg.patient_id, min(date_enrolled) art_start_date  									"
            + "            from patient p  																			"
            + "              inner join patient_program pg on p.patient_id=pg.patient_id  							"
            + "            where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id= :location  "
            + "              group by pg.patient_id  																"
            + "            																							"
            + "            union  																					"
            + "             																							"
            + "            select e.patient_id, min(e.encounter_datetime) as art_start_date  						"
            + "            from patient p  																			"
            + "              inner join encounter e on p.patient_id=e.patient_id  									"
            + "        where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id= :location  "
            + "              group by p.patient_id  																	"
            + "            																							"
            + "            union  																					"
            + "             																							"
            + "            select p.patient_id, min(value_datetime) art_start_date  									"
            + "            from patient p  																			"
            + "              inner join encounter e on p.patient_id=e.patient_id  									"
            + "                inner join obs o on e.encounter_id=o.encounter_id  									"
            + "          where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 						"
            + "              and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime <= :endDate and e.location_id= :location  "
            + "                group by p.patient_id  																"
            + "      ) art_start  																					"
            + "               group by patient_id  																	"
            + "  ) tx_new where art_start_date between :startDate and :endDate  										"
            + ") cd4_eligible 																						"
            + "left join( 																							"
            + "   																									"
            + "  select p.patient_id, o.obs_datetime 																"
            + "  from patient p 																						"
            + "    inner join encounter e on e.patient_id=p.patient_id 												"
            + "    inner join obs o on o.encounter_id=e.encounter_id 												"
            + "  where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0  "
            + "    and  e.location_id= :location and o.obs_datetime <= date_add(:endDate, interval  33 day) 			"
            + ")cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id 									"
            + " where  cd4_absolute.obs_datetime between cd4_eligible.art_start_date and date_add(cd4_eligible.art_start_date, interval 33 day)";

    public static final String findPatientsWithCD4 =
        "																"
            + "select p.patient_id from patient p 														"
            + "		inner join encounter e on e.patient_id=p.patient_id  "
            + "		inner join obs o on o.encounter_id=e.encounter_id  "
            + "where p.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) "
            + "		and e.location_id= :location and o.obs_datetime >= :startDate and o.obs_datetime <= :endDate ";

    public static final String findPatientsWIthHighVLWithCD4Count =
        "select cd4_eligible.patient_id  												"
            + "from ( 																																"
            + "    select distinct ultima_cv.patient_id , ultima_cv.data_cv                                                     						"
            + "    from(                                                                                                                            "
            + "                                                                                                                                     "
            + "    select p.patient_id ,o.obs_datetime data_cv, o.value_numeric                                                                     "
            + "    from patient p                                                                                                                   "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                                          "
            + "        inner join obs o on e.encounter_id=o.encounter_id                                                                            "
            + "    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric > 1000  "
            + "    and o.obs_datetime between  :startDate and  :endDate and e.location_id=:location                                                   		"
            + "   ) ultima_cv                                                                                                                       "
            + "   inner join(                                                                                                                       "
            + "                                                                                                                                     "
            + "    select p.patient_id ,o.obs_datetime data_cv, o.value_numeric                                                                     "
            + "    from patient p                                                                                                                   "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                                          "
            + "        inner join obs o on e.encounter_id=o.encounter_id                                                                            "
            + "    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric>1000    "
            + "        and o.obs_datetime <  :endDate and e.location_id=:location                                                                      	"
            + "                                                                                                                                     "
            + "   ) penultima_cv on penultima_cv.patient_id = ultima_cv.patient_id                                                                  "
            + "   left join(                                                                                                                        "
            + "                                                                                                                                     "
            + "    select p.patient_id ,o.obs_datetime data_cv, o.value_numeric                                                                     "
            + "    from patient p                                                                                                                   "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                                          "
            + "        inner join obs o on e.encounter_id=o.encounter_id                                                                            "
            + "    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric <=1000  "
            + "    and o.obs_datetime <  :endDate and e.location_id=:location                                                                          	"
            + "   ) outras_cv on ultima_cv.patient_id = outras_cv.patient_id                                                                        "
            + "   where penultima_cv.data_cv < ultima_cv.data_cv                                                                                    "
            + "    and (outras_cv.patient_id is null or outras_cv.data_cv < penultima_cv.data_cv or outras_cv.data_cv > ultima_cv.data_cv )         "
            + ")cd4_eligible 																														"
            + "left join( 																															"
            + "     																																	"
            + "    select p.patient_id, o.obs_datetime 																								"
            + "    from patient p 																													"
            + "        inner join encounter e on e.patient_id=p.patient_id 																			"
            + "        inner join obs o on o.encounter_id=e.encounter_id 																			"
            + "    where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0  						"
            + "        and  e.location_id=:location and o.obs_datetime <= date_add( :endDate, interval  33 day) 											"
            + ")cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id 																	"
            + "where cd4_absolute.obs_datetime between cd4_eligible.data_cv and date_add(cd4_eligible.data_cv, interval 33 day) 					";

    public static final String findPatientsWithTBLAMResults =
        "																						"
            + "select p.patient_id                                                                                                                         "
            + "from patient p                                                                                                                           "
            + "  inner join encounter e on e.patient_id=p.patient_id                                                                                         "
            + "  inner join obs o on o.encounter_id=e.encounter_id                                                                                           "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type in (6, 13, 51) and o.concept_id=23951 and o.value_coded is not null  and o.voided=0 "
            + "  and e.location_id=:location and e.encounter_datetime between :startDate and :endDate                                                  "
            + "union                                                                                                                                       "
            + "                                                                                                                                            "
            + "select p.patient_id                                                                                                                         "
            + "from patient p                                                                                                                           "
            + "  inner join encounter e on e.patient_id=p.patient_id                                                                                         "
            + "  inner join obs o on o.encounter_id=e.encounter_id                                                                                           "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type = 90 and o.concept_id=23951 and o.value_coded in (703, 664)  and o.voided=0         "
            + "  and  e.location_id=:location and o.obs_datetime between :startDate and :endDate                                                       ";

    public static final String findPatientsWithTBLAMResultsByreportGenereationDate =
        "																						"
            + "select p.patient_id                                                                                                                         "
            + "from patient p                                                                                                                           "
            + "  inner join encounter e on e.patient_id=p.patient_id                                                                                         "
            + "  inner join obs o on o.encounter_id=e.encounter_id                                                                                           "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type in (6, 13, 51) and o.concept_id=23951 and o.value_coded is not null  and o.voided=0 "
            + "  and e.location_id=:location and e.encounter_datetime between :endDate and CURDATE()                                                  "
            + "union                                                                                                                                       "
            + "                                                                                                                                            "
            + "select p.patient_id                                                                                                                         "
            + "from patient p                                                                                                                           "
            + "  inner join encounter e on e.patient_id=p.patient_id                                                                                         "
            + "  inner join obs o on o.encounter_id=e.encounter_id                                                                                           "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type = 90 and o.concept_id=23951 and o.value_coded in (703, 664)  and o.voided=0         "
            + "  and  e.location_id=:location and o.obs_datetime between :endDate and CURDATE()                                                       ";

    public static final String findPatientsWithNegativeTBLAMResults =
        "																							"
            + "select negative_tb_lam.patient_id                                                                                                         "
            + "from(                                                                                                                                     "
            + "                                                                                                                                          "
            + "  select p.patient_id                                                                                                                     "
            + "  from patient p                                                                                                                          "
            + "    inner join encounter e on e.patient_id=p.patient_id                                                                                   "
            + "    inner join obs o on o.encounter_id=e.encounter_id                                                                                     "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type in (6, 13, 51, 90) and o.concept_id=23951 and o.value_coded = 664  and o.voided=0 "
            + "    and  e.location_id= :location and e.encounter_datetime between  :startDate and date_add(:endDate, interval 33 day)                                              "
            + ")negative_tb_lam                                                                                                                          "
            + "left join (                                                                                                                               "
            + "                                                                                                                                          "
            + "  select p.patient_id                                                                                                                     "
            + "  from patient p                                                                                                                          "
            + "    inner join encounter e on e.patient_id=p.patient_id                                                                                   "
            + "    inner join obs o on o.encounter_id=e.encounter_id                                                                                     "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type in (6, 13, 51, 90) and o.concept_id=23951 and o.value_coded = 703  and o.voided=0 "
            + "    and  e.location_id= :location and e.encounter_datetime between  :startDate and date_add(:endDate, interval 33 day)                                              "
            + ") positive_tb_lam on negative_tb_lam.patient_id = positive_tb_lam.patient_id                                                              "
            + "where positive_tb_lam.patient_id is null                                                                                                  ";

    public static final String findPatientsWithPositiveTBLAMResults =
        "																							"
            + "select positive_tb_lam.patient_id                                                                                                         "
            + "from(                                                                                                                                     "
            + "                                                                                                                                          "
            + "  select p.patient_id                                                                                                                     "
            + "  from patient p                                                                                                                          "
            + "    inner join encounter e on e.patient_id=p.patient_id                                                                                   "
            + "    inner join obs o on o.encounter_id=e.encounter_id                                                                                     "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type in (6, 13, 51, 90) and o.concept_id=23951 and o.value_coded = 703  and o.voided=0 "
            + "    and  e.location_id= :location and e.encounter_datetime between  :startDate and date_add(:endDate, interval 33 day)                                              "
            + ")positive_tb_lam                                                                                                                          ";

    public static final String eValuatePatientsCheckingGenExpertTest =
        "																							"
            + "select distinct p.patient_id                                                    						"
            + "from patient p                                                                                   	"
            + "   inner join encounter e on e.patient_id=p.patient_id                                           	"
            + "   left join obs o on (e.encounter_id =  o.encounter_id and o.voided=0 and o.concept_id=23723)   	"
            + "where p.voided=0 and  e.voided=0 and e.encounter_type in (6,13,90)                               	"
            + "        and  e.location_id= :location and e.encounter_datetime between  :endDate and CURDATE()   	"
            + "        and o.concept_id  %s                                                                     	"
            + "union                                                                                            	"
            + "                                                                                                 	"
            + "select p.patient_id 																					"
            + "from patient p   																					"
            + "		inner join encounter e on e.patient_id=p.patient_id                                        		"
            + "		left join obs o on (e.encounter_id = o.encounter_id and o.voided=0 and o.concept_id=165588)     "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type = 13                                           "
            + "    and  e.location_id=:location and e.encounter_datetime between :endDate and CURDATE()             "
            + "    and o.concept_id  %s 																			";

    public static final String findPatientsWithPositiveGenExpert =
        "																							"
            + "select distinct p.patient_id                                                    						"
            + "from patient p                                                                                   	"
            + "   inner join encounter e on e.patient_id=p.patient_id                                           	"
            + "   inner join obs o on e.encounter_id =  o.encounter_id               							  	"
            + "where p.voided=0 and  e.voided=0 and e.encounter_type in (6,13) and o.voided=0 and o.concept_id=23723 and o.value_coded = 703                            	"
            + "        and  e.location_id= :location and e.encounter_datetime between  :endDate and CURDATE()   	"
            + "        										                                                     	"
            + "union                                                                                            	"
            + "                                                                                                 	"
            + "select p.patient_id 																					"
            + "from patient p   																					"
            + "		inner join encounter e on e.patient_id=p.patient_id                                        		"
            + "		inner join obs o on e.encounter_id = o.encounter_id 											     "
            + "where p.voided=0 and  e.voided=0 and e.encounter_type = 13 and o.voided=0 and o.concept_id=165588 and o.value_coded in (703,165190,6230,6229,6228,165587)"
            + "    and  e.location_id=:location and e.encounter_datetime between :endDate and CURDATE()             ";

    public static final String findPatientsWhoInitiatedTBTreatment =
        " 														"
            + "select patient_id                                                                                            "
            + "from (                                                                                                       "
            + "                                                                                                             "
            + "   select  p.patient_id,o.value_datetime data_inicio                                                         "
            + "   from    patient p                                                                                         "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                  "
            + "        inner join obs o on o.encounter_id=e.encounter_id                                                    "
            + "   where e.voided=0 and o.voided=0 and p.voided=0 and                                                        "
            + "        e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id=  :location                       "
            + "       and  o.value_datetime  between  :endDate and curDate()                                                "
            + "   union                                                                                                     "
            + "                                                                                                             "
            + "   select  pg.patient_id,date_enrolled data_inicio                                                           "
            + "   from    patient p                                                                                         "
            + "         inner join patient_program pg on p.patient_id=pg.patient_id                                         "
            + "   where   pg.voided=0 and p.voided=0 and program_id=5 and location_id=  :location                           "
            + "         and date_enrolled  between  :endDate and curDate()                                                  "
            + "   union                                                                                                     "
            + "                                                                                                             "
            + "   select  p.patient_id,o.obs_datetime data_inicio                                                           "
            + "   from    patient p                                                                                         "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                  "
            + "        inner join obs o on o.encounter_id=e.encounter_id                                                    "
            + "   where e.voided=0 and o.voided=0 and p.voided=0 and                                                        "
            + "        e.encounter_type=53 and o.concept_id=1406                                                            "
            + "        and o.value_coded=42  and e.location_id=  :location                                                  "
            + "        and  o.obs_datetime  between  :endDate and curDate()                                                 "
            + "                                                                                                             "
            + "   union                                                                                                     "
            + "                                                                                                             "
            + "   select  p.patient_id,e.encounter_datetime data_inicio                                                     "
            + "   from    patient p                                                                                         "
            + "        inner join encounter e on p.patient_id=e.patient_id                                                  "
            + "        inner join obs o on o.encounter_id=e.encounter_id                                                    "
            + "   where e.voided=0 and o.voided=0 and p.voided=0 and                                                        "
            + "        e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id=  :location       "
            + "        and e.encounter_datetime between  :endDate and curDate()                                             "
            + ") art_start                                                                                                  ";
  }
}
