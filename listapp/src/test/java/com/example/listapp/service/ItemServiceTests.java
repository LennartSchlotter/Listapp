package com.example.listapp.service;

import static com.example.listapp.util.entities.ItemTestBuilder.anItem;
import static com.example.listapp.util.entities.ListTestBuilder.aList;
import static com.example.listapp.util.entities.UserTestBuilder.aUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
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

import com.example.listapp.dto.item.ItemCreateDto;
import com.example.listapp.dto.item.ItemUpdateDto;
import com.example.listapp.entity.Item;
import com.example.listapp.entity.ListEntity;
import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.ResourceNotFoundException;
import com.example.listapp.mapper.ItemMapper;
import com.example.listapp.repository.ItemRepository;
import com.example.listapp.repository.ListRepository;
import com.example.listapp.security.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ItemService itemService;

    @Mock
    ItemMapper itemMapper;

    @Mock
    SecurityUtil securityUtil;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createItem_ShouldReturnUUID_WhenListContainsItem() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        when(listRepository.findById(list.getId())).thenReturn(Optional.of(list));
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();

        ItemCreateDto dto = new ItemCreateDto(item.getTitle(), item.getNotes(), item.getImagePath());
        when(itemMapper.toEntity(dto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);

        UUID result = itemService.createItem(list.getId(), dto);

        assertNotNull(result);
        verify(itemRepository).save(item);
        verify(itemMapper).toEntity(dto);
    }

    @Test
    void createItem_ShouldThrowException_WhenListDoesNotExist() {
        Item item = anItem().withId(UUID.randomUUID()).build();
        ItemCreateDto dto = new ItemCreateDto(item.getTitle(), item.getNotes(), item.getImagePath());

        assertThrows(ResourceNotFoundException.class, () -> itemService.createItem(UUID.randomUUID(), dto));
    }

    @Test
    void updateItem_ShouldReturnUUID_WhenListContainsItem() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();
        when(itemRepository.findByIdAndListId(item.getId(), list.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemUpdateDto dto = new ItemUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedNotes"), java.util.Optional.empty());

        UUID result = itemService.updateItem(list.getId(), item.getId(), dto);

        assertEquals(item.getId(), result);
        assertEquals("UpdatedTitle", item.getTitle());
        assertEquals("UpdatedNotes", item.getNotes());

        verify(itemRepository).save(item);
    }

    @Test
    void updateItemShouldCallSave_WhenEmptyDto() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();

        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();
        when(itemRepository.findByIdAndListId(item.getId(), list.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemUpdateDto dto = new ItemUpdateDto(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty());
        
        UUID result = itemService.updateItem(list.getId(), item.getId(), dto);

        assertEquals(item.getId(), result);
        verify(itemRepository).save(item);
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemDoesNotBelongToList() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list1 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        ListEntity list2 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 2").build();

        Item item = anItem().withId(UUID.randomUUID()).withList(list1).build();
        when(itemRepository.findByIdAndListId(item.getId(), list2.getId())).thenReturn(Optional.empty());

        ItemUpdateDto dto = new ItemUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedNotes"), Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.updateItem(list2.getId(), item.getId(), dto));
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemDoesNotExist() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        ItemUpdateDto dto = new ItemUpdateDto(Optional.of("UpdatedTitle"), Optional.of("UpdatedNotes"), Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.updateItem(list.getId(), UUID.randomUUID(), dto));
    }

    @Test
    void deleteItem_ShouldSoftDelete_WhenListBelongsToItem() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        Item item = anItem().withId(UUID.randomUUID()).withList(list).build();
        when(itemRepository.findByIdAndListId(item.getId(), list.getId())).thenReturn(Optional.of(item));

        itemService.deleteItem(list.getId(), item.getId());

        assertTrue(item.isDeleted());
        verify(itemRepository).save(item);
    }

    @Test
    void deleteItem_ShouldThrowException_WhenListDiffers() {
        User user = aUser().withId(UUID.randomUUID()).build();
        
        ListEntity list1 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        ListEntity list2 = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 2").build();

        Item item = anItem().withId(UUID.randomUUID()).withList(list2).build();
        when(itemRepository.findByIdAndListId(item.getId(), list1.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.deleteItem(list1.getId(), item.getId()));
    }

    @Test
    void deleteItem_ShouldThrowException_WhenItemDoesNotExist() {
        User user = aUser().withId(UUID.randomUUID()).build();
        ListEntity list = aList().withId(UUID.randomUUID()).withOwner(user).withTitle("List 1").build();
        assertThrows(ResourceNotFoundException.class, () -> itemService.deleteItem(list.getId(), UUID.randomUUID()));
    }
}
