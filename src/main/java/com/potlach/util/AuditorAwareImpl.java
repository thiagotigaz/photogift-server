package com.potlach.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.potlach.model.User;

public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public User getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	      return null;
	    }
	    
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    System.out.println("\n\n\nUSERNAME:" +userDetails.getUsername());
	    return (User) authentication.getPrincipal();
	}
}
