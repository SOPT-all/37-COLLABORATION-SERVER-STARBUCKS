package org.sopt.starbucks.global.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.sopt.starbucks.domain.mymenu.domain.PersonalOption;
import org.sopt.starbucks.global.exception.ConvertException;
import org.sopt.starbucks.global.exception.ErrorCode;

import java.io.IOException;
import java.util.List;

@Slf4j
@Converter
public class PersonalOptionConverter implements AttributeConverter<List<PersonalOption>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<PersonalOption> attribute) {
        // 객체 리스트 -> JSON String (DB 저장 시)
        try {
            if (attribute == null) return "[]";
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("[CONVERT ERROR] List -> JSON", e);
            throw new ConvertException(ErrorCode.CONVERT_ERROR);
        }
    }

    @Override
    public List<PersonalOption> convertToEntityAttribute(String dbData) {
        // JSON String -> 객체 리스트 (DB 조회 시)
        try {
            if (dbData == null || dbData.isBlank()) return List.of();
            return objectMapper.readValue(dbData, new TypeReference<List<PersonalOption>>() {});
        } catch (IOException e) {
            log.error("[CONVERT ERROR] JSON -> List", e);
            throw new ConvertException(ErrorCode.CONVERT_ERROR);
        }
    }
}
