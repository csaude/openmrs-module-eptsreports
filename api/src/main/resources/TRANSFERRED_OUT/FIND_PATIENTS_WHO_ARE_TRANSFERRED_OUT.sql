        select transferidopara.patient_id from  ( 
                        select patient_id,max(data_transferidopara) data_transferidopara from  ( 
                        select maxEstado.patient_id,maxEstado.data_transferidopara from  ( 
                        select pg.patient_id,max(ps.start_date) data_transferidopara  from patient p 
                        inner join patient_program pg on p.patient_id=pg.patient_id 
                        inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                        where pg.voided=0 and ps.voided=0 and p.voided=0 and 
                        pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location group by p.patient_id 
                        ) maxEstado 
                        inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                        inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
                        where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and 
                        ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 
                        union 
                        select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate 
                        and e.encounter_type=21 and o.voided=0 and o.concept_id=2016 and o.value_coded in(1706,23863)  and  e.location_id=:location 
                        group by p.patient_id 
                        union 
                        select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id 
                        where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 
                        and  e.location_id=:location 
                        group by p.patient_id 
                        union 
                        select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and 
                        o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and  e.location_id=:location 
                        group by p.patient_id 
                        ) transferido 
                        group by patient_id 
                        ) transferidopara 
                        inner join 
                        ( 
             		    select * from ( 
                    	select patient_id, max(data_ultimo_levantamento)  data_transferidopara 
				                    from 
				                    ( 
				                        select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento 
										from 
										( 
											select p.patient_id, max(encounter_datetime) data_fila 
				                        			from patient p 
				                              		inner join person pe on pe.person_id = p.patient_id 
				                              		inner join encounter e on e.patient_id=p.patient_id 
				                        			where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 
				                              		and e.location_id=:location and e.encounter_datetime <= :endDate 
				                        				group by p.patient_id 
				                        		) ultimo_fila 
										left join 
											obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id 
											and obs_fila.voided=0 
											and obs_fila.obs_datetime=ultimo_fila.data_fila 
											and obs_fila.concept_id=5096 
											and obs_fila.location_id=:location 
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
                                   )final where final.data_transferidopara between :startDate and :endDate 
                        ) TR_OUT on TR_OUT.patient_id=transferidopara.patient_id 
                        inner join  ( 
                        select patient_id,max(encounter_datetime) encounter_datetime from  ( 
                        select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p 
                        inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) 
                        group by p.patient_id 
                        ) consultaLev 
                        group by patient_id 
                        ) consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id 
                        where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara 
                        and transferidopara.data_transferidopara between :startDate AND :endDate 