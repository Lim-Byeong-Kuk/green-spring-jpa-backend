package com.green.blue.red.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList; // 해당 페이지의 todo 데이터
    private List<Integer> pageNumList; // 페이지 번호를 표시하기위한 목록
    private PageRequestDTO pageRequestDTO; //react로부터 받은 정보를 다시 response에 실어 보내야 이동할때 같이따라 다님
    //예를들어 다시 돌아왔는데 첫페이지로 가면 안된다.
    //검색하고 검색 결과에서 다시 돌아갈때 첫페이지로 가면 안되니까 page, size 정보를 기억해놔야 한다.
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    //생성자
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) totalCount;
        int end = (int)(Math.ceil(pageRequestDTO.getPage()/10.0)) * 10;
        int start = end-9;
        int last = (int) (Math.ceil((totalCount/(double)pageRequestDTO.getSize())));
        end = end > last? last: end;
        this.prev = start>1;
        this.next = totalCount > end * pageRequestDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
        if(prev) this.prevPage = start - 1;
        if(next) this.nextPage = end + 1;
        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDTO.getPage();
    }

}
