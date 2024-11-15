          select f.* from 
    (
          select 
           coorte12Meses.patient_id,
           DATE_FORMAT(DATE(coorte12Meses.art_start_date),'%d/%m/%Y') art_start_date,
           concat(coorte12Meses.tipo_coorte,' ','meses') as tipo_coorte,
           p.gender as sexo, 
           floor(datediff(coorte12Meses.art_start_date,p.birthdate)/365) as idade,
         
            case 
             when A.valor1 is not null then A.valor1 
             end as elegibilidade_tpt,
             case 
             when A.valor2 is not null  then A.valor2
             end as data_inicio_tpt,
             case 
             when A.valor3 is not null  then A.valor3
             end as data_resultado_cd4,
             case 
             when A.valor4 is not null  then A.valor4
             end as resultado_cd4_12_meses,
             
                        case
             when variaveisDatasCoorte12.data_primeiro_pedido_cv_12_meses is not null then variaveisDatasCoorte12.data_primeiro_pedido_cv_12_meses 
             end as data_primeiro_pedido_cv_12_meses,
             
             case
             when variaveisDatasCoorte12.data_primeiro_resultado_cv_12_meses is not null then variaveisDatasCoorte12.data_primeiro_resultado_cv_12_meses 
             end as data_primeiro_resultado_cv_12_meses,
             
             case
             when variaveisDeTextoCoorte12.resultado_cv_12_meses is not null then variaveisDeTextoCoorte12.resultado_cv_12_meses 
             end as resultado_cv_12_meses,
             
             case
             when variaveisDeTextoCoorte12.resultado_segundo_cd4_12_meses is not null then variaveisDeTextoCoorte12.resultado_segundo_cd4_12_meses 
             end as resultado_segundo_cd4_12_meses,
             
             case
             when variaveisDeTextoCoorte12.nivel_adesao_12_meses is not null then variaveisDeTextoCoorte12.nivel_adesao_12_meses 
             end as nivel_adesao_12_meses,
             
             case
             when variaveisDeTextoCoorte12.gravida_lactante_12_meses is not null then variaveisDeTextoCoorte12.gravida_lactante_12_meses 
             end as gravida_lactante_12_meses,
             
             case
             when variaveisDeTextoCoorte12.tuberculose_12_meses is not null then variaveisDeTextoCoorte12.tuberculose_12_meses 
             end as tuberculose_12_meses,

             DATE_FORMAT(DATE(variaveisDatasCoorte12.data_registo_primeiro_mdc),'%d/%m/%Y') as data_registo_primeiro_mdc_12_meses,
           
             case
             when variaveisDeTextoCoorte12.mdc_Simtomas_tb_12_meses is not null then variaveisDeTextoCoorte12.mdc_Simtomas_tb_12_meses 
             end as mdc_Simtomas_tb_12_meses,
             
             case
             when variaveisDeTextoCoorte12.tipo_estadio_12_meses is not null then variaveisDeTextoCoorte12.tipo_estadio_12_meses 
             end as tipo_estadio_12_meses,
             
             case
             when variaveisDeTextoCoorte12.mds_consultas_pb_imc_12_meses is not null then variaveisDeTextoCoorte12.mds_consultas_pb_imc_12_meses
             end as mds_consultas_pb_imc_12_meses,


             
           primeiroMds12Meses.INICIO_MDS1 INICIO_MDS1_12_MESES,
           primeiroMds12Meses.INICIO_MDS2 INICIO_MDS2_12_MESES,
           primeiroMds12Meses.INICIO_MDS3 INICIO_MDS3_12_MESES,
           primeiroMds12Meses.INICIO_MDS4 INICIO_MDS4_12_MESES,
           primeiroMds12Meses.INICIO_MDS5 INICIO_MDS5_12_MESES,
           
           DATE_FORMAT(DATE(primeiroMds12Meses.DATA_INICIO_MDS1),'%d/%m/%Y') DATA_INICIO_MDS1_12_MESES,
           DATE_FORMAT(DATE(primeiroMds12Meses.DATA_INICIO_MDS2),'%d/%m/%Y') DATA_INICIO_MDS2_12_MESES,
           DATE_FORMAT(DATE(primeiroMds12Meses.DATA_INICIO_MDS3),'%d/%m/%Y') DATA_INICIO_MDS3_12_MESES,
           DATE_FORMAT(DATE(primeiroMds12Meses.DATA_INICIO_MDS4),'%d/%m/%Y') DATA_INICIO_MDS4_12_MESES,
           DATE_FORMAT(DATE(primeiroMds12Meses.DATA_INICIO_MDS5),'%d/%m/%Y') DATA_INICIO_MDS5_12_MESES,
           
           primeiroMds12Meses.FIM_MDS1 FIM_MDS1_12_MESES,
           primeiroMds12Meses.FIM_MDS2 FIM_MDS2_12_MESES,
           primeiroMds12Meses.FIM_MDS3 FIM_MDS3_12_MESES,
           primeiroMds12Meses.FIM_MDS4 FIM_MDS4_12_MESES,
           primeiroMds12Meses.FIM_MDS5 FIM_MDS5_12_MESES,
          
            DATE_FORMAT(DATE(primeiroMds12Meses.DATA_FIM_MDS1),'%d/%m/%Y') DATA_FIM_MDS1_12_MESES,
            DATE_FORMAT(DATE(primeiroMds12Meses.DATA_FIM_MDS2),'%d/%m/%Y') DATA_FIM_MDS2_12_MESES,
            DATE_FORMAT(DATE(primeiroMds12Meses.DATA_FIM_MDS3),'%d/%m/%Y') DATA_FIM_MDS3_12_MESES,
            DATE_FORMAT(DATE(primeiroMds12Meses.DATA_FIM_MDS4),'%d/%m/%Y') DATA_FIM_MDS4_12_MESES,
            DATE_FORMAT(DATE( primeiroMds12Meses.DATA_FIM_MDS5),'%d/%m/%Y') DATA_FIM_MDS5_12_MESES,
            
            variaveisDeTextoCoorte12.mds_pf_12_meses mds_pf_12_meses,
            variaveisDeTextoCoorte12.mds_tpt_12_meses mds_tpt_12_meses,
            variaveisDeTextoCoorte12.mds_pa mds_pa,
            if(variaveisDeTextoCoorte12.total_fc is not null, variaveisDeTextoCoorte12.total_fc, '0') total_consultas_fc_12_meses,
            if(variaveisDeTextoCoorte12.total_apss is not null, variaveisDeTextoCoorte12.total_apss,'0')  total_consultas_apss_12_meses,
            variaveisDeTextoCoorte12.mds_via mds_via,
            variaveisDeTextoCoorte12.mds_via_positivo mds_via_positivo,
             
             case 
             when variaveisDeTextoCoorte12.tipo_saida = 1  then  'ABANDONO' 
             when variaveisDeTextoCoorte12.tipo_saida = 2  then  'OBITO' 
             when variaveisDeTextoCoorte12.tipo_saida = 3  then  'SUSPENSO' 
             when variaveisDeTextoCoorte12.tipo_saida = 4  then  'TRANSFERIDO PARA' 
             when (variaveisDeTextoCoorte12.activo is not null ) then 'ACTIVO'
             when (variaveisDeTextoCoorte12.tipo_saida <> 1 
              or variaveisDeTextoCoorte12.tipo_saida <> 2 
              or variaveisDeTextoCoorte12.tipo_saida <> 3
              or variaveisDeTextoCoorte12.tipo_saida <> 4
              or variaveisDeTextoCoorte12.activo is null)  then 'N/A' 
              else null  end as estado_permanencia_12_meses

            
        from         
        (
        select coorte12Meses.patient_id,coorte12Meses.art_start_date, 12 tipo_coorte
        from
        (
          SELECT tx_new.patient_id,tx_new.art_start_date
           FROM 
              (
		  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start GROUP BY patient_id 
          ) tx_new WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 24 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
         )coorte12Meses
        left join
        (
             SELECT tr.patient_id, tr.data_transferencia from  
              (
                SELECT p.patient_id, obsData.value_datetime as data_transferencia from patient p  
                INNER JOIN encounter e ON p.patient_id=e.patient_id  
                INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 
                INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 
                WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 and obsData.value_datetime<=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                union
                select final.patient_id,final.minStateDate  as data_transferencia from  
                ( 
                select states.patient_id,states.patient_program_id,min(states.minStateDate) as minStateDate,states.program_id,states.state from  
                ( 
                SELECT p.patient_id, pg.patient_program_id, ps.start_date as minStateDate, pg.program_id, ps.state  FROM patient p   
                inner join patient_program pg on p.patient_id=pg.patient_id  
                inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  
                WHERE pg.voided=0 
                and ps.start_date<=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
                and ps.voided=0   
                and p.voided=0 
                and pg.program_id=2 
                and location_id=:location  
                )states 
                group by states.patient_id 
                order by states.minStateDate asc  
                ) final 
                inner join patient_state ps on ps.patient_program_id=final.patient_program_id  
                where ps.start_date=final.minStateDate and ps.state=29 and ps.voided=0 
        ) tr GROUP BY tr.patient_id  
        )trasferedIn on coorte12Meses.patient_id=trasferedIn.patient_id
        left join
        (
         	SELECT tx_new.patient_id,tx_new.art_start_date
           FROM 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH)
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH)
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start GROUP BY patient_id 
          ) tx_new WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 36 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH)
        )coorte24Meses on coorte24Meses.patient_id=coorte12Meses.patient_id
        left join
         (
       	 SELECT tx_new.patient_id,tx_new.art_start_date
           FROM 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH)
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH)
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start GROUP BY patient_id 
          ) tx_new WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 36 MONTH)
       
         )coorte36Meses on coorte12Meses.patient_id=coorte36Meses.patient_id
         where 
          coorte24Meses.patient_id  is null 
          and coorte36Meses.patient_id is null  
          and trasferedIn.patient_id is null
         
        )coorte12Meses
       inner join person p on p.person_id=coorte12Meses.patient_id
       left join
       (
       	       select final.patient_id, tpt.valor1,tptFinal.valor2,resultadoCd4Inicial.valor3,resultadoDataCd4Inicial.valor4
             from
            (
               SELECT tx_new.patient_id,tx_new.art_start_date
                     FROM 
                        (
                        SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                        ( 
                        SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                        patient p 
                        INNER JOIN encounter e ON p.patient_id=e.patient_id 
                        INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                        WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                        AND e.location_id=:location 
                        GROUP BY p.patient_id 
                        UNION 
                        SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                        patient p 
                        INNER JOIN encounter e ON p.patient_id=e.patient_id 
                        INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                        WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                        AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                        AND e.location_id=:location 
                        GROUP BY p.patient_id 
                        ) 
                        art_start GROUP BY patient_id 
                    ) tx_new 
                     WHERE ((tx_new.art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 24 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)) 
                        OR (tx_new.art_start_date BETWEEN date_sub(date(concat(:year,'-12','-21')), interval 36 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH))
                        OR (tx_new.art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 36 MONTH)))

             ) final
            left join
            (
             select 
                    tpt.patient_id,
                    tpt.tpt.art_start_date,
                    tpt.data_tb as data,
                    if(tpt.data_tb is not null,'Não','Sim') valor1
            from 
            person p
            left join
            (
            SELECT tx_new.patient_id,tpt.data_tb,tx_new.art_start_date
             FROM 
                (
                SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) 
                art_start
                GROUP BY patient_id 
                )tx_new
                left join
                ( 
                select p.patient_id,e.encounter_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and o.voided=0 and  o.concept_id=23761  and o.value_coded=1065 
                union
                select p.patient_id,e.encounter_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and o.voided=0 and   o.concept_id=23758  and o.value_coded=1065 
                union
                select p.patient_id,e.encounter_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and  o.concept_id=1766  and o.value_coded in(1763,1764,1762,1760,23760,1765) 
                union
                select p.patient_id,e.encounter_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and  o.concept_id=1268  and o.value_coded in(1256,1257,1267)
                union
                select p.patient_id,o.obs_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=53 and  e.location_id=:location and e.voided=0 and p.voided=0 and  o.concept_id=42 
                union
                select p.patient_id,e.encounter_datetime data_tb,e.encounter_id from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on o.encounter_id=e.encounter_id 
                where e.encounter_type=6 and  e.location_id=:location and e.voided=0 and p.voided=0 and  o.concept_id=1406  and o.value_coded =42
                )tpt on tpt.patient_id=tx_new.patient_id
                )tpt on tpt.patient_id=p.person_id  and p.voided=0
                where ((tpt.data_tb BETWEEN tpt.art_start_date and date_add(tpt.art_start_date, interval 33 day)) or tpt.data_tb is null) 
            )tpt on tpt.patient_id=final.patient_id

                left join
       
            (
              SELECT tx_new.patient_id,DATE_FORMAT(DATE(min(tptFinal.dataInicioTPI)), '%d/%m/%Y') valor2, 2 tipo2
               FROM 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start
              GROUP BY patient_id 
          ) tx_new
          
          left join 
           (
             select tptFinal.patient_id,tptFinal.dataInicioTPI  from ( 
              select p.patient_id,estadoProfilaxia.obs_datetime dataInicioTPI  
              from patient p  
              inner join encounter e on p.patient_id = e.patient_id  
              inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id  
              inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id 
              where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0   
              and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded in (656,23954,165306) 
              and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256  
              and e.encounter_type in (6,9) and e.location_id=:location 
              union
              select p.patient_id, profilaxiaTpt.obs_datetime dataInicioTPI   
              from patient p   
              inner join encounter e on p.patient_id = e.patient_id   
              inner join obs profilaxiaTpt on profilaxiaTpt.encounter_id = e.encounter_id   
              where p.voided = 0 and e.voided = 0  and profilaxiaTpt.voided = 0     
              and  profilaxiaTpt.concept_id = 1719  and profilaxiaTpt.value_coded=165307
              and e.encounter_type=6 and e.location_id=:location 
              union

              select p.patient_id,estadoProfilaxia.obs_datetime dataInicioTPI  
              from patient p  
              inner join encounter e on p.patient_id = e.patient_id  
              inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id  
              inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id 
              where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0   
              and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded in (23954,656,165305,165306) 
              and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256  
              and e.encounter_type=53 and e.location_id=:location 
              ) tptFinal  
              )tptFinal  on tptFinal.patient_id=tx_new.patient_id
              WHERE ((tx_new.art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 24 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH)) 
                        OR (tx_new.art_start_date BETWEEN date_sub(date(concat(:year,'-12','-21')), interval 36 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 24 MONTH))
                        OR (tx_new.art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and  date_sub(date(concat(:year,'-06','-20')), interval 36 MONTH)))
                        and tptFinal.dataInicioTPI BETWEEN tx_new.art_start_date and date_add(tx_new.art_start_date, interval 33 day)
              GROUP BY tx_new.patient_id 
              )tptFinal on tptFinal.patient_id=final.patient_id

             left join

            (
             SELECT tx_new.patient_id,DATE_FORMAT(DATE(resultadoCd4Inicial.data_resultado_cd4 ), '%d/%m/%Y') valor3, 3 tipo
             FROM 
                (
                SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) 
                art_start
                GROUP BY patient_id 
                )tx_new
              inner join
              (
               select p.patient_id,o.obs_datetime data_resultado_cd4
                from patient p   
               inner join encounter e on p.patient_id = e.patient_id   
               inner join obs o on o.encounter_id = e.encounter_id   
               where p.voided = 0 and e.voided = 0  and o.voided = 0     
               and  o.concept_id = 1695 and o.value_numeric is not null
               and e.encounter_type=6 and e.location_id=:location 
               group by p.patient_id
              )resultadoCd4Inicial on tx_new.patient_id=resultadoCd4Inicial.patient_id
               where resultadoCd4Inicial.data_resultado_cd4 BETWEEN tx_new.art_start_date AND  date_add(tx_new.art_start_date, interval 33 day)
              )resultadoCd4Inicial on resultadoCd4Inicial.patient_id=final.patient_id

          left join
            (
               SELECT tx_new.patient_id,resultadoCd4Inicial.resultado_cd4_12_meses valor4, 4 tipo
               FROM 
                (
                SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) 
                art_start
                GROUP BY patient_id 
                )tx_new
              inner join
              (
               select p.patient_id,o.obs_datetime data_resultado_cd4, o.value_numeric resultado_cd4_12_meses
                from patient p   
               inner join encounter e on p.patient_id = e.patient_id   
               inner join obs o on o.encounter_id = e.encounter_id   
               where p.voided = 0 and e.voided = 0  and o.voided = 0     
               and  o.concept_id = 1695 and o.value_numeric is not null
               and e.encounter_type=6 and e.location_id=:location 
               group by p.patient_id
              )resultadoCd4Inicial on tx_new.patient_id=resultadoCd4Inicial.patient_id
              where resultadoCd4Inicial.data_resultado_cd4 BETWEEN tx_new.art_start_date AND  date_add(tx_new.art_start_date, interval 33 day)
              )resultadoDataCd4Inicial on resultadoDataCd4Inicial.patient_id=final.patient_id
              group by final.patient_id
       )A on A.patient_id=coorte12Meses.patient_id
    
             left join
       (
        select tmp.patient_id, 
                       MAX(CASE WHEN tmp.source = 't1' then tmp.value END) as data_primeiro_pedido_cv_12_meses, 
                       MAX(CASE WHEN tmp.source = 't2' then tmp.value END) as data_primeiro_resultado_cv_12_meses,
                       MAX(CASE WHEN tmp.source = 't11' then tmp.value END) as data_registo_primeiro_mdc
                       from (
                       
               SELECT tx_new.patient_id, DATE_FORMAT(DATE(max(primeiroPedidoCV.data_primeiro_pedido_cv_12_meses)),'%d/%m/%Y') value, 't1' as source
               FROM 
                (
                SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) 
                art_start
                GROUP BY patient_id 
                )tx_new
                inner join
                (
             select p.patient_id,e.encounter_datetime data_primeiro_pedido_cv_12_meses
                from patient p   
                inner join encounter e on p.patient_id = e.patient_id   
                inner join obs o on o.encounter_id = e.encounter_id   
                where p.voided = 0 and e.voided = 0  and o.voided = 0     
                and  o.concept_id = 23722 and o.value_coded=856
                and e.encounter_type=6 and e.location_id=:location 
                )primeiroPedidoCV on primeiroPedidoCV.patient_id=tx_new.patient_id
                where (primeiroPedidoCV.data_primeiro_pedido_cv_12_meses>=tx_new.art_start_date and primeiroPedidoCV.data_primeiro_pedido_cv_12_meses<=date_add(tx_new.art_start_date, interval 12 month))
                 GROUP BY tx_new.patient_id 

                union

          SELECT tx_new.patient_id, DATE_FORMAT(DATE(max(primeiroResultadoPedidoCV.data_primeiro_resultado_cv_12_meses)),'%d/%m/%Y') valor6, 't2' as source
               FROM 
                (
                SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) 
                art_start
                GROUP BY patient_id 
                )tx_new
                inner join
                (
                select p.patient_id,e.encounter_datetime data_primeiro_resultado_cv_12_meses
                from patient p   
                inner join encounter e on p.patient_id = e.patient_id   
                inner join obs o on o.encounter_id = e.encounter_id   
                where p.voided = 0 and e.voided = 0  and o.voided = 0     
                and  o.concept_id in(1305,856)
                and e.encounter_type=6 and e.location_id=:location 
                group by p.patient_id
                )primeiroResultadoPedidoCV on primeiroResultadoPedidoCV.patient_id=tx_new.patient_id
                where (primeiroResultadoPedidoCV.data_primeiro_resultado_cv_12_meses>=tx_new.art_start_date and primeiroResultadoPedidoCV.data_primeiro_resultado_cv_12_meses<=date_add(tx_new.art_start_date, interval 12 month))
                GROUP BY tx_new.patient_id 

                union

                
          SELECT tx_new.patient_id,min(primeiroMdc.data_registo_primeiro_mdc) data_registo_primeiro_mdc, 't11' as source
           FROM 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start GROUP BY patient_id 
          ) tx_new 
          inner join
          (
      
      select primeiroMdc.patient_id,primeiroMdc.data_mdc data_registo_primeiro_mdc from 
           (
          select p.patient_id,e.encounter_datetime as data_mdc  from patient p 
              
              join encounter e on p.patient_id=e.patient_id 
              join obs grupo on grupo.encounter_id=e.encounter_id 
              join obs o on o.encounter_id=e.encounter_id 
              join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
              where  e.encounter_type in(6) 
              and e.location_id=:location 
              and o.concept_id=165174  
              and o.voided=0 
              and grupo.concept_id=165323  
              and grupo.voided=0 
              and obsEstado.concept_id=165322  
              and obsEstado.value_coded in(1256) 
              and obsEstado.voided=0  
              and grupo.voided=0 
              and grupo.obs_id=o.obs_group_id 
              and grupo.obs_id=obsEstado.obs_group_id 
              )primeiroMdc
              )primeiroMdc on primeiroMdc.patient_id=tx_new.patient_id
              WHERE primeiroMdc.data_registo_primeiro_mdc BETWEEN tx_new.art_start_date and date_add(tx_new.art_start_date, interval 12 month)  
              group by primeiroMdc.patient_id

            
                )tmp
                 group by tmp.patient_id
       )variaveisDatasCoorte12 on variaveisDatasCoorte12.patient_id=coorte12Meses.patient_id  
              left join(
          select tmp.patient_id,
                   MAX(CASE WHEN tmp.source = 't3' then tmp.value END) as resultado_cv_12_meses,
                       MAX(CASE WHEN tmp.source = 't4' then tmp.value END) as resultado_segundo_cd4_12_meses,
                       MAX(CASE WHEN tmp.source = 't5' then tmp.value END) as nivel_adesao_12_meses,
                       MAX(CASE WHEN tmp.source = 't6' then tmp.value END) as gravida_lactante_12_meses,
                       MAX(CASE WHEN tmp.source = 't7' then tmp.value END) as tuberculose_12_meses,
                       MAX(CASE WHEN tmp.source = 't8' then tmp.value END) as mdc_Simtomas_tb_12_meses,
                       MAX(CASE WHEN tmp.source = 't9' then tmp.value END) as tipo_estadio_12_meses,
                       MAX(CASE WHEN tmp.source = 't10' then tmp.value END) as mds_consultas_pb_imc_12_meses,
                       MAX(CASE WHEN tmp.source = 't12' then tmp.value END) as mds_pf_12_meses,
                       MAX(CASE WHEN tmp.source = 't13' then tmp.value END) as mds_tpt_12_meses,
                       MAX(CASE WHEN tmp.source = 't14' then tmp.value END) as mds_pa,
                       MAX(CASE WHEN tmp.source = 't15' then tmp.value END) as total_fc,
                   MAX(CASE WHEN tmp.source = 't16' then tmp.value END) as total_apss,
                   MAX(CASE WHEN tmp.source = 't17' then tmp.value END) as mds_via,
                   MAX(CASE WHEN tmp.source = 't18' then tmp.value END) as mds_via_positivo,
                   MAX(CASE WHEN tmp.source = 't19' then tmp.value END) as tipo_saida,
                   MAX(CASE WHEN tmp.source = 't20' then tmp.value END) as activo
                   
                   
                       from (
            SELECT tx_new.patient_id,
            if(cv.comments is not null, CONCAT(cv.resultado_cv_12_meses,' ',cv. comments),cv.resultado_cv_12_meses) value, 't3' as source 
                       FROM 
                          (
                          SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                          ( 
                          SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                          patient p 
                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                          INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                          WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                          AND e.location_id=:location
                          GROUP BY p.patient_id 
                          UNION 
                          SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                          patient p 
                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                          INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                          WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                          AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                          AND e.location_id=:location 
                          GROUP BY p.patient_id 
                          ) 
                          art_start GROUP BY patient_id 
                      ) tx_new 
                      left join
              (
              select * from 
              (
              select p.patient_id,o.obs_datetime  data_primeiro_resultado_cv_12_meses,
              case o.value_coded 
                    when 23814  then 'INDETECTAVEL'
                    when 165331 then 'MENOR QUE'
                    when 1306   then 'NIVEL BAIXO DE DETECÇÃO'
                    when 1304   then 'MA QUALIDADE DA AMOSTRA'
                    when 23905  then 'MENOR QUE 10 COPIAS/ML'
                    when 23906  then 'MENOR QUE 20 COPIAS/ML'
                    when 23907  then 'MENOR QUE 40 COPIAS/ML'
                    when 23908  then 'MENOR QUE 400 COPIAS/ML'
                    when 23904  then 'MENOR QUE 839 COPIAS/ML'
                    when 165331 then CONCAT('MENOR QUE', ' ',o.comments)
                    else null end as resultado_cv_12_meses, o.comments
                    from patient p   
                    inner join encounter e on p.patient_id = e.patient_id   
                    inner join obs o on o.encounter_id = e.encounter_id   
                    where p.voided = 0 and e.voided = 0  and o.voided = 0
                    and  o.concept_id=1305
                    and e.encounter_type=6 and e.location_id=:location 
                    
                    union
                    
                    select p.patient_id,o.obs_datetime data_primeiro_resultado_cv_12_meses, o.value_numeric as resultado_cv_12_meses, o.comments
                    from patient p   
                    inner join encounter e on p.patient_id = e.patient_id   
                    inner join obs o on o.encounter_id = e.encounter_id   
                    where p.voided = 0 and e.voided = 0  and o.voided = 0
                    and  o.concept_id=856
                    and e.encounter_type=6 and e.location_id=:location 
                    ) f order by f.data_primeiro_resultado_cv_12_meses
                    )cv on cv.patient_id=tx_new.patient_id
                    where  cv.data_primeiro_resultado_cv_12_meses>=tx_new.art_start_date and (cv.data_primeiro_resultado_cv_12_meses<=date_add(tx_new.art_start_date, interval 12 month))
                    group by tx_new.patient_id

                    union

                    
            select primeiroCd4.patient_id,
                   segundoCd4.resultado_segundo_cd4_12_meses as valor8, 
                   't4' as source
                   
             from 
             (
               SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
               SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
               patient p 
               INNER JOIN encounter e ON p.patient_id=e.patient_id 
               INNER JOIN obs o ON o.encounter_id=e.encounter_id 
               WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
               AND e.location_id=:location 
               GROUP BY p.patient_id 
               UNION 
               SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
               patient p 
               INNER JOIN encounter e ON p.patient_id=e.patient_id 
               INNER JOIN obs o ON e.encounter_id=o.encounter_id 
               WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
               AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
               AND e.location_id=:location 
               GROUP BY p.patient_id 
               ) tx_new
               group by tx_new.patient_id
             )tx_new
             inner join
             (
                   select p.patient_id,min(o.obs_datetime) data_primeiro_cd4_12_meses, o.value_numeric primeiro_resultado_cd4_12_meses
                     from patient p   
                     inner join encounter e on p.patient_id = e.patient_id   
                     inner join obs o on o.encounter_id = e.encounter_id   
                     where p.voided = 0 and e.voided = 0  and o.voided = 0  
                     and  o.concept_id = 1695 and o.value_numeric is not null
                     and e.encounter_type=6 and e.location_id=:location
                     group by p.patient_id
             )primeiroCd4 on tx_new.patient_id=primeiroCd4.patient_id
                 inner join
                 (
                 select p.patient_id,o.obs_datetime data_segundo_cd4_12_meses, o.value_numeric resultado_segundo_cd4_12_meses
                     from patient p   
                     inner join encounter e on p.patient_id = e.patient_id   
                     inner join obs o on o.encounter_id = e.encounter_id   
                     where p.voided = 0 and e.voided = 0  and o.voided = 0 
                     and  o.concept_id = 1695 and o.value_numeric is not null
                     and e.encounter_type=6 and e.location_id=:location
                 )segundoCd4 on primeiroCd4.patient_id=segundoCd4.patient_id and segundoCd4.data_segundo_cd4_12_meses>primeiroCd4.data_primeiro_cd4_12_meses
                 where segundoCd4.data_segundo_cd4_12_meses BETWEEN date_add(tx_new.art_start_date, interval 33 day) and date_add(tx_new.art_start_date, interval 12 month)
                 group by segundoCd4.patient_id

                 union

                 
              select semAdesao.patient_id,
                         if(maAdesao.encounter_3 is not null,'Não',if(semAdesao.encounter_1=boaAdesao.encounter_2, 'Sim',' ')) valor9,
                         't5' as source
                     from  person p inner join
                     (
                       select tx_new.patient_id,tx_new.art_start_date, totalApss.data_adesao_apss_24_meses_1, count(totalApss.encounter_1) encounter_1
                from 
                (
                      SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                      ( 
                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      UNION 
                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      ) tx_new
                       group by tx_new.patient_id
                      )tx_new
                      inner join
                      (
                      select p.patient_id,e.encounter_datetime as data_adesao_apss_24_meses_1, e.encounter_id encounter_1
                      from patient p   
                      inner join encounter e on p.patient_id = e.patient_id   
                      where p.voided = 0 and e.voided = 0  
                      and e.encounter_type=35 
                      and e.location_id=:location
                      order by p.patient_id,e.encounter_datetime
                      )totalApss on totalApss.patient_id=tx_new.patient_id
                      WHERE totalApss.data_adesao_apss_24_meses_1 BETWEEN  date_add(tx_new.art_start_date, interval 33 day) and date_add(tx_new.art_start_date, interval 3 month) 
                      group by tx_new.patient_id
                      )semAdesao on semAdesao.patient_id=p.person_id
                      left join 
                      (
                         select boaAdesao.patient_id,boaAdesao.data_adesao_apss_24_meses_2, COUNT(boaAdesao.encounter_2) encounter_2
                         from 
                         (
                           select tx_new.patient_id,tx_new.art_start_date, adesaoApss.data_adesao_apss_24_meses_2,adesaoApss.encounter_2  
                          from 
                          (
                          SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                          ( 
                          SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                          patient p 
                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                          INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                          WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                          AND e.location_id=:location 
                          GROUP BY p.patient_id 
                          UNION 
                          SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                          patient p 
                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                          INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                          WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                          AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                          AND e.location_id=:location 
                          GROUP BY p.patient_id 
                          ) tx_new
                           group by tx_new.patient_id
                          )tx_new
                          inner join
                          (
                           select p.patient_id,e.encounter_datetime as data_adesao_apss_24_meses_2, e.encounter_id as encounter_2
                            from patient p   
                            inner join encounter e on p.patient_id = e.patient_id   
                            inner join obs o on o.encounter_id = e.encounter_id   
                            where p.voided = 0 and e.voided = 0  and o.voided = 0  
                            and  o.concept_id=6223   
                            and e.encounter_type=35 
                            and e.location_id=:location
                            and o.value_coded=1383
                            order by p.patient_id,e.encounter_datetime  
                        )adesaoApss on tx_new.patient_id=adesaoApss.patient_id
                             WHERE adesaoApss.data_adesao_apss_24_meses_2 BETWEEN  date_add(tx_new.art_start_date, interval 33 day) and date_add(tx_new.art_start_date, interval 3 month) 
                          )boaAdesao
                          group by boaAdesao.patient_id
                          )boaAdesao on boaAdesao.patient_id=p.person_id
                          left join
                          (
                           select maAdesao.patient_id,maAdesao.data_adesao_apss_24_meses_3, COUNT(maAdesao.encounter_3) encounter_3
                           from 
                           (
                             select tx_new.patient_id,tx_new.art_start_date, adesaoApss.data_adesao_apss_24_meses_3,adesaoApss.encounter_3  
                            from 
                            (
                            SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                            ( 
                            SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                            patient p 
                            INNER JOIN encounter e ON p.patient_id=e.patient_id 
                            INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                            WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                            AND e.location_id=:location 
                            GROUP BY p.patient_id 
                            UNION 
                            SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                            patient p 
                            INNER JOIN encounter e ON p.patient_id=e.patient_id 
                            INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                            WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                            AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                            AND e.location_id=:location 
                            GROUP BY p.patient_id 
                            ) tx_new
                             group by tx_new.patient_id
                            )tx_new
                            inner join
                            (
                             select p.patient_id,e.encounter_datetime as data_adesao_apss_24_meses_3, e.encounter_id as encounter_3
                              from patient p   
                              inner join encounter e on p.patient_id = e.patient_id   
                              inner join obs o on o.encounter_id = e.encounter_id   
                              where p.voided = 0 and e.voided = 0  and o.voided = 0  
                              and  o.concept_id=6223   
                              and e.encounter_type=35 
                              and e.location_id=:location
                              and o.value_coded in(1749,1385)
                              order by p.patient_id,e.encounter_datetime  
                              )adesaoApss on tx_new.patient_id=adesaoApss.patient_id
                               WHERE adesaoApss.data_adesao_apss_24_meses_3 BETWEEN  date_add(tx_new.art_start_date, interval 33 day) and date_add(tx_new.art_start_date, interval 3 month) 
                            )maAdesao
                            group by maAdesao.patient_id
                         )maAdesao on maAdesao.patient_id=p.person_id
                          group by p.person_id 

                          union

                          
                     select tx_new.patient_id,
                     if((gravidaLactante.data_gravida_lactante BETWEEN date_add(tx_new.art_start_date, interval 3 month) and date_add(tx_new.art_start_date, interval 9 month)) and  gravidaLactante.value_coded=1065,'Sim','Não') valor10,
                     't6' as source
                   from 
                   (
                   SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                   ( 
                   SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                   patient p 
                   INNER JOIN encounter e ON p.patient_id=e.patient_id 
                   INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                   WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                   AND e.location_id=:location 
                   GROUP BY p.patient_id 
                   UNION 
                   SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                   patient p 
                   INNER JOIN encounter e ON p.patient_id=e.patient_id 
                   INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                   WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                   AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                   AND e.location_id=:location 
                   GROUP BY p.patient_id 
                   ) tx_new
                    group by tx_new.patient_id
                   )tx_new
                   left join
                     (
                      select p.patient_id,e.encounter_datetime data_gravida_lactante ,o.concept_id,o.value_coded, e.encounter_id 
                         from patient p   
                         inner join encounter e on p.patient_id = e.patient_id   
                         inner join obs o on o.encounter_id = e.encounter_id   
                         where p.voided = 0 and e.voided = 0  and o.voided = 0     
                         and   o.concept_id in(1982,6332)
                         and   e.encounter_type=6 and e.location_id=:location
                         order by p.patient_id,e.encounter_datetime
                     )gravidaLactante on gravidaLactante.patient_id=tx_new.patient_id
                     inner join person p on p.person_id=tx_new.patient_id 
                      where  floor(datediff(tx_new.art_start_date,p.birthdate)/365)>9 
                     and p.gender='F'

                     union

                     
                select  tx_new.patient_id,if(tb.data_tb is not null,'Sim','Não') valor11, 't7' as source 
                from
                (
         
             SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                ( 
                SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                UNION 
                SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                AND e.location_id=:location 
                GROUP BY p.patient_id 
                ) tx_new
                 group by tx_new.patient_id
                )tx_new
                left join
                (
                  select p.patient_id,e.encounter_datetime as data_tb  from patient p 
                  left join encounter e on p.patient_id=e.patient_id 
                  left join obs o on o.encounter_id=e.encounter_id 
               where e.encounter_type in(6) and o.concept_id=23761 and o.value_coded=1065  and e.location_id=:location 
                and e.voided=0 and p.voided=0 and o.voided=0 
            union
              select p.patient_id,e.encounter_datetime as data_tb  from patient p 
              left join encounter e on p.patient_id=e.patient_id 
              left join obs o on o.encounter_id=e.encounter_id 
            where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded in(1256,1257)  and e.location_id=:location 
             and e.voided=0 and p.voided=0 and o.voided=0 
                )tb on tb.patient_id=tx_new.patient_id
                where   tb.data_tb BETWEEN date_add(tx_new.art_start_date, interval 3 month) and date_add(tx_new.art_start_date, interval 9 month)

                union

                
                      select f.patient_id,f.mdc_simtomas_tb_12_meses valor12, 't8' as source from
                    (
                     select p.person_id as patient_id,
                                data_registo_primeiro_mdc data_registo_primeiro_mdc, 
                                count(consultas_tb_12_meses) consultas_tb_12_meses,
                                count(consultas_tb_12_meses_tb) consultas_tb_12_meses_tb,
                                if(data_registo_primeiro_mdc is null,'N/A',
                                if(count(consultas_tb_12_meses)= count(consultas_tb_12_meses_tb),'Sim','Não')) mdc_simtomas_tb_12_meses
                          from 
                          person p

                               left join
                               (
                            select tb12Meses.patient_id,tb12Meses.consultas_tb_12_meses,fc.consultas_tb_12_meses_tb,tb12Meses.encounter_id_1,fc.encounter_id_2,mds.data_registo_primeiro_mdc,tx_new.art_start_date
                               from 
                               (
                                select  p.patient_id,e.encounter_datetime consultas_tb_12_meses,e.encounter_id encounter_id_1 from
                                patient p
                                left join encounter e on p.patient_id=e.patient_id
                                where e.encounter_type=6   and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
                                 order by p.patient_id,e.encounter_datetime
                                ) tb12Meses  
                                left join
                               (
                               select p.patient_id,e.encounter_datetime as consultas_tb_12_meses_tb,e.encounter_id  encounter_id_2 from patient p 
                               inner join encounter e on p.patient_id=e.patient_id 
                               inner join obs o on o.encounter_id=e.encounter_id 
                               where e.encounter_type in(6) and o.concept_id=23758  and e.location_id=:location and o.value_coded in(1065,1066)
                               and e.voided=0 and p.voided=0 and o.voided=0 
                                )fc on fc.consultas_tb_12_meses_tb=tb12Meses.consultas_tb_12_meses and tb12Meses.patient_id=fc.patient_id  
                                left join
                                (
                                select primeiroMdc.patient_id,min(primeiroMdc.data_mdc) data_registo_primeiro_mdc from 
                                (
                                select p.patient_id,e.encounter_datetime as data_mdc  from patient p 
                                    join encounter e on p.patient_id=e.patient_id 
                                    join obs grupo on grupo.encounter_id=e.encounter_id 
                                    join obs o on o.encounter_id=e.encounter_id 
                                    join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                                    where  e.encounter_type in(6) 
                                    and e.location_id=:location
                                    and o.concept_id=165174  
                                    and o.voided=0 
                                    and grupo.concept_id=165323  
                                    and grupo.voided=0 
                                    and obsEstado.concept_id=165322  
                                    and obsEstado.value_coded in(1256) 
                                    and obsEstado.voided=0  
                                    and grupo.voided=0 
                                    and grupo.obs_id=o.obs_group_id 
                                    and grupo.obs_id=obsEstado.obs_group_id 
                                    )primeiroMdc
                                    group by primeiroMdc.patient_id
                                )mds on tb12Meses.patient_id=mds.patient_id and fc.patient_id=mds.patient_id
                                left join
                                (
                                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                    ( 
                                    SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                    WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                    AND e.location_id=:location
                                    GROUP BY p.patient_id 
                                    UNION 
                                    SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                    WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                    AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                    AND e.location_id=:location
                                    GROUP BY p.patient_id 
                                    ) art_start 
                                    GROUP BY patient_id 
                                )tx_new on mds.patient_id=tx_new.patient_id
                               where (consultas_tb_12_meses between mds.data_registo_primeiro_mdc and date_add(tx_new.art_start_date, interval 12 month)) 
                               and (consultas_tb_12_meses_tb between mds.data_registo_primeiro_mdc and date_add(tx_new.art_start_date, interval 12 month))
                               )final on final.patient_id=p.person_id
                               group by p.person_id
                         )f 

                         union

                           select f.patient_id,f.tipo_estadio_12_meses as valor13,'t9' as source from
                          (
                     select mds.patient_id,
                        mds.art_start_date,
                        mds.data_inicio_mds,
                       if(mds.data_inicio_mds is null, 'N/A',
                       if(((estadiamentoClinico.encounter_datetime BETWEEN mds.data_inicio_mds and date_add(mds.data_inicio_mds, interval 12 month)) and (mds.data_inicio_mds BETWEEN mds.art_start_date and date_add(mds.art_start_date, interval 12 month)) and estadiamentoClinico.tipoEstadio=3),'Estadio III',
                       if(((estadiamentoClinico.encounter_datetime BETWEEN mds.data_inicio_mds and date_add(mds.data_inicio_mds, interval 12 month)) and (mds.data_inicio_mds BETWEEN mds.art_start_date and date_add(mds.art_start_date, interval 12 month)) and estadiamentoClinico.tipoEstadio=4),'Estadio IV',''))) tipo_estadio_12_meses
                 from
                          (
                          select tx_new.patient_id,tx_new.art_start_date,min(mds.data_inicio_mds) data_inicio_mds 
                          from  
                          (
                           SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                               ( 
                               SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                               patient p 
                               INNER JOIN encounter e ON p.patient_id=e.patient_id 
                               INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                               WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                               AND e.location_id=:location 
                               GROUP BY p.patient_id 
                               UNION 
                               SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                               patient p 
                               INNER JOIN encounter e ON p.patient_id=e.patient_id 
                               INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                               WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                               AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                               AND e.location_id=:location 
                               GROUP BY p.patient_id 
                               ) 
                               art_start GROUP BY patient_id 
                           )tx_new
                             left join
                                (
                              select p.patient_id,e.encounter_datetime data_inicio_mds
                                   from patient p 
                                   join encounter e on p.patient_id=e.patient_id 
                                   join obs grupo on grupo.encounter_id=e.encounter_id 
                                   join obs o on o.encounter_id=e.encounter_id 
                                   join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                                   where  e.encounter_type in(6) 
                                   and e.location_id=:location 
                                   and o.concept_id=165174  
                                   and o.voided=0 
                                   and grupo.concept_id=165323  
                                   and grupo.voided=0 
                                   and obsEstado.concept_id=165322  
                                   and obsEstado.value_coded in(1256) 
                                   and obsEstado.voided=0  
                                   and grupo.voided=0 
                                   and grupo.obs_id=o.obs_group_id 
                                   and grupo.obs_id=obsEstado.obs_group_id 
                         )mds on mds.patient_id=tx_new.patient_id
                             GROUP BY tx_new.patient_id 
                     )mds
                     left join
                     (
                     select estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime encounter_datetime,estadiamentoClinico.value_coded,estadiamentoClinico.tipoEstadio
                     from( 
                         select estadio4.patient_id,estadio4.encounter_datetime,o.value_coded, 4 as tipoEstadio 
                         from( 
                             select p.patient_id,e.encounter_datetime encounter_datetime 
                             from patient p 
                                 inner join encounter e on p.patient_id=e.patient_id 
                                 inner join obs o on o.encounter_id=e.encounter_id 
                             where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
                         ) estadio4 
                             inner join encounter e on e.patient_id = estadio4.patient_id 
                             inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio4.encounter_datetime 
                         where e.voided = 0 and o.voided = 0 and o.value_coded in (14656, 7180, 6990, 5344, 5340, 1294, 5042, 507, 1570, 60) 
                 
                         union 
                         
                         select estadio3.patient_id, estadio3.encounter_datetime,o.value_coded, 3 as tipoEstadio 
                         from( 
                             select p.patient_id,e.encounter_datetime encounter_datetime 
                             from patient p 
                                 inner join encounter e on p.patient_id=e.patient_id 
                                 inner join obs o on o.encounter_id=e.encounter_id 
                             where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
                         ) estadio3 
                             inner join encounter e on e.patient_id = estadio3.patient_id 
                             inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio3.encounter_datetime 
                         where e.voided = 0 and o.voided = 0 and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334) 
                         )estadiamentoClinico 
                         order by estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime 
                         )estadiamentoClinico on estadiamentoClinico.patient_id=mds.patient_id 
                          group by mds.patient_id order by mds.patient_id,estadiamentoClinico.encounter_datetime,estadiamentoClinico.tipoEstadio 
                          )f  

                          union

                          
                           select f.patient_id,f.consultas_pb_imc valor14, 't10' as source
                       from 
                       (
                          select 
                            p.person_id patient_id, 
                             if((count(consultas_pbimc_12_meses)=count(consultas_pb_imc) and ((consultas_pbimc_12_meses between data_registo_primeiro_mdc and date_add(art_start_date, interval 12 month))
                             and (consultas_pb_imc between data_registo_primeiro_mdc and date_add(art_start_date, interval 12 month)) ) ),'Sim',
                                 if(min(data_registo_primeiro_mdc) is null,'N/A','Não')) consultas_pb_imc  
                            from 
                            person p
                            left join
                            (
                                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                  ( 
                                  SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                  patient p 
                                  INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                  INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                  WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                  AND e.location_id=:location
                                  GROUP BY p.patient_id 
                                  UNION 
                                  SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                  patient p 
                                  INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                  INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                  WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                  AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                  AND e.location_id=:location
                                  GROUP BY p.patient_id 
                                  ) art_start 
                                  GROUP BY patient_id 
                                 )tx_new on tx_new.patient_id=p.person_id
                                 left join
                                 (
                                 select pbImc12Meses.patient_id,pbImc12Meses.consultas_pbimc_12_meses,fc.consultas_pb_imc
                                 from 
                                 (
                                  select  p.patient_id,e.encounter_datetime consultas_pbimc_12_meses from
                                  patient p
                                  left join encounter e on p.patient_id=e.patient_id
                                  where e.encounter_type=6   and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
                                   order by p.patient_id,e.encounter_datetime
                                  ) pbImc12Meses  
                                  left join
                                 (
                                select p.patient_id,e.encounter_datetime consultas_pb_imc,o.concept_id  from 
                                  patient p 
                                  inner join encounter e on p.patient_id=e.patient_id 
                                  left join obs o on e.encounter_id=o.encounter_id and o.concept_id in (1342,1343) and o.voided=0
                                  where e.encounter_type=6   and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0 and o.concept_id is not null
                                  order by p.patient_id
                                  )fc on fc.consultas_pb_imc=pbImc12Meses.consultas_pbimc_12_meses and pbImc12Meses.patient_id=fc.patient_id
                                 )pbImc on pbImc.patient_id=p.person_id
                                 left join
                                 (
                                 select primeiroMdc.patient_id,primeiroMdc.data_mdc data_registo_primeiro_mdc from 
                                  (
                                  select p.patient_id,e.encounter_datetime as data_mdc  from patient p 
                                      join encounter e on p.patient_id=e.patient_id 
                                      join obs grupo on grupo.encounter_id=e.encounter_id 
                                      join obs o on o.encounter_id=e.encounter_id 
                                      join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                                      where  e.encounter_type in(6) 
                                      and e.location_id=:location
                                      and o.concept_id=165174  
                                      and o.voided=0 
                                      and grupo.concept_id=165323  
                                      and grupo.voided=0 
                                      and obsEstado.concept_id=165322  
                                      and obsEstado.value_coded in(1256) 
                                      and obsEstado.voided=0  
                                      and grupo.voided=0 
                                      and grupo.obs_id=o.obs_group_id 
                                      and grupo.obs_id=obsEstado.obs_group_id 
                                      )primeiroMdc
                                      left join
                                      (
                                      SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                      ( 
                                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                      patient p 
                                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                      AND e.location_id=:location
                                      GROUP BY p.patient_id 
                                      UNION 
                                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                      patient p 
                                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                      AND e.location_id=:location
                                      GROUP BY p.patient_id 
                                      ) art_start 
                                      GROUP BY patient_id 
                                      )tx_new on tx_new.patient_id=primeiroMdc.patient_id
                                      --where primeiroMdc.data_mdc  between date_add(tx_new.art_start_date, interval 3 month) and date_add(tx_new.art_start_date, interval 9 month)
                                    )mds on mds.patient_id=p.person_id
                                    group by p.person_id     
                                   )f 

                            union
            select * from (
               select  mds.patient_id,
                    if(mds.data_inicio_mds is null, 'N/A',
                    if((mds.data_inicio_mds BETWEEN mds.art_start_date and date_add(mds.art_start_date, interval 12 month)) and (pf.encounter_datetime BETWEEN mds.data_inicio_mds and date_add(mds.art_start_date, interval 12 month)),'Sim','Não')) mds_pf,
                    't12' as source
                 from
                 (
                 select tx_new.patient_id,tx_new.art_start_date,min(mds.data_inicio_mds) data_inicio_mds from  
                 (
                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                      ( 
                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      UNION 
                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      ) 
                      art_start GROUP BY patient_id 
                  )tx_new
                    left join
                       (
                     select p.patient_id,e.encounter_datetime data_inicio_mds
                          from patient p 
                          join encounter e on p.patient_id=e.patient_id 
                          join obs grupo on grupo.encounter_id=e.encounter_id 
                          join obs o on o.encounter_id=e.encounter_id 
                          join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                          where  e.encounter_type in(6) 
                          and e.location_id=:location 
                          and o.concept_id=165174  
                          and o.voided=0 
                          and grupo.concept_id=165323  
                          and grupo.voided=0 
                          and obsEstado.concept_id=165322  
                          and obsEstado.value_coded in(1256) 
                          and obsEstado.voided=0  
                          and grupo.voided=0 
                          and grupo.obs_id=o.obs_group_id 
                          and grupo.obs_id=obsEstado.obs_group_id 
                )mds on mds.patient_id=tx_new.patient_id
                group by tx_new.patient_id

            )mds
            left join
            (
            select pf.patient_id, pf.encounter_datetime encounter_datetime,pf.value_coded
            from( 
                select pf.patient_id,pf.encounter_datetime,pf.value_coded 
                from( 
                    select p.patient_id,e.encounter_datetime encounter_datetime,o.value_coded 
                    from patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id 
                    where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 
                     and  ((o.concept_id=374 and o.value_coded in (190, 780, 5279, 21928, 5275, 5276, 23714, 23715)) or (o.concept_id=23728 and o.value_text is not null))  
                     and e.location_id=:location 
                ) pf 
                )pf order by pf.patient_id, pf.encounter_datetime 
                )pf on pf.patient_id=mds.patient_id
                group by mds.patient_id order by mds.patient_id,pf.encounter_datetime
                ) mds

                union
               
                        select final.patient_id,
                         if(final.data_inicio_mds is null,'N/A',   
                          if(final.data_inicio_tpt is null,'Não','Sim')) mds_tpt_12_meses,
                          't13' as source from 
                   (
                   select  mds.patient_id,mds.art_start_date,mds.data_inicio_mds, tpt12Meses.data_inicio_tpt

                    from
                    (
                    select tx_new.patient_id,tx_new.art_start_date,min(mds.data_inicio_mds) data_inicio_mds from  
                    (
                     SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                         ( 
                         SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                         patient p 
                         INNER JOIN encounter e ON p.patient_id=e.patient_id 
                         INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                         WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                         AND e.location_id=:location 
                         GROUP BY p.patient_id 
                         UNION 
                         SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                         patient p 
                         INNER JOIN encounter e ON p.patient_id=e.patient_id 
                         INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                         WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                         AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                         AND e.location_id=:location 
                         GROUP BY p.patient_id 
                         ) 
                         art_start GROUP BY patient_id 
                     )tx_new
                       left join
                          (
                        select p.patient_id,e.encounter_datetime data_inicio_mds
                             from patient p 
                             join encounter e on p.patient_id=e.patient_id 
                             join obs grupo on grupo.encounter_id=e.encounter_id 
                             join obs o on o.encounter_id=e.encounter_id 
                             join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                             where  e.encounter_type in(6) 
                             and e.location_id=:location 
                             and o.concept_id=165174  
                             and o.voided=0 
                             and grupo.concept_id=165323  
                             and grupo.voided=0 
                             and obsEstado.concept_id=165322  
                             and obsEstado.value_coded in(1256) 
                             and obsEstado.voided=0  
                             and grupo.voided=0 
                             and grupo.obs_id=o.obs_group_id 
                             and grupo.obs_id=obsEstado.obs_group_id 
                   )mds on mds.patient_id=tx_new.patient_id
                   group by tx_new.patient_id
               )mds
               left join
               (
                     select p.patient_id, obsEstado.obs_datetime data_inicio_tpt from patient p 
                             inner join encounter e on p.patient_id = e.patient_id 
                             inner join obs ultimaProfilaxia on ultimaProfilaxia.encounter_id = e.encounter_id 
                              inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                               where e.encounter_type in(6,9) and  ultimaProfilaxia.concept_id=23985
                               and obsEstado.concept_id=165308 and obsEstado.value_coded in(1256,1257) 
                               and p.voided=0 and e.voided=0 and ultimaProfilaxia.voided=0 and obsEstado.voided=0 
                               and e.location_id=:location
                )tpt12Meses on tpt12Meses.patient_id=mds.patient_id
                )final 
                where ((final.data_inicio_mds BETWEEN final.art_start_date and date_add(final.art_start_date, interval 12 month)) and (final.data_inicio_tpt BETWEEN final.data_inicio_mds and date_add(final.art_start_date, interval 12 month)))
                or final.data_inicio_tpt is null

                union

                  
                 select mds.patient_id,
                         if(mds.data_inicio_mds is null,'N/A',
                         if(((todasConsultas.data_todas_consultas_fc_12_meses BETWEEN mds.data_inicio_mds and date_add(mds.art_start_date, interval 12 month)) 
                         and (todasConsultasPA.data_todas_consultas_fc_12_meses_pa BETWEEN mds.data_inicio_mds and date_add(mds.art_start_date, interval 12 month))
                         and (count(todasConsultas.data_todas_consultas_fc_12_meses)=count(todasConsultasPA.data_todas_consultas_fc_12_meses_pa))),'Sim','Não')) as mds_pa,
                         't14' as source
                 from
                 (
                 select tx_new.patient_id,tx_new.art_start_date,min(mds.data_inicio_mds) data_inicio_mds from  
                 (
                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                      ( 
                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      UNION 
                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      ) 
                      art_start GROUP BY patient_id 
                  )tx_new
                    left join
                       (
                     select p.patient_id,e.encounter_datetime data_inicio_mds
                          from patient p 
                          join encounter e on p.patient_id=e.patient_id 
                          join obs grupo on grupo.encounter_id=e.encounter_id 
                          join obs o on o.encounter_id=e.encounter_id 
                          join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                          where  e.encounter_type in(6) 
                          and e.location_id=:location 
                          and o.concept_id=165174  
                          and o.voided=0 
                          and grupo.concept_id=165323  
                          and grupo.voided=0 
                          and obsEstado.concept_id=165322  
                          and obsEstado.value_coded in(1256) 
                          and obsEstado.voided=0  
                          and grupo.voided=0 
                          and grupo.obs_id=o.obs_group_id 
                          and grupo.obs_id=obsEstado.obs_group_id 
                )mds on mds.patient_id=tx_new.patient_id
                group by tx_new.patient_id
                )mds
                left join
                (
                    select f.patient_id,count(f.data_todas_consultas_fc_12_meses_pa) data_todas_consultas_fc_12_meses_pa from
                     (
                       select p.patient_id,e.encounter_datetime data_todas_consultas_fc_12_meses_pa, o.value_numeric  
                      from 
                      patient p 
                      inner join encounter e on p.patient_id=e.patient_id 
                      inner join obs o on o.encounter_id=e.encounter_id
                      where e.encounter_type=6 and o.concept_id=5085 and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
                      order by p.patient_id
                      )f
                      group by f.patient_id
                )todasConsultasPA on todasConsultasPA.patient_id=mds.patient_id
                left join
                (
                  select p.patient_id,count(e.encounter_datetime) data_todas_consultas_fc_12_meses  
                      from 
                      patient p 
                      inner join encounter e on p.patient_id=e.patient_id 
                      where e.encounter_type=6 and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
                       group by p.patient_id
                       order by p.patient_id
                )todasConsultas on todasConsultas.patient_id=mds.patient_id
                group by mds.patient_id 

                union

                
            SELECT consultas12Meses.patient_id, count(consultas12Meses.todas_consultas_fc_12_meses) total_fc, 't15' as source
             FROM 
              (
              select p.patient_id,e.encounter_datetime todas_consultas_fc_12_meses  from 
              patient p 
              inner join encounter e on p.patient_id=e.patient_id 
              where e.encounter_type=6 and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
              order by p.patient_id
              )consultas12Meses
              inner join 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) tx_new
               GROUP BY patient_id 
             ) tx_new  on consultas12Meses.patient_id=tx_new.patient_id
        where consultas12Meses.todas_consultas_fc_12_meses BETWEEN date_add(tx_new.art_start_date, interval 6 month)  and date_add(tx_new.art_start_date, interval 12 month)
        group by consultas12Meses.patient_id

        union

            SELECT consultas12Meses.patient_id, count(consultas12Meses.todas_consultas_fc_12_meses) total_apss, 't16' as source

           FROM 
              (
              select p.patient_id,e.encounter_datetime todas_consultas_fc_12_meses  from 
              patient p 
              inner join encounter e on p.patient_id=e.patient_id 
              where e.encounter_type=35 and e.location_id=:location and e.voided=0 and p.voided=0 and e.voided=0
              order by p.patient_id
              )consultas12Meses
              inner join 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
              patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
              AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) tx_new
               GROUP BY patient_id 
             ) tx_new  on consultas12Meses.patient_id=tx_new.patient_id
        where consultas12Meses.todas_consultas_fc_12_meses BETWEEN date_add(tx_new.art_start_date, interval 6 month)  and date_add(tx_new.art_start_date, interval 12 month)
        group by consultas12Meses.patient_id

        union

              select tx_new.patient_id,
                        if(p.gender='M','N/A', 
                        if((p.gender='F') and (via.encounter_datetime BETWEEN tx_new.art_start_date and date_add(tx_new.art_start_date, interval 12 month)),'Sim','Não')) mds_via,
                        't17' as source 
                 
                 from
                 (
                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                      ( 
                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      UNION 
                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      ) 
                      art_start GROUP BY patient_id 
                  )tx_new
                left join
                (
                select via.patient_id, via.encounter_datetime encounter_datetime
                from( 
                        select p.patient_id,e.encounter_datetime encounter_datetime 
                        from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                        where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=23722 
                        and  o.value_coded=2094
                        and e.location_id=:location 
                       
                         union
    
                        select p.patient_id,e.encounter_datetime encounter_datetime 
                        from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                        where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=2094 
                        and  o.value_coded in(703,664,2093)
                        and e.location_id=:location 
                    )via order by via.patient_id, via.encounter_datetime 
                   )via on via.patient_id=tx_new.patient_id
                   inner join person p on p.person_id=tx_new.patient_id

                   union

                         select tx_new.patient_id,
                        if(p.gender='M','N/A', 
                        if((p.gender='F') and (viaPositivo.encounter_datetime BETWEEN tx_new.art_start_date and date_add(tx_new.art_start_date, interval 12 month)),'Sim','Não')) mds_via_positivo,
                        't18' as source
                 
                 from
                 (
                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                      ( 
                      SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                      WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      UNION 
                      SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                      patient p 
                      INNER JOIN encounter e ON p.patient_id=e.patient_id 
                      INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                      WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                      AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                      AND e.location_id=:location 
                      GROUP BY p.patient_id 
                      ) 
                      art_start GROUP BY patient_id 
                  )tx_new
                left join
                (
                select via.patient_id, via.encounter_datetime encounter_datetime
                from( 
                        select p.patient_id,e.encounter_datetime encounter_datetime 
                        from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                        where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=2094 
                        and  o.value_coded=703
                        and e.location_id=:location 
                    )via order by via.patient_id, via.encounter_datetime 
                   )viaPositivo on viaPositivo.patient_id=tx_new.patient_id
                   inner join person p on p.person_id=tx_new.patient_id

                   union

                   
       select f.*, 't19' as source  from 
       (
       Select B7.patient_id, 1 tipo_saida  from 
                     (
                      select maxFilaRecepcao.patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 59 day) data_proximo_levantamento60, date_add(tx_new.art_start_date,  interval 12 month) as endDate 
                      from
                      (
                      SELECT tx_new.patient_id,tx_new.art_start_date
                           FROM 
                              (
                              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                              ( 
                              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                              patient p 
                              INNER JOIN encounter e ON p.patient_id=e.patient_id 
                              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                              AND e.location_id=:location 
                              AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                              GROUP BY p.patient_id 
                              UNION 
                              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                              patient p 
                              INNER JOIN encounter e ON p.patient_id=e.patient_id 
                              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                              AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                              AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                              AND e.location_id=:location 
                              GROUP BY p.patient_id 
                              ) 
                              art_start GROUP BY patient_id 
                          ) tx_new --WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and   date_sub(date(concat(:year,'-12','-21')), interval 12 month)
                        )tx_new
                     inner join 
                      (
                      select p.patient_id,o.value_datetime data_levantamento, date_add(o.value_datetime, INTERVAL 30 day)  data_proximo_levantamento 
                      from patient p inner 
                      join encounter e on p.patient_id = e.patient_id 
                      inner join obs o on o.encounter_id = e.encounter_id 
                      inner join obs obsLevantou on obsLevantou.encounter_id= e.encounter_id 
                      where  e.voided = 0 and p.voided = 0 
                      and o.voided = 0 and o.concept_id = 23866 and obsLevantou.concept_id=23865 
                      and obsLevantou.value_coded=1065 and obsLevantou.voided=0 and e.encounter_type=52 and e.location_id=:location 
                      union 
                      select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from 
                      ( 
                      select p.patient_id,e.encounter_datetime as data_levantamento from patient p 
                      inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 
                      and e.location_id=:location 
                      and e.voided=0 and p.voided=0 
                      )fila 
                      inner join obs obs_fila on obs_fila.person_id=fila.patient_id 
                      where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime 
                      ) maxFilaRecepcao on maxFilaRecepcao.patient_id=tx_new.patient_id
                      WHERE maxFilaRecepcao.data_levantamento<=date_add(tx_new.art_start_date,  interval 12 month) 
                      group by patient_id
                      having date_add(max(data_proximo_levantamento), INTERVAL 59 day )< endDate   
                      )B7 
                      where B7.patient_id not in
                      (
                               select tx_new.patient_id 
                               from 
                               (
                               SELECT tx_new.patient_id,tx_new.art_start_date
                                 FROM 
                                    (
                                    SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                    ( 
                                    SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                    WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                    AND e.location_id=:location 
                                    AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    GROUP BY p.patient_id 
                                    UNION 
                                    SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                    WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                    AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                    AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    AND e.location_id=:location 
                                    GROUP BY p.patient_id 
                                    ) 
                                    art_start GROUP BY patient_id 
                                ) tx_new --WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and   date_sub(date(concat(:year,'-12','-21')), interval 12 month)
                            )tx_new
                            inner join
                            ( 
                            select patient_id,max(data_obito) data_obito from  
                            ( 
                            select maxEstado.patient_id,maxEstado.data_obito from  
                            ( 
                            select pg.patient_id,ps.start_date data_obito from patient p 
                            inner join patient_program pg on p.patient_id=pg.patient_id 
                            inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                            where pg.voided=0 and ps.voided=0 and p.voided=0 and 
                            pg.program_id=2 
                            --and ps.start_date<=NOW()  
                            and pg.location_id=:location 
                            group by p.patient_id  
                            ) maxEstado 
                            inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                            inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
                            where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and 
                            ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state in (7,8,10)
                            union 
                            select p.patient_id,o.obs_datetime data_obito from  patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                            where e.voided=0 and p.voided=0 
                            --and o.obs_datetime<=NOW()  
                            and o.voided=0 and o.concept_id=6272 and o.value_coded in(1366,1709,1706) and e.encounter_type=53 and  e.location_id=:location 
                            --group by p.patient_id 
                            union  
                            select p.patient_id,e.encounter_datetime data_obito from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 
                            --and e.encounter_datetime<=NOW() 
                            and o.voided=0 and o.concept_id=6273 and o.value_coded in(1366,1709,1706) and e.encounter_type=6 and  e.location_id=:location 
                            --group by p.patient_id 
                            union  
                            Select person_id,death_date from person p where p.dead=1 
                            --and p.death_date<=NOW()  
                            )transferido 
                            group by patient_id 
                            ) obito on obito.patient_id=tx_new.patient_id
                            inner join 
                            ( 
                            select patient_id,encounter_datetime encounter_datetime from  
                            ( 
                            SELECT tx_new.patient_id,max(lev.encounter_datetime) encounter_datetime,tx_new.art_start_date
                                FROM 
                                 (
                                 SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                 ( 
                                 SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                 WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                 AND e.location_id=:location 
                                 AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 GROUP BY p.patient_id 
                                 UNION 
                                 SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                 WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                 AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                 AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 AND e.location_id=:location 
                                 GROUP BY p.patient_id 
                                 ) art_start GROUP BY patient_id 
                                 ) tx_new
                              inner join 
                              (
                              select p.patient_id,e.encounter_datetime encounter_datetime 
                              from patient p 
                              inner join encounter e on e.patient_id = p.patient_id 
                              where p.voided = 0 
                              and e.voided = 0 
                              and e.location_id=:location 
                              and e.encounter_type=18 
                              )lev on tx_new.patient_id=lev.patient_id
                              where lev.encounter_datetime<=date_add(tx_new.art_start_date, interval 12 month)
                              group by lev.patient_id
                            ) consultaLev 
                            group by patient_id  
                            ) consultaOuARV on obito.patient_id=consultaOuARV.patient_id 
                            where consultaOuARV.encounter_datetime<=obito.data_obito and obito.data_obito <= date_add(tx_new.art_start_date, interval 12 month)  group by obito.patient_id

                      )
                      
                             union

                            
                               select tx_new.patient_id, 2 tipo_saida 
                               from 
                               (
                               SELECT tx_new.patient_id,tx_new.art_start_date
                                 FROM 
                                    (
                                    SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                    ( 
                                    SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                    WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                    AND e.location_id=:location 
                                    AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    GROUP BY p.patient_id 
                                    UNION 
                                    SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                    WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                    AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                    AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    AND e.location_id=:location 
                                    GROUP BY p.patient_id 
                                    ) 
                                    art_start GROUP BY patient_id 
                                ) tx_new --WHERE art_start_date BETWEEN  date_sub(date(concat(:year,'-12','-21')), interval 48 MONTH) and   date_sub(date(concat(:year,'-12','-21')), interval 12 month)
                            )tx_new
                            inner join
                            ( 
                            select patient_id,max(data_obito) data_obito from  
                            ( 
                            select maxEstado.patient_id,maxEstado.data_obito from  
                            ( 
                            select pg.patient_id,ps.start_date data_obito from patient p 
                            inner join patient_program pg on p.patient_id=pg.patient_id 
                            inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                            where pg.voided=0 and ps.voided=0 and p.voided=0 and 
                            pg.program_id=2 
                            --and ps.start_date<=NOW()  
                            and pg.location_id=:location 
                            group by p.patient_id  
                            ) maxEstado 
                            inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                            inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
                            where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and 
                            ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 
                            union 
                            select p.patient_id,o.obs_datetime data_obito from  patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                            where e.voided=0 and p.voided=0 
                            and o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 and  e.location_id=:location 
                            union  
                            select p.patient_id,e.encounter_datetime data_obito from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 
                            and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 and  e.location_id=:location 
                            union  
                            Select person_id,death_date from person p where p.dead=1 
                            )transferido 
                             group by patient_id 
                            ) obito on obito.patient_id=tx_new.patient_id
                            inner join 
                            ( 
                            select patient_id,encounter_datetime encounter_datetime from  
                            ( 
                            SELECT tx_new.patient_id,max(lev.encounter_datetime) encounter_datetime,tx_new.art_start_date
                                FROM 
                                 (
                                 SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                 ( 
                                 SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                 WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                 AND e.location_id=:location 
                                 AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 GROUP BY p.patient_id 
                                 UNION 
                                 SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                 WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                 AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                 AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 AND e.location_id=:location 
                                 GROUP BY p.patient_id 
                                 ) art_start GROUP BY patient_id 
                                 ) tx_new
                              inner join 
                              (
                              select p.patient_id,e.encounter_datetime encounter_datetime 
                              from patient p 
                              inner join encounter e on e.patient_id = p.patient_id 
                              where p.voided = 0 
                              and e.voided = 0 
                              and e.location_id=:location 
                              and e.encounter_type=18 
                              )lev on tx_new.patient_id=lev.patient_id
                              where lev.encounter_datetime<=date_add(tx_new.art_start_date, interval 12 month)
                              group by lev.patient_id
                            ) consultaLev 
                            group by patient_id  
                            ) consultaOuARV on obito.patient_id=consultaOuARV.patient_id 
                            where consultaOuARV.encounter_datetime<=obito.data_obito and obito.data_obito <= date_add(tx_new.art_start_date, interval 12 month)  group by obito.patient_id

                           union
   
                            select tx_new.patient_id,  3 tipo_saida 
                            from 
                            (
                            SELECT tx_new.patient_id,tx_new.art_start_date
                              FROM 
                                 (
                                 SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                 ( 
                                 SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                 WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                 AND e.location_id=:location 
                                 AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 GROUP BY p.patient_id 
                                 UNION 
                                 SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                 patient p 
                                 INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                 INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                 WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                 AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                 AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                 AND e.location_id=:location 
                                 GROUP BY p.patient_id 
                                 ) 
                                 art_start GROUP BY patient_id 
                             ) tx_new 
                           )tx_new
                            inner join
                            ( 
                            select patient_id,max(data_suspencao) data_suspencao from  
                            ( 
                            select maxEstado.patient_id,maxEstado.data_suspencao from 
                            ( 
                            select pg.patient_id,ps.start_date data_suspencao from patient p 
                            inner join patient_program pg on p.patient_id=pg.patient_id 
                            inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                            where pg.voided=0 and ps.voided=0 and p.voided=0 and 
                            pg.program_id=2  
                            and pg.location_id=:location  
                            )maxEstado 
                            inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                            inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and 
                            ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
                            union 
                             select p.patient_id,o.obs_datetime data_suspencao from  patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                            where e.voided=0 and p.voided=0 
                            --and o.obs_datetime<=NOW() 
                            and o.voided=0 and o.concept_id=6272 
                            and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
                            union 
                            select  p.patient_id,e.encounter_datetime data_suspencao from  patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs o on o.encounter_id=e.encounter_id 
                            where  e.voided=0 and p.voided=0 
                            and o.voided=0 and o.concept_id=6273 
                            and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
                            ) suspenso 
                            group by patient_id 
                            ) suspenso1 on suspenso1.patient_id=tx_new.patient_id
                            inner join 
                            ( 
                             SELECT tx_new.patient_id,max(lev.encounter_datetime) encounter_datetime,tx_new.art_start_date
                                 FROM 
                                    (
                                    SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                    ( 
                                    SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                    WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                    AND e.location_id=:location 
                                    AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    GROUP BY p.patient_id 
                                    UNION 
                                    SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                    patient p 
                                    INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                    INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                    WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                    AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                    AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                    AND e.location_id=:location 
                                    GROUP BY p.patient_id 
                                    ) art_start GROUP BY patient_id 
                                    ) tx_new
                                 inner join 
                                 (
                                 select p.patient_id,e.encounter_datetime encounter_datetime 
                                 from patient p 
                                 inner join encounter e on e.patient_id = p.patient_id 
                                 where p.voided = 0 
                                 and e.voided = 0 
                                 and e.location_id=:location 
                                 and e.encounter_type=18 
                                 )lev on tx_new.patient_id=lev.patient_id
                                 where lev.encounter_datetime<=date_add(tx_new.art_start_date, interval 12 month)
                                 group by lev.patient_id
                                   ) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
                                  where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao and suspenso1.data_suspencao <= date_add(tx_new.art_start_date, interval 12 month)  group by suspenso1.patient_id
                                  
                             union
         
                             select transferidopara.patient_id, 4 tipo_saida 
                               from 
                               (
                               select transferidopara.patient_id,transferidopara.data_transferidopara
                               from
                               (
                               select transferido.patient_id,max(transferido.data_transferidopara) data_transferidopara
                                  from
                                  (
                                     select maxEstado.patient_id,maxEstado.data_transferidopara
                                     from
                                     (
                                        select
                                        pg.patient_id,
                                        ps.start_date data_transferidopara
                                        from patient p
                                        inner join patient_program pg on p.patient_id = pg.patient_id
                                        inner join patient_state ps on pg.patient_program_id = ps.patient_program_id
                                        where pg.voided = 0
                                        and ps.voided = 0
                                        and p.voided = 0
                                        and pg.program_id = 2
                                        --and ps.start_date <= NOW()
                                        and pg.location_id=:location                               
                                     )maxEstado
                                     inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id
                                     inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id
                                     where pg2.voided = 0
                                     and ps2.voided = 0
                                     and pg2.program_id = 2
                                     and ps2.start_date = maxEstado.data_transferidopara
                                     and pg2.location_id=:location
                                     and ps2.state = 7
                                     union
                                     select
                                     p.patient_id,o.obs_datetime data_transferidopara
                                     from patient p
                                     inner join encounter e on p.patient_id = e.patient_id
                                     inner join obs o on o.encounter_id = e.encounter_id
                                     where e.voided = 0
                                     and p.voided = 0
                                     and o.voided = 0
                                     and o.concept_id = 6272
                                     and o.value_coded = 1706
                                     and e.encounter_type = 53
                                     and e.location_id=:location
                                     union
                                     select
                                     p.patient_id,
                                     e.encounter_datetime data_transferidopara
                                     from patient p
                                     inner join encounter e on p.patient_id = e.patient_id
                                     inner join obs o on o.encounter_id = e.encounter_id
                                     where e.voided = 0
                                     and p.voided = 0
                                     and o.voided = 0
                                     and o.concept_id = 6273
                                     and o.value_coded = 1706
                                     and e.encounter_type = 6
                                     and e.location_id=:location
                                  )transferido
         
                                  inner join 
                                  (
         
                                  SELECT tx_new.patient_id,tx_new.art_start_date
                                       FROM 
                                          (
                                          SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                          ( 
                                          SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                          patient p 
                                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                          INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                          WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                          AND e.location_id=:location 
                                          AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                          GROUP BY p.patient_id 
                                          UNION 
                                          SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                          patient p 
                                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                          INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                          WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                          AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                          AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                          AND e.location_id=:location 
                                          GROUP BY p.patient_id 
                                          ) art_start GROUP BY patient_id 
                                      ) tx_new 
                                    )tx_new on tx_new.patient_id=transferido.patient_id
                                    where transferido.data_transferidopara <= date_add(tx_new.art_start_date, interval 12 month)
                                     group by transferido.patient_id
                                    )transferidopara
                                    inner join 
                                    (
                                     SELECT tx_new.patient_id,max(lev.encounter_datetime) encounter_datetime,tx_new.art_start_date
                                       FROM 
                                          (
                                          SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                          ( 
                                          SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                          patient p 
                                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                          INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                          WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                          AND e.location_id=:location 
                                          AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                          GROUP BY p.patient_id 
                                          UNION 
                                          SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                          patient p 
                                          INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                          INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                          WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                          AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                          AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                          AND e.location_id=:location 
                                          GROUP BY p.patient_id 
                                          ) art_start GROUP BY patient_id 
                                          ) tx_new
                                       inner join 
                                       (
                                       select p.patient_id,e.encounter_datetime encounter_datetime 
                                       from patient p 
                                       inner join encounter e on e.patient_id = p.patient_id 
                                       where p.voided = 0 
                                       and e.voided = 0 
                                       and e.location_id=:location 
                                       and e.encounter_type=18 
                                       )lev on tx_new.patient_id=lev.patient_id
                                       where lev.encounter_datetime<=date_add(tx_new.art_start_date, interval 30 month)
                                       group by lev.patient_id
                                     )lev where lev.encounter_datetime<=transferidopara.data_transferidopara
                                  )transferidopara
                                  inner join
                                  (
                                    select final.patient_id,max(final.data_lev) as data_lev,final.data_ultimo_levantamento,tx_new.art_start_date from
                                     (
                                        select patient_id, data_lev,data_ultimo_levantamento data_ultimo_levantamento from
                                        (
                                           select ultimo_fila.patient_id,ultimo_fila.data_fila data_lev,date_add(obs_fila.value_datetime , interval 1 day ) data_ultimo_levantamento from
                                           (
                                              select
                                              p.patient_id, encounter_datetime data_fila
                                              from patient p
                                              inner join person pe on pe.person_id = p.patient_id
                                              inner join encounter e on e.patient_id = p.patient_id
                                              where p.voided = 0
                                              and pe.voided = 0
                                              and e.voided = 0
                                              and e.encounter_type = 18
                                              and e.location_id=:location
                                           )ultimo_fila
                                    left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=ultimo_fila.patient_id 
                                     and ultimo_fila_data_criacao.voided=0 
                                     and ultimo_fila_data_criacao.encounter_type = 18 
                                     and date(ultimo_fila_data_criacao.encounter_datetime) = date(ultimo_fila.data_fila) 
                                     and ultimo_fila_data_criacao.location_id=:location 
                                     left join 
                                     obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id 
                                     and obs_fila.voided=0 
                                     and (date(obs_fila.obs_datetime)=date(ultimo_fila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
                                     and obs_fila.concept_id=5096 
                                     and obs_fila.location_id=:location 
                                           union
                                           select p.patient_id,value_datetime data_lev,date_add(value_datetime,interval 31 day ) data_ultimo_levantamento
                                           from patient p
                                           inner join person pe on pe.person_id = p.patient_id
                                           inner join encounter e on p.patient_id = e.patient_id
                                           inner join obs o on e.encounter_id = o.encounter_id
                                           where p.voided = 0
                                           and pe.voided = 0
                                           and e.voided = 0
                                           and o.voided = 0
                                           and e.encounter_type = 52
                                           and o.concept_id = 23866
                                           and o.value_datetime is not null
                                           and e.location_id=:location
                                        )ultimo_levantamento
                                        order by ultimo_levantamento.data_lev desc
                                     )final
                                     inner join
                                 (
                                 SELECT tx_new.patient_id,tx_new.art_start_date
                                                FROM 
                                                   (
                                                   SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                                                   ( 
                                                   SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                                                   patient p 
                                                   INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                                   INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                                                   WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                                                   AND e.location_id=:location 
                                                   AND e.encounter_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                                   GROUP BY p.patient_id 
                                                   UNION 
                                                   SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                                                   patient p 
                                                   INNER JOIN encounter e ON p.patient_id=e.patient_id 
                                                   INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                                                   WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                                                   AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                                                   AND o.value_datetime <=date_sub(date(concat(:year,'-06','-20')), interval 12 month)
                                                   AND e.location_id=:location 
                                                   GROUP BY p.patient_id 
                                                   ) art_start GROUP BY patient_id 
                                               ) tx_new 
                                             )tx_new on tx_new.patient_id=final.patient_id
                                              where final.data_lev <= date_add(tx_new.art_start_date, interval 12 month) 
                                              group by final.patient_id                                       
                                         )final on transferidopara.patient_id=final.patient_id
                                         )f

                                         union
                            
              select coorte12meses_final.patient_id, coorte12meses_final.patient_id as patient_id, 't20' as source
                from( 
                    select inicio_fila_seg_prox.*,
                        data_encountro data_usar_c, 
                          data_proximo_lev data_usar 
                     from( 
                            select inicio_fila_seg.*
                            from( 
                            select inicio.*, 
                                 saida.data_estado, 
                                 max_fila.data_fila,
                                 fila_recepcao.data_proximo_lev,
                                 consulta.data_encountro
                                from( 
                                Select patient_id, min(data_inicio) data_inicio 
                                 from ( 
                                        select f.patient_id, min(f.data_inicio) data_inicio from
                                        (
                                         select e.patient_id, min(e.encounter_datetime) as data_inicio
                                         from patient p
                                             inner join encounter e on p.patient_id=e.patient_id
                                         where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=CURDATE()
                                         and e.location_id=:location and YEAR(STR_TO_DATE(e.encounter_datetime, '%Y-%m-%d')) >= 1000
                                             group by p.patient_id
                                         union
                                         Select p.patient_id,min(value_datetime) data_inicio
                                         from patient p
                                             inner join encounter e on p.patient_id=e.patient_id
                                             inner join obs o on e.encounter_id=o.encounter_id
                                         where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866
                                             and o.value_datetime is not null and  o.value_datetime<=CURDATE() and e.location_id=:location
                                             and YEAR(STR_TO_DATE(o.value_datetime, '%Y-%m-%d')) >= 1000
                                             group by p.patient_id
                                       )f
                                       where f.patient_id is not null and f.data_inicio is not null
                                       group by f.patient_id
                                         ) 
                                         inicio_real group by patient_id 
                                     )inicio 
                                     left join ( 
                                        select saida.patient_id, max(saida.data_estado)data_estado
                                        from(
                                            select saidas_por_suspensao.patient_id, saidas_por_suspensao.data_estado 
                                                    from(
                                                    
                                                    select saidas_por_suspensao.patient_id, max(saidas_por_suspensao.data_estado) data_estado 
                                                        from( 
                                                            
                                                            select maxEstado.patient_id,maxEstado.data_estado data_estado 
                                                            from( 
                                                                
                                                                select pg.patient_id,max(ps.start_date) data_estado 
                                                             from patient p 
                                                                inner join patient_program pg on p.patient_id=pg.patient_id 
                                                                  inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                                                                where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=CURDATE() and pg.location_id=:location 
                                                                group by p.patient_id 
                                                        ) 
                                                      maxEstado 
                                                        inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                                                        inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
                                                    where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
                                                        and ps2.start_date=maxEstado.data_estado and pg2.location_id=:location and ps2.state =8 
                                                                     
                                                    union 
                                                                
                                                        select p.patient_id, max(o.obs_datetime) data_estado 
                                                        from patient p 
                                                        inner join encounter e on p.patient_id=e.patient_id 
                                                            inner join obs  o on e.encounter_id=o.encounter_id 
                                                        where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) 
                                                        and o.value_coded in (1709) and o.obs_datetime<=CURDATE() and e.location_id=:location 
                                                            group by p.patient_id 
                                                  ) saidas_por_suspensao group by saidas_por_suspensao.patient_id
                                               ) saidas_por_suspensao
                                               left join(
                                                
                                                select p.patient_id,max(e.encounter_datetime) encounter_datetime
                                                 from patient p
                                                    inner join encounter e on e.patient_id = p.patient_id
                                                 where p.voided = 0 and e.voided = 0 and e.encounter_datetime <CURDATE() and e.location_id=:location and e.encounter_type=18
                                                  group by p.patient_id
                                            ) fila on saidas_por_suspensao.patient_id = fila.patient_id 
                                            where saidas_por_suspensao.data_estado>=fila.encounter_datetime or fila.encounter_datetime is null
                                             
                                        union
                                            
                                        select dead_state.patient_id, dead_state.data_estado
                                        from(
                                            
                                            select patient_id,max(data_estado) data_estado                                                                                              
                                            from (  
                                                
                                                select distinct max_estado.patient_id, max_estado.data_estado 
                                                from(                                                                
                                                    
                                                    select  pg.patient_id,                                                                                                          
                                                        max(ps.start_date) data_estado                                                                                          
                                                    from patient p                                                                                                               
                                                        inner join person pe on pe.person_id = p.patient_id                                                                         
                                                        inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                                                        inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
                                                    where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
                                                                and ps.start_date<=CURDATE() and pg.location_id=:location group by pg.patient_id                                           
                                                ) max_estado                                                                                                                        
                                                    inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                                                    inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                                                where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location  
                                                
                                                union
                                                
                                                select  p.patient_id,max(o.obs_datetime) data_estado                                                                                              
                                                from patient p                                                                                                                   
                                                    inner join person pe on pe.person_id = p.patient_id                                                                         
                                                    inner join encounter e on p.patient_id=e.patient_id                                                                         
                                                    inner join obs  o on e.encounter_id=o.encounter_id                                                                          
                                                where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
                                                    and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
                                                    and o.obs_datetime<=CURDATE() and e.location_id=:location                                                                        
                                                    group by p.patient_id                                                                                                               
                                                
                                                union                                                                                                                               
                                                
                                                select person_id as patient_id,death_date as data_estado                                                                            
                                                from person                                                                                                                         
                                                where dead=1 and voided = 0 and death_date is not null and death_date<=CURDATE() 
                                                                                                                                                           
                                            ) dead_state group by dead_state.patient_id  
                                    ) dead_state 
                                    left join(
                                        
                                        select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
                                        from(
                                            
                                            select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
                                            from patient p                                                                                                                                   
                                                inner join person pe on pe.person_id = p.patient_id                                                                                         
                                                inner join encounter e on e.patient_id=p.patient_id                                                                                         
                                            where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
                                                and e.location_id=:location and e.encounter_datetime<=CURDATE()                                                                                  
                                                group by p.patient_id  
                                            
                                            union
                                                    
                                            select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
                                            from patient p                                                                                                                                   
                                                inner join person pe on pe.person_id = p.patient_id                                                                                         
                                                inner join encounter e on e.patient_id=p.patient_id                                                                                         
                                            where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
                                                and e.location_id=:location and e.encounter_datetime<=CURDATE()                                                                                  
                                                group by p.patient_id   
                                        ) fila_seguimento group by fila_seguimento.patient_id  
                                 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
                                    where fila_seguimento.data_encountro <= dead_state.data_estado or fila_seguimento.data_encountro is null
                    
                                union
                    
                                    select saidas_por_transferencia.patient_id,ultimo_levantamento.data_ultimo_levantamento data_estado
                                from (
                                        
                                        select saidas_por_transferencia.patient_id, saidas_por_transferencia.data_estado
                                        from (
                                           
                                           select saidas_por_transferencia.patient_id, max(data_estado) data_estado
                                           from(
                                                select distinct max_estado.patient_id, max_estado.data_estado 
                                                from(                                                                
                                                         select pg.patient_id, max(ps.start_date) data_estado                                                                                          
                                                         from patient p                                                                                                               
                                                                inner join person pe on pe.person_id = p.patient_id                                                                         
                                                                inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                                                                inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                           
                                                         where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                    
                                                                and ps.start_date< CURDATE() and pg.location_id=:location group by pg.patient_id                                          
                                                    ) max_estado                                                                                                                        
                                                    inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                                                        inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                                                    where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location                 
                                                 
                                                 union                                                                                                                               
                                                        
                                                    select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
                                                    from patient p                                                                                                                   
                                                    inner join person pe on pe.person_id = p.patient_id                                                                         
                                                        inner join encounter e on p.patient_id=e.patient_id                                                                         
                                                        inner join obs  o on e.encounter_id=o.encounter_id                                                                          
                                                    where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                    
                                                    and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
                                                        and o.obs_datetime<=CURDATE() and e.location_id=:location                                                                        
                                                        group by p.patient_id                                                                                                               
                    
                                              ) saidas_por_transferencia group by saidas_por_transferencia.patient_id
                                         ) saidas_por_transferencia
                                           left join (
                                              
                                              select p.patient_id,max(e.encounter_datetime) encounter_datetime
                                              from patient p
                                                inner join encounter e on e.patient_id = p.patient_id
                                              where p.voided = 0 and e.voided = 0 and e.encounter_datetime < CURDATE() and e.location_id=:location and e.encounter_type=18
                                                group by p.patient_id
                                            )lev on saidas_por_transferencia.patient_id=lev.patient_id
                                        where lev.encounter_datetime<=saidas_por_transferencia.data_estado or lev.encounter_datetime is null
                                            group by saidas_por_transferencia.patient_id 
                               ) saidas_por_transferencia
                               inner join (  
                                    
                                    select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
                                        from (
                         
                                            select maxFila.patient_id, date_add(max(obs_fila.value_datetime), interval 1 day) data_ultimo_levantamento 
                                            from ( 
                                                
                                                select fila.patient_id,fila.data_fila data_fila,e.encounter_id 
                                                from( 
                                                  
                                                   select p.patient_id,max(encounter_datetime) data_fila  
                                                   from patient p 
                                                          inner join encounter e on e.patient_id=p.patient_id 
                                                   where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=CURDATE() 
                                                         group by p.patient_id 
                                                  )fila 
                                                   inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
                                            )maxFila  
                                          left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=maxFila.patient_id 
                                            and ultimo_fila_data_criacao.voided=0 
                                            and ultimo_fila_data_criacao.encounter_type = 18 
                                            and date(ultimo_fila_data_criacao.encounter_datetime) = date(maxFila.data_fila) 
                                            and ultimo_fila_data_criacao.location_id=:location 
                                           left join obs obs_fila on obs_fila.person_id=maxFila.patient_id 
                                            and obs_fila.voided=0 
                                            and (date(obs_fila.obs_datetime)=date(maxFila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
                                            and obs_fila.concept_id=5096 
                                            and obs_fila.location_id=:location 
                                                group by maxFila.patient_id 
                    
                                            union
                    
                                            select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
                                            from patient p                                                                                                                                   
                                                inner join person pe on pe.person_id = p.patient_id                                                                                         
                                                 inner join encounter e on p.patient_id=e.patient_id                                                                                         
                                                 inner join obs o on e.encounter_id=o.encounter_id                                                                                           
                                            where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
                                                 and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime<=CURDATE()                                                                                        
                                            group by p.patient_id
                                       ) ultimo_levantamento group by patient_id
                                ) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
                                    where (ultimo_levantamento.data_ultimo_levantamento <= CURDATE()    and saidas_por_transferencia.data_estado<=CURDATE())
                             ) saida group by saida.patient_id
                           ) saida on inicio.patient_id=saida.patient_id 
                                    
                           left join 
                           ( 
                            select patient_id, data_fila_or_segu_or_recepcao, max(data_proximo_lev) data_proximo_lev from 
                            (
                               select maxFila.patient_id, maxFila.data_fila data_fila_or_segu_or_recepcao, max(obs_fila.value_datetime) data_proximo_lev from 
                               ( 
                                    select fila.patient_id,fila.data_fila data_fila,e.encounter_id from 
                                    ( 
                                    Select p.patient_id,max(encounter_datetime) data_fila from patient p 
                                    inner join encounter e on e.patient_id=p.patient_id 
                                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                    and date(e.encounter_datetime)<=CURDATE() 
                                    group by p.patient_id 
                                    )fila 
                                    inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
                               )maxFila 
                              left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=maxFila.patient_id 
                                    and ultimo_fila_data_criacao.voided=0 
                                    and ultimo_fila_data_criacao.encounter_type = 18 
                                    and date(ultimo_fila_data_criacao.encounter_datetime) = date(maxFila.data_fila) 
                                    and ultimo_fila_data_criacao.location_id=:location 
                                    left join 
                                    obs obs_fila on obs_fila.person_id=maxFila.patient_id 
                                    and obs_fila.voided=0 
                                    and (date(obs_fila.obs_datetime)=date(maxFila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
                                    and obs_fila.concept_id=5096 
                                    and obs_fila.location_id=:location 
                                    group by maxFila.patient_id 
                
                             union
                            select patient_id,  dat_fila, date_add(dat_fila, interval 30 day) data_proximo_lev from (
                               Select p.patient_id, max(value_datetime) as  dat_fila
                               from patient p 
                                   inner join encounter e on p.patient_id=e.patient_id 
                                   inner join obs o on e.encounter_id=o.encounter_id 
                               where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 
                                   and o.value_datetime is not null and  o.value_datetime<=CURDATE() and e.location_id=:location 
                                   group by p.patient_id 
                                   )fila_recepcao
                                   ) fila_recepcao group by fila_recepcao.patient_id
                          
                          )fila_recepcao on fila_recepcao.patient_id=inicio.patient_id
                
                           left join
                           (
                            select patient_id, max(data_encountro) data_encountro from 
                            (
                
                               Select p.patient_id,max(encounter_datetime) data_encountro
                               from patient p 
                                   inner join encounter e on e.patient_id=p.patient_id 
                               where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=CURDATE() 
                                   group by p.patient_id 
                                union
                
                
                                Select p.patient_id,max(encounter_datetime) data_encountro 
                                from patient p 
                                    inner join encounter e on e.patient_id=p.patient_id 
                                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                    and date(e.encounter_datetime)<=CURDATE() 
                                    group by p.patient_id 
                                    ) consulta group by consulta.patient_id
                           )consulta on consulta.patient_id=inicio.patient_id
                           left join
                               (
                            Select p.patient_id,max(encounter_datetime) data_fila from patient p 
                                    inner join encounter e on e.patient_id=p.patient_id 
                                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                    and date(e.encounter_datetime)<=CURDATE() 
                                    group by p.patient_id 
                            )max_fila on max_fila.patient_id=inicio.patient_id
                
                ) inicio_fila_seg 
                
                     ) inicio_fila_seg_prox 
                         group by patient_id 
                    ) coorte12meses_final 
                 WHERE  ((data_estado is null or ((data_estado is not null) and  data_fila>data_estado))  and date_add(data_usar, interval 59 day) >=date_add(coorte12meses_final.data_inicio, interval 12 month)) 

                                           )tmp
                 group by tmp.patient_id

       ) variaveisDeTextoCoorte12 on variaveisDeTextoCoorte12.patient_id=coorte12Meses.patient_id
       
       left join

       (
    select inicioFimTxNew.patient_id,
                    inicioFimTxNew.data_inicio_mds,
                    @num_first_mds := 1 + LENGTH(firstMds) - LENGTH(REPLACE(inicioFimTxNew.firstMds, ',', '')) AS num_first_mds ,
                   SUBSTRING_INDEX(inicioFimTxNew.firstMds, ',', 1) AS INICIO_MDS1,
                   IF(@num_first_mds > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.firstMds, ',', 2), ',', -1), '') AS INICIO_MDS2,
                   IF(@num_first_mds > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.firstMds, ',', 3), ',', -1), '') AS INICIO_MDS3, 
                   IF(@num_first_mds > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.firstMds, ',', 4), ',', -1), '') AS INICIO_MDS4,
                   IF(@num_first_mds > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.firstMds, ',', 5), ',', -1), '') AS INICIO_MDS5,
                  
                   @num_datas := 1 + LENGTH(data_inicio_mds) - LENGTH(REPLACE(inicioFimTxNew.data_inicio_mds, ',', '')) AS num_datas ,
                   SUBSTRING_INDEX(inicioFimTxNew.data_inicio_mds, ',', 1) AS DATA_INICIO_MDS1,
                   IF(@num_datas > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_inicio_mds, ',', 2), ',', -1), '') AS DATA_INICIO_MDS2,
                   IF(@num_datas > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_inicio_mds, ',', 3), ',', -1), '') AS DATA_INICIO_MDS3, 
                   IF(@num_datas > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_inicio_mds, ',', 4), ',', -1), '') AS DATA_INICIO_MDS4,
                   IF(@num_datas > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_inicio_mds, ',', 5), ',', -1), '') AS DATA_INICIO_MDS5,
      
                   @num_first_mds_fim := 1 + LENGTH(firstMds) - LENGTH(REPLACE(inicioFimTxNew.lastMds, ',', '')) AS num_first_mds_fim ,
                   SUBSTRING_INDEX(inicioFimTxNew.lastMds, ',', 1) AS FIM_MDS1,      
                   IF(@num_first_mds_fim > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.lastMds, ',', 2), ',', -1), '') AS FIM_MDS2,
                   IF(@num_first_mds_fim > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.lastMds, ',', 3), ',', -1), '') AS FIM_MDS3, 
                   IF(@num_first_mds_fim > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.lastMds, ',', 4), ',', -1), '') AS FIM_MDS4,
                   IF(@num_first_mds_fim > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.lastMds, ',', 5), ',', -1), '') AS FIM_MDS5,
                   data_fim_mds,
                   @num_datas_fim := 1 + LENGTH(data_fim_mds) - LENGTH(REPLACE(inicioFimTxNew.data_fim_mds, ',', '')) AS num_datas_fim ,
                   SUBSTRING_INDEX(inicioFimTxNew.data_fim_mds, ',', 1) AS DATA_FIM_MDS1,
                   IF(@num_datas_fim > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_fim_mds, ',', 2), ',', -1), '') AS DATA_FIM_MDS2,
                   IF(@num_datas_fim > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_fim_mds, ',', 3), ',', -1), '') AS DATA_FIM_MDS3, 
                   IF(@num_datas_fim > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_fim_mds, ',', 4), ',', -1), '') AS DATA_FIM_MDS4,
                   IF(@num_datas_fim > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(inicioFimTxNew.data_fim_mds, ',', 5), ',', -1), '') AS DATA_FIM_MDS5,
                   12 tipo_coorte

                 from (
            select inicioFimTxNew.patient_id,group_concat(inicioFimTxNew.data_inicio_mds ORDER BY inicioFimTxNew.data_inicio_mds ASC) data_inicio_mds, group_concat(inicioFimTxNew.firstMds ORDER BY inicioFimTxNew.data_inicio_mds) firstMds,group_concat(inicioFimTxNew.data_fim_mds ORDER BY inicioFimTxNew.data_fim_mds) data_fim_mds ,group_concat(inicioFimTxNew.lastMds ORDER BY inicioFimTxNew.data_fim_mds) lastMds 
            from 
            (
            select inicioFimTxNew.patient_id,inicioFimTxNew.data_inicio_mds,inicioFimTxNew.firstMds,if(inicioFimTxNew.data_fim_mds is null,null,inicioFimTxNew.data_fim_mds ) data_fim_mds,if(inicioFimTxNew.lastMds is null, null,inicioFimTxNew.lastMds) lastMds 
            from 
            (
            select inicioFim.patient_id, 
                   inicioFim.data_inicio_mds, 
                   inicioFim.firstMds,
                   if(inicioFim.data_fim_mds is not null and inicioFim.data_fim_mds BETWEEN tx_new.art_start_date  and date_add(tx_new.art_start_date, interval 12 month),inicioFim.data_fim_mds,null ) data_fim_mds,
                   if(inicioFim.data_fim_mds is not null and inicioFim.data_fim_mds BETWEEN tx_new.art_start_date  and date_add(tx_new.art_start_date, interval 12 month),inicioFim.lastMds ,null ) lastMds, 
                   tx_new.art_start_date, 
                   date_add(tx_new.art_start_date, interval 33 day),
                   date_add(tx_new.art_start_date, interval 12 month)
                   from 
            (
            select inicioMds.patient_id,inicioMds.data_inicio_mds,inicioMds.firstMds,fimMds.data_fim_mds,fimMds.lastMds 

            from    
             (  
                  select p.patient_id,date(e.encounter_datetime) data_inicio_mds,
                  case o.value_coded
                  when  165340 then 'DB'
                  when  23730 then 'DT' 
                  when  165314 then 'DA' 
                  when  23888 then 'DS' 
                  when  23724 then 'GA'
                  when  23726 then 'CA'
                  when  165317 then 'TB' 
                  when  165320 then 'SMI' 
                  when  165321 then 'DAH' 
                  when  165178 then 'DCP' 
                  when  165179 then 'DCA' 
                  when  165318 then 'CT' 
                  when  165315 then 'DD' 
                  when  165264 then 'BM' 
                  when  165265 then 'CM'
                  when  23725  then 'AF'
                  when  23729  then 'FR'
                  when  165176 then 'EH' 
                  when  165319 then 'SAAJ'
                  when  23727  then 'PU'
                  when  165177 then 'FARMAC/Farmácia Privada'
                  when  23732  then 'OUTRO'
                  end AS firstMds
                  from patient p 
                  join encounter e on p.patient_id=e.patient_id 
                  join obs grupo on grupo.encounter_id=e.encounter_id 
                  join obs o on o.encounter_id=e.encounter_id 
                  join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                  where  e.encounter_type in(6) 
                  and e.location_id=:location 
                  and o.concept_id=165174  
                  and o.voided=0 
                  and grupo.concept_id=165323  
                  and grupo.voided=0 
                  and obsEstado.concept_id=165322  
                  and obsEstado.value_coded in(1256) 
                  and obsEstado.voided=0  
                  and grupo.voided=0 
                  and grupo.obs_id=o.obs_group_id 
                  and grupo.obs_id=obsEstado.obs_group_id 
                  order by date(e.encounter_datetime) ASC
                  )inicioMds
                left join
                (
                  select p.patient_id,date(e.encounter_datetime) data_fim_mds,
                  case o.value_coded
                  when  165340 then 'DB'
                  when  23730 then 'DT' 
                  when  165314 then 'DA' 
                  when  23888 then 'DS' 
                  when  23724 then 'GA'
                  when  23726 then 'CA'
                  when  165317 then 'TB' 
                  when  165320 then 'SMI' 
                  when  165321 then 'DAH' 
                  when  165178 then 'DCP' 
                  when  165179 then 'DCA' 
                  when  165318 then 'CT' 
                  when  165315 then 'DD' 
                  when  165264 then 'BM' 
                  when  165265 then 'CM'
                  when  23725  then 'AF'
                  when  23729  then 'FR'
                  when  165176 then 'EH' 
                  when  165319 then 'SAAJ'
                  when  23727  then 'PU'
                  when  165177 then 'FARMAC/Farmácia Privada'
                  when  23732  then 'OUTRO'
                  end AS lastMds
                  from patient p 
                  join encounter e on p.patient_id=e.patient_id 
                  join obs grupo on grupo.encounter_id=e.encounter_id 
                  join obs o on o.encounter_id=e.encounter_id 
                  join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
                  where  e.encounter_type in(6) 
                  and e.location_id=:location 
                  and o.concept_id=165174  
                  and o.voided=0 
                  and grupo.concept_id=165323  
                  and grupo.voided=0 
                  and obsEstado.concept_id=165322  
                  and obsEstado.value_coded in(1267) 
                  and obsEstado.voided=0  
                  and grupo.voided=0 
                  and grupo.obs_id=o.obs_group_id 
                  and grupo.obs_id=obsEstado.obs_group_id
                  order by  date(e.encounter_datetime) asc 
                ) fimMds on inicioMds.patient_id=fimMds.patient_id and fimMds.lastMds=inicioMds.firstMds
                )inicioFim
                left join 
               (
                  SELECT patient_id, MIN(art_start_date) art_start_date FROM 
                  ( 
                  SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM 
                  patient p 
                  INNER JOIN encounter e ON p.patient_id=e.patient_id 
                  INNER JOIN obs o ON o.encounter_id=e.encounter_id 
                  WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
                  AND e.location_id=:location 
                  GROUP BY p.patient_id 
                  UNION 
                  SELECT p.patient_id, MIN(value_datetime) art_start_date FROM 
                  patient p 
                  INNER JOIN encounter e ON p.patient_id=e.patient_id 
                  INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                  WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
                  AND o.concept_id=23866 AND o.value_datetime is NOT NULL 
                  AND e.location_id=:location 
                  GROUP BY p.patient_id 
                  ) 
                  art_start GROUP BY patient_id 
               )tx_new on tx_new.patient_id=inicioFim.patient_id
               WHERE inicioFim.data_inicio_mds BETWEEN tx_new.art_start_date  and date_add(tx_new.art_start_date, interval 12 month)
               order by inicioFim.patient_id,inicioFim.data_inicio_mds
               )inicioFimTxNew
               order by inicioFimTxNew.data_inicio_mds,inicioFimTxNew.data_fim_mds asc
               )inicioFimTxNew
               GROUP BY inicioFimTxNew.patient_id
               )inicioFimTxNew  
          )primeiroMds12Meses on primeiroMds12Meses.patient_id=coorte12Meses.patient_id 
         
            group by coorte12Meses.patient_id 
            )f where 
          f.patient_id in
            (
              SELECT p.patient_id FROM patient p 
                INNER JOIN encounter e ON e.patient_id=p.patient_id 
                WHERE e.voided=0 AND p.voided=0 AND e.encounter_type IN (5,7) 
                AND e.encounter_datetime<=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH) AND e.location_id=:location 
                UNION 
                SELECT pg.patient_id FROM patient p 
                INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
                WHERE pg.voided=0 AND p.voided=0 AND program_id IN (1,2) AND date_enrolled<=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH) AND location_id=:location 
                UNION 
                SELECT p.patient_id FROM patient p 
                INNER JOIN encounter e ON p.patient_id=e.patient_id 
                INNER JOIN obs o ON e.encounter_id=o.encounter_id 
                WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53 AND o.concept_id=23891 
                AND o.value_datetime IS NOT NULL AND o.value_datetime<=date_sub(date(concat(:year,'-06','-20')), interval 12 MONTH) AND e.location_id=:location 
                 union 
                 select p.patient_id from patient p 
                 inner join patient_program pp on pp.patient_id = p.patient_id 
                 where p.voided = 0 and pp.voided = 0 and pp.program_id = 25 
            ) 