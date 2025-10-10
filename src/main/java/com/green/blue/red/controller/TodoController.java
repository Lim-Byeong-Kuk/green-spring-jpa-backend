package com.green.blue.red.controller;

import com.green.blue.red.domain.Todo;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.TodoDTO;
import com.green.blue.red.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService service;

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<TodoDTO>> list(PageRequestDTO pageRequestDTO) {

        return ResponseEntity.ok(service.list(pageRequestDTO, "tno"));
    }

    @GetMapping("/read/{tno}")
    public ResponseEntity<TodoDTO> get(@PathVariable("tno") Long tno) {

        return ResponseEntity.ok(service.get(tno));
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, TodoDTO>> register(@RequestBody TodoDTO dto) {
        log.info("todo controller 추가 : dto => {}", dto);
        TodoDTO returnDTO = service.register(dto);
        return ResponseEntity.ok(Map.of("dto", returnDTO));
    }

    @PutMapping("/{tno}")
    public ResponseEntity<String> modify(@PathVariable(name = "tno") Long tno,
                                      @RequestBody TodoDTO dto
    ) {
        dto.setTno(tno);
        log.info("수정 컨트롤러 dto:{}", dto);

        service.update(dto);

        return ResponseEntity.ok("성공");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable(name = "tno") Long tno) {
        log.info("controller 삭제 tno: {}", tno);
        service.remove(tno);
        return Map.of("result", "성공");
    }


    Todo toEntity(TodoDTO t) {
        Todo te = new Todo();
        te.setComplete(t.isComplete());
//        te.setTno(t.getTno());
        te.setWriter(t.getWriter());
        te.setTitle(t.getTitle());
        te.setDueDate(t.getDueDate());
        log.info("entity:{}", te);
        return te;
    }

    TodoDTO todoDTO(Todo todo) {
        return TodoDTO.builder()
                .writer(todo.getWriter())
                .title(todo.getTitle())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate())
                .tno(todo.getTno())
                .build();
    }

//    @PostMapping("/todo/add")
//    public ResponseEntity<String> add(@RequestBody  TodoDTO d){
//        System.out.println("d:" +d);
//        r.save(toEntity(d));
//        return  ResponseEntity.ok("성공");
//    }
//    @GetMapping("/todo/list")
//    public ResponseEntity<List<TodoDTO>> list(){
//        return  ResponseEntity.ok(r.findAll().stream().map(i->todoDTO(i)).toList());
//    }
//
//    @GetMapping("/todo/read/{tno}")
//    public ResponseEntity<Todo> get(@PathVariable("tno") Long tno){
//        return  ResponseEntity.ok(r.findById(tno).get());
//    }
}
