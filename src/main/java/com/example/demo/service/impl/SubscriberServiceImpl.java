package com.example.demo.service.impl;

import com.example.demo.advice.exception.ResourceAlreadyExistsException;
import com.example.demo.dto.request.subscriber.DefaultSubscriberRequest;
import com.example.demo.dto.response.subscriber.DefaultSubscriberResponse;
import com.example.demo.model.domain.subscriber.Skill;
import com.example.demo.model.domain.subscriber.Subscriber;
import com.example.demo.repository.SkillRepository;
import com.example.demo.repository.SubscriberRepository;
import com.example.demo.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public DefaultSubscriberResponse createSelfSubscriber(DefaultSubscriberRequest request){
        Subscriber selfSubscriber = getSelfSubscriberEntity();
        if(selfSubscriber != null){
            throw new ResourceAlreadyExistsException("Subscriber already exists by user");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        selfSubscriber = new Subscriber(email);

        if(request.getSkillIds() != null){
            List<Long> skillIds = request.getSkillIds()
                    .stream()
                    .map(DefaultSubscriberRequest.SkillId::getId)
                    .toList();

            List<Skill> skills = skillRepository.findAllById(skillIds);
            selfSubscriber.setSkills(skills);
        }

        return mapSubscriberToDefaultResponse(subscriberRepository.saveAndFlush(selfSubscriber));
    }

    @Override
    public DefaultSubscriberResponse getSelfSubscriber() {
        Subscriber subscriber = getSelfSubscriberEntity();
        if (subscriber == null) {
            return null; // or throw exception
        }
        return mapSubscriberToDefaultResponse(subscriber);
    }

    @Override
    public DefaultSubscriberResponse updateSelfSubscriber(DefaultSubscriberRequest request) {
        Subscriber subscriber = getSelfSubscriberEntity();
        if (subscriber == null) {
            throw new ResourceAlreadyExistsException("Subscriber not found");
        }

        if (request.getSkillIds() != null) {
            List<Long> skillIds = request.getSkillIds()
                    .stream()
                    .map(DefaultSubscriberRequest.SkillId::getId)
                    .toList();

            List<Skill> skills = skillRepository.findAllById(skillIds);
            subscriber.setSkills(skills);
        }

        return mapSubscriberToDefaultResponse(subscriberRepository.saveAndFlush(subscriber));
    }

    @Override
    public void deleteSelfSubscriber() {
        Subscriber subscriber = getSelfSubscriberEntity();
        if (subscriber != null) {
            subscriberRepository.delete(subscriber);
        }
    }

    private DefaultSubscriberResponse mapSubscriberToDefaultResponse(Subscriber sub){
        DefaultSubscriberResponse response = new DefaultSubscriberResponse();
        response.setId(sub.getId());
        response.setEmail(sub.getEmail());
        response.setSkills(
                sub.getSkills()
                        .stream()
                        .map(skill -> new DefaultSubscriberResponse
                                .SkillDTO(skill.getId(), skill.getName()))
                        .toList());

        return response;
    }

    private Subscriber getSelfSubscriberEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return subscriberRepository.findByEmail(email).orElse(null);
    }
}
