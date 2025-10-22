package com.green.blue.red.repository;

import com.green.blue.red.domain.Cart;
import com.green.blue.red.domain.CartItem;
import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.Product;
import com.green.blue.red.dto.CartItemDTO;
import com.green.blue.red.dto.CartItemListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct() {
        log.info("test1--------");
        //사용자가 전송하는 정보
        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 1;

        //만일 기존에 사용자의 장바구니 아이템이 있었다면
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        //장바구니 아이템이 없없다면 장바구니부터 확인 필요
        //사용자가 장바구니를 만든적이 있는지 확인
        Optional<Cart> cartOfMember = cartRepository.getCartOfMember(email);

        Cart cart = null;

        //사용자의 장바구니가 존재하지 않으면 장바구니 생성
        if(cartOfMember.isEmpty()) {
            log.info("MemberCart is not exist!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = cartOfMember.get();
        }

        log.info("cart : {}", cart);

        //-----------------------

        if(cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        }
        //상품 아이템 저장
        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct2() {
        log.info("testInsertByProduct start");
        String email = "user1@aaa.com";
        Long pno = 6L;
        int qty = 1;

        // 0) 회원 보장: (없으면 생성)
        Member owner = memberRepository.findById(email)
                .orElseGet(() -> {
                    Member m = Member.builder()
                            .email(email)
                            .pw("pw")
                            .nickname("user1")
                            .social(false)
                            .build();
                    return memberRepository.save(m);
                });

        // 1) 기존 CartItem 있는지 확인
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if (cartItem != null) {
            // 엔티티 메서드 이름 확인: changetQty vs changeQty (실제 이름과 맞추세요!)
            cartItem.changeQty(qty);
            cartItemRepository.saveAndFlush(cartItem);
            return;
        }

        // 2) 사용자의 Cart 확보 (없으면 생성)
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        Cart cart = result.orElseGet(() -> cartRepository.save(
                Cart.builder().owner(owner).build()
        ));

        log.info("cart: {}", cart);

        // 3) Product 확보: FK 대상이 실제 DB에 있어야 함
        Product product = productRepository.findById(pno)
                .orElseGet(() -> {
                    // 테스트용 더미 상품 생성 (필수 필드 채워주세요)
                    Product p = Product.builder()
                            .pname("테스트상품")
                            .price(10000)
                            .build();
                    p = productRepository.save(p);
                    // pno를 5로 강제해야 한다면, 미리 5번 상품을 migration/insert 해두는 게 더 안전합니다.
                    return p;
                });

        // 4) CartItem 생성/저장 (관리 엔티티 사용!)
        CartItem newItem = CartItem.builder()
                .product(product)  // 영속/관리 엔티티
                .cart(cart)        // 영속/관리 엔티티
                .qty(qty)
                .build();

        cartItemRepository.saveAndFlush(newItem);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdateByCno() {
        Long cino = 2l;
        int qty = 4;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();
        cartItem.changeQty(qty);
    }

    @Test
    public void testListOfMember() {
        String email = "user1@aaa.com";
        List<CartItemListDTO> carItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);
        for(CartItemListDTO dto : carItemList) {
            log.info("dto : {}" ,dto);

        }
    }

    @Test
    public void testListOfMember2() {
        List<String> allEmail = memberRepository.findAll().stream().map(m -> m.getEmail()).toList();
        //전체 이메일을 전달하여 데이터를 출력한 후

        List<List<CartItemListDTO>> allCartItemList = allEmail.stream()
                .map(e -> cartItemRepository.getItemsOfCartDTOByEmail(e)).toList();

        for(List<CartItemListDTO> list : allCartItemList) {
            for(CartItemListDTO dto : list) {
                System.out.println("dto = " + dto);
            }
        }

    }

    @Test
    public void testDeleteThenList() {

        Long cino = 1L;
        //장바구니 번호
        Long cno = cartItemRepository.getCartFromItem(cino);

        // 삭제는 임시 주석처리
//        cartItemRepository.deleteById(cino);

        //목록
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cino);
        for(CartItemListDTO dto : cartItemList) {
            System.out.println("dto = " + dto);
        }
    }


}
