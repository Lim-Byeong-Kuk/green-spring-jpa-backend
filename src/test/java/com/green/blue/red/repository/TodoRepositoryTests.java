package com.green.blue.red.repository;

import com.green.blue.red.domain.Todo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

@SpringBootTest
@Slf4j
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository r;
    boolean[] rr={true,false};
    @Test
    public void insertDummy(){
        //200개 추가
        List<Todo> list = LongStream.rangeClosed(1, 200).mapToObj(i -> new Todo(
                null,
                "제목" + i,
                "작성자" + i,
                rr[(int) (Math.random() * rr.length)],
                LocalDate.of(2020, (int)(1+(1+i)%11), (int) (1+i%27))
        )).toList();
        list.forEach(i->r.save(i));
    }
    @Test
    public void testRead(){
        //문제를 풀어요
        //findAll로 모든id로 검색을 하여 짝수인 id 홀수인 id를 분류하여
        Map<String,List<Todo>> map =new HashMap<>();
        map.put("홀수",new ArrayList<>());
        map.put("짝수",new ArrayList<>());
        //findById로 검색하여
//        {"홀수":[],"짝수":[]}
        r.findAll().forEach(i->{
                    if(i.getTno()%2==0) map.get("짝수").add(i);
                    else map.get("홀수").add(i);
            Optional<Todo> v = r.findById(i.getTno());
            System.out.println("이것은  findById를 공부하기위함 =>"+v.get());
        });
        System.out.println(map);
    }

    @Test
    public void testModify(){
        Long tno = 2l;
        Optional<Todo> result = r.findById(tno);
        Todo todo = result.orElseThrow();
        todo.changeTitle("2번 수정");
        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.of(2025,9,30));
        r.save(todo);
    }

    @Test
    public void testModify2() {
        r.findAll().forEach(todo -> {
            todo.changeTitle(todo.getTno()+todo.getTitle());
            todo.changeComplete(!todo.isComplete());
            r.save(todo);
        });
    }

    @Test
    public void testDelete() {

        r.findAll().stream()
                .filter(i -> i.getTitle().length()%2==0)
                .forEach(i -> r.deleteById(i.getTno()));
    }
    //findAll 을 이용하여 title의 문자열의 길이가 짝수인것만 골라서
    //deleteById를 호출하여 삭제


    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());
        Page<Todo> result = r.findAll(pageable);
        System.out.println(result.getTotalElements());
        result.getContent().forEach(i -> System.out.println("i = " + i));


    }

    //총 데이터의 갯수를 찾으시고
    //findAll().size) 데이터의 크기이고 size=10,20,30,40 으로 해봐서
    //page 를 데이터의 크기 만큼 반복해서 출력
    //size 10으로 page=0,1,2,3,4...findAll().size()/size
    //size 20으로 page=0,1,2,3,4...
    @Test
    public void testPaging2() {
        int totalSize = r.findAll().size();
        int pageCnt;
        int[] sizes = {10,20,30,40};
        
        for(int i=0; i<4; i++) {
            pageCnt = (int)Math.ceil(totalSize/(float)sizes[i]);
            for(int j=0; j< pageCnt; j++) {
                Pageable pageable =PageRequest.of(j,sizes[i],Sort.by("tno").descending());
                Page<Todo> todos = r.findAll(pageable);
                todos.getContent().forEach(todo -> System.out.println("todo = " + todo));
            }
        }
    }

    @Test
    public void testPaging3() {

        String[] arr = {"tno", "title", "writer"};

        for(int i=0; i<3; i++) {
            Pageable pageable = PageRequest.of(0,10,Sort.by(arr[i]).descending());
            Page<Todo> todos = r.findAll(pageable);
            todos.getContent().forEach(k-> System.out.println("todo = " + k));
        }

        for(int i=0; i<3; i++) {
            Pageable pageable = PageRequest.of(0,10,Sort.by(arr[i]).ascending());
            Page<Todo> todos = r.findAll(pageable);
            todos.getContent().forEach(j-> System.out.println("todo = " + j));
        }

    }

}
