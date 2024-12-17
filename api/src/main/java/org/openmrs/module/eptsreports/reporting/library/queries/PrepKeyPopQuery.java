package org.openmrs.module.eptsreports.reporting.library.queries;

public class PrepKeyPopQuery {

  public static final String findPatientsWhoAreKeyPop(final KeyPopType keyPopType) {

    String query =
        "        select patient_id from ( "
            + "            select * from (select * from  ( "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,if(o.value_coded=20454,1,5) ordemKp from "
            + "            (Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=81 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0 and e.encounter_type=81 and e.voided=0 and e.location_id=:location and o.value_coded in (20454,20426) "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,2 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=81 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0  and e.encounter_type=81 and e.voided=0 and e.location_id=:location "
            + "            AND o.value_coded=1901 "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,3 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=81 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0 and o.value_coded=165205 and e.encounter_type=81 and e.voided=0 and e.location_id=:location "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,4 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=81 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=23703 and o.voided=0 and e.encounter_type=81 and e.voided=0 and e.location_id=:location  and pe.voided=0 "
            + "            and o.value_coded=1377 and pe.gender = 'M' "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,if(o.value_coded=20454,1,5) ordemKp from  ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type = 80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0 and e.encounter_type = 80 and e.voided=0 and e.location_id=:location and o.value_coded in (20454,20426) "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,2 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type = 80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0 and e.encounter_type = 80 and e.voided=0 and e.location_id=:location "
            + "            AND o.value_coded=1901  "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,3 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type = 80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            where o.concept_id=23703 and o.voided=0 and o.value_coded=165205 and e.encounter_type = 80 and e.voided=0 and e.location_id=:location "
            + "            union "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,4 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type = 80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=23703 and o.voided=0 and e.encounter_type = 80 and e.voided=0 and e.location_id=:location "
            + "            and pe.voided=0 and o.value_coded=1377 and pe.gender = 'M' "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,6 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=1995 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,7 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=1903 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,8 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=1908 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,9 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=1902 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,10 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=6332 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,11 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=1982 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,12 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=165287 "
            + "            UNION "
            + "            select maxkp.patient_id, o.value_coded,o.obs_datetime,1 ordemSource,13 ordemKp from ( "
            + "            Select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on e.encounter_id=o.encounter_id "
            + "            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) maxkp "
            + "            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime "
            + "            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime "
            + "            inner join person pe on pe.person_id=maxkp.patient_id "
            + "            where o.concept_id=23703 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "            AND o.value_coded=5622 "
            + "            ) allkpsource "
            + "            order by patient_id,obs_datetime desc,ordemSource,ordemKp "
            + "            ) allkpsorcetakefirst "
            + "            group by patient_id "
            + "            ) finalkptable ";

    switch (keyPopType) {
      case HOMOSEXUAL:
        query = query + "where value_coded=1377 ";
        break;

      case PRISIONER:
        query = query + "where value_coded=20426 ";
        break;

      case SEXWORKER:
        query = query + "where value_coded=1901 ";
        break;

      case DRUGUSER:
        query = query + "where value_coded=20454 ";
        break;

      case TRANSGENDER:
        query = query + "where value_coded=165205 ";
        break;

      case SERODISCORDANTS:
        query = query + "where value_coded=1995 ";
        break;

      case TRUCKER:
        query = query + "where value_coded=1903 ";
        break;

      case MINER:
        query = query + "where value_coded=1908 ";
        break;

      case MILITARY:
        query = query + "where value_coded=1902 ";
        break;

      case BREASTFEEDING:
        query = query + "where value_coded=6332 ";
        break;

      case PREGNANT:
        query = query + "where value_coded=1982 ";
        break;

      case YOUTHS:
        query = query + "where value_coded=165287 ";
        break;

      case OTHER:
        query = query + "where value_coded=5622 ";
        break;
    }

    return query;
  }
}
