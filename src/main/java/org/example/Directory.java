package org.example;

import java.util.*;

public class Directory extends FileEntry {
    private Map<String, FileEntry> entries;
    private Directory parent;

    public Directory(String name) {
        super(name);
        this.entries = new LinkedHashMap<>();
        this.parent = null;
    }

    public Directory(String name, Directory parent) {
        super(name);
        this.entries = new LinkedHashMap<>();
        this.parent = parent;
    }

    public Map<String, FileEntry> getEntries() {
        return entries;
    }

    public void addEntry(FileEntry entry) {
        if (entry instanceof Directory) {
            ((Directory) entry).setParent(this);
        }
        entries.put(entry.getName(), entry);
    }

    public void removeEntry(String name) {
        entries.remove(name);
    }

    public FileEntry getEntry(String name) {
        return entries.get(name);
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}