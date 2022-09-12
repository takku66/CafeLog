package com.cafelog.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cafelog.entity.ApiLimitCount;
import com.cafelog.entity.ApiLimitManage;
import com.cafelog.repository.ApiLimitCountRepository;
import com.cafelog.repository.ApiLimitManageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiLimitService {
    
    private final ApiLimitManageRepository apiLimitManageRepository;
    private final ApiLimitCountRepository apiLimitCountRrepository;

    public boolean isOverLimit(int apiId){
        ApiLimitManage limitData = apiLimitManageRepository.findById(apiId);
        if(limitData.getApiId() == null){
            return true;
        }

        // TODO: limitIntervalTypeによって、開始日時・終了日時の取得方法を変更する
        int calledCount = apiLimitCountRrepository.calculateCurrentCount(apiId, 
                                                                LocalDateTime.of(2022, 9, 1, 0, 0),
                                                                LocalDateTime.of(2022, 9, 30, 0, 0));
        if(limitData.getLimitCount() < calledCount){
            return true;
        }
        
        return false;
    }

    public void count(int apiId, int userId){
        ApiLimitCount apiLimitCount = new ApiLimitCount();
        apiLimitCount.setApiId(apiId);
        apiLimitCount.setUserId(userId);
        apiLimitCountRrepository.save(apiLimitCount);
    }

}
