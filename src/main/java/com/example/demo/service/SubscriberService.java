package com.example.demo.service;

import com.example.demo.dto.request.subscriber.DefaultSubscriberRequest;
import com.example.demo.dto.response.subscriber.DefaultSubscriberResponse;

public interface SubscriberService {
    DefaultSubscriberResponse createSelfSubscriber(DefaultSubscriberRequest request);
    DefaultSubscriberResponse getSelfSubscriber();
    DefaultSubscriberResponse updateSelfSubscriber(DefaultSubscriberRequest request);
    void deleteSelfSubscriber();
}
