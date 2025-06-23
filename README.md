# Chat Multicanale - Progetto di Basi di Dati

Questo repository contiene l'implementazione di un'applicazione Java di chat multicanale, sviluppata come progetto per il corso di Basi di Dati. L'applicazione è basata su un'interfaccia a riga di comando (CLI) e segue fedelmente le specifiche fornite nella documentazione di progetto, con particolare attenzione alla robustezza, alla sicurezza e a un'architettura software ben definita.

---

## 🏗️ Architettura e Design Pattern

La struttura del progetto è stata progettata per essere modulare, manutenibile e scalabile, seguendo principi di software design moderni.

- **Model-View-Controller (MVC) like:**
    - **Model:** POJO (Plain Old Java Objects) che rappresentano le entità del database (`Utente`, `Progetto`, etc.).
    - **View:** Classi responsabili dell'interfaccia a riga di comando. Ogni vista ha una singola, chiara responsabilità (es. `LoginView`, `AmministrazioneView`).
    - **Controller:** Contiene la logica di business, agendo da ponte tra le Viste e i DAO.

- **Data Access Object (DAO):**
    - Separa la logica di accesso ai dati dal resto dell'applicazione.
    - Ogni DAO è responsabile per una singola entità del database.
    - Utilizza `PreparedStatement` per query dirette e `CallableStatement` per l'invocazione delle **Stored Procedure** documentate.

- **Navigation Manager:**
    - Un gestore di navigazione custom basato su uno **Stack** (`Stack<View>`).
    - Centralizza la logica di flusso dell'applicazione, gestendo le azioni `VAI_A`, `INDIETRO`, `LOGOUT` e `EXIT` in modo coerente.

- **Gestione dell'Input con `CommandException`:**
    - Un sistema robusto che previene conflitti tra i dati inseriti dall'utente e i comandi di navigazione (es. `/b`, `/q`). L'input viene intercettato e, se è un comando, viene lanciata un'eccezione custom per controllare il flusso dell'applicazione.

---

## 🔧 Tecnologie Utilizzate

- **Java 17+**
- **MySQL 8.0+**
- **JDBC (Java Database Connectivity)**
- **Maven** per la gestione delle dipendenze
- **jBCrypt** per l'hashing sicuro delle password

---

## 🚀 Setup e Installazione

Per eseguire il progetto in locale, segui questi passaggi:

**1. Prerequisiti:**
   - Aver installato **JDK 17** o superiore.
   - Aver installato **Maven**.
   - Avere un server **MySQL** in esecuzione.

**2. Database Setup:**
   - Crea un nuovo schema (database) nel tuo server MySQL (es. `mydb`).
     - Importa lo script `src/main/java/com/chatmulticanale/db/chat-multicanale_schema.sql` nel tuo database.
     Puoi farlo ad esempio con:
     ```bash
     mysql -u root -p mydb < src/main/java/com/chatmulticanale/db/chat-multicanale_schema.sql
     ```
     Lo script crea tutte le tabelle, le trigger e le stored procedure necessarie, oltre agli utenti di database con i relativi privilegi.
   - **Default admin:** al termine dell'import troverai un utente applicativo predefinito `admin` con password `superpassword123` (hashato nello script) per eseguire il primo login.

**3. Configurazione dell'Applicazione:**
   - Naviga in `src/main/resources/`.
   - Apri `config.properties` e configura i parametri di connessione JDBC (URL, username e password) in base alla tua installazione MySQL.

## 📝 Utilizzo
Una volta avviato il programma comparirà la schermata di login da terminale.
Per il primo accesso utilizza le credenziali dell'amministratore di default (`admin` / `superpassword123`).
Il menu e i comandi varieranno a seconda del ruolo con cui si effettua l'accesso.


---

## 📂 Struttura del Progetto

```plaintext
.
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── chatmulticanale/
        │           ├── Main.java               # Entry Point dell'applicazione
        │           ├── controller/             # Contiene la logica di business
        │           ├── dao/                    # Data Access Objects
        │           ├── exception/              # Eccezioni custom (es. CommandException)
        │           ├── model/                  # POJO che rappresentano le entità
        │           ├── utils/                  # Classi di utilità (DB, Input, Password...)
        │           └── view/                   # Classi per l'interfaccia utente (CLI)
        │               └── navigation/         # Componenti per il sistema di navigazione
        └── resources/
            └── config.properties               # File di configurazione del database

```
---

## 👤 Autore

**[Elia Cinti]**
