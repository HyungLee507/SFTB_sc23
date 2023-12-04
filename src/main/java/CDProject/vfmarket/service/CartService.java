package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.itemDTO.CartItemDto;
import CDProject.vfmarket.domain.entity.Cart;
import CDProject.vfmarket.domain.entity.Item;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.exceptions.AlreadySavedItem;
import CDProject.vfmarket.exceptions.ItemNotFoundException;
import CDProject.vfmarket.exceptions.UserNotFoundException;
import CDProject.vfmarket.repository.CartRepository;
import CDProject.vfmarket.repository.ItemRepository;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    public void saveItemToCart(Long userId, Long itemId)
            throws ItemNotFoundException, UserNotFoundException, AlreadySavedItem {
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<User> user = userRepository.findById(userId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("해당 아이템을 찾을 수 없습니다.");
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        Cart cart = user.get().getShoppingBaskets();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user.get());
        }

        if (cart.getItems().stream().anyMatch(it -> it.getId().equals(itemId))) {
            throw new AlreadySavedItem("이미 저장된 상품입니다.");
        }

        cart.getItems().add(item.get());
        cartRepository.save(cart);
    }

    public void deleteItemInCart(Long itemId, Long userId) throws NoSuchObjectException {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new NoSuchObjectException("장바구니에 저장된 목록이 없습니다.");
        }
        cart.removeItem(itemId);
        cartRepository.save(cart);
    }

    public List<CartItemDto> cartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null){
            return null;
        }
        List<Item> items = cart.getItems();
        return items.stream()
                .map(item -> {
                    String firstImageFileName =
                            item.getImages().isEmpty() ? null : item.getImages().get(0).getFileName();
                    return new CartItemDto(
                            item.getId(),
                            item.getItemName(),
                            item.getPrice(),
                            item.getShoeSize(),
                            item.getCategory(),
                            item.getDescription(),
                            firstImageFileName
                    );
                })
                .collect(Collectors.toList());
    }
}
