package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.request.ObjectRequestDto;
import com.pragma.traceability.application.dto.response.ObjectResponseDto;

import java.util.List;

public interface IObjectHandler {

    void saveObject(ObjectRequestDto objectRequestDto);

    List<ObjectResponseDto> getAllObjects();
}