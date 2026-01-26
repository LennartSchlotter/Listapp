package com.example.listapp.service.security;

import static com.example.listapp.util.entities.ItemTestBuilder.anItem;
import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;
import com.example.listapp.repository.ItemRepository;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class ItemSecurityServiceTests {
    
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ItemSecurityService itemSecurityService;

    @Mock
    private SecurityUtil securityUtil;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void canAccessItem_ShouldReturnTrue_whenListContainsItem() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();

        boolean result = itemSecurityService.canAccessItem(list.getId(), item.getId());

        assertTrue(result);
    }

    @Test
    void canAccessItem_ShouldReturnFalse_whenItemBelongsToDifferentList() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list1 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        ListEntity list2 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 2").build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list2).build();
        when(itemRepository.findByIdAndListId(item.getId(), list1.getId())).thenReturn(null);

        boolean result = itemSecurityService.canAccessItem(list1.getId(), item.getId());

        assertFalse(result);
    }

    @Test
    void canAccessItem_ShouldReturnFalse_whenItemDoesNotExist() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);
        UUID randomID = UUID.randomUUID();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(itemRepository.findByIdAndListId(randomID, list.getId())).thenReturn(null);

        boolean result = itemSecurityService.canAccessItem(list.getId(), randomID);

        assertFalse(result);
    }

    @Test
    void canAccessItem_ShouldReturnFalse_whenUserDoesNotMatch() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user2).withTitle("List 1").build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();
        when(listRepository.findByIdAndOwnerId(list.getId(), user1.getId())).thenReturn(null);
    
        boolean result = itemSecurityService.canAccessItem(list.getId(), item.getId());

        assertFalse(result);
    }

    @Test
    void canAccessItem_ShouldThrowException_whenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> itemSecurityService.canAccessItem(item.getId(), list.getId()));
    }

    @Test
    void canAccessList_ShouldReturnTrue_whenAuthenticatedUserOwnsList() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findByIdAndOwnerId(list.getId(), user.getId())).thenReturn(Optional.of(list));

        boolean result = itemSecurityService.canAccessList(list.getId());

        assertTrue(result);
    }

    @Test
    void canAccessList_ShouldReturnFalse_whenListDoesNotExist() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        UUID randomId = UUID.randomUUID();

        when(listRepository.findByIdAndOwnerId(randomId, user.getId())).thenReturn(Optional.empty());

        boolean result = itemSecurityService.canAccessList(randomId);

        assertFalse(result);
    }

    @Test
    void canAccessList_ShouldReturnFalse_whenUserDiffers() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user2).withTitle("List 1").build();
        when(listRepository.findByIdAndOwnerId(list.getId(), user1.getId())).thenReturn(Optional.empty());

        when(listRepository.findByIdAndOwnerId(list.getId(), user1.getId())).thenReturn(Optional.empty());

        boolean result = itemSecurityService.canAccessList(list.getId());

        assertFalse(result);
    }

    @Test
    void canAccessList_ShouldThrowException_whenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> itemSecurityService.canAccessList(list.getId()));
    }
}
