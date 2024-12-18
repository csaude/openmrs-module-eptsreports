              select 
                 coorteFinal.patient_id as patient_id,
                 concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
                 pid.identifier as NID, 
                 p.gender as gender, 
                 if(inicio.data_inicio is not null,DATE_FORMAT(DATE(inicio.data_inicio), '%d/%m/%Y'),'N/A')  as data_inicio,
                 floor(datediff(:endDate,p.birthdate)/365) as idade_actual,
                 if(transferedIn.patient_id is not null,'Sim','Não') transfered_in,
                 if(gravida_real.data_gravida is null and lactante_real.data_parto is null, 'Não',if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
				 max_fila.data_fila,
				 max_fila.data_proximo_lev,
				 DATE_FORMAT(DATE(maxConsulta.data_seguimento), '%d/%m/%Y')  as data_seguimento,
				 if(max(obs_seguimento.value_datetime) is not null,DATE_FORMAT(DATE(max(obs_seguimento.value_datetime)), '%d/%m/%Y'),'N/A') as data_proximo_seguimento,
				 coorteFinal.criteria,
			     case 
	             when coorteFinal.criteria = 1 then 'C1– CD4 Inicial - Novo Início TARV' 
	             when coorteFinal.criteria = 2 then 'C2– CD4 Inicial - Reinício TARV' 
	             when coorteFinal.criteria =:location then 'C3– CV Alta' 
	             when coorteFinal.criteria = 4 then 'C4– Estadiamento Clínico III ou IV' 
	             when coorteFinal.criteria = 5 then 'C5– CD4 de Seguimento'
	             when coorteFinal.criteria = 6 then 'C6– Mulher Grávida'
	             end as criterioElegibilidade,
	            
	             if(cd4.data_ultimo_cd4 is not null,DATE_FORMAT(DATE(cd4.data_ultimo_cd4), '%d/%m/%Y'),'N/A')  as data_ultimo_cd4,
	             
	             if(ultimoCD4.data_resultado_cd4 is not null,DATE_FORMAT(DATE(ultimoCD4.data_resultado_cd4), '%d/%m/%Y'),'N/A')  as data_resultado_cd4,
                 if(ultimoCD4.valor_cd4 is not null,ultimoCD4.valor_cd4,'N/A') valor_cd4,
                 
                 if(cd4FinalFC.data_resultado_peneultimo_cd4 is not null,DATE_FORMAT(DATE(cd4FinalFC.data_resultado_peneultimo_cd4), '%d/%m/%Y'),'N/A')  as data_resultado_peneultimo_cd4,
	             if(cd4FinalFC.resultado_peneultimo_cd4 is not null,cd4FinalFC.resultado_peneultimo_cd4,'N/A')  resultado_peneultimo_cd4, 
	             
	             if(ultimoCD4LAB.data_resultado_cd4_lab is not null,DATE_FORMAT(DATE(ultimoCD4LAB.data_resultado_cd4_lab), '%d/%m/%Y'),'N/A')  as data_resultado_cd4_lab,
                 if(ultimoCD4LAB.valor_cd4_lab is not null,ultimoCD4LAB.valor_cd4_lab,'N/A') valor_cd4_lab,
	             
	             if(cd4FinalLAB.data_resultado_peneultimo_cd4_lab is not null,DATE_FORMAT(DATE(cd4FinalLAB.data_resultado_peneultimo_cd4_lab), '%d/%m/%Y'),'N/A')  as data_resultado_peneultimo_cd4_lab,
	             if(cd4FinalLAB.resultado_peneultimo_cd4_lab is not null,cd4FinalLAB.resultado_peneultimo_cd4_lab,'N/A')  resultado_peneultimo_cd4_lab, 
	             if(estadiamentoClinico.encounter_datetime is not null,DATE_FORMAT(DATE(estadiamentoClinico.encounter_datetime), '%d/%m/%Y'),'N/A' ) as encounter_datetime,
	             case 
		         when estadiamentoClinico.tipoEstadio=3 then 'Estadio III' 
		         when estadiamentoClinico.tipoEstadio=4 then 'Estadio IV' 
		         when ISNULL(estadiamentoClinico.tipoEstadio) then 'N/A' 
		         end as tipo_estadio, 
		         @motivoIndice := 1 + LENGTH(estadiamentoClinicoFinal.motivoEstadio) - LENGTH(REPLACE(estadiamentoClinicoFinal.motivoEstadio, ',', '')) AS motivoIndice, 
		         IF(ISNULL(SUBSTRING_INDEX(estadiamentoClinicoFinal.motivoEstadio, ',', 1)), 'N/A', SUBSTRING_INDEX(estadiamentoClinicoFinal.motivoEstadio, ',', 1)) AS motivoEstadio1, 
		         IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinicoFinal.motivoEstadio, ',', 2), ',', -1), '') = '', 'N/A', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinicoFinal.motivoEstadio, ',', 2), ',', -1), '')) AS motivoEstadio2,
		         
		         if(ultimoCV.data_ultimo_resultado_cv is not null,DATE_FORMAT(DATE(ultimoCV.data_ultimo_resultado_cv), '%d/%m/%Y'),'N/A')  as data_ultimo_resultado_cv,
		         if(ultimoCV.resultado_cv is not null,ultimoCV.resultado_cv,'N/A')  as resultado_cv,
		        
		         if(finalCVFC.data_ultimo_resultado_cv_anterior is not null,DATE_FORMAT(DATE(finalCVFC.data_ultimo_resultado_cv_anterior), '%d/%m/%Y'),'N/A')  as data_ultimo_resultado_cv_anterior,
		         if(finalCVFC.resultado_cv_anterior is not null,finalCVFC.resultado_cv_anterior,'N/A')  as resultado_cv_anterior,
		         
		         if(ultimoCVLAB.data_ultimo_resultado_cv_lab is not null,DATE_FORMAT(DATE(ultimoCVLAB.data_ultimo_resultado_cv_lab), '%d/%m/%Y'),'N/A')  as data_ultimo_resultado_cv_lab,
		         if(ultimoCVLAB.resultado_cv_lab is not null,ultimoCVLAB.resultado_cv_lab,'N/A')  as resultado_cv_lab,
		        
		         if(finalCVLAB.data_ultimo_resultado_cv_anterior_lab is not null,DATE_FORMAT(DATE(finalCVLAB.data_ultimo_resultado_cv_anterior_lab), '%d/%m/%Y'),'N/A')  as data_ultimo_resultado_cv_anterior_lab,
		         if(finalCVLAB.resultado_cv_anterior_lab is not null,finalCVLAB.resultado_cv_anterior_lab,'N/A')  as resultado_cv_anterior_lab

		            from
		             (
		              select final.patient_id  as patient_id, final.criteria
						from
						(
						select p.person_id as patient_id,
						if(C1.patient_id is not null,1,
						if(C2.patient_id is not null,2,
						if(C3.patient_id is not null,3,
						if(C4.patient_id is not null,4,
						if(C5.patient_id is not null,5,
						if(C6.patient_id is not null,6,null)))))) criteria
						from person p
						left join
						(
			               SELECT C1.patient_id, 1 criteria FROM  
			              (
			              SELECT tx_new. patient_id,art_start_date,trInProgram.minStateDate,trInMasterCard.value_datetime, cd4.data_cd4 FROM 
			              (
			              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
			              ( 
			              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM patient p 
			              INNER JOIN encounter e ON p.patient_id=e.patient_id 
			              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
			              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
			              AND e.encounter_datetime<=:endDate AND e.location_id=:location 
			              GROUP BY p.patient_id 
			              UNION 
			              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p 
			              INNER JOIN encounter e ON p.patient_id=e.patient_id 
			              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
			              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
			              AND o.concept_id=23866 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endDate AND e.location_id=:location 
			              GROUP BY p.patient_id 
			              ) 
			              art_start GROUP BY patient_id 
			            ) tx_new 
			
			            left join
			            (
			             select final.* from  ( 
			              select states.patient_id,states.patient_program_id,min(states.minStateDate) as minStateDate,states.program_id,states.state from  
			              ( 
			              SELECT p.patient_id, pg.patient_program_id, ps.start_date as minStateDate, pg.program_id, ps.state  FROM patient p   
			              inner join patient_program pg on p.patient_id=pg.patient_id  
			              inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  
			              WHERE pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  
			              and ps.start_date <= :endDate 
			              )states 
			              group by states.patient_id 
			              order by states.minStateDate asc  
			              ) final 
			              inner join patient_state ps on ps.patient_program_id=final.patient_program_id  
			              where ps.start_date=final.minStateDate and ps.state=29 and ps.voided=0 
			  
			            )trInProgram on trInProgram.patient_id=tx_new.patient_id
			
			            left join
			            (
			             SELECT tr.* from  
			             (
			              SELECT p.patient_id, MIN(obsData.value_datetime) value_datetime from patient p  
			              INNER JOIN encounter e ON p.patient_id=e.patient_id  
			              INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 
			              INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 
			              WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime <= :endDate AND e.location_id=:location 
			              GROUP BY p.patient_id 
			              ) tr 
			              GROUP BY tr.patient_id 
			            )trInMasterCard on trInMasterCard.patient_id=tx_new.patient_id
			            left join
			            (
			              select p.patient_id,o.obs_datetime data_cd4
			              from patient p   
			              inner join encounter e on p.patient_id = e.patient_id   
			              inner join obs o on o.encounter_id = e.encounter_id   
			              where p.voided = 0 
			              and e.voided = 0  
			              and o.voided = 0     
			              and  o.concept_id in(1695,730,165515) 
			              and e.encounter_type=6 
			              and e.location_id=:location 
			              and o.obs_datetime  BETWEEN :startDate and CURDATE()
			            )cd4 on cd4.patient_id=tx_new.patient_id
			            )C1 
			           where (C1.art_start_date BETWEEN :startDate AND :endDate) and (C1.minStateDate is null and C1.value_datetime is null and C1.data_cd4 is null)
			           )C1 on C1.patient_id=p.person_id
			           left join
			           (
			           	  select C2.patient_id,C2.criteria from 
			           (
			              select C2.patient_id,C2.data_estado, 2 criteria 
			              from 
			              ( 
			               select p.patient_id, max(o.obs_datetime) data_estado 
			               from patient p 
			               inner join encounter e on p.patient_id=e.patient_id  
			               inner join obs  o on e.encounter_id=o.encounter_id 
			               where e.voided=0 and o.voided=0 
			               and p.voided=0 
			               and  e.encounter_type in (53,6) 
			               and o.concept_id in (6272,6273) 
			               and o.value_coded in (1705) 
			               and o.obs_datetime BETWEEN :startDate and :endDate 
			               and e.location_id=:location 
			               group by p.patient_id 
			               )C2
			               left join
			               (
			                select p.patient_id,o.obs_datetime data_cd4
			                from patient p   
			                inner join encounter e on p.patient_id = e.patient_id   
			                inner join obs o on o.encounter_id = e.encounter_id   
			                where p.voided = 0 
			                and e.voided = 0  
			                and o.voided = 0     
			                and  o.concept_id in(1695,730,165515) 
			                and e.encounter_type=6 
			                and e.location_id=:location 
			                and o.obs_datetime  BETWEEN :startDate and  CURDATE()
			              )cd4 on cd4.patient_id=C2.patient_id
			               WHERE cd4.patient_id is null
			               )C2 
			           )C2 on C2.patient_id=p.person_id
			           left join
			           (
			          	select C3.patient_id,3 criteria from 
			          (
		                 select final.* from
		                 (
		                  select C3.*, 1 type_form from 
		                  (
		                  select C3.*,o.value_numeric,o.concept_id 
		                  from  
		                  (
		                   select ultimoCV.patient_id,ultimoCV.data_resultado,ultimoCV.resultado,max(cvAnterior.data_resultado_anterior) data_resultado_anterior  
		                   from
		                   ( 
		                     select cv.patient_id,cv.data_resultado,o.value_numeric resultado from 
		                     (
				           select cv.patient_id,max(cv.data_resultado) data_resultado
				            from (
				             select p.patient_id,e.encounter_datetime data_resultado
			                  from patient p   
			                  inner join encounter e on p.patient_id = e.patient_id
			                  inner join obs o on o.encounter_id=e.encounter_id   
			                  where p.voided = 0 
			                  and e.voided = 0  
			                  and e.encounter_datetime <=:endDate
			                  and e.encounter_type=6
			                  and o.concept_id in(856,1305) 
			                  and e.location_id=:location
			                  
			                  and o.voided=0 
			                  )cv 
			                  group by cv.patient_id
			                  )cv
			                  left join encounter e on e.patient_id=cv.patient_id
			                  left join obs o on o.encounter_id=e.encounter_id
			                  WHERE e.encounter_type=6 and e.voided=0 and o.voided=0 and ((o.concept_id=856 and o.value_numeric>1000)) and date(e.encounter_datetime)=date(cv.data_resultado)
			                  group by cv.patient_id
			                  )ultimoCV
			                  left join 
			                 (
				              select p.patient_id,o.obs_datetime data_resultado_anterior, o.value_numeric
				                from patient p   
				              inner join encounter e on p.patient_id = e.patient_id   
				              inner join obs o on o.encounter_id = e.encounter_id   
				              where p.voided = 0 
				              and e.voided = 0  
				              and o.voided = 0
				              and  o.concept_id in(856,1305)
				              and  o.obs_datetime<=:endDate
				              and e.encounter_type=6 
				              and e.location_id=:location 
				              order by o.obs_datetime desc
				             ) cvAnterior on cvAnterior.patient_id=ultimoCV.patient_id
				            where cvAnterior.data_resultado_anterior<date_sub(ultimoCV.data_resultado,interval 3 month)
				              group by patient_id
				             )C3
				            left join encounter e on C3.patient_id = e.patient_id   
			                 left join obs o on o.encounter_id = e.encounter_id   
			                  WHERE e.encounter_type=6 and ((o.concept_id=856 and o.value_numeric>1000)) and date(o.obs_datetime)=date(C3.data_resultado_anterior) and e.voided=0 and o.voided=0
			                  group by patient_id
			                  )C3
			                  left join
			                  (
			                   select cv.patient_id,cv.data_resultado,cv.resultado  
			                   from
			                   (
			                     select cv.patient_id,cv.data_resultado,o.value_numeric resultado from 
			                     (
					            select cv.patient_id,max(cv.data_resultado) data_resultado
					            from (
					             select p.patient_id,e.encounter_datetime data_resultado
				                  from patient p   
				                  inner join encounter e on p.patient_id = e.patient_id
				                  inner join obs o on o.encounter_id=e.encounter_id   
				                  where p.voided = 0 
				                  and e.voided = 0  
				                  and e.encounter_datetime <=:endDate
				                  and e.encounter_type=6
				                  and o.concept_id=856 
				                  and e.location_id=:location
				                  and o.voided=0 
				                  )cv 
				                  group by cv.patient_id
				                  )cv
				                  left join encounter e on e.patient_id=cv.patient_id
				                  left join obs o on o.encounter_id=e.encounter_id
				                  WHERE e.encounter_type=6 and e.voided=0 and o.voided=0 and ((o.concept_id=856 and o.value_numeric>1000)) and date(o.obs_datetime)=date(cv.data_resultado) 
				                  group by cv.patient_id
				                  )cv
                                  left join
				                  (
				                    select p.patient_id,o.obs_datetime data_cd4,o.value_numeric valor
				                    from patient p   
				                    inner join encounter e on p.patient_id = e.patient_id   
				                    inner join obs o on o.encounter_id = e.encounter_id   
				                    where p.voided = 0 
				                    and e.voided = 0  
				                    and o.voided = 0     
				                    and  o.concept_id in(1695,730,165515) 
				                    and e.encounter_type in(6) 
				                    and e.location_id=:location 
				                   )cd4 on cd4.patient_id=cv.patient_id
				                    where cd4.data_cd4 BETWEEN cv.data_resultado and CURDATE()
				                    group by cv.patient_id
			                      )cd4 on cd4.patient_id=C3.patient_id
			                      WHERE  cd4.patient_id  is null 

			                      union
			             select C3.*, 2 type_form from 
		                  (
		                  select C3.*,o.value_numeric,o.concept_id 
		                  from  
		                  (
		                   select ultimoCV.patient_id,ultimoCV.data_resultado,ultimoCV.resultado,max(cvAnterior.data_resultado_anterior) data_resultado_anterior  
		                   from
		                   (
		                     select cv.patient_id,cv.data_resultado,o.value_numeric resultado from 
		                     (
				           select cv.patient_id,max(cv.data_resultado) data_resultado
				            from (
				             select p.patient_id,e.encounter_datetime data_resultado
			                  from patient p   
			                  inner join encounter e on p.patient_id = e.patient_id
			                  inner join obs o on o.encounter_id=e.encounter_id   
			                  where p.voided = 0 
			                  and e.voided = 0  
			                  and e.encounter_datetime <=:endDate
			                  and e.encounter_type in(13,51)
			                  and o.concept_id in(856,1305) 
			                  and e.location_id=:location
			                  and o.voided=0 
			                  )cv 
			                  group by cv.patient_id
			                  )cv
			                  left join encounter e on e.patient_id=cv.patient_id
			                  left join obs o on o.encounter_id=e.encounter_id
			                  WHERE e.encounter_type in(13,51) and e.voided=0 and o.voided=0 and ((o.concept_id=856 and o.value_numeric>1000)) and date(e.encounter_datetime)=date(cv.data_resultado) 
			                  group by cv.patient_id
			                  )ultimoCV
			                  left join 
			                 (
				              select p.patient_id,o.obs_datetime data_resultado_anterior, o.value_numeric
				                from patient p   
				              inner join encounter e on p.patient_id = e.patient_id   
				              inner join obs o on o.encounter_id = e.encounter_id   
				              where p.voided = 0 
				              and e.voided = 0  
				              and o.voided = 0
				              and  o.concept_id in(856,1305)
				              and  o.obs_datetime<=:endDate
				              and e.encounter_type in(13,51) 
				              and e.location_id=:location 
				              order by o.obs_datetime desc
				             ) cvAnterior on cvAnterior.patient_id=ultimoCV.patient_id
				            where cvAnterior.data_resultado_anterior<date_sub(ultimoCV.data_resultado,interval 3 month)
				              group by patient_id
				             )C3
				            left join encounter e on C3.patient_id = e.patient_id   
			                 left join obs o on o.encounter_id = e.encounter_id   
			                  WHERE e.encounter_type in(13,51) and ((o.concept_id=856 and o.value_numeric>1000)) and date(o.obs_datetime)=date(C3.data_resultado_anterior) and e.voided=0 and o.voided=0
			                  group by patient_id
			                  )C3
			                  left join
			                  (
			                   select cv.patient_id,cv.data_resultado,cv.resultado  
			                   from
			                   (
			                     select cv.patient_id,cv.data_resultado,o.value_numeric resultado from 
			                     (
					           select cv.patient_id,max(cv.data_resultado) data_resultado
					            from (
					             select p.patient_id,e.encounter_datetime data_resultado
				                  from patient p   
				                  inner join encounter e on p.patient_id = e.patient_id
				                  inner join obs o on o.encounter_id=e.encounter_id   
				                  where p.voided = 0 
				                  and e.voided = 0  
				                  and e.encounter_datetime <=:endDate
				                  and e.encounter_type in(13,51)
				                  and o.concept_id=856 
				                  and e.location_id=:location
				                  and o.voided=0 
				                  )cv 
				                  group by cv.patient_id
				                  )cv
				                  left join encounter e on e.patient_id=cv.patient_id
				                  left join obs o on o.encounter_id=e.encounter_id
				                  WHERE e.encounter_type in(13,51) and e.voided=0 and o.voided=0 and ((o.concept_id=856 and o.value_numeric>1000)) and date(o.obs_datetime)=date(cv.data_resultado)
				                  group by cv.patient_id
				                  )cv
                                left join
				                  (
				                    select p.patient_id,o.obs_datetime data_cd4,o.value_numeric valor
				                    from patient p   
				                    inner join encounter e on p.patient_id = e.patient_id   
				                    inner join obs o on o.encounter_id = e.encounter_id   
				                    where p.voided = 0 
				                    and e.voided = 0  
				                    and o.voided = 0     
				                    and  o.concept_id in(1695,730,165515) 
				                    and e.encounter_type in(6) 
				                    and e.location_id=:location 
				                   )cd4 on cd4.patient_id=cv.patient_id
				                    where cd4.data_cd4 BETWEEN cv.data_resultado and CURDATE()
				                    group by cv.patient_id
			                      )cd4 on cd4.patient_id=C3.patient_id
			                      WHERE  cd4.patient_id  is null 
			                      )final order by final.patient_id,final.data_resultado desc
			                      )C3  
			                      group by C3.patient_id order by C3.type_form
			           )C3 on C3.patient_id=p.person_id
			           left join
			           (
			            select C4.patient_id,4 criteria
			                  from
			                  (
			                  select III.patient_id,III.data_estadio
			                  from 
			                  ( 
			                   select p.patient_id, min(o.obs_datetime) data_estadio 
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0 
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=1406 
			                   and o.value_coded in (5018,5334,5018,6783,5945,126,60,43,42) 
			                   and o.obs_datetime BETWEEN :startDate and :endDate 
			                   and e.location_id=:location 
			                   group by p.patient_id 
			                   )III
			           
			                  union 
			
			                  select IV.patient_id,IV.data_estadio
			                  from 
			                  ( 
			                   select p.patient_id, min(o.obs_datetime) data_estadio 
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0 
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=1406 
			                   and o.value_coded in (5340,6990,5344,1294,507,14656,7180,5042,1570) 
			                   and o.obs_datetime BETWEEN :startDate and :endDate 
			                   and e.location_id=:location 
			                   group by p.patient_id 
			                   )IV
			                   )C4
			                  left join
			
			                  (
			
			                  select estadioOMS.patient_id,min(estadioOMS.data_estadio)
			                  from
			                  (
			                  select III.patient_id,III.data_estadio
			                  from 
			                  ( 
			                   select p.patient_id, min(o.obs_datetime) data_estadio 
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0 
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=1406 
			                   and o.value_coded in (5018,5334,5018,6783,5945,126,60,43,42) 
			                   and o.obs_datetime BETWEEN :startDate and :endDate 
			                   and e.location_id=:location 
			                   group by p.patient_id 
			                   )III
			           
			                  union 
			
			                  select IV.patient_id,IV.data_estadio
			                  from 
			                  ( 
			                   select p.patient_id, min(o.obs_datetime) data_estadio 
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0 
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=1406 
			                   and o.value_coded in (5340,6990,5344,1294,507,14656,7180,5042,1570) 
			                   and o.obs_datetime BETWEEN :startDate and :endDate 
			                   and e.location_id=:location 
			                   group by p.patient_id 
			                   )IV
			                   )estadioOMS
			                   left join
			                   (
			                    select p.patient_id,o.obs_datetime data_cd4,e.encounter_id
			                      from patient p   
			                      inner join encounter e on p.patient_id = e.patient_id   
			                      inner join obs o on o.encounter_id = e.encounter_id   
			                      where p.voided = 0 
			                      and e.voided = 0  
			                      and o.voided = 0     
			                      and  o.concept_id in(1695,730,165515) 
			                      and e.encounter_type=6 
			                      and e.location_id=:location 
			                      )cd4 on cd4.patient_id=estadioOMS.patient_id
			                      where cd4.data_cd4 BETWEEN estadioOMS.data_estadio and CURDATE()
			                      group by estadioOMS.patient_id
			                  )exclusao on exclusao.patient_id=C4.patient_id
			                  where exclusao.patient_id is null
			                  group by C4.patient_id
			           )C4 on C4.patient_id=p.person_id
			           left join
			           (
			             select C5.patient_id, 5 criteria from
			                  (
			                  select CD4Absuluto.patient_id,CD4Absuluto.data_cd4,CD4Absuluto.cd4
			                  from 
			                  ( 
			                   select p.patient_id, max(e.encounter_datetime) data_cd4,o.value_numeric cd4
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0  
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=1695 
			                   and o.obs_datetime<=date_sub(:endDate, interval 12 month)  
			                   and e.location_id=:location 
			                   and o.value_numeric<350
			                   group by p.patient_id 
			                   )CD4Absuluto
			                   union
			                  select CD4Percentual.patient_id,CD4Percentual.data_cd4,CD4Percentual.cd4
			                  from 
			                  ( 
			                   select p.patient_id, max(e.encounter_datetime) data_cd4,o.value_numeric cd4
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0  
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=730 
			                   and o.obs_datetime<=date_sub(:endDate, interval 12 month) 
			                   and e.location_id=:location 
			                   and o.value_numeric<30
			                   group by p.patient_id 
			                   )CD4Percentual
			                  union
			                  select CD4SemiQuantitativo.patient_id,CD4SemiQuantitativo.data_cd4,CD4SemiQuantitativo.cd4
			                  from 
			                  ( 
			                   select p.patient_id, max(e.encounter_datetime) data_cd4,o.value_numeric cd4
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0  
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=165515 
			                   and o.obs_datetime<=date_sub(:endDate, interval 12 month) 
			                   and e.location_id=:location 
			                   and o.value_coded=165513
			                   group by p.patient_id 
			                   )CD4SemiQuantitativo
			                   )C5
			                   left join
			                   (
			                    select final.patient_id from
			                     (
			                    select CD4Absuluto.patient_id,CD4Absuluto.data_cd4,CD4Absuluto.cd4
			                    from 
			                    ( 
			                     select p.patient_id, max(o.obs_datetime) data_cd4,o.value_numeric cd4
			                     from patient p 
			                     inner join encounter e on p.patient_id=e.patient_id  
			                     inner join obs  o on e.encounter_id=o.encounter_id 
			                     where e.voided=0 and o.voided=0  
			                     and p.voided=0 
			                     and  e.encounter_type=6 
			                     and o.concept_id=1695 
			                     and o.obs_datetime<=date_sub(:endDate, interval 12 month)
			                     and e.location_id=:location 
			                     and o.value_numeric<350
			                     group by p.patient_id 
			                     )CD4Absuluto
			                     union
			                    select CD4Percentual.patient_id,CD4Percentual.data_cd4,CD4Percentual.cd4
			                    from 
			                    ( 
			                     select p.patient_id, max(o.obs_datetime) data_cd4,o.value_numeric cd4
			                     from patient p 
			                     inner join encounter e on p.patient_id=e.patient_id  
			                     inner join obs  o on e.encounter_id=o.encounter_id 
			                     where e.voided=0 and o.voided=0  
			                     and p.voided=0 
			                     and  e.encounter_type=6 
			                     and o.concept_id=730 
			                     and o.obs_datetime<=date_sub(:endDate, interval 12 month) 
			                     and e.location_id=:location 
			                     and o.value_numeric<30
			                     group by p.patient_id 
			                     )CD4Percentual
			                    union
			                  select CD4SemiQuantitativo.patient_id,CD4SemiQuantitativo.data_cd4,CD4SemiQuantitativo.cd4
			                  from 
			                  ( 
			                   select p.patient_id, max(e.encounter_datetime) data_cd4,o.value_numeric cd4
			                   from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id  
			                   inner join obs  o on e.encounter_id=o.encounter_id 
			                   where e.voided=0 and o.voided=0  
			                   and p.voided=0 
			                   and  e.encounter_type=6 
			                   and o.concept_id=165515 
			                   and o.obs_datetime<=date_sub(:endDate, interval 12 month) 
			                   and e.location_id=:location 
			                   and o.value_coded=165513
			                   group by p.patient_id 
			                   )CD4SemiQuantitativo
			                     )final
			                     left join
			                     (
			                     select p.patient_id,o.obs_datetime data_cd4,o.value_numeric
			                        from patient p   
			                        inner join encounter e on p.patient_id = e.patient_id   
			                        inner join obs o on o.encounter_id = e.encounter_id   
			                        where p.voided = 0 
			                        and e.voided = 0  
			                        and o.voided = 0     
			                        and  o.concept_id in(1695,730,165515) 
			                        and e.encounter_type=6 
			                        and e.location_id=:location 
			                      )cd4 on cd4.patient_id=final.patient_id
			                      where cd4.data_cd4 BETWEEN date_add(final.data_cd4, interval 1 day) and CURDATE()
			                   )exclusion on exclusion.patient_id=C5.patient_id
			                   where exclusion.patient_id is null 
			           )C5 on C5.patient_id=p.person_id
			           left join
			           (
			            select C6.patient_id, 6 criteria
			                    from 
			                    ( 
			                     select p.patient_id, min(e.encounter_datetime) data_gravida
			                     from patient p 
			                     inner join encounter e on p.patient_id=e.patient_id  
			                     inner join obs  o on e.encounter_id=o.encounter_id 
			                     inner join person pe on pe.person_id=p.patient_id
			                     where e.voided=0 and o.voided=0  
			                     and p.voided=0 
			                     and  e.encounter_type=6 
			                     and o.concept_id=1982 
			                     and o.value_coded=1065
			                     and  pe.gender='F' 
			                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 9 month) and :endDate 
			                     and e.location_id=:location 
			                     group by p.patient_id 
			                     )C6
			                     left join
			                     (			
			                    select gravida.patient_id,gravida.data_gravida
			                    from 
			                    ( 
			                     select p.patient_id, min(e.encounter_datetime) data_gravida
			                     from patient p 
			                     inner join encounter e on p.patient_id=e.patient_id  
			                     inner join obs  o on e.encounter_id=o.encounter_id 
			                     inner join person pe on pe.person_id=p.patient_id
			                     where e.voided=0 and o.voided=0  
			                     and p.voided=0 
			                     and  e.encounter_type=6 
			                     and o.concept_id=1982 
			                     and o.value_coded=1065
			                     and  pe.gender='F' 
			                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 9 month) and :endDate 
			                     and e.location_id=:location 
			                     group by p.patient_id 
			                     )gravida
			                     left join
			                     (
			                        select p.patient_id,o.obs_datetime data_cd4
			                        from patient p   
			                        inner join encounter e on p.patient_id = e.patient_id   
			                        inner join obs o on o.encounter_id = e.encounter_id   
			                        where p.voided = 0 
			                        and e.voided = 0  
			                        and o.voided = 0     
			                        and  o.concept_id in(1695,730,165515) 
			                        and e.encounter_type=6 
			                        and e.location_id=:location 
			                      )cd4 on cd4.patient_id=gravida.patient_id 
			                      WHERE cd4.data_cd4  BETWEEN gravida.data_gravida and CURDATE()
			                      )exclusion on exclusion.patient_id=C6.patient_id
			                      where exclusion.patient_id is null
			           )C6 on C6.patient_id=p.person_id
			           )final
			            where final.patient_id is not null and final.criteria in(1,2,3,4,5,6) 
                        group by final.patient_id
		             )coorteFinal
		             inner join 
		             (
		             select coorte12meses_final.patient_id 
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
								                 where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate
								                 and e.location_id=:location and YEAR(STR_TO_DATE(e.encounter_datetime, '%Y-%m-%d')) >= 1000
								                     group by p.patient_id
								                 union
								                 Select p.patient_id,min(value_datetime) data_inicio
								                 from patient p
								                     inner join encounter e on p.patient_id=e.patient_id
								                     inner join obs o on e.encounter_id=o.encounter_id
								                 where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866
								                     and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location
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
											                        	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
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
								                              	and o.value_coded in (1709) and o.obs_datetime<=:endDate and e.location_id=:location 
								                                 	group by p.patient_id 
							                              ) saidas_por_suspensao group by saidas_por_suspensao.patient_id
							                           ) saidas_por_suspensao
							                           left join(
														
														select p.patient_id,max(e.encounter_datetime) encounter_datetime
													     from patient p
													     	inner join encounter e on e.patient_id = p.patient_id
													     where p.voided = 0 and e.voided = 0 and e.encounter_datetime <:endDate and e.location_id =:location and e.encounter_type=18
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
																		and ps.start_date<=:endDate and pg.location_id =:location group by pg.patient_id                                           
														) max_estado                                                                                                                        
															inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
															inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
														where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location  
														
														union
														
														select  p.patient_id,max(o.obs_datetime) data_estado                                                                                              
														from patient p                                                                                                                   
															inner join person pe on pe.person_id = p.patient_id                                                                         
															inner join encounter e on p.patient_id=e.patient_id                                                                         
															inner join obs  o on e.encounter_id=o.encounter_id                                                                          
														where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
															and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
															and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
															group by p.patient_id                                                                                                               
														
														union                                                                                                                               
														
														select person_id as patient_id,death_date as data_estado                                                                            
														from person                                                                                                                         
														where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate 
														                                                                                                           
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
														and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
														group by p.patient_id  
													
													union
															
													select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
													from patient p                                                                                                                                   
														inner join person pe on pe.person_id = p.patient_id                                                                                         
														inner join encounter e on e.patient_id=p.patient_id                                                                                         
													where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
														and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
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
								                                 		and ps.start_date< :endDate and pg.location_id =:location group by pg.patient_id                                          
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
							                                	and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
							                        			group by p.patient_id                                                                                                               
							
							                          ) saidas_por_transferencia group by saidas_por_transferencia.patient_id
							                     ) saidas_por_transferencia
							                       left join (
												      
												      select p.patient_id,max(e.encounter_datetime) encounter_datetime
												      from patient p
												      	inner join encounter e on e.patient_id = p.patient_id
												      where p.voided = 0 and e.voided = 0 and e.encounter_datetime < :endDate and e.location_id =:location and e.encounter_type=18
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
										                   where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate 
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
								                         and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime<=:endDate                                                                                        
								                   	group by p.patient_id
								               ) ultimo_levantamento group by patient_id
							           	) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
							          		where (ultimo_levantamento.data_ultimo_levantamento <= :endDate	and saidas_por_transferencia.data_estado<=:endDate)
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
						                    and date(e.encounter_datetime)<=:endDate 
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
						                   and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
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
						               where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=:endDate 
						                   group by p.patient_id 
						                union
						
						
						                Select p.patient_id,max(encounter_datetime) data_encountro 
						                from patient p 
						                    inner join encounter e on e.patient_id=p.patient_id 
						                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
						                    and date(e.encounter_datetime)<=:endDate 
						                    group by p.patient_id 
						                    ) consulta group by consulta.patient_id
						           )consulta on consulta.patient_id=inicio.patient_id
						           left join
						               (
						           	Select p.patient_id,max(encounter_datetime) data_fila from patient p 
						                    inner join encounter e on e.patient_id=p.patient_id 
						                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
						                    and date(e.encounter_datetime)<=:endDate 
						                    group by p.patient_id 
						           	)max_fila on max_fila.patient_id=inicio.patient_id
						
						) inicio_fila_seg 
						
						     ) inicio_fila_seg_prox 
						         group by patient_id 
						    ) coorte12meses_final 
						 WHERE  ((data_estado is null or ((data_estado is not null) and  data_fila>data_estado))  and date_add(data_usar, interval 59 day) >=:endDate) 
	
		             ) B13 on B13.patient_id=coorteFinal.patient_id
                      left join
                      (   Select patient_id,min(data_inicio) data_inicio 
		                    from 
		                        (    
		                            Select  p.patient_id,min(e.encounter_datetime) data_inicio 
		                            from    patient p  
		                                    inner join encounter e on p.patient_id=e.patient_id  
		                                    inner join obs o on o.encounter_id=e.encounter_id 
		                            where   e.voided=0 and o.voided=0 and p.voided=0 and  
		                                    e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and  
		                                    e.encounter_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id 
		                            union 
		                            Select  p.patient_id,min(value_datetime) data_inicio 
		                            from    patient p 
		                                    inner join encounter e on p.patient_id=e.patient_id 
		                                    inner join obs o on e.encounter_id=o.encounter_id 
		                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  
		                                    o.concept_id=1190 and o.value_datetime is not null and  
		                                    o.value_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id 
		                            union 
		                            select  pg.patient_id,min(date_enrolled) data_inicio 
		                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id 
		                            where   pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location
		                            group by pg.patient_id 
		                            union 
		                              SELECT    e.patient_id, MIN(e.encounter_datetime) AS data_inicio  
		                              FROM      patient p 
		                                        inner join encounter e on p.patient_id=e.patient_id 
		                              WHERE     p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location
		                              GROUP BY  p.patient_id 
		                            union 
		                            Select  p.patient_id,min(value_datetime) data_inicio 
		                            from    patient p 
		                                    inner join encounter e on p.patient_id=e.patient_id 
		                                    inner join obs o on e.encounter_id=o.encounter_id 
		                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  
		                                    o.concept_id=23866 and o.value_datetime is not null and  
		                                    o.value_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id    
		                        ) inicio_real 
		                    group by patient_id 
		                 )inicio on inicio.patient_id=coorteFinal.patient_id 
		                 left join
		                 (
		                  SELECT p.patient_id, obsData.value_datetime as data_estado from patient p  
				          INNER JOIN encounter e ON p.patient_id=e.patient_id  
				          INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 
				          INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 
				          WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 and obsData.value_datetime<=:endDate
				          AND e.location_id=:location 
				          GROUP BY p.patient_id 
				        
				        union
			            
				        select max_estado.patient_id, max_estado.data_estado 
						from(                                                                
							
							select max_estado.*
							from(
								
								select pp.patient_id, ps.patient_state_id, ps.state, max_estado.data_estado
								from(
									
									select pg.patient_id, min(ps.start_date) data_estado                                                                                          
									from patient p                                                                                                               
											inner join patient_program pg on p.patient_id = pg.patient_id                                                               
									  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
									where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                    
										and ps.start_date <= :endDate  and pg.location_id =:location 
										group by pg.patient_id 
								)max_estado
									inner join patient_program pp on pp.patient_id = max_estado.patient_id
								 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado  
						  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location 
						    			group by pp.patient_id, ps.patient_state_id  order by pp.patient_id, ps.patient_state_id desc         
						  	)max_estado group by max_estado.patient_id                                        
						) max_estado                                                                                                                        
							inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id  
							inner join patient_program pp on pp.patient_program_id = ps.patient_program_id         
						where ps.state = 29
						)transferedIn on transferedIn.patient_id=coorteFinal.patient_id
	                      inner join person p on p.person_id=coorteFinal.patient_id        
	                      left join  
	                      (   select pad1.* 
	                          from person_address pad1 
	                          inner join  
	                          ( 
	                              select person_id,min(person_address_id) id  
	                              from person_address 
	                              where voided=0 
	                              group by person_id 
	                          ) pad2 
	                          where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id 
	                      ) pad3 on pad3.person_id=coorteFinal.patient_id              
	                      left join            
	                      (   select pn1.* 
	                          from person_name pn1 
	                          inner join  
	                          ( 
	                              select person_id,min(person_name_id) id  
	                              from person_name 
	                              where voided=0 
	                              group by person_id 
	                          ) pn2 
	                          where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
	                      ) pn on pn.person_id=coorteFinal.patient_id          
	                      left join 
	                      (   select pid1.* 
	                          from patient_identifier pid1 
	                          inner join 
	                          ( 
	                              select patient_id,min(patient_identifier_id) id 
	                              from patient_identifier 
	                              where voided=0 
	                              group by patient_id 
	                          ) pid2 
	                          where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
	                      ) pid on pid.patient_id=coorteFinal.patient_id 
	
	                      left join                                                                                                                                                                                                                                                   
	                          (   
	                            select p.patient_id,e.encounter_datetime data_gravida from patient p     
	                              inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
	                                  inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
	                                  inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
	                              where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
	                              and value_coded=1065 and e.encounter_type=6                                                                                                                               
	                              and e.encounter_datetime  between :startDate and :endDate  
	                              and e.location_id=:location  
	                              and pe.gender = 'F' 
	                        ) gravida_real on gravida_real.patient_id=coorteFinal.patient_id
	
	                          left join                                                                                                                                                                                                                                                   
	                          (   
	                            select p.patient_id,e.encounter_datetime data_parto from patient p     
	                              inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
	                                  inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
	                                  inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
	                              where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332
	                              and value_coded=1065 and e.encounter_type=6                                                                                                                               
	                              and e.encounter_datetime  between :startDate and :endDate  
	                              and e.location_id=:location  
	                              and pe.gender = 'F' 
	                          ) lactante_real on lactante_real.patient_id = coorteFinal.patient_id
	                          left join 
                               ( 
                                select patient_id, max(obs_fila.value_datetime) data_proximo_lev, data_fila from ( 
                                select fila.patient_id,fila.data_fila data_fila,e.encounter_id from 
                                 ( 
                                 Select p.patient_id,max(encounter_datetime) data_fila from patient p 
                                 inner join encounter e on e.patient_id=p.patient_id 
                                 where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                 and date(e.encounter_datetime)<=:endDate 
                                  group by p.patient_id 
                                 )fila 
                                inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
                                )maxFila 
                                left join 
                                obs obs_fila on obs_fila.person_id=maxFila.patient_id 
                                and obs_fila.voided=0 
                                and obs_fila.encounter_id=maxFila.encounter_id 
                                and obs_fila.concept_id=5096 
                                and obs_fila.location_id=:location 
                                group by maxFila.patient_id 
                                ) max_fila on coorteFinal.patient_id=max_fila.patient_id
                                left join
                                (
                                 Select p.patient_id,max(encounter_datetime) data_seguimento from patient p 
                                 inner join encounter e on e.patient_id=p.patient_id 
                                 where p.voided=0 
                                 and e.voided=0 
                                 and e.encounter_type in (6,9)
                                 and e.location_id=:location 
                                 and e.encounter_datetime<=:endDate 
                                 group by p.patient_id 
                                )  maxConsulta on maxConsulta.patient_id=coorteFinal.patient_id
                                left join obs obs_seguimento on obs_seguimento.person_id=maxConsulta.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=maxConsulta.data_seguimento 
                                and obs_seguimento.concept_id=1410 
                                and obs_seguimento.location_id=:location 
                                left join
		                      (
		                        select p.patient_id,max(o.obs_datetime) data_ultimo_cd4
		                        from patient p   
		                        inner join encounter e on p.patient_id = e.patient_id   
		                        inner join obs o on o.encounter_id = e.encounter_id   
		                        where p.voided = 0 
		                        and e.voided = 0  
		                        and o.voided = 0     
		                        and  o.concept_id=23722
		                        and o.value_coded=1695
		                        and e.encounter_type=6 
		                        and e.location_id=:location 
		                        and o.obs_datetime<=:endDate
		                        group by p.patient_id
		                      )cd4 on cd4.patient_id=coorteFinal.patient_id
		                      left join
		                      (
		                      
		                             select cd4.patient_id,cd4.data_resultado_cd4 data_resultado_cd4,o.value_numeric valor_cd4 ,o.encounter_id 
					                 from 
					                 (
						              select cd4.patient_id,max(cd4.data_resultado) data_resultado_cd4
						              from (
					                       select p.patient_id,e.encounter_datetime data_resultado
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and  e.voided = 0  
						                  and  o.voided = 0
						                  and  o.concept_id in (1695,730,165515)
						                  and  e.encounter_datetime <= :endDate
						                  and  e.encounter_type=6
						                  and  e.location_id=:location
						                  )cd4
						                  group by cd4.patient_id
						                  )cd4
						                  left join encounter e on e.patient_id=cd4.patient_id
						                  left join obs o on o.encounter_id=e.encounter_id 
						                  where o.concept_id in (1695,730,165515) and cd4.data_resultado_cd4=o.obs_datetime and o.voided=0 and e.encounter_type=6
		                      )ultimoCD4 on ultimoCD4.patient_id=coorteFinal.patient_id
		                       left join
		                      (
		                             select cd4.patient_id,cd4.data_resultado_cd4 data_resultado_cd4_lab,o.value_numeric valor_cd4_lab  
					                 from 
					                 (
						              select cd4.patient_id,max(cd4.data_resultado) data_resultado_cd4
						              from (
					                       select p.patient_id,e.encounter_datetime data_resultado
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and  e.voided = 0  
						                  and  o.voided = 0
						                  and  o.concept_id in (1695,730,165515)
						                  and  e.encounter_datetime <= :endDate
						                  and  e.encounter_type in(13,51)
						                  and  e.location_id=:location 
						                  )cd4
						                  group by cd4.patient_id
						                  )cd4
						                  left join encounter e on e.patient_id=cd4.patient_id
						                  left join obs o on o.encounter_id=e.encounter_id 
						                  where o.concept_id in (1695,730,165515) and cd4.data_resultado_cd4=o.obs_datetime and o.voided=0 and e.encounter_type in(13,51)
		                      )ultimoCD4LAB on ultimoCD4LAB.patient_id=coorteFinal.patient_id
		                      left join
		                      (
					            select cd4Final.*, o.value_numeric as resultado_peneultimo_cd4 from
					                 (
					                 select ultimoCd4.patient_id,ultimoCd4.data_resultado_cd4,ultimoCd4.valor_cd4,max(peneultimoCd4.data_resultado_peneultimo_cd4) data_resultado_peneultimo_cd4
					                 from
					                 (
					                 select cd4.patient_id,cd4.data_resultado_cd4 data_resultado_cd4,o.value_numeric valor_cd4  
					                 from 
					                 (
						              select cd4.patient_id,max(cd4.data_resultado) data_resultado_cd4
						              from (
					                       select p.patient_id,e.encounter_datetime data_resultado
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and  e.voided = 0  
						                  and  o.voided = 0
						                  and  o.concept_id in (1695,730,165515)
						                  and  e.encounter_datetime <= :endDate
						                  and  e.encounter_type=6
						                  and  e.location_id=:location 
						                  )cd4
						                  group by cd4.patient_id
						                  )cd4
						                  left join obs o on o.person_id=cd4.patient_id 
						                  where o.concept_id in (1695,730,165515) and cd4.data_resultado_cd4=o.obs_datetime and o.voided=0
					                 )ultimoCd4
					                 left join
					                 (
					 	              select cd4.patient_id,cd4.data_resultado data_resultado_peneultimo_cd4
						              from (
						                  select p.patient_id,e.encounter_datetime data_resultado,o.value_numeric value_numeric
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and   e.voided = 0  
						                  and   o.voided = 0
						                  and   o.concept_id in (1695,730,165515)
						                  and   e.encounter_datetime <= :endDate
						                  and   e.encounter_type=6
						                  and   e.location_id=:location 
						                  )cd4
						                )peneultimoCd4 on peneultimoCd4.patient_id=ultimoCd4.patient_id
						                 where peneultimoCd4.data_resultado_peneultimo_cd4<ultimoCd4.data_resultado_cd4
						                 group by ultimoCd4.patient_id
						                )cd4Final
						                inner join encounter e on e.patient_id=cd4Final.patient_id
						                inner join obs o on o.encounter_id=e.encounter_id 
						                where o.voided=0 and o.concept_id in (1695,730,165515) and e.encounter_datetime=cd4Final.data_resultado_peneultimo_cd4 and  e.encounter_type=6 
						                group by cd4Final.patient_id 
						      )cd4FinalFC on cd4FinalFC.patient_id=coorteFinal.patient_id
						      
						      left join
						      (
						        select cd4Final.*, o.value_numeric as resultado_peneultimo_cd4_lab from
					                 (
					                 select ultimoCd4.patient_id,ultimoCd4.data_resultado_cd4_lab,ultimoCd4.valor_cd4_lab,max(peneultimoCd4.data_resultado_peneultimo_cd4_lab) data_resultado_peneultimo_cd4_lab
					                 from
					                 (
					                 select cd4.patient_id,cd4.data_resultado_cd4_lab data_resultado_cd4_lab,o.value_numeric valor_cd4_lab  
					                 from 
					                 (
						              select cd4.patient_id,max(cd4.data_resultado_lab) data_resultado_cd4_lab
						              from (
					                       select p.patient_id,e.encounter_datetime data_resultado_lab
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and  e.voided = 0  
						                  and  o.voided = 0
						                  and  o.concept_id in (1695,730,165515)
						                  and  e.encounter_datetime <= :endDate
						                  and  e.encounter_type=13
						                  and  e.location_id=:location 
						                  )cd4
						                  group by cd4.patient_id
						                  )cd4
						                  left join obs o on o.person_id=cd4.patient_id 
						                  where o.concept_id in (1695,730,165515) and cd4.data_resultado_cd4_lab=o.obs_datetime and o.voided=0
					                 )ultimoCd4
					                 left join
					                 (
					 	              select cd4.patient_id,cd4.data_resultado_lab data_resultado_peneultimo_cd4_lab
						              from (
						                  select p.patient_id,e.encounter_datetime data_resultado_lab,o.value_numeric value_numeric
						                  from patient p   
						                  inner join encounter e on p.patient_id = e.patient_id   
						                  inner join obs o on o.encounter_id = e.encounter_id   
						                  where p.voided = 0 
						                  and   e.voided = 0  
						                  and   o.voided = 0
						                  and   o.concept_id in (1695,730,165515)
						                  and   e.encounter_datetime <= :endDate
						                  and   e.encounter_type=13
						                  and   e.location_id=:location 
						                  )cd4
						                )peneultimoCd4 on peneultimoCd4.patient_id=ultimoCd4.patient_id
						                 where peneultimoCd4.data_resultado_peneultimo_cd4_lab<ultimoCd4.data_resultado_cd4_lab
						                 group by ultimoCd4.patient_id
						                )cd4Final
						                inner join encounter e on e.patient_id=cd4Final.patient_id
						                inner join obs o on o.encounter_id=e.encounter_id 
						                where o.voided=0 and o.concept_id in (1695,730,165515) and e.encounter_datetime=cd4Final.data_resultado_peneultimo_cd4_lab and  e.encounter_type=13 
						                group by cd4Final.patient_id
						      )cd4FinalLAB on cd4FinalLAB.patient_id=coorteFinal.patient_id
			                   left join
			                      ( 
								select patient_id, encounter_datetime, group_concat(motivoEstadioClinico) motivoEstadio, tipoEstadio 
								from( 
									select estadiamentoClinico.patient_id, encounter_datetime encounter_datetime, 
										case estadiamentoClinico.value_coded 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi (SK)' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarreia Crónica' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarréia Crónica > 1 Mês' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
										end as motivoEstadioClinico, tipoEstadio 
									from (
									    select final.patient_id,min(final.encounter_datetime) encounter_datetime,final.value_coded,final.tipoEstadio 
									    from
									    (
								   		select estadio4.patient_id, estadio4.encounter_datetime,o.value_coded, 4 as tipoEstadio 
								   		from( 
								   			select p.patient_id,e.encounter_datetime encounter_datetime 
								   			from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on o.encounter_id=e.encounter_id 
											where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
												and o.obs_datetime <= :endDate 
												--group by p.patient_id 
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
												and o.obs_datetime <= :endDate 
												--group by p.patient_id 
										) estadio3 
											inner join encounter e on e.patient_id = estadio3.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio3.encounter_datetime 
										where e.voided = 0 and o.voided = 0 and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334) 
										 )final group by final.patient_id
										)estadiamentoClinico order by estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime 
									)estadiamentoClinico group by estadiamentoClinico.patient_id 
								)estadiamentoClinico on estadiamentoClinico.patient_id = coorteFinal.patient_id 
								left join
								(
								select patient_id, encounter_datetime, group_concat(motivoEstadioClinico) motivoEstadio, tipoEstadio 
								from( 
									select estadiamentoClinico.patient_id, encounter_datetime encounter_datetime, 
										case estadiamentoClinico.value_coded 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi (SK)' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarreia Crónica' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarréia Crónica > 1 Mês' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
										end as motivoEstadioClinico, tipoEstadio 
									from (
									    select final.patient_id,final.encounter_datetime encounter_datetime,final.value_coded,final.tipoEstadio 
									    from
									    (
								   		select estadio4.patient_id, estadio4.encounter_datetime,o.value_coded, 4 as tipoEstadio 
								   		from( 
								   			select p.patient_id,e.encounter_datetime encounter_datetime 
								   			from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on o.encounter_id=e.encounter_id 
											where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
												and o.obs_datetime <= :endDate 
												--group by p.patient_id 
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
												and o.obs_datetime <= :endDate 
												--group by p.patient_id 
										) estadio3 
											inner join encounter e on e.patient_id = estadio3.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio3.encounter_datetime 
										where e.voided = 0 and o.voided = 0 and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334) 
										 )final 
										)estadiamentoClinico order by estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime 
									)estadiamentoClinico group by estadiamentoClinico.patient_id 
								)estadiamentoClinicoFinal on estadiamentoClinicoFinal.patient_id=coorteFinal.patient_id
								
								left join
								(
								   	    select  f.patient_id,
		                                    f.data_resultado data_ultimo_resultado_cv,
		                                    if(f.comments is not null,concat(f.resultado,' ',f.comments),f.resultado) resultado_cv 
		                              from 
		                              (
		                                select final.patient_id,final.data_resultado,final.resultado,final.comments from
		                                (           	
		                               select final.patient_id,final.data_resultado data_resultado,o.value_numeric as resultado,o.comments from
		                                           	(
									           select cv.patient_id,max(cv.data_resultado) data_resultado,cv.comments
									            from (
									             select p.patient_id,e.encounter_datetime data_resultado,o.comments
								                  from patient p   
								                  inner join encounter e on p.patient_id = e.patient_id
								                  inner join obs o on o.encounter_id=e.encounter_id   
								                  where p.voided = 0 
								                  and e.voided = 0  
								                  and e.encounter_datetime <=:endDate
								                  and e.encounter_type=6
								                  and o.concept_id=856 
								                  and e.location_id=:location
								                  and o.voided=0
								                  )cv 
								                  group by cv.patient_id
								                  )final
								                  left join encounter e on e.patient_id=final.patient_id
								                  left join obs o on o.encounter_id=e.encounter_id
								                  where e.encounter_type=6 and e.voided=0 and o.voided=0 and e.encounter_datetime=final.data_resultado and o.concept_id=856
								                 
								                  union
							         
								     		   select final.patient_id,final.data_resultado, 
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
									                 else null 
											         end as resultado,o.comments
							                    
							                    from 
							                    (
							                     select p.patient_id,max(e.encounter_datetime)  data_resultado,o.comments
												 from patient p   
												 inner join encounter e on p.patient_id = e.patient_id   
												 inner join obs o on o.encounter_id = e.encounter_id   
											     where p.voided = 0 
						                         and  e.voided = 0  
								                 and  o.voided = 0     
												 and  o.concept_id=1305
												 and  e.encounter_type=6
												 and  e.encounter_datetime<=:endDate
												 and  e.location_id=:location
												 group by p.patient_id
											 )final
							                  left join encounter e on e.patient_id=final.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  WHERE e.encounter_type=6 and o.concept_id=1305 and e.voided=0 and o.voided=0  and o.obs_datetime=final.data_resultado
							                  )final order by final.data_resultado desc
							                  )f group by patient_id order by data_resultado asc
								)ultimoCV on ultimoCV.patient_id=coorteFinal.patient_id 
								left join
								(
						     	 		   select finalCV.*,
		                                           if(final.comments is not null,concat(final.resultado_anterior,' ',final.comments),final.resultado_anterior) resultado_cv_anterior
		                                            
		                                           from
		                                           (
		                                           select lastCV.*,
		                                           max(e.encounter_datetime) data_ultimo_resultado_cv_anterior
		                                           
		                                           from
		                                           (
		                                           			   	    select  f.patient_id,
		                                    f.data_resultado data_ultimo_resultado_cv,
		                                    if(f.comments is not null,concat(f.resultado,' ',f.comments),f.resultado) resultado_cv 
		                              from 
		                              (
		                                select final.patient_id,final.data_resultado,final.resultado,final.comments from
		                                (           	
		                               select final.patient_id,final.data_resultado data_resultado,o.value_numeric as resultado,o.comments from
		                                           	(
									           select cv.patient_id,max(cv.data_resultado) data_resultado,cv.comments
									            from (
									             select p.patient_id,e.encounter_datetime data_resultado,o.comments
								                  from patient p   
								                  inner join encounter e on p.patient_id = e.patient_id
								                  inner join obs o on o.encounter_id=e.encounter_id   
								                  where p.voided = 0 
								                  and e.voided = 0  
								                  and e.encounter_datetime <=:endDate
								                  and e.encounter_type=6
								                  and o.concept_id=856 
								                  and e.location_id=:location
								                  and o.voided=0
								                  )cv 
								                  group by cv.patient_id
								                  )final
								                  left join encounter e on e.patient_id=final.patient_id
								                  left join obs o on o.encounter_id=e.encounter_id
								                  where e.encounter_type=6 and e.voided=0 and o.voided=0 and e.encounter_datetime=final.data_resultado and o.concept_id=856
								                 
								                  union
							         
								     		   select final.patient_id,final.data_resultado, 
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
									                 else null 
											         end as resultado,o.comments
							                    
							                    from 
							                    (
							                     select p.patient_id,max(e.encounter_datetime)  data_resultado,o.comments
												 from patient p   
												 inner join encounter e on p.patient_id = e.patient_id   
												 inner join obs o on o.encounter_id = e.encounter_id   
											     where p.voided = 0 
						                         and  e.voided = 0  
								                 and  o.voided = 0     
												 and  o.concept_id=1305
												 and  e.encounter_type=6
												 and  e.encounter_datetime<=:endDate
												 and  e.location_id=:location
												 group by p.patient_id
											 )final
							                  left join encounter e on e.patient_id=final.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  WHERE e.encounter_type=6 and o.concept_id=1305 and e.voided=0 and o.voided=0  and o.obs_datetime=final.data_resultado
							                  )final order by final.data_resultado desc
							                  )f group by patient_id order by data_resultado asc
							                  )lastCV 
							                  left join encounter e on e.patient_id=lastCV.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  where e.voided=0 and o.voided=0 and e.encounter_type=6 and date(e.encounter_datetime) < date_sub(date(lastCV.data_ultimo_resultado_cv), interval 3 month) and o.concept_id in(856,1305) and e.location_id=:location
							                  group by lastCV.patient_id
							                  )finalCV
							                  left join
							                  ( 
								              select final.* 
	                                                 from
	                                                 (
	                                                   select p.patient_id,e.encounter_datetime data_resultado_anterior,
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
									               else null 
												   end as resultado_anterior,o.comments
													 from patient p   
													 inner join encounter e on p.patient_id = e.patient_id   
													 inner join obs o on o.encounter_id = e.encounter_id   
												      where p.voided = 0 
							                         and  e.voided = 0  
									                 and  o.voided = 0     
													 and  o.concept_id=1305
													 and  e.encounter_type=6
													 and  e.encounter_datetime<=:endDate
													 and  e.location_id=:location 
													 union
											           select p.patient_id,o.obs_datetime data_resultado_anterior, o.value_numeric as resultado_anterior,o.comments
											                from patient p   
											              inner join encounter e on p.patient_id = e.patient_id   
											              inner join obs o on o.encounter_id = e.encounter_id   
											              where p.voided = 0 
											              and e.voided = 0  
											              and o.voided = 0
											              and  o.concept_id=856
											              and  o.obs_datetime<=:endDate
											              and e.encounter_type=6 
											              and e.location_id=:location 
										              )final
										              ) final on final.patient_id=finalCV.patient_id
										              where date(final.data_resultado_anterior)=date(finalCV.data_ultimo_resultado_cv_anterior) 	
								 )finalCVFC on finalCVFC.patient_id=coorteFinal.patient_id
								left join
								(
								    select  f.patient_id,
		                                    f.data_resultado data_ultimo_resultado_cv_lab,
		                                    if(f.comments is not null,concat(f.resultado,' ',f.comments),f.resultado) resultado_cv_lab 
		                              from 
		                              (
		                                           	
		                              select final.patient_id,final.data_resultado,final.resultado,final.comments from
		                                (           	
		                               select final.patient_id,final.data_resultado data_resultado,o.value_numeric as resultado,o.comments from
		                                           	(
									           select cv.patient_id,max(cv.data_resultado) data_resultado,cv.comments
									            from (
									             select p.patient_id,e.encounter_datetime data_resultado,o.comments
								                  from patient p   
								                  inner join encounter e on p.patient_id = e.patient_id
								                  inner join obs o on o.encounter_id=e.encounter_id   
								                  where p.voided = 0 
								                  and e.voided = 0  
								                  and e.encounter_datetime <=:endDate
								                  and e.encounter_type in(13,51)
								                  and o.concept_id=856 
								                  and e.location_id=:location
								                  and o.voided=0
								                  )cv 
								                  group by cv.patient_id
								                  )final
								                  left join encounter e on e.patient_id=final.patient_id
								                  left join obs o on o.encounter_id=e.encounter_id
								                  where e.encounter_type in(13,51) and e.voided=0 and o.voided=0 and e.encounter_datetime=final.data_resultado and o.concept_id=856
								                 
								                  union
							         
								     		   select final.patient_id,final.data_resultado, 
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
									                 else null 
											         end as resultado,o.comments
							                    
							                    from 
							                    (
							                     select p.patient_id,max(e.encounter_datetime)  data_resultado,o.comments
												 from patient p   
												 inner join encounter e on p.patient_id = e.patient_id   
												 inner join obs o on o.encounter_id = e.encounter_id   
											     where p.voided = 0 
						                         and  e.voided = 0  
								                 and  o.voided = 0     
												 and  o.concept_id=1305
												 and  e.encounter_type in(13,51)
												 and  e.encounter_datetime<=:endDate
												 and  e.location_id=:location
												 group by p.patient_id
											 )final
							                  left join encounter e on e.patient_id=final.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  WHERE e.encounter_type in(13,51) and o.concept_id=1305 and e.voided=0 and o.voided=0  and date(o.obs_datetime)=date(final.data_resultado)
							                  )final order by final.data_resultado desc
							                   )f
								 )ultimoCVLAB  on ultimoCVLAB.patient_id=coorteFinal.patient_id
								left join
								(
										    select finalCV.*,
		                                           if(final.comments is not null,concat(final.resultado_anterior,' ',final.comments),final.resultado_anterior) resultado_cv_anterior_lab
		                                            
		                                           from
		                                           (
		                                           select lastCV.*,
		                                           max(e.encounter_datetime) data_ultimo_resultado_cv_anterior_lab
		                                           
		                                           from
		                                           (
		                                           	select  f.patient_id,
		                                           	        f.data_resultado data_ultimo_resultado_cv,
		                                           	        if(f.comments is not null,concat(f.resultado,' ',f.comments),f.resultado) resultado_cv
		                                           	        
		                                           	from 
		                                           	(
		                                           	
		                                           select final.patient_id,final.data_resultado,final.resultado,final.comments from
		                                (           	
		                               select final.patient_id,final.data_resultado data_resultado,o.value_numeric as resultado,o.comments from
		                                           	(
									           select cv.patient_id,max(cv.data_resultado) data_resultado,cv.comments
									            from (
									             select p.patient_id,e.encounter_datetime data_resultado,o.comments
								                  from patient p   
								                  inner join encounter e on p.patient_id = e.patient_id
								                  inner join obs o on o.encounter_id=e.encounter_id   
								                  where p.voided = 0 
								                  and e.voided = 0  
								                  and e.encounter_datetime <=:endDate
								                  and e.encounter_type in(13,51)
								                  and o.concept_id=856 
								                  and e.location_id=:location
								                  and o.voided=0
								                  )cv 
								                  group by cv.patient_id
								                  )final
								                  left join encounter e on e.patient_id=final.patient_id
								                  left join obs o on o.encounter_id=e.encounter_id
								                  where e.encounter_type in(13,51) and e.voided=0 and o.voided=0 and e.encounter_datetime=final.data_resultado and o.concept_id=856
								                 
								                  union
							         
								     		   select final.patient_id,final.data_resultado, 
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
									                 else null 
											         end as resultado,o.comments
							                    
							                    from 
							                    (
							                     select p.patient_id,max(e.encounter_datetime)  data_resultado,o.comments
												 from patient p   
												 inner join encounter e on p.patient_id = e.patient_id   
												 inner join obs o on o.encounter_id = e.encounter_id   
											     where p.voided = 0 
						                         and  e.voided = 0  
								                 and  o.voided = 0     
												 and  o.concept_id=1305
												 and  e.encounter_type in(13,51)
												 and  e.encounter_datetime<=:endDate
												 and  e.location_id=:location
												 group by p.patient_id
											 )final
							                  left join encounter e on e.patient_id=final.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  WHERE e.encounter_type in(13,51) and o.concept_id=1305 and e.voided=0 and o.voided=0  and date(o.obs_datetime)=date(final.data_resultado)
							                  )final order by final.data_resultado desc
							                  )f group by patient_id order by data_resultado desc
							                  )lastCV 
							                  left join encounter e on e.patient_id=lastCV.patient_id
							                  left join obs o on o.encounter_id=e.encounter_id
							                  where e.voided=0 and o.voided=0 and e.encounter_type in(13,51) and e.encounter_datetime < date_sub(lastCV.data_ultimo_resultado_cv, interval 3 month) and o.concept_id in(856,1305) and o.location_id=:location
							                  group by lastCV.patient_id
							                  )finalCV
							                  left join
							                  ( 
								              select final.* 
	                                                 from
	                                                 (
	                                                   select p.patient_id,e.encounter_datetime data_resultado_anterior,
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
									              else null 
												 end as resultado_anterior,o.comments
												 from patient p   
												 inner join encounter e on p.patient_id = e.patient_id   
												 inner join obs o on o.encounter_id = e.encounter_id   
											      where p.voided = 0 
						                               and  e.voided = 0  
								                     and  o.voided = 0     
												 and  o.concept_id=1305
												 and  e.encounter_type in(13,51)
												 and  e.encounter_datetime<=:endDate
												 and  e.location_id=:location 
												 union
										           select p.patient_id,e.encounter_datetime data_resultado_anterior, o.value_numeric as resultado_anterior,o.comments
										                from patient p   
										              inner join encounter e on p.patient_id = e.patient_id   
										              inner join obs o on o.encounter_id = e.encounter_id   
										              where p.voided = 0 
										              and e.voided = 0  
										              and o.voided = 0
										              and  o.concept_id=856
										              and  e.encounter_datetime<=:endDate
										              and e.encounter_type in(13,51) 
										              and e.location_id=:location 
									              )final
									              ) final on final.patient_id=finalCV.patient_id
									              where date(final.data_resultado_anterior)=date(finalCV.data_ultimo_resultado_cv_anterior_lab) 
							   )finalCVLAB on finalCVLAB.patient_id=coorteFinal.patient_id
								group by coorteFinal.patient_id