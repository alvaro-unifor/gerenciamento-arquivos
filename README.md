# 🗂️ Simulador de Sistema de Arquivos com Journaling em Java

Repositório disponível em: [link-do-seu-repositorio]

Este projeto implementa um simulador completo de sistema de arquivos com journaling, permitindo criar, manipular e gerenciar arquivos e diretórios através de um shell interativo, com registro persistente de todas as operações.

## 1. 📂 Descrição do Sistema de Arquivos e Journaling

### Sistema de Arquivos
Um sistema de arquivos é a estrutura lógica que gerencia como os dados são armazenados e recuperados em dispositivos de armazenamento. Nosso simulador implementa:

- **Hierarquia de diretórios** (estrutura em árvore)
- **Operações básicas** (criar, mover, copiar, remover)
- **Persistência** (salvamento automático do estado)

### 🧾 Journaling Implementado
Nosso sistema utiliza **write-ahead logging** para:

- Garantir **integridade** dos dados
- Permitir **recuperação** após falhas
- Manter **histórico completo** de operações

**Funcionamento:**
1. Antes de executar a operação, registra no log
2. Executa a operação no sistema de arquivos
3. Atualiza o timestamp da operação

**Características:**
- Log estruturado em `filesystem.journal`
- Registro de todas as operações com timestamp
- Persistência entre execuções

## 2. 🏗️ Estrutura de Dados e Arquitetura

### Classes Principais
```java
org.example.
├── FileSystemSimulator.java  # Gerencia todo o sistema
├── FileEntry.java           # Classe abstrata base
├── File.java                # Arquivos com conteúdo
├── Directory.java           # Diretórios hierárquicos
└── Journal.java             # Sistema de journaling
