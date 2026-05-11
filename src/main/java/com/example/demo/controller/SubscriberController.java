package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.request.subscriber.DefaultSubscriberRequest;
import com.example.demo.dto.response.subscriber.DefaultSubscriberResponse;
import com.example.demo.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

/**
 * SubscriberController - Quản lý các subscriber trong hệ thống
 * Endpoints: POST, GET, PUT, DELETE /subscribers/me
 */
@RestController
@RequestMapping("/subscribers")
@RequiredArgsConstructor
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/me")
    @ApiMessage("Tạo subscriber cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('POST /subscribers/me')")
    public ResponseEntity<DefaultSubscriberResponse> createSubscriber(@RequestBody DefaultSubscriberRequest subscriberRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.createSelfSubscriber(subscriberRequest));
    }

    @GetMapping("/me")
    @ApiMessage("Lấy subscriber của người dùng hiện tại")
    @PreAuthorize("hasAuthority('GET /subscribers/me')")
    public ResponseEntity<DefaultSubscriberResponse> getSubscriber(){
        return ResponseEntity.ok(subscriberService.getSelfSubscriber());
    }

    @PutMapping("/me")
    @ApiMessage("Cập nhật subscriber của người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('PUT /subscribers/me')")
    public ResponseEntity<DefaultSubscriberResponse> updateSubscriber(@RequestBody DefaultSubscriberRequest subscriberRequest){
        return ResponseEntity.ok(subscriberService.updateSelfSubscriber(subscriberRequest));
    }

    @DeleteMapping("/me")
    @ApiMessage("Xóa subscriber của người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('DELETE /subscribers/me')")
    public ResponseEntity<Void> deleteSubscriber(){
        subscriberService.deleteSelfSubscriber();
        return ResponseEntity.ok().build();
    }
}
