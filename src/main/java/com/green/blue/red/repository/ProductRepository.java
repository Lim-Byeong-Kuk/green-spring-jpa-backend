package com.green.blue.red.repository;

import com.green.blue.red.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p where p.pno = :pno") // jpql : jpa 에서 사용하는 쿼리
    Optional<Product> selectOne(@Param("pno") Long pno);
    // 얘가 findById 랑 다를게 뭐야??? pno 를 이용해서 1행(1 low) 를 찾는거야

    // custom 메서드를 왜 만드는지에 대해서 검색 결과가 나온 것같아요
    // 그런데 지금은 pno pk 야
    // 사실상 findById 쿼리는 같아요

    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);

    //전체 조회
    @Query("select p,pi from Product p left join p.imageList pi where pi.ord =0 and p.delFlag =false")
    Page<Object[]> selectList(Pageable pageable);

    // 다형성에 의해서 Object[] 안에 Product 와 ProductImage 가 들어갈 수 있는거에요
    // 무슨 의미냐? Object 는 Product 의 부모 클래스, ProductImage의 부모 클래스
    // 자바에서 모든 클래스의 최상위 클래스, 모든 클래스의 부모 클래스 : Object
}
