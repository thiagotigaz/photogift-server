package com.potlach.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.potlach.client.api.GiftSvcApi;
import com.potlach.model.Role;

@RepositoryRestResource(path = GiftSvcApi.ROLE_SVC_PATH)
public interface RoleRepository extends PagingAndSortingRepository<Role, Long>{
	
	List<Role> findAll();
}
