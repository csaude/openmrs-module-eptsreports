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
				--group by p.patient_id,estadoProfilaxia.obs_datetime
	
	          ) inicioAnterior                                                                                                                          
	          on inicio.patient_id = inicioAnterior.patient_id
	          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
	            where inicioAnterior.patient_id is null             
         
      ) inicio_inh                                                                                                                                         
	inner join        																																
	(  		
		select p.patient_id, estadoProfilaxia.obs_datetime data_final_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1267 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate																															
	) termino_inh  																																	
		on inicio_inh.patient_id=termino_inh.patient_id      																						
	where termino_inh.data_final_inh between  (inicio_inh.data_inicio_inh + interval 173 day) and (inicio_inh.data_inicio_inh + interval 365 day ) 

union	

select inicio_inh.patient_id                                                                                                               
from (  
	  select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
			--group by p.patient_id,estadoProfilaxia.obs_datetime
   ) inicio_inh 
   inner join        																																
	(  select p.patient_id, estadoProfilaxia.obs_datetime data_final_inh,e.encounter_id
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded in (1256,1257)
			and e.encounter_type in (6,9) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate																																		
	) termino_inh  																																	
		on inicio_inh.patient_id=termino_inh.patient_id 
	where termino_inh.data_final_inh between (inicio_inh.data_inicio_inh + INTERVAL 1 DAY) and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH)
	 group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct termino_inh.encounter_id) >= 5
   
union

select inicio_inh.patient_id 
from(
    select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
			--group by p.patient_id,estadoProfilaxia.obs_datetime
)inicio_inh 
inner join
(
	 select distinct p.patient_id, estadoProfilaxia.obs_datetime data_final_inh,e.encounter_id 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
			inner join obs outraPrescricaoDTINH on e.encounter_id=outraPrescricaoDTINH.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 and outraPrescricaoDTINH.voided=0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded in (1256,1257)
			and outraPrescricaoDTINH.concept_id=1719 and outraPrescricaoDTINH.value_coded=23955
			and e.encounter_type in (6,9) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate			
)
consultasINH on inicio_inh.patient_id = consultasINH.patient_id
where consultasINH.data_final_inh between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 5 MONTH)
group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct consultasINH.encounter_id) >= 2

union

select consultasSemDTINH.patient_id 
from (
select inicio_inh.patient_id 
from(
     select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
			--group by p.patient_id,estadoProfilaxia.obs_datetime
 )inicio_inh 
 inner join
(
   	select p.patient_id, estadoProfilaxia.obs_datetime data_final_inh,e.encounter_id 
	from patient p 
		inner join encounter e on p.patient_id = e.patient_id 
		inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
		inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
	where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
		and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded  in (1256, 1257) 
		and e.encounter_type in (6,9) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate
)
consultasSemDTINH on inicio_inh.patient_id = consultasSemDTINH.patient_id
where consultasSemDTINH.data_final_inh between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH)
    group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct consultasSemDTINH.encounter_id)>=3 
  ) 
 consultasSemDTINH
 inner join
 (
    select inicio_inh.patient_id                                                                                                           
    from(   
         select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_inh 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
			--group by p.patient_id,estadoProfilaxia.obs_datetime
      )
    inicio_inh 
    inner join
     (
     select p.patient_id, estadoProfilaxia.obs_datetime data_final_inh,e.encounter_id 
     from patient p 
     	inner join encounter e on p.patient_id = e.patient_id 
	  	inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
	  	inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id 
	  	inner join obs outraPrescricaoDTINH on e.encounter_id=outraPrescricaoDTINH.encounter_id
	where p.voided = 0 and e.voided = 0 and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 and outraPrescricaoDTINH.voided=0
		and e.encounter_type in(6,9) and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded in (1256, 1257)
	     and outraPrescricaoDTINH.concept_id=1719 and outraPrescricaoDTINH.value_coded=23955
	     and e.encounter_datetime <= :endDate and e.location_id=:location   
    )
consultasComDTINH on inicio_inh.patient_id = consultasComDTINH.patient_id
where consultasComDTINH.data_final_inh between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH)
    group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct consultasComDTINH.encounter_id)>=1
  ) consultasComDTINH on  consultasComDTINH.patient_id = consultasSemDTINH.patient_id
  
union

select inicio_inh.patient_id                                                                                                               
from                                                                                                                                                
     (  
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
				--group by p.patient_id,estadoProfilaxia.obs_datetime
	
	          ) inicioAnterior                                                                                                                          
	          on inicio.patient_id = inicioAnterior.patient_id
	          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
	            where inicioAnterior.patient_id is null                                                                                              
      ) inicio_inh   
	inner join encounter e on e.patient_id=inicio_inh.patient_id      
	inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id            
	inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id 
where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type = 60      
      and obsDTINH.concept_id=23986 and obsDTINH.value_coded=1098  and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
      and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location  
      group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct e.encounter_id)>=6  

union

 select inicio_inh.patient_id                                                                                                               
 from                                                                                                                                                
     (  
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
				--group by p.patient_id,estadoProfilaxia.obs_datetime
	
	          ) inicioAnterior                                                                                                                          
	          on inicio.patient_id = inicioAnterior.patient_id
	          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
	            where inicioAnterior.patient_id is null                                                                                   
      ) inicio_inh  
		inner join 
		(
			select p.patient_id,e.encounter_datetime data_fim_INH,e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs regimeTPT on regimeTPT.encounter_id = e.encounter_id																			 		
				inner join obs tipoDispensa on tipoDispensa.encounter_id=e.encounter_id
				where regimeTPT.concept_id=23985 and regimeTPT.value_coded in(656,23982) and tipoDispensa.concept_id=23986 and tipoDispensa.value_coded=23720
				and p.voided=0 and e.voided=0 and regimeTPT.voided=0 and tipoDispensa.voided=0 and e.encounter_datetime<=:endDate  and e.location_id=:location

		) DTINH on DTINH.patient_id=inicio_inh.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + interval 5 month) 
		group by inicio_inh.patient_id
		having count(distinct DTINH.encounter_id)>=2

union               

 select inicio_inh.patient_id                                                                                                               
 from                                                                                                                                                
     (  
 select inicio_inh.patient_id                                                                                                               
 from                                                                                                                                                
     (  
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
				--group by p.patient_id,estadoProfilaxia.obs_datetime
	
	          ) inicioAnterior                                                                                                                          
	          on inicio.patient_id = inicioAnterior.patient_id
	          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
	            where inicioAnterior.patient_id is null                                                                                
      ) inicio_inh 
			inner join encounter e on e.patient_id=inicio_inh.patient_id               
			inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id      
			inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id          
		where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type in (60)  
			and obsDTINH.concept_id=23986 and obsDTINH.value_coded=1098  and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
			and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location  
			group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct e.encounter_id)>=3           
)inicio_inh
inner join     
(
	 select  inicio_inh.patient_id                                                                                                               
 	from                                                                                                                                                
     (  
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
			--	group by p.patient_id,estadoProfilaxia.obs_datetime
	          ) inicioAnterior                                                                                                                          
	          on inicio.patient_id = inicioAnterior.patient_id
	          	and inicioAnterior.data_inicio_inh between (inicio.data_inicio_inh - INTERVAL 7 MONTH) and (inicio.data_inicio_inh - INTERVAL 1 day)                                                                                     
	            where inicioAnterior.patient_id is null                                                                                                      
      ) inicio_inh 
		inner join encounter e on e.patient_id=inicio_inh.patient_id               
		inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id      
		inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id          
	where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type in (60)  
	  and obsDTINH.concept_id=23986 and obsDTINH.value_coded=23720  and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
	  and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location  
	  group by inicio_inh.patient_id,inicio_inh.data_inicio_inh having count(distinct e.encounter_id)>=1 
) inicio_inh_dt on inicio_inh_dt.patient_id = inicio_inh.patient_id