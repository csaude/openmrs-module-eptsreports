select           TB6.patient_id as patient_id,
                 concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
                 pid.identifier as NID, 
                 case p.gender when 'F' then 'F' when 'M' then 'M' else null end as gender, 
                 floor(datediff(:endDate,p.birthdate)/365) as idade_actual,
                 pat.value telefone,
                 DATE_FORMAT(DATE(tx_new.data_inicio), '%%d/%%m/%%Y') as data_inicio, 
                 DATE_FORMAT(DATE(TB6.encounter_datetime), '%%d/%%m/%%Y') as lastClinicalConsultationDate,
                 DATE_FORMAT(DATE(TB6.screeningDate), '%%d/%%m/%%Y') as firstPositiveTbScreening,
                 DATE_FORMAT(DATE(TB6.genExpertRequestDate), '%%d/%%m/%%Y') as genExpertRequestDate,
		         DATE_FORMAT(DATE(TB6.lastBKResquestDate), '%%d/%%m/%%Y') as lastBKResquestDate,
		         DATE_FORMAT(DATE(TB6.lastTbLamRequestDate), '%%d/%%m/%%Y') as lastTbLamRequestDate,
		         DATE_FORMAT(DATE(TB6.tbTreatmentInitialDate), '%%d/%%m/%%Y') as tbTreatmentInitialDate,
		         DATE_FORMAT(DATE(TB6.tbTretment6MonthsDate), '%%d/%%m/%%Y') as tbTretment6MonthsDate,
		         DATE_FORMAT(DATE(TB6.lastGenExpertResultDate), '%%d/%%m/%%Y') as lastGenExpertResultDate,
		         case TB6.tipoTestePcrTBLabFormDate when 664 then 'Negativo' when 703 then 'Positivo'  when 165190 then 'Traco' when 6230 then 'Detectado - Alto' when 6229 then 'Detectado - Médio' when 6228 then 'Detectado - Baixo' when 165587 then 'Detectado- Muito Baixo'  else null end as tipoTestePcrTBLabFormDate, 
		         if(TB6.lastGenExpertResultDate = 703, 'Positivo',if(TB6.lastGenExpertResultDate = 664, 'Negativo','')) as genExpertResult,
		         if(TB6.resistenciaRinfapinaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaRinfapinaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaRinfapinaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaRinfapinaLabFormDate,
			     if(TB6.resistenciakanimicinaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciakanimicinaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciakanimicinaLabFormDate = 1138, 'Indeterminado',''))) as resistenciakanimicinaLabFormDate,
			     if(TB6.resistenciaAmikacinaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaAmikacinaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaAmikacinaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaAmikacinaLabFormDate,
			     if(TB6.resistenciaCepreomicinaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaCepreomicinaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaCepreomicinaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaCepreomicinaLabFormDate,
			     if(TB6.resistenciaEtheonamidaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaEtheonamidaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaEtheonamidaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaEtheonamidaLabFormDate,
			     if(TB6.resistenciaFloroquinolonaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaFloroquinolonaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaFloroquinolonaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaFloroquinolonaLabFormDate,
			     if(TB6.resistenciaIsoneazidaLabFormDate = 1065, 'Resistência Detectada',if(TB6.resistenciaIsoneazidaLabFormDate = 1066, 'Resistência Não-Detectada ',if(TB6.resistenciaIsoneazidaLabFormDate = 1138, 'Indeterminado',''))) as resistenciaIsoneazidaLabFormDate,
                 if(TB6.lastBKResultDate = 703, 'Positivo',if(TB6.lastBKResultDate = 664, 'Negativo','')) as baciloscopiaResult,
                 if(TB6.lastBKLabResultDate = 703, 'Positivo',if(TB6.lastBKLabResultDate = 165184, 'Não encontrado','')) as baciloscopiaLabResult,
                 if(TB6.lastTbLamResultDate = 703, 'Positivo',if(TB6.lastTbLamResultDate = 664, 'Negativo','')) as tbLamResult,
                 if(TB6.lastTbLamLabResultDate = 703, 'Positivo',if(TB6.lastTbLamLabResultDate = 664, 'Negativo',if(TB6.lastTbLamLabResultDate = 1138, 'Indeterminado',''))) as tbLamLabResult       
from
(
select final.*,
       		   pedidos.genExpertRequestDate,
		       pedidos.lastBKResquestDate,
		       pedidos.lastTbLamRequestDate,
		       pedidos.tbTreatmentInitialDate,
		       pedidos.tbTretment6MonthsDate,
       		   resultados.lastGenExpertResultDate,
			   resultados.lastBKResultDate,
			   resultados.lastTbLamResultDate,
			   resultados.gExpertLabResultDate,
			   resultados.gExpertLabResult,
			   resultados.rifampinLabResultDate,
			   resultados.lastBKLabResultDate,
		       resultados.lastTbLamLabResultDate,
		       resultados.tipoTestePcrTBLabFormDate,
		       resultados.resistenciaRinfapinaLabFormDate,
		       resultados.resistenciaAmikacinaLabFormDate,
		       resultados.resistenciakanimicinaLabFormDate,
		       resultados.resistenciaCepreomicinaLabFormDate,
		       resultados.resistenciaEtheonamidaLabFormDate,
		       resultados.resistenciaFloroquinolonaLabFormDate,
		       resultados.resistenciaIsoneazidaLabFormDate
			 from
			(
			 select coorte12meses_final.*,maxEnc.encounter_datetime,positiveTbScreening.screeningDate,p.gender
                  
            from 
            (
            %s
            ) coorte12meses_final 
            inner join
            (
		    Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p 
		    inner join encounter e on p.patient_id=e.patient_id 
		    where p.voided=0 and e.voided=0 and e.encounter_type=6 and 
		    e.encounter_datetime<=curdate()  and e.location_id=:location 
		    group by p.patient_id 
		     )maxEnc on maxEnc.patient_id = coorte12meses_final.patient_id
    
		    inner join 
		    (
			select TBPositive.patient_id,min(TBPositive.data_tratamento) screeningDate
			from(
					select p.patient_id,o.value_datetime data_tratamento                                                           
					from patient p                                                                                                   
						inner join encounter e on p.patient_id=e.patient_id                                                         
					    inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
						and e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id=:location 
			     		and  o.value_datetime  between :startDate and :endDate
			     	union
					select p.patient_id,e.encounter_datetime data_tratamento                                                           
					from patient p                                                                                                   
						inner join encounter e on p.patient_id=e.patient_id                                                         
					    inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
					      and e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded =1065 and e.location_id=:location 
					      and  e.encounter_datetime  between :startDate and :endDate
					 union
					select p.patient_id,e.encounter_datetime data_tratamento                                                           
			        from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			     		and e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded = 703  and e.location_id=:location 
			     		and e.encounter_datetime  between :startDate and :endDate
			     	union
			     	select  p.patient_id,e.encounter_datetime data_inicio                                                           
					from patient p                                                                                                   
						inner join encounter e on p.patient_id=e.patient_id                                                         
						inner join obs obs6277 on obs6277.encounter_id= e.encounter_id 
						inner join obs obs6257 on obs6257.encounter_id= e.encounter_id                                                           
					where p.voided = 0 and e.voided = 0 and obs6277.voided = 0 and obs6257.voided = 0                                                                  
						and e.encounter_type in (6,9) and obs6277.concept_id=6277 and obs6277.value_coded = 664 and e.location_id=:location and obs6257.concept_id= 6257 and obs6257.value_coded in(1065,1066)
						and e.encounter_datetime  between :startDate and :endDate
					union
					select p.patient_id,o.obs_datetime data_tratamento                                                           
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			      		and e.encounter_type=53 and o.concept_id=1406 and o.value_coded=42  and e.location_id=:location 
			     		and o.obs_datetime  between :startDate and :endDate
			     	union
					select pg.patient_id, date_enrolled data_tratamento                                                             
					from patient p 
			        	inner join patient_program pg on p.patient_id=pg.patient_id                                       
					where pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location  
			        	and date_enrolled between :startDate and :endDate
			        union
					select p.patient_id,e.encounter_datetime data_tratamento                                                           
					from patient p                                                                                                   
						inner join encounter e on p.patient_id=e.patient_id                                                         
						inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
						and e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location 
					    and  e.encounter_datetime  between :startDate and  :endDate
					union
					select p.patient_id,e.encounter_datetime data_tratamento                                                           
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			      		and e.encounter_type=6 and o.concept_id=23758 and o.value_coded=1065  and e.location_id=:location 
			     		and e.encounter_datetime  between :startDate and :endDate
			     	union
					select p.patient_id,e.encounter_datetime data_tratamento                                                           
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id                                                           
					where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			      		and e.encounter_type=6 and o.concept_id=23761 and o.value_coded=1065  and e.location_id=:location 
			     		and e.encounter_datetime  between :startDate and :endDate
					union
					select p.patient_id,e.encounter_datetime data_inicio                                                           
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			     		and e.encounter_type = 6 and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,23760,161)   
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate
			      	union
			      	select p.patient_id,e.encounter_datetime data_inicio                                                      
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			      		and e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate
			      	union
					select p.patient_id,e.encounter_datetime data_inicio                                                      
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			     		and e.encounter_type=6 and o.concept_id  in(23723,23774,23951,307,12) and o.value_coded is not null
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate
			      
					union
					select p.patient_id,e.encounter_datetime data_inicio                                                      
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			     		and e.encounter_type=13 and o.concept_id  in(307, 23723, 23774, 23951, 165588) and o.value_coded is not null
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate
			      	union
			         select p.patient_id,e.encounter_datetime data_inicio                                                      
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			     		and e.encounter_type=13 and o.concept_id=23774 and o.value_coded is not null
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate 
			 		union
			 		select p.patient_id,e.encounter_datetime data_inicio                                                      
					from patient p                                                                                                   
			      		inner join encounter e on p.patient_id=e.patient_id                                                         
			      		inner join obs o on o.encounter_id=e.encounter_id
					where e.voided=0 and o.voided=0 and p.voided=0                                                             
			      		and e.encounter_type= 51 and o.concept_id = 23951 and o.value_coded is not null
			      		and e.location_id=:location  and e.encounter_datetime   between :startDate and :endDate
			)TBPositive group by TBPositive.patient_id
		    ) positiveTbScreening on positiveTbScreening.patient_id = coorte12meses_final.patient_id
		    inner join person p on p.person_id=coorte12meses_final.patient_id        
		    )final
		    left join
		    (
		    select final.patient_id, 
		           MAX(CASE WHEN final.source = 'T1' then final.data_pedido END) as genExpertRequestDate,
		           MAX(CASE WHEN final.source = 'T2' then final.data_pedido END) as lastBKResquestDate,
		           MAX(CASE WHEN final.source = 'T3' then final.data_pedido END) as lastTbLamRequestDate,
		           MAX(CASE WHEN final.source = 'T4' then final.data_pedido END) as tbTreatmentInitialDate,
		           MAX(CASE WHEN final.source = 'T5' then final.data_pedido END) as tbTretment6MonthsDate
		     from

			    (
			    select genexpertTest.patient_id, genexpertTest.data_pedido, "T1" as source from
			    (
			    select p.patient_id, e.encounter_datetime data_pedido  from patient p 
			    inner join encounter e on e.patient_id=p.patient_id 
			    inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
			    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
			    e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
			    and obsLabResearch.value_coded = 23723 and obsLabResearch.voided=0 
			    )genexpertTest
			    union
			    select baciloscopia.patient_id, baciloscopia.data_pedido, "T2" as source from
			    (
			  	select p.patient_id, e.encounter_datetime data_pedido  from patient p 
			     inner join encounter e on e.patient_id=p.patient_id 
				inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
				where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
				e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
				and obsLabResearch.value_coded = 307 and obsLabResearch.voided=0 
			    )baciloscopia
			    union
			    select tbLam.patient_id, tbLam.data_pedido, "T3" as source from
			    (
			    select p.patient_id, e.encounter_datetime data_pedido  from patient p 
			    inner join encounter e on e.patient_id=p.patient_id 
			    inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
			    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
			    e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
			    and obsLabResearch.value_coded = 23951 and obsLabResearch.voided=0 
			    )tbLam
			    union
			    select tbTreatment.patient_id, tbTreatment.data_inicio, "T4" as source from
			    (
			    select  pg.patient_id,date_enrolled data_inicio from  patient p  
			    inner join patient_program pg on p.patient_id=pg.patient_id                         
			    where pg.voided=0 and p.voided=0 and program_id=5 and date_enrolled between :startDate and  curdate()  and location_id=:location 
			    union 
			    select p.patient_id,obsTB.obs_datetime data_inicio from patient p  
			    inner join encounter e on e.patient_id=p.patient_id  
			    inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
			    where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
			    e.location_id=:location and e.encounter_type=53 and obsTB.concept_id=1406 and obsTB.value_coded =42 and obsTB.voided=0 
			    union                 
			    select p.patient_id, obsTB.obs_datetime data_inicio from patient p  
			    inner join encounter e on e.patient_id=p.patient_id  
			    inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
			    where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
			    e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=1268 and obsTB.value_coded in (1256) and obsTB.voided=0  
			    union 
			    select p.patient_id,obsTB.obs_datetime data_inicio from patient p  
			    inner join encounter e on e.patient_id=p.patient_id  
			    inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
			    where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
			    e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=23761 and obsTB.value_coded =1065 and obsTB.voided=0  
			    )tbTreatment
			    union
			 select  tbTretment6Months.patient_id,tbTretment6Months.data_tratamento, "T5" source
			 from
		      (
	 	      Select p.patient_id, o.value_datetime data_tratamento from  patient p  
		      inner join encounter e on p.patient_id=e.patient_id  
		      inner join obs o on e.encounter_id=o.encounter_id  
		      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
		      and o.value_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
		      and o.concept_id=1113 and  e.location_id=:location    
		      union
		      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
		      from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
		      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location 
		      and pg.date_enrolled between  DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate    
		      union
		      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
		      inner join encounter e on p.patient_id=e.patient_id  
		      inner join obs o on e.encounter_id=o.encounter_id  
		      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 
		      and o.obs_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
		      and o.concept_id=1406 and o.value_coded=42 and e.location_id=:location    
		      union 
		      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
		      inner join encounter e on p.patient_id=e.patient_id  
		      inner join obs o on e.encounter_id=o.encounter_id  
		      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 
		      and o.obs_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
		      and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location    
		    )tbTretment6Months 
		    )final
		    group by final.patient_id
		    )pedidos on pedidos.patient_id=final.patient_id
		    left join
		    (
			select final.patient_id, 
			  MAX(CASE WHEN (final.source = 'T1') then final.value_coded END) as lastGenExpertResultDate,
			  MAX(CASE WHEN final.source = 'T2' then final.value_coded END) as lastBKResultDate,
			  MAX(CASE WHEN final.source = 'T3' then final.value_coded END) as lastTbLamResultDate,
			  MAX(CASE WHEN final.source = 'T4' then final.value_coded END) as gExpertLabResultDate,
			  MAX(CASE WHEN final.source = 'T5' then final.value_coded END) as gExpertLabResult,
			  MAX(CASE WHEN final.source = 'T6' then final.value_coded END) as rifampinLabResultDate,
			  MAX(CASE WHEN final.source = 'T7' then final.value_coded END) as lastBKLabResultDate,
		      MAX(CASE WHEN final.source = 'T8' then final.value_coded END) as lastTbLamLabResultDate,
		      MAX(CASE WHEN final.source = 'T9' then final.value_coded END) as tipoTestePcrTBLabFormDate,
		      MAX(CASE WHEN final.source = 'T10' then final.value_coded END) as resistenciaRinfapinaLabFormDate,
		      MAX(CASE WHEN final.source = 'T11' then final.value_coded END) as resistenciaAmikacinaLabFormDate,
		      MAX(CASE WHEN final.source = 'T12' then final.value_coded END) as resistenciakanimicinaLabFormDate,
		      MAX(CASE WHEN final.source = 'T13' then final.value_coded END) as resistenciaCepreomicinaLabFormDate,
		      MAX(CASE WHEN final.source = 'T14' then final.value_coded END) as resistenciaEtheonamidaLabFormDate,
		      MAX(CASE WHEN final.source = 'T15' then final.value_coded END) as resistenciaFloroquinolonaLabFormDate,
		      MAX(CASE WHEN final.source = 'T16' then final.value_coded END) as resistenciaIsoneazidaLabFormDate
		from
			(	           
		    select geneXpertResult.patient_id,geneXpertResult.data_resultado,geneXpertResult.value_coded, "T1" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 23723 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
		    )geneXpertResult
		    union
		    select baciloscopiaResult.patient_id,baciloscopiaResult.data_resultado,baciloscopiaResult.value_coded,"T2" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado,obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 307 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
		    )baciloscopiaResult
		    union
		    select tbLamResult.patient_id,tbLamResult.data_resultado,tbLamResult.value_coded, "T3" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 23951 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
		    )tbLamResult
		    union
		    select gExpertLabResult.patient_id,gExpertLabResult.data_resultado,gExpertLabResult.value_coded, "T4" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 23723 
		    and obsTestResult.value_coded in (703,664) and obsTestResult.voided=0
		    )gExpertLabResult
		    union
		    select xpertLabResult.patient_id,xpertLabResult.data_resultado,xpertLabResult.value_coded,"T5"
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado,obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165189 
		    and obsTestResult.value_coded in (1065, 1066) and obsTestResult.voided=0
		    )xpertLabResult
		    union
		    select rifampinResistanceLabResult.patient_id,rifampinResistanceLabResult.data_resultado,rifampinResistanceLabResult.value_coded, "T6" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165192 
		    and obsTestResult.value_coded in (1065, 1066, 1138) and obsTestResult.voided=0
		    )rifampinResistanceLabResult
		    union
		    select baciloscopiaLabResult.patient_id,baciloscopiaLabResult.data_resultado,baciloscopiaLabResult.value_coded,"T7" source
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 307 
		    and obsTestResult.value_coded in (703, 165184) and obsTestResult.voided=0
		    )baciloscopiaLabResult
		    union
		    select tbLamLabResult.patient_id,tbLamLabResult.data_resultado,tbLamLabResult.value_coded, "T8" source 
		    from
		    (
		    select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 23951 
		    and obsTestResult.value_coded in (703, 664, 1138) and obsTestResult.voided=0
		    )tbLamLabResult
		    union
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id in(165588,23723)
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    union
		    select resistenciaRinfapinaLabForm.patient_id,resistenciaRinfapinaLabForm.data_resultado,resistenciaRinfapinaLabForm.value_coded, "T10" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165192
		    group by p.patient_id 
		    )resistenciaRinfapinaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaRinfapinaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaRinfapinaLabForm.data_resultado
		    union
		    select resistenciaAmikacinaLabForm.patient_id,resistenciaAmikacinaLabForm.data_resultado,resistenciaAmikacinaLabForm.value_coded, "T11" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165589
		    group by p.patient_id 
		    )resistenciaAmikacinaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaAmikacinaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaAmikacinaLabForm.data_resultado
		    union
		    select resistenciakanimicinaLabForm.patient_id,resistenciakanimicinaLabForm.data_resultado,resistenciakanimicinaLabForm.value_coded, "T12" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165590
		    group by p.patient_id 
		    )resistenciakanimicinaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciakanimicinaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciakanimicinaLabForm.data_resultado
		    union
		    select resistenciaCepreomicinaLabForm.patient_id,resistenciaCepreomicinaLabForm.data_resultado,resistenciaCepreomicinaLabForm.value_coded, "T13" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165591
		    group by p.patient_id 
		    )resistenciaCepreomicinaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaCepreomicinaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaCepreomicinaLabForm.data_resultado
		    union
		    select resistenciaEtheonamidaLabForm.patient_id,resistenciaEtheonamidaLabForm.data_resultado,resistenciaEtheonamidaLabForm.value_coded, "T14" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165592
		    group by p.patient_id 
		    )resistenciaEtheonamidaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaEtheonamidaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaEtheonamidaLabForm.data_resultado
		    union
		    select resistenciaFloroquinolonaLabForm.patient_id,resistenciaFloroquinolonaLabForm.data_resultado,resistenciaFloroquinolonaLabForm.value_coded, "T15" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165346
		    group by p.patient_id 
		    )resistenciaFloroquinolonaLabForm
		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaFloroquinolonaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaFloroquinolonaLabForm.data_resultado		    
		    union
		    select resistenciaIsoneazidaLabForm.patient_id,resistenciaIsoneazidaLabForm.data_resultado,resistenciaIsoneazidaLabForm.value_coded, "T16" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165347
		    group by p.patient_id 
		    )resistenciaIsoneazidaLabForm

		    inner join
		    (
		    select tipoTestePcrTBLabForm.patient_id,tipoTestePcrTBLabForm.data_resultado,tipoTestePcrTBLabForm.value_coded, "T9" source 
		    from
		    (
		    select p.patient_id, max(e.encounter_datetime) data_resultado, obsTestResult.value_coded  from patient p 
		    inner join encounter e on e.patient_id=p.patient_id 
		    inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
		    where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
		    e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165588
		    group by p.patient_id 
		    )tipoTestePcrTBLabForm
		    )tipoTestePcrTBLabForm on tipoTestePcrTBLabForm.patient_id=resistenciaIsoneazidaLabForm.patient_id and tipoTestePcrTBLabForm.data_resultado=resistenciaIsoneazidaLabForm.data_resultado
		    )final
		    group by final.patient_id
            )resultados on resultados.patient_id=final.patient_id
            WHERE pedidos.tbTretment6MonthsDate is null
            )TB6
             inner join person p on p.person_id=TB6.patient_id        
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
            ) pad3 on pad3.person_id=TB6.patient_id              
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
            ) pn on pn.person_id=TB6.patient_id          
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
            ) pid on pid.patient_id=TB6.patient_id 
            left join person_attribute pat on pat.person_id=TB6.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value <> '' and pat.voided=0 
                left join
    (
    				select patient_id,data_inicio from 
							(
								  select patient_id, min(data_inicio) data_inicio 
								  from 
										(
											  select p.patient_id, min(e.encounter_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on o.encounter_id=e.encounter_id 
											  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
													and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  union 
											  
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
													and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select pg.patient_id, min(date_enrolled) data_inicio 
											  from patient p 
													inner join patient_program pg on p.patient_id=pg.patient_id 
											  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
													group by pg.patient_id 
											  
											  union 
							
											  select e.patient_id, min(e.encounter_datetime) as data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
											  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
													and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id
										) 
								  art_start group by patient_id 
							) tx_new
               )tx_new on tx_new.patient_id=TB6.patient_id
               group by TB6.patient_id
