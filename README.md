# 🗂️ Gerenciador de Sistema de Arquivos em Java

Repositório disponível em: https://github.com/alvaro-unifor/gerenciamento-arquivos

Este projeto simula um sistema de arquivos completo **dentro de um único arquivo** (`filesystem.dat`), oferecendo operações de criação, remoção, renomeação, cópia e listagem de arquivos e diretórios, além de um **journal** (`filesystem.journal`) que registra todas as operações mutáveis para garantir a integridade e persistência do estado.

---

## 1. 📂 Descrição do Sistema de Arquivos

Um **sistema de arquivos** é a estrutura de dados usada pelo sistema operacional para organizar, armazenar e recuperar informações em dispositivos de armazenamento. Sem ele, os dados seriam apenas um bloco contínuo de bytes, tornando impossível a localização eficiente e segura.

### 🧾 Journaling

**Journaling** é uma técnica para evitar corrupção de dados em caso de falhas (queda de energia, travamentos).  
- Antes de aplicar uma modificação real, registra-se a operação em um *log* (o journal).  
- Em caso de interrupção, o sistema “replay” esse log para restaurar um estado consistente.

---

## 2. 🧩 Estrutura de Dados e Journaling

### Estruturas de Dados

- **FileEntry (abstract):** base comum para arquivos e diretórios, armazena nome e timestamps.  
- **File:** herda de `FileEntry`, guarda conteúdo como `String`.  
- **Directory:** herda de `FileEntry`, mantém um `Map<String, FileEntry>` de filhos e referência `parent` (transient para evitar ciclos na serialização).  
- **Journal:** gerencia o arquivo `filesystem.journal`, gravando operações mutáveis com timestamp.

### Persistência no arquivo `.dat`

- A árvore de diretórios/arquivos é armazenada em `filesystem.dat` via **Java Serialization**.  
- Ao iniciar, o programa **desserializa** `filesystem.dat` (se existir) e chama `rebuildParents` para restaurar referências de pai.  
- Após cada operação mutável (mkdir, touch, rm, cp, mv, rmdir), chama-se `saveFileSystem()` para regravar o `.dat`.

---

## 3. ✅ Funcionalidades

- ✅ Listar comandos disponíveis (`help`) 
- ✅ Criar diretórios e subdiretórios (`mkdir <nome>`)  
- ✅ Criar arquivo (com ou sem conteúdo) (`touch <nome>`)
- ✅ Mostra conteúdo do arquivo (`cat <nome>`)
- ✅ Apagar arquivos (`rm <nome>`)  
- ✅ Copiar arquivos (`cp <origem> <dest>`)  
- ✅ Renomear arquivos e diretórios (`mv <origem> <novo>`)  
- ✅ Remover diretórios vazios (`rmdir <nome>`)  
- ✅ Navegar entre diretórios (`cd <dir>`, incluindo `cd ..`)  
- ✅ Listar conteúdo de diretórios (`ls`)  
- ✅ Exibir log de operações (`journal`)  

---

## 4. 🚀 Como Executar

### Pré-requisitos

- Java 8 ou superior

Para rodar o projeto basta baixa-lo em sua máquina, abrir em sua IDE de preferência e rodar a classe Main

Ao iniciar, o shell exibirá um prompt com o **caminho atual**, por exemplo:
```
Simulador de Sistema de Arquivos - Shell (digite 'help' para comandos)
/ $
```

## 🧠 Observações Técnicas

- Toda a estrutura vive **dentro de** `filesystem.dat`; **nenhum** arquivo ou diretório real é criado fora dele.  
- O journal registra apenas operações mutáveis, não listagens (`ls`).  
- Implementação baseada em **Serializable** para persistência e **transient** em `parent` para evitar referências circulares.  
- O método `rebuildParents` reconstrói as referências `parent` ao desserializar.  
- Para “resetar” o simulador, basta apagar `filesystem.dat` e `filesystem.journal`.

---

## 📌 Desenvolvedores

Desenvolvido por **Álvaro Araújo** e **Guilherme Amaral**
