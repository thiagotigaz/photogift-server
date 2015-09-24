package com.potlach.client.api;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import com.potlach.model.Chain;
import com.potlach.model.Gift;

public interface GiftSvcApi {
	
	public static final String ROLE_SVC_PATH = "/role";
	
	public static final String LIKE_SVC_PATH = "/liketouch";

	public static final String OBSCENE_SVC_PATH = "/obscenetouch";
	
	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";
	
	public static final String GIFT_ID_PARAMETER = "giftid";
	
	public static final String TITLE_PARAMETER = "title";

	public static final String OBSCENE_PARAMETER = "obscene";

	public static final String CHAIN_PARAMETER = "chainid";

	public static final String CREATEDBY_PARAMETER = "createdbyid";

	public static final String CREATEDDATE_PARAMETER = "createddate";

    public static final String PAGENUMBER_PARAMETER = "pagenumber";

	public static final String GIFT_SVC_PATH = "/gift";

	public static final String GIFT_DATA_PATH = GIFT_SVC_PATH + "/{id}/data";
	
	public static final String GIFT_LIKE_TOUCH_PATH = GIFT_SVC_PATH + "/{id}/like";

	public static final String GIFT_UNLIKE_TOUCH_PATH = GIFT_SVC_PATH + "/{id}/unlike";
	
	public static final String GIFT_OBSCENE_TOUCH_PATH = GIFT_SVC_PATH + "/{id}/obscene";

	public static final String GIFT_REVERT_OBSCENE_TOUCH_PATH = GIFT_SVC_PATH + "/{id}/revertobscene";
	
	public static final String GIFT_CHAIN_PATH = GIFT_SVC_PATH + "/{id}/chain/{chainid}";

    public static final String GIFT_CHAINID_AND_PAGE_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/chain/{chainid}/obscene/{obscene}/page/{pagenumber}";

    public static final String GIFT_COUNTBY_CHAINID_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/chain/{chainid}/obscene/{obscene}/count";

    public static final String GIFT_COUNTBY_TITLE_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/title/{title}/obscene/{obscene}/count";

    public static final String GIFT_COUNTBY_OWNER_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/owner/obscene/{obscene}/count";
    
    public static final String GIFT_COUNTBY_USER_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/user/{createdbyid}/obscene/{obscene}/count";
    
    public static final String GIFT_TITLE_AND_PAGE_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/title/{title}/obscene/{obscene}/page/{pagenumber}";
	
    public static final String GIFT_OWNER_AND_PAGE_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/owner/obscene/{obscene}/page/{pagenumber}";

    public static final String GIFT_USER_AND_PAGE_AND_OBSCENE_FLAG_PATH = GIFT_SVC_PATH + "/user/{createdbyid}/obscene/{obscene}/page/{pagenumber}";
    
    public static final String GIFT_LATEST_BY_OWNER_PATH = GIFT_SVC_PATH + "/owner/latest";

	
    @POST(GIFT_SVC_PATH)
	public Gift addGift(@Body Gift g);
	
	@Multipart
	@POST(GIFT_DATA_PATH)
	public Chain setGiftData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile giftData);
	
	@Streaming
    @GET(GIFT_DATA_PATH)
    Response getGiftData(@Path(ID_PARAMETER) long id);
	
    @GET(GIFT_TITLE_AND_PAGE_AND_OBSCENE_FLAG_PATH)
    public List<Gift> findByTitleAndObsceneFlagAndPage(@Path(TITLE_PARAMETER)String title, @Path(OBSCENE_PARAMETER)Boolean obscene, @Path(PAGENUMBER_PARAMETER)Integer page);

    @GET(GIFT_CHAINID_AND_PAGE_AND_OBSCENE_FLAG_PATH)
    public List<Gift> findByChainIdAndPageAndObscene(@Path(CHAIN_PARAMETER)Long chainId, @Path(PAGENUMBER_PARAMETER)Integer page, @Path(OBSCENE_PARAMETER)Boolean obscene);
    
    @GET(GIFT_OWNER_AND_PAGE_AND_OBSCENE_FLAG_PATH)
    public List<Gift> findByOwnerAndPageAndObscene(@Path(PAGENUMBER_PARAMETER)Integer page, @Path(OBSCENE_PARAMETER)Boolean obscene);
    
    @GET(GIFT_USER_AND_PAGE_AND_OBSCENE_FLAG_PATH)
    public List<Gift> findByUserAndPageAndObscene(@Path(CREATEDBY_PARAMETER)Long userId, @Path(OBSCENE_PARAMETER)Boolean obscene, @Path(PAGENUMBER_PARAMETER)Integer page);
    
    @GET(GIFT_LATEST_BY_OWNER_PATH)
    public List<Gift> findLatestByOwner();
    
    @GET(GIFT_COUNTBY_CHAINID_AND_OBSCENE_FLAG_PATH)
    public Long countByChainIdAndObscene(@Path(CHAIN_PARAMETER)Long chainId, @Path(OBSCENE_PARAMETER)Boolean obscene);
    
    @GET(GIFT_COUNTBY_OWNER_AND_OBSCENE_FLAG_PATH)
    public Long countByOwnerAndObscene(@Path(OBSCENE_PARAMETER)Boolean obscene);
    
    @GET(GIFT_COUNTBY_USER_AND_OBSCENE_FLAG_PATH)
    public Long countByUserAndObscene(@Path(CREATEDBY_PARAMETER)Long userId, @Path(OBSCENE_PARAMETER)Boolean obscene);

    @GET(GIFT_COUNTBY_TITLE_AND_OBSCENE_FLAG_PATH)
    public Long countByGiftTitleAndObscene(@Path(TITLE_PARAMETER)String title, @Path(OBSCENE_PARAMETER)Boolean obscene);
    
    @POST(GIFT_LIKE_TOUCH_PATH)
    public Gift likeTouch(@Path(ID_PARAMETER) Long giftId);
    
    @POST(GIFT_UNLIKE_TOUCH_PATH)
    public Gift unlikeTouch(@Path(ID_PARAMETER) Long giftId);
    
    @POST(GIFT_OBSCENE_TOUCH_PATH)
    public Gift obsceneTouch(@Path(ID_PARAMETER) Long giftId);
    
    @POST(GIFT_REVERT_OBSCENE_TOUCH_PATH)
    public Gift revertObsceneTouch(@Path(ID_PARAMETER) Long giftId);
}
