package backend.blog.web.service;

import backend.blog.web.repository.ItemRepository;
import backend.blog.web.domain.item.Item;
import backend.blog.web.dto.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경감지(dirty Checking)
     */
    @Transactional
    public void updateItem(Long updateId, UpdateItemDto itemDto) {
        // 영속 상태의 Item 찾아옴
        Item findItem = itemRepository.findOne(updateId);
        findItem.updateBasicInfo(itemDto);
        // @Transactional에 의해 commit 되고 jpa가 영속성 entity의 flush() 처리를 함
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
