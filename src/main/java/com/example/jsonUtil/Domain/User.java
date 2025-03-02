package com.example.jsonUtil.Domain;

import com.example.jsonUtil.entity.UserEntity;
import com.example.jsonUtil.info.UserResponse;
import com.example.jsonUtil.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private String userContent;

    public static User emptyUser() {
        return User.builder().userId(0).userContent("Unknown").build();
    }

    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(this, userEntity);
        return userEntity;
    }

    public UserResponse toResponse() {
        return UserResponse.builder().userId(userId).userContent(userContent == null ? null : JsonUtil.stringToJson(userContent)).build();
    }
}
