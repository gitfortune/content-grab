package com.company.contentgrab.service.impl;

import com.company.contentgrab.entity.CommonDO;
import com.company.contentgrab.repository.CommonRepository;
import com.company.contentgrab.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonRepository repository;

    @Override
    public BigInteger findUsedIdById() {
        CommonDO commonDO = repository.findById(1);
        return commonDO.getUsedId();
    }
}
