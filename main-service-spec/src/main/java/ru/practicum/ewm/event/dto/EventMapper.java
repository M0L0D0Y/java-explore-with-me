package ru.practicum.ewm.event.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.User;

@Service
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final CommonMethods commonMethods;

    @Autowired
    public EventMapper(CategoryMapper categoryMapper,
                       UserMapper userMapper,
                       CommonMethods commonMethods) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.commonMethods = commonMethods;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        Category category = event.getCategory();
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        eventFullDto.setCategory(categoryDto);
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        String createdOn = commonMethods.toString(event.getCreatedOn());
        eventFullDto.setCreatedOn(createdOn);
        eventFullDto.setDescription(event.getDescription());
        String eventDate = commonMethods.toString(event.getEventDate());
        eventFullDto.setEventDate(eventDate);
        User user = event.getInitiator();
        UserShortDto userShortDto = userMapper.toUserShortDto(user);
        eventFullDto.setInitiator(userShortDto);
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            String publishedOn = commonMethods.toString(event.getPublishedOn());
            eventFullDto.setPublishedOn(publishedOn);
        }
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        Category category = event.getCategory();
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        eventShortDto.setCategory(categoryDto);
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        String eventDate = commonMethods.toString(event.getEventDate());
        eventShortDto.setEventDate(eventDate);
        User user = event.getInitiator();
        UserShortDto userShortDto = userMapper.toUserShortDto(user);
        eventShortDto.setInitiator(userShortDto);
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        Category category = commonMethods.checkExistCategory(newEventDto.getCategory());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(commonMethods.toLocalDataTime(newEventDto.getEventDate()));
        event.setLocation(newEventDto.getLocation());
        insertingUnvalidatedFields(event, newEventDto);
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    private void insertingUnvalidatedFields(Event event, NewEventDto newEventDto) {
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        } else {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        } else {
            event.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }
    }
}
