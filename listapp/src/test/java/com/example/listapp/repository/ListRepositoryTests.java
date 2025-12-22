package com.example.listapp.repository;

import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ListRepositoryTests {
    
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByOwnerId_shouldReturnLists_WhenOwnerMatches() {
        var owner = entityManager.persist(aUser().build());
        var list1 = aList().withOwner(owner).withTitle("List 1").build();
        var list2 = aList().withOwner(owner).withTitle("List 2").build();

        entityManager.persist(list1);
        entityManager.persist(list2);
        entityManager.flush();

        var result = listRepository.findAllByOwnerId(owner.getId());

        assertEquals(2, result.size());
    }

    @Test
    void findAllByOwnerId_shouldReturnEmpty_WhenOwnerDiffers() {
        var owner1 = entityManager.persist(aUser().build());
        var owner2 = entityManager.persist(aUser().withName("Reiner").withEmail("reiner@mail.com").build());
        var list1 = aList().withOwner(owner1).withTitle("List 1").build();
        var list2 = aList().withOwner(owner2).withTitle("List 2").build();

        entityManager.persist(list1);
        entityManager.persist(list2);
        entityManager.flush();

        var result = listRepository.findAllByOwnerId(owner1.getId());

        assertEquals(1, result.size());
    }

    @Test
    void findByIdAndOwnerId_shouldReturnList_WhenOwnerMatches() {
        var owner = entityManager.persist(aUser().build());
        var list = aList().withOwner(owner).withTitle("List").build();

        entityManager.persistAndFlush(list);

        var result = listRepository.findByIdAndOwnerId(list.getId(), owner.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void findByIdAndOwnerId_shouldReturnEmpty_WhenOwnerDiffers() {
        var owner1 = entityManager.persist(aUser().build());
        var owner2 = entityManager.persist(aUser().withName("Reiner").withEmail("reiner@mail.com").build());
        var list1 = aList().withOwner(owner1).withTitle("List 1").build();
        var list2 = aList().withOwner(owner2).withTitle("List 2").build();

        entityManager.persist(list1);
        entityManager.persist(list2);
        entityManager.flush();

        var result = listRepository.findByIdAndOwnerId(list2.getId(), owner1.getId());

        assertTrue(result.isEmpty());
    }
}
