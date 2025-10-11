package com.green.blue.red.service;

import com.green.blue.red.domain.Todo;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.TodoDTO;
import com.green.blue.red.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository repository;
    private final ModelMapper mapper;

    @Override
    public TodoDTO register(TodoDTO dto) {
        Todo entity = mapper.map(dto, Todo.class);
        Todo savedTodo = repository.save(entity);
        return mapper.map(savedTodo, TodoDTO.class);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO, String id) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(), Sort.by(id).descending());
        Page<Todo> result = repository.findAll(pageable);
        List<TodoDTO> dtoList = result.getContent().stream().map(i -> mapper.map(i, TodoDTO.class)).toList();
        long totalCount = result.getTotalElements();
        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        return responseDTO;
    }

    @Override
    public TodoDTO get(Long tno) {
        return mapper.map(repository.findById(tno), TodoDTO.class);
    }

    @Override
    @Transactional
    public void update(TodoDTO dto) {
        Todo todo = repository.findById(dto.getTno())
                .orElseThrow(() -> new NoSuchElementException("Todo Not Found : "+dto.getTno()));
        todo.changeComplete(dto.isComplete());
        todo.changeTitle(dto.getTitle());
        todo.changeDueDate(dto.getDueDate());
    }

    @Override
    public void remove(Long tno) {
        repository.deleteById(tno);
    }
}
