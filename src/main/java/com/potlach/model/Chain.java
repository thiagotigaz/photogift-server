package com.potlach.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "T_CHAIN")
public class Chain extends AbstractEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(mappedBy = "chain", fetch=FetchType.LAZY)
	@JsonManagedReference
	@OrderBy("createdDate DESC")
	private List<Gift> gifts;
	@OneToOne(fetch=FetchType.EAGER)
	private Gift featuredGift;
	private Integer giftsCount=0;
	private Long followersCount=0l;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Gift> getGifts() {
		return gifts;
	}

	public void setGifts(List<Gift> gifts) {
		this.gifts = gifts;
	}

	public Gift getFeaturedGift() {
		return featuredGift;
	}

	public void setFeaturedGift(Gift featuredGift) {
		this.featuredGift = featuredGift;
	}

	public Integer getGiftsCount() {
		return giftsCount;
	}

	public void setGiftsCount(Integer giftsCount) {
		this.giftsCount = giftsCount;
	}

	public Long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Long followersCount) {
		this.followersCount = followersCount;
	}
	
}
