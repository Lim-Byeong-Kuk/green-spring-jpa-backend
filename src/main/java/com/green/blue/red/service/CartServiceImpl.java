package com.green.blue.red.service;

import com.green.blue.red.domain.Cart;
import com.green.blue.red.domain.CartItem;
import com.green.blue.red.domain.Member;
import com.green.blue.red.domain.Product;
import com.green.blue.red.dto.CartItemDTO;
import com.green.blue.red.dto.CartItemListDTO;
import com.green.blue.red.repository.CartItemRepository;
import com.green.blue.red.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();
        if(cino != null) { //장바구니 아이템 번호가 있어서 수량만 변경하는 경우
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItems(email);
        }

        //장바구니 아이템 번호 cino가 없는 경우
        Cart cart = getCart(email);
        CartItem cartItem = null;

        // 이미 동일한 상품이 담긴적이 있을 수 있으므로
        cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem == null) {
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        } else {
            cartItem.changeQty(qty);
        }

        //상품 아이템 저장
        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    //사용자의 장바구니가 없었다면 새로운 장바구니를 생성하고 반환
    private Cart getCart(String email) {

        Cart cart = null;
        Optional<Cart> optionalCart = cartRepository.getCartOfMember(email);
        if(optionalCart.isEmpty()) {
            log.info("Cart of the member is not exist!!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = optionalCart.get();
        }
        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {
        Long cno = cartItemRepository.getCartFromItem(cino);
        log.info("cart no: {}", cno);

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }
}
