package com.green.blue.red.repository;

import com.green.blue.red.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p where p.pno = :pno") // jpql : jpa 에서 사용하는 쿼리
    Optional<Product> selectOne(@Param("pno") Long pno);
    // 얘가 findById 랑 다를게 뭐야??? pno 를 이용해서 1행(1 low) 를 찾는거야


    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);

    //전체 조회
    @Query("select p,pi from Product p left join p.imageList pi where pi.ord =0 and p.delFlag =false")
    Page<Object[]> selectList(Pageable pageable);

}
