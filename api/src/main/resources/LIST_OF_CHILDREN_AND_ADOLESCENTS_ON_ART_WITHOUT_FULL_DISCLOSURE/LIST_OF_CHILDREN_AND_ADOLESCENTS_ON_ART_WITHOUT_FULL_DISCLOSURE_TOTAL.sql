select revelado.patient_id from
					(
                          select 
                          coorte12meses_final.patient_id,
                          dataRevelacaoTotal,
                          dataRevelacaoParcial,
                          dataNaoRevelado,
                          if(revelado.patient_id is not null,1,if(revelacaoParcial.patient_id is not null,2,if(naoRevelacao.patient_id is not null,3,4))) statusRevelacao
                          from 
                             ( 
           					%s
                             ) coorte12meses_final 
                          inner join person p on p.person_id=coorte12meses_final.patient_id  
                          
                          
                          left join 
                          (
                            select  p.patient_id,max(e.encounter_datetime) dataRevelacaoTotal
                            from    patient p 
                                    inner join encounter e on p.patient_id=e.patient_id
                                    inner join obs o on e.encounter_id=o.encounter_id
                            where   p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6337 and e.encounter_type=35 and 
                                    e.encounter_datetime <=:endDate and e.location_id=:location
                            group by p.patient_id
                            ) revelado on revelado.patient_id=coorte12meses_final.patient_id
                            left join 
                            (
                                select  p.patient_id,max(e.encounter_datetime) dataRevelacaoParcial
                                from    patient p 
                                        inner join encounter e on p.patient_id=e.patient_id
                                        inner join obs o on e.encounter_id=o.encounter_id
                                where   p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6338 and e.encounter_type=35 and 
                                        e.encounter_datetime <=:endDate and e.location_id=:location
                                group by p.patient_id
                            ) revelacaoParcial on revelacaoParcial.patient_id=coorte12meses_final.patient_id
                            left join 
                            (
                                select  p.patient_id,max(e.encounter_datetime) dataNaoRevelado
                                from    patient p 
                                        inner join encounter e on p.patient_id=e.patient_id
                                        inner join obs o on e.encounter_id=o.encounter_id
                                where   p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6339 and e.encounter_type=35 and 
                                        e.encounter_datetime <=:endDate and e.location_id=:location
                                group by p.patient_id
                            ) naoRevelacao on naoRevelacao.patient_id=coorte12meses_final.patient_id
                            
                          where floor(datediff(:endDate,p.birthdate)/365)  between 8 and 14 
                       )revelado