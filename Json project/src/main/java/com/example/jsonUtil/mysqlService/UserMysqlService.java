package com.example.jsonUtil.mysqlService;

import com.example.jsonUtil.Domain.User;
import com.example.jsonUtil.entity.UserEntity;
import com.example.jsonUtil.info.UserInfo;
import com.example.jsonUtil.repository.UserRepository;
import com.example.jsonUtil.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserMysqlService {
    private final UserRepository userRepository;

    public User insert(UserInfo userInfo) {
        return userRepository.save(userInfo.toEntity()).toDomain();
    }

    public User findById(Integer userId) {
        Optional<UserEntity> opt = userRepository.findById(userId);
        if (opt.isEmpty()) {
            return User.emptyUser();
        }
        return opt.get().toDomain();
    }

    public User update(UserInfo userInfo) {
        User userUpdate = User.emptyUser();
        try {
            User user = findById(userInfo.getUserId());
            user.setUserContent(updateDataContent(user.getUserContent(), userInfo.getUserContent()));
            userUpdate = userRepository.save(user.toEntity()).toDomain();
        } catch (Exception e) {
            log.error("Error when update: %s".formatted(e.getMessage()));
        }
        return userUpdate;
    }

    private String updateDataContent(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return newContent;
        }
        ObjectNode jsonNewData = (ObjectNode) JsonUtil.stringToJson(newContent);
        ObjectNode jsonOldData = (ObjectNode) JsonUtil.stringToJson(oldContent);
        Map<String, JsonNode> addNode = new HashMap<>();
        Map<String, JsonNode> removeNode = new HashMap<>();
        Map<String, JsonNode> changeNode = new HashMap<>();
        JsonUtil.compareObjectNode(addNode, removeNode, changeNode, jsonOldData, jsonNewData, "");
        return JsonUtil.UpdateNode(jsonOldData, addNode, removeNode, changeNode);
    }


    public boolean deleteUser(Integer userId) {
        boolean isDeleted = false;
        try {
            userRepository.deleteById(userId);
            isDeleted =true;
        } catch (Exception e) {
            log.error("Error delete User: %s".formatted(e.getMessage()));
        }
        return isDeleted;
    }

}
