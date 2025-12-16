package com.example.listapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.service.ListService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lists")
public class ListController {

    private final ListService _listService;

    public ListController(ListService listService){
        _listService = listService;
    }

    @PostMapping
    public ResponseEntity<UUID> CreateList(@Valid @RequestBody ListCreateDto dto){
        UUID createdId = _listService.createList(dto);
        return ResponseEntity.ok(createdId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListResponseDto> GetListById(@PathVariable UUID id){
        ListResponseDto response = _listService.getListById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ListResponseDto>> GetLists(){
        List<ListResponseDto> lists = _listService.getAllUserLists();
        return ResponseEntity.ok(lists);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UUID> UpdateList(@PathVariable UUID id, @Valid @RequestBody ListUpdateDto dto){
        UUID updatedId = _listService.updateList(id, dto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteList(@PathVariable UUID id){
        _listService.deleteList(id);
        return ResponseEntity.noContent().build();
    }
}
