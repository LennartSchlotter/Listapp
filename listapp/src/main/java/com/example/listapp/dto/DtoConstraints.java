package com.example.listapp.dto;

/**
 * Constraints used for validation in DTOs.
 */
public final class DtoConstraints {

    /**
     * Static variable for the max length of the title of an object.
     */
    public static final int TITLE_MAX_LENGTH = 100;

    /**
     * Static variable for the max length of the notes of an item.
     */
    public static final int NOTES_MAX_LENGTH = 2000;

    /**
     * Static variable for the max length of the image path of an item.
     */
    public static final int IMAGE_PATH_MAX_LENGTH = 1024;

    /**
     * Static variable for the max length of the description of a list.
     */
    public static final int DESCRIPTION_MAX_LENGTH = 2000;

    /**
     * Static variable for the max length of the name of a user.
     */
    public static final int NAME_MAX_LENGTH = 64;

    /**
     * Static variable for the max length of the email of a user.
     */
    public static final int EMAIL_MAX_LENGTH = 255;

    private DtoConstraints() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}
