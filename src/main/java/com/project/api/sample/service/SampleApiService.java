package com.project.api.sample.service;


import com.project.api.sample.repository.SampleApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Sample API 서비스
 */
@Service
public class SampleApiService {

    private final SampleApiRepository dao ;

    @Autowired
    public SampleApiService(SampleApiRepository dao ){
        this.dao=dao;
    }

}
