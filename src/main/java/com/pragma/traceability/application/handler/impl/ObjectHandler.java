package com.pragma.traceability.application.handler.impl;

import com.pragma.traceability.application.dto.request.ObjectRequestDto;
import com.pragma.traceability.application.dto.response.ObjectResponseDto;
import com.pragma.traceability.application.handler.IObjectHandler;
import com.pragma.traceability.application.mapper.IObjectRequestMapper;
import com.pragma.traceability.application.mapper.IObjectResponseMapper;
import com.pragma.traceability.domain.api.IObjectServicePort;
import com.pragma.traceability.domain.model.ObjectModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectHandler implements IObjectHandler {

    private final IObjectServicePort objectServicePort;
    private final IObjectRequestMapper objectRequestMapper;
    private final IObjectResponseMapper objectResponseMapper;

    @Override
    public void saveObject(ObjectRequestDto objectRequestDto) {
        ObjectModel objectModel = objectRequestMapper.toObject(objectRequestDto);
        objectServicePort.saveObject(objectModel);
    }

    @Override
    public List<ObjectResponseDto> getAllObjects() {
        return objectResponseMapper.toResponseList(objectServicePort.getAllObjects());
    }
}