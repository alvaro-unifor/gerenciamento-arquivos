package org.example;

import org.example.Directory;
import org.example.FileEntry;
import org.example.Journal;

import java.io.*;
import java.util.*;

public class FileSystemSimulator {
    private Directory root;
    private Journal journal;

    public FileSystemSimulator() {
        root = new Directory("root", null);
        loadFileSystem();
        journal = new Journal();
        journal.loadJournal();
    }

    public void startShell() {
        Scanner scanner = new Scanner(System.in);
        String currentPath = getPath(root);
        Directory currentDir = root;

        System.out.println("Simulador de Sistema de Arquivos - Shell (digite 'help' para comandos)");

        while (true) {
            System.out.print(getPath(currentDir) + " $ ");
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) continue;
            String[] parts = commandLine.split(" ");
            String cmd = parts[0];

            try {
                switch (cmd) {
                    case "exit":
                        System.out.println("Saindo...");
                        return;
                    case "help":
                        printHelp();
                        break;
                    case "ls":
                        listDir(currentDir);
                        break;
                    case "mkdir":
                        if (parts.length < 2) System.out.println("Uso: mkdir <nome>");
                        else {
                            makeDir(currentDir, parts[1]);
                            journal.logOperation("mkdir " + currentPath + parts[1]);
                        }
                        break;
                    case "touch":
                        if (parts.length < 2) System.out.println("Uso: touch <nome>");
                        else {
                            createFile(currentDir, parts[1]);
                            journal.logOperation("touch " + currentPath + parts[1]);
                        }
                        break;
                    case "rm":
                        if (parts.length < 2) System.out.println("Uso: rm <nome>");
                        else {
                            removeEntry(currentDir, parts[1]);
                            journal.logOperation("rm " + currentPath + parts[1]);
                        }
                        break;
                    case "cp":
                        if (parts.length < 3) System.out.println("Uso: cp <origem> <destino>");
                        else {
                            copyFile(currentDir, parts[1], parts[2]);
                            journal.logOperation("cp " + currentPath + parts[1] + " " + parts[2]);
                        }
                        break;
                    case "mv":
                        if (parts.length < 3) System.out.println("Uso: mv <origem> <novo_nome>");
                        else {
                            renameEntry(currentDir, parts[1], parts[2]);
                            journal.logOperation("mv " + currentPath + parts[1] + " " + parts[2]);
                        }
                        break;
                    case "cd":
                        if (parts.length < 2) {
                            currentDir = root;
                            currentPath = "/";
                        } else {
                            Directory newDir = changeDirectory(currentDir, parts[1]);
                            if (newDir != null) {
                                currentDir = newDir;
                                currentPath = getPath(currentDir);
                            }
                        }
                        break;
                    case "rmdir":
                        if (parts.length < 2) System.out.println("Uso: rmdir <nome>");
                        else {
                            removeDirectory(currentDir, parts[1]);
                            journal.logOperation("rmdir " + currentPath + parts[1]);
                        }
                        break;
                    case "journal":
                        journal.printJournal();
                        break;
                    default:
                        System.out.println("Comando desconhecido. Use 'help' para ajuda.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void printHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("ls                  - lista arquivos/diretórios");
        System.out.println("mkdir <nome>        - cria diretório");
        System.out.println("touch <nome>        - cria arquivo vazio");
        System.out.println("rm <nome>           - remove arquivo");
        System.out.println("cp <origem> <dest>  - copia arquivo");
        System.out.println("mv <origem> <novo>  - renomeia arquivo/diretório");
        System.out.println("cd <dir>            - entra em diretório");
        System.out.println("rmdir <nome>        - remove diretório vazio");
        System.out.println("journal             - exibe o log de operações");
        System.out.println("exit                - sair do simulador");
    }

    private void saveFileSystem() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("filesystem.dat"))) {
            out.writeObject(root);
        } catch (IOException e) {
            System.err.println("Erro ao salvar sistema de arquivos: " + e.getMessage());
        }
    }

    private void loadFileSystem() {
        java.io.File f = new java.io.File("filesystem.dat");
        if (!f.exists()) {
            root = new Directory("root", null);
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            root = (Directory) in.readObject();
            rebuildParents(root, null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar sistema de arquivos: " + e.getMessage());
            root = new Directory("root", null);
        }
    }

    private void rebuildParents(Directory dir, Directory parent) {
        dir.setParent(parent);
        for (FileEntry entry : dir.getEntries().values()) {
            if (entry instanceof Directory) {
                rebuildParents((Directory) entry, dir);
            }
        }
    }

    private void listDir(Directory dir) {
        for (FileEntry entry : dir.getEntries().values()) {
            System.out.println((entry.isDirectory() ? "[DIR] " : "[ARQ] ") + entry.getName());
        }
    }

    private void makeDir(Directory dir, String name) {
        if (dir.getEntries().containsKey(name)) throw new RuntimeException("Já existe!");
        dir.addEntry(new Directory(name, dir));
        saveFileSystem();
        System.out.println("Diretório criado: " + name);
    }

    private void createFile(Directory dir, String name) {
        if (dir.getEntries().containsKey(name)) throw new RuntimeException("Já existe!");
        dir.addEntry(new File(name));
        saveFileSystem();
        System.out.println("Arquivo criado: " + name);
    }

    private void removeEntry(Directory dir, String name) {
        FileEntry entry = dir.getEntries().get(name);
        if (entry == null || entry.isDirectory()) throw new RuntimeException("Arquivo não encontrado ou é diretório.");
        dir.removeEntry(name);
        saveFileSystem();
        System.out.println("Arquivo removido: " + name);
    }

    private void copyFile(Directory dir, String orig, String dest) {
        FileEntry entry = dir.getEntries().get(orig);
        if (entry == null || entry.isDirectory()) throw new RuntimeException("Arquivo não encontrado ou é diretório.");
        if (dir.getEntries().containsKey(dest)) throw new RuntimeException("Destino já existe!");
        File origFile = (File) entry;
        dir.addEntry(new File(dest, origFile.getContent()));
        saveFileSystem();
        System.out.println("Arquivo copiado para: " + dest);
    }

    private void renameEntry(Directory dir, String oldName, String newName) {
        FileEntry entry = dir.getEntries().get(oldName);
        if (entry == null) throw new RuntimeException("Entrada não encontrada.");
        if (dir.getEntries().containsKey(newName)) throw new RuntimeException("Nome de destino já existe!");
        entry.setName(newName);
        dir.getEntries().remove(oldName);
        dir.addEntry(entry);
        saveFileSystem();
        System.out.println("Renomeado para: " + newName);
    }

    private void removeDirectory(Directory dir, String name) {
        FileEntry entry = dir.getEntries().get(name);
        if (entry == null || !entry.isDirectory()) throw new RuntimeException("Diretório não encontrado.");
        Directory d = (Directory) entry;
        if (!d.getEntries().isEmpty()) throw new RuntimeException("Diretório não está vazio.");
        dir.removeEntry(name);
        saveFileSystem();
        System.out.println("Diretório removido: " + name);
    }

    private Directory changeDirectory(Directory current, String name) {
        if (name.equals("..")) {
            if (current.getParent() != null) {
                return current.getParent();
            } else {
                System.out.println("Já está na raiz.");
                return current;
            }
        }
        FileEntry entry = current.getEntries().get(name);
        if (entry == null || !entry.isDirectory())
            throw new RuntimeException("Diretório não encontrado.");
        return (Directory) entry;
    }

    private String getPath(Directory dir) {
        if (dir.getParent() == null) return "/";
        List<String> names = new ArrayList<>();
        Directory current = dir;
        while (current.getParent() != null) {
            names.add(current.getName());
            current = current.getParent();
        }
        Collections.reverse(names);
        return "/" + String.join("/", names);
    }

    // Método main para rodar o simulador
    public static void main(String[] args) {
        FileSystemSimulator sim = new FileSystemSimulator();
        sim.startShell();
    }
}