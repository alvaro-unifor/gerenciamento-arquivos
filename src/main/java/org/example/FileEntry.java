package org.example;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class FileEntry implements Serializable {
    protected String name;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public FileEntry(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public abstract boolean isDirectory();
}
