package com.example.jsonUtil.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class JsonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode stringToJson(String content) {
        try {
            JsonNode jsonNode = objectMapper.readTree(content);
            return jsonNode;
        } catch (Exception e) {
            log.error("Convert json fail");
            throw new RuntimeException(e);
        }
    }

    public static void compareObjectNode(
            Map<String, JsonNode> addNode, Map<String, JsonNode> removeNode, Map<String, JsonNode> changeNode,
            ObjectNode oldObjectNode, ObjectNode newObjectNode, String path) {
        Iterator<Map.Entry<String, JsonNode>> fields = oldObjectNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode oldValue = entry.getValue();
            JsonNode newValue = newObjectNode.get(fieldName);
            newObjectNode.remove(fieldName);
            String currentPath = path.isEmpty() ? fieldName : "%s.%s".formatted(path, fieldName);
            if (newValue == null) {
                removeNode.putIfAbsent(currentPath, newValue);
            } else if (oldValue == null) {
                changeNode.putIfAbsent(currentPath,newValue);
            } else if (!oldValue.equals(newValue)) {
                if (oldValue instanceof ObjectNode && newValue instanceof ObjectNode) {
                    compareObjectNode(addNode, removeNode, changeNode, (ObjectNode) oldValue, (ObjectNode) newValue, currentPath);
                } else if (oldValue instanceof ArrayNode && newValue instanceof ArrayNode) {
                    compareArrayNode(addNode, removeNode, changeNode, (ArrayNode) oldValue, (ArrayNode) newValue, currentPath);
                } else {
                    changeNode.putIfAbsent(currentPath, newValue);
                }
            }
        }
        Iterator<Map.Entry<String, JsonNode>> newfields = newObjectNode.fields();
        while (newfields.hasNext()) {
            Map.Entry<String, JsonNode> entry = newfields.next();
            String currentPath = path.isEmpty() ? entry.getKey() : "%s.%s".formatted(path, entry.getKey());
            addNode.putIfAbsent(currentPath, entry.getValue());
        }
    }

    public static void compareArrayNode(
            Map<String, JsonNode> addNode, Map<String, JsonNode> removeNode, Map<String, JsonNode> changeNode,
            ArrayNode oldArrayNode, ArrayNode newArrayNode, String path) {
        if ((oldArrayNode == null || oldArrayNode.isEmpty()) && (newArrayNode == null || newArrayNode.isEmpty())) {
            return;
        } else if (oldArrayNode == null || oldArrayNode.isEmpty()) {
            addNode.putIfAbsent(path, newArrayNode);
        } else if (newArrayNode == null || newArrayNode.isEmpty()) {
            removeNode.putIfAbsent(path, newArrayNode);
        } else if (!newArrayNode.get(0).isContainerNode() || !oldArrayNode.get(0).isContainerNode()) {
            Set<JsonNode> oldElementSet = new HashSet<>();
            oldArrayNode.forEach(oldElementSet::add);
            ArrayNode arrAdd = objectMapper.createArrayNode();
            for (JsonNode newElement : newArrayNode) {
                if (!oldElementSet.contains(newElement)) {
                    arrAdd.add(newElement);
                } else {
                    oldElementSet.remove(newElement);
                }
            }
            ArrayNode arrRemove = objectMapper.createArrayNode();
            for (JsonNode removeElement : oldElementSet) {
                arrRemove.add(removeElement);
            }
            if (!arrRemove.isEmpty()) {
                removeNode.putIfAbsent(path, arrRemove);
            }
            if (!arrAdd.isEmpty()) {
                addNode.putIfAbsent(path, arrAdd);
            }
        } else {
            // neu la array hoac object => khong check [Special case]
            if (!oldArrayNode.equals(newArrayNode)) {
                changeNode.putIfAbsent(path, newArrayNode);
            }
        }
    }

    public static String UpdateNode(ObjectNode jsonOriginData,
            Map<String, JsonNode> addNode, Map<String, JsonNode> removeNode, Map<String, JsonNode> changeNode) {
        addNode(jsonOriginData, addNode);
        removeNode(jsonOriginData, removeNode);
        changeNode(jsonOriginData, changeNode);
        return JsonToString(jsonOriginData);
    }

    public static String JsonToString(ObjectNode jsonData) {
        try {
            return objectMapper.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeNode(ObjectNode jsonOriginData, Map<String, JsonNode> changeNode) {
        for (Map.Entry<String, JsonNode> entry : changeNode.entrySet()) {
            String[] parts = entry.getKey().split("\\.");
            JsonNode value = entry.getValue();
            JsonNode current = jsonOriginData;
            for (int i=0; i < parts.length - 1; i++) {
                current = current.get(parts[i]);
            }
            if (current instanceof ObjectNode objectNode) {
                objectNode.remove(parts[parts.length-1]);
                objectNode.set(parts[parts.length-1], value);
            }
        }
    }

    public static void removeNode(ObjectNode jsonOriginData, Map<String, JsonNode> removeNode) {
        for (Map.Entry<String, JsonNode> entry : removeNode.entrySet()) {
            String[] parts = entry.getKey().split("\\.");
            JsonNode value = entry.getValue();
            JsonNode current = jsonOriginData;
            for (int i=0; i < parts.length - 1; i++) {
                current = current.get(parts[i]);
            }
            if (current instanceof ObjectNode objectNode) {
                objectNode.remove(parts[parts.length-1]);
            }
        }
    }

    public static void addNode(ObjectNode jsonOriginData, Map<String, JsonNode> addNode) {
        for (Map.Entry<String, JsonNode> entry : addNode.entrySet()) {
            String[] parts = entry.getKey().split("\\.");
            JsonNode value = entry.getValue();
            JsonNode current = jsonOriginData;
            for (int i=0; i < parts.length - 1; i++) {
                current = current.get(parts[i]);
            }
            if (current instanceof ObjectNode objectNode) {
                objectNode.set(parts[parts.length-1], value);
            }
        }
    }
}
