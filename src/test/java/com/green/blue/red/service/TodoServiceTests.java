package com.green.blue.red.service;

import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.TodoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TodoServiceTests {

    @Autowired
    private TodoService service;

//    @Test
//    public void testRegister() {
//        TodoDTO dto = TodoDTO.builder()
//                .title("서비스테스트")
//                .writer("이재오 서비스테스트")
//                .dueDate(LocalDate.of(2025,10,01))
//                .build();
//        Long tno = service.register(dto);
//        log.info("Tno:{}",tno);
//    }

//    @Test
//    public void testRegisterDummy() {
//
//        String[] names1={"김","이","박","장","홍","강","황","이","신","나","정","구","표","성","인","하","금"};
//        String[] names2={"정수","준하","지연","지혜","혜주","이나","윤하","구름","하늘","징어","가인","진희","예은"};
//        String[] strs = {"가","나","다","라","마","바","사","아","자","차","카","타","파","하"};
//
//        for(int i=0; i<100; i++) {
//
//            StringBuilder title= new StringBuilder();
//            for(int j=1; j<(int)(Math.random()*10);j++) {
//                title.append(strs[(int) (Math.random() * strs.length)]);
//            }
//
//            TodoDTO dto = TodoDTO.builder()
//                    .title(title.toString())
//                    .writer(names1[(int)(Math.random()*names1.length)]+names2[(int)(Math.random()*names2.length)])
//                    .complete(false)
//                    .dueDate(LocalDate.now())
//                    .build();
//            service.register(dto);
//        }
//    }

//    @Test
//    public void testQuiz() {
//
//        Map<String, List<Long>> map = new HashMap<>();
//        List<TodoDTO> dtos = service.list();
//        List<Long> evenList = dtos.stream()
//                .filter(i -> i.getTitle().length()%2==0)
//                .map(i -> i.getTno()).toList();
//        List<Long> oddList = dtos.stream()
//                .filter(i -> i.getTitle().length()%2==1)
//                .map(i -> i.getTno()).toList();
//
//        map.put("홀수",oddList);
//        map.put("짝수",evenList);
//        System.out.println(map);
//
//    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2)
                .size(10)
                .build();

        PageResponseDTO<TodoDTO> response = service.list(pageRequestDTO,"tno");
        log.info("response: {}",response);
    }

}
