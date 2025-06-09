        select coorte12meses_final.patient_id as patient_id
            from 
            (
            %s
            ) coorte12meses_final 
            inner join person p on p.person_id=coorte12meses_final.patient_id        
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
            ) pad3 on pad3.person_id=coorte12meses_final.patient_id              
            left join            
            (   select pn1.* 
                from person_name pn1 
                inner join  
                ( 
                    select person_id,max(person_name_id) id  
                    from person_name 
                    where voided=0 
                    group by person_id 
                ) pn2 
                where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
            ) pn on pn.person_id=coorte12meses_final.patient_id          
            left join 
            (   select pid1.* 
                from patient_identifier pid1 
                inner join 
                ( 
                    select patient_id,max(patient_identifier_id) id 
                    from patient_identifier 
                    where voided=0 
                    group by patient_id 
                ) pid2 
                where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
            ) pid on pid.patient_id=coorte12meses_final.patient_id 
                left join                                                                                                                                                                                                                                                   
            (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
                and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
                and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
                and curdate() and e.location_id=:location  
                and pe.gender = 'F'                                                                                                                             
                union                                                                                                                                                                                                                                               
                select p.patient_id,e.encounter_datetime data_gravida from patient p 
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
                and  e.encounter_type in (5,6)                                                                                                                                                   
                    and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
                    and curdate() and e.location_id=:location
                    and pe.gender = 'F'                                                                                                                                
                union                                                                                                                                                                                                                                               
                select p.patient_id,e.encounter_datetime data_gravida from patient p  
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
                and e.encounter_type in (5,6)                                                                                                                                                    
                    and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
                    and curdate() and e.location_id=:location  
                    and pe.gender = 'F'                                                                                                                              
                union                                                                                                                                                                                                                                               
                select p.patient_id,e.encounter_datetime data_gravida from patient p 
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
                    and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
                    and curdate() and e.location_id=:location     
                    and pe.gender = 'F'                                                                                            
                union                                                                                                                                                                                                                                               
                select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
                inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
                where pp.program_id=8 and pp.voided=0 
                and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
                and curdate() and pp.location_id=:location  
                and pe.gender = 'F'                                                                                              
                union                                                                                                                                                                                                                                               
                select p.patient_id,obsART.value_datetime data_gravida from patient p  
                 inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                    inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
                where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
                and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
                    and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
                    and curdate() and e.location_id=:location 
                    and  obsART.concept_id=1190 and obsART.voided=0
                    and pe.gender = 'F'                                                                               
                union                                                                                                                                                                                                                                               
                select p.patient_id,data_colheita.value_datetime data_gravida from patient p
                    inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
                    inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
                where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
                and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
                  and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
                  and curdate() and e.location_id=:location   
                  and pe.gender = 'F'                                                                                       
            )                                                                                                                                                                                                                                                       
           gravida_real on gravida_real.patient_id=coorte12meses_final.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
        left join                                                                                                                                                                                                                                                   
            (   select p.patient_id,o.value_datetime data_parto from patient p 
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
                and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
                and curdate() and e.location_id=:location
                and pe.gender = 'F'                                      
                union                                                                                                                                                                                                                                               
                select p.patient_id, e.encounter_datetime data_parto from patient p 
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
                and value_coded=1065 and  e.encounter_type=6 
                and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
                and curdate() and e.location_id=:location  
                and pe.gender ='F'                  
                union                                                                                                                                                                                                                                               
                select p.patient_id, obsART.value_datetime data_parto from patient p  
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                    inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
                where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
                and o.value_coded=1065 and  e.encounter_type=53 
                and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
                and  obsART.concept_id=1190 and obsART.voided=0  
                and pe.gender ='F'    
                union                                                                                                                                                                                                                                               
                select p.patient_id, e.encounter_datetime data_parto from patient p 
                inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
                and value_coded=6332 and  e.encounter_type in (5,6) 
                and pe.gender = 'F'
                and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
                union                                                                                                                                                                                                                                               
                select pg.patient_id,ps.start_date data_parto from patient p    
                    inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
                    inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
                    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
                where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
                and pe.gender = 'F'  
                and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
               union                                                                                                                                                                                                                                                
             select p.patient_id,data_colheita.value_datetime data_parto from patient p    
                    inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
                    inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
                    inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
                    inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
                where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
                and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
                and pe.gender = 'F'                                                                                             
                and data_colheita.concept_id =23821 and data_colheita.value_datetime 
                between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
                )                                                                                                                                                                                                                                                       
     lactante_real on lactante_real.patient_id = coorte12meses_final.patient_id                                                                                                                          
        and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
        and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
     left join 
     (
     	 select patient_id, max(data_estado) state_date, state, source from 
	     (
	     select patient_id, data_estado,state, source from 
	     (
		 select max_estado.patient_id, max_estado.start_date data_estado, ps.state,3 as source
		from(
				select pp.patient_id, max(max_estado.start_date) start_date,ps.patient_state_id,ps.state
				from(
					select pg.patient_id, ps.start_date,ps.voided,ps.state
					from patient p
							inner join patient_program pg on p.patient_id = pg.patient_id
					  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id
					where pg.voided=0  and p.voided=0 and  pg.program_id = 2 and ps.voided=0
						and ps.start_date  <= curdate()  and pg.location_id=:location 
	                    order by pg.patient_id, ps.patient_state_id desc, ps.start_date desc
				)max_estado
					inner join patient_program pp on pp.patient_id = max_estado.patient_id
				 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.start_date and max_estado.state=ps.state
		  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location
				group by pp.patient_id
		) max_estado
		inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id
		inner join patient_program pp on pp.patient_program_id = ps.patient_program_id
     union
    select  p.patient_id, 
             e.encounter_datetime data_estado, o.value_coded state, 2 as source
    from    patient p 
            inner join encounter e on p.patient_id=e.patient_id 
            inner join obs  o on e.encounter_id=o.encounter_id 
            inner join location on (location.location_id = e.location_id and location.retired =0) 
    where   e.voided=0 and o.voided=0 and p.voided=0 and 
            e.encounter_type = 6 and o.concept_id = 6273 and o.value_coded in (1709,1707,1705,1706,1366,23903) and 
            e.encounter_datetime<=curdate() 
    union
        select  p.patient_id, 
            o.obs_datetime data_estado, o.value_coded state, 1 as source 
    from    patient p 
            inner join encounter e on p.patient_id=e.patient_id 
            inner join obs  o on e.encounter_id=o.encounter_id 
            inner join location on (location.location_id = e.location_id and location.retired =0) 
    where   e.voided=0 and o.voided=0 and p.voided=0 and 
            e.encounter_type = 53 and o.concept_id = 6272 and o.value_coded in (1709,1707,1705,1706,1366,23903) and 
            o.obs_datetime<=curdate()
            ) final order by patient_id, data_estado desc, source 
            ) final group by patient_id
     ) estadoPermanencia on estadoPermanencia.patient_id = coorte12meses_final.patient_id
     inner join  
	( 
		select distinct max_filaFinal.patient_id,max_filaFinal.data_fila, 
		case  o.value_coded 
		when 1703 then 'AZT+3TC+EFV' 
		when 6100 then 'AZT+3TC+LPV/r' 
		when 1651 then 'AZT+3TC+NVP' 
		when 6324 then 'TDF+3TC+EFV' 
		when 6104 then 'ABC+3TC+EFV' 
		when 23784 then 'TDF+3TC+DTG' 
		when 23786 then 'ABC+3TC+DTG' 
		when 6116 then 'AZT+3TC+ABC' 
		when 6106 then 'ABC+3TC+LPV/r' 
		when 6105 then 'ABC+3TC+NVP' 
		when 6108 then 'TDF+3TC+LPV/r' 
		when 23790 then 'TDF+3TC+LPV/r+RTV' 
		when 23791 then 'TDF+3TC+ATV/r' 
		when 23792 then 'ABC+3TC+ATV/r' 
		when 23793 then 'AZT+3TC+ATV/r' 
		when 23795 then 'ABC+3TC+ATV/r+RAL' 
		when 23796 then 'TDF+3TC+ATV/r+RAL' 
		when 23801 then 'AZT+3TC+RAL' 
		when 23802 then 'AZT+3TC+DRV/r' 
		when 23815 then 'AZT+3TC+DTG' 
		when 6329 then 'TDF+3TC+RAL+DRV/r' 
		when 23797 then 'ABC+3TC+DRV/r+RAL' 
		when 23798 then '3TC+RAL+DRV/r' 
		when 23803 then 'AZT+3TC+RAL+DRV/r' 
		when 6243 then 'TDF+3TC+NVP' 
		when 6103 then 'D4T+3TC+LPV/r' 
		when 792 then 'D4T+3TC+NVP' 
		when 1827 then 'D4T+3TC+EFV' 
		when 6102 then 'D4T+3TC+ABC' 
		when 1311 then 'ABC+3TC+LPV/r' 
		when 1312 then 'ABC+3TC+NVP' 
		when 1313 then 'ABC+3TC+EFV' 
		when 1314 then 'AZT+3TC+LPV/r' 
		when 1315 then 'TDF+3TC+EFV'    
		when 6330 then 'AZT+3TC+RAL+DRV/r' 
		when 6325 then 'D4T+3TC+ABC+LPV/r' 
		when 6326 then 'AZT+3TC+ABC+LPV/r' 
		when 6327 then 'D4T+3TC+ABC+EFV' 
		when 6328 then 'AZT+3TC+ABC+EFV' 
		when 6109 then 'AZT+DDI+LPV/r' 
		when 21163 then 'AZT+3TC+LPV/r' 
		when 23799 then 'TDF+3TC+DTG ' 
		when 23800 then 'ABC+3TC+DTG '  
		when 6110 then  'D4T20+3TC+NVP' 
		when 1702 then 'AZT+3TC+NFV' 
		when 817  then 'AZT+3TC+ABC' 
		when 6244 then 'AZT+3TC+RTV' 
		when 1700 then 'AZT+DDl+NFV' 
		when 633  then 'EFV' 
		when 625  then 'D4T' 
		when 631  then 'NVP' 
		when 628  then '3TC' 
		when 635  then 'NFV' 
		when 797  then 'AZT' 
		when 814  then 'ABC' 
		when 6107 then 'TDF+AZT+3TC+LPV/r' 
		when 6236 then 'D4T+DDI+RTV-IP' 
		when 1701 then 'ABC+DDI+NFV' 
		when 6114 then 'AZT60+3TC+NVP' 
		when 6115 then '2DFC+EFV' 
		when 6233 then 'AZT+3TC+DDI+LPV' 
		when 6234 then 'ABC+TDF+LPV' 
		when 6242 then 'D4T+DDI+NVP' 
		when 6118 then 'DDI50+ABC+LPV' 
		when 23785 then 'TDF+3TC+DTG2' 
		when 5424 then 'OUTRO MEDICAMENTO ANTI-RETROVIRAL'
		else null end as code, 
		drug.D4,drug.D3,drug.D2,drug.D1,obs_proximo.value_datetime proximo_levantamento from ( 
		select * from (
		Select p.patient_id,encounter_datetime data_fila,e.encounter_id  from  patient p  
		inner join encounter e on e.patient_id=p.patient_id 
		where p.voided=0 and e.voided=0 and e.encounter_type=18 and   
		e.location_id=:location and e.encounter_datetime >= :startDate and e.encounter_datetime <=:endDate
		order by e.patient_id, e.encounter_id desc  
		)maxConsulta group by maxConsulta.patient_id
		) max_filaFinal  
		inner join obs o on o.person_id=max_filaFinal.patient_id and o.concept_id=1088 and o.encounter_id=max_filaFinal.encounter_id and o.voided=0 
		inner join obs obs_proximo on obs_proximo.person_id=max_filaFinal.patient_id and obs_proximo.concept_id=5096 and obs_proximo.encounter_id=max_filaFinal.encounter_id and obs_proximo.voided=0 
		left join 
		(  
					select  
			@num_drugs := 1 + LENGTH(drugname) - LENGTH(REPLACE(drugname, ',', '')) AS num_drugs,  
			SUBSTRING_INDEX(drugname, ',', 1) AS D1,  
			IF(@num_drugs > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(drugname, ',', 2), ',', -1), '') AS D2,  
			IF(@num_drugs > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(drugname, ',', 3), ',', -1), '') AS D3,  
			IF(@num_drugs > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(drugname, ',', 4), ',', -1), '') AS D4,  
			drug.person_id, drug.encounter_datetime  from 
			(  
			select formulacoes.person_id, formulacoes.encounter_datetime, formulacoes.drugname from (
			select formulacao.person_id as person_id, e.encounter_datetime encounter_datetime, e.encounter_id, group_concat(drug1.name order by formulacao.person_id, formulacao.encounter_id desc, formulacao.value_drug  DESC) drugname from encounter e  
			join obs grupo on grupo.encounter_id=e.encounter_id  
			join obs formulacao on formulacao.encounter_id=e.encounter_id  
			inner join drug drug1 on formulacao.value_drug=drug1.drug_id  
			where formulacao.concept_id = 165256  
			and  grupo.concept_id =165252  
			and formulacao.obs_group_id = grupo.obs_id  
			and formulacao.voided=0  
			and grupo.voided=0  
			and e.voided=0  
			and e.encounter_datetime <=:endDate  
			and e.location_id=:location
			and e.encounter_type = 18
			group by formulacao.person_id, e.encounter_datetime, e.encounter_id 
			order by grupo.person_id, e.encounter_datetime desc, e.encounter_id desc
			)formulacoes inner join
			(
			select * from (
			SELECT e.patient_id, e.encounter_id FROM encounter e 
			where e.encounter_type = 18 and e.encounter_datetime <= :endDate
			and e.voided = 0 and e.location_id=:location
			order by e.patient_id, e.encounter_id desc
			)maxConsulta group by maxConsulta.patient_id
			)maxConsulta on maxConsulta.patient_id = formulacoes.person_id and formulacoes.encounter_id = maxConsulta.encounter_id
			) drug  
		) drug on drug.person_id=max_filaFinal.patient_id and max_filaFinal.data_fila=drug.encounter_datetime 
	)max_filaF  on max_filaF.patient_id=coorte12meses_final.patient_id 
	group by coorte12meses_final.patient_id 