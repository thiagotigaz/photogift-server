package com.potlach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potlach.client.api.ChainSvcApi;
import com.potlach.client.api.GiftSvcApi;
import com.potlach.client.api.UserSvcApi;
import com.potlach.model.User;
import com.potlach.model.UserCredentialsStatus.UserCredentialsState;
import com.potlach.repository.RoleRepository;
import com.potlach.repository.UserRepository;
@Controller
public class UserController {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	
	@RequestMapping(value=UserSvcApi.USER_ADD_NEW_PATH, method=RequestMethod.POST)
	public @ResponseBody User addUser(@RequestBody User u){
		u.setUsername(u.getUsername().toLowerCase());
		u.setEmail(u.getEmail().toLowerCase());
		u.setRoles(roleRepo.findAll());
		u = userRepo.save(u);
		return u;
	}
	
	@RequestMapping(value=UserSvcApi.USER_LIKETOUCH_GIFT_PATH, method=RequestMethod.GET)
	public @ResponseBody List<User> findByLikeTouchGiftId(@PathVariable(UserSvcApi.GIFT_PARAMETER)Long giftId){
		//TODO code this
		return userRepo.findByGiftTouchUserId(giftId);
	}
	
	@RequestMapping(value=UserSvcApi.USER_CHECK_CREDENTIALS_PATH, method=RequestMethod.GET)
	public @ResponseBody UserCredentialsState checkExistingCredentials(@PathVariable(UserSvcApi.USERNAME_PARAMETER) String username, @PathVariable(UserSvcApi.EMAIL_PARAMETER) String email){
		User u = null;
		UserCredentialsState state = UserCredentialsState.NONE_AVAILABLE;
		if(username!=null){
			u = userRepo.findByUsernameIgnoreCase(username);
			if(u==null){
				state = UserCredentialsState.USERNAME_AVAILABLE;
			}
		}
		u = null;
		if(email!=null){
			System.out.println("\n\nEMAIl:"+email);
			u = userRepo.findByEmailIgnoreCase(email);
			if(u==null){
				if(state==UserCredentialsState.USERNAME_AVAILABLE)
					state = UserCredentialsState.BOTH_AVAILABLE;
				else
					state = UserCredentialsState.EMAIL_AVAILABLE;
				u = null;
			}
		}
		return state;
	}
	
	@RequestMapping(value=UserSvcApi.USER_USERNAME_AND_PASSWORD_SEARCH_PATH, method=RequestMethod.POST)
	public @ResponseBody User login(@RequestParam(UserSvcApi.USERNAME_PARAMETER) String username, @RequestParam(UserSvcApi.PASSWORD_PARAMETER) String pass){
		System.out.println("username: "+username+" pass: "+pass);
		
		return userRepo.findByUsernameAndPassword(username, pass);
	}
	
	@RequestMapping(value=UserSvcApi.USER_NUMBER_OF_TOUCHES_AND_PAGE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody List<User> findByNumberOfTouchesDescAndPage(@PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER)int pageNumber){
		return userRepo.findAll(new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "numberOfTouches"))).getContent();
	}

	@RequestMapping(value=UserSvcApi.USER_COUNTALL_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countAll() {
		return userRepo.count();
	}
}

