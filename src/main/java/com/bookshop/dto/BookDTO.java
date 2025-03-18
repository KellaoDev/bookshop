package com.bookshop.dto;

import java.util.Set;

public class BookDTO {

    private Long id;
    private String title;
    private String isbn;
    private String imageCover;
    private Boolean isRented = false;
    private Set<AuthorDTO> authors;
    private CategoryDTO category;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String isbn, String imageCover, Boolean isRented, Set<AuthorDTO> authors, CategoryDTO category) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.imageCover = imageCover;
        this.isRented = isRented;
        this.authors = authors;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public Boolean getRented() {
        return isRented;
    }

    public void setRented(Boolean rented) {
        isRented = rented;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}
