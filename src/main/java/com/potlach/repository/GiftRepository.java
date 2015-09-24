package com.potlach.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.potlach.client.api.GiftSvcApi;
import com.potlach.model.Gift;

@RepositoryRestResource(path = GiftSvcApi.GIFT_SVC_PATH)
public interface GiftRepository extends PagingAndSortingRepository<Gift, Long> {

	public Gift findById(@Param(value = GiftSvcApi.ID_PARAMETER) Long id);

	public Long countByChainId(@Param(value = GiftSvcApi.CHAIN_PARAMETER)Long chainId);

	public Long countByChainIdAndObscene(@Param(value = GiftSvcApi.CHAIN_PARAMETER)Long chainId, @Param(value = GiftSvcApi.OBSCENE_PARAMETER)Boolean obscene);
	
	public Long countByTitleContainingIgnoreCase(@Param(value = GiftSvcApi.TITLE_PARAMETER)String title);

	public Long countByTitleContainingIgnoreCaseAndObscene(@Param(value = GiftSvcApi.TITLE_PARAMETER)String title, @Param(value = GiftSvcApi.OBSCENE_PARAMETER) Boolean obscene);

	public Long countByCreatedById(@Param(value = GiftSvcApi.CREATEDBY_PARAMETER)Long id);

	public Long countByCreatedByIdAndObscene(@Param(value = GiftSvcApi.CREATEDBY_PARAMETER)Long id, @Param(value = GiftSvcApi.OBSCENE_PARAMETER)Boolean obscene);

	public Page<Gift> findByChainId(@Param(value = GiftSvcApi.CHAIN_PARAMETER)Long chainId, Pageable pageable);

	public Page<Gift> findByChainIdAndObscene(@Param(value = GiftSvcApi.CHAIN_PARAMETER)Long chainId, @Param(value = GiftSvcApi.OBSCENE_PARAMETER)Boolean obscene, Pageable pageable);

	public Page<Gift> findByTitleContainingIgnoreCase(@Param(value = GiftSvcApi.TITLE_PARAMETER)String title, Pageable pageable);

	public Page<Gift> findByTitleContainingIgnoreCaseAndObscene(@Param(value = GiftSvcApi.TITLE_PARAMETER)String title, @Param(value = GiftSvcApi.OBSCENE_PARAMETER) Boolean obscene, Pageable pageable);

	public Page<Gift> findByCreatedById(@Param(value = GiftSvcApi.CREATEDBY_PARAMETER)Long id, Pageable pageable);
	
	public Page<Gift> findByCreatedByIdAndObscene(@Param(value = GiftSvcApi.CREATEDBY_PARAMETER)Long id, @Param(value = GiftSvcApi.OBSCENE_PARAMETER)Boolean obscene, Pageable pageable);

	public List<Gift> findByCreatedByIdAndCreatedDateAfter(@Param(value = GiftSvcApi.CREATEDBY_PARAMETER)Long id, @Param(value = GiftSvcApi.CREATEDDATE_PARAMETER)Date createdDate);
}
