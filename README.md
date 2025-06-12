# Chat Multicanale - Progetto di Basi di Dati

Questo repository contiene l'implementazione di un'applicazione Java di chat multicanale, sviluppata come progetto per il corso di Basi di Dati. L'applicazione è basata su un'interfaccia a riga di comando (CLI) e segue fedelmente le specifiche fornite nella documentazione di progetto, con particolare attenzione alla robustezza, alla sicurezza e a un'architettura software ben definita.

---

## ✨ Funzionalità

Il sistema è progettato per gestire le interazioni tra utenti con ruoli diversi (Amministratore, Capo Progetto, Dipendente) all'interno di canali di comunicazione legati a progetti aziendali.

### Funzionalità Implementate
- [x] **Autenticazione Sicura:** Sistema di Login e Sign Up con hashing delle password tramite **BCrypt**.
- [x] **Architettura di Navigazione Robusta:** Un sistema a Stack che gestisce il flusso tra le viste, permettendo di tornare indietro, fare logout e uscire in modo controllato.
- [x] **Input Utente a Prova di Errore:** Gestione centralizzata dell'input che previene dati vuoti e conflitti tra dati e comandi di navigazione (es. `/b` per indietro).
- [x] **Gestione Ruolo Amministratore:**
    - [x] **Promozione Utente (AM1):** L'admin può visualizzare una lista di dipendenti e promuoverli a Capo Progetto.
    - [x] **Rimozione Ruolo (AM2):** Implementata una procedura guidata sicura che forza la riassegnazione dei progetti prima del degrado del ruolo, come da regola di business RA3.
    - [x] **Assegnazione Progetti (AM3):** L'admin può assegnare progetti senza responsabile a un Capo Progetto.
    - [x] **Creazione Progetti:** Aggiunta una funzionalità di supporto per permettere all'admin di creare nuovi progetti, rendendo il sistema auto-consistente.

### Funzionalità da Implementare
- [ ] **Gestione Ruolo Capo Progetto:**
    - [ ] Creazione di canali di progetto (CP1).
    - [ ] Aggiunta e rimozione di utenti dai canali (CP2, CP3).
    - [ ] Supervisione delle chat private (CP4).
- [ ] **Gestione Ruolo Dipendente:**
    - [ ] Visualizzazione di canali e messaggi (UT2, UT6).
    - [ ] Invio di messaggi in canali e chat private (UT1, UT4).
    - [ ] Creazione di chat private (UT3).
- [ ] Implementazione completa delle operazioni rimanenti (AM4, etc.).

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
   - Esegui lo script SQL completo fornito nel repository (`il_tuo_file.sql`). Questo creerà le tabelle, i trigger, le Stored Procedure e gli utenti di database con i privilegi corretti.
   - **Importante:** Lo script inizializza il database con un utente **Amministratore** (`admin` / `superpassword123`) necessario per il primo avvio.

**3. Configurazione dell'Applicazione:**
   - Naviga in `src/main/resources/`.
   - Rinomina il file `config.properties.template` in `config.properties`.
   - Modifica il file `config.properties` con le credenziali del tuo database MySQL (URL, username e password).

**4. Esecuzione:**
   - Apri un terminale nella directory principale del progetto.
   - Compila il progetto con Maven:
     ```bash
     mvn clean install
     ```
   - Esegui l'applicazione:
     ```bash
     java -jar target/chatmulticanale-1.0-SNAPSHOT.jar 
     ```
     *(il nome del file .jar potrebbe variare, controlla nella cartella `target`)*

---

## 📂 Struttura del Progetto
.
└── src/
└── main/
├── java/
│ └── com/
│ └── chatmulticanale/
│ ├── Main.java # Entry Point dell'applicazione
│ ├── controller/ # Contiene la logica di business
│ ├── dao/ # Data Access Objects
│ ├── exception/ # Eccezioni custom (es. CommandException)
│ ├── model/ # POJO che rappresentano le entità
│ ├── utils/ # Classi di utilità (DB, Input, Password...)
│ └── view/ # Classi per l'interfaccia utente (CLI)
│ └── navigation/ # Componenti per il sistema di navigazione
└── resources/
  └── config.properties # File di configurazione del database


---

## 👤 Autore

**[Il Tuo Nome e Cognome]**
