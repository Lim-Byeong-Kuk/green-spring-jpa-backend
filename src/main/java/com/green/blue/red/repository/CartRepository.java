package com.green.blue.red.repository;

import com.green.blue.red.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //이메일로 해당 유저의 카트 데이터 가져오기
    @Query("select cart from Cart cart where cart.owner.email = :email")
    public Optional<Cart> getCartOfMember(@Param("email") String email);
}
