select 
  finalTable.patient_id
from 
  (
    select 
      INH_3HP.patient_id, 
      INH_3HP.dataInicioTPI, 
      INH_3HP.value_coded, 
      INH_3HP.encounter_id, 
      INH_3HP.encounter_id_estado 
    from 
      (
        select 
          * 
        from 
          (
            select 
              allEncounter.patient_id, 
              allEncounter.encounter_datetime dataInicioTPI, 
              obsEstado.value_coded, 
              allEncounter.encounter_id, 
              obsEstado.encounter_id encounter_id_estado 
            from 
              (
                select 
                  allEncounter.patient_id, 
                  allEncounter.encounter_datetime, 
                  allEncounter.encounter_id 
                from 
                  (
                    select 
                      p.patient_id, 
                      e.encounter_datetime, 
                      e.encounter_id 
                    from 
                      patient p 
                      inner join encounter e on e.patient_id = p.patient_id 
                    where 
                      e.encounter_type = 6 
                      and e.voided = 0 
                      and p.voided = 0 
                      and e.location_id =:location
                  ) allEncounter 
                where 
                  allEncounter.encounter_id not in (
                    select 
                      e.encounter_id 
                    from 
                      patient p 
                      inner join encounter e on e.patient_id = p.patient_id 
                      inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                      inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id 
                    where 
                      e.encounter_type = 6 
                      and e.voided = 0 
                      and p.voided = 0 
                      and ultimaProfilaxiaIsoniazia.concept_id = 23985 
                      and ultimaProfilaxiaIsoniazia.value_coded in(656, 23954) 
                      and obsEstado.concept_id = 165308 
                      and obsEstado.value_coded = 1256 
                      and e.location_id =:location
                  )
              ) allEncounter 
              left join obs o on o.encounter_id = allEncounter.encounter_id 
              and o.concept_id = 23985 
              and o.value_coded in(656, 23954) 
              left join obs obsEstado on obsEstado.encounter_id = allEncounter.encounter_id 
              and obsEstado.concept_id = 165308 
              and obsEstado.value_coded = 1256 
            union 
            select 
              p.patient_id, 
              e.encounter_datetime dataInicioTPI, 
              obsEstado.value_coded, 
              e.encounter_id, 
              obsEstado.encounter_id encounter_id_estado 
            from 
              patient p 
              inner join encounter e on e.patient_id = p.patient_id 
              inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
              inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id 
            where 
              e.encounter_type = 6 
              and e.voided = 0 
              and p.voided = 0 
              and e.location_id =:location 
              and ultimaProfilaxiaIsoniazia.concept_id = 23985 
              and ultimaProfilaxiaIsoniazia.value_coded in(656, 23954) 
              and obsEstado.concept_id = 165308 
              and obsEstado.value_coded = 1256 
              and obsEstado.voided = 0
          ) f 
        order by 
          f.dataInicioTPI, 
          f.value_coded ASC
      ) INH_3HP 
      inner join (
        select 
          f.patient_id, 
          f.initialDate 
        from 
          (
            select 
              preTarv.patient_id, 
              preTarv.initialDate initialDate, 
              1 type 
            FROM 
              (
                select 
                  preTarv.patient_id, 
                  min(preTarv.initialDate) initialDate, 
                  1 type 
                FROM 
                  (
                    SELECT 
                      p.patient_id, 
                      MIN(o.value_datetime) AS initialDate 
                    FROM 
                      patient p 
                      INNER JOIN encounter e ON e.patient_id = p.patient_id 
                      INNER JOIN obs o on o.encounter_id = e.encounter_id 
                    WHERE 
                      e.voided = 0 
                      AND o.voided = 0 
                      AND e.encounter_type = 53 
                      AND e.location_id =:location 
                      AND o.value_datetime IS NOT NULL 
                      AND o.concept_id = 23808 
                      AND o.value_datetime <= :endDate 
                    GROUP BY 
                      p.patient_id 
                    UNION 
                    SELECT 
                      p.patient_id, 
                      min(e.encounter_datetime) AS initialDate 
                    FROM 
                      patient p 
                      INNER JOIN encounter e ON e.patient_id = p.patient_id 
                      INNER JOIN obs o on o.encounter_id = e.encounter_id 
                    WHERE 
                      e.voided = 0 
                      AND o.voided = 0 
                      AND e.encounter_type IN(5, 7) 
                      AND e.location_id =:location 
                      AND e.encounter_datetime <= :endDate 
                    GROUP BY 
                      p.patient_id 
                    UNION 
                    SELECT 
                      pg.patient_id, 
                      min(pg.date_enrolled) AS initialDate 
                    FROM 
                      patient p 
                      INNER JOIN patient_program pg on pg.patient_id = p.patient_id 
                    WHERE 
                      pg.program_id = 1 
                      AND pg.location_id =:location 
                      AND pg.voided = 0 
                      AND pg.date_enrolled <= :endDate 
                    GROUP BY 
                      patient_id
                  ) preTarv 
                group by 
                  preTarv.patient_id
              ) preTarv 
            UNION 
            SELECT 
              tarvFinal.patient_id, 
              tarvFinal.initialDate, 
              2 type 
            FROM 
              (
                SELECT 
                  tarvFinal.patient_id, 
                  tarvFinal.initialDate 
                FROM 
                  (
                    SELECT 
                      p.patient_id, 
                      MIN(o.value_datetime) AS initialDate 
                    FROM 
                      patient p 
                      INNER JOIN encounter e ON e.patient_id = p.patient_id 
                      INNER JOIN obs o on o.encounter_id = e.encounter_id 
                    WHERE 
                      e.voided = 0 
                      AND o.voided = 0 
                      AND e.encounter_type = 53 
                      AND e.location_id =:location 
                      AND o.value_datetime IS NOT NULL 
                      AND o.concept_id = 1190 
                      AND o.value_datetime <= :endDate 
                    GROUP BY 
                      p.patient_id 
                    UNION 
                    SELECT 
                      pg.patient_id, 
                      min(pg.date_enrolled) AS initialDate 
                    FROM 
                      patient p 
                      INNER JOIN patient_program pg on pg.patient_id = p.patient_id 
                    WHERE 
                      pg.program_id = 2 
                      AND pg.location_id =:location 
                      AND pg.voided = 0 
                      AND pg.date_enrolled <= :endDate 
                    GROUP BY 
                      patient_id
                  ) tarvFinal 
                WHERE 
                  tarvFinal.patient_id not in (
                    select 
                      preTarv.patient_id 
                    FROM 
                      (
                        select 
                          preTarv.patient_id, 
                          min(preTarv.initialDate) initialDate 
                        FROM 
                          (
                            SELECT 
                              p.patient_id, 
                              MIN(o.value_datetime) AS initialDate 
                            FROM 
                              patient p 
                              INNER JOIN encounter e ON e.patient_id = p.patient_id 
                              INNER JOIN obs o on o.encounter_id = e.encounter_id 
                            WHERE 
                              e.voided = 0 
                              AND o.voided = 0 
                              AND e.encounter_type = 53 
                              AND e.location_id =:location 
                              AND o.value_datetime IS NOT NULL 
                              AND o.concept_id = 23808 
                              AND o.value_datetime <= :endDate 
                            GROUP BY 
                              p.patient_id 
                            UNION 
                            SELECT 
                              p.patient_id, 
                              min(e.encounter_datetime) AS initialDate 
                            FROM 
                              patient p 
                              INNER JOIN encounter e ON e.patient_id = p.patient_id 
                              INNER JOIN obs o on o.encounter_id = e.encounter_id 
                            WHERE 
                              e.voided = 0 
                              AND o.voided = 0 
                              AND e.encounter_type IN(5, 7) 
                              AND e.location_id =:location 
                              AND e.encounter_datetime <= :endDate 
                            GROUP BY 
                              p.patient_id 
                            UNION 
                            SELECT 
                              pg.patient_id, 
                              min(pg.date_enrolled) AS initialDate 
                            FROM 
                              patient p 
                              INNER JOIN patient_program pg on pg.patient_id = p.patient_id 
                            WHERE 
                              pg.program_id = 1 
                              AND pg.location_id =:location 
                              AND pg.voided = 0 
                              AND pg.date_enrolled <= :endDate 
                            GROUP BY 
                              patient_id
                          ) preTarv 
                        GROUP BY 
                          preTarv.patient_id
                      ) preTarv 
                    group by 
                      patient_id
                  )
              ) tarvFinal
          ) f 
        WHERE 
          f.initialDate BETWEEN (:startDate - INTERVAL 1 MONTH) 
          and :endDate
      ) preTarv on preTarv.patient_id = INH_3HP.patient_id 
    where 
      INH_3HP.dataInicioTPI >= preTarv.initialDate 
      and preTarv.initialDate between (:startDate - INTERVAL 1 MONTH) 
      and :endDate 
      and INH_3HP.dataInicioTPI <= :endDate 
      and INH_3HP.value_coded is not null 
  ) finalTable 
  inner join (
    select 
      * 
    from 
      (
        Select 
          firstFC.patient_id, 
          firstFC.encounter_datetime, 
          firstFC.encounter_id 
        from 
          (
            select 
              p.patient_id, 
              min(e.encounter_datetime) encounter_datetime, 
              e.encounter_id 
            from 
              patient p 
              inner join encounter e on e.patient_id = p.patient_id 
            where 
              e.encounter_type = 6 
              and e.voided = 0 
              and p.voided = 0 
              and e.encounter_datetime BETWEEN (:startDate - INTERVAL 1 MONTH) 
              and :endDate 
              and e.location_id =:location 
            group by 
              p.patient_id
          ) firstFC 
        union 
        Select 
          e.patient_id, 
          min(e.encounter_datetime), 
          e.encounter_id 
        from 
          (
            select 
              p.patient_id, 
              min(e.encounter_datetime) encounter_datetime, 
              e.encounter_id 
            from 
              patient p 
              inner join encounter e on e.patient_id = p.patient_id 
            where 
              e.encounter_type = 6 
              and e.voided = 0 
              and p.voided = 0 
              and e.encounter_datetime BETWEEN (:startDate - INTERVAL 1 MONTH) 
              and :endDate 
              and e.location_id =:location 
            group by 
              p.patient_id
          ) firstFC 
          inner join encounter e on e.patient_id = firstFC.patient_id 
          and e.encounter_type = 6 
          and e.voided = 0 
          and e.encounter_datetime > firstFC.encounter_datetime 
        group by 
          e.patient_id
      ) f 
    order by 
      f.patient_id, 
      f.encounter_datetime
  ) f2 on f2.patient_id = finalTable.patient_id 
where 
  f2.encounter_datetime = finalTable.dataInicioTPI
