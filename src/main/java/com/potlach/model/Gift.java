package com.potlach.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "T_GIFT")
public class Gift extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String description;
	private String s3Path;
	@ManyToOne
	@JsonBackReference
	private Chain chain;
	private Long likeTouches = 0l;
	private Long obsceneTouches = 0l;
	private Boolean obscene = false;
	@Transient
	private transient Boolean likeTouched = false;
	@Transient
	private transient Boolean obsceneTouched = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getS3Path() {
		return s3Path;
	}

	public void setS3Path(String s3Path) {
		this.s3Path = s3Path;
	}

	public Chain getChain() {
		return chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
	}

	public Long getLikeTouches() {
		return likeTouches;
	}

	public void setLikeTouches(Long likeTouches) {
		this.likeTouches = likeTouches;
	}

	public Long getObsceneTouches() {
		return obsceneTouches;
	}

	public void setObsceneTouches(Long obsceneTouches) {
		this.obsceneTouches = obsceneTouches;
	}

	public Boolean getObscene() {
		return obscene;
	}

	public void setObscene(Boolean obscene) {
		this.obscene = obscene;
	}

	public Boolean getLikeTouched() {
		return likeTouched;
	}

	public void setLikeTouched(Boolean likeTouched) {
		this.likeTouched = likeTouched;
	}

	public Boolean getObsceneTouched() {
		return obsceneTouched;
	}

	public void setObsceneTouched(Boolean obsceneTouched) {
		this.obsceneTouched = obsceneTouched;
	}

}
