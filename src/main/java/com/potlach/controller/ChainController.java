package com.potlach.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potlach.client.api.ChainSvcApi;
import com.potlach.model.Chain;
import com.potlach.repository.ChainRepository;

@Controller
public class ChainController {

	@Autowired
	ChainRepository chainRepo;
	
	@RequestMapping(value=ChainSvcApi.CHAIN_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Chain create(@RequestBody Chain c){
		c.setName(c.getName().toUpperCase());
		Chain aux = chainRepo.findByNameIgnoreCase(c.getName());
		if(aux!=null)
			return aux;
		else
			return chainRepo.save(c);
	}
	
	@RequestMapping(value=ChainSvcApi.CHAIN_ID_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Chain getChain(@PathVariable(ChainSvcApi.ID_PARAMETER) long id) {
		return chainRepo.findById(id);
	}
	
	@RequestMapping(value=ChainSvcApi.CHAIN_PAGE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody List<Chain> findByPage(@PathVariable(ChainSvcApi.PAGENUMBER_PARAMETER) int pageNumber ) {
		return chainRepo.findAll(new PageRequest(pageNumber, 10,new Sort(Sort.Direction.DESC, "lastModifiedDate"))).getContent();
	}
	
	@RequestMapping(value=ChainSvcApi.CHAIN_COUNTALL_PATH, method=RequestMethod.GET)
	public @ResponseBody Long countAll() {
		return chainRepo.count();
	}
	
	
}
