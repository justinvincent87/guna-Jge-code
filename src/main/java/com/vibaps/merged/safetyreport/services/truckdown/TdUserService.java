package com.vibaps.merged.safetyreport.services.truckdown;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.repo.truckdown.TdUserRepo;

@Service
public class TdUserService implements ITdUserService{
	
	@Autowired
	private TdUserRepo tdUserRepo;

	@Transactional
	public Optional<TdUser> findById(Long id) {
		// TODO Auto-generated method stub
		
		Optional<TdUser> value=null;
		try
		{
			value= tdUserRepo.findById(id);
		}
		catch (Exception e) {
			System.out.println(e+"error");
			
		}
		return value;
	}

}
