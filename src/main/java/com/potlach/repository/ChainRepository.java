package com.potlach.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.potlach.client.api.ChainSvcApi;
import com.potlach.model.Chain;

@RepositoryRestResource(path = ChainSvcApi.CHAIN_SVC_PATH)
public interface ChainRepository extends PagingAndSortingRepository<Chain, Long>{

	List<Chain> findByNameLikeIgnoreCase(@Param(value=ChainSvcApi.NAME_PARAMETER) String name);
	
	Chain findByNameIgnoreCase(@Param(value=ChainSvcApi.NAME_PARAMETER) String name);

	Chain findById(@Param(value=ChainSvcApi.ID_PARAMETER) Long id);

	List<Chain> findByCreatedDate(Pageable pageable);

	Page<Chain> findAll(Pageable pageable);

}
