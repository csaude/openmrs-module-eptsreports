   select patient_id from (
         select preTarv.patient_id, preTarv.initialDate, 1 type  FROM 
         ( 
            SELECT p.patient_id,MIN(o.value_datetime) AS initialDate FROM patient p   
            INNER JOIN encounter e  ON e.patient_id=p.patient_id  
            INNER JOIN obs o on o.encounter_id=e.encounter_id  
            WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53 AND e.location_id=:location AND o.value_datetime IS NOT NULL 
            AND o.concept_id=23808 AND o.value_datetime<:startDate  
            GROUP BY p.patient_id  
            UNION 
            SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p   
            INNER JOIN encounter e  ON e.patient_id=p.patient_id  
            INNER JOIN obs o on o.encounter_id=e.encounter_id  
            WHERE e.voided=0 AND o.voided=0 AND e.encounter_type IN(5,7)  
            AND e.location_id=:location  AND e.encounter_datetime<:startDate  
            GROUP BY p.patient_id  
            UNION  
            SELECT pg.patient_id, min(pg.date_enrolled)  AS initialDate FROM patient p  
            INNER JOIN patient_program pg on pg.patient_id=p.patient_id  
            WHERE pg.program_id=1 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<:startDate  
            GROUP BY patient_id 
            )preTarv             

            UNION

            SELECT tarvFinal.patient_id,tarvFinal.initialDate,2 type FROM
            (

            SELECT tarvFinal.patient_id,tarvFinal.initialDate FROM 
            (
            SELECT p.patient_id,MIN(o.value_datetime) AS initialDate FROM patient p   
            INNER JOIN encounter e  ON e.patient_id=p.patient_id  
            INNER JOIN obs o on o.encounter_id=e.encounter_id  
            WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53 AND e.location_id=:location AND o.value_datetime IS NOT NULL 
            AND o.concept_id=1190 AND o.value_datetime<:startDate  
            GROUP BY p.patient_id  
            UNION 
            SELECT pg.patient_id, min(pg.date_enrolled)  AS initialDate FROM patient p  
            INNER JOIN patient_program pg on pg.patient_id=p.patient_id  
            WHERE pg.program_id=2 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<:startDate 
            GROUP BY patient_id 
            
            )tarvFinal

            WHERE tarvFinal.patient_id  not in 
            (
               select preTarv.patient_id  FROM 
            ( 
               SELECT p.patient_id,MIN(o.value_datetime) AS initialDate FROM patient p   
               INNER JOIN encounter e  ON e.patient_id=p.patient_id  
               INNER JOIN obs o on o.encounter_id=e.encounter_id  
               WHERE e.voided=0 AND o.voided=0 AND e.encounter_type=53 AND e.location_id=:location AND o.value_datetime IS NOT NULL 
               AND o.concept_id=23808 AND o.value_datetime<:startDate  
               GROUP BY p.patient_id  
               UNION 
               SELECT p.patient_id,min(e.encounter_datetime) AS initialDate FROM patient p   
               INNER JOIN encounter e  ON e.patient_id=p.patient_id  
               INNER JOIN obs o on o.encounter_id=e.encounter_id  
               WHERE e.voided=0 AND o.voided=0 AND e.encounter_type IN(5,7)  
               AND e.location_id=:location  AND e.encounter_datetime<:startDate  
               GROUP BY p.patient_id  
               UNION  
               SELECT pg.patient_id, min(pg.date_enrolled)  AS initialDate FROM patient p  
               INNER JOIN patient_program pg on pg.patient_id=p.patient_id  
               WHERE pg.program_id=1 AND pg.location_id=:location AND pg.voided=0 AND pg.date_enrolled<:startDate  
               GROUP BY patient_id 
               )preTarv                
               )
            )tarvFinal
            )f