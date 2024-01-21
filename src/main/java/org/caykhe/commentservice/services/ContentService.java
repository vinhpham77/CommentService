package org.caykhe.commentservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.commentservice.dtos.ApiException;
import org.caykhe.commentservice.dtos.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final RestTemplate restTemplate;

    @Value("${content.service.url}")
    private String contentServiceUrl;

    public void upCountCommentOfPost(String token, Integer id, Integer count) {
        String url = contentServiceUrl + "/posts/updateCommentCount?id=" + id + "&commentCount=" + count;

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", token);
            return execution.execute(request, body);
        });

        try {
            restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode.equals(UNAUTHORIZED)) { // 401
                throw new ApiException("Có lỗi trong quá trình xác thực. Vui lòng thử lại sau!", UNAUTHORIZED);
            } else if (statusCode.equals(FORBIDDEN)) { // 403
                throw new ApiException("Yêu cầu không được phép", FORBIDDEN);
            } else if (statusCode.equals(NOT_ACCEPTABLE)) { //406
                throw new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", NOT_ACCEPTABLE);
            } else if (statusCode.equals(PRECONDITION_FAILED)) { // 412
                throw new ApiException("Phiên truy cập đã hết hạn. Vui lòng đăng nhập lại!", PRECONDITION_FAILED);
            } else {
                throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void upCountCommentOfSeries(String token, Integer id, Integer count) {
        String url = contentServiceUrl + "/series/updateCommentCount?id=" + id + "&commentCount=" + count;

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", token);
            return execution.execute(request, body);
        });

        try {
            restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode.equals(UNAUTHORIZED)) { // 401
                throw new ApiException("Có lỗi trong quá trình xác thực. Vui lòng thử lại sau!", UNAUTHORIZED);
            } else if (statusCode.equals(FORBIDDEN)) { // 403
                throw new ApiException("Yêu cầu không được phép", FORBIDDEN);
            } else if (statusCode.equals(NOT_ACCEPTABLE)) { //406
                throw new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", NOT_ACCEPTABLE);
            } else if (statusCode.equals(PRECONDITION_FAILED)) { // 412
                throw new ApiException("Phiên truy cập đã hết hạn. Vui lòng đăng nhập lại!", PRECONDITION_FAILED);
            } else {
                throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", INTERNAL_SERVER_ERROR);
            }
        }
    }
}
