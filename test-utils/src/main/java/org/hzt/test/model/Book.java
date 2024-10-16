package org.hzt.test.model;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {

    private static final long serialVersionUID = 1;

    private String title;
    private transient String description;
    private transient int copies;
    private final transient String category;
    private transient String language = "Java";

    public Book(final String category) {
        this("", category);
    }

    public Book(final String title, final String category) {
        this.title = title;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(final int copies) {
        this.copies = copies;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public boolean hasCopies() {
        return copies > 0;
    }

    public boolean isAboutProgramming() {
        return "Programming".equals(category);
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && getClass() == o.getClass() &&
                Objects.equals(title, ((Book) o).title) &&
                Objects.equals(category, ((Book) o).category));
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, category);
    }

    @Override
    public String toString() {
        return "Book{" +
                "((Book) o)Name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", copies=" + copies +
                ", ((Book) o)Category='" + category + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
