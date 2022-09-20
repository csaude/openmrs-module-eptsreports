select inicio_inh.patient_id                                                                                                               
from                                                                                                                                                
  (   
	select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
	from patient p 
		inner join encounter e on p.patient_id = e.patient_id 
		inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
		inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
	where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
		and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
		and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
        group by p.patient_id,estadoProfilaxia.obs_datetime
	union
     
     select p.patient_id, e.encounter_datetime data_inicio_inh                                                                                   
      from patient p                                                                                                                              
          inner join encounter e on p.patient_id = e.patient_id                                                                                     
          inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id = e.encounter_id                                                                                       
          inner join obs seguimentoTPT on seguimentoTPT.encounter_id = e.encounter_id                                                               
      where e.voided=0 and p.voided = 0 and e.encounter_datetime < :endDate and e.location_id=:location                                             
          and seguimentoTPT.voided = 0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)                            
          and regimeIsoniazida.voided = 0 and regimeIsoniazida.concept_id = 23985 and regimeIsoniazida.value_coded in (656,23982) and e.encounter_type=60                                          
      
      union                                                                                                                                       
      
      select inicio.patient_id, inicio.data_inicio_inh                                                                                        
      from                                                                                                                                    
          (  select p.patient_id, e.encounter_datetime data_inicio_inh                                                                       
              from patient p                                                                                                                  
                  inner join encounter e on p.patient_id=e.patient_id                                                                         
                  inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                           
                  inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                   
              where e.voided=0 and p.voided=0 and regimeIsoniazida.voided=0 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982) and e.encounter_type=60  
                  and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)                     
                  and e.encounter_datetime < :endDate and e.location_id=:location                                                             
              
              union                                                                                                                           
              
              select p.patient_id, e.encounter_datetime data_inicio_inh                                                                       
              from patient p                                                                                                                  
                  inner join encounter e on p.patient_id=e.patient_id                                                                         
                  inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                           
                   left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in(1256,1257,1705,1267) and seguimentoTPT.voided =0) 
              where e.voided=0 and p.voided=0 and regimeIsoniazida.voided=0 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982) and e.encounter_type=60  
                  and e.encounter_datetime < :endDate and e.location_id=:location and seguimentoTPT.obs_id is null                            
          ) inicio                                                                                                                                  
      left join                                                                                                                               
          ( 
          	select p.patient_id, e.encounter_datetime data_inicio_inh                                                                       
             from patient p                                                                                                                  
                  inner join encounter e on p.patient_id=e.patient_id                                                                         
                  inner join obs o on o.encounter_id=e.encounter_id                                                                           
             where e.voided=0 and p.voided=0 and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60  
                  and e.encounter_datetime < :endDate and e.location_id=:location       
          	
             union

		select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
            group by p.patient_id,estadoProfilaxia.obs_datetime

          ) inicioAnterior                                                                                                                          
          on inicio.patient_id = inicioAnterior.patient_id
          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
            where inicioAnterior.patient_id is null                                                                                                 
          
       ) inicio_inh                                                                                                                                         ;
