package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto;
import CDProject.vfmarket.domain.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.sellerId =:sellerId"
            + " and i.status = CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE")
    List<Item> findAllBySellerId(@Param("sellerId") Long sellerId);


    @Query("select i.sellerId from Item i where i.id =:itemId")
    Long findSellerIdByItemId(@Param("itemId") Long itemId);

    @Query("select i.itemName from Item i where i.id =:itemId")
    String findItemNameByItemId(@Param("itemId") Long itemId);

//    @Query("select new CDProject.vfmarket.domain.dto.itemDTO.SalesItemDto(i.id,i.price,i.itemName,(SELECT image.fileName FROM i.images image WHERE image.item = i ORDER BY image.id ASC))"
//            + " from Item i"
//            + " left join i.images img"
//            + " where i.status = CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE"
//            + " AND i.sellerId = :sellerId")
//    List<SalesItemDto> findAllSalesItems(@Param("sellerId") Long sellerId);

    @Query("select new CDProject.vfmarket.domain.dto.itemDTO.ItemViewDto(i.id,i.itemName,i.price,i.createdDate,img.fileName,i.status)"
            + " from Item i "
            + " left join i.images img"
            + " where i.status = CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE")
    List<ItemViewDto> findAllItemListDto();

//    @Query("select new CDProject.vfmarket.domain.dto.itemDTO.CartItemDto(i.id,i.itemName,i.price,i.shoeSize,i.category,i.description,img.fileName)"
//            + " from Item i "
//            + " left join i.images img"
//            + " where i.status = CDProject.vfmarket.domain.entity.ItemStatus.FOR_SALE")
//    List<CartItemDto> findAllItemsInCart();


}
