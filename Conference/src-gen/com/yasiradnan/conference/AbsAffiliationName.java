package com.yasiradnan.conference;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ABS_AFFILIATION_NAME.
 */
public class AbsAffiliationName {

    private Long id;
    private String af_name;

    public AbsAffiliationName() {
    }

    public AbsAffiliationName(Long id) {
        this.id = id;
    }

    public AbsAffiliationName(Long id, String af_name) {
        this.id = id;
        this.af_name = af_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAf_name() {
        return af_name;
    }

    public void setAf_name(String af_name) {
        this.af_name = af_name;
    }

}
