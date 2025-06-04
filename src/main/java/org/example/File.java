package org.example;

public class File extends FileEntry {
    private String content;

    public File(String name) {
        super(name);
        this.content = "";
    }

    public File(String name, String content) {
        super(name);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}