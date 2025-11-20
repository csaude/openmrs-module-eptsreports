select 
inicioDAH.patient_id, pid.identifier as NID, concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
        DATE_FORMAT(p.birthdate, '%d/%m/%Y') AS data_nascimento, 
        floor(datediff(:endDate,p.birthdate)/365) as idade_actual, 
     p.gender as gender, 
     pat.value contacto, 
     IF(ISNULL(inicioTarv.data_inicio), 'N/A', DATE_FORMAT(inicioTarv.data_inicio, '%d/%m/%Y')) AS data_inicio, 
     IF(ISNULL( ultimoLevantamentoTarv.data_levantamento), 'N/A', DATE_FORMAT(ultimoLevantamentoTarv.data_levantamento, '%d/%m/%Y')) AS data_ultimo_levantamento, 
     IF(ISNULL(transferredIn.dataTransferido), 'Não', 'Sim') AS transferido_de, 
     case 
     when estadoPermanencia.RespostaEstadoPermanencia = 1 then 'Abandono' 
     when estadoPermanencia.RespostaEstadoPermanencia = 2 then 'Óbito' 
     when estadoPermanencia.RespostaEstadoPermanencia = 3 then 'Suspenso' 
     when estadoPermanencia.RespostaEstadoPermanencia = 4 then 'Activo' 
     when estadoPermanencia.RespostaEstadoPermanencia = 5 then 'Reinício' 
     when ISNULL(estadoPermanencia.RespostaEstadoPermanencia) then 'N/A' 
     end as ultimoEstadoPermanencia, 
          case 
     when situacaoTARVnoDAH.value_coded = 1256 then 'Novo Início' 
     when situacaoTARVnoDAH.value_coded = 1705 then 'Reinício' 
     when situacaoTARVnoDAH.value_coded = 6276 then 'Em TARV' 
     when situacaoTARVnoDAH.value_coded = 6275 then 'Pré TARV' 
     when ISNULL(situacaoTARVnoDAH.value_coded) then 'N/A' 
     end as tarvDAH, 
     IF(ISNULL(seguimentoDAH.encounter_datetime), 'N/A', DATE_FORMAT(seguimentoDAH.encounter_datetime, '%d/%m/%Y')) AS data_inicioDAH, 
     IF(ISNULL(cd4EligibilidadeMDSDAH.max_data_cd4), 'N/A',DATE_FORMAT(cd4EligibilidadeMDSDAH.max_data_cd4, '%d/%m/%Y')) AS max_data_cd4, 
     IF(ISNULL(cd4EligibilidadeMDSDAH.value_numeric), 'N/A',IF(type=1, cd4EligibilidadeMDSDAH.value_numeric,'<=200')) AS ultimoResultadoCD4, 
     IF(ISNULL(estadiamentoClinico.encounter_datetime), 'N/A', DATE_FORMAT(estadiamentoClinico.encounter_datetime, '%d/%m/%Y')) AS data_registo_estadiamento, 
     case 
        when estadiamentoClinico.tipoEstadio=3 then 'Estadio III' 
        when estadiamentoClinico.tipoEstadio=4 then 'Estadio IV' 
        when ISNULL(estadiamentoClinico.tipoEstadio) then 'N/A' 
        end as tipo_estadio, 
      @motivoIndice := 1 + LENGTH(estadiamentoClinico.motivoEstadio) - LENGTH(REPLACE(estadiamentoClinico.motivoEstadio, ',', '')) AS motivoIndice, 
      IF(ISNULL(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 1)), 'N/A', SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 1)) AS motivoEstadio1, 
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 2), ',', -1), '') = '', 'N/A', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 2), ',', -1), '')) AS motivoEstadio2, 
      IF(ISNULL( valor_ultimo_cd4.value_numeric), 'N/A', valor_ultimo_cd4.value_numeric) AS lastCd4Result, 
      IF(ISNULL(maxCD4.max_data_cd4), 'N/A',  DATE_FORMAT(maxCD4.max_data_cd4, '%d/%m/%Y')) AS ultimoDataCd4,
      IF(ISNULL(valor_penultimo_cd4.value_numeric), 'N/A',valor_penultimo_cd4.value_numeric) AS penultimoResultadoCd4,
      IF(ISNULL(penultimoCd4.dataCd4Anterior), 'N/A',DATE_FORMAT(penultimoCd4.dataCd4Anterior, '%d/%m/%Y') ) AS penultimoCd4Data,
      IF(ISNULL( valor_ultimo_cd4Lab.value_numeric), 'N/A', valor_ultimo_cd4Lab.value_numeric) AS lastCd4ResultLab, 
      IF(ISNULL(maxCD4Lab.max_data_cd4), 'N/A',  DATE_FORMAT(maxCD4Lab.max_data_cd4, '%d/%m/%Y')) AS ultimoDataCd4Lab,
      IF(ISNULL(valor_penultimo_cd4Lab.value_numeric), 'N/A',valor_penultimo_cd4Lab.value_numeric) AS penultimoResultadoCd4Lab,
      IF(ISNULL(penultimoCd4Lab.dataCd4Anterior), 'N/A',DATE_FORMAT(penultimoCd4Lab.dataCd4Anterior, '%d/%m/%Y') ) AS penultimoCd4DataLab,
      IF(ISNULL(ultimaCV.resultadoCV), 'N/A',ultimaCV.resultadoCV) AS resultadoCV,
      IF(ISNULL(ultimaCV.data_carga), 'N/A', DATE_FORMAT(ultimaCV.data_carga, '%d/%m/%Y')) AS ultimaDataCv,
      IF(ISNULL(penultimaCV.penultimaDataCarga), 'N/A',DATE_FORMAT(penultimaCV.penultimaDataCarga, '%d/%m/%Y') ) AS penultimaDataCv,
      IF(ISNULL(penultimaCV.resultadoCV), 'N/A',penultimaCV.resultadoCV) AS penultimoResultadoCV,
      IF(ISNULL(ultimaCVLab.resultadoCV), 'N/A',ultimaCVLab.resultadoCV) AS resultadoCVLab,
      IF(ISNULL(ultimaCVLab.data_carga), 'N/A', DATE_FORMAT(ultimaCVLab.data_carga, '%d/%m/%Y')) AS ultimaDataCvLab,
      IF(ISNULL(penultimaCVLab.penultimaDataCarga), 'N/A',DATE_FORMAT(penultimaCVLab.penultimaDataCarga, '%d/%m/%Y') ) AS penultimaDataCvLab,
      IF(ISNULL(penultimaCVLab.resultadoCV), 'N/A',penultimaCVLab.resultadoCV) AS penultimoResultadoCVLab,
      IF(ISNULL(ultimoTbLam.resultadoTbLam), 'N/A',ultimoTbLam.resultadoTbLam ) AS resultadoTbLam,
      IF(ISNULL(ultimoTbLam.max_tblam), 'N/A', DATE_FORMAT(ultimoTbLam.max_tblam, '%d/%m/%Y')) AS ultimoTbLam,
      IF(ISNULL(cragSoro.resultadoCragSoro), 'N/A',cragSoro.resultadoCragSoro) AS resultadoCragSoro,
      IF(ISNULL(cragSoro.max_cragSoro), 'N/A',DATE_FORMAT(cragSoro.max_cragSoro, '%d/%m/%Y')) AS ultimoCragSoroData,
      IF(ISNULL(cragLcr.resultadoCragLcr), 'N/A',cragLcr.resultadoCragLcr) AS resultadoCragLcr,
      IF(ISNULL(cragLcr.max_cragLcr), 'N/A',DATE_FORMAT(cragLcr.max_cragLcr, '%d/%m/%Y')) AS ultimoCragLcrData,
      IF(ISNULL( ultimoCacu.resultadoCacu), 'N/A',ultimoCacu.resultadoCacu) AS resultadoCacu,
      IF(ISNULL(ultimoCacu.max_cacu), 'N/A',DATE_FORMAT(ultimoCacu.max_cacu, '%d/%m/%Y')) AS ultimoCacuData
from ( 
	select inicioDAH.patient_id 
	from( 
		select p.patient_id 
		from patient p 
	    		inner join encounter e on p.patient_id=e.patient_id 
	    	where e.voided=0 and p.voided=0 and  e.encounter_type =90 
	    		and e.encounter_datetime between :startDate and :endDate and e.location_id=:location 
	    
	    union 
	    
	    select distinct max_cd4.patient_id  
	    from( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cd4  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
	    			and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
	    				group by p.patient_id 
	    	)
	    max_cd4 
	    		inner join person per on per.person_id=max_cd4.patient_id 
	    		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
	    		and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 1695 and o.value_numeric<200 and o.location_id=:location 
	    
	    union 

	    	    
	    select distinct max_cd4.patient_id  
	    from( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cd4  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,51,13,53,90) 
	    			and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
	    				group by p.patient_id 
	    	)
	    max_cd4 
	    		inner join person per on per.person_id=max_cd4.patient_id 
	    		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
	    		and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165515 and o.value_coded=165513 and o.location_id=:location 

	    		union
	    
	    select distinct max_cd4.patient_id  
	    from( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cd4  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
	    			and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
	    			group by p.patient_id 
	    	)
	    max_cd4 
	    		inner join person per on per.person_id=max_cd4.patient_id 
	    		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
	    		and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)>=12 and timestampdiff(year,per.birthdate,:endDate)< 5 and o.concept_id = 1695 and o.value_numeric<500 and o.location_id=:location 
	    
	    union 
	    
	    select distinct max_cd4.patient_id  
	    from ( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cd4  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
	    			and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
	    			group by p.patient_id 
	    		)
	    	max_cd4 
	    		inner join person per on per.person_id=max_cd4.patient_id 
	    		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
	    		and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)<12 and o.concept_id = 1695 and o.value_numeric<750 and o.location_id=:location 
	    
	    union 
	    
	    select patient_id 
	    from ( 
	    		select p.patient_id,min(e.encounter_datetime) encounter_datetime 
	    		from patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on o.encounter_id=e.encounter_id 
	    		where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 
	    			and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334, 14656, 7180, 6990, 5344, 5340, 1294, 5042, 507, 1570)and e.location_id=:location 
	    			and o.obs_datetime between :startDate and :endDate 
	    			group by p.patient_id 
	    		) estadio 
	    ) inicioDAH 
	    left join
	    (
	   	select p.patient_id 
		from patient p 
	    		inner join encounter e on p.patient_id=e.patient_id 
	    	where e.voided=0 and p.voided=0 and  e.encounter_type =90 
	    		 and e.encounter_datetime >= date_add(:startDate, interval - 12 month) and e.encounter_datetime < :startDate and e.location_id=:location 
	    	
	    	union
	 	select transferido_para.patient_id  
	    	from ( 
		    	 select transferido_para.patient_id,data_transferencia  
		    	 from ( 
		    			select patient_id,max(data_transferencia) data_transferencia 
		    			from ( 
		    					select maxEstado.patient_id,maxEstado.data_transferencia 
		    					from ( 
		    							select pg.patient_id,max(ps.start_date) data_transferencia 
		    							from patient p 
									    inner join patient_program pg on p.patient_id=pg.patient_id 
									    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
		    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
		    								group by p.patient_id 
		    					) maxEstado 
		    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
		    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
		    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
		    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
		
		    					union
		    					 
							select p.patient_id,max(e.encounter_datetime) data_transferencia 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							   	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
								and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
							  	group by p.patient_id 
		   					
		   					union 
							
							select p.patient_id,max(o.obs_datetime) data_transferencia 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							    	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
								group by p.patient_id 
							    	
		    		) 
		    	   transferido group by patient_id 
		      ) 
		     transferido_para 
		     left join
			(
				select ultimo_fila.patient_id, ultimo_fila.max_date
				from(
					select p.patient_id,max(encounter_datetime) max_date                                                                                                
					from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id                                                                                         
					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
						and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
						group by p.patient_id 
				) ultimo_fila
				inner join(
				select patient_id , data_ultimo_levantamento    
				from(  	
		       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		               from(
		         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
							from patient p                                                                                                                                   
								inner join encounter e on e.patient_id= p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id                                                                                        
							where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
								and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
								group by p.patient_id 
		         
		         				union
		         
		              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
		              			from patient p                                                                                                                                   
		               			inner join person pe on pe.person_id = p.patient_id                                                                                         
		                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
		                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
		              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
		                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
		              				group by p.patient_id
		               ) ultimo_levantamento group by patient_id
		      	) ultimo_levantamento
		     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
		     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
			
			) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
					where transferido_para.data_transferencia >= ultimo_fila.max_date
	   )
	   transferido_para
	   inner join
		(
			select patient_id , data_ultimo_levantamento    
			from(  	
		  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		          from(
		
					select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
					from patient p                                                                                                                                   
						inner join encounter e on e.patient_id= p.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id                                                                                        
					where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
						and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
						group by p.patient_id 
		
					union
		
					select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
		       		from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
			     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
			     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
		       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
		               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
						group by p.patient_id
		          ) ultimo_levantamento group by patient_id
		 	) ultimo_levantamento
			where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
		)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
		where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
	   ) saidas on inicioDAH.patient_id = saidas.patient_id 
	   where saidas.patient_id is null
 )inicioDAH
 	inner join person p on p.person_id=inicioDAH.patient_id 
     left join 
	(   
		select pad1.* 
          from person_address pad1 
           	inner join 
               ( 
				select person_id,min(person_address_id) id 
                    from person_address 
                    where voided=0 group by person_id 
                ) pad2 
            where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id 
	) pad3 on pad3.person_id=inicioDAH.patient_id 
	left join 
    	(
		select pn1.* 
          from person_name pn1 
                inner join 
                ( 
                    select person_id,min(person_name_id) id 
                    from person_name 
                    where voided=0 
                    	group by person_id 
                ) pn2 
                where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
       ) pn on pn.person_id=inicioDAH.patient_id 
       left join 
       (   
       	select pid1.* 
          from patient_identifier pid1 
           	inner join 
               ( 
				select patient_id,min(patient_identifier_id) id 
                    from patient_identifier 
                    where voided=0 and identifier_type = 2 
                    	group by patient_id 
                ) pid2 
                where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
         ) pid on pid.patient_id=inicioDAH.patient_id 
         left join person_attribute pat on pat.person_id=inicioDAH.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value <> '' and pat.voided=0 
         left join 
         ( 
        		select patient_id, min(data_inicio) data_inicio 
        		from ( 
         				select e.patient_id, min(e.encounter_datetime) as data_inicio  
         				from patient p 
        					inner join encounter e on p.patient_id=e.patient_id 
        				where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location group by p.patient_id 
        				
        				union 
         				
         				select p.patient_id,min(o.value_datetime) data_inicio 
         				from patient p 
        					inner join encounter e on p.patient_id=e.patient_id 
        					inner join obs o on e.encounter_id=o.encounter_id 
        					inner join obs obsLevantoArv on  obsLevantoArv.encounter_id = e.encounter_id
        				where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null 
        					and  o.value_datetime<=:endDate and e.location_id=:location and obsLevantoArv.concept_id = 23865 and obsLevantoArv.value_coded = 1065 
        					group by p.patient_id 

        				union

        				select p.patient_id, min(value_datetime) data_inicio 
        				from patient p 
        					inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
						where p.voided=0 and e.voided=0 and o.voided=0 and e.location_id =5 and e.encounter_type in (6,9,53) 
					and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate 
					group by p.patient_id 

					union

					select p.patient_id, min(e.encounter_datetime) data_inicio 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on o.encounter_id=e.encounter_id 
					where e.voided=0 and o.voided=0 and p.voided=0 and e.location_id =5 and e.encounter_type in (6,9) 
					and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate 
					group by p.patient_id 

					union

					select pg.patient_id, min(date_enrolled) data_inicio 
					from patient p 
						inner join patient_program pg on p.patient_id=pg.patient_id 
					where pg.voided=0 and p.voided=0 and program_id=2 and pg.location_id=:location and date_enrolled<=:endDate 
					group by pg.patient_id 

					
        		) inicioTARV group by patient_id 
        	) inicioTarv ON inicioTarv.patient_id = inicioDAH.patient_id -- Column G 
		left join 
          ( 
         		select e.patient_id, max(e.encounter_datetime) as data_levantamento 
         		from patient p 
        			inner join encounter e on p.patient_id=e.patient_id 
        		where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
        			group by p.patient_id 
        ) ultimoLevantamentoTarv ON ultimoLevantamentoTarv.patient_id = inicioDAH.patient_id -- Column H 
        left join 
        ( 
			select distinct min_estado.patient_id, min_estado.data_estado dataTransferido 
			from ( 
				select pg.patient_id, 
                    	min(ps.start_date) data_estado, pg.program_id, ps.state
             		from patient p 
                 		inner join person pe on pe.person_id = p.patient_id 
                 		inner join patient_program pg on p.patient_id = pg.patient_id 
                 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
             		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id in (1,2) 
                 		and ps.start_date<= :endDate and pg.location_id =5 
                 		group by pg.patient_id 
         		) min_estado 
             		inner join patient_program pp on pp.patient_id = min_estado.patient_id 
             		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = min_estado.data_estado 
               where ps.state=29 and pp.program_id in (1,2) and pp.voided = 0 and ps.voided = 0 and pp.location_id =5 
        		
        		union 
        		
        		select p.patient_id, min(obsData.value_datetime) as initialDate 
        		from patient p 
        			inner join encounter e on e.patient_id=p.patient_id 
        			inner join obs o on o.encounter_id=e.encounter_id 
        			inner join obs obsData on e.encounter_id=obsData.encounter_id 
        		where e.voided=0 and o.voided=0 and e.encounter_type=53 and obsData.concept_id=23891 
        			and obsData.voided=0 and e.location_id=:location and o.concept_id=1369 and o.value_coded=1065 and obsData.value_datetime<= :endDate 
        			group by p.patient_id 
        ) transferredIn on transferredIn.patient_id = inicioDAH.patient_id -- Column I
        left join 
        ( 
        	select estadoPermanencia.patient_id, max(estadoPermanencia.data_proximo_levantamento) data_estado_permanencia, RespostaEstadoPermanencia 
        	from ( 
        			select *
        			from(
	        			select abandono.patient_id, abandono.data_proximo_levantamento, RespostaEstadoPermanencia 
	        			from (
		        			select abandono.patient_id, abandono.data_proximo_levantamento, 1 as RespostaEstadoPermanencia 
		        			from ( 
		   						select patient_id, max(data_proximo_levantamento) data_proximo_levantamento
		   						from ( 
										select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
										from patient p 
								     		inner join encounter e on p.patient_id = e.patient_id 
								        		inner join obs o on o.encounter_id = e.encounter_id 
								        		inner join obs obsLevantou on obsLevantou.encounter_id=e.encounter_id 
								    		where e.voided = 0 and p.voided = 0 and o.value_datetime <=:endDate and o.voided = 0 
								     		and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 
								        		and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
								        		group by p.patient_id 
								  		union 
								     
								    		select p.patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id= p.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id                                                                                        
										where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
											and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
											group by p.patient_id 
								)proximo_levantamento group by proximo_levantamento.patient_id
		   	   			)abandono where date_add(abandono.data_proximo_levantamento, INTERVAL 60 DAY) < :endDate 
	        		) abandono 
			     left join
			     ( 
						select transferido_para.patient_id 
						from(
							select transferido_para.patient_id --,data_transferencia  
					    		from ( 
						    			select patient_id,max(data_transferencia) data_transferencia 
						    			from ( 
						    					select maxEstado.patient_id,maxEstado.data_transferencia 
						    					from ( 
						    							select pg.patient_id,max(ps.start_date) data_transferencia 
						    							from patient p 
													    inner join patient_program pg on p.patient_id=pg.patient_id 
													    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						    								group by p.patient_id 
						    					) maxEstado 
						    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
						    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
						    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
						
						    					union
						    					 
											select p.patient_id,max(e.encounter_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											   	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
												and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
											  	group by p.patient_id 
						   					
						   					union 
											
											select p.patient_id,max(o.obs_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											    	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
												group by p.patient_id 
											    	
						    		) 
						    	   transferido group by patient_id 
						      ) 
						     transferido_para 
						     left join
							(
								select ultimo_fila.patient_id, ultimo_fila.max_date
								from(
									select p.patient_id,max(encounter_datetime) max_date                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
										group by p.patient_id 
								) ultimo_fila
								inner join(
								select patient_id , data_ultimo_levantamento    
								from(  	
						       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						               from(
						         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
						         
						         				union
						         
						              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						              			from patient p                                                                                                                                   
						               			inner join person pe on pe.person_id = p.patient_id                                                                                         
						                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
						                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
						              				group by p.patient_id
						               ) ultimo_levantamento group by patient_id
						      	) ultimo_levantamento
						     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
						     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
							
							) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
									where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
						) transferido_para
						inner join
						(
							select patient_id , data_ultimo_levantamento    
							from(  	
						  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						          from(
						
									select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id                                                                                        
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
										and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
										group by p.patient_id 
						
									union
						
									select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						       		from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
							     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
										group by p.patient_id
						          ) ultimo_levantamento group by patient_id
						 	) ultimo_levantamento
							where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
						)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
						where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
	
						union
						
						select patient_id 
						from(
							select suspenso1.patient_id, data_suspencao  
							from( 
								select patient_id,max(data_suspencao) data_suspencao 
								from( 
									select maxEstado.patient_id,maxEstado.data_suspencao 
									from( 
										select pg.patient_id,max(ps.start_date) data_suspencao 
										from patient p 
									    		inner join patient_program pg on p.patient_id=pg.patient_id 
									        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
									    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
									     	group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
										inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
									where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
										and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
									
									union 
							        
							        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
							        	from patient p 
							        		inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
							        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
							        		group by p.patient_id 
									
									union 
							        
							        	select p.patient_id,max(o.obs_datetime) data_suspencao 
							        	from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
							        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
							        		group by p.patient_id 
							        
							      ) suspenso group by patient_id) suspenso1 
								inner join 
							   	( 
							   		select patient_id,max(encounter_datetime) encounter_datetime 
							   		from( 
							   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
							   			from patient p 
							   				inner join encounter e on e.patient_id=p.patient_id 
							   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
							   				group by p.patient_id 
							   		) consultaLev group by patient_id
							   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
							   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
									
						) suspensos
	
						union
						
						select patient_id
						from(
							select obito.patient_id 
							from ( 
								select patient_id, max(data_obito) data_obito 
								from ( 
							 		select maxEstado.patient_id,maxEstado.data_obito 
							 		from ( 
							 			select pg.patient_id, max(ps.start_date) data_obito 
							 			from patient p 
									 		inner join patient_program pg on p.patient_id = pg.patient_id 
									 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
									 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
									  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
									 		group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
									 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
									where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
							        
							        	union 
							 		
							 		select p.patient_id, max(o.obs_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
							 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union
							 		 
							 		select p.patient_id, max(e.encounter_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
							 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union 
								 	
								 	select person_id, death_date 
								 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
								) obito group by patient_id
							) obito 
							inner join 
							( 
								select patient_id, max(encounter_datetime) encounter_datetime 
								from( 
							 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
							 		from patient p 
							 			inner join encounter e on e.patient_id = p.patient_id 
							 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
							 			and e.location_id =5 
							 			group by p.patient_id 
								) consultaLev group by patient_id 
							) 
							consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
							 where consultaOuARV.encounter_datetime <= obito.data_obito 
						) obitos
			    ) exclusoes on abandono.patient_id = exclusoes.patient_id where exclusoes.patient_id is null
		   )abandonos

		   union

		   select *
		   from(
				select obito.patient_id, obito.data_obito, 2 as RespostaEstadoPermanencia 
				from ( 
					select patient_id, max(data_obito) data_obito 
					from ( 
				 		select maxEstado.patient_id,maxEstado.data_obito 
				 		from ( 
				 			select pg.patient_id, max(ps.start_date) data_obito 
				 			from patient p 
						 		inner join patient_program pg on p.patient_id = pg.patient_id 
						 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
						 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
						  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
						 		group by p.patient_id 
						) maxEstado 
							inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
						 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
						where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
				        
				        	union 
				 		
				 		select p.patient_id, max(o.obs_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
				 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
				 			group by p.patient_id 
				 		
				 		union
				 		 
				 		select p.patient_id, max(e.encounter_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
				 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
				 			group by p.patient_id 
				 		
				 		union 
					 	
					 	select person_id, death_date 
					 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
					) obito group by patient_id
				) obito 
				inner join 
				( 
					select patient_id, max(encounter_datetime) encounter_datetime 
					from( 
				 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
				 		from patient p 
				 			inner join encounter e on e.patient_id = p.patient_id 
				 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
				 			and e.location_id =5 
				 			group by p.patient_id 
					) consultaLev group by patient_id 
				) 
				consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
				 where consultaOuARV.encounter_datetime <= obito.data_obito 
		   )obitos

		   union

		   select *
		   from(
		   		select suspenso1.patient_id, data_suspencao, 3 as RespostaEstadoPermanencia  
				from( 
					select patient_id,max(data_suspencao) data_suspencao 
					from( 
						select maxEstado.patient_id,maxEstado.data_suspencao 
						from( 
							select pg.patient_id,max(ps.start_date) data_suspencao 
							from patient p 
						    		inner join patient_program pg on p.patient_id=pg.patient_id 
						        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						     	group by p.patient_id 
						) maxEstado 
							inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
							inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
							and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
						
						union 
				        
				        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
				        	from patient p 
				        		inner join encounter e on p.patient_id=e.patient_id 
				        		inner join obs o on o.encounter_id=e.encounter_id 
				        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
				        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
				        		group by p.patient_id 
						
						union 
				        
				        	select p.patient_id,max(o.obs_datetime) data_suspencao 
				        	from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
				        		inner join obs o on o.encounter_id=e.encounter_id 
				        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
				        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
				        		group by p.patient_id 
				        
				      ) suspenso group by patient_id) suspenso1 
					inner join 
				   	( 
				   		select patient_id,max(encounter_datetime) encounter_datetime 
				   		from( 
				   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
				   			from patient p 
				   				inner join encounter e on e.patient_id=p.patient_id 
				   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
				   				group by p.patient_id 
				   		) consultaLev group by patient_id
				   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
				   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
		   ) suspensos

		   union
			
		   select * 
		   from(
		   		select activo.patient_id,activo.data_inicio, 4 as RespostaEstadoPermanencia 
				from( 
					select inicioTARV.patient_id, inicioTARV.data_inicio
					from (
						select patient_id, min(data_inicio) data_inicio 
				        	from ( 
							select e.patient_id, min(e.encounter_datetime) as data_inicio  
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location group by p.patient_id 
							
							union 
							
							select p.patient_id,min(o.value_datetime) data_inicio 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
								inner join obs obsLevantoArv on  obsLevantoArv.encounter_id = e.encounter_id
							where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null 
								and  o.value_datetime<=:endDate and e.location_id=:location and obsLevantoArv.concept_id = 23865 and obsLevantoArv.value_coded = 1065 
								group by p.patient_id 
				
				        ) inicioTARV group by patient_id 
				  	)inicioTARV
				  	left join
				  	(
				  		select abandonos.patient_id
						from(
							select abandono.patient_id, abandono.data_proximo_levantamento
							from (
								select abandono.patient_id, abandono.data_proximo_levantamento
								from ( 
										select patient_id, max(data_proximo_levantamento) data_proximo_levantamento
										from ( 
											select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
											from patient p 
									     		inner join encounter e on p.patient_id = e.patient_id 
									        		inner join obs o on o.encounter_id = e.encounter_id 
									        		inner join obs obsLevantou on obsLevantou.encounter_id=e.encounter_id 
									    		where e.voided = 0 and p.voided = 0 and o.value_datetime <=:endDate and o.voided = 0 
									     		and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 
									        		and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
									        		group by p.patient_id 
									  		union 
									     
									    		select p.patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
									)proximo_levantamento group by proximo_levantamento.patient_id
									)abandono where date_add(abandono.data_proximo_levantamento, INTERVAL 60 DAY) < :endDate 
						) abandono 
						left join
						( 
							select transferido_para.patient_id 
							from(
								select transferido_para.patient_id --,data_transferencia  
						    		from ( 
							    			select patient_id,max(data_transferencia) data_transferencia 
							    			from ( 
							    					select maxEstado.patient_id,maxEstado.data_transferencia 
							    					from ( 
							    							select pg.patient_id,max(ps.start_date) data_transferencia 
							    							from patient p 
														    inner join patient_program pg on p.patient_id=pg.patient_id 
														    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
							    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
							    								group by p.patient_id 
							    					) maxEstado 
							    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
							    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
							    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
							    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
							
							    					union
							    					 
												select p.patient_id,max(e.encounter_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												   	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
													and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
												  	group by p.patient_id 
							   					
							   					union 
												
												select p.patient_id,max(o.obs_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												    	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
													group by p.patient_id 
												    	
							    		) 
							    	   transferido group by patient_id 
							      ) 
							     transferido_para 
							     left join
								(
									select ultimo_fila.patient_id, ultimo_fila.max_date
									from(
										select p.patient_id,max(encounter_datetime) max_date                                                                                                
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
											and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
											group by p.patient_id 
									) ultimo_fila
									inner join(
									select patient_id , data_ultimo_levantamento    
									from(  	
							       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							               from(
							         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
												from patient p                                                                                                                                   
													inner join encounter e on e.patient_id= p.patient_id 
													inner join obs o on o.encounter_id = e.encounter_id                                                                                        
												where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
													and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
													group by p.patient_id 
							         
							         				union
							         
							              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
							              			from patient p                                                                                                                                   
							               			inner join person pe on pe.person_id = p.patient_id                                                                                         
							                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
							              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
							                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
							              				group by p.patient_id
							               ) ultimo_levantamento group by patient_id
							      	) ultimo_levantamento
							     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
							     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
								
								) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
										where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
							) transferido_para
							inner join
							(
								select patient_id , data_ultimo_levantamento    
								from(  	
							  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							          from(
							
										select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id= p.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id                                                                                        
										where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
											and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
											group by p.patient_id 
							
										union
							
										select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
							       		from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
								     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
								     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
							       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
							               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
											group by p.patient_id
							          ) ultimo_levantamento group by patient_id
							 	) ultimo_levantamento
								where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
							)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
							where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
							
							union
							
							select patient_id 
							from(
								select suspenso1.patient_id, data_suspencao  
								from( 
									select patient_id,max(data_suspencao) data_suspencao 
									from( 
										select maxEstado.patient_id,maxEstado.data_suspencao 
										from( 
											select pg.patient_id,max(ps.start_date) data_suspencao 
											from patient p 
										    		inner join patient_program pg on p.patient_id=pg.patient_id 
										        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
										    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
										     	group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
											inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
										where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
											and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
										
										union 
								        
								        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
								        	from patient p 
								        		inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
								        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
								        		group by p.patient_id 
										
										union 
								        
								        	select p.patient_id,max(o.obs_datetime) data_suspencao 
								        	from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
								        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
								        		group by p.patient_id 
								        
								      ) suspenso group by patient_id) suspenso1 
									inner join 
								   	( 
								   		select patient_id,max(encounter_datetime) encounter_datetime 
								   		from( 
								   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
								   			from patient p 
								   				inner join encounter e on e.patient_id=p.patient_id 
								   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
								   				group by p.patient_id 
								   		) consultaLev group by patient_id
								   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
								   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
										
							) suspensos
						
							union
							
							select patient_id
							from(
								select obito.patient_id 
								from ( 
									select patient_id, max(data_obito) data_obito 
									from ( 
								 		select maxEstado.patient_id,maxEstado.data_obito 
								 		from ( 
								 			select pg.patient_id, max(ps.start_date) data_obito 
								 			from patient p 
										 		inner join patient_program pg on p.patient_id = pg.patient_id 
										 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
										 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
										  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
										 		group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
										 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
										where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
								        
								        	union 
								 		
								 		select p.patient_id, max(o.obs_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
								 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union
								 		 
								 		select p.patient_id, max(e.encounter_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
								 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union 
									 	
									 	select person_id, death_date 
									 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
									) obito group by patient_id
								) obito 
								inner join 
								( 
									select patient_id, max(encounter_datetime) encounter_datetime 
									from( 
								 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
								 		from patient p 
								 			inner join encounter e on e.patient_id = p.patient_id 
								 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
								 			and e.location_id =5 
								 			group by p.patient_id 
									) consultaLev group by patient_id 
								) 
								consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
								 where consultaOuARV.encounter_datetime <= obito.data_obito 
							) obitos
						) exclusoes on abandono.patient_id = exclusoes.patient_id where exclusoes.patient_id is null
						)abandonos
				
						union
						
						select transferido_para.patient_id
						from(
							select transferido_para.patient_id --,data_transferencia  
				    	 		from ( 
						    			select patient_id,max(data_transferencia) data_transferencia 
						    			from ( 
						    					select maxEstado.patient_id,maxEstado.data_transferencia 
						    					from ( 
						    							select pg.patient_id,max(ps.start_date) data_transferencia 
						    							from patient p 
													    inner join patient_program pg on p.patient_id=pg.patient_id 
													    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						    								group by p.patient_id 
						    					) maxEstado 
						    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
						    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
						    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
						
						    					union
						    					 
											select p.patient_id,max(e.encounter_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											   	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
												and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
											  	group by p.patient_id 
						   					
						   					union 
											
											select p.patient_id,max(o.obs_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											    	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
												group by p.patient_id 
											    	
						    		) 
						    	   transferido group by patient_id 
						      ) 
						     transferido_para 
						     left join
							(
								select ultimo_fila.patient_id, ultimo_fila.max_date
								from(
									select p.patient_id,max(encounter_datetime) max_date                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
										group by p.patient_id 
								) ultimo_fila
								inner join(
								select patient_id , data_ultimo_levantamento    
								from(  	
						       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						               from(
						         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
						         
						         				union
						         
						              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						              			from patient p                                                                                                                                   
						               			inner join person pe on pe.person_id = p.patient_id                                                                                         
						                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
						                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
						              				group by p.patient_id
						               ) ultimo_levantamento group by patient_id
						      	) ultimo_levantamento
						     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
						     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
							
							) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
									where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
						) transferido_para
						inner join
						(
							select patient_id , data_ultimo_levantamento    
							from(  	
						  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						          from(
						
									select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id                                                                                        
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
										and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
										group by p.patient_id 
						
									union
						
									select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						       		from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
							     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
										group by p.patient_id
						          ) ultimo_levantamento group by patient_id
						 	) ultimo_levantamento
							where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
						)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
						where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
				
						union
				
						select suspensos.patient_id
						from(
							select suspenso1.patient_id, data_suspencao  
							from( 
								select patient_id,max(data_suspencao) data_suspencao 
								from( 
									select maxEstado.patient_id,maxEstado.data_suspencao 
									from( 
										select pg.patient_id,max(ps.start_date) data_suspencao 
										from patient p 
									    		inner join patient_program pg on p.patient_id=pg.patient_id 
									        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
									    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
									     	group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
										inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
									where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
										and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
									
									union 
							        
							        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
							        	from patient p 
							        		inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
							        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
							        		group by p.patient_id 
									
									union 
							        
							        	select p.patient_id,max(o.obs_datetime) data_suspencao 
							        	from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
							        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
							        		group by p.patient_id 
							        
							      ) suspenso group by patient_id) suspenso1 
								inner join 
							   	( 
							   		select patient_id,max(encounter_datetime) encounter_datetime 
							   		from( 
							   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
							   			from patient p 
							   				inner join encounter e on e.patient_id=p.patient_id 
							   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
							   				group by p.patient_id 
							   		) consultaLev group by patient_id
							   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
							   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
						) suspensos
				
						union
				
						select obitos.patient_id
						from(
							select obito.patient_id 
							from ( 
								select patient_id, max(data_obito) data_obito 
								from ( 
							 		select maxEstado.patient_id,maxEstado.data_obito 
							 		from ( 
							 			select pg.patient_id, max(ps.start_date) data_obito 
							 			from patient p 
									 		inner join patient_program pg on p.patient_id = pg.patient_id 
									 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
									 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
									  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
									 		group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
									 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
									where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
							        
							        	union 
							 		
							 		select p.patient_id, max(o.obs_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
							 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union
							 		 
							 		select p.patient_id, max(e.encounter_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
							 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union 
								 	
								 	select person_id, death_date 
								 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
								) obito group by patient_id
							) obito 
							inner join 
							( 
								select patient_id, max(encounter_datetime) encounter_datetime 
								from( 
							 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
							 		from patient p 
							 			inner join encounter e on e.patient_id = p.patient_id 
							 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
							 			and e.location_id =5 
							 			group by p.patient_id 
								) consultaLev group by patient_id 
							) 
							consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
							 where consultaOuARV.encounter_datetime <= obito.data_obito 
						) obitos

						union

								   select reinicios.patient_id
		   from(
		   		select reinicios.patient_id,reinicios.data_estado, 5 as RespostaEstadoPermanencia 
				from( 
					select reinicioTARV.patient_id, reinicioTARV.data_estado
					from (
						select patient_id, max(data_estado) data_estado 
				        	from ( 
				
							select p.patient_id,max(e.encounter_datetime) data_estado
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							   	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
								and o.voided=0 and o.concept_id=6273 and o.value_coded=1705 and e.encounter_type=6 and e.location_id=:location 
							  	group by p.patient_id 
				
							union
				
							select p.patient_id,max(o.obs_datetime) data_estado 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
						    		inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1705 and e.encounter_type=53 and e.location_id=:location 
								group by p.patient_id 
				        ) reinicioTARV group by patient_id 
				  	)reinicioTARV
				  	left join
				  	(
				  		select abandonos.patient_id
						from(
							select abandono.patient_id, abandono.data_proximo_levantamento
							from (
								select abandono.patient_id, abandono.data_proximo_levantamento
								from ( 
										select patient_id, max(data_proximo_levantamento) data_proximo_levantamento
										from ( 
											select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
											from patient p 
									     		inner join encounter e on p.patient_id = e.patient_id 
									        		inner join obs o on o.encounter_id = e.encounter_id 
									        		inner join obs obsLevantou on obsLevantou.encounter_id=e.encounter_id 
									    		where e.voided = 0 and p.voided = 0 and o.value_datetime <=:endDate and o.voided = 0 
									     		and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 
									        		and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
									        		group by p.patient_id 
									  		union 
									     
									    		select p.patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
									)proximo_levantamento group by proximo_levantamento.patient_id
								)abandono where date_add(abandono.data_proximo_levantamento, INTERVAL 60 DAY) < :endDate 
						) abandono 
						left join
						( 
							select patient_id 
							from(
								select transferido_para.patient_id --,data_transferencia  
						    		from ( 
							    			select patient_id,max(data_transferencia) data_transferencia 
							    			from ( 
							    					select maxEstado.patient_id,maxEstado.data_transferencia 
							    					from ( 
							    							select pg.patient_id,max(ps.start_date) data_transferencia 
							    							from patient p 
														    inner join patient_program pg on p.patient_id=pg.patient_id 
														    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
							    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
							    								group by p.patient_id 
							    					) maxEstado 
							    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
							    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
							    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
							    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
							
							    					union
							    					 
												select p.patient_id,max(e.encounter_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												   	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
													and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
												  	group by p.patient_id 
							   					
							   					union 
												
												select p.patient_id,max(o.obs_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												    	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
													group by p.patient_id 
												    	
							    		) 
							    	   transferido group by patient_id 
							      ) 
							     transferido_para 
							     left join
								(
									select ultimo_fila.patient_id, ultimo_fila.max_date
									from(
										select p.patient_id,max(encounter_datetime) max_date                                                                                                
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
											and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
											group by p.patient_id 
									) ultimo_fila
									inner join(
									select patient_id , data_ultimo_levantamento    
									from(  	
							       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							               from(
							         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
												from patient p                                                                                                                                   
													inner join encounter e on e.patient_id= p.patient_id 
													inner join obs o on o.encounter_id = e.encounter_id                                                                                        
												where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
													and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
													group by p.patient_id 
							         
							         				union
							         
							              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
							              			from patient p                                                                                                                                   
							               			inner join person pe on pe.person_id = p.patient_id                                                                                         
							                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
							              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
							                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
							              				group by p.patient_id
							               ) ultimo_levantamento group by patient_id
							      	) ultimo_levantamento
							     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
							     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
								
								) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
										where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
							) transferido_para
						
							union
							
							select patient_id 
							from(
								select suspenso1.patient_id, data_suspencao  
								from( 
									select patient_id,max(data_suspencao) data_suspencao 
									from( 
										select maxEstado.patient_id,maxEstado.data_suspencao 
										from( 
											select pg.patient_id,max(ps.start_date) data_suspencao 
											from patient p 
										    		inner join patient_program pg on p.patient_id=pg.patient_id 
										        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
										    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
										     	group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
											inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
										where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
											and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
										
										union 
								        
								        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
								        	from patient p 
								        		inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
								        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
								        		group by p.patient_id 
										
										union 
								        
								        	select p.patient_id,max(o.obs_datetime) data_suspencao 
								        	from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
								        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
								        		group by p.patient_id 
								        
								      ) suspenso group by patient_id) suspenso1 
									inner join 
								   	( 
								   		select patient_id,max(encounter_datetime) encounter_datetime 
								   		from( 
								   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
								   			from patient p 
								   				inner join encounter e on e.patient_id=p.patient_id 
								   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
								   				group by p.patient_id 
								   		) consultaLev group by patient_id
								   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
								   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
										
							) suspensos
						
							union
							
							select patient_id
							from(
								select obito.patient_id 
								from ( 
									select patient_id, max(data_obito) data_obito 
									from ( 
								 		select maxEstado.patient_id,maxEstado.data_obito 
								 		from ( 
								 			select pg.patient_id, max(ps.start_date) data_obito 
								 			from patient p 
										 		inner join patient_program pg on p.patient_id = pg.patient_id 
										 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
										 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
										  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
										 		group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
										 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
										where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
								        
								        	union 
								 		
								 		select p.patient_id, max(o.obs_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
								 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union
								 		 
								 		select p.patient_id, max(e.encounter_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
								 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union 
									 	
									 	select person_id, death_date 
									 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
									) obito group by patient_id
								) obito 
								inner join 
								( 
									select patient_id, max(encounter_datetime) encounter_datetime 
									from( 
								 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
								 		from patient p 
								 			inner join encounter e on e.patient_id = p.patient_id 
								 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
								 			and e.location_id =5 
								 			group by p.patient_id 
									) consultaLev group by patient_id 
								) 
								consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
								 where consultaOuARV.encounter_datetime <= obito.data_obito 
							) obitos
						) exclusoes on abandono.patient_id = exclusoes.patient_id where exclusoes.patient_id is null
						)abandonos
				
						union
						
						select transferido_para.patient_id
						from(
							select transferido_para.patient_id --,data_transferencia  
				    	 		from ( 
						    			select patient_id,max(data_transferencia) data_transferencia 
						    			from ( 
						    					select maxEstado.patient_id,maxEstado.data_transferencia 
						    					from ( 
						    							select pg.patient_id,max(ps.start_date) data_transferencia 
						    							from patient p 
													    inner join patient_program pg on p.patient_id=pg.patient_id 
													    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						    								group by p.patient_id 
						    					) maxEstado 
						    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
						    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
						    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
						
						    					union
						    					 
											select p.patient_id,max(e.encounter_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											   	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
												and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
											  	group by p.patient_id 
						   					
						   					union 
											
											select p.patient_id,max(o.obs_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											    	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
												group by p.patient_id 
											    	
						    		) 
						    	   transferido group by patient_id 
						      ) 
						     transferido_para 
						     left join
							(
								select ultimo_fila.patient_id, ultimo_fila.max_date
								from(
									select p.patient_id,max(encounter_datetime) max_date                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
										group by p.patient_id 
								) ultimo_fila
								inner join(
								select patient_id , data_ultimo_levantamento    
								from(  	
						       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						               from(
						         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
						         
						         				union
						         
						              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						              			from patient p                                                                                                                                   
						               			inner join person pe on pe.person_id = p.patient_id                                                                                         
						                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
						                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
						              				group by p.patient_id
						               ) ultimo_levantamento group by patient_id
						      	) ultimo_levantamento
						     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
						     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
							
							) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
									where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
						) transferido_para
				
						union
				
						select obitos.patient_id
						from(
							select obito.patient_id 
							from ( 
								select patient_id, max(data_obito) data_obito 
								from ( 
							 		select maxEstado.patient_id,maxEstado.data_obito 
							 		from ( 
							 			select pg.patient_id, max(ps.start_date) data_obito 
							 			from patient p 
									 		inner join patient_program pg on p.patient_id = pg.patient_id 
									 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
									 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
									  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
									 		group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
									 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
									where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
							        
							        	union 
							 		
							 		select p.patient_id, max(o.obs_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
							 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union
							 		 
							 		select p.patient_id, max(e.encounter_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
							 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union 
								 	
								 	select person_id, death_date 
								 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
								) obito group by patient_id
							) obito 
							inner join 
							( 
								select patient_id, max(encounter_datetime) encounter_datetime 
								from( 
							 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
							 		from patient p 
							 			inner join encounter e on e.patient_id = p.patient_id 
							 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
							 			and e.location_id =5 
							 			group by p.patient_id 
								) consultaLev group by patient_id 
							) 
							consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
							 where consultaOuARV.encounter_datetime <= obito.data_obito 
						) obitos
				  		
				  	) saidas on reinicioTARV.patient_id = saidas.patient_id
				  	where saidas.patient_id is null
				  ) reinicios
		   ) reinicios
				  		
				  	) saidas on inicioTARV.patient_id = saidas.patient_id
				  	where saidas.patient_id is null
				  ) activo
		   ) activos
		   
		   union

		   select * 
		   from(
		   		select reinicios.patient_id,reinicios.data_estado, 5 as RespostaEstadoPermanencia 
				from( 
					select reinicioTARV.patient_id, reinicioTARV.data_estado
					from (
						select patient_id, max(data_estado) data_estado 
				        	from ( 
				
							select p.patient_id,max(e.encounter_datetime) data_estado
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							   	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
								and o.voided=0 and o.concept_id=6273 and o.value_coded=1705 and e.encounter_type=6 and e.location_id=:location 
							  	group by p.patient_id 
				
							union
				
							select p.patient_id,max(o.obs_datetime) data_estado 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
						    		inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1705 and e.encounter_type=53 and e.location_id=:location 
								group by p.patient_id 
				        ) reinicioTARV group by patient_id 
				  	)reinicioTARV
				  	left join
				  	(
				  		select abandonos.patient_id
						from(
							select abandono.patient_id, abandono.data_proximo_levantamento
							from (
								select abandono.patient_id, abandono.data_proximo_levantamento
								from ( 
										select patient_id, max(data_proximo_levantamento) data_proximo_levantamento
										from ( 
											select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
											from patient p 
									     		inner join encounter e on p.patient_id = e.patient_id 
									        		inner join obs o on o.encounter_id = e.encounter_id 
									        		inner join obs obsLevantou on obsLevantou.encounter_id=e.encounter_id 
									    		where e.voided = 0 and p.voided = 0 and o.value_datetime <=:endDate and o.voided = 0 
									     		and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 
									        		and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
									        		group by p.patient_id 
									  		union 
									     
									    		select p.patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
									)proximo_levantamento group by proximo_levantamento.patient_id
								)abandono where date_add(abandono.data_proximo_levantamento, INTERVAL 60 DAY) < :endDate 
						) abandono 
						left join
						( 
							select transferido_para.patient_id 
							from(
								select transferido_para.patient_id --,data_transferencia  
						    		from ( 
							    			select patient_id,max(data_transferencia) data_transferencia 
							    			from ( 
							    					select maxEstado.patient_id,maxEstado.data_transferencia 
							    					from ( 
							    							select pg.patient_id,max(ps.start_date) data_transferencia 
							    							from patient p 
														    inner join patient_program pg on p.patient_id=pg.patient_id 
														    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
							    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
							    								group by p.patient_id 
							    					) maxEstado 
							    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
							    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
							    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
							    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
							
							    					union
							    					 
												select p.patient_id,max(e.encounter_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												   	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
													and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
												  	group by p.patient_id 
							   					
							   					union 
												
												select p.patient_id,max(o.obs_datetime) data_transferencia 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
												    	inner join obs o on o.encounter_id=e.encounter_id 
												where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
													group by p.patient_id 
												    	
							    		) 
							    	   transferido group by patient_id 
							      ) 
							     transferido_para 
							     left join
								(
									select ultimo_fila.patient_id, ultimo_fila.max_date
									from(
										select p.patient_id,max(encounter_datetime) max_date                                                                                                
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
											and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
											group by p.patient_id 
									) ultimo_fila
									inner join(
									select patient_id , data_ultimo_levantamento    
									from(  	
							       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							               from(
							         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
												from patient p                                                                                                                                   
													inner join encounter e on e.patient_id= p.patient_id 
													inner join obs o on o.encounter_id = e.encounter_id                                                                                        
												where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
													and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
													group by p.patient_id 
							         
							         				union
							         
							              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
							              			from patient p                                                                                                                                   
							               			inner join person pe on pe.person_id = p.patient_id                                                                                         
							                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
							              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
							                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
							              				group by p.patient_id
							               ) ultimo_levantamento group by patient_id
							      	) ultimo_levantamento
							     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
							     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
								
								) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
										where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
							) transferido_para
							inner join
							(
								select patient_id , data_ultimo_levantamento    
								from(  	
							  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							          from(
							
										select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id= p.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id                                                                                        
										where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
											and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
											group by p.patient_id 
							
										union
							
										select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
							       		from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
								     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
								     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
							       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
							               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
											group by p.patient_id
							          ) ultimo_levantamento group by patient_id
							 	) ultimo_levantamento
								where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
							)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
							where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
						
							union
							
							select patient_id 
							from(
								select suspenso1.patient_id, data_suspencao  
								from( 
									select patient_id,max(data_suspencao) data_suspencao 
									from( 
										select maxEstado.patient_id,maxEstado.data_suspencao 
										from( 
											select pg.patient_id,max(ps.start_date) data_suspencao 
											from patient p 
										    		inner join patient_program pg on p.patient_id=pg.patient_id 
										        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
										    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
										     	group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
											inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
										where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
											and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
										
										union 
								        
								        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
								        	from patient p 
								        		inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
								        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
								        		group by p.patient_id 
										
										union 
								        
								        	select p.patient_id,max(o.obs_datetime) data_suspencao 
								        	from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
								        		inner join obs o on o.encounter_id=e.encounter_id 
								        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
								        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
								        		group by p.patient_id 
								        
								      ) suspenso group by patient_id) suspenso1 
									inner join 
								   	( 
								   		select patient_id,max(encounter_datetime) encounter_datetime 
								   		from( 
								   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
								   			from patient p 
								   				inner join encounter e on e.patient_id=p.patient_id 
								   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
								   				group by p.patient_id 
								   		) consultaLev group by patient_id
								   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
								   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
										
							) suspensos
						
							union
							
							select patient_id
							from(
								select obito.patient_id 
								from ( 
									select patient_id, max(data_obito) data_obito 
									from ( 
								 		select maxEstado.patient_id,maxEstado.data_obito 
								 		from ( 
								 			select pg.patient_id, max(ps.start_date) data_obito 
								 			from patient p 
										 		inner join patient_program pg on p.patient_id = pg.patient_id 
										 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
										 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
										  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
										 		group by p.patient_id 
										) maxEstado 
											inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
										 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
										where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
								        
								        	union 
								 		
								 		select p.patient_id, max(o.obs_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
								 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union
								 		 
								 		select p.patient_id, max(e.encounter_datetime) data_obito 
								 		from patient p 
								 			inner join encounter e on p.patient_id = e.patient_id 
								 			inner join obs o on o.encounter_id = e.encounter_id 
								 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
								 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
								 			group by p.patient_id 
								 		
								 		union 
									 	
									 	select person_id, death_date 
									 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
									) obito group by patient_id
								) obito 
								inner join 
								( 
									select patient_id, max(encounter_datetime) encounter_datetime 
									from( 
								 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
								 		from patient p 
								 			inner join encounter e on e.patient_id = p.patient_id 
								 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
								 			and e.location_id =5 
								 			group by p.patient_id 
									) consultaLev group by patient_id 
								) 
								consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
								 where consultaOuARV.encounter_datetime <= obito.data_obito 
							) obitos
						) exclusoes on abandono.patient_id = exclusoes.patient_id where exclusoes.patient_id is null
						)abandonos
				
						union
						
						select transferido_para.patient_id
						from(
							select transferido_para.patient_id --,data_transferencia  
				    	 		from ( 
						    			select patient_id,max(data_transferencia) data_transferencia 
						    			from ( 
						    					select maxEstado.patient_id,maxEstado.data_transferencia 
						    					from ( 
						    							select pg.patient_id,max(ps.start_date) data_transferencia 
						    							from patient p 
													    inner join patient_program pg on p.patient_id=pg.patient_id 
													    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						    								group by p.patient_id 
						    					) maxEstado 
						    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
						    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
						    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
						
						    					union
						    					 
											select p.patient_id,max(e.encounter_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											   	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate  
												and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
											  	group by p.patient_id 
						   					
						   					union 
											
											select p.patient_id,max(o.obs_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											    	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
												group by p.patient_id 
											    	
						    		) 
						    	   transferido group by patient_id 
						      ) 
						     transferido_para 
						     left join
							(
								select ultimo_fila.patient_id, ultimo_fila.max_date
								from(
									select p.patient_id,max(encounter_datetime) max_date                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
										group by p.patient_id 
								) ultimo_fila
								inner join(
								select patient_id , data_ultimo_levantamento    
								from(  	
						       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						               from(
						         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
												group by p.patient_id 
						         
						         				union
						         
						              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						              			from patient p                                                                                                                                   
						               			inner join person pe on pe.person_id = p.patient_id                                                                                         
						                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
						                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
						              				group by p.patient_id
						               ) ultimo_levantamento group by patient_id
						      	) ultimo_levantamento
						     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
						     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
							
							) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
									where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
						) transferido_para
						inner join
						(
							select patient_id , data_ultimo_levantamento    
							from(  	
						  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						          from(
						
									select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id                                                                                        
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
										and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                
										group by p.patient_id 
						
									union
						
									select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						       		from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
							     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                         
										group by p.patient_id
						          ) ultimo_levantamento group by patient_id
						 	) ultimo_levantamento
							where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
						)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
						where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
				
						union
				
						select obitos.patient_id
						from(
							select obito.patient_id 
							from ( 
								select patient_id, max(data_obito) data_obito 
								from ( 
							 		select maxEstado.patient_id,maxEstado.data_obito 
							 		from ( 
							 			select pg.patient_id, max(ps.start_date) data_obito 
							 			from patient p 
									 		inner join patient_program pg on p.patient_id = pg.patient_id 
									 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
									 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
									  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =5 
									 		group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
									 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
									where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =5 and ps2.state = 10 
							        
							        	union 
							 		
							 		select p.patient_id, max(o.obs_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
							 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union
							 		 
							 		select p.patient_id, max(e.encounter_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
							 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =5 
							 			group by p.patient_id 
							 		
							 		union 
								 	
								 	select person_id, death_date 
								 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
								) obito group by patient_id
							) obito 
							inner join 
							( 
								select patient_id, max(encounter_datetime) encounter_datetime 
								from( 
							 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
							 		from patient p 
							 			inner join encounter e on e.patient_id = p.patient_id 
							 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
							 			and e.location_id =5 
							 			group by p.patient_id 
								) consultaLev group by patient_id 
							) 
							consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
							 where consultaOuARV.encounter_datetime <= obito.data_obito 
						) obitos
				  		
				  	) saidas on reinicioTARV.patient_id = saidas.patient_id
				  	where saidas.patient_id is null
				  ) reinicios
		   ) reinicios
	) estadoPermanencia group by estadoPermanencia.patient_id
) estadoPermanencia on estadoPermanencia.patient_id = inicioDAH.patient_id 
left join
( 
	select ultima_situacao_tarv.patient_id,ultima_situacao_tarv.data_situacao_inicio_tarv_dah, o.value_coded 
	from(
		select p.patient_id,max(e.encounter_datetime) data_situacao_inicio_tarv_dah, o.value_coded 
		from patient p 
			inner join encounter e on p.patient_id=e.patient_id 
	   		inner join obs o on o.encounter_id=e.encounter_id 
		where e.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate and o.voided=0 
			 and e.encounter_type=90 and  e.location_id=:location group by p.patient_id 
	)ultima_situacao_tarv
		inner join encounter e on e.patient_id = ultima_situacao_tarv.patient_id
		inner join obs o on o.encounter_id = e.encounter_id 
		where e.voided = 0 and o.voided = 0 and e.encounter_type = 90  and o.concept_id=1255 and ultima_situacao_tarv.data_situacao_inicio_tarv_dah = o.obs_datetime  
		and o.value_coded in (1256, 1705, 6276, 6275) 	
)situacaoTARVnoDAH on inicioDAH.patient_id = situacaoTARVnoDAH.patient_id 
left join 
( 
	select p.patient_id, max(e.encounter_datetime) encounter_datetime 
	from patient p 
    		inner join encounter e on p.patient_id=e.patient_id 
    where e.voided=0 and p.voided=0 and  e.encounter_type =90 
    		and e.encounter_datetime between :startDate and :endDate and e.location_id=:location 
    		group by patient_id 
)seguimentoDAH on inicioDAH.patient_id = seguimentoDAH.patient_id 
left join 
( 
	select patient_id,max(max_data_cd4) max_data_cd4, value_numeric, type 
	from( 
		select distinct max_cd4.patient_id, max_data_cd4,o.value_numeric, 1 as type
		from( 
    			select p.patient_id,max(o.obs_datetime) max_data_cd4  
    			from patient p 
    				inner join encounter e on p.patient_id=e.patient_id 
    				inner join obs o on e.encounter_id=o.encounter_id 
    			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
    				and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
    				group by p.patient_id 
    		)max_cd4 
    			inner join person per on per.person_id=max_cd4.patient_id 
    			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
    			and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 1695 and o.value_numeric<200 and o.location_id=:location 
        	
        	union 

        		    select distinct max_cd4.patient_id, max_data_cd4, o.value_coded, 2 as type   
	    from( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cd4  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,51,13,53,90) 
	    			and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
	    				group by p.patient_id 
	    	)
	    max_cd4 
	    		inner join person per on per.person_id=max_cd4.patient_id 
	    		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
	    		and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165515 and o.value_coded=165513 and o.location_id=:location 

	    		union
       	
       	select distinct max_cd4.patient_id,max_data_cd4,o.value_numeric, 1 as type 
       	from ( 
    				select p.patient_id,max(o.obs_datetime) max_data_cd4  
    				from patient p 
    					inner join encounter e on p.patient_id=e.patient_id 
    					inner join obs o on e.encounter_id=o.encounter_id 
    				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
    					and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
    					group by p.patient_id 
    		)max_cd4 
    			inner join person per on per.person_id=max_cd4.patient_id 
    			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
   			and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)>=12 and timestampdiff(year,per.birthdate,:endDate)<5 and o.concept_id = 1695 and o.value_numeric<500 and o.location_id=:location 
            
       	union 
       	
       	select distinct max_cd4.patient_id,max_data_cd4,o.value_numeric, 1 as type 
       	from( 
    			select p.patient_id,max(o.obs_datetime) max_data_cd4 
    			from patient p 
    				inner join encounter e on p.patient_id=e.patient_id 
    				inner join obs o on e.encounter_id=o.encounter_id 
    			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
    				and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
    				group by p.patient_id 
    		)max_cd4 
    			inner join person per on per.person_id=max_cd4.patient_id 
    			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
    			and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)<12 and o.concept_id = 1695 and o.value_numeric<750 and o.location_id=:location 
    	) cd4 group by patient_id 
) cd4EligibilidadeMDSDAH on inicioDAH.patient_id = cd4EligibilidadeMDSDAH.patient_id  
left join( 
select patient_id, encounter_datetime, group_concat(motivoEstadioClinico) motivoEstadio, MAX(estadiamentoClinico.tipoEstadio) AS tipoEstadio 
from( 
	select estadiamentoClinico.patient_id, encounter_datetime encounter_datetime, 
		case estadiamentoClinico.value_coded 
			when 14656 then 'Caquexia' 
			when 7180 then 'Toxoplasmose' 
			when 6990 then 'Doença pelo HIV resultando encefalopatia' 
			when 5344 then 'Herpes simples> 1 mês ou viisceral' 
			when 5340 then 'Candidíase esofágica' 
			when 1294 then 'Meningite Criptocócica' 
			when 5042 then 'Tuberculose extrapulmonar' 
			when 507 then 'Sarcoma de Kaposi (SK)' 
			when 1570 then 'Cancro do colo do útero' 
			when 5018 then 'Diarreia Crónica' 
			when 5945 then 'Febre Inexplicada' 
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
			when 1294 then 'Meningite Criptocócica' 
			when 5042 then 'Tuberculose extrapulmonar' 
			when 507 then 'Sarcoma de Kaposi' 
			when 1570 then 'Cancro do colo do útero' 
			when 60 then 'Meningite' 
			when 5018 then 'Diarréia Crónica > 1 Mês' 
			when 5945 then 'Febre' 
			when 42 then 'Tuberculose Pulmonar' 
			when 3 then 'Anemia' 
			when 43 then 'Pneumonia' 
			when 126 then 'Gengivite' 
			when 6783 then 'Estomatite ulcerativa necrotizante' 
			when 5334 then 'Candidíase oral' 
		end as motivoEstadioClinico, tipoEstadio 
	from( 
   		select estadio4.patient_id, estadio4.encounter_datetime,o.value_coded, 4 as tipoEstadio 
   		from( 
   			select p.patient_id,min(e.encounter_datetime) encounter_datetime 
   			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on o.encounter_id=e.encounter_id 
			where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
				and o.obs_datetime <= :endDate 
				group by p.patient_id 
		) estadio4 
			inner join encounter e on e.patient_id = estadio4.patient_id 
			inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio4.encounter_datetime 
		where e.voided = 0 and o.voided = 0 and o.value_coded in (14656, 7180, 6990, 5344, 5340, 1294, 5042, 507, 1570) 

		union 
		
		select estadio3.patient_id, estadio3.encounter_datetime,o.value_coded, 3 as tipoEstadio 
		from( 
   			select p.patient_id,min(e.encounter_datetime) encounter_datetime 
   			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on o.encounter_id=e.encounter_id 
			where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
				and o.obs_datetime <= :endDate 
				group by p.patient_id 
		) estadio3 
			inner join encounter e on e.patient_id = estadio3.patient_id 
			inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio3.encounter_datetime 
		where e.voided = 0 and o.voided = 0 and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334) 
		)estadiamentoClinico order by estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime, tipoEstadio desc  
	)estadiamentoClinico group by estadiamentoClinico.patient_id 
)estadiamentoClinico on estadiamentoClinico.patient_id = inicioDAH.patient_id 
left join 
( 
	select patient_id, max(data_cd4) max_data_cd4 
	from ( 
		select p.patient_id, e.encounter_datetime data_cd4  
		From patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
			and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
			
		select p.patient_id, o.obs_datetime data_cd4  
		From patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 90 
			and o.obs_datetime <= :endDate and e.location_id=:location
	
	union
	
		select p.patient_id, o.obs_datetime data_cd4  
		From patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 53
			and o.obs_datetime <= :endDate and e.location_id=:location
	 		
	) maxCD4 group by patient_id 
 ) maxCD4 on inicioDAH.patient_id = maxCD4.patient_id 
 left join
 (
 	select maxCD4.patient_id, IF(ISNULL(o.value_numeric) and ISNULL(o.value_coded), 'N/A',IF(ISNULL(o.value_coded), o.value_numeric,IF(o.value_coded=165513, '<=200','>200'))) as value_numeric
	from ( 
		select patient_id, max(data_cd4) max_data_cd4 
		from ( 
			select p.patient_id, e.encounter_datetime data_cd4  
			From patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
				and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
			
			select p.patient_id, o.obs_datetime data_cd4  
			From patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 90 
				and o.obs_datetime <= :endDate and e.location_id=:location
		
		union
		
			select p.patient_id, o.obs_datetime data_cd4  
			From patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id in (1695,165515) and e.encounter_type = 53
				and o.obs_datetime <= :endDate and e.location_id=:location
		 		
		) maxCD4 group by patient_id 
	) maxCD4
		inner join encounter e on e.patient_id = maxCD4.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where e.voided = 0 and o.voided = 0 and  o.concept_id in (1695, 165515)
	 and (( e.encounter_type = 90 and o.obs_datetime = maxCD4.max_data_cd4) or ( e.encounter_type = 53 and o.obs_datetime = maxCD4.max_data_cd4)
	 or ( e.encounter_type = 6 and e.encounter_datetime = maxCD4.max_data_cd4))
 ) valor_ultimo_cd4 on  valor_ultimo_cd4.patient_id = inicioDAH.patient_id 
  left join 
( 
	select patient_id, max(data_cd4) max_data_cd4 
	from ( 
		select p.patient_id, o.obs_datetime data_cd4  
		From patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type in (51,13) 
			and o.obs_datetime <= :endDate and e.location_id=:location
	) maxCD4 group by patient_id 
 ) maxCD4Lab on inicioDAH.patient_id = maxCD4Lab.patient_id 
  left join
 (
 	select maxCD4.patient_id, IF(ISNULL(o.value_numeric) and ISNULL(o.value_coded), 'N/A',IF(ISNULL(o.value_coded), o.value_numeric,IF(o.value_coded=165513, '<=200','>200'))) as value_numeric
	from ( 
		select patient_id, max(data_cd4) max_data_cd4 
		from ( 
			select p.patient_id, o.obs_datetime data_cd4  
			From patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type in (51,13) 
				and o.obs_datetime <= :endDate and e.location_id=:location	
		) maxCD4 group by patient_id 
	) maxCD4
		inner join encounter e on e.patient_id = maxCD4.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where e.voided = 0 and o.voided = 0 and  o.concept_id in (1695, 165515)
	 and (( e.encounter_type in (51,13) and o.obs_datetime = maxCD4.max_data_cd4))
 ) valor_ultimo_cd4Lab on  valor_ultimo_cd4Lab.patient_id = inicioDAH.patient_id 
left join
( 
	select penultimoCd4.patient_id, penultimoCd4.dataCd4Anterior
	from(
		select ultimoCd4.patient_id, max(penultimoCd4.obs_datetime) dataCd4Anterior
		from( 
		
	    		select patient_id, max(data_cd4) max_data_cd4 
			from ( 
				select p.patient_id, e.encounter_datetime data_cd4  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
					and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
			
				select p.patient_id, o.obs_datetime data_cd4  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 90 
					and o.obs_datetime <= :endDate and e.location_id=:location
			
			union
			
				select p.patient_id, o.obs_datetime data_cd4  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 53
					and o.obs_datetime <= :endDate and e.location_id=:location
			 		
			) maxCD4 group by patient_id 	
	    ) ultimoCd4 
	    inner join 
	    ( 
	    		select p.patient_id, e.encounter_datetime obs_datetime, o.value_numeric  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
					and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
			
			Select p.patient_id, o.obs_datetime, o.value_numeric  
			From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type = 90 
	    			and o.obs_datetime <= :endDate and e.location_id=:location

	    		union

	    		Select p.patient_id, o.obs_datetime, o.value_numeric  
			From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type =53
	    			and o.obs_datetime <= :endDate and e.location_id=:location 
	    )
	   penultimoCd4 on penultimoCd4.patient_id = ultimoCd4.patient_id 
	   	and date(penultimoCd4.obs_datetime) < ultimoCd4.max_data_cd4 
	    	group by patient_id order by penultimoCd4.obs_datetime desc 
	) penultimoCd4  group by penultimoCd4.patient_id
)penultimoCd4 on inicioDAH.patient_id = penultimoCd4.patient_id 
left join
(
	
	select valor_penultimo_cd4.patient_id, IF(ISNULL(o.value_numeric) and ISNULL(o.value_coded), 'N/A',IF(ISNULL(o.value_coded), o.value_numeric,IF(o.value_coded=165513, '<=200','>200'))) as value_numeric
	from ( 
		select penultimoCd4.patient_id, penultimoCd4.dataCd4Anterior
		from(
			select ultimoCd4.patient_id, max(penultimoCd4.obs_datetime) dataCd4Anterior
			from( 
			
		    		select patient_id, max(data_cd4) max_data_cd4 
				from ( 
				select p.patient_id, e.encounter_datetime data_cd4  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
					and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
					select p.patient_id, o.obs_datetime data_cd4  
					From patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 90 
						and o.obs_datetime <= :endDate and e.location_id=:location
				
				union
				
					select p.patient_id, o.obs_datetime data_cd4  
					From patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 53
						and o.obs_datetime <= :endDate and e.location_id=:location
				 		
				) maxCD4 group by patient_id 	
		    ) ultimoCd4 
		    inner join 
		    ( 
		    	select p.patient_id, e.encounter_datetime obs_datetime, o.value_numeric  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type = 6
					and e.encounter_datetime <= :endDate and e.location_id=:location
			
			union
			
				Select p.patient_id, o.obs_datetime, o.value_numeric  
				From patient p 
		    			inner join encounter e on p.patient_id=e.patient_id 
		    			inner join obs o on e.encounter_id=o.encounter_id 
		    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type = 90 
		    			and o.obs_datetime <= :endDate and e.location_id=:location
	
		    		union
	
		    		Select p.patient_id, o.obs_datetime, o.value_numeric  
				From patient p 
		    			inner join encounter e on p.patient_id=e.patient_id 
		    			inner join obs o on e.encounter_id=o.encounter_id 
		    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type =53
		    			and o.obs_datetime <= :endDate and e.location_id=:location 
		    )
		   penultimoCd4 on penultimoCd4.patient_id = ultimoCd4.patient_id 
		   	and date(penultimoCd4.obs_datetime) < ultimoCd4.max_data_cd4 
		    	group by patient_id order by penultimoCd4.obs_datetime desc 
		) penultimoCd4  group by penultimoCd4.patient_id
	) valor_penultimo_cd4
		inner join encounter e on e.patient_id = valor_penultimo_cd4.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where e.voided = 0 and o.voided = 0 and  o.concept_id in (1695,165515)
	 and (( e.encounter_type = 90 and o.obs_datetime = valor_penultimo_cd4.dataCd4Anterior) or ( e.encounter_type = 53 and o.obs_datetime = valor_penultimo_cd4.dataCd4Anterior)
	 or ( e.encounter_type = 6 and e.encounter_datetime = valor_penultimo_cd4.dataCd4Anterior))
)
valor_penultimo_cd4 on inicioDAH.patient_id = valor_penultimo_cd4.patient_id 
left join
( 
	select penultimoCd4.patient_id, penultimoCd4.dataCd4Anterior
	from(
		select ultimoCd4.patient_id, max(penultimoCd4.obs_datetime) dataCd4Anterior
		from( 
		
	    		select patient_id, max(data_cd4) max_data_cd4 
			from ( 
				select p.patient_id, o.obs_datetime data_cd4  
				From patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type in (51,13) 
					and o.obs_datetime <= :endDate and e.location_id=:location
			) maxCD4 group by patient_id 	
	    ) ultimoCd4 
	    inner join 
	    ( 
			Select p.patient_id, o.obs_datetime, o.value_numeric  
			From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type in (51,13) 
	    			and o.obs_datetime <= :endDate and e.location_id=:location
	    )
	   penultimoCd4 on penultimoCd4.patient_id = ultimoCd4.patient_id 
	   	and date(penultimoCd4.obs_datetime) < ultimoCd4.max_data_cd4 
	    	group by patient_id order by penultimoCd4.obs_datetime desc 
	) penultimoCd4  group by penultimoCd4.patient_id
)penultimoCd4Lab on inicioDAH.patient_id = penultimoCd4Lab.patient_id 
left join
(
	
	select valor_penultimo_cd4.patient_id, IF(ISNULL(o.value_numeric) and ISNULL(o.value_coded), 'N/A',IF(ISNULL(o.value_coded), o.value_numeric,IF(o.value_coded=165513, '<=200','>200'))) as value_numeric
	from ( 
		select penultimoCd4.patient_id, penultimoCd4.dataCd4Anterior
		from(
			select ultimoCd4.patient_id, max(penultimoCd4.obs_datetime) dataCd4Anterior
			from( 
			
		    		select patient_id, max(data_cd4) max_data_cd4 
				from ( 
					select p.patient_id, o.obs_datetime data_cd4  
					From patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and e.encounter_type in (51,13) 
						and o.obs_datetime <= :endDate and e.location_id=:location
				) maxCD4 group by patient_id 	
		    ) ultimoCd4 
		    inner join 
		    (
				Select p.patient_id, o.obs_datetime, o.value_numeric  
				From patient p 
		    			inner join encounter e on p.patient_id=e.patient_id 
		    			inner join obs o on e.encounter_id=o.encounter_id 
		    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (1695,165515) and  e.encounter_type in (51,13) 
		    			and o.obs_datetime <= :endDate and e.location_id=:location
		    )
		   penultimoCd4 on penultimoCd4.patient_id = ultimoCd4.patient_id 
		   	and date(penultimoCd4.obs_datetime) < ultimoCd4.max_data_cd4 
		    	group by patient_id order by penultimoCd4.obs_datetime desc 
		) penultimoCd4  group by penultimoCd4.patient_id
	) valor_penultimo_cd4
		inner join encounter e on e.patient_id = valor_penultimo_cd4.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where e.voided = 0 and o.voided = 0 and  o.concept_id in (1695,165515)
	 and (( e.encounter_type in (51,13) and o.obs_datetime = valor_penultimo_cd4.dataCd4Anterior))
)
valor_penultimo_cd4Lab on inicioDAH.patient_id = valor_penultimo_cd4Lab.patient_id 
 left join ( 
    select patient_id, max(data_carga) data_carga, resultadoCV, comments from (
    select patient_id, data_carga, resultadoCV, comments from (
    select ultima_carga.patient_id,ultima_carga.data_carga, IF(ISNULL(obs.value_numeric), IF(ISNULL(obs.value_coded), 'N/A', 
 case 
                                    obs.value_coded 
                                    when 
                                       1306
                                    then 
                                       'Nivel baixo de detecção' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                       CONCAT('MENOR QUE', ' ',obs.comments) 
                                    else 
                                       null 
                                 end), obs.value_numeric) AS resultadoCV , obs.comments from ( 
            Select p.patient_id,max(o.obs_datetime) data_carga, e.encounter_type from patient p 
            inner join encounter e on p.patient_id=e.patient_id 
            inner join obs o on e.encounter_id=o.encounter_id 
            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) 
            and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
            inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
            where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
            or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
            ) ultimaCV order by ultimaCV.data_carga desc
            ) ultimaCV group by ultimaCV.patient_id
    )ultimaCV on ultimaCV.patient_id = inicioDAH.patient_id
      left join ( 
    select patient_id,max(penultimaDataCarga) penultimaDataCarga, resultadoCV from ( 
    select patient_id, penultimaDataCarga, resultadoCV from (
    select * from (
    select e.patient_id, date(o.obs_datetime) penultimaDataCarga,IF(ISNULL(o.value_numeric), IF(ISNULL(o.value_coded), 'N/A', 
 case 
                                    o.value_coded 
                                    when 
                                       1306 
                                    then 
                                       'Nivel baixo de detecção' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                        CONCAT('MENOR QUE', ' ',o.comments) 
                                 end), o.value_numeric) AS resultadoCV, data_carga 
                                 from (select ultima_carga.patient_id,ultima_carga.data_carga,obs.value_numeric valor_carga,obs.concept_id,obs.value_coded from ( 
                Select p.patient_id,max(o.obs_datetime) data_carga from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on e.encounter_id=o.encounter_id 
                where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) 
                and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
                inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
                where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
                or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
    ) ultimaCV inner join encounter e on e.patient_id = ultimaCV.patient_id 
    inner join obs o on o.encounter_id = e.encounter_id 
    where  ((o.concept_id=856 and o.value_numeric is not null) 
     or (o.concept_id=1305 and o.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and e.location_id =5 and o.voided = 0 and e.voided = 0 and e.encounter_type in (6,9,53) 
    and date(o.obs_datetime) < date(ultimaCV.data_carga) order by date(o.obs_datetime) desc 
    ) primeira
    ) penultimaCV order by penultimaCV.penultimaDataCarga desc
    ) penultimaCV group by penultimaCV.patient_id 
    )penultimaCV on penultimaCV.patient_id = inicioDAH.patient_id
        left join ( 
    select patient_id, max(data_carga) data_carga, resultadoCV, comments from (
    select patient_id, data_carga, resultadoCV, comments from (
    select ultima_carga.patient_id,ultima_carga.data_carga, IF(ISNULL(obs.value_numeric), IF(ISNULL(obs.value_coded), 'N/A', 
 case 
                                    obs.value_coded 
                                    when 
                                       1306
                                    then 
                                       'Nivel baixo de detecção' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                       CONCAT('MENOR QUE', ' ',obs.comments) 
                                    else 
                                       null 
                                 end), obs.value_numeric) AS resultadoCV , obs.comments from ( 
            Select p.patient_id,max(o.obs_datetime) data_carga, e.encounter_type from patient p 
            inner join encounter e on p.patient_id=e.patient_id 
            inner join obs o on e.encounter_id=o.encounter_id 
            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type = 13
            and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
            inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
            where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
            or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
            union
                select ultima_carga.patient_id,ultima_carga.data_carga, IF(ISNULL(obs.value_numeric), IF(ISNULL(obs.value_coded), 'N/A', 
 case 
                                    obs.value_coded 
                                    when 
                                       1306
                                    then 
                                       'Nivel de detecção baixo' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                       CONCAT('MENOR QUE', ' ',obs.comments) 
                                    else 
                                       null 
                                 end), obs.value_numeric) AS resultadoCV , obs.comments from ( 
            Select p.patient_id,max(o.obs_datetime) data_carga, e.encounter_type from patient p 
            inner join encounter e on p.patient_id=e.patient_id 
            inner join obs o on e.encounter_id=o.encounter_id 
            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type = 51 
            and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
            inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
            where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
            or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
            ) ultimaCVLab order by ultimaCVLab.data_carga desc
            ) ultimaCVLab group by ultimaCVLab.patient_id
    )ultimaCVLab on ultimaCVLab.patient_id = inicioDAH.patient_id
      left join ( 
    select patient_id,max(penultimaDataCarga) penultimaDataCarga, resultadoCV from ( 
    select patient_id, penultimaDataCarga, resultadoCV from (
    select * from (
    select e.patient_id, date(o.obs_datetime) penultimaDataCarga,IF(ISNULL(o.value_numeric), IF(ISNULL(o.value_coded), 'N/A', 
 case 
                                    o.value_coded 
                                    when 
                                       1306 
                                    then 
                                       'Nivel baixo de detecção' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                        CONCAT('MENOR QUE', ' ',o.comments) 
                                 end), o.value_numeric) AS resultadoCV, data_carga 
                                 from (select ultima_carga.patient_id,ultima_carga.data_carga,obs.value_numeric valor_carga,obs.concept_id,obs.value_coded from ( 
                Select p.patient_id,max(o.obs_datetime) data_carga from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on e.encounter_id=o.encounter_id 
                where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51) 
                and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
                inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
                where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
                or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
    ) ultimaCVLab inner join encounter e on e.patient_id = ultimaCVLab.patient_id 
    inner join obs o on o.encounter_id = e.encounter_id 
    where  ((o.concept_id=856 and o.value_numeric is not null) 
     or (o.concept_id=1305 and o.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and e.location_id =5 and o.voided = 0 and e.voided = 0 and e.encounter_type = 13
    and date(o.obs_datetime) < date(ultimaCVLab.data_carga) order by date(o.obs_datetime) desc 
    ) primeira
    union
    select e.patient_id, date(o.obs_datetime) penultimaDataCarga,IF(ISNULL(o.value_numeric), IF(ISNULL(o.value_coded), 'N/A', 
                      case 
                                    o.value_coded 
                                    when 
                                       1306 
                                    then 
                                       'Nivel de detecção baixo' 
                                    when 
                                       1304 
                                    then 
                                       'MA QUALIDADE DA AMOSTRA' 
                                    when 
                                       23814 
                                    then 
                                       'Indetectável' 
                                    when 
                                       23905 
                                    then 
                                       'MENOR QUE 10 COPIAS/ML' 
                                    when 
                                       23906 
                                    then 
                                       'MENOR QUE 20 COPIAS/ML' 
                                    when 
                                       23907 
                                    then 
                                       'MENOR QUE 40 COPIAS/ML' 
                                    when 
                                       23908 
                                    then 
                                       'MENOR QUE 400 COPIAS/ML' 
                                    when 
                                       23904 
                                    then 
                                       'MENOR QUE 839 COPIAS/ML' 
                                    when 
                                       165331 
                                    then 
                                        CONCAT('MENOR QUE', ' ',o.comments) 
                                 end), o.value_numeric) AS resultadoCV, data_carga 
                                 from (select ultima_carga.patient_id,ultima_carga.data_carga,obs.value_numeric valor_carga,obs.concept_id,obs.value_coded from ( 
                Select p.patient_id,max(o.obs_datetime) data_carga from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on e.encounter_id=o.encounter_id 
                where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51) 
                and  o.concept_id in (856,1305) and  date(o.obs_datetime) <= :endDate and e.location_id=:location group by p.patient_id) ultima_carga 
                inner join obs on obs.person_id=ultima_carga.patient_id and date(obs.obs_datetime)=date(ultima_carga.data_carga) 
                where obs.voided=0 and ((obs.concept_id=856 and obs.value_numeric is not null) 
                or (obs.concept_id=1305 and obs.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and obs.location_id=:location 
    ) ultimaCVLab inner join encounter e on e.patient_id = ultimaCVLab.patient_id 
    inner join obs o on o.encounter_id = e.encounter_id 
    where  ((o.concept_id=856 and o.value_numeric is not null) 
     or (o.concept_id=1305 and o.value_coded in (1306,23814,23905,23906,23907,23908,23904,165331)))  and e.location_id =5 and o.voided = 0 and e.voided = 0 and e.encounter_type = 51
    and date(o.obs_datetime) < date(ultimaCVLab.data_carga) order by date(penultimaDataCarga) desc 
    ) penultimaCVLab order by penultimaCVLab.penultimaDataCarga desc
    ) penultimaCVLab group by penultimaCVLab.patient_id 
    )penultimaCVLab on penultimaCVLab.patient_id = inicioDAH.patient_id 
    left join( 
	select patient_id, max(max_tblam) max_tblam,resultadoTbLam from (
	      Select p.patient_id,o.obs_datetime max_tblam, 
	     case o.value_coded 
	      when 703 then 'Positivo' 
	      when 664 then 'Negativo' 
	      when 21233 then 'N/A' 
	      when 1118 then 'NF' 
	      end as resultadoTbLam From patient p 
	    inner join encounter e on p.patient_id=e.patient_id 
	    inner join obs o on e.encounter_id=o.encounter_id 
	    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 23951 and  e.encounter_type in (6,13,90)
	    and o.obs_datetime <= :endDate and e.location_id=:location
	    order by o.obs_datetime desc
    ) max_tblam
    group by max_tblam.patient_id 
    )ultimoTbLam on ultimoTbLam.patient_id = inicioDAH.patient_id 
    left join ( 
    select patient_id, max(max_cragSoro) max_cragSoro, resultadoCragSoro from (
	select patient_id, max(max_cragSoro) max_cragSoro,resultadoCragSoro from (
     Select p.patient_id,o.obs_datetime max_cragSoro, 
     case o.value_coded 
     when 703 then 'Positivo' 
      when 664 then 'Negativo' 
      when 21233 then 'N/A' 
      when 1118 then 'NF' 
      end as resultadoCragSoro 
      From patient p 
    inner join encounter e on p.patient_id=e.patient_id 
    inner join obs o on e.encounter_id=o.encounter_id 
    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 23952 and  e.encounter_type in (6,90)
    and o.obs_datetime <= :endDate and e.location_id=:location 
    order by o.obs_datetime desc
        ) cragSoro
    group by cragSoro.patient_id 
    union
    	select patient_id, max(max_cragSoro) max_cragSoro,resultadoCragSoro from (
     Select p.patient_id,o.obs_datetime max_cragSoro, 
     case o.value_coded 
     when 703 then 'Positivo' 
      when 664 then 'Negativo'
      end as resultadoCragSoro 
      From patient p 
    inner join encounter e on p.patient_id=e.patient_id 
    inner join obs o on e.encounter_id=o.encounter_id 
    inner join obs oAmostra on e.encounter_id=oAmostra.encounter_id 
    where p.voided=0 and e.voided=0 and o.voided=0 and oAmostra.voided = 0  and o.concept_id = 23952
    and oAmostra.concept_id = 23832 and (oAmostra.value_coded = 1001 or oAmostra.value_coded is null)
    and  e.encounter_type = 13
    and o.obs_datetime <= :endDate and e.location_id=:location 
    order by o.obs_datetime desc
    ) cragSoro
    group by cragSoro.patient_id 
    ) final group by final.patient_id
    )cragSoro on cragSoro.patient_id = inicioDAH.patient_id 
    left join ( 
    select patient_id, max(max_cragLcr) max_cragLcr, resultadoCragLcr from (
         Select p.patient_id,max(o.obs_datetime) max_cragLcr, 
         case o.value_coded 
          when 703 then 'Positivo' 
            when 664 then 'Negativo' 
            when 21233 then 'N/A' 
            when 1118 then 'NF' 
            end as resultadoCragLcr 
            From patient p 
    inner join encounter e on p.patient_id=e.patient_id 
    inner join obs o on e.encounter_id=o.encounter_id 
    where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165362 and  e.encounter_type in (6,13,90) 
    and o.obs_datetime <= :endDate and e.location_id=:location 
    group by p.patient_id 
    union
        	select patient_id, max(max_cragLcr) max_cragLcr,resultadoCragLcr from (
     Select p.patient_id,o.obs_datetime max_cragLcr, 
     case o.value_coded 
     when 703 then 'Positivo' 
      when 664 then 'Negativo'
      end as resultadoCragLcr 
      From patient p 
    inner join encounter e on p.patient_id=e.patient_id 
    inner join obs o on e.encounter_id=o.encounter_id 
    inner join obs oAmostra on e.encounter_id=oAmostra.encounter_id 
    where p.voided=0 and e.voided=0 and o.voided=0 and oAmostra.voided = 0  and o.concept_id = 23952
    and oAmostra.concept_id = 23832 and oAmostra.value_coded = 159995
    and  e.encounter_type = 13
    and o.obs_datetime <= :endDate and e.location_id=:location 
    order by o.obs_datetime desc
    ) cragLcr
    group by cragLcr.patient_id 
    ) final group by final.patient_id
    )cragLcr on cragLcr.patient_id = inicioDAH.patient_id 
    left join ( 
              select max_cacu.patient_id,
    o.obs_datetime max_cacu,
      case o.value_coded 
              when 703 then 'Via Positivo' 
                 when 664 then 'Via Negativo' 
                 	when 2093 then 'Suspeita de cancro' 
                 end as resultadoCacu
	    from( 
	    		Select p.patient_id,max(o.obs_datetime) max_data_cacu  
	    		From patient p 
	    			inner join encounter e on p.patient_id=e.patient_id 
	    			inner join obs o on e.encounter_id=o.encounter_id 
	    		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 2094 and  e.encounter_type in (6,53,90,28)
	    			and o.obs_datetime <= :endDate and e.location_id=:location 
	    				group by p.patient_id 
	    	)
	    max_cacu 
	    		inner join encounter e on e.patient_id=max_cacu.patient_id 
	    		inner join obs o on o.encounter_id=e.encounter_id and max_cacu.max_data_cacu=o.obs_datetime and o.voided=0  
	    		and o.concept_id = 2094 and o.location_id=:location 
    )ultimoCacu on ultimoCacu.patient_id =  inicioDAH.patient_id 
    group by inicioDAH.patient_id 