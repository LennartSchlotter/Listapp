package com.example.listapp.repository;

import static com.example.listapp.util.entities.ItemTestBuilder.anItem;
import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByListIdOrderByPositionAsc_shouldReturnSortedItems() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).build());

           var item1 = anItem().withList(list).withPosition(2).build();
           var item2 = anItem().withList(list).withPosition(1).build();

           entityManager.persist(item1);
           entityManager.persist(item2);
           entityManager.flush();

           var result = itemRepository.findAllByListIdOrderByPositionAsc(list.getId());

           assertEquals(2, result.size());
           assertEquals(1, result.get(0).getPosition());
           assertEquals(2, result.get(1).getPosition());
    }

    @Test
    void findAllByListIdOrderByPositionAsc_shouldOnlyReturnItemsBelongingToTheList() {
           var owner = entityManager.persist(aUser().build());
           var list1 = entityManager.persist(aList().withOwner(owner).withTitle("List1").build());
           var list2 = entityManager.persist(aList().withOwner(owner).withTitle("List2").build());

           var item1 = anItem().withList(list1).withPosition(2).build();
           var item2 = anItem().withList(list1).withPosition(1).build();
           var item3 = anItem().withList(list2).withPosition(1).build();

           entityManager.persist(item1);
           entityManager.persist(item2);
           entityManager.persist(item3);
           entityManager.flush();

           var result = itemRepository.findAllByListIdOrderByPositionAsc(list1.getId());

           assertEquals(2, result.size());
    }

    @Test
    void findAllByListIdOrderByPositionAsc_shouldReturnEmpty_whenNoItemsExist() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).withTitle("List").build());

           entityManager.flush();

           var result = itemRepository.findAllByListIdOrderByPositionAsc(list.getId());

           assertTrue(result.isEmpty());
    }

    @Test
    void findByIdAndListId_shouldReturnItem_whenBothIDsMatch() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).withTitle("List").build());
           var item = anItem().withList(list).withPosition(1).build();

           entityManager.persistAndFlush(item);

           var result = itemRepository.findByIdAndListId(item.getId(), list.getId());

           assertTrue(result.isPresent());
    }

    @Test
    void findByIdAndListId_shouldReturnEmpty_whenItemExistsButListDiffers() {
           var owner = entityManager.persist(aUser().build());
           var list1 = entityManager.persist(aList().withOwner(owner).withTitle("List 1").build());
           var list2 = entityManager.persist(aList().withOwner(owner).withTitle("List 2").build());
           var item = anItem().withList(list1).withPosition(1).build();

           entityManager.persistAndFlush(item);

           var result = itemRepository.findByIdAndListId(item.getId(), list2.getId());

           assertTrue(result.isEmpty());
    }

    @Test
    void findByIdAndListId_shouldReturnEmpty_whenListMatchesButItemDiffers() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).withTitle("List").build());
           var item = anItem().withList(list).withPosition(1).build();

           entityManager.persistAndFlush(item);

           var result = itemRepository.findByIdAndListId(UUID.randomUUID(), list.getId());

           assertTrue(result.isEmpty());
    }

    @Test
    void countByListId_shouldReturnCorrectCount_forListWithItems() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).withTitle("List").build());
           var item = anItem().withList(list).withPosition(0).build();
           var item2 = anItem().withList(list).withPosition(1).build();

           entityManager.persist(item2);
           entityManager.persistAndFlush(item);

           var result = itemRepository.countByListId(list.getId());

           assertEquals(2, result);
    }

    @Test
    void countByListId_shouldReturnZero_forListWithoutItems() {
           var owner = entityManager.persist(aUser().build());
           var list = entityManager.persist(aList().withOwner(owner).withTitle("List").build());
           entityManager.flush();

           var result = itemRepository.countByListId(list.getId());

           assertEquals(0, result);
    }

    @Test
    void countByListId_doesNotCountItemsFromOtherLists() {
           var owner = entityManager.persist(aUser().build());
           var list1 = entityManager.persist(aList().withOwner(owner).withTitle("List 1").build());
           var list2 = entityManager.persist(aList().withOwner(owner).withTitle("List 2").build());
           var item = anItem().withList(list2).withPosition(0).build();

           entityManager.persistAndFlush(item);

           var result = itemRepository.countByListId(list1.getId());

           assertEquals(0, result);
    }
}
