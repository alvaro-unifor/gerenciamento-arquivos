package org.example;

import java.io.*;
import java.io.File;
import java.util.*;

public class Journal {
    private final String journalFile = "filesystem.journal";
    private List<String> log = new ArrayList<>();

    public void logOperation(String operation) {
        log.add(operation);
        try (FileWriter fw = new FileWriter(journalFile, true)) {
            fw.write(operation + "\n");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no journal: " + e.getMessage());
        }
    }

    public void printJournal() {
        System.out.println("== Journal de Operações ==");
        for (String line : log) {
            System.out.println(line);
        }
    }

    public void loadJournal() {
        log.clear();
        java.io.File file = new File(journalFile);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                log.add(line);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o journal: " + e.getMessage());
        }
    }
}