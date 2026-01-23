package com.example.listapp.service.security;

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

import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;
import com.example.listapp.service.security.ListSecurityService;

@ExtendWith(MockitoExtension.class)
public class ListSecurityServiceTests {
    
    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListSecurityService listSecurityService;

    @Mock
    private SecurityUtil securityUtil;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void isOwner_ReturnsTrue_IfListBelongsToUser() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findByIdAndOwnerId(list.getId(), user.getId())).thenReturn(Optional.of(list));

        boolean result = listSecurityService.isOwner(list.getId());

        assertTrue(result);
    }

    @Test
    void isOwner_ReturnsFalse_IfListDoesNotBelongToUser() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user2).withTitle("List 1").build();
        when(listRepository.findByIdAndOwnerId(list.getId(), user1.getId())).thenReturn(Optional.empty());

        boolean result = listSecurityService.isOwner(list.getId());

        assertFalse(result);
    }

    @Test
    void isOwner_ReturnsFalse_IfListDoesNotExist() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        boolean result = listSecurityService.isOwner(UUID.randomUUID());

        assertFalse(result);
    }

    @Test
    void isOwner_ThrowsException_IfNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> listSecurityService.isOwner(list.getId()));
    }
}
