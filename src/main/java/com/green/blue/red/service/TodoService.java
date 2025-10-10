package com.green.blue.red.service;

import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.TodoDTO;

import java.util.List;

public interface TodoService {

    TodoDTO register(TodoDTO dto);
    PageResponseDTO<TodoDTO> list(PageRequestDTO dto, String id);
    TodoDTO get(Long tno);
    void update(TodoDTO dto);
    void remove(Long tno);
}
