package com.potlach.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.potlach.client.api.GiftSvcApi;
import com.potlach.client.api.UserSvcApi;
import com.potlach.model.ObsceneTouch;
@RepositoryRestResource(path = GiftSvcApi.OBSCENE_SVC_PATH)
public interface ObsceneTouchRepository extends PagingAndSortingRepository<ObsceneTouch, Long>{

	ObsceneTouch findByGiftIdAndCreatedById(@Param(value = GiftSvcApi.GIFT_ID_PARAMETER)Long giftId,@Param(value = UserSvcApi.USER_ID_PARAMETER) Long userId);

}
