package com.example.listapp.service;

import static com.example.listapp.util.dtos.ListResponseDtoBuilder.aListResponseDto;
import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.listapp.dto.list.ListCreateDto;
import com.example.listapp.dto.list.ListResponseDto;
import com.example.listapp.dto.list.ListUpdateDto;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;
import com.example.listapp.exception.custom.ResourceNotFoundException;
import com.example.listapp.mapper.ListMapper;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class ListServiceTests {

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListService listService;

    @Mock
    ListMapper listMapper;

    @Mock
    SecurityUtil securityUtil;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAllUserLists_ShouldReturnDtoList_WhenUserIsAuthenticated() {
        User user = aUser().build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list1 = aList().withOwner(user).withTitle("List 1").build();
        ListEntity list2 = aList().withOwner(user).withTitle("List 2").build();
        List<ListEntity> persistedLists = List.of(list1, list2);

        when(listRepository.findAllByOwnerId(user.getId())).thenReturn(persistedLists);
        
        ListResponseDto dto1 = aListResponseDto().withId(list1.getId()).withTitle("List 1").build();
        ListResponseDto dto2 = aListResponseDto().withId(list2.getId()).withTitle("List 2").build();

        when(listMapper.toResponseDto(list1)).thenReturn(dto1);
        when(listMapper.toResponseDto(list2)).thenReturn(dto2);

        List<ListResponseDto> result = listService.getAllUserLists();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        verify(listRepository).findAllByOwnerId(user.getId());
        verify(listMapper).toResponseDto(list1);
        verify(listMapper).toResponseDto(list2);
    }

    @Test
    void getAllUserLists_ShouldReturnEmpty_WhenUserDiffers() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        aList().withOwner(user2).withTitle("List 1").build();
        aList().withOwner(user2).withTitle("List 2").build();

        when(listRepository.findAllByOwnerId(user1.getId())).thenReturn(List.of());

        List<ListResponseDto> result = listService.getAllUserLists();

        assertEquals(0, result.size());

        verify(listRepository).findAllByOwnerId(user1.getId());
        verifyNoInteractions(listMapper);
    }

    @Test
    void getAllUserLists_ShouldThrowException_WhenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        aList().withOwner(user).withTitle("List 1").build();
        aList().withOwner(user).withTitle("List 2").build();

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> listService.getAllUserLists());
    }

    @Test
    void getListById_ShouldReturnDto_WhenUserIsAuthenticated() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        
        ListResponseDto dto = aListResponseDto().withId(list.getId()).withTitle("List 1").build();
        when(listMapper.toResponseDto(list)).thenReturn(dto);

        ListResponseDto result = listService.getListById(list.getId());

        assertNotNull(result);
        verify(listRepository).findById(list.getId());
        verify(listMapper).toResponseDto(list);
    }

    @Test
    void getListById_ShouldThrowException_WhenUserDiffers() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        ListEntity list = aList().withOwner(user2).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));

        assertThrows(AccessDeniedException.class, () -> listService.getListById(list.getId()));
    }

    @Test
    void getListById_ShouldThrowException_WhenListNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(listRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> listService.getListById(nonExistentId));
        verify(listRepository).findById(nonExistentId);
    }

    @Test
    void getListById_ShouldThrowException_WhenNoAuth() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        ListEntity list = aList().withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));

        assertThrows(AccessDeniedException.class, () -> listService.getListById(list.getId()));
    }

    @Test
    void createList_ShouldReturnUUID_WhenUserIsAuthenticated() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.save(list)).thenReturn(list);
        
        ListCreateDto dto = new ListCreateDto(list.getTitle(), list.getDescription());
        when(listMapper.toEntity(dto)).thenReturn(list);

        UUID result = listService.createList(dto);

        assertNotNull(result);
        verify(listRepository).save(list);
        verify(listMapper).toEntity(dto);
    }

    @Test
    void createList_ShouldThrowException_WhenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        
        ListCreateDto dto = new ListCreateDto(list.getTitle(), list.getDescription());
        when(listMapper.toEntity(dto)).thenReturn(list);

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> listService.createList(dto));
    }

    @Test
    void updateList_ShouldReturnUUID_WhenAuthenticated() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        
        ListUpdateDto dto = new ListUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedValue"));

        UUID result = listService.updateList(list.getId(), dto);

        assertEquals(list.getId(), result);
        assertEquals("UpdatedTitle", list.getTitle());
        assertEquals("UpdatedValue", list.getDescription());

        verify(listRepository).save(list);
    }

    @Test
    void updateList_ShouldCallSave_WhenEmptyDto() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        
        ListUpdateDto dto = new ListUpdateDto(java.util.Optional.empty(), java.util.Optional.empty());

        UUID result = listService.updateList(list.getId(), dto);

        assertEquals(list.getId(), result);
        verify(listRepository).save(list);
    }

    @Test
    void updateList_ShouldThrowException_WhenUserIsNotOwner() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user2).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        
        ListUpdateDto dto = new ListUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedValue"));

        assertThrows(AccessDeniedException.class, () -> listService.updateList(list.getId(), dto));
    }

    @Test
    void updateList_ShouldThrowException_WhenListDoesNotExist() {
        ListUpdateDto dto = new ListUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedValue"));

        assertThrows(ResourceNotFoundException.class, () -> listService.updateList(UUID.randomUUID(), dto));
    }

    @Test
    void updateList_ShouldThrowException_WhenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        ListUpdateDto dto = new ListUpdateDto(Optional.of(list.getTitle()), Optional.of(list.getDescription()));

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> listService.updateList(list.getId(), dto));
    }

    @Test
    void deleteList_ShouldSoftDelete_WhenAuthenticated() {
        User user = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user);

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));

        listService.deleteList(list.getId());

        assertTrue(list.isDeleted());
        verify(listRepository).save(list);
    }

    @Test
    void deleteList_ShouldThrowException_WhenUserDiffers() {
        User user1 = aUser().withId(UUID.randomUUID()).build();
        User user2 = aUser().withId(UUID.randomUUID()).build();
        when(securityUtil.getCurrentUser()).thenReturn(user1);
        
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user2).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));

        assertThrows(AccessDeniedException.class, () -> listService.deleteList(list.getId()));
    }

    @Test
    void deleteList_ShouldThrowException_WhenListDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> listService.deleteList(UUID.randomUUID()));
    }

    @Test
    void deleteList_ShouldThrowException_WhenNoAuthentication() {
        User user = aUser().withId(UUID.randomUUID()).build();

        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));

        when(securityUtil.getCurrentUser())
            .thenThrow(new AccessDeniedException("No authenticated user found"));

        assertThrows(AccessDeniedException.class, () -> listService.deleteList(list.getId()));
    }
}
