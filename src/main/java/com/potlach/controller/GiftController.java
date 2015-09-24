package com.potlach.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.client.Response;

import com.amazonaws.auth.BasicAWSCredentials;
import com.potlach.client.api.ChainSvcApi;
import com.potlach.client.api.GiftSvcApi;
import com.potlach.model.Chain;
import com.potlach.model.Gift;
import com.potlach.model.LikeTouch;
import com.potlach.model.ObsceneTouch;
import com.potlach.model.User;
import com.potlach.repository.ChainRepository;
import com.potlach.repository.GiftRepository;
import com.potlach.repository.LikeTouchRepository;
import com.potlach.repository.ObsceneTouchRepository;
import com.potlach.repository.UserRepository;
import com.potlach.util.GiftFileManager;
import com.potlach.util.IS3Client;
import com.potlach.util.S3ClientAmazon;

@Controller
public class GiftController {
	@Autowired
	GiftRepository giftRepo;
	@Autowired
	ChainRepository chainRepo;
	@Autowired
	LikeTouchRepository likeRepo;
	@Autowired
	ObsceneTouchRepository obsceneRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	AuditorAware<User> auditorProvider;
	private GiftFileManager giftDataMgr;
	private IS3Client s3Client;
	
	@Transactional
	@RequestMapping(value=GiftSvcApi.GIFT_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Gift addGift(@RequestBody Gift g){
		Chain aux = chainRepo.findByNameIgnoreCase(g.getChain().getName());
		if(aux!=null)
			g.setChain(aux);
		else{
			aux = g.getChain();
			aux.setName(aux.getName().toUpperCase());
			aux = chainRepo.save(aux);
			g.setChain(aux);
		}
		
		g.setTitle(g.getTitle().toUpperCase());
		g.setDescription(g.getDescription()!=null?g.getDescription().toLowerCase():null);
		g = giftRepo.save(g);
		aux.setFeaturedGift(g);
		aux.setGiftsCount(aux.getGiftsCount()+1);
		chainRepo.save(aux);
		
		User owner = auditorProvider.getCurrentAuditor();
		owner.setNumberOfGifts(owner.getNumberOfGifts()+1);
		userRepo.save(owner);
		return g;
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody Chain setGiftData(@PathVariable(GiftSvcApi.ID_PARAMETER) long id, @RequestParam(GiftSvcApi.DATA_PARAMETER)MultipartFile giftData, HttpServletResponse response) {
		Gift v = giftRepo.findById(id);
		s3Client = new S3ClientAmazon(new BasicAWSCredentials("AKIAJHTHQYCQA5GOCR6Q", "13Ib+a4w3OftxhWqjKyV+tKQx5Z7A+cuIIhABkr9"));
		
		try {
			if (v == null)
				response.sendError(404, "Gift does not exist!");
			else {
				giftDataMgr = GiftFileManager.get();
				giftDataMgr.saveGiftData(v, giftData.getInputStream());
				s3Client.putObject("photogift", "gift"+v.getId().toString(), giftData.getInputStream());
				v.setS3Path(s3Client.getUrl("photogift", "gift"+v.getId().toString()).toString());
				giftRepo.save(v);
				return giftRepo.findById(id).getChain();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_DATA_PATH, method=RequestMethod.GET)
	public @ResponseBody Response getGiftData(@PathVariable(GiftSvcApi.ID_PARAMETER) long id, HttpServletResponse response) {
		try {
			Gift v = giftRepo.findById(id);
			if(v==null)
				response.sendError(404, "Gift does not exist!");
			else{
				giftDataMgr = GiftFileManager.get();
				giftDataMgr.copyGiftData(v, response.getOutputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_COUNTBY_CHAINID_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countByChainIdAndObscene(@PathVariable(GiftSvcApi.CHAIN_PARAMETER)Long chainId,@PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled) {
		if(obsceneEnabled)
			return giftRepo.countByChainId(chainId);
		else
			return giftRepo.countByChainIdAndObscene(chainId, false);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_COUNTBY_TITLE_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countByGiftTitleAndObscene(@PathVariable(GiftSvcApi.TITLE_PARAMETER)String title, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled) {
		if(obsceneEnabled)
			return giftRepo.countByTitleContainingIgnoreCase(title);
		else
			return giftRepo.countByTitleContainingIgnoreCaseAndObscene(title, false);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_COUNTBY_OWNER_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countByOwnerAndObscene(@PathVariable(GiftSvcApi.CREATEDBY_PARAMETER)Long userId, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled) {
		User user = auditorProvider.getCurrentAuditor();
		
		return countByUserAndObscene(user.getId(), obsceneEnabled);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_COUNTBY_USER_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countByUserAndObscene(@PathVariable(GiftSvcApi.CREATEDBY_PARAMETER)Long userId, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled) {
		if(obsceneEnabled)
			return giftRepo.countByCreatedById(userId);
		else
			return giftRepo.countByCreatedByIdAndObscene(userId, false);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_CHAINID_AND_PAGE_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Gift> findByChainIdAndPageAndObscene(@PathVariable(GiftSvcApi.CHAIN_PARAMETER)Long chainId, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled, @PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER)int pageNumber) {
		List<Gift> result;
		if(obsceneEnabled)
			result = giftRepo.findByChainId(chainId,new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		else
			result = giftRepo.findByChainIdAndObscene(chainId, false, new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		
		return prepareLikeAndObsceneFlags(result);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_TITLE_AND_PAGE_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Gift> findByTitleAndObsceneFlagAndPage(@PathVariable(GiftSvcApi.TITLE_PARAMETER)String title, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled, @PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER)int pageNumber) {
		List<Gift> result;
		if(obsceneEnabled)
			result = giftRepo.findByTitleContainingIgnoreCase(title,new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		else
			result = giftRepo.findByTitleContainingIgnoreCaseAndObscene(title, false, new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		
		return prepareLikeAndObsceneFlags(result);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_OWNER_AND_PAGE_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Gift> findByOwnerAndPage(@PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled, @PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER)int pageNumber) {
		User user = auditorProvider.getCurrentAuditor();
		
		return findByUserAndObsceneFlagAndPage(user.getId(), obsceneEnabled, pageNumber);
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_USER_AND_PAGE_AND_OBSCENE_FLAG_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Gift> findByUserAndObsceneFlagAndPage(@PathVariable(GiftSvcApi.CREATEDBY_PARAMETER)Long userId, @PathVariable(GiftSvcApi.OBSCENE_PARAMETER)Boolean obsceneEnabled, @PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER)int pageNumber) {
		List<Gift> result;
		if(obsceneEnabled)
			result = giftRepo.findByCreatedById(userId, new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		else
			result = giftRepo.findByCreatedByIdAndObscene(userId, obsceneEnabled, new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "createdDate"))).getContent();
		
		return prepareLikeAndObsceneFlags(result);
	}
	
	
	@RequestMapping(value=GiftSvcApi.GIFT_LATEST_BY_OWNER_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Gift> findLatestByOwner(){
		User user = auditorProvider.getCurrentAuditor();
		Date date = Calendar.getInstance().getTime();
		date.setDate(date.getDate()-3);
		List<Gift> result = giftRepo.findByCreatedByIdAndCreatedDateAfter(user.getId(), date);
		System.out.println("\n\nSIZE: "+result.size()+"\n\n\n");
		for(Gift g:result){
			System.out.println("\n\n"+g.getTitle());
		}
		return prepareLikeAndObsceneFlags(result);
	}
	
	
	
	private List<Gift> prepareLikeAndObsceneFlags(List<Gift> gifts){
		Iterator<Gift> i = gifts.iterator();
		while (i.hasNext()) {
			Gift gift = i.next(); 
			prepareLikeAndObsceneFlags(gift);
		}
		return gifts;
	}

	private Gift prepareLikeAndObsceneFlags(Gift gift){
			User user = auditorProvider.getCurrentAuditor();
			LikeTouch likeTouch = likeRepo.findByGiftIdAndCreatedById(
					gift.getId(), user.getId());
			if (likeTouch != null) {
				gift.setLikeTouched(true);
			}
			ObsceneTouch obsceneTouch = obsceneRepo.findByGiftIdAndCreatedById(
					gift.getId(), user.getId());
			if (obsceneTouch != null)
				gift.setObsceneTouched(true);
		return gift;
	}
	
	@Transactional
	@RequestMapping(value=GiftSvcApi.GIFT_LIKE_TOUCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Gift likeTouch(@PathVariable(GiftSvcApi.ID_PARAMETER)Long giftId, Long userId,HttpServletResponse response) {
		LikeTouch likeTouch = null;
		User user = auditorProvider.getCurrentAuditor();
		try {
			likeTouch = likeRepo.findByGiftIdAndCreatedById(giftId, user.getId());
			if (likeTouch != null) {
				response.sendError(400, "You alredy liked this Gift!");
			}else{
				likeTouch = new LikeTouch();
				Gift g = giftRepo.findById(giftId);
				g.setLikeTouches(g.getLikeTouches()+1);
				g = giftRepo.save(g);
				likeTouch.setGift(g);
				likeRepo.save(likeTouch);
				g.setLikeTouched(true);
				
				user = g.getCreatedBy();
				user.setNumberOfTouches(user.getNumberOfTouches()+1);
				userRepo.save(user);
				
				return prepareLikeAndObsceneFlags(g);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Transactional
	@RequestMapping(value=GiftSvcApi.GIFT_UNLIKE_TOUCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Gift unlikeTouch(@PathVariable(GiftSvcApi.ID_PARAMETER)Long giftId, Long userId,HttpServletResponse response) {
		LikeTouch likeTouch = null;
		User user = auditorProvider.getCurrentAuditor();
		try {
			likeTouch = likeRepo.findByGiftIdAndCreatedById(giftId, user.getId());
			if (likeTouch == null) {
				response.sendError(404, "Gift not found!");
			}else{
				likeRepo.delete(likeTouch);
				Gift g = giftRepo.findById(giftId);
				g.setLikeTouches(g.getLikeTouches()-1);
				g = giftRepo.save(g);
				g.setLikeTouched(false);
				
				user = g.getCreatedBy();
				user.setNumberOfTouches(user.getNumberOfTouches()-1);
				userRepo.save(user);
				
				return prepareLikeAndObsceneFlags(g);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_OBSCENE_TOUCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Gift obsceneTouch(@PathVariable(GiftSvcApi.ID_PARAMETER)Long giftId, Long userId,HttpServletResponse response) {
		ObsceneTouch obsceneTouch = null;
		User user = auditorProvider.getCurrentAuditor();
		try {
			obsceneTouch = obsceneRepo.findByGiftIdAndCreatedById(giftId, user.getId());
			if (obsceneTouch != null) {
				response.sendError(400, "You already marked this gift as obscene!");
			}else{
				obsceneTouch = new ObsceneTouch();
				Gift g = giftRepo.findById(giftId);
				g.setObsceneTouches(g.getObsceneTouches()+1);
				g.setObsceneTouched(true);
				g.setObscene(true);
				g = giftRepo.save(g);
				
				obsceneTouch.setGift(g);
				obsceneRepo.save(obsceneTouch);
				return prepareLikeAndObsceneFlags(g);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value=GiftSvcApi.GIFT_REVERT_OBSCENE_TOUCH_PATH, method=RequestMethod.POST)
	public @ResponseBody Gift revertObsceneTouch(@PathVariable(GiftSvcApi.ID_PARAMETER)Long giftId, Long userId,HttpServletResponse response) {
		ObsceneTouch obsceneTouch = null;
		User user = auditorProvider.getCurrentAuditor();
		try {
			obsceneTouch = obsceneRepo.findByGiftIdAndCreatedById(giftId, user.getId());
			if (obsceneTouch == null) {
				response.sendError(404, "Gift not found!");
			}else{
				obsceneRepo.delete(obsceneTouch);
				Gift g = giftRepo.findById(giftId);
				g.setObsceneTouches(g.getObsceneTouches()-1);
				g.setObsceneTouched(false);
				if(g.getObsceneTouches()==0)
					g.setObscene(false);
				g = giftRepo.save(g);
				return prepareLikeAndObsceneFlags(g);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
