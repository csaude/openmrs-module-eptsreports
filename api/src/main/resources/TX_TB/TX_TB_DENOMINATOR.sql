 Select TX_TB.patient_id from  (


                            Select  p.patient_id,o.value_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id=:location and  o.value_datetime <=:endDate

                            union

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded in(1065,1066)  
                                    and e.location_id=:location  and e.encounter_datetime <=:endDate

                                    union

                           Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded in(703)  
                                    and e.location_id=:location  and e.encounter_datetime <=:endDate

                                    union                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (53) and o.concept_id=1406 and o.value_coded=42  and e.location_id=:location   and e.encounter_datetime <=:endDate

                            union
                            
                            select  pg.patient_id,date_enrolled data_inicio                                                             
                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id                                       
                            where   pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location  and date_enrolled <=:endDate 

                            union

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6) and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id=:location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23761 and o.value_coded in(1065)   and e.location_id=:location  and e.encounter_datetime <=:endDate



                            union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,1761)   and e.location_id=:location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   and e.location_id=:location and e.encounter_datetime <=:endDate

                          union


                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id in(23723,23774,23951,307) and o.value_coded in(703,664)   and e.location_id=:location  and e.encounter_datetime <=:endDate


                            union
                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (6) and o.concept_id in(12) and o.value_coded in(703,664,1138)  and e.location_id=:location  and e.encounter_datetime <=:endDate

                            union        

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23723,23724,23951) and o.value_coded in(703,664)  and e.location_id=:location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(307) and o.value_coded in(703,165184)  and e.location_id=:location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(165189) and o.value_coded in(1065,1066)  and e.location_id=:location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23951)  and e.location_id=:location  and e.encounter_datetime <=:endDate
          


                            )TX_TB

                              
 
                           left join  (

                                    Select TB.patient_id,TB.data_tratamento from    
                                   (
                                    Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                                      and o.concept_id=1113 and  e.location_id=:location    

                                      union

                                      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                              from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                                      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location     

                                      union

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                                      and o.concept_id=1406 and o.value_coded=42 and e.location_id=:location    

                                      union 

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                                      and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location    
                                      

                                      ) TB  where TB.data_tratamento  between date_sub(:endDate, INTERVAL 12 MONTH)  and date_sub(:endDate, INTERVAL 6 MONTH)

                              )TB on TB.patient_id=TX_TB.patient_id

                              where TX_TB.data_inicio between :startDate and :endDate and TB.patient_id is null
                              
                             group by TX_TB.patient_id